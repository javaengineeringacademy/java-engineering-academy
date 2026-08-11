package academy.javaengineering.exceptions.examples;

/**
 * Basic try-catch example demonstrating handling of ArithmeticException.
 * This example shows the fundamental syntax of try-catch blocks in Java.
 */
public class BasicTryCatchExample {

    public static void main(String[] args) {
        System.out.println("=== Basic Try-Catch Example ===");
        
        try {
            // This will cause ArithmeticException (division by zero)
            int result = 10 / 0;
            System.out.println("Result: " + result); // This line won't execute
        } catch (ArithmeticException e) {
            // Handle the exception
            System.out.println("Caught ArithmeticException: " + e.getMessage());
            System.out.println("Exception type: " + e.getClass().getSimpleName());
        }
        
        System.out.println("Program continues after exception handling");
        
        // Another example with array index out of bounds
        try {
            int[] numbers = {1, 2, 3};
            System.out.println("Element at index 5: " + numbers[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught ArrayIndexOutOfBoundsException: Index " + 
                e.getMessage() + " is out of bounds");
        }
        
        System.out.println("=== End of Basic Try-Catch Example ===");
    }
}