package Guiao7;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {

    public static void main (String[] args) throws IOException {
        
        ServerSocket serverSocket = new ServerSocket(12345);
        try{
            ContactManager manager = new ContactManager();

            while(true){
                Socket socket = serverSocket.accept();
                Thread worker = new Thread(new ServerWorker(socket, manager));
                worker.start();
            }
        }
        finally{
            serverSocket.close();
        }
    }
}