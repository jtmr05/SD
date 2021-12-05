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
        Iterator<Contact> iter = this.iterator();
        while(iter.hasNext())
            iter.next().serialize(out);
    }

    // @TODO
    public static ContactList deserialize (DataInputStream in) throws IOException{
        ContactList cl = new ContactList(in.readInt());

        for(Contact c = Contact.deserialize(in); c != null;){
            cl.add(c);
            c = Contact.deserialize(in);
        }
        
        return cl;
    }
}