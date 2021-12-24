package guiao1;

class Increment implements Runnable {

	public void run() {
		final long N=100;

		for (long i = 0; i < N; i++)
			System.out.println(i);
	}
}
