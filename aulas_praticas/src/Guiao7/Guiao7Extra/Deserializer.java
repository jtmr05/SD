package guiao7.guiao7extra;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Deserializer{

    private final Map<Long, Friend> people;
    private Friend ret;

    Deserializer(){
        this.people = new HashMap<>();
        this.ret = null;
    }

    public Friend deserialize(DataInputStream in) {

        try{
            this.deserializePersonalInfo(in);

            for(int i = 0; i < this.people.values().size(); i++)
                in.readBoolean();

            int size = in.readInt();
            for(int i = 0; i < size; i++){
                Long key = in.readLong();
                Long value = in.readLong();
                this.people.get(key).setFriendship(this.people.get(value));
            }

            return this.ret;
        }
        catch(IOException e){
            return null;
        }
    }

    public void deserializePersonalInfo(DataInputStream in) throws IOException {
        while(in.readBoolean()){
            String name = in.readUTF();
            int age = in.readInt();
            long phoneNumber = in.readLong();
            String email = in.readUTF();

            Friend f = new Friend(name, age, phoneNumber, email);
            this.people.put(Long.valueOf(phoneNumber), f);

            if(this.ret == null)
                this.ret = f;
        }
    }
}