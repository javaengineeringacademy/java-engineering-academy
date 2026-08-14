package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== 'this' Keyword Patterns ===\n");

        // WHY: 'this' resolves ambiguity between parameters and fields
        // INTERNAL: 'this' is a hidden parameter passed to every instance method (aload_0)
        // ENGINEERING: Use this() for constructor chaining to avoid duplication

        Point p1 = new Point(3, 4);
        Point p2 = p1.moveTo(10, 20);
        Point p3 = p1.translate(5, 5);

        System.out.println("p1: " + p1);
        System.out.println("p2: " + p2);
        System.out.println("p3: " + p3);

        // TRADE-OFF: returning 'this' creates new reference vs modifying in place
        // Immutable objects MUST return new instance (p1 unchanged above)
        // Mutable objects can modify in place for performance
    }
}

class Point {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;  // 'this' disambiguates field from parameter
        this.y = y;
    }

    public Point moveTo(int x, int y) {
        return new Point(x, y);  // New instance (immutable pattern)
    }

    public Point translate(int dx, int dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    @Override
    public String toString() { return "(" + x + "," + y + ")"; }
}
