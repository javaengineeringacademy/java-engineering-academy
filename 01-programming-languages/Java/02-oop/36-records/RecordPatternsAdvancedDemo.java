import java.util.*;

/**
 * Record Patterns Advanced (Java 21)
 *
 * Record patterns allow destructuring records directly in patterns.
 * They work with nested records, enabling deep deconstruction.
 *
 * Features:
 * - Basic record patterns: case Point(int x, int y)
 * - Nested record patterns: case Line(Point start, Point end)
 * - Type inference in patterns
 * - Generic record patterns
 *
 * Expected output:
 * === Basic Record Patterns ===
 * Point at (3, 4)
 * Point at (0, 0)
 *
 * === Nested Record Patterns ===
 * Line from (0,0) to (5,5) has length 7.07
 * Nested: Container with Point(1, 2)
 *
 * === Generic Record Patterns ===
 * Pair: (Hello, 42)
 * First: Hello, Second: 42
 *
 * === Complex Destructuring ===
 * Person: Alice, Age: 30, Address: Street=123 Main St, City=Ny
 */
public class RecordPatternsAdvancedDemo {

    record Point(int x, int y) {}
    record Line(Point start, Point end) {}
    record Container(Point point, String label) {}

    record Pair<A, B>(A first, B second) {}
    record Triple<A, B, C>(A first, B second, C third) {}

    record Address(String street, String city, String zip) {}
    record Person(String name, int age, Address address) {}
    record Company(String name, List<Person> employees) {}

    record Color(int r, int g, int b) {}
    record ColoredPoint(Point point, Color color) {}

    public static void main(String[] args) {
        basicRecordPatterns();
        nestedRecordPatterns();
        genericRecordPatterns();
        complexDestructuring();
        patternWithGuards();
    }

    // =========================================================
    // 1. BASIC RECORD PATTERNS
    // =========================================================
    static void basicRecordPatterns() {
        System.out.println("=== Basic Record Patterns ===\n");

        // --- Before Java 21: manual destructuring ---
        // Point p = new Point(3, 4);
        // if (p instanceof Point) {
        //     int x = p.x();
        //     int y = p.y();
        //     System.out.println("Point at (" + x + ", " + y + ")");
        // }

        // --- With Java 21: record patterns destructure automatically ---
        Point p1 = new Point(3, 4);
        Point p2 = new Point(0, 0);

        String desc1 = switch (p1) {
            case Point(int x, int y) -> "Point at (" + x + ", " + y + ")";
        };
        System.out.println(desc1);

        String desc2 = switch (p2) {
            case Point(int x, int y) -> "Point at (" + x + ", " + y + ")";
        };
        System.out.println(desc2);

        // With instanceof pattern
        Object obj = new Point(10, 20);
        if (obj instanceof Point(int x, int y)) {
            System.out.println("instanceof destructured: x=" + x + ", y=" + y);
        }

        System.out.println();
    }

    // =========================================================
    // 2. NESTED RECORD PATTERNS
    // =========================================================
    static void nestedRecordPatterns() {
        System.out.println("=== Nested Record Patterns ===\n");

        // --- Before Java 21: deeply nested instanceof ---
        // Line line = new Line(new Point(0, 0), new Point(5, 5));
        // if (line instanceof Line) {
        //     Point start = line.start();
        //     Point end = line.end();
        //     if (start instanceof Point) {
        //         int x1 = start.x();
        //         int y1 = start.y();
        //         // ... and so on for end point
        //     }
        // }

        // --- With Java 21: nested patterns destructure in one step ---
        Line line = new Line(new Point(0, 0), new Point(5, 5));

        String lineDesc = switch (line) {
            case Line(Point(int x1, int y1), Point(int x2, int y2)) -> {
                double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                yield String.format("Line from (%d,%d) to (%d,%d) has length %.2f",
                        x1, y1, x2, y2, length);
            }
        };
        System.out.println(lineDesc);

        // Nested container pattern
        Container container = new Container(new Point(1, 2), "My Container");
        String containerDesc = switch (container) {
            case Container(Point(int x, int y), String label) ->
                "Nested: " + label + " with Point(" + x + ", " + y + ")";
        };
        System.out.println(containerDesc);

        // ColoredPoint - combining record and type patterns
        ColoredPoint cp = new ColoredPoint(new Point(5, 10), new Color(255, 0, 0));
        String colorDesc = switch (cp) {
            case ColoredPoint(Point(int x, int y), Color(int r, int g, int b)) ->
                String.format("ColoredPoint(%d,%d) color=(%d,%d,%d)", x, y, r, g, b);
        };
        System.out.println(colorDesc);

        System.out.println();
    }

    // =========================================================
    // 3. GENERIC RECORD PATTERNS
    // =========================================================
    static void genericRecordPatterns() {
        System.out.println("=== Generic Record Patterns ===\n");

        Pair<String, Integer> pair = new Pair<>("Hello", 42);
        Triple<String, Integer, Boolean> triple = new Triple<>("test", 100, true);

        // Pattern matching with generic records
        String pairDesc = switch (pair) {
            case Pair<String s, Integer i) -> "Pair: (" + s + ", " + i + ")";
        };
        System.out.println(pairDesc);

        // Destructure and use values
        if (pair instanceof Pair<String s, Integer i)) {
            System.out.println("First: " + s + ", Second: " + i);
        }

        // Triple pattern
        String tripleDesc = switch (triple) {
            case Triple<String s, Integer i, Boolean b) ->
                "Triple: (" + s + ", " + i + ", " + b + ")";
        };
        System.out.println(tripleDesc);

        System.out.println();
    }

    // =========================================================
    // 4. COMPLEX DESTRUCTURING
    // =========================================================
    static void complexDestructuring() {
        System.out.println("=== Complex Destructuring ===\n");

        // Nested records with deep destructuring
        Person person = new Person("Alice", 30,
                new Address("123 Main St", "NY", "10001"));

        // --- Before Java 21: manual deep destructuring ---
        // Person person = ...;
        // String name = person.name();
        // int age = person.age();
        // Address addr = person.address();
        // String street = addr.street();
        // String city = addr.city();

        // --- With Java 21: one pattern does it all ---
        String personDesc = switch (person) {
            case Person(String name, int age, Address(String street, String city, String zip)) ->
                String.format("Person: %s, Age: %d, Address: Street=%s, City=%s",
                        name, age, street, city);
        };
        System.out.println(personDesc);

        // Company with nested list
        Company company = new Company("Acme", List.of(
                new Person("Bob", 25, new Address("456 Oak Ave", "SF", "94102")),
                new Person("Carol", 35, new Address("789 Pine Rd", "LA", "90001"))
        ));

        // Pattern matching on company structure
        String companyDesc = switch (company) {
            case Company(String name, List<Person> employees) ->
                String.format("Company: %s, %d employees", name, employees.size());
        };
        System.out.println(companyDesc);

        System.out.println();
    }

    // =========================================================
    // 5. PATTERNS WITH GUARDS
    // =========================================================
    static void patternWithGuards() {
        System.out.println("=== Patterns with Guards ===\n");

        Point[] points = {
            new Point(0, 0),
            new Point(3, 4),
            new Point(-1, -1),
            new Point(10, 0)
        };

        for (Point p : points) {
            // --- Before Java 21: if-else after instanceof ---
            // if (p instanceof Point) {
            //     int x = p.x();
            //     int y = p.y();
            //     if (x == 0 && y == 0) { ... }
            //     else if (x > 0) { ... }
            // }

            // --- With Java 21: patterns with guards ---
            String location = switch (p) {
                case Point(int x, int y) when x == 0 && y == 0 -> "Origin";
                case Point(int x, int y) when x > 0 && y == 0  -> "Positive X-axis: " + x;
                case Point(int x, int y) when x == 0 && y > 0  -> "Positive Y-axis: " + y;
                case Point(int x, int y) when x < 0 && y < 0   -> "Third quadrant: (" + x + "," + y + ")";
                case Point(int x, int y)                       -> "Other point: (" + x + "," + y + ")";
            };
            System.out.println(location);
        }

        System.out.println();
    }
}
