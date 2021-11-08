package Guiao3;

import java.util.Random;
import java.util.function.Supplier;

class Runner implements Runnable {

	private final IBank b;
	private int num_of_accounts;
	private int delta;
	private static final int CASES = 5;
	private static final int MAX_VALUE = 2501;

	Runner(IBank b, int n) { 
		this.b = b;
		this.num_of_accounts = n;
		this.delta = 0;
	}

	public void run() {
		
		Random rand = new Random();
		
		Supplier<Integer> gen_id = () -> {
			return rand.nextInt(this.num_of_accounts);
		};

		Supplier<Integer> gen_value = () -> {
			return rand.nextInt(MAX_VALUE);
		};


		switch(rand.nextInt(CASES)){
			case 0 -> {
				int value = gen_value.get();
				this.delta += value;
				this.b.createAccount(value);
				this.num_of_accounts++;
			} 

			case 1 ->
				this.delta -= this.b.closeAccount(gen_id.get());

			case 2 -> {
				int value = gen_value.get();
				if(this.b.deposit(gen_id.get(), value))
					this.delta += value;
			}
			
			case 3 -> {
				int value = gen_value.get();
				if(this.b.withdraw(gen_id.get(), value))
					this.delta -= value;
			}

			case 4 ->
				this.b.transfer(gen_id.get(), gen_id.get(), gen_value.get());
		}
	}

	public int get_delta(){
		return this.delta;
	}
}