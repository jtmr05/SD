package Guiao6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class IntReaderGlobal implements Runnable {

    private final Socket socket;
    private final Numbers n;

    IntReaderGlobal(Socket socket, Numbers n){
        this.socket = socket;
        this.n = n;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());

            String line;
            int sum, count;
            count = sum = 0;

            while (((line = in.readLine()) != null) && !line.contains("stop")) {
                try{
                    sum += Integer.parseInt(line);
                    count++;
                    //this.n.add(line);
                }
                catch(NumberFormatException e){}
                out.println(sum + "");
                out.flush();
            }
            
            this.n.add(sum, count);

            if(count <= 0) 
                count = 1;
            out.println("average = " + (double) sum/count + " and global average = " + this.n.get_average());
            out.flush();
            

            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}