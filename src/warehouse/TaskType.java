package warehouse;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by joaorodrigues on 14 Dec 14.
 */
public class TaskType {
<<<<<<< HEAD
    private static int nextId = 0;
    private static HashMap<Integer, Integer> taskIndex = new HashMap<>();
    private static ReentrantLock indexLock = new ReentrantLock();

    private final int id = ++nextId;
    private final String name;

    private Map<Integer, Task> running;
    private Map<String, Integer> needs;

    private ReentrantLock runningLock;
    private ReentrantLock mainLock;

    //Constructors

    public TaskType(String na, Map<String, Integer> ne) {
        name = na;
        running = new HashMap<>();
        needs = new HashMap<>(ne);
        runningLock = new ReentrantLock();
        mainLock = new ReentrantLock();
    }


    public void startTask(int userId){
        Task t = new Task(userId, name);

        indexLock.lock();
        taskIndex.put(t.getId(), this.id);
        indexLock.unlock();

        runningLock.lock();
        running.put( t.getId(), t);
        runningLock.unlock();
    }


    public void endTask( int taskId ){
        runningLock.lock();
        // TODO: notifySubscribers
        running.remove(taskId);
        runningLock.unlock();
    }

    public static int getTypeOfTask(int taskId) throws InexistentTaskException {
        Integer typeId;
        indexLock.lock();
        try{
            typeId = taskIndex.get(taskId);

            if(typeId == null)
                throw new InexistentTaskException("User referenced task with id: " + taskId + " but was not found");
        }
        finally{
            indexLock.unlock();
        }
        return typeId;
    }

    public void lock(){
        mainLock.lock();
    }

    public void unlock(){
        mainLock.unlock();
    }

    public String getRunningString(){
        StringBuilder result = new StringBuilder();
        result.append(id + " -- " + name + ":\n");
        for(Task t: running.values()){
            result.append("--- " + t.getId() + ": " +t.getClientId() + '\n');
        }
        return result.toString();
=======
    private static int idCount = 0;
    private int id;
    private String name;
    private Set<Integer> running;
    private Map<String, Integer> needs;
    private ReentrantLock lock;

    //Constructors

    public TaskType() {
        id = ++idCount;
        name = "";
        running = new HashSet<>();
        needs = new HashMap<>();
        lock = new ReentrantLock();
    }

    public TaskType(String na, Map<String, Integer> ne) {
        id = ++idCount;
        name = na;
        running = new HashSet<Integer>();
        needs = new HashMap<>(ne);
        lock = new ReentrantLock();
    }

    public void addTask(int id){
        running.add(id);
>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence
    }


    //Getters & Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Map<String, Integer> getNeeds() {
<<<<<<< HEAD
        HashMap<String, Integer> result = new HashMap<String, Integer>(needs);
        return result;
=======
        return new HashMap<String, Integer>(needs);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence
    }

    public void setNeeds(Map<String, Integer> ne) {
        needs = new HashMap<>(ne);
    }

<<<<<<< HEAD
    public Map<Integer, Task> getRunning() {
        return new HashMap<Integer, Task>(running);
    }

    public void setRunning(Map<Integer, Task> running) {
        this.running = new HashMap<Integer, Task>(running);
    }

=======
    public Set<Integer> getRunning() {
        return new HashSet<Integer>(running);
    }

    public void setRunning(Set<Integer> running) {
        this.running = new HashSet<Integer>(running);
    }
>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence
}
