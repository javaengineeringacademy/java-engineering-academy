package academy.javaengineering.oop.internals;

public class ConstructorsInternals {

    static class Person {
        String name;
        int age;

        // Default constructor
        Person() {
            this.name = "Unknown";
            this.age = 0;
        }

        // Parameterized constructor
        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Copy constructor
        Person(Person other) {
            this.name = other.name;
            this.age = other.age;
        }
    }

    static class Student {
        String name;
        int age;
        String school;

        // Constructor overloading - different signatures
        Student() {
            this("Unknown", 0, "Unknown School");
        }

        Student(String name) {
            this(name, 18, "Unknown School");
        }

        Student(String name, int age) {
            this(name, age, "Unknown School");
        }

        Student(String name, int age, String school) {
            this.name = name;
            this.age = age;
            this.school = school;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Constructors Internals ===\n");

        // 1. Constructor Types
        System.out.println("--- Constructor Types ---");
        Person p1 = new Person();
        Person p2 = new Person("Alice", 25);
        Person p3 = new Person(p2);
        System.out.println("Default: " + p1.name);
        System.out.println("Parameterized: " + p2.name);
        System.out.println("Copy: " + p3.name);

        // 2. Constructor Overloading
        System.out.println("\n--- Constructor Overloading ---");
        Student s1 = new Student();
        Student s2 = new Student("Bob");
        Student s3 = new Student("Alice", 20);
        Student s4 = new Student("Charlie", 22, "MIT");
        System.out.println("s1: " + s1.name + ", " + s1.age);
        System.out.println("s2: " + s2.name + ", " + s2.age);
        System.out.println("s3: " + s3.name + ", " + s3.age);
        System.out.println("s4: " + s4.name + ", " + s4.school);
        System.out.println("Same name, different parameters");

        // 3. Constructor Overriding - NOT POSSIBLE
        System.out.println("\n--- Constructor Overriding ---");
        System.out.println("IMPORTANT: Constructors CANNOT be overridden!");
        System.out.println("Reason: Constructors are NOT inherited");
        System.out.println("Child class must define its own constructors");
        System.out.println("Can call parent constructor using super()");

        // 4. Constructor Chaining
        System.out.println("\n--- Constructor Chaining ---");
        System.out.println("this(): calls another constructor (same class)");
        System.out.println("super(): calls parent constructor");
        System.out.println("Must be first statement");

        // 5. Private Constructor
        System.out.println("\n--- Private Constructor ---");
        System.out.println("Prevents instantiation");
        System.out.println("Used in Singleton pattern");
        System.out.println("Used in utility classes");
    }
}
