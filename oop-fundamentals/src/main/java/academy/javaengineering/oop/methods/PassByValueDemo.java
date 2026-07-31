package academy.javaengineering.oop.methods;

/**
 * Demonstrates pass-by-value in Java.
 */
public final class PassByValueDemo {

    public static void main(String[] args) {
        // Primitive - pass by value (copy)
        int primitive = 10;
        modifyPrimitive(primitive);
        System.out.println("After modifyPrimitive: " + primitive); // Still 10

        // Object reference - pass by value (copy of reference)
        StringBuilder sb = new StringBuilder("Original");
        modifyReference(sb);
        System.out.println("After modifyReference: " + sb); // Modified!

        // Reassignment doesn't affect original reference
        StringBuilder sb2 = new StringBuilder("Original");
        reassignReference(sb2);
        System.out.println("After reassignReference: " + sb2); // Still "Original"
    }

    static void modifyPrimitive(int x) {
        x = 20; // Modifies local copy
    }

    static void modifyReference(StringBuilder sb) {
        sb.append(" - Modified"); // Modifies the SAME object
    }

    static void reassignReference(StringBuilder sb) {
        sb = new StringBuilder("Reassigned"); // Changes local reference only
    }
}