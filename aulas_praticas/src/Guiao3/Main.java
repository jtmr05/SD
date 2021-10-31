package Guiao3;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

class Common{

    private static final int NUM_OF_ACCOUNTS = 8;
    private static final int NUM_OF_THREADS = 8;

    public static void common(IBank bank){
        int expected = 0;

        Random random = new Random();
        Supplier<Integer> sup_of_int = () -> {
            return random.nextInt(10) * 100;
        };

		for(int i = 0; i < NUM_OF_ACCOUNTS; i++){
            int value = sup_of_int.get();
			expected += value;
            bank.createAccount(value);
        }
		
		Thread[] threads = new Thread[NUM_OF_THREADS];
		Runnable[] runners = new Runner[NUM_OF_THREADS]; //we'll need to keep track of these...
               
		for(int i = 0; i < NUM_OF_THREADS; i++){
            runners[i] = new Runner(bank, NUM_OF_ACCOUNTS);
			threads[i] = new Thread(runners[i]);
        }

        long start = System.currentTimeMillis();

		for(Thread t : threads)
			t.start();
		
		for(Thread t : threads)
			try{
				t.join();
			}
			catch(InterruptedException e){
                System.out.println("uh oh");
            }
        
        long end = System.currentTimeMillis();
        
        expected += Arrays.stream(runners).map(Runner.class::cast).
                                           mapToInt(Runner::get_delta).
                                           sum();

        System.out.println("Expected:\n-> " + expected + "\nGot: ");
		bank.debug();
        System.out.println((end-start) + " milliseconds");
    }
}

class Ex2{
	public static void main(String[] args) {
		IBank b = new Bank();
        Common.common(b);
    }
}

class Ex3{
	public static void main(String[] args) {
		IBank b = new RWBank();
        Common.common(b);
    }
}