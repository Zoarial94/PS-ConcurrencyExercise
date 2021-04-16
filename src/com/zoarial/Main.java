package com.zoarial;

public class Main {

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

    }
}
