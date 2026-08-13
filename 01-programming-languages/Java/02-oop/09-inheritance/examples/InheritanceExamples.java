package academy.javaengineering.oop.examples;

/**
 * Inheritance Examples - Why inheritance exists and when to use it.
 * 
 * WHY INHERITANCE EXISTS:
 * - Code reuse: Subclass inherits parent's fields and methods
 * - Polymorphism: Treat subclasses as parent type
 * - Hierarchy: Models "is-a" relationships
 * 
 * TRADE-OFFS:
 * - Pros: Code reuse, polymorphism
 * - Cons: Tight coupling, fragile base class problem
 * 
 * ENGINEERING DECISION: Prefer composition over inheritance.
 */
public class InheritanceExamples {

    public static void main(String[] args) {
        System.out.println("=== Inheritance Examples ===\n");

        // Example 1: Basic Inheritance
        example1_BasicInheritance();

        // Example 2: Method Overriding
        example2_MethodOverriding();

        // Example 3: Super Keyword
        example3_SuperKeyword();
    }

    /**
     * WHY: Inheritance enables code reuse.
     * 
     * ENGINEERING DECISION: Use inheritance for "is-a" relationships only.
     */
    private static void example1_BasicInheritance() {
        System.out.println("--- Example 1: Basic Inheritance ---");

        Dog dog = new Dog("Buddy", "Golden Retriever");
        Cat cat = new Cat("Whiskers", "Persian");

        dog.eat();
        dog.bark();

        cat.eat();
        cat.purr();
    }

    /**
     * WHY: Method overriding allows customization.
     * 
     * ENGINEERING DECISION: Override methods to customize behavior.
     */
    private static void example2_MethodOverriding() {
        System.out.println("\n--- Example 2: Method Overriding ---");

        Animal animal = new Dog("Rex", "German Shepherd");
        animal.makeSound(); // Calls Dog's makeSound()
    }

    /**
     * WHY: Super keyword accesses parent class members.
     * 
     * ENGINEERING DECISION: Use super to invoke parent constructor or methods.
     */
    private static void example3_SuperKeyword() {
        System.out.println("\n--- Example 3: Super Keyword ---");

        Dog dog = new Dog("Max", "Labrador");
        dog.displayInfo();
    }

    // Supporting classes
    static class Animal {
        protected String name;

        public Animal(String name) {
            this.name = name;
        }

        public void eat() {
            System.out.println(name + " is eating");
        }

        public void makeSound() {
            System.out.println(name + " makes a sound");
        }
    }

    static class Dog extends Animal {
        private String breed;

        public Dog(String name, String breed) {
            super(name);
            this.breed = breed;
        }

        public void bark() {
            System.out.println(name + " barks!");
        }

        @Override
        public void makeSound() {
            System.out.println(name + " barks!");
        }

        public void displayInfo() {
            System.out.println("Name: " + name + ", Breed: " + breed);
        }
    }

    static class Cat extends Animal {
        private String breed;

        public Cat(String name, String breed) {
            super(name);
            this.breed = breed;
        }

        public void purr() {
            System.out.println(name + " purrs...");
        }

        @Override
        public void makeSound() {
            System.out.println(name + " meows!");
        }
    }
}
