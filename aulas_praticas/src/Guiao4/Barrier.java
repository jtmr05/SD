package guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Barrier implements IBarrier {

    private final int threshold;
    private final Lock lock;
    private final Condition cond;
    private int counter;

    Barrier (int N){
        this.threshold = N;
        this.lock = new ReentrantLock();
        this.cond = this.lock.newCondition();
        this.counter = 0;
    }

    public void await() throws InterruptedException {
        this.lock.lock();

        this.counter++;
        if(this.counter < this.threshold)
            while(this.counter < this.threshold)
                this.cond.await();
        else
            this.cond.signalAll(); //forall n >= threshold, nth thread would signalAll()
                                   //if-clause prevents this

        this.lock.unlock();
    }
}