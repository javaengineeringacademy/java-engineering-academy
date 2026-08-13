package academy.javaengineering.text.internals;

public class TextBlocksInternals {

    public static void main(String[] args) {
        System.out.println("=== Text Blocks Internals ===\n");

        // 1. Text Block Syntax
        System.out.println("--- Text Block Syntax ---");
        String textBlock = """
                Hello
                World
                """;
        System.out.println("Text block: " + textBlock);

        // 2. Text Block Features
        System.out.println("\n--- Text Block Features ---");
        String json = """
                {
                    "name": "Alice",
                    "age": 25
                }
                """;
        System.out.println("JSON: " + json);

        // 3. Text Block vs String
        System.out.println("\n--- Text Block vs String ---");
        System.out.println("Text block: multi-line, readable");
        System.out.println("String: single-line, escape sequences");
        System.out.println("Text block: Java 15+");
    }
}
