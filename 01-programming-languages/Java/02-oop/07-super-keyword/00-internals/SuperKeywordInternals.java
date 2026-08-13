package academy.javaengineering.oop.internals;

public class SuperKeywordInternals {

    static class Animal {
        String type;

        Animal(String type) {
            this.type = type;
        }

        void eat() {
            System.out.println(type + " is eating");
        }
    }

    static class Dog extends Animal {
        String breed;

        Dog(String type, String breed) {
            super(type); // call parent constructor
            this.breed = breed;
        }

        @Override
        void eat() {
            super.eat(); // call parent method
            System.out.println("Dog is chewing");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 'super' Keyword Internals ===\n");

        // 1. Call Parent Constructor
        System.out.println("--- Parent Constructor ---");
        Dog dog = new Dog("Mammal", "Labrador");
        System.out.println("Type: " + dog.type);
        System.out.println("Breed: " + dog.breed);

        // 2. Call Parent Method
        System.out.println("\n--- Parent Method ---");
        dog.eat();

        // 3. Access Parent Field
        System.out.println("\n--- Parent Field ---");
        System.out.println("super.field accesses parent field");
        System.out.println("Useful when child has same field name");

        // 4. super vs this
        System.out.println("\n--- super vs this ---");
        System.out.println("this: current class reference");
        System.out.println("super: parent class reference");
        System.out.println("super must be first in constructor");
    }
}
