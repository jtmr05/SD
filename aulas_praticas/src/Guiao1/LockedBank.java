package Guiao1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LockedBank implements IBank {

    private Account savings;
    private Lock lock;

	LockedBank(){
		this.savings = new Account(0);
        this.lock = new ReentrantLock();
	}

	public int balance() {
        this.lock.lock();
        int ret = this.savings.balance();
		this.lock.unlock();
        return ret;
	}

	public boolean deposit(int value) {
        this.lock.lock();
        boolean ret = this.savings.deposit(value);
		this.lock.unlock();
		return ret;
	}
}
