package academy.javaengineering.oop.internals;

public class DynamicBindingInternals {

    static class Animal {
        void makeSound() {
            System.out.println("Animal makes sound");
        }
    }

    static class Dog extends Animal {
        @Override
        void makeSound() {
            System.out.println("Dog barks");
        }
    }

    static class Cat extends Animal {
        @Override
        void makeSound() {
            System.out.println("Cat meows");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Dynamic Binding Internals ===\n");

        // 1. Runtime Method Resolution
        System.out.println("--- Runtime Resolution ---");
        Animal animal = getAnimal();
        animal.makeSound(); // JVM decides at runtime
        System.out.println("Method called based on actual object type");

        // 2. Dynamic Binding Process
        System.out.println("\n--- Dynamic Binding Process ---");
        System.out.println("1. JVM looks at actual object type");
        System.out.println("2. Searches method table in class");
        System.out.println("3. Calls matching method");
        System.out.println("4. Falls back to parent if not found");

        // 3. Static vs Dynamic
        System.out.println("\n--- Static vs Dynamic ---");
        System.out.println("Static: compile-time (overloading)");
        System.out.println("Dynamic: runtime (overriding)");
        System.out.println("Dynamic: more flexible");
    }

    static Animal getAnimal() {
        return Math.random() > 0.5 ? new Dog() : new Cat();
    }
}
