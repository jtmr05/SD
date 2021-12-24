package guiao4;

import java.util.Random;
import java.util.function.Supplier;

class AgreeRunner implements Runnable {

    private final Agreement agreement;

    AgreeRunner(Agreement a){
        this.agreement = a;
    }

    public void run(){
        String name = Thread.currentThread().getName();

        System.out.println("Thread " +name+ " calling agreement.propose()");

        Random random = new Random();
        Supplier<Integer> supplier = () -> {
            return random.nextInt(100)*10;
        };

        int agreed_value = 0;

        try{
            agreed_value = this.agreement.propose(supplier.get());
        }
        catch(InterruptedException e){}
        System.out.println("Thread " +name+ " woke up and settled for " +agreed_value);
    }
}
