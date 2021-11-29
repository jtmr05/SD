package Guiao7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


class ContactList extends ArrayList<Contact> {

    ContactList(){
        super();
    }

    ContactList(Collection<Contact> cs){
        super(cs);
    }

    ContactList(int size){
        super(size);
    }

    // @TODO
    public void serialize (DataOutputStream out) throws IOException{
        out.writeInt(this.size());
        Iterator<Contact> iter = super.iterator();
        while(iter.hasNext())
            iter.next().serialize(out);
    }

    // @TODO
    public static ContactList deserialize (DataInputStream in) throws IOException{
        ContactList cl = new ContactList(in.readInt());

        while(true){
            Contact c = Contact.deserialize(in);

            if(c != null)
                cl.add(c);
            else
                break;
        }

        return cl;
    }
}