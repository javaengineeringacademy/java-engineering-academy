package academy.javaengineering.oop.memory;

public class ValueObjectsMemory {

    record Point(int x, int y) {}
    record Money(String currency, long amount) {}

    public static void main(String[] args) {
        System.out.println("=== Value Objects Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Record Size
        System.out.println("--- Record Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Point p = new Point(1, 2);
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Point: " + (after - before) + " bytes");
        System.out.println("Header: 12 + x: 4 + y: 4 = 20 (padded to 24)");

        // 2. Immutability Memory
        System.out.println("\n--- Immutability Memory ---");
        System.out.println("No defensive copies needed");
        System.out.println("Safe to share references");
        System.out.println("Reduces memory allocations");

        // 3. Value vs Reference Equality
        System.out.println("\n--- Value Equality ---");
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("Structural equality - same values");
    }
}
