/**
 * Solution 2: Integer Cache
 */
public class Solution2 {
    public static void main(String[] args) {
        System.out.println("=== Integer Cache Boundary Tests ===\n");
        
        // Test -128 (within cache)
        Integer a = -128;
        Integer b = -128;
        System.out.println("Testing -128:");
        System.out.println("  a == b: " + (a == b) + " (within cache)");
        System.out.println("  a.equals(b): " + a.equals(b));
        
        // Test -129 (outside cache)
        Integer c = -129;
        Integer d = -129;
        System.out.println("\nTesting -129:");
        System.out.println("  c == d: " + (c == d) + " (outside cache)");
        System.out.println("  c.equals(d): " + c.equals(d));
        
        // Test 127 (within cache)
        Integer e = 127;
        Integer f = 127;
        System.out.println("\nTesting 127:");
        System.out.println("  e == f: " + (e == f) + " (within cache)");
        System.out.println("  e.equals(f): " + e.equals(f));
        
        // Test 128 (outside cache)
        Integer g = 128;
        Integer h = 128;
        System.out.println("\nTesting 128:");
        System.out.println("  g == h: " + (g == h) + " (outside cache)");
        System.out.println("  g.equals(h): " + g.equals(h));
        
        System.out.println("\n=== Conclusion ===");
        System.out.println("Use .equals() for reliable value comparison!");
    }
}
