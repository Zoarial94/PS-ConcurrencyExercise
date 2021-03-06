package com.zoarial;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// People is more performant than StrictPeople, but doesn't conform to the assignment
// Since the underlying maps (ConcurrentHashMap) are thread-safe, there is no strict need for exclusive locks
// Exclusive locks are used when coherency is necessary (see clear())
public class People implements PeopleInterface {
    // Maps containing all the data
    final private ConcurrentHashMap<String, Person> nameMap;
    final private ConcurrentHashMap<Integer, Person> idMap;
    final private ConcurrentHashMap<String, Person> addressMap;
    final private ConcurrentHashMap<String, Person> phoneMap;

    final private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    // Constructors
    public People() {
        this(32);
    }
    public People(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }
    public People(int initialCapacity, float loadFactor) {
        nameMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        idMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        addressMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        phoneMap = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    public People(People oldPeople) {
        // Use a write lock to make sure have ALL the data since the lock
        // There are modifying functions that use the read lock
        oldPeople.rwlock.writeLock().lock();
        nameMap = new ConcurrentHashMap<>(oldPeople.nameMap);
        idMap = new ConcurrentHashMap<>(oldPeople.idMap);
        addressMap = new ConcurrentHashMap<>(oldPeople.addressMap);
        phoneMap = new ConcurrentHashMap<>(oldPeople.phoneMap);
        oldPeople.rwlock.writeLock().unlock();
    }

    public boolean add(Person p) {
        rwlock.readLock().lock();

        // If there are any duplicates, then don't add the new person.
        boolean hasDuplicate =  nameMap.containsKey(p.getName()) ||
                idMap.containsKey(p.getId()) ||
                addressMap.containsKey(p.getAddress()) ||
                phoneMap.containsKey(p.getPhoneNumber());

        // If there is a duplicate, then do nothing
        if(hasDuplicate) {
            rwlock.readLock().unlock();
            return false;
        }

        // If there is no duplicate, then add it
        nameMap.put(p.getName(), p);
        idMap.put(p.getId(), p);
        addressMap.put(p.getAddress(), p);
        phoneMap.put(p.getPhoneNumber(), p);

        rwlock.readLock().unlock();
        return true;
    }

    // For performance reasons, there is no blocking.
    // The underlying maps are thread-safe
    public boolean remove(Person p) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  nameMap.containsKey(p.getName()) &&
                idMap.containsKey(p.getId()) &&
                addressMap.containsKey(p.getAddress()) &&
                phoneMap.containsKey(p.getPhoneNumber());

        // If there is a duplicate, then remove it
        if(hasDuplicate) {
            nameMap.remove(p.getName());
            idMap.remove(p.getId());
            addressMap.remove(p.getAddress());
            phoneMap.remove(p.getPhoneNumber());
        }
        rwlock.readLock().unlock();
        return hasDuplicate;
    }

    public boolean removeName(String s) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  nameMap.containsKey(s);

        if(hasDuplicate) {
            Person p = nameMap.get(s);
            nameMap.remove(s);
            idMap.remove(p.getId());
            addressMap.remove(p.getAddress());
            phoneMap.remove(p.getPhoneNumber());
        }
        rwlock.readLock().unlock();
        return hasDuplicate;
    }

    public boolean removeId(int i) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  idMap.containsKey(i);

        if(hasDuplicate) {
            Person p = idMap.get(i);
            nameMap.remove(p.getName());
            idMap.remove(i);
            addressMap.remove(p.getAddress());
            phoneMap.remove(p.getPhoneNumber());
        }
        rwlock.readLock().unlock();
        return hasDuplicate;
    }

    public boolean removeAddress(String s) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  addressMap.containsKey(s);

        if(hasDuplicate) {
            Person p = addressMap.get(s);
            nameMap.remove(p.getName());
            idMap.remove(p.getId());
            addressMap.remove(s);
            phoneMap.remove(p.getPhoneNumber());
        }
        rwlock.readLock().unlock();
        return hasDuplicate;
    }

    public boolean removePhone(String s) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  phoneMap.containsKey(s);

        if(hasDuplicate) {
            Person p = phoneMap.get(s);
            nameMap.remove(p.getName());
            idMap.remove(p.getId());
            addressMap.remove(p.getAddress());
            phoneMap.remove(s);
        }
        rwlock.readLock().unlock();
        return hasDuplicate;
    }

    // Clear all maps. Use an exclusive lock to make sure all data is cleared at the same time.
    public void clear() {
        rwlock.writeLock().lock();

        nameMap.clear();
        idMap.clear();
        addressMap.clear();
        phoneMap.clear();

        rwlock.writeLock().unlock();
    }

    public Person findByName(String s) {
        rwlock.readLock().lock();
        Person ret = nameMap.get(s);
        rwlock.readLock().unlock();
        return ret;
    }

    public Person findById(int i) {
        rwlock.readLock().lock();
        Person ret = idMap.get(i);
        rwlock.readLock().unlock();
        return ret;
    }

    public Person findByAddress(String s) {
        rwlock.readLock().lock();
        Person ret = addressMap.get(s);
        rwlock.readLock().unlock();
        return ret;
    }

    public Person findByPhone(String s) {
        rwlock.readLock().lock();
        Person ret = phoneMap.get(s);
        rwlock.readLock().unlock();
        return ret;
    }
}
