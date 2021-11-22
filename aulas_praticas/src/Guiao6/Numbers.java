package Guiao6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Numbers {

    private int sum;
    private int count;
    private final Lock lock;

    Numbers(){
        this.sum = this.count = 0;
        this.lock = new ReentrantLock();
    }

    /** add one by one */
    public void add(String s){
        this.lock.lock();
        try{
            this.sum += Integer.parseInt(s);
            this.count++;
        }
        catch(NumberFormatException e){}
        this.lock.unlock();
    }

    /** add once, at the end */
    public void add(int sum, int count){
        this.lock.lock();
        this.sum += sum;
        this.count += count;
        this.lock.unlock();
    }

    public double get_average(){
        this.lock.lock();
        double ret = (count==0) ? 0 : ((double) this.sum / this.count);
        this.lock.unlock();
        return ret;
    }
}
