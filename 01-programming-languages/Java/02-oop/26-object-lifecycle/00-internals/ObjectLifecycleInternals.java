package academy.javaengineering.oop.internals;

public class ObjectLifecycleInternals {

    static class Person {
        String name;

        Person(String name) {
            this.name = name;
            System.out.println("1. Constructor: Object created");
        }

        void initialize() {
            System.out.println("2. Initialization: Object initialized");
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                System.out.println("4. Finalize: Object being garbage collected");
            } finally {
                super.finalize();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Object Lifecycle Internals ===\n");

        // 1. Object Creation
        System.out.println("--- Object Creation ---");
        Person person = new Person("Alice");
        person.initialize();

        // 2. Usage
        System.out.println("\n--- Object Usage ---");
        System.out.println("3. Usage: Object being used");
        System.out.println("Name: " + person.name);

        // 3. Eligible for GC
        System.out.println("\n--- Eligible for GC ---");
        person = null; // Now eligible
        System.out.println("Reference set to null");
        System.out.println("Object eligible for garbage collection");

        // 4. Lifecycle Stages
        System.out.println("\n--- Lifecycle Stages ---");
        System.out.println("1. Creation (constructor)");
        System.out.println("2. Initialization");
        System.out.println("3. Usage");
        System.out.println("4. Finalization (before GC)");
        System.out.println("5. Garbage Collection");
    }
}
