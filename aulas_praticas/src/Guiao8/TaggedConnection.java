package Guiao8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.*;

class TaggedConnection implements AutoCloseable {
    
    static class Frame {
        
        public final int tag;
        public final byte[] data;
        
        Frame(int tag, byte[] data){ 
            this.tag = tag; 
            this.data = data;
        }
    }

    private final Socket s;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Lock readLock;
    private final Lock writeLock;

    TaggedConnection(Socket socket) throws IOException {
        this.s = socket;
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.writeLock = new ReentrantLock();
        this.readLock = new ReentrantLock();
    }

    public void send(Frame frame) throws IOException { 
        this.writeLock.lock();

        this.out.writeInt(frame.tag);
        this.out.writeInt(frame.data.length);
        this.out.write(frame.data);
        this.out.flush();

        this.writeLock.unlock();
    }
    
    public void send(int tag, byte[] data) throws IOException {
        this.send(new Frame(tag, data));
    }
    
    public Frame receive() throws IOException {
        this.readLock.lock();

        int tag = this.in.readInt();
        int size = this.in.readInt();
        byte[] data = new byte[size];
        this.in.readFully(data);
        Frame f = new Frame(tag, data);

        this.readLock.unlock();
        return f;
    }
    
    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.s.close();
    }
}