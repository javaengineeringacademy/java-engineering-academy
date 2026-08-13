package academy.javaengineering.text.internals;

public class StringBufferInternals {

    public static void main(String[] args) {
        System.out.println("=== StringBuffer Internals ===\n");

        // 1. StringBuffer Basics
        System.out.println("--- StringBuffer Basics ---");
        StringBuffer sb = new StringBuffer();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println("Result: " + sb.toString());
        System.out.println("Capacity: " + sb.capacity());

        // 2. Thread Safety
        System.out.println("\n--- Thread Safety ---");
        System.out.println("StringBuffer: synchronized methods");
        System.out.println("StringBuilder: not synchronized");
        System.out.println("StringBuffer: thread-safe but slower");

        // 3. StringBuffer vs StringBuilder
        System.out.println("\n--- StringBuffer vs StringBuilder ---");
        System.out.println("StringBuffer: synchronized (safe)");
        System.out.println("StringBuilder: not synchronized (fast)");
        System.out.println("Use StringBuilder for single-threaded");
        System.out.println("Use StringBuffer for multi-threaded");
    }
}
