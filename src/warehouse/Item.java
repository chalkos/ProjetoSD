package warehouse;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by frmendes on 12/14/14.
 */
public class Item {
    String name;
    int quantity;
    ReentrantLock lock;
    Condition available;

    public Item(String name) {
        this.name = name;
        this.quantity = 0;
        this.lock = new ReentrantLock();
        this.available = this.lock.newCondition();
    }

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.lock = new ReentrantLock();
        this.available = lock.newCondition();
    }

    // wakes up all threads waiting for this item
    public void signalAll(){
        this.available.signalAll();
    }

    // go to sleep until we have this item
    public void await() throws InterruptedException {
        this.available.await();
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }

    public void add(int quantity) {
        this.lock();
        this.quantity += quantity;
        this.unlock();
        this.signalAll();
    }

    public void remove(int quantity) throws InterruptedException {
        this.lock();
        while(quantity > this.quantity)
            this.await();

        this.quantity -= quantity;
        this.unlock();
    }

    public int getQuantity() {
        this.lock();
        int qnt = this.quantity;
        this.unlock();
        return qnt;
    }
}
