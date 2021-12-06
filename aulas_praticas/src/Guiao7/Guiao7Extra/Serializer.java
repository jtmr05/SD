package Guiao7.Guiao7Extra;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.function.Consumer;

class Serializer {
 
    private final Set<Long> people;
    private final Set<Entry<Long, Long>> relationships;

    Serializer(){
        this.people = new HashSet<>();
        this.relationships = new HashSet<>();
    }

    public void serialize(DataOutputStream out, Friend f) throws IOException {
        this.serializePersonalInfo(out, f);
        this.serializeRelationships(out);
    }
    
    public void serializePersonalInfo(DataOutputStream out, Friend f) throws IOException {
        Long key = Long.valueOf(f.getPhoneNumber());

        if(!this.people.contains(key)){
            
            this.people.add(key);
            out.writeBoolean(true); //has next...
            out.writeUTF(f.getName());
            out.writeInt(f.getAge());
            out.writeLong(f.getPhoneNumber());
            out.writeUTF(f.getEmail());

            Consumer<Friend> consumer = x -> {
                Long number = Long.valueOf(x.getPhoneNumber());
                SimpleEntry<Long, Long> entry = new SimpleEntry<>(key, number);
                this.relationships.add(entry);
                try {
                    this.serializePersonalInfo(out, f);
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            };

            f.getFriends().stream().forEach(consumer);
            
            out.writeBoolean(false);
        }
    }

    private void serializeRelationships(DataOutputStream out) throws IOException {
        int size = this.relationships.size();
        out.writeInt(size);
        for(Entry<Long, Long> entry : this.relationships){
            out.writeLong(entry.getKey());
            out.writeLong(entry.getValue());
        }
    }

}