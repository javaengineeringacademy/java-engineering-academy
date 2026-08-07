/**
 * Solution 8: Wrapper Class Methods
 */
public class Solution8 {
    public static void main(String[] args) {
        System.out.println("=== Wrapper Class Methods ===\n");
        
        // 1. Convert String to Integer
        String numberString = "123";
        Integer intValue = Integer.parseInt(numberString);
        System.out.println("1. String \"123\" → Integer: " + intValue);
        
        // 2. Convert Integer to String
        Integer myNum = 456;
        String numString = myNum.toString();
        System.out.println("2. Integer 456 → String: \"" + numString + "\"");
        
        // 3. Get binary representation
        int number = 42;
        String binary = Integer.toBinaryString(number);
        System.out.println("3. Binary of 42: " + binary);
        
        // 4. Get maximum Integer value
        int maxValue = Integer.MAX_VALUE;
        System.out.println("4. Maximum Integer value: " + maxValue);
        
        // Additional useful methods
        System.out.println("\n=== Additional Methods ===");
        System.out.println("Integer.MIN_VALUE: " + Integer.MIN_VALUE);
        System.out.println("Integer.compare(10, 20): " + Integer.compare(10, 20));
        System.out.println("Integer.sum(10, 20): " + Integer.sum(10, 20));
        System.out.println("Integer.max(10, 20): " + Integer.max(10, 20));
        System.out.println("Integer.min(10, 20): " + Integer.min(10, 20));
    }
}
