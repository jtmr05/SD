package Guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReentrantBarrier implements IBarrier {

    private final int threshold;
    private Lock lock;
    private Condition cond;
    private int counter;
    private int round;

    ReentrantBarrier (int N){
        this.threshold = N;
        this.lock = new ReentrantLock();
        this.cond = this.lock.newCondition();
        this.counter = this.round = 0;
    }

    public void await() throws InterruptedException {
        this.lock.lock();

        this.counter++;
        int r = this.round;

        if(this.counter < this.threshold)
            while(r == this.round)
                this.cond.await();
        else{
            this.round++;
            this.counter = 0;
            this.cond.signalAll();
        }

        this.lock.unlock();
    }   
}