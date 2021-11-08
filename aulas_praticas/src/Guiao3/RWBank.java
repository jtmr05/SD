package Guiao3;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class RWBank implements IBank{

    private Map<Integer, Account> map;
    private int nextId;
	private ReadWriteLock lock;
	
	RWBank(){
		this.map = new HashMap<Integer, Account>();
		this.nextId = 0;
		this.lock = new ReentrantReadWriteLock();
	}

	private void readLock(){
		this.lock.readLock().lock();
	}

	private void readUnlock(){
		this.lock.readLock().unlock();
	}

	private void writeLock(){
		this.lock.writeLock().lock();
	}

	private void writeUnlock(){
		this.lock.writeLock().unlock();
	}

    // create account and return account id
    public int createAccount(int balance) {
		this.writeLock();
		Account c = new Account(balance);
		int id = this.nextId;
		this.nextId++;
		this.map.put(id, c);
		this.writeUnlock();
		return id;
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
		this.writeLock();
        Account c = this.map.remove(id);
		if(c != null){
			c.lock();
			this.writeUnlock();
			int b = c.balance();
			c.unlock();
			return b;
		}
		else{
			this.writeUnlock();
			return 0;
		}
    }

    // account balance; 0 if no such account
    public int balance(int id) {
		this.readLock();
        Account c = this.map.get(id);
		if(c != null){
			c.lock();
			this.readUnlock();
			int b = c.balance();
			c.unlock();
			return b;
		}
		else{
			this.readUnlock();
			return 0;
		}
	}

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        this.readLock();
		Account c = this.map.get(id);
		if(c != null){
			c.lock();
			this.readUnlock();
			boolean b = c.deposit(value);
			c.unlock();
			return b;
		}
		else{
			this.readUnlock();
			return false;
		}
	}

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        this.readLock();
		Account c = this.map.get(id);
		if(c != null){
			c.lock();
			this.readUnlock();
			boolean b = c.withdraw(value);
			c.unlock();
			return b;
		}
		else{
			this.readUnlock();
			return false;
		}
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
		this.readLock();
        
		Account c_from = this.map.get(from), c_to = this.map.get(to);
		boolean ret = false;

		if(c_from != null && c_to != null){
			c_from.lock();
			this.readUnlock();
			ret = c_from.withdraw(value);

			if(ret){
				c_to.lock();
				c_from.unlock();
				ret = c_to.deposit(value);
				c_to.unlock();
			}
			else
				c_from.unlock();
		}
		else
			this.readUnlock();
		
		return ret;
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
		
		List<Account> lc = new ArrayList<>(ids.length);

		this.readLock();
        for (int i : ids) {
            Account c = this.map.get(i);
            if (c != null){
                c.lock();
				lc.add(c);
			}
		}
		this.readUnlock();

		int total = 0;
		for(Account c : lc){
			total += c.balance();
			c.unlock();
		}

        return total;
	}

	public void debug(){
		this.map.values().stream().forEach(x -> System.out.print(x.balance()+" "));

		int total = this.map.values().stream().mapToInt(Account::balance).sum();

		System.out.println("\n-> " + total);
	}
}