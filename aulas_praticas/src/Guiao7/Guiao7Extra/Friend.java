package Guiao7.Guiao7Extra;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

class Friend {
    private final String name;
    private final int age;
    private final long phoneNumber;
    private final List<String> emails;
    private final List<Friend> friends;

    Friend(String name, int age, long phoneNumber, List<String> emails, List<Friend> friends){
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.emails = new ArrayList<>(emails);
        this.friends = new ArrayList<>(friends);
    }

    public String getName(){ 
        return this.name; 
    }

    public int getAge(){ 
        return this.age; 
    }
    
    public long getPhoneNumber(){ 
        return this.phoneNumber;
    }
    
    public List<String> getEmails(){ 
        return new ArrayList<>(this.emails); 
    }

    public List<Friend> getFriends(){
        return new ArrayList<>(this.friends);
    }

    public void serialize (DataOutputStream out) throws IOException{
       Serializer s = new Serializer();
       s.serialize(out, this);
    }

    public static Friend deserialize (DataInputStream in){ 
        Deserializer ds = new Deserializer();
        return ds.deserialize(in);
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";")
               .append(this.age).append(";")
               .append(this.phoneNumber).append(";")
               .append(this.emails.toString())
               .append(this.friends.toString());
        return builder.toString();
    }
}