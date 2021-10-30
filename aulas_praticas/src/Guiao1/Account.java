package Guiao1;

class Account {

    private int balance;

    Account(int balance) { 
        this.balance = balance; 
    }

    public int balance() { 
        return this.balance;
    }

    public boolean deposit(int value) {
      	this.balance += value;
      	return true;
    }
}