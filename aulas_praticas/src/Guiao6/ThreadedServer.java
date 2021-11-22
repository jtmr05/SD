package Guiao6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ThreadedServer {

    public static final boolean GLOBAL_ON = true;
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(12345);
            Numbers n = new Numbers();

            while (true) {
                Socket socket = ss.accept();

                if(GLOBAL_ON)
                    new Thread(new IntReaderGlobal(socket, n)).start();
                else
                    new Thread(new IntReader(socket)).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
