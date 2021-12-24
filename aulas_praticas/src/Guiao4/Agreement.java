package guiao4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Agreement {

    private final int threshold;
    private final Lock lock;
    private final Condition cond;
    private int max_so_far;
    private int agreed_value;
    private int counter;
    private int round;

    Agreement (int N) {
        this.threshold = N;
        this.lock = new ReentrantLock();
        this.cond = this.lock.newCondition();
        this.counter = this.round = 0;
        this.max_so_far = Integer.MIN_VALUE;
        this.agreed_value = 0;
    }

    public int propose(int choice) throws InterruptedException {
        this.lock.lock();

        this.max_so_far = Math.max(this.max_so_far, choice);
        this.counter++;
        int r = this.round;

        if(this.counter < this.threshold)
            while(r == this.round)
                this.cond.await();
        else{
            this.agreed_value = this.max_so_far;
            this.counter = 0;
            this.round++;
            this.cond.signalAll();
            this.max_so_far = Integer.MIN_VALUE;
        }

        this.lock.unlock();
        return this.agreed_value;
    }
}
