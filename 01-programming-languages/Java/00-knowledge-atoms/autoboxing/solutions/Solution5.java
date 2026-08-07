/**
 * Solution 5: Performance Comparison
 */
public class Solution5 {
    public static void main(String[] args) {
        System.out.println("=== Performance Comparison ===\n");
        
        int iterations = 1000000;
        
        // Test with primitive int
        long startTime = System.currentTimeMillis();
        long sum1 = 0;
        for (int i = 0; i < iterations; i++) {
            sum1 += i;
        }
        long primitiveTime = System.currentTimeMillis() - startTime;
        
        // Test with Integer wrapper
        startTime = System.currentTimeMillis();
        Long sum2 = 0L;  // Using Long wrapper
        for (int i = 0; i < iterations; i++) {
            sum2 += i;  // Autoboxing on each iteration
        }
        long wrapperTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Primitive int time: " + primitiveTime + "ms");
        System.out.println("Integer wrapper time: " + wrapperTime + "ms");
        System.out.println("Difference: " + (wrapperTime - primitiveTime) + "ms");
        System.out.println("Wrapper is " + (wrapperTime > primitiveTime ? "slower" : "faster"));
        
        System.out.println("\n=== Conclusion ===");
        System.out.println("Primitive types are faster for loops!");
    }
}
