package academy.javaengineering.oop.memory;

public class ImmutableObjectsMemory {

    static final class Person {
        private final String name;
        private final int age;
        Person(String name, int age) { this.name = name; this.age = age; }
        public String getName() { return name; }
        public int getAge() { return age; }
    }

    public static void main(String[] args) {
        System.out.println("=== Immutable Objects Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Immutable Object Size
        System.out.println("--- Immutable Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Person p = new Person("Alice", 25);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Person: " + (after - before) + " bytes");
        System.out.println("Same as mutable object");

        // 2. No Defensive Copy Overhead
        System.out.println("\n--- No Defensive Copy ---");
        System.out.println("Immutable: no need to copy");
        System.out.println("Safe to share references");
        System.out.println("Saves memory vs mutable");

        // 3. Thread Safety
        System.out.println("\n--- Thread Safety ---");
        System.out.println("No synchronization needed");
        System.out.println("No memory visibility issues");
        System.out.println("Zero cost thread safety");
    }
}
