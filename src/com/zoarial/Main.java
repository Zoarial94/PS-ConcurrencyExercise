package com.zoarial;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Main {
    static Random random = new Random();

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

        StrictPeople strictPeople = new StrictPeople();
        People people = new People();

        final int NUM_OF_PEOPLE = 100000;
        final int CONCURRENT_FACTOR = 32;
        Thread[] threads = new Thread[4];

        HashSet<String> nameSet = new HashSet<>();
        threads[0] = new Thread(()-> {
            while(nameSet.size() < NUM_OF_PEOPLE) {
                nameSet.add(randString(8));
            }
        });

        HashSet<Integer> idSet = new HashSet<>();
        threads[1] = new Thread(()-> {
            for(int i = 0; i < NUM_OF_PEOPLE; i++) {
                idSet.add(i);
            }
        });

        HashSet<String> addressSet = new HashSet<>();
        threads[2] = new Thread(()-> {
            while(addressSet.size() < NUM_OF_PEOPLE) {
                addressSet.add(randString(32));
            }
        });

        HashSet<String> phoneSet = new HashSet<>();
        threads[3] = new Thread(()-> {
            while(phoneSet.size() < NUM_OF_PEOPLE) {
                phoneSet.add(randNumberString(10));
            }
        });

        long oldTime = curTime();
        println("Starting threads...");
        for(Thread t : threads) {
            t.start();
        }


        print("Waiting for threads to finish");
        for(Thread t : threads) {
            try {
                t.join();
                print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long time = curTime();
        println();
        println("Finished creating " + NUM_OF_PEOPLE + " sets in: " + (time - oldTime) + " milliseconds");


        Iterator<String> nameIter;
        Iterator<Integer> idIter;
        Iterator<String> addrIter;
        Iterator<String> phoneIter;

        // Test StrictPeople
        strictPeople = new StrictPeople((int)(NUM_OF_PEOPLE*1.5), 0.75f);
        nameIter = nameSet.iterator();
        idIter = idSet.iterator();
        addrIter = addressSet.iterator();
        phoneIter = phoneSet.iterator();

        oldTime = curTime();
        for(int i = 0; i < NUM_OF_PEOPLE; i++) {
            strictPeople.add(new Person(nameIter.next(), idIter.next(), addrIter.next(), phoneIter.next()));
        }
        time = curTime();
        println("Time to add to StrictPeople: " + (time-oldTime));


        // Test People
        people.close();
        people = new People((int)(NUM_OF_PEOPLE*1.5), 0.75f, CONCURRENT_FACTOR);
        nameIter = nameSet.iterator();
        idIter = idSet.iterator();
        addrIter = addressSet.iterator();
        phoneIter = phoneSet.iterator();
        oldTime = curTime();
        for(int i = 0; i < NUM_OF_PEOPLE; i++) {
            people.add(new Person(nameIter.next(), idIter.next(), addrIter.next(), phoneIter.next()), true);
        }
        time = curTime();
        println("Time to add to People: " + (time-oldTime));

        // Test StrictPeople
        nameIter = nameSet.iterator();
        idIter = idSet.iterator();
        addrIter = addressSet.iterator();
        phoneIter = phoneSet.iterator();

        oldTime = curTime();
        for(int i = 0; i < NUM_OF_PEOPLE; i++) {
            strictPeople.remove(new Person(nameIter.next(), idIter.next(), addrIter.next(), phoneIter.next()));
        }
        time = curTime();
        println("Time to remove from StrictPeople: " + (time-oldTime));


        // Test People
        nameIter = nameSet.iterator();
        idIter = idSet.iterator();
        addrIter = addressSet.iterator();
        phoneIter = phoneSet.iterator();
        oldTime = curTime();
        for(int i = 0; i < NUM_OF_PEOPLE; i++) {
            people.remove(new Person(nameIter.next(), idIter.next(), addrIter.next(), phoneIter.next()));
        }
        time = curTime();
        println("Time to add remove from People: " + (time-oldTime));

        people.close();

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


        if(!people.add(me, true)) {
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
