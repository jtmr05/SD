package Guiao7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

class Contact{
    private final String name;
    private final int age;
    private final long phoneNumber;
    private final String company;     // Pode ser null
    private final List<String> emails;

    Contact (String name, int age, long phoneNumber, String company, List<String> emails){
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.emails = new ArrayList<>(emails);
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
    
    public String getCompany(){ 
        return this.company;
    }
    
    public List<String> getEmails(){ 
        return new ArrayList<>(this.emails); 
    }

    // @TODO
    public void serialize (DataOutputStream out) throws IOException{
        out.writeUTF(this.name);
        out.writeInt(this.age);
        out.writeLong(this.phoneNumber);

        if(this.company != null){
            out.writeBoolean(true);
            out.writeUTF(this.company);
        }
        else
            out.writeBoolean(false);
        
        out.writeInt(this.emails.size());
        Iterator<String> iter = this.emails.iterator();
        while(iter.hasNext())
            out.writeUTF(iter.next());
    }

    // @TODO
    public static Contact deserialize (DataInputStream in){ 
        try{
            String name = in.readUTF();
            int age = in.readInt();
            long phoneNumber = in.readLong();
            
            String company = in.readBoolean() ? in.readUTF() : null;
            
            int size = in.readInt();
            List<String> emails = new ArrayList<>(size);
            for(int i = 0; i < size; i++)
                emails.add(in.readUTF());

            return new Contact(name, age, phoneNumber, company, emails);
        }
        catch(IOException e){
            return null;
        }
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";")
               .append(this.age).append(";")
               .append(this.phoneNumber).append(";")
               .append(this.company).append(";")
               .append(this.emails.toString());
        return builder.toString();
    }
}