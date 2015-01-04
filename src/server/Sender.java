package server;

import packet.Packet;
import packet.Subscribe;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Sender implements Runnable{
    private Queue<Packet> objects;
    private ReentrantLock objectsLock;
    private Condition hasObjects;
    private ObjectOutputStream out;

    private String user;

    private IOException exception;
    private ReentrantLock exceptionLock;

    Sender(ObjectOutputStream o){
        out = o;

        objects = new LinkedList<>();
        objectsLock = new ReentrantLock();
        hasObjects = objectsLock.newCondition();

        exception = null;
        exceptionLock = new ReentrantLock();
    }

    public void setUser(String s) {
        this.user = s;
    }

    private ObjectOutputStream getUserOutputStream() {
        Server.userLock.lock();
        User u = Server.users.get(this.user);
        ObjectOutputStream o = u.getOutputStream();
        Server.userLock.unlock();
        return o;
    }

    public void send(Packet p) throws IOException{
        // is stream ok?
        exceptionLock.lock();
        if( exception != null ) {
            exceptionLock.unlock();
            throw exception;
        }
        exceptionLock.unlock();

        objectsLock.lock();
        objects.add(p);
        hasObjects.signalAll();
        objectsLock.unlock();
    }

    @Override
    public void run() {
        Boolean streamOK = true;

        while (streamOK) {
            objectsLock.lock();
            while (objects.isEmpty())
                try {
                    hasObjects.await();
                } catch (InterruptedException e) {}

            try {
                Packet obj = objects.remove();
                objectsLock.unlock();

                if (obj instanceof Subscribe) {
                    ObjectOutputStream userOut = getUserOutputStream();
                    userOut.writeObject(obj);
                    userOut.flush();
                } else {
                    out.writeObject(obj);
                    out.flush();
                }

            } catch (IOException e) {
                streamOK = false;
                exceptionLock.lock();
                exception = e;
                exceptionLock.unlock();
            }
        }

    }
}





