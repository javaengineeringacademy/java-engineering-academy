package academy.javaengineering.text.internals;

public class StringBuilderInternals {

    public static void main(String[] args) {
        System.out.println("=== StringBuilder Internals ===\n");

        // 1. StringBuilder Basics
        System.out.println("--- StringBuilder Basics ---");
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("Result: " + sb.toString());
        System.out.println("Capacity: " + sb.capacity());
        System.out.println("Length: " + sb.length());

        // 2. StringBuilder Methods
        System.out.println("\n--- StringBuilder Methods ---");
        sb.insert(5, ",");
        System.out.println("insert: " + sb);
        sb.delete(5, 6);
        System.out.println("delete: " + sb);
        sb.replace(6, 11, "Java");
        System.out.println("replace: " + sb);
        sb.reverse();
        System.out.println("reverse: " + sb);

        // 3. StringBuilder vs String
        System.out.println("\n--- StringBuilder vs String ---");
        System.out.println("String: immutable, thread-safe");
        System.out.println("StringBuilder: mutable, not thread-safe");
        System.out.println("Use StringBuilder for modifications");
    }
}
