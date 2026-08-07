/**
 * Exercise 5: Performance Comparison
 * 
 * Write a program comparing the performance of using Integer vs int in a loop.
 * 
 * Test with 1,000,000 iterations and measure:
 * - Time taken using primitive int
 * - Time taken using Integer wrapper
 * 
 * Expected observation:
 * - Primitive int should be significantly faster
 * - Integer wrapper creates objects on each iteration
 */
public class Exercise5 {
    public static void main(String[] args) {
        int iterations = 1000000;
        
        // Test with primitive int
        long startTime = System.currentTimeMillis();
        long sum1 = 0;
        for (int i = 0; i < iterations; i++) {
            sum1 += i;
        }
        long primitiveTime = System.currentTimeMillis() - startTime;
        
        // TODO: Test with Integer wrapper
        // Compare execution times
        System.out.println("Primitive int time: " + primitiveTime + "ms");
        System.out.println("Sum: " + sum1);
    }
}
