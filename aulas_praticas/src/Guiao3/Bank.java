package Guiao3;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank implements IBank{

    private Map<Integer, Account> map;
    private int nextId;
	private Lock lock;
	
	Bank(){
		this.map = new HashMap<Integer, Account>();
		this.nextId = 0;
		this.lock = new ReentrantLock();
	}

    // create account and return account id
    public int createAccount(int balance) {
		this.lock.lock();
		Account c = new Account(balance);
		int id = this.nextId;
		this.nextId++;
		this.map.put(id, c);
		this.lock.unlock();
		return id;
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
		this.lock.lock();
        Account c = this.map.remove(id);
		if(c != null){
			c.lock();
			this.lock.unlock();
			int b = c.balance();
			c.unlock();
			return b;
		}
		else{
			this.lock.unlock();
			return 0;
		}
    }

    // account balance; 0 if no such account
    public int balance(int id) {
		this.lock.lock();
        Account c = this.map.get(id);
		if(c != null){
			c.lock();
			this.lock.unlock();
			int b = c.balance();
			c.unlock();
			return b;
		}
		else{
			this.lock.unlock();
			return 0;
		}
	}

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        this.lock.lock();
		Account c = this.map.get(id);
		if(c != null){
			c.lock();
			this.lock.unlock();
			boolean b = c.deposit(value);
			c.unlock();
			return b;
		}
		else{
			this.lock.unlock();
			return false;
		}
	}

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        this.lock.lock();
		Account c = this.map.get(id);
		if(c != null){
			c.lock();
			this.lock.unlock();
			boolean b = c.withdraw(value);
			c.unlock();
			return b;
		}
		else{
			this.lock.unlock();
			return false;
		}
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
		this.lock.lock();
        
		Account c_from = this.map.get(from), c_to = this.map.get(to);
		boolean ret = false;

		if(c_from != null && c_to != null){
			c_from.lock();
			this.lock.unlock();
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
			this.lock.unlock();
		
		return ret;
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
		
		List<Account> lc = new ArrayList<>(ids.length);

		this.lock.lock();
        for (int i : ids) {
            Account c = this.map.get(i);
            if (c != null){
                c.lock();
				lc.add(c);
			}
		}
		this.lock.unlock();

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