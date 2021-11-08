package Guiao4;

class Runner implements Runnable {
    
    private IBarrier barrier;

    Runner(IBarrier b){
        this.barrier = b;
    }

    public void run(){
        String name = Thread.currentThread().getName();

        System.out.println("Thread " +name+ " calling barrier.await()");

        try{
            this.barrier.await();
        }
        catch(InterruptedException e){}
        System.out.println("Thread " +name+ " woke up");
    }
}
