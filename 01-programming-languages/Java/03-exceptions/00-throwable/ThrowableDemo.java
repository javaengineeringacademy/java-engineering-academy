package academy.javaengineering.exceptions.throwable;

/**
 * Demonstrates the core APIs of {@link java.lang.Throwable}.
 *
 * <p><b>Complexity:</b> O(1) per API call; O(depth) for fillInStackTrace.</p>
 * <p><b>Thread-safety:</b> Throwable is safe to share after construction (fields are
 * effectively immutable in normal use). Stack trace mutation via setStackTrace
 * is not synchronized.</p>
 * <p><b>Key characteristics:</b> Serializable, carries message/cause/stackTrace/suppressed.</p>
 */
public class ThrowableDemo {

    public static void main(String[] args) {
        demoCreateWithMessage();
        demoCauseChaining();
        demoStackTraceManipulation();
        demoSuppressedExceptions();
        demoToStringAndPrint();
    }

    // --- Creating a Throwable with a message ---

    private static void demoCreateWithMessage() {
        System.out.println("=== Demo: Create with message ===");

        Throwable t = new Throwable("disk write failed");
        System.out.println("getMessage():        " + t.getMessage());
        System.out.println("getLocalizedMessage(): " + t.getLocalizedMessage());
        System.out.println("toString():         " + t.toString());
        System.out.println();
    }

    // --- Cause chaining ---

    private static void demoCauseChaining() {
        System.out.println("=== Demo: Cause chaining ===");

        IOException original = new IOException("connection refused");
        RuntimeException wrapped = new RuntimeException("request failed", original);

        System.out.println("wrapped.getMessage():  " + wrapped.getMessage());
        System.out.println("wrapped.getCause():    " + wrapped.getCause());
        System.out.println("root cause:            " + getCauseChain(wrapped));
        System.out.println();
    }

    private static String getCauseChain(Throwable t) {
        StringBuilder sb = new StringBuilder();
        Throwable current = t;
        while (current != null) {
            if (sb.length() > 0) {
                sb.append(" -> ");
            }
            sb.append(current.getClass().getSimpleName());
            current = current.getCause();
        }
        return sb.toString();
    }

    // --- Stack trace manipulation ---

    private static void demoStackTraceManipulation() {
        System.out.println("=== Demo: Stack trace manipulation ===");

        Throwable t = new Throwable();

        StackTraceElement[] original = t.getStackTrace();
        System.out.println("Original stack depth: " + original.length);
        System.out.println("Top frame:            " + original[0]);
        System.out.println();

        // Trim to only the first 2 frames
        StackTraceElement[] trimmed = new StackTraceElement[2];
        System.arraycopy(original, 0, trimmed, 0, 2);
        t.setStackTrace(trimmed);

        System.out.println("After setStackTrace depth: " + t.getStackTrace().length);
        System.out.println();
    }

    // --- Suppressed exceptions ---

    private static void demoSuppressedExceptions() {
        System.out.println("=== Demo: Suppressed exceptions ===");

        RuntimeException primary = new RuntimeException("primary failure");
        primary.addSuppressed(new IOException("resource A leaked"));
        primary.addSuppressed(new IOException("resource B leaked"));

        System.out.println("Primary:  " + primary.getMessage());
        System.out.println("Suppressed count: " + primary.getSuppressed().length);
        for (Throwable suppressed : primary.getSuppressed()) {
            System.out.println("  - " + suppressed);
        }
        System.out.println();
    }

    // --- toString and printStackTrace ---

    private static void demoToStringAndPrint() {
        System.out.println("=== Demo: toString and printStackTrace ===");

        Throwable t = new IllegalArgumentException("null key");
        System.out.println("toString(): " + t.toString());
        System.out.println();

        System.out.println("printStackTrace():");
        t.printStackTrace(System.out);
        System.out.println();
    }
}
