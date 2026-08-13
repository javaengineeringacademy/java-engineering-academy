package academy.javaengineering.oop.memory;

public class ClassesMemory {

    static class Person {
        String name;
        int age;
    }

    static class Empty {}

    public static void main(String[] args) {
        System.out.println("=== Classes Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Empty Class Size
        System.out.println("--- Empty Class Size ---");
        System.out.println("Empty object: 12 bytes (header only)");
        System.out.println("With int field: 16 bytes");
        System.out.println("With reference: 16 bytes");

        // 2. Field Memory
        System.out.println("\n--- Field Memory ---");
        System.out.println("boolean: 1 byte");
        System.out.println("byte: 1 byte");
        System.out.println("char: 2 bytes");
        System.out.println("int: 4 bytes");
        System.out.println("long: 8 bytes");
        System.out.println("float: 4 bytes");
        System.out.println("double: 8 bytes");
        System.out.println("reference: 8 bytes (compressed)");

        // 3. Alignment Padding
        System.out.println("\n--- Alignment Padding ---");
        System.out.println("Objects padded to 8-byte boundary");
        System.out.println("Example: 12 bytes -> padded to 16");
        System.out.println("Improves CPU cache performance");

        // 4. Class Metadata
        System.out.println("\n--- Class Metadata ---");
        System.out.println("Stored in Metaspace (not heap)");
        System.out.println("Contains: method table, field info");
        System.out.println("One copy per class loaded");
    }
}
