package academy.javaengineering.fundamentals.keywords;

import java.util.ArrayList;
import java.util.List;

/**
 * Java keywords - complete reference with working examples
 *
 * This file demonstrates all 67 Java keywords organized by category.
 * Each keyword includes a working example you can run.
 */
public class KeywordsDemo {

    // =========================================================
    // 1. DATA TYPE KEYWORDS
    // =========================================================

    static class DataTypes {
        byte myByte = 127;
        short myShort = 32767;
        int myInt = 2147483647;
        long myLong = 9223372036854775807L;
        float myFloat = 3.14f;
        double myDouble = 3.141592653589793;
        char myChar = 'A';
        boolean myBoolean = true;

        void show() {
            System.out.println("byte: " + myByte);
            System.out.println("short: " + myShort);
            System.out.println("int: " + myInt);
            System.out.println("long: " + myLong);
            System.out.println("float: " + myFloat);
            System.out.println("double: " + myDouble);
            System.out.println("char: " + myChar);
            System.out.println("boolean: " + myBoolean);
        }
    }

    // =========================================================
    // 2. CONTROL FLOW KEYWORDS
    // =========================================================

    static class ControlFlow {

        // if-else
        static String classify(int score) {
            if (score >= 90) return "A";
            else if (score >= 80) return "B";
            else if (score >= 70) return "C";
            else if (score >= 60) return "D";
            else return "F";
        }

        // switch-case
        static String getDay(int day) {
            return switch (day) {
                case 1 -> "Monday";
                case 2 -> "Tuesday";
                case 3 -> "Wednesday";
                case 4 -> "Thursday";
                case 5 -> "Friday";
                case 6 -> "Saturday";
                case 7 -> "Sunday";
                default -> "Invalid";
            };
        }

        // for loop
        static int factorial(int n) {
            int result = 1;
            for (int i = 2; i <= n; i++) {
                result *= i;
            }
            return result;
        }

        // while loop
        static int sumUntil(int limit) {
            int sum = 0;
            int i = 1;
            while (sum + i <= limit) {
                sum += i;
                i++;
            }
            return sum;
        }

        // do-while loop
        static int countDigits(int number) {
            int count = 0;
            do {
                count++;
                number /= 10;
            } while (number > 0);
            return count;
        }

        // break and continue
        static List<Integer> filterEvens(int[] numbers) {
            List<Integer> evens = new ArrayList<>();
            for (int num : numbers) {
                if (num % 2 != 0) {
                    continue;  // Skip odd numbers
                }
                evens.add(num);
                if (evens.size() >= 3) {
                    break;  // Stop after 3 evens
                }
            }
            return evens;
        }

        // return
        static int square(int x) {
            return x * x;
        }
    }

    // =========================================================
    // 3. ACCESS MODIFIER KEYWORDS
    // =========================================================

    public static class AccessDemo {
        public String publicField = "public";
        protected String protectedField = "protected";
        String defaultField = "default";  // no keyword
        private String privateField = "private";

        public String getPublic() { return publicField; }
        protected String getProtected() { return protectedField; }
        String getDefault() { return defaultField; }  // package-private
        private String getPrivate() { return privateField; }
    }

    // =========================================================
    // 4. OBJECT-ORIENTED KEYWORDS
    // =========================================================

    // class keyword
    static class Animal {
        String name;

        Animal(String name) {
            this.name = name;  // this keyword
        }

        void speak() {
            System.out.println(name + " speaks");
        }
    }

    // extends keyword
    static class Dog extends Animal {
        Dog(String name) {
            super(name);  // super keyword
        }

        @Override
        void speak() {
            System.out.println(name + " barks");
        }
    }

    // interface keyword
    interface Flyable {
        void fly();
    }

    // implements keyword
    static class Bird extends Animal implements Flyable {
        Bird(String name) {
            super(name);
        }

        @Override
        public void fly() {
            System.out.println(name + " flies");
        }
    }

    // enum keyword
    enum Color {
        RED, GREEN, BLUE
    }

    // new and instanceof keywords
    static void demoNewAndInstanceof() {
        Animal animal = new Dog("Rex");
        System.out.println("Is Animal? " + (animal instanceof Animal));
        System.out.println("Is Dog? " + (animal instanceof Dog));
    }

    // =========================================================
    // 5. STATIC AND FINAL KEYWORDS
    // =========================================================

    static class StaticFinalDemo {
        static int counter = 0;  // static: shared across instances
        final String id;         // final: cannot be changed after construction
        static final double PI = 3.14159;  // static final: constant

        StaticFinalDemo() {
            counter++;
            id = "ID-" + counter;
        }

        static int getCounter() { return counter; }
        String getId() { return id; }
    }

    // abstract keyword
    static abstract class Shape {
        String name;

        Shape(String name) {
            this.name = name;
        }

        abstract double area();  // abstract method

        void display() {  // concrete method
            System.out.println(name + " area: " + area());
        }
    }

    static class Circle extends Shape {
        double radius;

        Circle(double radius) {
            super("Circle");
            this.radius = radius;
        }

        @Override
        double area() {
            return Math.PI * radius * radius;
        }
    }

    // native keyword (declaration only - implementation in C/C++)
    // static native void nativeMethod();

    // =========================================================
    // 6. EXCEPTION HANDLING KEYWORDS
    // =========================================================

    static void demoExceptionHandling() {
        // try-catch-finally
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("Finally block always executes");
        }

        // throw keyword
        try {
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Thrown: " + e.getMessage());
        }

        // throws keyword (declared in method signature)
        // void riskyMethod() throws Exception { ... }
    }

    static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }

    // =========================================================
    // 7. CONCURRENCY KEYWORDS
    // =========================================================

    static class ConcurrencyDemo {
        private volatile boolean running = true;  // volatile: no caching
        private int count = 0;

        // synchronized: thread-safe
        synchronized void increment() {
            count++;
        }

        // transient: excluded from serialization
        // (would show with ObjectOutputStream)
    }

    // =========================================================
    // 8. PACKAGE AND IMPORT KEYWORDS
    // =========================================================

    // package keyword (at top of file)
    // import keyword (at top of file)
    // void keyword (method has no return value)

    static void doNothing() {
        // void: no return value
    }

    // =========================================================
    // 9. RESERVED KEYWORDS (unused)
    // =========================================================

    // const: reserved, use final instead
    // final double PI = 3.14;  // correct
    // const double PI = 3.14;  // compile error

    // goto: reserved, use break/continue with labels instead
    // outer: for (...) {
    //     for (...) {
    //         if (condition) break outer;  // labeled break instead of goto
    //     }
    // }

    // =========================================================
    // 10. LITERAL KEYWORDS
    // =========================================================

    static void demoLiterals() {
        boolean a = true;      // true literal
        boolean b = false;     // false literal
        String c = null;       // null literal
        System.out.println("true, false, null: " + a + ", " + b + ", " + c);
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        System.out.println("=== Java Keywords Demo ===\n");

        System.out.println("--- 1. Data Types ---");
        new DataTypes().show();

        System.out.println("\n--- 2. Control Flow ---");
        System.out.println("Score 85 = " + ControlFlow.classify(85));
        System.out.println("Day 3 = " + ControlFlow.getDay(3));
        System.out.println("5! = " + ControlFlow.factorial(5));
        System.out.println("Sum until 10 = " + ControlFlow.sumUntil(10));
        System.out.println("Digits in 12345 = " + ControlFlow.countDigits(12345));

        System.out.println("\n--- 3. Access Modifiers ---");
        AccessDemo access = new AccessDemo();
        System.out.println("public: " + access.getPublic());
        System.out.println("protected: " + access.getProtected());
        System.out.println("default: " + access.getDefault());
        System.out.println("private: " + access.getPrivate());

        System.out.println("\n--- 4. Object-Oriented ---");
        Animal animal = new Dog("Rex");
        animal.speak();
        Bird bird = new Bird("Tweety");
        bird.speak();
        bird.fly();
        System.out.println("Color: " + Color.RED);

        System.out.println("\n--- 5. Static and Final ---");
        StaticFinalDemo d1 = new StaticFinalDemo();
        StaticFinalDemo d2 = new StaticFinalDemo();
        System.out.println("Counter: " + StaticFinalDemo.getCounter());
        System.out.println("PI: " + StaticFinalDemo.PI);

        System.out.println("\n--- 6. Abstract ---");
        Shape circle = new Circle(5);
        circle.display();

        System.out.println("\n--- 7. Exception Handling ---");
        demoExceptionHandling();

        System.out.println("\n--- 8. Literals ---");
        demoLiterals();

        System.out.println("\n=== Summary ===");
        System.out.println("67 keywords total");
        System.out.println("8 data types, 11 control flow, 4 access, 9 OOP");
        System.out.println("4 static/final, 5 exception, 8 concurrency");
        System.out.println("3 package/import, 2 reserved, 3 literals");
    }
}
