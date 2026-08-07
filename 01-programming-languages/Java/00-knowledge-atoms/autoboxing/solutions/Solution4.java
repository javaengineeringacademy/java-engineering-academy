/**
 * Solution 4: Null Safety
 */
public class Solution4 {
    public static void main(String[] args) {
        System.out.println("=== Null Safety Demo ===\n");
        
        Integer value1 = 42;
        Integer value2 = null;
        
        System.out.println("Value 1: " + safeUnbox(value1));
        System.out.println("Value 2: " + safeUnbox(value2));
        
        // Additional test
        Integer value3 = 100;
        System.out.println("Value 3: " + safeUnbox(value3, -1));  // Custom default
        System.out.println("Value 2 (custom default): " + safeUnbox(value2, -1));
    }
    
    // Method 1: Simple null check
    public static int safeUnbox(Integer value) {
        return value != null ? value : 0;
    }
    
    // Method 2: Custom default value
    public static int safeUnbox(Integer value, int defaultValue) {
        return value != null ? value : defaultValue;
    }
}
