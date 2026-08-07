/**
 * Exercise 9: Boolean Autoboxing
 * 
 * Test autoboxing behavior with Boolean values.
 * 
 * Verify that:
 * - Boolean.TRUE == Boolean.TRUE returns true
 * - Boolean.TRUE == Boolean.FALSE returns false
 * - Boolean values are cached (TRUE and FALSE)
 */
public class Exercise9 {
    public static void main(String[] args) {
        // TODO: Test Boolean autoboxing
        Boolean a = true;
        Boolean b = true;
        Boolean c = false;
        
        // Test using ==
        System.out.println("a == b: " + (a == b));
        System.out.println("a == c: " + (a == c));
        
        // Test using equals()
        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a.equals(c): " + a.equals(c));
        
        // Test Boolean.TRUE and Boolean.FALSE
        System.out.println("Boolean.TRUE == Boolean.TRUE: " + (Boolean.TRUE == Boolean.TRUE));
    }
}
