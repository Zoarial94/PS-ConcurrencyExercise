package com.zoarial;

// This class just holds information about one person
public class Person {

    final private String name;
    final private int id;
    final private String address;
    final private String phoneNumber;

    public Person(String name, int id, String addr, String phone) {
        this.name = name;
        this.id = id;
        address = addr;
        phoneNumber = phone;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
