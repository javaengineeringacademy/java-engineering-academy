/**
 * Solution 6: Method Overloading
 */
public class Solution6 {
    public static void main(String[] args) {
        System.out.println("=== Method Overloading Demo ===\n");
        
        System.out.println("Calling process(42):");
        process(42);  // Calls process(int)
        
        System.out.println("\nCalling process(Integer.valueOf(42)):");
        process(Integer.valueOf(42));  // Calls process(Integer)
        
        Integer num = 100;
        System.out.println("\nCalling process(num) where num = 100:");
        process(num);  // Calls process(Integer)
        
        System.out.println("\nCalling process(null):");
        Integer nullNum = null;
        process(nullNum);  // Calls process(Integer)
        
        System.out.println("\n=== Explanation ===");
        System.out.println("- process(int) is called with int literals");
        System.out.println("- process(Integer) is called with Integer objects");
        System.out.println("- null calls process(Integer) because null is a valid Integer");
    }
    
    public static void process(int value) {
        System.out.println("  → process(int): " + value);
    }
    
    public static void process(Integer value) {
        System.out.println("  → process(Integer): " + value);
    }
}
