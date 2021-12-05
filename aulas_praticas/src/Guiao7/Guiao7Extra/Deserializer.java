package Guiao7.Guiao7Extra;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Deserializer{

    private final Map<Integer, Friend> keys;
    private int current;

    Deserializer(){
        this.keys = new HashMap<>();
        this.current = 0;
    }

    public Friend deserialize(DataInputStream in) {
        
        try{
            Friend f; Integer key;

            if(!in.readBoolean()){
                key = Integer.valueOf(in.readInt());
                f = this.keys.get(key);
            }
            else{
                key = Integer.valueOf(this.current++);

                String name = in.readUTF();
                int age = in.readInt();
                long phoneNumber = in.readLong();
                
                int e_size = in.readInt();
                List<String> emails = new ArrayList<>(e_size);
                for(int i = 0; i < e_size; i++)
                    emails.add(in.readUTF());

                int f_size = in.readInt();
                List<Friend> friends = new ArrayList<>(f_size);
                for(int i = 0; i < f_size; i++){
                    Friend aux = this.deserialize(in);
                    if(aux != null)
                        friends.add(aux);
                    else
                        break;
                }
                f = new Friend(name, age, phoneNumber, emails, friends);
            }
            return f;
        }
        catch(IOException e){
            return null;
        }    
    }
}