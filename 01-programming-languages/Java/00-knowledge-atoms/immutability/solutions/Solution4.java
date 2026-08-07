public final class Solution4 {
    public static void main(String[] args) {
        Coordinate c1 = new Coordinate(40.7128, -74.0060);
        Coordinate c2 = new Coordinate(40.7128, -74.0060);
        System.out.println("c1: " + c1);
        System.out.println("c1 == c2: " + c1.equals(c2));

        Color red = new Color(255, 0, 0);
        Color brighter = red.brighter();
        System.out.println("\nOriginal color: " + red);
        System.out.println("Brighter color: " + brighter);

        Range range = new Range(1, 10);
        System.out.println("\nRange: " + range);
        System.out.println("Contains 5: " + range.contains(5));
        System.out.println("Contains 15: " + range.contains(15));
        Range expanded = range.expand(3);
        System.out.println("Expanded: " + expanded);
    }
}

record Coordinate(double latitude, double longitude) {
    Coordinate {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }
}

record Color(int red, int green, int blue) {
    Color {
        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));
    }

    public Color brighter() {
        return new Color(
            Math.min(255, red + 30),
            Math.min(255, green + 30),
            Math.min(255, blue + 30)
        );
    }
}

record Range(int min, int max) {
    Range {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
    }

    public boolean contains(int value) {
        return value >= min && value <= max;
    }

    public Range expand(int delta) {
        return new Range(min - delta, max + delta);
    }
}
