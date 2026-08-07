/**
 * Solution 9: Boolean Autoboxing
 */
public class Solution9 {
    public static void main(String[] args) {
        System.out.println("=== Boolean Autoboxing ===\n");
        
        Boolean a = true;
        Boolean b = true;
        Boolean c = false;
        
        // Test using ==
        System.out.println("a == b (both true): " + (a == b));
        System.out.println("a == c (true vs false): " + (a == c));
        
        // Test using equals()
        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a.equals(c): " + a.equals(c));
        
        // Test Boolean.TRUE and Boolean.FALSE
        System.out.println("Boolean.TRUE == Boolean.TRUE: " + (Boolean.TRUE == Boolean.TRUE));
        System.out.println("Boolean.FALSE == Boolean.FALSE: " + (Boolean.FALSE == Boolean.FALSE));
        System.out.println("Boolean.TRUE == Boolean.FALSE: " + (Boolean.TRUE == Boolean.FALSE));
        
        System.out.println("\n=== Conclusion ===");
        System.out.println("Boolean values are cached (TRUE and FALSE)");
        System.out.println("Always use .equals() for wrapper comparison!");
    }
}
