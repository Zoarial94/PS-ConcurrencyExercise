package com.zoarial;

public class People2 extends People{
    public People2(int initialCapacity, float loadFactor, int concurrentFactor) {
        super(initialCapacity, loadFactor, concurrentFactor*=2);

    }
}
