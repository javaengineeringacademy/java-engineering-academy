/**
 * Exercise 4: Null Safety
 * 
 * Write a method that safely unboxes an Integer parameter.
 * The method should:
 * - Return the integer value if not null
 * - Return a default value (0) if null
 * 
 * Test with both null and non-null values.
 */
public class Exercise4 {
    public static void main(String[] args) {
        // TODO: Test with null and non-null values
        Integer value1 = 42;
        Integer value2 = null;
        
        System.out.println("Value 1: " + safeUnbox(value1));
        System.out.println("Value 2: " + safeUnbox(value2));
    }
    
    // TODO: Implement this method
    public static int safeUnbox(Integer value) {
        // Your implementation here
        return 0;
    }
}
