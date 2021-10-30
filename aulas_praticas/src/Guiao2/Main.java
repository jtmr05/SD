package Guiao2;

class Ex2 {
    public static void main(String[] args) {
		final int N=10;

		IBank b = new Bank(N);

		for (int i=0; i<N; i++) 
			b.deposit(i,1000);

		System.out.println(b.totalBalance());

		Thread t1 = new Thread(new Mover(b,10)); 
		Thread t2 = new Thread(new Mover(b,10));

		t1.start(); t2.start(); 
		try{
			t1.join(); t2.join();
		}
		catch(InterruptedException e){
			System.out.println("uh oh");
		}

		System.out.println(b.totalBalance());
    }
}

class Ex3 {
    public final static void main(String[] args) {
		final int N=10;

		IBank b = new BankLockedAccount(N);

		for (int i=0; i<N; i++) 
			b.deposit(i,1000);

		System.out.println(b.totalBalance());

		Thread t1 = new Thread(new Mover(b,10)); 
		Thread t2 = new Thread(new Mover(b,10));
		Thread t3 = new Thread(new Mover(b,10));


		t1.start(); t2.start(); t3.start(); 
		try{
			t1.join(); t2.join(); t3.join();
		}
		catch(InterruptedException e){
			System.out.println("uh oh");
		}

		System.out.println(b.totalBalance());
    }
}
