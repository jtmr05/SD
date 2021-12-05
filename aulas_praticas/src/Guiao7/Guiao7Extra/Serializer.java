package Guiao7.Guiao7Extra;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Serializer {
 
    //map the phone number (unique id) to the position in which it appeared
    private Map<Long, Integer> keys;
    private int current;

    Serializer(){
        this.keys = new HashMap<>();
        this.current = 0;
    }
    
    public void serialize(DataOutputStream out, Friend f) throws IOException {
        Long key = Long.valueOf(f.getPhoneNumber());

        if(this.keys.containsKey(key)){
            out.writeBoolean(false); //will we be writing the object completely?
            out.writeInt(this.keys.get(key));
        }
        else{
            this.keys.put(key, Integer.valueOf(this.current++));

            out.writeBoolean(true);
            out.writeUTF(f.getName());
            out.writeInt(f.getAge());
            out.writeLong(f.getPhoneNumber());
    
            out.writeInt(f.getEmails().size());
            Iterator<String> e_iter = f.getEmails().iterator();
            while(e_iter.hasNext())
                out.writeUTF(e_iter.next());
            
            out.writeInt(f.getFriends().size());
            Iterator<Friend> f_iter = f.getFriends().iterator();
            while(f_iter.hasNext())
                this.serialize(out, f_iter.next());
        }
    }
}