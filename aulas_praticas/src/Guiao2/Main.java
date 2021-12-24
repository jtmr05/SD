package guiao2;

class Common{

	public static void common(IBank bank){
		final int N=10;

		for (int i=0; i<N; i++)
			bank.deposit(i,1000);

		System.out.println(bank.totalBalance());

		Thread t1 = new Thread(new Mover(bank,10));
		Thread t2 = new Thread(new Mover(bank,10));

		t1.start(); t2.start();
		try{
			t1.join(); t2.join();
		}
		catch(InterruptedException e){
			System.out.println("uh oh");
		}

		System.out.println(bank.totalBalance());
	}
}
class Ex2 {
    public static void main(String[] args) {

		final int N=10;
		IBank b = new Bank(N);
		Common.common(b);
    }
}

class Ex3 {
    public static void main(String[] args) {

		final int N=10;
		IBank b = new BankLockedAccount(N);
		Common.common(b);
    }
}
