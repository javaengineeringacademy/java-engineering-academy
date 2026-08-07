public class IntegerCache {
    public static void main(String[] args) {
        System.out.println("=== Integer Cache Demonstration ===\n");

        // Test within cache range (-128 to 127)
        Integer a = 127;
        Integer b = 127;
        System.out.println("Within cache range (127):");
        System.out.println("  a == b: " + (a == b) + " (same object)");
        System.out.println("  a.equals(b): " + a.equals(b) + " (same value)");

        // Test outside cache range
        Integer c = 128;
        Integer d = 128;
        System.out.println("\nOutside cache range (128):");
        System.out.println("  c == d: " + (c == d) + " (different objects)");
        System.out.println("  c.equals(d): " + c.equals(d) + " (same value)");

        // Negative values
        Integer e = -128;
        Integer f = -128;
        System.out.println("\nNegative cache range (-128):");
        System.out.println("  e == f: " + (e == f) + " (same object)");

        Integer g = -129;
        Integer h = -129;
        System.out.println("\nNegative outside cache range (-129):");
        System.out.println("  g == h: " + (g == h) + " (different objects)");

        // Demonstrate Integer.valueOf()
        System.out.println("\n=== Using Integer.valueOf() ===");
        Integer i1 = Integer.valueOf(100);
        Integer i2 = Integer.valueOf(100);
        System.out.println("valueOf(100) == valueOf(100): " + (i1 == i2));

        Integer i3 = Integer.valueOf(200);
        Integer i4 = Integer.valueOf(200);
        System.out.println("valueOf(200) == valueOf(200): " + (i3 == i4));

        // Show memory addresses (using identityHashCode)
        System.out.println("\n=== Object Identity ===");
        Integer x = 50;
        Integer y = 50;
        System.out.println("Identity hash code of x: " + System.identityHashCode(x));
        System.out.println("Identity hash code of y: " + System.identityHashCode(y));
        System.out.println("Same object? " + (System.identityHashCode(x) == System.identityHashCode(y)));
    }
}
