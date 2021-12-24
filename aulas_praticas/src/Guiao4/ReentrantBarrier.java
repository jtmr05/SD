package guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReentrantBarrier implements IBarrier {

    private final int threshold;
    private final Lock lock;
    private final Condition cond;
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
            this.counter = 0;
            this.round++;
            this.cond.signalAll();
        }

        this.lock.unlock();
    }
}