package com.zoarial;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static Random random = new Random();
    static Maps maps;

    public static long curTime() {
        return System.currentTimeMillis();
    }

    // Generates a random string with the characters a-z
    public static String randString(int length) {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // Generates a random string with the characters 0-9
    public static String randNumberString(int length) {
        final int leftLimit = 48; // 0
        final int rightLimit = 57; // 9
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // System.out.println generic shortcuts
    public static <T> void println(T var) {
        System.out.println(var);
    }
    public static <T> void println() {
        System.out.println();
    }
    public static <T> void print(T var) {
        System.out.print(var);
    }

    public static void main(String[] args) {
        /*
         * Start with some test code
         * These functions try to make sure the classes are coherent
         * (Meaning they act like they should. Adding actually adds, removing actually removes, etc)
         */
        if(!testStrictPeople() || !testPeople()) {
            //something went wrong
            return;
        }

        final int NUM_OF_PEOPLE = 500000;
        final int ROUNDS = 40;
        int concurrentFactor = 1;               // concurrentFactor is used to in the multithreaded tests to determine how many threads to use
        final int SINGLE_THREAD_OPTIMISE = 128; // This is to set the concurrentFactor in the concurrentHashMaps inside the People classes. It does not actually determine how many threads are used.
        maps = new Maps(NUM_OF_PEOPLE);         // Created the shared set of data

        // Turns out, in corretto 11 concurrentFactor does nothing in ConcurrentHashMap
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        println("Working...");
        println("Add StrictPeople average: " + addPeopleInterface(new StrictPeople(NUM_OF_PEOPLE, 0.75f), ROUNDS));
        println("Add People average (1 thread): " + addPeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, 1), ROUNDS));
        println("Add People average (" + SINGLE_THREAD_OPTIMISE + " threads): " + addPeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Add People2 average (1 thread): " + addPeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, 1), ROUNDS));
        println("Add People2 average (" + SINGLE_THREAD_OPTIMISE + " threads): " + addPeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Add People3 average (1 thread): " + addPeopleInterface(new People3(NUM_OF_PEOPLE, 0.75f, 1), ROUNDS));
        println("Add People3 average (" + SINGLE_THREAD_OPTIMISE + " threads): " + addPeopleInterface(new People3(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Remove StrictPeople average: " + removePeopleInterface(new StrictPeople(NUM_OF_PEOPLE, 0.75f), ROUNDS));
        println("Remove People average (1 thread): " + removePeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, 1), ROUNDS));
        println("Remove People average (" + SINGLE_THREAD_OPTIMISE + " threads): " + removePeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Remove People2 average (1 thread): " + removePeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, 1), ROUNDS));
        println("Remove People2 average (" + SINGLE_THREAD_OPTIMISE + " threads): " + removePeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Remove People3 average (1 thread): " + removePeopleInterface(new People3(NUM_OF_PEOPLE, 0.75f, 1), ROUNDS));
        println("Remove People3 average (" + SINGLE_THREAD_OPTIMISE + " threads): " + removePeopleInterface(new People3(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));



        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 4; i++) {
            println("Add ConcurrentStrictPeople average: (" + concurrentFactor + " threads): " + concurrentAddPeopleInterface(new StrictPeople(NUM_OF_PEOPLE), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

        concurrentFactor = 1;

        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 12; i++) {
            println("Add ConcurrentPeople average: (" + concurrentFactor + " threads): " + concurrentAddPeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, concurrentFactor), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

        concurrentFactor = 1;

        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 12; i++) {
            println("Add ConcurrentPeople2 average: (" + concurrentFactor + " threads): " + concurrentAddPeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, concurrentFactor), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

        concurrentFactor = 1;

        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 12; i++) {
            println("Add ConcurrentPeople3 average: (" + concurrentFactor + " threads): " + concurrentAddPeopleInterface(new People3(NUM_OF_PEOPLE, 0.75f, concurrentFactor), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

        concurrentFactor = 1;

        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 4; i++) {
            println("Remove ConcurrentStrictPeople average: (" + concurrentFactor + " threads): " + concurrentRemovePeopleInterface(new StrictPeople(NUM_OF_PEOPLE), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

        concurrentFactor = 1;

        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 12; i++) {
            println("Remove ConcurrentPeople average: (" + concurrentFactor + " threads): " + concurrentRemovePeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, concurrentFactor), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

        concurrentFactor = 1;

        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 12; i++) {
            println("Remove ConcurrentPeople2 average: (" + concurrentFactor + " threads): " + concurrentRemovePeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, concurrentFactor), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

        concurrentFactor = 1;

        println();
        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        for(int i = 0; i < 12; i++) {
            println("Remove ConcurrentPeople3 average: (" + concurrentFactor + " threads): " + concurrentRemovePeopleInterface(new People3(NUM_OF_PEOPLE, 0.75f, concurrentFactor), ROUNDS, concurrentFactor));
            concurrentFactor *= 2;
        }

    }


    /*
     * Adds Person instances into the People class concurrently
     * You can set the amount of rounds to do to create the return value (averaged)
     * You can determine how many threads to use
     */
    static long concurrentAddPeopleInterface(PeopleInterface people, int times, int threads) {
        Thread[] threadsArr = new Thread[threads];
        final AtomicBoolean startFlag = new AtomicBoolean(false);   // Start all the threads at the same time
        final AtomicInteger threadCounter = new AtomicInteger(0);   // How many threads are ready for the benchmark
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        // The same data is used for each round, but is shuffled each time
        for(int round = 0; round < times; round++) {
            // Get shuffled data
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();

            // Each thread will get an ArrayList with part of the data
            ArrayList<String>[] nameList =  new ArrayList[threads];
            ArrayList<Integer>[] idList =  new ArrayList[threads];
            ArrayList<String>[] addressList =  new ArrayList[threads];
            ArrayList<String>[] phoneList =  new ArrayList[threads];

            // Instantiate the ArrayLists
            for(int i = 0; i < threads; i++) {
                nameList[i] = new ArrayList<>();
                idList[i] = new ArrayList<>();
                addressList[i] = new ArrayList<>();
                phoneList[i] = new ArrayList<>();
            }

            // Add the data to the ArrayLists using a loop and (try to) distribute the data equally
            int threadArrayListIndex = 0;
            ArrayList<String> curNameList;
            ArrayList<Integer> curIdList;
            ArrayList<String> curAddressList;
            ArrayList<String> curPhoneList;
            int size = nameArr.length; // All the lists are the same size. Guaranteed by Maps.
            for(int i = 0; i < size; i++) {
                // Get the next ArrayList we need to add to
                curNameList = nameList[threadArrayListIndex];
                curIdList = idList[threadArrayListIndex];
                curAddressList = addressList[threadArrayListIndex];
                curPhoneList = phoneList[threadArrayListIndex];

                // Add the data to the ArrayList
                curNameList.add(nameArr[i]);
                curIdList.add(idArr[i]);
                curAddressList.add(addrArr[i]);
                curPhoneList.add(phoneArr[i]);

                // Prepare to get the next ArrayList
                // Wrap around if needed
                if(threadArrayListIndex == threads-1) {
                    threadArrayListIndex = 0;
                } else {
                    threadArrayListIndex++;
                }
            }

            // Prepare for the benchmark
            people.clear();         // Start with empty dataset
            threadCounter.set(0);   // No threads are ready at this point
            System.gc();            // Try to gc to prevent it from happening during benchmark (No guarantees)
                                    // All Person instances in people are now unreferenced
            for (int i = 0; i < threads; i++) {
                int finalI = i; // Effectively final for lambda

                // Create the new thread with lambda
                threadsArr[i] = new Thread(() -> {
                    // Get assigned list
                    ArrayList<String> threadNameList = new ArrayList<>(nameList[finalI]);
                    ArrayList<Integer> threadIdList = new ArrayList<>(idList[finalI]);
                    ArrayList<String> threadAddressList = new ArrayList<>(addressList[finalI]);
                    ArrayList<String> threadPhoneList = new ArrayList<>(phoneList[finalI]);
                    int threadArraySize = threadNameList.size(); // All arrays are the same size

                    // I used iterators because of a previous setup. Maybe there is a faster way.
                    Iterator<String> threadNameIter = threadNameList.iterator();
                    Iterator<Integer> threadIdIter = threadIdList.iterator();
                    Iterator<String> threadAddrIter = threadAddressList.iterator();
                    Iterator<String> threadPhoneIter = threadPhoneList.iterator();



                    // Stop the thread until all other threads are ready and then start all at the same time
                    // There is a race condition if threadCounter.incrementAndGet() is outside the synchronized block
                    synchronized (startFlag) {
                        int count = threadCounter.incrementAndGet();    // Current thread is ready
                        if(count == threads) {                          // If all threads are ready, notify the master thread to start the benchmark
                            synchronized (threadCounter) {
                                threadCounter.notifyAll();              // Notify the master thread
                            }
                        }
                        try {
                            startFlag.wait();                           // Wait until all other threads are ready
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Do the work
                    for (int j = 0; j < threadArraySize; j++) {
                        people.add(new Person(threadNameIter.next(), threadIdIter.next(), threadAddrIter.next(), threadPhoneIter.next()));
                    }
                });
            }

            // Start all threads. They will wait after they finish preparing
            for (Thread t : threadsArr) {
                t.start();
            }

            // Wait for all threads to be finished
            synchronized (threadCounter) {
                try {
                    threadCounter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // All threads are ready. Start the benchmark
            oldTime = curTime();
            synchronized (startFlag) {
                startFlag.notifyAll();
            }

            // Wait for all threads to finish before finishing benchmark
            for (Thread t : threadsArr) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            time = curTime();
            total += time-oldTime;  // Calculate time it took
            //println(time-oldTime);
        }
        return (total/times);


    }

    /*
     * Removes Person instances from the People class concurrently
     * You can set the amount of rounds to do to create the return value (averaged)
     * You can determine how many threads to use
     * See concurrentAddPeopleInterface for more detailed comments.
     * These two functions are almost identical
     */
    static long concurrentRemovePeopleInterface(PeopleInterface people, int times, int threads) {
        Thread[] threadsArr = new Thread[threads];
        final AtomicBoolean startFlag = new AtomicBoolean(false);
        final AtomicInteger threadCounter = new AtomicInteger(0);
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        for(int round = 0; round < times; round++) {
            // Get shuffled data
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();

            // Split up the work for the threads
            ArrayList<String>[] nameList =  new ArrayList[threads];
            ArrayList<Integer>[] idList =  new ArrayList[threads];
            ArrayList<String>[] addressList =  new ArrayList[threads];
            ArrayList<String>[] phoneList =  new ArrayList[threads];

            // Instantiate the ArrayLists
            for(int i = 0; i < threads; i++) {
                nameList[i] = new ArrayList<>();
                idList[i] = new ArrayList<>();
                addressList[i] = new ArrayList<>();
                phoneList[i] = new ArrayList<>();
            }

            // Divide up the work
            int threadArrayListIndex = 0;
            ArrayList<String> curNameList;
            ArrayList<Integer> curIdList;
            ArrayList<String> curAddressList;
            ArrayList<String> curPhoneList;
            int size = nameArr.length;
            for(int i = 0; i < size; i++) {
                curNameList = nameList[threadArrayListIndex];
                curIdList = idList[threadArrayListIndex];
                curAddressList = addressList[threadArrayListIndex];
                curPhoneList = phoneList[threadArrayListIndex];

                curNameList.add(nameArr[i]);
                curIdList.add(idArr[i]);
                curAddressList.add(addrArr[i]);
                curPhoneList.add(phoneArr[i]);
                if(threadArrayListIndex == threads-1) {
                    threadArrayListIndex = 0;
                } else {
                    threadArrayListIndex++;
                }
            }
            people.clear();
            threadCounter.set(0);
            System.gc();            // Try to gc to prevent it from happening during benchmark (No guarantees)
                                    // All Person instances in people are now unreferenced
            for (int i = 0; i < threads; i++) {
                int finalI = i;
                // Create the threads with a lambda
                threadsArr[i] = new Thread(() -> {
                    ArrayList<String> threadNameList = new ArrayList<>(nameList[finalI]);
                    ArrayList<Integer> threadIdList = new ArrayList<>(idList[finalI]);
                    ArrayList<String> threadAddressList = new ArrayList<>(addressList[finalI]);
                    ArrayList<String> threadPhoneList = new ArrayList<>(phoneList[finalI]);
                    int threadArraySize = threadNameList.size();

                    Iterator<String> threadNameIter = threadNameList.iterator();
                    Iterator<Integer> threadIdIter = threadIdList.iterator();
                    Iterator<String> threadAddrIter = threadAddressList.iterator();
                    Iterator<String> threadPhoneIter = threadPhoneList.iterator();

                    // Add the data to people so we can actually benchmark the removal of that data
                    for (int j = 0; j < threadArraySize; j++) {
                        people.add(new Person(threadNameIter.next(), threadIdIter.next(), threadAddrIter.next(), threadPhoneIter.next()));
                    }

                    // Shuffle the lists again for good measure
                    Collections.shuffle(threadNameList);
                    Collections.shuffle(threadIdList);
                    Collections.shuffle(threadAddressList);
                    Collections.shuffle(threadPhoneList);

                    // Get new iterators
                    threadNameIter = threadNameList.iterator();
                    threadIdIter = threadIdList.iterator();
                    threadAddrIter = threadAddressList.iterator();
                    threadPhoneIter = threadPhoneList.iterator();

                    // There is a race condition if threadCounter.incrementAndGet() is outside the synchronized block
                    // Synchronize the threads so they all start at the same time
                    synchronized (startFlag) {
                        int count = threadCounter.incrementAndGet();
                        if(count == threads) {
                            synchronized (threadCounter) {
                                threadCounter.notifyAll();
                            }
                        }
                        try {
                            startFlag.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Do the work
                    for (int j = 0; j < threadArraySize; j++) {
                        people.remove(new Person(threadNameIter.next(), threadIdIter.next(), threadAddrIter.next(), threadPhoneIter.next()));
                    }
                });
            }

            // Start the threads. They will wait before the benchmark
            for (Thread t : threadsArr) {
                t.start();
            }

            // Wait until all threads are ready
            synchronized (threadCounter) {
                try {
                    threadCounter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Start the benchmark
            oldTime = curTime();
            synchronized (startFlag) {
                startFlag.notifyAll();
            }

            // Wait for all threads to finish
            for (Thread t : threadsArr) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Calculate the time
            time = curTime();
            total += time-oldTime;
            //println(time-oldTime);
        }
        return (total/times);

    }


    /*
     * Add to People in a single threaded manor
     * More detailed comments are in concurrentAddPeopleInterface
     */
    static long addPeopleInterface(PeopleInterface people, int times) {
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        for(int outer = 0; outer < times; outer++) {
            // Get the random data
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();
            people.clear();
            System.gc();

            // Do the work and benchmark
            oldTime = curTime();
            for (int i = 0; i < numOfPeople; i++) {
                people.add(new Person(nameArr[i], idArr[i], addrArr[i], phoneArr[i]));
            }

            // Calculate the time
            time = curTime();
            total += time-oldTime;
            //println(time-oldTime);
        }
        return total/times; // Return the average
    }

    /*
     * Add to People in a single threaded manor
     * More detailed comments are in concurrentAddPeopleInterface
     */
    static long removePeopleInterface(PeopleInterface people, int times) {
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        for(int outer = 0; outer < times; outer++) {
            // Get the random data
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();
            people.clear();

            // Add the data so we can remove it next
            for (int i = 0; i < numOfPeople; i++) {
                people.add(new Person(nameArr[i], idArr[i], addrArr[i], phoneArr[i]));
            }

            // Shuffle the same data
            nameArr = maps.getNameRandArray();
            idArr = maps.getIdRandArray();
            addrArr = maps.getAddressRandArray();
            phoneArr = maps.getPhoneRandArray();
            System.gc();

            // Start the benchmark
            oldTime = curTime();
            for (int i = 0; i < numOfPeople; i++) {
                people.remove(new Person(nameArr[i], idArr[i], addrArr[i], phoneArr[i]));
            }
            time = curTime();
            total += time-oldTime;
            //println(time-oldTime);
        }
        return (total/times); // Return the average
    }

    static class Maps {
        private final Thread[] threads = new Thread[4];
        private final HashSet<String> nameSet = new HashSet<>();
        private final HashSet<Integer> idSet = new HashSet<>();
        private final HashSet<String> addressSet = new HashSet<>();
        private final HashSet<String> phoneSet = new HashSet<>();
        private final int NUM_OF_PEOPLE;
        private long prevTime;

        public Maps(int num) {
            NUM_OF_PEOPLE = num;
            generate();
        }

        public void generate() {
            nameSet.clear();
            idSet.clear();
            addressSet.clear();
            phoneSet.clear();

            threads[0] = new Thread(() -> {
                while (nameSet.size() < NUM_OF_PEOPLE) {
                    nameSet.add(randString(8));
                }
            });

            threads[1] = new Thread(() -> {
                for (int i = 0; i < NUM_OF_PEOPLE; i++) {
                    idSet.add(i);
                }
            });

            threads[2] = new Thread(() -> {
                while (addressSet.size() < NUM_OF_PEOPLE) {
                    addressSet.add(randString(32));
                }
            });

            threads[3] = new Thread(() -> {
                while (phoneSet.size() < NUM_OF_PEOPLE) {
                    phoneSet.add(randNumberString(10));
                }
            });
            long oldTime = curTime();
            for(Thread t : threads) {
                t.start();
            }


            for(Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long time = curTime();
            prevTime = time-oldTime;
        }

        public long getPrevTime() {
            return prevTime;
        }

        public HashSet<String> getNameSet() {
            return nameSet;
        }
        public HashSet<Integer> getIdSet() {
            return idSet;
        }
        public HashSet<String> getAddressSet() {
            return addressSet;
        }
        public HashSet<String> getPhoneSet() {
            return phoneSet;
        }
        public String[] getNameRandArray() {
            String[] arr = new String[NUM_OF_PEOPLE];
            nameSet.toArray(arr);
            shuffleArray(arr);
            return arr;
        }
        public Integer[] getIdRandArray() {
            Integer[] arr = new Integer[NUM_OF_PEOPLE];
            idSet.toArray(arr);
            shuffleArray(arr);
            return arr;
        }
        public String[] getAddressRandArray() {
            String[] arr = new String[NUM_OF_PEOPLE];
            addressSet.toArray(arr);
            shuffleArray(arr);
            return arr;
        }
        public String[] getPhoneRandArray() {
            String[] arr = new String[NUM_OF_PEOPLE];
            phoneSet.toArray(arr);
            shuffleArray(arr);
            return arr;
        }

        public int getNUM_OF_PEOPLE() {
            return NUM_OF_PEOPLE;
        }

        static <T> void shuffleArray(T[] ar)
        {
            Random rnd = new Random();
            for (int i = ar.length - 1; i > 0; i--)
            {
                int index = rnd.nextInt(i + 1);
                // Simple swap
                T a = ar[index];
                ar[index] = ar[i];
                ar[i] = a;
            }
        }
    }

    static boolean testStrictPeople() {
        StrictPeople strictPeople = new StrictPeople();
        Person me = new Person("Hunter", 0, "123 Street rd", "0123456789");

        Person me1 = new Person("Hunter", 1, "234 Street rd", "1234567890");
        Person me2 = new Person("Tyler", 0, "345 Street rd", "2345678901");
        Person me3 = new Person("Ethan", 2, "123 Street rd", "3456789012");
        Person me4 = new Person("Jeremy", 3, "456 Street rd", "0123456789");


        if(!strictPeople.add(me)) {
            println("Something went wrong");
            return false;
        }

        if(strictPeople.add(me1)) {
            println("Something went wrong");
            return false;
        } else if(strictPeople.add(me2)) {
            println("Something went wrong");
            return false;
        } else if(strictPeople.add(me3)) {
            println("Something went wrong");
            return false;
        } else if(strictPeople.add(me4)) {
            println("Something went wrong");
            return false;
        }

        Person newPerson = new Person("Ethan", 1, "987 Road blvd", "9876543210");

        if(!strictPeople.add(newPerson)) {
            println("Something went wrong");
            return false;
        }

        if(strictPeople.findByName("Hunter") != me) {
            println("Something went wrong");
            return false;
        } else if(strictPeople.findById(0) != me) {
            println("Something went wrong");
            return false;
        } else if(strictPeople.findByAddress("123 Street rd") != me) {
            println("Something went wrong");
            return false;
        } else if(strictPeople.findByPhone("0123456789") != me) {
            println("Something went wrong");
            return false;
        }

        if(strictPeople.findByName("Samantha") != null) {
            println("Something went wrong");
            return false;
        }

        println("Everything has seemed to go smoothly for StrictPeople...");
        return true;

    }
    static boolean testPeople() {
        People people = new People();
        Person me = new Person("Hunter", 0, "123 Street rd", "0123456789");

        Person me1 = new Person("Hunter", 1, "234 Street rd", "1234567890");
        Person me2 = new Person("Tyler", 0, "345 Street rd", "2345678901");
        Person me3 = new Person("Ethan", 2, "123 Street rd", "3456789012");
        Person me4 = new Person("Jeremy", 3, "456 Street rd", "0123456789");


        if(!people.add(me)) {
            println("Something went wrong");
            return false;
        }

        if(people.add(me1)) {
            println("Something went wrong");
            return false;
        } else if(people.add(me2)) {
            println("Something went wrong");
            return false;
        } else if(people.add(me3)) {
            println("Something went wrong");
            return false;
        } else if(people.add(me4)) {
            println("Something went wrong");
            return false;
        }

        Person newPerson = new Person("Ethan", 1, "987 Road blvd", "9876543210");

        if(!people.add(newPerson)) {
            println("Something went wrong");
            return false;
        }

        if(people.findByName("Hunter") != me) {
            println("Something went wrong");
            return false;
        } else if(people.findById(0) != me) {
            println("Something went wrong");
            return false;
        } else if(people.findByAddress("123 Street rd") != me) {
            println("Something went wrong");
            return false;
        } else if(people.findByPhone("0123456789") != me) {
            println("Something went wrong");
            return false;
        }

        if(people.findByName("Samantha") != null) {
            println("Something went wrong");
            return false;
        }

        println("Everything has seemed to go smoothly for People...");


        return true;
    }


}
