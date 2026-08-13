package academy.javaengineering.oop.internals;

public class ValueObjectsInternals {

    record Point(int x, int y) {}
    record Money(String currency, long amount) {}

    public static void main(String[] args) {
        System.out.println("=== Value Objects Internals ===\n");

        // 1. Record as Value Object
        System.out.println("--- Record Value Object ---");
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        System.out.println("p1: " + p1);
        System.out.println("p2: " + p2);
        System.out.println("p1.equals(p2): " + p1.equals(p2));

        // 2. Characteristics
        System.out.println("\n--- Characteristics ---");
        System.out.println("1. Immutable");
        System.out.println("2. Identity-free");
        System.out.println("3. Structural equality");
        System.out.println("4. Self-documenting");

        // 3. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("Thread-safe without synchronization");
        System.out.println("Safe for HashMap keys");
        System.out.println("Easy to reason about");
    }
}
