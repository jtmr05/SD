package guiao2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Stream;

class BankLockedAccount implements IBank{

    private final int slots;
    private final LockedAccount[] av;
    private final Lock lock; //useless

    BankLockedAccount(int slots){
        this.slots = slots;
        this.av = new LockedAccount[slots];
        for(int i = 0; i < slots; i++)
            this.av[i] = new LockedAccount(0);
        this.lock = new ReentrantLock();
    }

    public int balance(int id) {
        if (id < 0 || id >= this.slots)
            return 0;

        this.av[id].lock();
        int ret = this.av[id].balance();
        this.av[id].unlock();
        return ret;
    }

    public boolean deposit(int id, int value) {
        if (id < 0 || id >= this.slots)
            return false;

        this.av[id].lock();
        boolean ret = this.av[id].deposit(value);
        this.av[id].unlock();
        return ret;
    }

    public boolean withdraw(int id, int value) {
        if (id < 0 || id >= this.slots)
            return false;

        this.av[id].lock();
        boolean ret = this.av[id].withdraw(value);
        this.av[id].unlock();
        return ret;
    }

    public boolean transfer(int from, int to, int value){

        boolean ret;
        if(from < 0 || from >= this.slots || to < 0 || to >= this.slots)
			return false;

        if(from < to){
            this.av[from].lock();
            if((ret = this.av[from].withdraw(value))){
                this.av[to].lock();
                this.av[from].unlock();
                ret = this.av[to].deposit(value);
                this.av[to].unlock();
            }
            else
                this.av[from].unlock();
        }
        else{
            this.av[to].lock();
            this.av[from].lock();
            if((ret = this.av[from].withdraw(value))){
                this.av[from].unlock();
                ret = this.av[to].deposit(value);
                this.av[to].unlock();
            }
            else{
                this.av[to].unlock();
                this.av[from].unlock();
            }
        }
        return ret;
    }

    //This does not guarantee that there isn't an "extra"
    //amount of money at the moment of the calculation
    public int totalBalanceWrong(){
		this.lock.lock();
		int ret = Stream.of(this.av).map(LockedAccount::balance).reduce(0, (x1, x2) -> x1 + x2);
		this.lock.unlock();
		return ret;
	}

    //Correct implementation
    public int totalBalance(){
        for(LockedAccount a : this.av)
            a.lock();

        Function<LockedAccount, Integer> fun = a -> {
            int ret = a.balance();
            a.unlock();
            return ret;
        };

        return Stream.of(this.av).map(fun).reduce(0, (x1, x2) -> x1 + x2);
    }
}
