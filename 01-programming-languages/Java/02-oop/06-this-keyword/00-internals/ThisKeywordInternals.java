package academy.javaengineering.oop.internals;

public class ThisKeywordInternals {

    static class Person {
        String name;
        int age;

        Person(String name) {
            this.name = name; // this.name = field, name = parameter
        }

        Person(String name, int age) {
            this(name); // calls other constructor
            this.age = age;
        }

        void printInfo() {
            System.out.println("Name: " + this.name + ", Age: " + this.age);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 'this' Keyword Internals ===\n");

        // 1. Reference to Current Object
        System.out.println("--- Current Object Reference ---");
        Person p = new Person("Alice", 25);
        p.printInfo();
        System.out.println("this refers to current instance");

        // 2. Disambiguate Field vs Parameter
        System.out.println("\n--- Disambiguation ---");
        System.out.println("this.name = field");
        System.out.println("name = parameter/local");
        System.out.println("Resolves naming conflicts");

        // 3. Constructor Chaining
        System.out.println("\n--- Constructor Chaining ---");
        System.out.println("this() calls another constructor");
        System.out.println("Must be first statement");
        System.out.println("Avoids code duplication");

        // 4. Return Current Object
        System.out.println("\n--- Method Chaining ---");
        System.out.println("return this; enables fluent API");
        System.out.println("obj.method1().method2().method3()");
    }
}
