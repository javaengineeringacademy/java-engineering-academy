package academy.javaengineering.modern.instanceofpattern;

/**
 * Basic instanceof pattern matching examples.
 */
public class BasicInstanceofExample {

    public static void main(String[] args) {
        // Basic pattern matching
        System.out.println("=== Basic Pattern Matching ===");
        Object[] objects = {"Hello", 42, 3.14, true, null};
        for (Object obj : objects) {
            if (obj instanceof String s) {
                System.out.println("String: " + s + " (length: " + s.length() + ")");
            } else if (obj instanceof Integer i) {
                System.out.println("Integer: " + i + " (doubled: " + (i * 2) + ")");
            } else if (obj instanceof Double d) {
                System.out.println("Double: " + d + " (rounded: " + Math.round(d) + ")");
            } else if (obj instanceof Boolean b) {
                System.out.println("Boolean: " + b);
            } else {
                System.out.println("Null or unknown");
            }
        }

        // Guarded patterns
        System.out.println("\n=== Guarded Patterns ===");
        String[] strings = {"Hello", "World", "Java", "Programming", "AI"};
        for (String str : strings) {
            if (str instanceof String s && s.length() > 5) {
                System.out.println("Long string: " + s);
            } else if (str instanceof String s && s.startsWith("J")) {
                System.out.println("Starts with J: " + s);
            } else if (str instanceof String s) {
                System.out.println("Other string: " + s);
            }
        }

        // Pattern in conditional expressions
        System.out.println("\n=== Pattern in Conditionals ===");
        Object input = "Hello, World!";
        String result = (input instanceof String s) ? 
            "String with length " + s.length() : 
            "Not a string";
        System.out.println("Result: " + result);

        // Pattern with complex objects
        System.out.println("\n=== Pattern with Complex Objects ===");
        Object obj = new StringBuilder("Hello");
        if (obj instanceof StringBuilder sb) {
            sb.append(" World");
            System.out.println("StringBuilder: " + sb);
        }

        // Multiple conditions
        System.out.println("\n=== Multiple Conditions ===");
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve"};
        for (String name : names) {
            if (name instanceof String s && s.length() > 3 && !s.startsWith("D")) {
                System.out.println("Valid name: " + s);
            }
        }
    }
}
