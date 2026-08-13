package academy.javaengineering.oop.memory;

public class ObjectsMemory {

    static class Person {
        String name;
        int age;
        double salary;
    }

    public static void main(String[] args) {
        System.out.println("=== Objects Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object Size Calculation
        System.out.println("--- Object Size ---");
        System.out.println("Person object:");
        System.out.println("  Header: 12 bytes");
        System.out.println("  name (ref): 8 bytes");
        System.out.println("  age (int): 4 bytes");
        System.out.println("  salary (double): 8 bytes");
        System.out.println("  Total: 32 bytes (with padding)");

        // 2. Reference Counting
        System.out.println("\n--- Reference Counting ---");
        Person p1 = new Person();
        Person p2 = p1;
        System.out.println("p1 references: 1");
        System.out.println("p2 references: 2 (same object)");
        p1 = null;
        System.out.println("After p1=null: still 1 reference (p2)");

        // 3. Heap vs Stack
        System.out.println("\n--- Heap vs Stack ---");
        System.out.println("Stack: method calls, local variables");
        System.out.println("Heap: objects, instance variables");
        System.out.println("Stack: fast, automatic cleanup");
        System.out.println("Heap: flexible, GC managed");

        // 4. Memory Leak Scenarios
        System.out.println("\n--- Memory Leak Scenarios ---");
        System.out.println("1. Unclosed resources");
        System.out.println("2. Static collections growing");
        System.out.println("3. Inner classes holding outer reference");
        System.out.println("4. Circular references (rare in Java)");
    }
}
