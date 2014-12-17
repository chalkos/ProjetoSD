package warehouse;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by joaorodrigues on 17 Dec 14.
 */
public class Task {
<<<<<<< HEAD
    private static int nextId = 0;
    private final int id = ++nextId;
    private final int clientId;
    private Set<Integer> subscribers;
    private final String typeName;
=======
    private static int idCount = 0;
    private int id;
    private int clientId;
    private Set<Integer> subscribers;
    private String typeName;
>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence
    private ReentrantLock lock;


    public Task(int client, String type){
<<<<<<< HEAD
=======
        id = ++idCount;
>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence
        clientId = client;
        subscribers = new HashSet<>();
        typeName = type;
        lock = new ReentrantLock();
    }


    public void notifySubscribers(){
<<<<<<< HEAD
            //TODO : tratar do user e das subs, depois voltar a isto
    }


=======
        lock.lock();
        for( int sub : subscribers){
            //TODO : tratar do user e das subs, depois voltar a isto
        }
        lock.unlock();
    }

    public void stop(){

    }

>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence




    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        lock.lock();
        result.append(typeName);
        result.append(" task (ID: ");
        result.append(id);
        result.append(" ran by client ");
        result.append(clientId);
        lock.unlock();
        
        return result.toString();
    }



    public void setSubscribers(Set<Integer> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<Integer> getSubscribers() {
        return subscribers;
    }

<<<<<<< HEAD
=======
    public static void setIdCount(int idCount) {
        Task.idCount = idCount;
    }
>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence

    public int getId() {
        return id;
    }

<<<<<<< HEAD
    public int getClientId() {
        return clientId;
    }


=======
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public static int getIdCount() {
        return idCount;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }


>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence
    public String getTypeName() {
        return typeName;
    }

<<<<<<< HEAD
=======
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
>>>>>>> Created TaskType and Task, supporting Task instances. Several changes in consequence
}
