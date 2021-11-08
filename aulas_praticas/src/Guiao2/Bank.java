package Guiao2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

class Bank implements IBank{
    
    private final int slots;
    private final Account[] av; 
    private final Lock lock;

    Bank(int slots){
        this.slots = slots;
        this.av = new Account[slots];
        for(int i = 0; i < slots; i++)
            this.av[i] = new Account(0);
        this.lock = new ReentrantLock();
    }

    public int balance(int id) {
        if (id < 0 || id >= this.slots)
            return 0;

        this.lock.lock();
        int ret = this.av[id].balance();
        this.lock.unlock();
        return ret;
    }

    public boolean deposit(int id, int value) {
        if (id < 0 || id >= this.slots)
            return false;

        this.lock.lock();
        boolean ret = this.av[id].deposit(value);
        this.lock.unlock();
        return ret;    
    }

    public boolean withdraw(int id, int value) {
        if (id < 0 || id >= this.slots)
            return false;
        
        this.lock.lock();
        boolean ret = this.av[id].withdraw(value);
        this.lock.unlock();
        return ret;
    }

    public boolean transfer(int from, int to, int value){

        if(from < 0 || from >= this.slots || to < 0 || to >= this.slots)
			return false;
        
        this.lock.lock();
        boolean ret;
        if((ret = this.av[from].withdraw(value)))
            ret = this.av[to].deposit(value);
        this.lock.unlock();
        return ret;
    }

    public int totalBalance(){
		this.lock.lock();
		int ret = Stream.of(this.av).map(Account::balance).reduce(0, (x1, x2) -> x1 + x2);
		this.lock.unlock();
		return ret;
	}   
}