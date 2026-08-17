public final class Exercise4 {
    public static void main(String[] args) {
        // Coordinate
        Coordinate c1 = new Coordinate(40.7128, -74.0060);
        Coordinate c2 = new Coordinate(40.7128, -74.0060);
        System.out.println("c1: " + c1);
        System.out.println("c1 == c2: " + c1.equals(c2));

        // Color
        Color red = new Color(255, 0, 0);
        Color brighter = red.brighter();
        System.out.println("\nOriginal color: " + red);
        System.out.println("Brighter color: " + brighter);

        // Range
        Range range = new Range(1, 10);
        System.out.println("\nRange: " + range);
        System.out.println("Contains 5: " + range.contains(5));
        System.out.println("Contains 15: " + range.contains(15));
        Range expanded = range.expand(3);
        System.out.println("Expanded: " + expanded);
    }
}

/*
 * TODO: Implement the three records below.
 *
 * Coordinate:
 * - Compact constructor must validate latitude is in [-90, 90]
 * - Compact constructor must validate longitude is in [-180, 180]
 * - Throw IllegalArgumentException for invalid values
 *
 * Color:
 * - brighter() returns a new Color with each component increased by 30, capped at 255
 *
 * Range:
 * - contains(int value) returns true if value is between min and max (inclusive)
 * - expand(int delta) returns a new Range with min - delta and max + delta
 */

// TODO: record Coordinate(...)

// TODO: record Color(...)

// TODO: record Range(...)
