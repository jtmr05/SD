package guiao2;

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

    public boolean withdraw(int value){

        if(value > this.balance)
            return false;

        this.balance -= value;
        return true;
    }
}