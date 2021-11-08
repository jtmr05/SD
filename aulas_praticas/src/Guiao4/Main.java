package Guiao4;

class Common {

    public static void common(IBarrier barrier, int NUM_OF_THREADS) {
        
        Thread[] threads = new Thread[NUM_OF_THREADS];
        
        for(int i = 0; i < NUM_OF_THREADS; i++)
            threads[i] = new Thread(new Runner(barrier), (i+1)+""); //name each thread

        for(Thread t : threads)
            t.start();
        
        for(Thread t : threads)
            try{
                t.join();
            }
            catch(InterruptedException e){
                System.out.println("uh oh");
            }
    }
}

class Ex1{

    private static final int NUM_OF_THREADS = 10;
    public static void main(String[] args) {

        IBarrier barrier = new Barrier(NUM_OF_THREADS);
        Common.common(barrier, NUM_OF_THREADS);
    }
}

class Ex2{

    private static final int NUM_OF_THREADS = 20;
    private static final int THRESHOLD = 10;

    public static void main(String[] args) {

        IBarrier barrier = new ReentrantBarrier(THRESHOLD);
        Common.common(barrier, NUM_OF_THREADS);
    }
}

class Ex3{

    private static final int NUM_OF_THREADS = 20;
    private static final int THRESHOLD = 5;
    
    public static void main(String[] args) {
        
        Agreement agreement = new Agreement(THRESHOLD);

        Thread[] threads = new Thread[NUM_OF_THREADS];
        
        for(int i = 0; i < NUM_OF_THREADS; i++)
            threads[i] = new Thread(new AgreeRunner(agreement), (i+1)+"");

        for(Thread t : threads)
            t.start();
        
        for(Thread t : threads)
            try{
                t.join();
            }
            catch(InterruptedException e){
                System.out.println("uh oh");
            }
    }
}