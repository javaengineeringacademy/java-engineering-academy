/**
 * Solution: Safe Casting with instanceof
 */
public class Solution1_Casting {
    public static void main(String[] args) {
        processObject("Hello");
        processObject(42);
        processObject(3.14);
        processObject(true);
    }

    static void processObject(Object obj) {
        if (obj instanceof String s) {
            System.out.println("String length " + s.length() + ": " + s.toUpperCase());
        } else if (obj instanceof Integer i) {
            System.out.println("Integer doubled: " + (i * 2));
        } else if (obj instanceof Double d) {
            System.out.println("Double rounded: " + Math.round(d));
        } else {
            System.out.println("Unknown type: " + obj.getClass().getSimpleName());
        }
    }
}
