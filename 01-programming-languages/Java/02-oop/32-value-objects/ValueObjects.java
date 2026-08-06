import java.time.*;
import java.util.*;

/**
 * Value Objects in Java
 * Record types, value-based classes, and immutable patterns.
 */
public class ValueObjects {

    // --- Record types (Java 16+) ---
    record Point(int x, int y) {
        // Compact constructor for validation
        Point {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Coordinates must be non-negative");
            }
        }

        // Custom method
        double distanceTo(Point other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) +
                             Math.pow(this.y - other.y, 2));
        }

        // Static factory
        static Point origin() {
            return new Point(0, 0);
        }

        // Override accessor name
        String toCoordinateString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // --- Record with components ---
    record Person(String name, int age, List<String> hobbies) {
        // Records automatically provide:
        // - equals() and hashCode() based on components
        // - toString() with component values
        // - Accessor methods for each component
        // - Canonical constructor

        // Canonical constructor with defensive copy
        Person {
            Objects.requireNonNull(name);
            Objects.requireNonNull(hobbies);
            hobbies = List.copyOf(hobbies); // Make unmodifiable copy
        }
    }

    // --- Record extending interfaces ---
    interface Describable {
        String describe();
    }

    record Color(int r, int g, int b) implements Describable {
        @Override
        public String describe() {
            return String.format("RGB(%d, %d, %d)", r, g, b);
        }

        // Static constants
        static final Color RED = new Color(255, 0, 0);
        static final Color GREEN = new Color(0, 255, 0);
        static final Color BLUE = new Color(0, 0, 255);
    }

    // --- Value-based classes (JDK style) ---
    static final class Money {
        private final long amount;
        private final String currency;

        Money(long amount, String currency) {
            this.amount = amount;
            this.currency = currency;
        }

        long amount() { return amount; }
        String currency() { return currency; }

        // Value-based: equals/hashCode based on content
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Money other)) return false;
            return amount == other.amount &&
                   Objects.equals(currency, other.currency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(amount, currency);
        }

        @Override
        public String toString() {
            return amount + " " + currency;
        }

        static Money of(long amount, String currency) {
            return new Money(amount, currency);
        }
    }

    // --- Immutable value object pattern ---
    static final class Temperature {
        private final double value;
        private final char unit; // 'C' or 'F'

        private Temperature(double value, char unit) {
            this.value = value;
            this.unit = unit;
        }

        static Temperature celsius(double value) {
            return new Temperature(value, 'C');
        }

        static Temperature fahrenheit(double value) {
            return new Temperature(value, 'F');
        }

        Temperature toCelsius() {
            if (unit == 'C') return this;
            return new Temperature((value - 32) * 5.0 / 9.0, 'C');
        }

        Temperature toFahrenheit() {
            if (unit == 'F') return this;
            return new Temperature(value * 9.0 / 5.0 + 32, 'F');
        }

        boolean isBelowFreezing() {
            return toCelsius().value < 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Temperature other)) return false;
            Temperature otherC = other.toCelsius();
            Temperature thisC = this.toCelsius();
            return Math.abs(thisC.value - otherC.value) < 0.001;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Math.round(toCelsius().value * 1000));
        }

        @Override
        public String toString() {
            return String.format("%.1f°%c", value, unit);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Value Objects in Java ===\n");

        recordBasics();
        recordVsClass();
        valueBasedClasses();
        immutablePatterns();

        System.out.println("\n=== Complete ===");
    }

    static void recordBasics() {
        System.out.println("--- Record Basics ---");

        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = Point.origin();

        System.out.println("p1: " + p1);
        System.out.println("p1.x(): " + p1.x());
        System.out.println("p1 == p2: " + (p1 == p2));
        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1 distance to p3: " + p1.distanceTo(p3));

        // Records are immutable
        // p1.x = 5; // Compile error

        Person person = new Person("Alice", 30, List.of("reading", "coding"));
        System.out.println("Person: " + person);

        System.out.println();
    }

    static void recordVsClass() {
        System.out.println("--- Record vs Class ---");

        // Record: concise, immutable, value semantics
        record Coordinate(double lat, double lon) {}

        // Equivalent traditional class
        class TraditionalCoordinate {
            final double lat;
            final double lon;

            TraditionalCoordinate(double lat, double lon) {
                this.lat = lat;
                this.lon = lon;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof TraditionalCoordinate other)) return false;
                return Double.compare(lat, other.lat) == 0 &&
                       Double.compare(lon, other.lon) == 0;
            }

            @Override
            public int hashCode() {
                return Objects.hash(lat, lon);
            }

            @Override
            public String toString() {
                return "TraditionalCoordinate[lat=" + lat + ", lon=" + lon + "]";
            }
        }

        Coordinate rec = new Coordinate(40.7128, -74.0060);
        TraditionalCoordinate trad = new TraditionalCoordinate(40.7128, -74.0060);

        System.out.println("Record: " + rec);
        System.out.println("Traditional: " + trad);
        System.out.println("Record generated ~10x less code");

        System.out.println();
    }

    static void valueBasedClasses() {
        System.out.println("--- Value-Based Classes (Optional, Instant) ---");

        // Optional is value-based - use for composition, not identity
        Optional<String> opt = Optional.of("hello");
        Optional<String> opt2 = Optional.of("hello");
        System.out.println("Optional equals: " + opt.equals(opt2));

        // Instant is value-based
        Instant now = Instant.now();
        Instant later = now.plusSeconds(60);
        System.out.println("Instant now: " + now);
        System.out.println("Instant later: " + later);
        System.out.println("Instant isBefore: " + now.isBefore(later));

        // Value-based class rules:
        // 1. Use == for comparison (identity not guaranteed)
        // 2. Use as set/map values carefully
        // 3. Final fields set in constructor
        // 4. No settable fields

        Money m1 = Money.of(100, "USD");
        Money m2 = Money.of(100, "USD");
        Money m3 = Money.of(200, "EUR");

        System.out.println("m1: " + m1);
        System.out.println("m1.equals(m2): " + m1.equals(m2));
        System.out.println("m1.equals(m3): " + m1.equals(m3));

        System.out.println();
    }

    static void immutablePatterns() {
        System.out.println("--- Immutable Value Object Patterns ---");

        Temperature freezing = Temperature.celsius(0);
        Temperature body = Temperature.fahrenheit(98.6);
        Temperature boiling = Temperature.celsius(100);

        System.out.println("Freezing: " + freezing);
        System.out.println("Body: " + body);
        System.out.println("Body in Celsius: " + body.toCelsius());
        System.out.println("Freezing below zero: " + freezing.isBelowFreezing());
        System.out.println("Body below zero: " + body.isBelowFreezing());

        // Value semantics
        Temperature t1 = Temperature.celsius(100);
        Temperature t2 = Temperature.fahrenheit(212);
        System.out.println("100C == 212F: " + t1.equals(t2));

        // Collections with value objects
        Set<Point> points = new HashSet<>();
        points.add(new Point(1, 2));
        points.add(new Point(1, 2));
        points.add(new Point(3, 4));
        System.out.println("Unique points: " + points.size()); // 2

        // Record in collection
        Set<Coordinate> coords = new HashSet<>();
        coords.add(new Coordinate(40.7, -74.0));
        coords.add(new Coordinate(40.7, -74.0));
        System.out.println("Unique coords: " + coords.size()); // 1
    }
}
