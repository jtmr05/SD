package guiao1;

class Bank implements IBank {

	private final Account savings;

	Bank(){
		this.savings = new Account(0);
	}

	public int balance() {
		return this.savings.balance();
	}

	public boolean deposit(int value) {
		return this.savings.deposit(value);
	}
}
