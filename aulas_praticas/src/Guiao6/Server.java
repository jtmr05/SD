package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12345);
        
        try {
            while (true) {
                Socket socket = ss.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line;
                int sum, count;
                count = sum = 0;

                while (((line = in.readLine()) != null) && !line.contains("stop")) {
                    try{
                        sum += Integer.parseInt(line);
                        count++;
                    }
                    catch(NumberFormatException e){}
                        out.println(sum + "");
                        out.flush();
                    
                }
                if(count > 0){
                    out.println("average = " + (double) sum/count);
                    out.flush();
                }

                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
            }

        } 
        finally{
            ss.close();
        }
    }
}
