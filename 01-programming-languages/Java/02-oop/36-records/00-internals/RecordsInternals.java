package academy.javaengineering.oop.internals;

public class RecordsInternals {

    record Point(int x, int y) {}
    record Person(String name, int age, String email) {}
    record Range(int start, int end) {
        boolean contains(int value) {
            return value >= start && value <= end;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Records Internals ===\n");

        // 1. Record Basics
        System.out.println("--- Record Basics ---");
        Point p = new Point(1, 2);
        System.out.println("Point: " + p);
        System.out.println("x: " + p.x() + ", y: " + p.y());

        // 2. Auto-Generated Methods
        System.out.println("\n--- Auto-Generated ---");
        Person person = new Person("Alice", 25, "alice@email.com");
        System.out.println("toString: " + person);
        System.out.println("equals: " + person.equals(new Person("Alice", 25, "alice@email.com")));
        System.out.println("hashCode: " + person.hashCode());

        // 3. Custom Methods
        System.out.println("\n--- Custom Methods ---");
        Range range = new Range(1, 10);
        System.out.println("contains(5): " + range.contains(5));
        System.out.println("contains(15): " + range.contains(15));

        // 4. Benefits
        System.out.println("\n--- Benefits ---");
        System.out.println("1. Immutable by default");
        System.out.println("2. Structural equality");
        System.out.println("3. Concise syntax");
        System.out.println("4. Serialization support");
    }
}
