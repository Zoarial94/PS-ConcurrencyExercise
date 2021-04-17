package com.zoarial;

// This is a test of a certain optimization
// This just allocates extra buckets in the underlying maps (ConcurrentHashMap)
// For some reason this seems to improve performance in single threaded applications
// Needs more testing to understand why
public class People2 extends People{
    public People2(int initialCapacity, float loadFactor, int concurrentFactor) {
        // Allocate extra buckets
        super(initialCapacity, loadFactor, concurrentFactor*=2);

    }
}
