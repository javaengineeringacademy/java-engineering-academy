/**
 * Exercise 6: Method Overloading
 * 
 * Create a class with overloaded methods that accept both int and Integer parameters.
 * 
 * Determine which method gets called in different scenarios:
 * - Calling with int literal
 * - Calling with Integer object
 * - Calling with Integer variable
 * - Calling with null
 */
public class Exercise6 {
    public static void main(String[] args) {
        // TODO: Test different scenarios
        process(42);                          // Which method?
        process(Integer.valueOf(42));         // Which method?
        
        Integer num = 100;
        process(num);                         // Which method?
        
        Integer nullNum = null;
        // process(nullNum);                  // What happens here?
    }
    
    // TODO: Create overloaded methods
    public static void process(int value) {
        System.out.println("process(int): " + value);
    }
    
    public static void process(Integer value) {
        System.out.println("process(Integer): " + value);
    }
}
