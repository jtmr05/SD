package guiao7;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

class Client {

    public static Contact parseLine(String userInput){
        String[] tokens = userInput.split(" ");

        if (tokens[3].equals("null"))
            tokens[3] = null;

        return new Contact(tokens[0], Integer.parseInt(tokens[1]), Long.parseLong(tokens[2]),
                tokens[3], new ArrayList<>(Arrays.asList(tokens).subList(4, tokens.length)));
    }

    public static void main (String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);

        DataInputStream in_server = new DataInputStream(socket.getInputStream());

        ContactList cl = ContactList.deserialize(in_server);
        for(Contact c : cl)
            System.out.println(c.toString());
        socket.shutdownInput();
        in_server = null;
        cl = null;



        String userInput;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        while ((userInput = in.readLine()) != null) {
            Contact newContact = Client.parseLine(userInput);
            System.out.println(newContact.toString());
            newContact.serialize(out);
            out.flush();
        }

        socket.shutdownOutput();
        socket.close();
    }
}
