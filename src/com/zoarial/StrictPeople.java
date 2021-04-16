package com.zoarial;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// StrictPeople conforms to the assignment, but is not as performant.
public class StrictPeople {
    final private HashMap<String, Person> nameMap;
    final private HashMap<Integer, Person> idMap;
    final private HashMap<String, Person> addressMap;
    final private HashMap<String, Person> phoneMap;

    final private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public StrictPeople() {
        nameMap = new HashMap<>();
        idMap = new HashMap<>();
        addressMap = new HashMap<>();
        phoneMap = new HashMap<>();
    }
    public StrictPeople(int initialCapacity) {
        nameMap = new HashMap<>(initialCapacity);
        idMap = new HashMap<>(initialCapacity);
        addressMap = new HashMap<>(initialCapacity);
        phoneMap = new HashMap<>(initialCapacity);
    }
    public StrictPeople(int initialCapacity, float loadFactor) {
        nameMap = new HashMap<>(initialCapacity, loadFactor);
        idMap = new HashMap<>(initialCapacity, loadFactor);
        addressMap = new HashMap<>(initialCapacity, loadFactor);
        phoneMap = new HashMap<>(initialCapacity, loadFactor);
    }
    public StrictPeople(StrictPeople oldPeople) {
        oldPeople.rwlock.readLock().lock();
        nameMap = new HashMap<>(oldPeople.nameMap);
        idMap = new HashMap<>(oldPeople.idMap);
        addressMap = new HashMap<>(oldPeople.addressMap);
        phoneMap = new HashMap<>(oldPeople.phoneMap);
        oldPeople.rwlock.readLock().unlock();
    }

    public boolean add(Person p) {
        rwlock.readLock().lock();
        // If there are any duplicates, then don't add the new person.
        boolean hasDuplicate =  nameMap.containsKey(p.getName()) ||
                                idMap.containsKey(p.getId()) ||
                                addressMap.containsKey(p.getAddress()) ||
                                phoneMap.containsKey(p.getPhoneNumber());
        rwlock.readLock().unlock();

        if(hasDuplicate) {
           return false;
        }

        rwlock.writeLock().lock();

        nameMap.put(p.getName(), p);
        idMap.put(p.getId(), p);
        addressMap.put(p.getAddress(), p);
        phoneMap.put(p.getPhoneNumber(), p);

        rwlock.writeLock().unlock();

        return true;
    }

    // Always blocks
    public boolean remove(Person p) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  nameMap.containsKey(p.getName()) &&
                idMap.containsKey(p.getId()) &&
                addressMap.containsKey(p.getAddress()) &&
                phoneMap.containsKey(p.getPhoneNumber());
        rwlock.readLock().unlock();

        if(hasDuplicate) {
            rwlock.writeLock().lock();
            nameMap.remove(p.getName());
            idMap.remove(p.getId());
            addressMap.remove(p.getAddress());
            phoneMap.remove(p.getPhoneNumber());
            rwlock.writeLock().unlock();
            return true;
        }
        return false;
    }

    public boolean removeName(String s) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  nameMap.containsKey(s);
        rwlock.readLock().unlock();

        if(hasDuplicate) {
            rwlock.writeLock().lock();
            Person p = nameMap.get(s);
            nameMap.remove(s);
            idMap.remove(p.getId());
            addressMap.remove(p.getAddress());
            phoneMap.remove(p.getPhoneNumber());
            rwlock.writeLock().unlock();
            return true;
        }
        return false;
    }

    public boolean removeId(int i) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  idMap.containsKey(i);
        rwlock.readLock().unlock();

        if(hasDuplicate) {
            rwlock.writeLock().lock();
            Person p = idMap.get(i);
            nameMap.remove(p.getName());
            idMap.remove(i);
            addressMap.remove(p.getAddress());
            phoneMap.remove(p.getPhoneNumber());
            rwlock.writeLock().unlock();
            return true;
        }
        return false;
    }

    public boolean removeAddress(String s) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  addressMap.containsKey(s);
        rwlock.readLock().unlock();

        if(hasDuplicate) {
            rwlock.writeLock().lock();
            Person p = addressMap.get(s);
            nameMap.remove(p.getName());
            idMap.remove(p.getId());
            addressMap.remove(s);
            phoneMap.remove(p.getPhoneNumber());
            rwlock.writeLock().unlock();
            return true;
        }
        return false;
    }

    public boolean removePhone(String s) {
        rwlock.readLock().lock();
        boolean hasDuplicate =  phoneMap.containsKey(s);
        rwlock.readLock().unlock();

        if(hasDuplicate) {
            rwlock.writeLock().lock();
            Person p = phoneMap.get(s);
            nameMap.remove(p.getName());
            idMap.remove(p.getId());
            addressMap.remove(p.getAddress());
            phoneMap.remove(s);
            rwlock.writeLock().unlock();
            return true;
        }
        return false;
    }

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
