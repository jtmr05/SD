package guiao8;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FramedConnection implements AutoCloseable {

    private final Socket s;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Lock writeLock;
    private final Lock readLock;

    FramedConnection(Socket socket) throws IOException {
        this.s = socket;
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.writeLock = new ReentrantLock();
        this.readLock = new ReentrantLock();
    }

    public void send(byte[] data) throws IOException {
        this.writeLock.lock();

        this.out.writeInt(data.length);
        this.out.write(data);
        this.out.flush();

        this.writeLock.unlock();
    }

    public byte[] receive() throws IOException {
        this.readLock.lock();

        int size = this.in.readInt();
        byte[] data = new byte[size];
        this.in.readFully(data);

        this.readLock.unlock();
        return data;
    }

    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.s.close();
    }
}