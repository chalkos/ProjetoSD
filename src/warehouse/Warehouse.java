package warehouse;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Warehouse {
    private Map<String, TaskType> taskTypes;
    private Map<String, Item> stock;
    private ReentrantLock stockLock;
    private ReentrantLock taskTypesLock;
    Condition turn;
    PriorityQueue<Long> removeQueue;
    long nextTicket;

    private static final int REM_QUEUE_LIMIT = 4;

    public Warehouse() {
        taskTypes = new HashMap<>();
        stock = new HashMap<>();
        stockLock = new ReentrantLock();
        taskTypesLock = new ReentrantLock();
        this.turn = stockLock.newCondition();
        this.removeQueue = new PriorityQueue<Long>();
        this.nextTicket = 1;
    }

    // TODO: can that lock mixup cause a deadlock?
    public void stockUp(String itemName, int quantity) throws InvalidItemQuantityException {
        stockLock.lock();
        Item i = stock.get(itemName);
        stockLock.unlock();

        if(i == null)
            i = new Item(itemName);

        i.lock();
        i.add(quantity);

        stockLock.lock();
        stock.put(itemName, i);

        i.unlock();
        stockLock.unlock();
    }

    public void newTaskType(String name, Map<String, Integer> items) throws ExistentTaskException {
        TaskType newTaskType = new TaskType(name, items);

        taskTypesLock.lock();
        try {
            if (taskTypes.containsKey(name)) {
                taskTypesLock.unlock();
                throw new ExistentTaskException();
            }

            taskTypes.put(name, newTaskType);
        }
        finally {
            taskTypesLock.unlock();
        }

    }

    public int startTask(String typeName) throws InexistentTaskTypeException, InexistentItemException {
        TaskType type;

        requestMaterial(type.getNeeds());
        taskTypesLock.lock();
        int taskId;
        try {
            type = taskTypes.get(typeName);

            if (type == null)
                throw new InexistentTaskTypeException("User referenced task type with name: " + typeName + " but was not found");

            type.lock();
            taskId = type.startTask();
            type.unlock();
        }
        finally {
            taskTypesLock.unlock();
        }

        return taskId;
    }

    public void endTask(int id) throws InexistentTaskTypeException, InexistentItemException {

        String typeName = TaskType.getTypeOfTask(id);

        taskTypesLock.lock();

        TaskType type = taskTypes.get(typeName);

        type.endTask(id);

        taskTypesLock.unlock();

        try {
            returnMaterial(type.getNeeds());
        } catch (InvalidItemQuantityException e) {} // Since we are returning a value that is already valid, the exception never occurs
    }

    //Get list of tasks currently being done
    public Map<String, Collection<Integer>> getRunningTasks() {

        Map<String, Collection<Integer>> result = new HashMap<>();

        taskTypesLock.lock();

        for( TaskType type : taskTypes.values())
            result.put(type.getName(), type.getRunningIDs());

        taskTypesLock.unlock();

        return result;

    }

    public Task getTask(int id) throws InexistentTaskTypeException, InexistentTaskException {
        String typeName = TaskType.getTypeOfTask(id);

        taskTypesLock.lock();
        TaskType type = taskTypes.get(typeName);

        type.lock();
        taskTypesLock.unlock();

        Task result = type.getTask(id);

        type.unlock();
        return result;
    }

    private void requestMaterial(Map<String, Integer> material) throws InexistentItemException, InterruptedException {
        stockLock.lock();

        long myTurn = nextTicket++;
        Iterator<Map.Entry<String, Integer>> it = material.entrySet().iterator();

        while( it.hasNext() ) {
            Map.Entry<String, Integer> pair = it.next();
            Item i = stock.get(pair.getKey());
            boolean waited = false;

            while( !i.isAvailable( pair.getValue() ) ) {
                stockLock.unlock();
                i.waitForMore();
                stockLock.lock();
                waited = true;
                it = material.entrySet().iterator();        // rewinding the iterator
            }

            if(waited)
                continue;

            Long next = removeQueue.peek();

            while(next != null && myTurn > next.longValue() + REM_QUEUE_LIMIT) { // peek returns null if the queue is empty
                removeQueue.add(myTurn);
                this.turn.await();
                removeQueue.remove(myTurn);
                next = removeQueue.peek();
                it = material.entrySet().iterator();        // rewinding the iterator
            }
        }

        for(Map.Entry<String, Integer> pair : material.entrySet()) {
            try {
                stock.get( pair.getKey() ).remove( pair.getValue() );
            } catch (InvalidItemQuantityException e) {} // never occurs because the task validates its needs upon creation. we always remove valid values
        }

        this.turn.signalAll();
        stockLock.unlock();
    }

    private void returnMaterial(Map<String, Integer> material) throws InexistentItemException, InvalidItemQuantityException {
        stockLock.lock();

        try {
            for (Map.Entry<String, Integer> pair : material.entrySet()) {
                Item i = stock.get(pair.getKey());

                if (i == null)
                    throw new InexistentItemException("User returned " + pair.getKey());

                i.add(pair.getValue());
            }
        }finally {
            stockLock.unlock();
        }
    }
}
