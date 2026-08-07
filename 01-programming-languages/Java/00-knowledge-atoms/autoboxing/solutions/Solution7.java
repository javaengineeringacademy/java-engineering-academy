/**
 * Solution 7: Autoboxing in Expressions
 */
public class Solution7 {
    public static void main(String[] args) {
        System.out.println("=== Autoboxing in Expressions ===\n");
        
        Integer a = 100;
        Integer b = 200;
        Integer c = a + b;
        
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a + b = " + c);
        
        System.out.println("\n=== Step-by-Step Explanation ===");
        System.out.println("1. a (Integer) is unboxed to int (100)");
        System.out.println("2. b (Integer) is unboxed to int (200)");
        System.out.println("3. int + int = int (300)");
        System.out.println("4. int (300) is autoboxed to Integer (300)");
        System.out.println("5. Result stored in c (Integer)");
    }
}
