package com.zoarial;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// People is more performant than StrictPeople, but doesn't conform to the assignment
public class People {
    final private ConcurrentHashMap<String, Person> nameMap;
    final private ConcurrentHashMap<Integer, Person> idMap;
    final private ConcurrentHashMap<String, Person> addressMap;
    final private ConcurrentHashMap<String, Person> phoneMap;

    final private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
    final private ExecutorService threadPool;

    public People() {
        this(32);
    }
    public People(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }
    public People(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 16);
    }
    public People(int initialCapacity, float loadFactor, int concurrentFactor) {
        nameMap = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrentFactor);
        idMap = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrentFactor);
        addressMap = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrentFactor);
        phoneMap = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrentFactor);
        threadPool = Executors.newFixedThreadPool(concurrentFactor);
    }

    public People(People oldPeople) {
        this(oldPeople, 16);
    }
    public People(People oldPeople, int concurrentFactor) {
        oldPeople.rwlock.readLock().lock();
        nameMap = new ConcurrentHashMap<>(oldPeople.nameMap);
        idMap = new ConcurrentHashMap<>(oldPeople.idMap);
        addressMap = new ConcurrentHashMap<>(oldPeople.addressMap);
        phoneMap = new ConcurrentHashMap<>(oldPeople.phoneMap);
        threadPool = Executors.newFixedThreadPool(concurrentFactor);
        oldPeople.rwlock.readLock().unlock();
    }

    public void close() {
        threadPool.shutdownNow();
    }

    // add doesn't need a exclusive lock
    // the map is thread-safe
    public boolean add(Person p, boolean block) {
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

        Runnable thread = ()-> {
            rwlock.readLock().lock();
            nameMap.put(p.getName(), p);
            idMap.put(p.getId(), p);
            addressMap.put(p.getAddress(), p);
            phoneMap.put(p.getPhoneNumber(), p);
            rwlock.readLock().unlock();
        };

        Future<?> f = threadPool.submit(thread);

        if(block) {
            try {
                f.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean add(Person p) {
        return add(p, false);
    }

    // Always blocks
    // remove needs an exclusive lock (write lock)
    // the lock isn't strictly need, but makes sure you don't reference a Person which is partially deleted
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
