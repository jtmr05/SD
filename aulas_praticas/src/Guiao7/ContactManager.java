package Guiao7;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ContactManager {
    private final Map<String, Contact> contacts;
    private final Lock lock;

    ContactManager() {
        this.contacts = new HashMap<>();
        // example pre-population
        this.lock = new ReentrantLock();
        this.update(new Contact("John", 20, 253123321, null, Arrays.asList("john@mail.com")));
        this.update(new Contact("Alice", 30, 253987654, "CompanyInc.", Arrays.asList("alice.personal@mail.com", "alice.business@mail.com")));
        this.update(new Contact("Bob", 40, 253123456, "Comp.Ld", Arrays.asList("bob@mail.com", "bob.work@mail.com")));
    }


    // @TODO
    public void update(Contact c) {
        this.lock.lock();
        
        String key = c.getPhoneNumber() + "";
        if(!this.contacts.containsKey(key))
            this.contacts.put(key, c);

        this.lock.unlock();
    }

    // @TODO
    public ContactList getContacts(){
        this.lock.lock();
        ContactList cl = new ContactList(this.contacts.values());
        this.lock.unlock();
        return cl;
    }
}