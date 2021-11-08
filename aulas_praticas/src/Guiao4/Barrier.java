package Guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Barrier implements IBarrier {
 
    private final int num_of_threads;
    private Lock lock;
    private Condition cond;
    private int counter;

    Barrier (int N){
        this.num_of_threads = N;
        this.lock = new ReentrantLock();
        this.cond = this.lock.newCondition();
        this.counter = 0;
    }
    
    public void await() throws InterruptedException {
        this.lock.lock();
        
        this.counter++;
        if(this.counter < this.num_of_threads)
            while(this.counter < this.num_of_threads)
                this.cond.await();
        else
            this.cond.signalAll(); //forall n >= num_of_threads, nth thread will signalAll()
                                   //if-clause prevents this
        
        this.lock.unlock();
    }
}