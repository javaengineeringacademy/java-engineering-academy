package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Static Binding ===\n");

        // WHY: Static binding resolves at compile time - faster, predictable
        // INTERNAL: invokestatic for static methods, invokespecial for private/constructor/super
        // ENGINEERING: Use for performance-critical code, utility methods

        // Static method - resolved at compile time
        Formatter.format("Hello");  // Calls Formatter.format(String)
        Formatter.format(42);       // Calls Formatter.format(int)

        // Private methods - also statically bound
        TextProcessor tp = new TextProcessor();
        tp.process("Test");

        // TRADE-OFF: Static binding is faster but less flexible
        // Can't override static methods (only hide them)
        // Private methods can't be overridden
    }
}

class Formatter {
    public static void format(String s) {
        System.out.println("String: " + s);
    }

    public static void format(int i) {
        System.out.println("Int: " + i);
    }
}

class TextProcessor {
    public void process(String text) {
        String trimmed = trim(text);  // Static call to private method
        String upper = toUpper(trimmed);
        System.out.println("Processed: " + upper);
    }

    private String trim(String s) { return s.trim(); }
    private String toUpper(String s) { return s.toUpperCase(); }
}
