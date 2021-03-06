package guiao7.guiao7extra;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

class Friend {
    private final String name;
    private final int age;
    private final long phoneNumber;
    private final String email;
    private final Set<Friend> friends;

    Friend(String name, int age, long phoneNumber, String email, Collection<Friend> friends){
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.friends = new HashSet<>(friends);
    }

    Friend(String name, int age, long phoneNumber, String email){
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.friends = new HashSet<>();
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

    public String getEmail(){
        return this.email;
    }

    public List<Friend> getFriends(){
        return new ArrayList<>(this.friends);
    }

    public void setFriendship(Friend f){
        this.friends.add(f);
    }

    public void serialize(DataOutputStream out) throws IOException {
       Serializer s = new Serializer();
       s.serialize(out, this);
    }

    public static Friend deserialize(DataInputStream in){
        Deserializer ds = new Deserializer();
        return ds.deserialize(in);
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";")
               .append(this.age).append(";")
               .append(this.phoneNumber).append(";")
               .append(this.email).append(";")
               .append(this.friends.toString());
        return builder.toString();
    }
}