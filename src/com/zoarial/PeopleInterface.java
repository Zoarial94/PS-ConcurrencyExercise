package com.zoarial;

public interface PeopleInterface {
    boolean add(Person p);
    boolean remove(Person p);
    void clear();

    Person findByName(String s);
    Person findById(int i);
    Person findByAddress(String s);
    Person findByPhone(String s);
}
