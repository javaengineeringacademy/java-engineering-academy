package academy.javaengineering.oop.internals;

public class AnonymousClassesInternals {

    interface Greeting {
        void greet(String name);
    }

    abstract static class Animal {
        abstract void makeSound();
    }

    public static void main(String[] args) {
        System.out.println("=== Anonymous Classes Internals ===\n");

        // 1. Anonymous Class Implementation
        System.out.println("--- Anonymous Class ---");
        Greeting greeting = new Greeting() {
            @Override
            public void greet(String name) {
                System.out.println("Hello, " + name + "!");
            }
        };
        greeting.greet("Alice");

        // 2. Anonymous Class Extension
        System.out.println("\n--- Anonymous Extension ---");
        Animal dog = new Animal() {
            @Override
            void makeSound() {
                System.out.println("Woof!");
            }
        };
        dog.makeSound();

        // 3. When to Use
        System.out.println("\n--- When to Use ---");
        System.out.println("1. One-time implementation");
        System.out.println("2. Short callbacks");
        System.out.println("3. Before lambdas existed");
        System.out.println("Use lambda for single-method interfaces");
    }
}
