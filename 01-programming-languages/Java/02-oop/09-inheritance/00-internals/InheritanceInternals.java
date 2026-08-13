package academy.javaengineering.oop.internals;

public class InheritanceInternals {

    static class Animal {
        String name;

        void eat() {
            System.out.println(name + " is eating");
        }

        void sleep() {
            System.out.println(name + " is sleeping");
        }
    }

    static class Dog extends Animal {
        void bark() {
            System.out.println(name + " is barking");
        }

        @Override
        void eat() {
            System.out.println(name + " is chewing bones");
        }
    }

    static class Cat extends Animal {
        void meow() {
            System.out.println(name + " is meowing");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Inheritance Internals ===\n");

        // 1. IS-A Relationship
        System.out.println("--- IS-A Relationship ---");
        Dog dog = new Dog();
        dog.name = "Buddy";
        dog.eat();   // From Animal (overridden)
        dog.sleep(); // From Animal
        dog.bark();  // From Dog
        System.out.println("Dog IS-A Animal");

        // 2. Single Inheritance
        System.out.println("\n--- Single Inheritance ---");
        System.out.println("Java: single inheritance only");
        System.out.println("Class can extend only one class");
        System.out.println("Use interfaces for multiple inheritance");

        // 3. Method Overriding
        System.out.println("\n--- Method Overriding ---");
        System.out.println("Child can override parent method");
        System.out.println("@Override annotation recommended");
        System.out.println("Runtime decides which method to call");

        // 4. Constructor Chaining
        System.out.println("\n--- Constructor Chaining ---");
        System.out.println("super() called implicitly");
        System.out.println("Parent constructor runs first");
    }
}
