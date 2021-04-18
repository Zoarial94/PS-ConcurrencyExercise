package com.zoarial;

public class People3 extends People {
    public People3(int initialCapacity, float loadFactor, int concurrentFactor) {
        // Allocate extra buckets
        super(initialCapacity, loadFactor, concurrentFactor*=4);

    }
}
