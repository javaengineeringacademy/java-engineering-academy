package academy.javaengineering.text.memory;

public class CharacterMemory {

    public static void main(String[] args) {
        System.out.println("=== Character Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. char vs Character
        System.out.println("--- char vs Character ---");
        System.out.println("char: 2 bytes (primitive)");
        System.out.println("Character: 16 bytes (object)");
        System.out.println("Character: wraps char");

        // 2. Autoboxing Cost
        System.out.println("\n--- Autoboxing Cost ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        Character c = 'A';
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Character: " + (after - before) + " bytes");
        System.out.println("Object overhead: 14 bytes");

        // 3. String char Storage
        System.out.println("\n--- String char Storage ---");
        System.out.println("String: char[] array");
        System.out.println("Each char: 2 bytes");
        System.out.println("String \"Hello\": 10 bytes for chars");
    }
}
