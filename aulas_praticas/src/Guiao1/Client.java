package guiao1;

class Client implements Runnable {

    private final IBank bank;

	Client(IBank b){
		this.bank = b;
	}

	public void run(){
		final int I = 1000, V = 100;

		for(int i = 0; i < I; i++)
			this.bank.deposit(V);
	}
}
