package guiao5;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {

    private final Map<String, Product> map;
    private final Lock lock;

    Warehouse(){
        this.map = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    private class Product {
        private int quantity;
        private final Condition is_empty;

        Product(){
            this.quantity = 0;
            this.is_empty = Warehouse.this.lock.newCondition();
        }
    }

    private Product get(String item) {
        Product p = this.map.get(item);

        /**
         * get method won't ever be called outside a lock/unlock wrap
         * if it were possible, one or more threads would be pointlessly updating
         * soon-to-be dereferenced Product objects
         */

        if (p == null){
            p = new Product();
            this.map.put(item, p);
        }

        return p;
    }

    public void supply(String item, int quantity) throws InterruptedException {
        this.lock.lock();

        Product p = this.get(item); //never null
        p.quantity += quantity;
        p.is_empty.signalAll();

        this.lock.unlock();
    }

    public void consume_greedy(String[] items) throws InterruptedException {
        this.lock.lock();

        for (String i : items){
            Product p = this.get(i);

            while(p.quantity <= 0)
                p.is_empty.await();

            p.quantity--;
        }

        this.lock.unlock();
    }

    public void consume_cooperative(String[] items) throws InterruptedException {
        this.lock.lock();

        for (int i = 0; i < items.length; i++){
            Product p = this.get(items[i]);

            while(p.quantity <= 0){
                p.is_empty.await();
                i = 0;
            }
        }

        for(String i : items)
            this.get(i).quantity--;

        this.lock.unlock();
    }

    public void consume_mixed(String[] items) throws InterruptedException {
        this.lock.lock();

        int number_of_tries = 0;

        for (int i = 0; i < items.length && number_of_tries < 3; i++){
            Product p = this.get(items[i]);

            while(p.quantity <= 0){
                p.is_empty.await();
                i = 0;
                number_of_tries++;
            }
        }

        if(number_of_tries==3){
            this.consume_greedy(items);
            this.lock.unlock();
        }
        else{
            for(String i : items)
                this.get(i).quantity--;

            this.lock.unlock();
        }
    }
}