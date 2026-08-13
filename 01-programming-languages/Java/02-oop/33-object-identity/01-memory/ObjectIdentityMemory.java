package academy.javaengineering.oop.memory;

public class ObjectIdentityMemory {

    static class Person {
        String name;
        Person(String name) { this.name = name; }
    }

    public static void main(String[] args) {
        System.out.println("=== Object Identity Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Identity Memory
        System.out.println("--- Identity Memory ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Person p1 = new Person("Alice");
        Person p2 = p1;
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Identity: " + (after - before) + " bytes");
        System.out.println("p2 references same object as p1");

        // 2. String Pool Identity
        System.out.println("\n--- String Pool Identity ---");
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        System.out.println("s1 == s2: " + (s1 == s2)); // true (pool)
        System.out.println("s1 == s3: " + (s1 == s3)); // false (new object)

        // 3. Identity Hash
        System.out.println("\n--- Identity Hash ---");
        System.out.println("System.identityHashCode(obj)");
        System.out.println("Based on memory address");
        System.out.println("Unique per object");
    }
}
