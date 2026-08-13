package academy.javaengineering.oop.internals;

public class MethodOverridingInternals {

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
        System.out.println("=== Method Overriding Internals ===\n");

        // 1. Runtime Polymorphism
        System.out.println("--- Runtime Polymorphism ---");
        Animal animal1 = new Dog();
        Animal animal2 = new Cat();
        animal1.makeSound();
        animal2.makeSound();
        System.out.println("JVM decides at runtime");

        // 2. Overriding Rules
        System.out.println("\n--- Overriding Rules ---");
        System.out.println("1. Same method name");
        System.out.println("2. Same parameter types");
        System.out.println("3. Same or covariant return type");
        System.out.println("4. Access not more restrictive");

        // 3. @Override Annotation
        System.out.println("\n--- @Override ---");
        System.out.println("Compile-time check");
        System.out.println("Prevents accidental overloading");
        System.out.println("Recommended for clarity");
    }
}
