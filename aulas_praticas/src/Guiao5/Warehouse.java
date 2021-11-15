package Guiao5;

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
            this.is_empty = lock.newCondition();
        }        
    }

    private Product get(String item) {
        Product p = this.map.get(item);
        
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

    public void consume(String[] items) throws InterruptedException {
        this.lock.lock();

        for (String i : items){
            Product p = this.get(i);

            while(p.quantity <= 0)
                p.is_empty.await();

            p.quantity--;
        }
        
        this.lock.unlock();
    }

    public void coop_consume(String[] items) throws InterruptedException {
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
}

/**
 * A | B | C
 * 1 | 1 | 0
 * 
 * cliente 1 chega, quer os 3, espera...
 * cliente 2
 * 
 * 
 */