package com.zoarial;

// Person interface decreases the amount of code duplication for testing
// The People classes are supposed to be thread-safe
public interface PeopleInterface {
    boolean add(Person p);
    boolean remove(Person p);
    void clear();

    Person findByName(String s);
    Person findById(int i);
    Person findByAddress(String s);
    Person findByPhone(String s);
}
