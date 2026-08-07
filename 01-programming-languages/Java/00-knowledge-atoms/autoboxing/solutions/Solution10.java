/**
 * Solution 10: Character Autoboxing
 */
public class Solution10 {
    public static void main(String[] args) {
        System.out.println("=== Character Autoboxing Cache ===\n");
        
        // Test 127 (within cache)
        Character a = 127;
        Character b = 127;
        System.out.println("127 == 127: " + (a == b) + " (within cache)");
        
        // Test 128 (outside cache)
        Character c = 128;
        Character d = 128;
        System.out.println("128 == 128: " + (c == d) + " (outside cache)");
        
        // Test 0 (within cache)
        Character e = 0;
        Character f = 0;
        System.out.println("0 == 0: " + (e == f) + " (within cache)");
        
        // Test 255 (outside cache)
        Character g = 255;
        Character h = 255;
        System.out.println("255 == 255: " + (g == h) + " (outside cache)");
        
        System.out.println("\n=== Character Cache Range ===");
        System.out.println("Character cache range: 0 to 127");
        System.out.println("Use .equals() for reliable comparison!");
    }
}
