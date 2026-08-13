package academy.javaengineering.text.memory;

public class TextBlocksMemory {

    public static void main(String[] args) {
        System.out.println("=== Text Blocks Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. Text Block Size
        System.out.println("--- Text Block Size ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        String textBlock = """
                Hello
                World
                """;
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("Text block: " + (after - before) + " bytes");
        System.out.println("Same as equivalent String");

        // 2. Text Block vs String Concatenation
        System.out.println("\n--- Text Block vs Concat ---");
        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        String concat = "Hello\nWorld\n";
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("String concat: " + (after - before) + " bytes");
        System.out.println("Same memory - compile-time");

        // 3. Compile-Time Processing
        System.out.println("\n--- Compile-Time ---");
        System.out.println("Text block: processed at compile time");
        System.out.println("No runtime overhead");
        System.out.println("Same bytecode as String");
    }
}
