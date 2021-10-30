package Guiao2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LockedAccount {

    private int balance;
    private Lock lock;

    LockedAccount(int balance) { 
        this.balance = balance; 
        this.lock = new ReentrantLock();
    }

    public void lock(){
        this.lock.lock();
    }

    public void unlock(){
        this.lock.unlock();
    }
    
    public int balance() { 
        return this.balance;
    }

    public boolean deposit(int value) {
      	this.balance += value;
      	return true;
    }

    public boolean withdraw(int value){

        if(value > this.balance)
            return false;
        
        this.balance -= value;
        return true;
    }
}