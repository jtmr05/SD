package Guiao8;

import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Guiao8.TaggedConnection.Frame;

class Demultiplexer implements AutoCloseable {
    
    public static final int NUM_OF_TAGS = 3;

    private final TaggedConnection tagged;
    private final Map<Integer, Deque<byte[]>> map;
    private final Map<Integer, Condition> conditions;
    
    private final Lock lock;

    Demultiplexer(TaggedConnection conn){
        this.tagged = conn;
        this.map = new HashMap<>();
        this.lock = new ReentrantLock();
        this.conditions = new HashMap<>();
    }

    public void start(){
        Runnable r = () -> {
            try{
                while(true){
                    Frame f = this.tagged.receive();
                    Integer key = Integer.valueOf(f.tag);
                    
                    this.lock.lock();
                    if(this.map.containsKey(key))
                        this.map.get(key).add(f.data);
                    else{
                        Deque<byte[]> ll = new LinkedList<>();
                        Condition c = this.lock.newCondition();
                        ll.add(f.data);
                        this.map.put(key, ll);
                        this.conditions.put(key, c);
                    }
                    this.conditions.get(key).signal();
                    this.lock.unlock();
                }
             }
             catch(IOException e){
                this.conditions.values().stream().forEach(Condition::signalAll);
                e.printStackTrace();
             }
        };

        Thread t = new Thread(r);
        t.start();
    }
    
    public void send(Frame frame) throws IOException {
        this.tagged.send(frame);
    }
    
    public void send(int tag, byte[] data) throws IOException {
        this.tagged.send(tag, data);
    }
    
    public byte[] receive(int tag) throws IOException, InterruptedException {
        this.lock.lock();

        Deque<byte[]> queue = this.map.get(tag);

        while(queue.isEmpty())
            this.conditions.get(tag).await();
        
        byte[] ret = queue.pop();
        
        this.lock.unlock();
        return ret;
    }
    
    public void close() throws IOException {
        this.tagged.close();
    }
}