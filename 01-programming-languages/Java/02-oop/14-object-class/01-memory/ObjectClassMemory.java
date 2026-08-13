package academy.javaengineering.oop.memory;

public class ObjectClassMemory {

    static class Person {
        String name;
        int age;
        Person(String name, int age) { this.name = name; this.age = age; }
    }

    public static void main(String[] args) {
        System.out.println("=== Object Class Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Object Header
        System.out.println("--- Object Header ---");
        System.out.println("Mark word: 8 bytes (hashCode, GC age)");
        System.out.println("Klass pointer: 4 bytes (compressed)");
        System.out.println("Total header: 12 bytes");

        // 2. Object Size
        System.out.println("\n--- Object Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Person p = new Person("Alice", 25);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Person: " + (after - before) + " bytes");
        System.out.println("Header: 12 + name: 8 + age: 4 = 24 (padded)");

        // 3. hashCode() Memory
        System.out.println("\n--- hashCode() Memory ---");
        System.out.println("Stored in object header");
        System.out.println("No extra memory");
        System.out.println("Computed once, cached");

        // 4. toString() Memory
        System.out.println("\n--- toString() Memory ---");
        System.out.println("Returns String object");
        System.out.println("Creates new String each call");
        System.out.println("Use StringBuilder for efficiency");
    }
}
