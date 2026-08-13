package academy.javaengineering.oop.memory;

public class InheritanceMemory {

    static class Animal {
        String name;
        void eat() { System.out.println("Eating"); }
    }

    static class Dog extends Animal {
        void bark() { System.out.println("Barking"); }
    }

    public static void main(String[] args) {
        System.out.println("=== Inheritance Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object Size with Inheritance
        System.out.println("--- Inheritance Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Dog dog = new Dog();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Dog object: " + (after - before) + " bytes");
        System.out.println("Contains: Animal fields + Dog fields");

        // 2. Method Table Memory
        System.out.println("\n--- Method Table ---");
        System.out.println("Each class has a method table");
        System.out.println("Dog table: Animal methods + Dog methods");
        System.out.println("Stored in Metaspace, not heap");

        // 3. Virtual Dispatch
        System.out.println("\n--- Virtual Dispatch ---");
        System.out.println("Method call: lookup in method table");
        System.out.println("Cost: ~2-3 cycles");
        System.out.println("JIT may devirtualize for performance");

        // 4. Memory Layout
        System.out.println("\n--- Memory Layout ---");
        System.out.println("Dog object layout:");
        System.out.println("  Header: 12 bytes");
        System.out.println("  Animal.name: 8 bytes");
        System.out.println("  Total: ~20 bytes (padded to 24)");
    }
}
