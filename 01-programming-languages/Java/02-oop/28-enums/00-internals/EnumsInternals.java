package academy.javaengineering.oop.internals;

public class EnumsInternals {

    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    enum Planet {
        MERCURY(3.303e+23, 2.4397e6),
        VENUS(4.869e+24, 6.0518e6),
        EARTH(5.976e+24, 6.37814e6);

        private final double mass;
        private final double radius;

        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
        }

        double surfaceGravity() {
            final double G = 6.67300E-11;
            return G * mass / (radius * radius);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Enums Internals ===\n");

        // 1. Basic Enum
        System.out.println("--- Basic Enum ---");
        Day today = Day.WEDNESDAY;
        System.out.println("Today: " + today);
        System.out.println("Ordinal: " + today.ordinal());
        System.out.println("Name: " + today.name());

        // 2. Enum with Fields
        System.out.println("\n--- Enum with Fields ---");
        System.out.println("EARTH surface gravity: " + Planet.EARTH.surfaceGravity());

        // 3. Enum Methods
        System.out.println("\n--- Enum Methods ---");
        System.out.println("values(): " + java.util.Arrays.toString(Day.values()));
        System.out.println("valueOf(\"MONDAY\"): " + Day.valueOf("MONDAY"));
        System.out.println("compareTo(): " + Day.MONDAY.compareTo(Day.WEDNESDAY));

        // 4. Enum as Singleton
        System.out.println("\n--- Enum Singleton ---");
        System.out.println("Enum guarantees single instance");
        System.out.println("Thread-safe serialization");
    }
}
