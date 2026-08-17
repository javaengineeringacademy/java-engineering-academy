/**
 * Exercise 10: Character Autoboxing
 * 
 * Test autoboxing behavior with Character values.
 * 
 * Verify the cache range for Character (0-127).
 * Test values at boundaries: 127, 128, 0, 255
 */
public class Exercise10 {
    public static void main(String[] args) {
        // TODO: Test Character autoboxing cache
        Character a = 127;
        Character b = 127;
        System.out.println("127 == 127: " + (a == b));
        
        Character c = 128;
        Character d = 128;
        System.out.println("128 == 128: " + (c == d));
        
        Character e = 0;
        Character f = 0;
        System.out.println("0 == 0: " + (e == f));
        
        Character g = 255;
        Character h = 255;
        System.out.println("255 == 255: " + (g == h));
    }
}
