/**
 * Solution 1: Basic Autoboxing
 */
public class Solution1 {
    public static void main(String[] args) {
        System.out.println("=== Basic Autoboxing Demo ===\n");
        
        // byte → Byte
        byte bytePrimitive = 10;
        Byte byteWrapper = bytePrimitive;  // Autoboxing
        byte byteUnboxed = byteWrapper;    // Unboxing
        System.out.println("byte: " + bytePrimitive + " → " + byteWrapper + " → " + byteUnboxed);
        
        // short → Short
        short shortPrimitive = 200;
        Short shortWrapper = shortPrimitive;
        short shortUnboxed = shortWrapper;
        System.out.println("short: " + shortPrimitive + " → " + shortWrapper + " → " + shortUnboxed);
        
        // int → Integer
        int intPrimitive = 1000;
        Integer intWrapper = intPrimitive;
        int intUnboxed = intWrapper;
        System.out.println("int: " + intPrimitive + " → " + intWrapper + " → " + intUnboxed);
        
        // long → Long
        long longPrimitive = 999999999L;
        Long longWrapper = longPrimitive;
        long longUnboxed = longWrapper;
        System.out.println("long: " + longPrimitive + " → " + longWrapper + " → " + longUnboxed);
        
        // float → Float
        float floatPrimitive = 3.14f;
        Float floatWrapper = floatPrimitive;
        float floatUnboxed = floatWrapper;
        System.out.println("float: " + floatPrimitive + " → " + floatWrapper + " → " + floatUnboxed);
        
        // double → Double
        double doublePrimitive = 2.71828;
        Double doubleWrapper = doublePrimitive;
        double doubleUnboxed = doubleWrapper;
        System.out.println("double: " + doublePrimitive + " → " + doubleWrapper + " → " + doubleUnboxed);
        
        // char → Character
        char charPrimitive = 'A';
        Character charWrapper = charPrimitive;
        char charUnboxed = charWrapper;
        System.out.println("char: " + charPrimitive + " → " + charWrapper + " → " + charUnboxed);
        
        // boolean → Boolean
        boolean booleanPrimitive = true;
        Boolean booleanWrapper = booleanPrimitive;
        boolean booleanUnboxed = booleanWrapper;
        System.out.println("boolean: " + booleanPrimitive + " → " + booleanWrapper + " → " + booleanUnboxed);
    }
}
