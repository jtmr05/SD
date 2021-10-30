package Guiao1;

class Ex1 {
    public static void main(String[] args) {
		final int N = 10;
		Thread[] threads = new Thread[N];

		for(int i = 0; i < N; i++)
			threads[i] = new Thread(new Increment());

		for(int i = 0; i < N; i++)
			threads[i].start();

		for(int i = 0; i < N; i++)
			try {
				threads[i].join();
			} 
			catch (InterruptedException e) {
				System.out.println("uh oh");
			}
		
		System.out.println("fim");
	}
}

class Ex2{
    public static void main(String[] args) {
		final int N = 10;
		Thread[] threads = new Thread[N];
		IBank bank = new Bank();

		for(int i = 0; i < N; ++i)
			threads[i] = new Thread(new Client(bank));

		for(int i = 0; i < N; i++)
			threads[i].start();

		for(int i = 0; i < N; i++)
			try {
				threads[i].join();
			} 
			catch (InterruptedException e) {
				System.out.println("uh oh");
			}
		
        int b = bank.balance(); 
        System.out.println(b);
        System.out.println(b == 1000000);
	}
}

class Ex3{
    public static void main(String[] args) {
		final int N = 10;
		Thread[] threads = new Thread[N];
		IBank bank = new LockedBank();

		for(int i = 0; i < N; ++i)
			threads[i] = new Thread(new Client(bank));

		for(int i = 0; i < N; i++)
			threads[i].start();

		for(int i = 0; i < N; i++)
			try {
				threads[i].join();
			} 
			catch (InterruptedException e) {
				System.out.println("uh oh");
			}
		
        int b = bank.balance(); 
		System.out.println(b);
		System.out.println(b == 1000000);
	}
}
