/**
 * Exercise: Safe Casting with instanceof
 *
 * Task: Complete the processObject method to safely handle different types.
 * - Check if obj is a String, Integer, or Double using instanceof
 * - Cast and process each type appropriately
 * - Handle unknown types with a default message
 */
public class Exercise1_Casting {
    public static void main(String[] args) {
        processObject("Hello");
        processObject(42);
        processObject(3.14);
        processObject(true);
    }

    /**
     * TODO: Implement type checking and safe casting
     * - If String, print length and uppercase version
     * - If Integer, print doubled value
     * - If Double, print rounded value
     * - Otherwise, print type name
     */
    static void processObject(Object obj) {
        // Your code here
    }
}
