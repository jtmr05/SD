package guiao7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ServerWorker implements Runnable {
    private final Socket socket;
    private final ContactManager manager;

    public ServerWorker (Socket socket, ContactManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    // @TODO
    @Override
    public void run(){
        try{
            //send list of contacts
            DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
            ContactList cl = this.manager.getContacts();
            cl.serialize(out);
            out.flush();
            this.socket.shutdownOutput();
            out = null;

            //read contacts to update/add
            DataInputStream in = new DataInputStream(this.socket.getInputStream());
            while(true){
                Contact c = Contact.deserialize(in);

                if(c != null)
                    this.manager.update(c);
                else{
                    this.socket.shutdownInput();
                    this.socket.close();
                    break;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}