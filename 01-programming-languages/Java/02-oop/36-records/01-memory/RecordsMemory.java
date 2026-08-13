package academy.javaengineering.oop.memory;

public class RecordsMemory {

    record Point(int x, int y) {}
    record Person(String name, int age) {}

    public static void main(String[] args) {
        System.out.println("=== Records Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Record Object Size
        System.out.println("--- Record Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Point p = new Point(1, 2);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Point record: " + (after - before) + " bytes");
        System.out.println("Header: 12 + x: 4 + y: 4 = 20 (padded to 24)");

        // 2. Record vs Class Memory
        System.out.println("\n--- Record vs Class ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        Person person = new Person("Alice", 25);
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Person record: " + (after - before) + " bytes");
        System.out.println("Same as equivalent class");

        // 3. Immutability Benefits
        System.out.println("\n--- Immutability Benefits ---");
        System.out.println("No defensive copies");
        System.out.println("Safe for concurrent access");
        System.out.println("Can be shared freely");
    }
}
