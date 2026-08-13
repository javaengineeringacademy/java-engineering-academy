package academy.javaengineering.oop.memory;

public class ConstructorsMemory {

    static class Person {
        String name;
        int age;

        Person() { this.name = "Unknown"; this.age = 0; }
        Person(String name, int age) { this.name = name; this.age = age; }
    }

    public static void main(String[] args) {
        System.out.println("=== Constructors Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Constructor Call Memory
        System.out.println("--- Constructor Call ---");
        System.out.println("1. Memory allocated for object");
        System.out.println("2. Constructor called on allocated memory");
        System.out.println("3. Fields initialized");
        System.out.println("4. Reference returned");

        // 2. Constructor Chaining Cost
        System.out.println("\n--- Constructor Chaining ---");
        System.out.println("this(): same object, different initialization");
        System.out.println("super(): allocates parent part first");
        System.out.println("No extra memory for chaining");

        // 3. Default vs Parameterized
        System.out.println("\n--- Default vs Parameterized ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Person p1 = new Person();
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Default: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Person p2 = new Person("Alice", 25);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Parameterized: " + (after - before) + " bytes");
        System.out.println("Same size - constructor doesn't affect size");
    }
}
