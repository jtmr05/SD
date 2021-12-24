package guiao2;

import java.util.Random;

public class Mover implements Runnable{

    private final IBank b;
    private final int s;

    Mover(IBank b, int s){
        this.b=b;
        this.s=s;
    }

    public void run() {
        final int moves=100000;
        int from, to;
        Random rand = new Random();

        for (int m = 0; m < moves; m++){
            from = rand.nextInt(this.s);
            while((to = rand.nextInt(this.s))==from);
            b.transfer(from, to, 1);
        }
        System.out.println(b.totalBalance());
    }
}
