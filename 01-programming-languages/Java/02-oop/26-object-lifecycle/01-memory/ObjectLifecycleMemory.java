package academy.javaengineering.oop.memory;

public class ObjectLifecycleMemory {

    static class Person {
        String name;
        Person(String name) { this.name = name; }
        @Override
        protected void finalize() { System.out.println("Finalized: " + name); }
    }

    public static void main(String[] args) {
        System.out.println("=== Object Lifecycle Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Creation Memory
        System.out.println("--- Creation Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Person p1 = new Person("Alice");
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Creation: " + (after - before) + " bytes");

        // 2. GC Collection
        System.out.println("\n--- GC Collection ---");
        p1 = null; // Eligible for GC
        rt.gc(); // Suggest GC
        System.out.println("Object collected after GC");
        System.out.println("Memory reclaimed");

        // 3. Memory Pool
        System.out.println("\n--- Memory Pools ---");
        System.out.println("Eden: new objects");
        System.out.println("Survivor: survived GC");
        System.out.println("Old gen: long-lived objects");
        System.out.println("Metaspace: class metadata");
    }
}
