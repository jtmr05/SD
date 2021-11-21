package Guiao5;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** Extra **/

/**
 * limited stock
 * more concurrency
 * adding and removing products
 */


class ImprovedWarehouse {

    private final Map<String, Product> map;
    private final ReadWriteLock lock;
    private static final int STOCK_SIZE = 10;

    ImprovedWarehouse(){
        this.map = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    private class Product {
        private int quantity;
        private final Lock l;
        private final Condition is_empty;
        private final Condition is_full;

        Product(){
            this.quantity = 0;
            this.l = new ReentrantLock();
            this.is_empty = this.l.newCondition();
            this.is_full = this.l.newCondition();
        }
        
        private void lock(){
            this.l.lock();
        }
        
        private void unlock(){
            this.l.unlock();
        }
    }

    //true if successful
    public boolean add(String id){
        this.lock.writeLock().lock();

        boolean ret = false;

        if(!this.map.containsKey(id)){
            this.map.put(id, new Product());
            ret = true;
        }

        this.lock.writeLock().unlock();
        return ret;
    }

    //true if successful
    public boolean remove(String id){
        this.lock.writeLock().lock();
        boolean ret = null != this.map.remove(id);
        this.lock.writeLock().unlock();
        return ret;
    }

    private Product get(String item) {
        this.lock.readLock().lock();
        Product p = this.map.get(item);
        this.lock.readLock().unlock();
        return p;
    }

    public void supply(String item, int quantity) throws InterruptedException {
        Product p = this.get(item); 
        
        if(p != null){
            p.lock();

            int diff, initialq = quantity;
            
            while(quantity > 0){
                
                p.quantity += (diff = Math.min(STOCK_SIZE - p.quantity, quantity));
                quantity -= diff;

                /** before sleeping, make sure others will consume 
                 *  don't sleep if everything is already supplied
                */
                while(p.quantity == STOCK_SIZE && quantity > 0){ 
                    p.is_empty.signalAll();
                    p.is_full.await();
                }
            }
                
            if(initialq > 0)
                p.is_empty.signalAll();

            p.unlock();
        }
    }

    public void consume(String[] items) throws InterruptedException {

        for (String i : items){
            Product p;
            if((p = this.get(i)) != null){
                p.lock();

                /**
                 * it wouldn't be helpful to signalAll at this point 
                 * since quantity hasn't been updated yet
                 */
                while(p.quantity <= 0)
                    p.is_empty.await();
                
                p.quantity--;
                p.is_full.signalAll();

                p.unlock();
            }
        }
    }
}