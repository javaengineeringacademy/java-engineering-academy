package academy.javaengineering.oop.memory;

public class ThisKeywordMemory {

    static class Person {
        String name;
        Person(String name) { this.name = name; }
        Person() { this("Unknown"); }
    }

    public static void main(String[] args) {
        System.out.println("=== 'this' Keyword Memory ===\n");

        // 1. 'this' is Not an Object
        System.out.println("--- 'this' is Reference ---");
        System.out.println("this is a reference to current object");
        System.out.println("Not a new object, just a pointer");
        System.out.println("Same memory as the object");

        // 2. Constructor Chaining Memory
        System.out.println("\n--- Constructor Chaining ---");
        System.out.println("this() reuses same object");
        System.out.println("No extra memory allocation");
        System.out.println("Just different initialization");

        // 3. Method Chaining Memory
        System.out.println("\n--- Method Chaining ---");
        System.out.println("return this; returns same reference");
        System.out.println("No new object created");
        System.out.println("Efficient for fluent APIs");
    }
}
