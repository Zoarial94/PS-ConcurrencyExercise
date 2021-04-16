package com.zoarial;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main {
    static Random random = new Random();

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
        StrictPeople everyone = new StrictPeople();
        Person me = new Person("Hunter", 0, "123 Street rd", "0123456789");

        Person me1 = new Person("Hunter", 1, "234 Street rd", "1234567890");
        Person me2 = new Person("Tyler", 0, "345 Street rd", "2345678901");
        Person me3 = new Person("Ethan", 2, "123 Street rd", "3456789012");
        Person me4 = new Person("Jeremy", 3, "456 Street rd", "0123456789");


        if(!everyone.add(me)) {
            println("Something went wrong");
            return;
        }

        if(everyone.add(me1)) {
            println("Something went wrong");
            return;
        } else if(everyone.add(me2)) {
            println("Something went wrong");
            return;
        } else if(everyone.add(me3)) {
            println("Something went wrong");
            return;
        } else if(everyone.add(me4)) {
            println("Something went wrong");
            return;
        }

        Person newPerson = new Person("Ethan", 1, "987 Road blvd", "9876543210");

        if(!everyone.add(newPerson)) {
            println("Something went wrong");
            return;
        }

        if(everyone.findByName("Hunter") != me) {
            println("Something went wrong");
            return;
        } else if(everyone.findById(0) != me) {
            println("Something went wrong");
            return;
        } else if(everyone.findByAddress("123 Street rd") != me) {
            println("Something went wrong");
            return;
        } else if(everyone.findByPhone("0123456789") != me) {
            println("Something went wrong");
            return;
        }

        if(everyone.findByName("Samantha") != null) {
            println("Something went wrong");
            return;
        }

        println("Everything has seemed to go smoothly...");

        final int NUM_OF_PEOPLE = 100000;
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

        long time = System.currentTimeMillis();
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
        long newTime = System.currentTimeMillis();
        println();
        println("Finished creating sets in: " + (newTime - time) + " milliseconds");

    }
}
