package com.zoarial;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Adler32;

public class Main {
    static Random random = new Random();
    static Maps maps;

    public static long curTime() {
        return System.currentTimeMillis();
    }

    public static String randString(int length) {
        final int leftLimit = 97; // letter 'a'
        final int rightLimit = 122; // letter 'z'
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
    public static String randNumberString(int length) {
        final int leftLimit = 48; // 0
        final int rightLimit = 57; // 9
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
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
         */
        if(!testStrictPeople() || !testPeople()) {
            //something went wrong
            return;
        }

        final int NUM_OF_PEOPLE = 50000;
        final int ROUNDS = 60;
        int concurrentFactor = 1;
        final int SINGLE_THREAD_OPTIMISE = 128;
        maps = new Maps(NUM_OF_PEOPLE);

        println("Working with " + NUM_OF_PEOPLE + " people and " + ROUNDS + " rounds.");
        println("Working...");
        println("Add StrictPeople average: " + addPeopleInterface(new StrictPeople(NUM_OF_PEOPLE, 0.75f), ROUNDS));
        println("Add People average (1 thread): " + addPeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, 1), 1));
        println("Add People average (" + SINGLE_THREAD_OPTIMISE + " threads): " + addPeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Add People2 average (1 thread): " + addPeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, 1), 1));
        println("Add People2 average (" + SINGLE_THREAD_OPTIMISE + " threads): " + addPeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Remove People average (1 thread): " + removePeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, 1), 1));
        println("Remove People average (" + SINGLE_THREAD_OPTIMISE + " threads): " + removePeopleInterface(new People(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));
        println("Remove People2 average (1 thread): " + removePeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, 1), 1));
        println("Remove People2 average (" + SINGLE_THREAD_OPTIMISE + " threads): " + removePeopleInterface(new People2(NUM_OF_PEOPLE, 0.75f, SINGLE_THREAD_OPTIMISE), ROUNDS));



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

    }



    static long concurrentAddPeopleInterface(PeopleInterface people, int times, int threads) {
        Thread[] threadsArr = new Thread[threads];
        final AtomicBoolean startFlag = new AtomicBoolean(false);
        final AtomicInteger threadCounter = new AtomicInteger(0);
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        for(int round = 0; round < times; round++) {
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();

            //Split up the work for the threads
            ArrayList<String>[] nameList =  new ArrayList[threads];
            ArrayList<Integer>[] idList =  new ArrayList[threads];
            ArrayList<String>[] addressList =  new ArrayList[threads];
            ArrayList<String>[] phoneList =  new ArrayList[threads];

            for(int i = 0; i < threads; i++) {
                nameList[i] = new ArrayList<>();
                idList[i] = new ArrayList<>();
                addressList[i] = new ArrayList<>();
                phoneList[i] = new ArrayList<>();
            }

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
            for (int i = 0; i < threads; i++) {
                int finalI = i;
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



                    // There is a race condition if threadCounter.incrementAndGet() is outside the synchronized block
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

                    for (int j = 0; j < threadArraySize; j++) {
                        people.add(new Person(threadNameIter.next(), threadIdIter.next(), threadAddrIter.next(), threadPhoneIter.next()));
                    }
                });
            }

            for (Thread t : threadsArr) {
                t.start();
            }

            oldTime = curTime();
            synchronized (threadCounter) {
                try {
                    threadCounter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (startFlag) {
                startFlag.notifyAll();
            }

            for (Thread t : threadsArr) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            time = curTime();
            total += time-oldTime;
        }
        return (total/times);


    }

    static long concurrentRemovePeopleInterface(PeopleInterface people, int times, int threads) {
        Thread[] threadsArr = new Thread[threads];
        final AtomicBoolean startFlag = new AtomicBoolean(false);
        final AtomicInteger threadCounter = new AtomicInteger(0);
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        for(int round = 0; round < times; round++) {
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();

            //Split up the work for the threads
            ArrayList<String>[] nameList =  new ArrayList[threads];
            ArrayList<Integer>[] idList =  new ArrayList[threads];
            ArrayList<String>[] addressList =  new ArrayList[threads];
            ArrayList<String>[] phoneList =  new ArrayList[threads];

            for(int i = 0; i < threads; i++) {
                nameList[i] = new ArrayList<>();
                idList[i] = new ArrayList<>();
                addressList[i] = new ArrayList<>();
                phoneList[i] = new ArrayList<>();
            }

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
            for (int i = 0; i < threads; i++) {
                int finalI = i;
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

                    for (int j = 0; j < threadArraySize; j++) {
                        people.add(new Person(threadNameIter.next(), threadIdIter.next(), threadAddrIter.next(), threadPhoneIter.next()));
                    }

                    // There is a race condition if threadCounter.incrementAndGet() is outside the synchronized block
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


                    threadNameIter = threadNameList.iterator();
                    threadIdIter = threadIdList.iterator();
                    threadAddrIter = threadAddressList.iterator();
                    threadPhoneIter = threadPhoneList.iterator();
                    for (int j = 0; j < threadArraySize; j++) {
                        people.remove(new Person(threadNameIter.next(), threadIdIter.next(), threadAddrIter.next(), threadPhoneIter.next()));
                    }
                });
            }

            for (Thread t : threadsArr) {
                t.start();
            }

            oldTime = curTime();
            synchronized (threadCounter) {
                try {
                    threadCounter.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (startFlag) {
                startFlag.notifyAll();
            }

            for (Thread t : threadsArr) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            time = curTime();
            total += time-oldTime;
        }
        return (total/times);

    }


    /*
     * Single threaded tests
     */

    static long addPeopleInterface(PeopleInterface people, int times) {
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        for(int outer = 0; outer < times; outer++) {
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();
            people.clear();

            oldTime = curTime();
            for (int i = 0; i < numOfPeople; i++) {
                people.add(new Person(nameArr[i], idArr[i], addrArr[i], phoneArr[i]));
            }
            time = curTime();
            total += time-oldTime;
        }

        return total/times;

    }

    static long removePeopleInterface(PeopleInterface people, int times) {
        long oldTime, time, total = 0;
        int numOfPeople = maps.getNUM_OF_PEOPLE();

        for(int outer = 0; outer < times; outer++) {
            String[] nameArr = maps.getNameRandArray();
            Integer[] idArr = maps.getIdRandArray();
            String[] addrArr = maps.getAddressRandArray();
            String[] phoneArr = maps.getPhoneRandArray();
            people.clear();

            for (int i = 0; i < numOfPeople; i++) {
                people.add(new Person(nameArr[i], idArr[i], addrArr[i], phoneArr[i]));
            }

            oldTime = curTime();
            for (int i = 0; i < numOfPeople; i++) {
                people.remove(new Person(nameArr[i], idArr[i], addrArr[i], phoneArr[i]));
            }
            time = curTime();
            total += time-oldTime;
            //println(time-oldTime);
        }
        return (total/times);
    }

    static class Maps {
        private Thread[] threads = new Thread[4];
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
