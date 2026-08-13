package academy.javaengineering.text.memory;

public class UnicodeMemory {

    public static void main(String[] args) {
        System.out.println("=== Unicode Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. char vs Code Point
        System.out.println("--- char vs Code Point ---");
        System.out.println("char: 2 bytes (BMP only)");
        System.out.println("Code point: 4 bytes (full Unicode)");
        System.out.println("Emoji: 4 bytes (surrogate pair)");

        // 2. String with Unicode
        System.out.println("\n--- String with Unicode ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        String text = "Hello \uD83D\uDE00";
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("String with emoji: " + (after - before) + " bytes");
        System.out.println("Emoji: 2 chars (surrogate pair)");

        // 3. Unicode Memory Impact
        System.out.println("\n--- Unicode Memory ---");
        System.out.println("BMP characters: 2 bytes each");
        System.out.println("Supplementary: 4 bytes each");
        System.out.println("UTF-8: 1-4 bytes per character");
    }
}
