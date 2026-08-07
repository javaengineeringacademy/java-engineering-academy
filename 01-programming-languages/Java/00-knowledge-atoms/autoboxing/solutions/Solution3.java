import java.util.ArrayList;
import java.util.List;

/**
 * Solution 3: Collection Operations
 */
public class Solution3 {
    public static void main(String[] args) {
        System.out.println("=== Collection Operations ===\n");
        
        List<Integer> numbers = new ArrayList<>();
        
        // Add values using autoboxing
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        numbers.add(40);
        numbers.add(50);
        
        // Display all values
        System.out.println("Values in list:");
        for (int num : numbers) {
            System.out.println("  " + num);
        }
        
        // Calculate sum
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        System.out.println("\nSum: " + sum);
        
        // Find maximum
        int max = Integer.MIN_VALUE;
        for (int num : numbers) {
            if (num > max) {
                max = num;
            }
        }
        System.out.println("Maximum: " + max);
    }
}
