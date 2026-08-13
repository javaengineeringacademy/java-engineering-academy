package academy.javaengineering.text.examples;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Charset Examples - Practical demonstrations of Charset usage.
 * 
 * WHY CHARSET IS IMPORTANT:
 * - Platform independence: Different systems use different default charsets
 * - Data integrity: Wrong charset causes data corruption
 * - Security: Charset issues can lead to injection attacks
 * 
 * ENGINEERING DECISION: Always specify charset explicitly, never rely on default.
 */
public class CharsetExamples {

    public static void main(String[] args) {
        System.out.println("=== Charset Examples ===\n");

        // Example 1: Available Charsets
        example1_AvailableCharsets();

        // Example 2: Encoding/Decoding
        example2_EncodingDecoding();

        // Example 3: Platform Default vs Explicit
        example3_PlatformDefault();

        // Example 4: Charset Comparison
        example4_CharsetComparison();
    }

    /**
     * WHY: Understanding available charsets helps choose the right one.
     * 
     * INTERNAL: JVM supports multiple charsets. Available charsets depend on
     * the platform and installed fonts.
     */
    private static void example1_AvailableCharsets() {
        System.out.println("--- Example 1: Available Charsets ---");

        Map<String, Charset> charsets = Charset.availableCharsets();
        System.out.println("Total available charsets: " + charsets.size());

        System.out.println("\nCommon charsets:");
        System.out.println("UTF-8: " + StandardCharsets.UTF_8);
        System.out.println("US-ASCII: " + StandardCharsets.US_ASCII);
        System.out.println("ISO-8859-1: " + StandardCharsets.ISO_8859_1);
        System.out.println("UTF-16: " + StandardCharsets.UTF_16);
    }

    /**
     * WHY: Encoding/Decoding is essential for file and network operations.
     * 
     * ENGINEERING DECISION: Always use StandardCharsets constants, not strings.
     */
    private static void example2_EncodingDecoding() {
        System.out.println("\n--- Example 2: Encoding/Decoding ---");

        String text = "Hello, World! 🌍";

        // Encoding to bytes
        byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] asciiBytes = text.getBytes(StandardCharsets.US_ASCII);

        System.out.println("Original: " + text);
        System.out.println("UTF-8 bytes: " + utf8Bytes.length);
        System.out.println("ASCII bytes: " + asciiBytes.length);

        // Decoding from bytes
        String decoded = new String(utf8Bytes, StandardCharsets.UTF_8);
        System.out.println("Decoded: " + decoded);
        System.out.println("Equals original: " + text.equals(decoded));
    }

    /**
     * WHY: Platform default charset varies by OS and JVM settings.
     * 
     * PRODUCTION ISSUE: Code that works on Windows may fail on Linux.
     * SOLUTION: Always specify charset explicitly.
     */
    private static void example3_PlatformDefault() {
        System.out.println("\n--- Example 3: Platform Default vs Explicit ---");

        Charset defaultCharset = Charset.defaultCharset();
        System.out.println("Platform default charset: " + defaultCharset);

        // This is BAD practice (uses platform default)
        // byte[] bad = text.getBytes();

        // This is GOOD practice (explicit charset)
        String text = "Hello";
        byte[] good = text.getBytes(StandardCharsets.UTF_8);
        System.out.println("Explicit UTF-8 bytes: " + good.length);
    }

    /**
     * WHY: Different charsets have different byte sizes.
     * 
     * PERFORMANCE: UTF-8 is most efficient for English text.
     * UTF-16 is better for some Asian characters.
     */
    private static void example4_CharsetComparison() {
        System.out.println("\n--- Example 4: Charset Comparison ---");

        String text = "Hello";

        System.out.println("Text: " + text);
        System.out.println("UTF-8: " + text.getBytes(StandardCharsets.UTF_8).length + " bytes");
        System.out.println("UTF-16: " + text.getBytes(StandardCharsets.UTF_16).length + " bytes");
        System.out.println("ASCII: " + text.getBytes(StandardCharsets.US_ASCII).length + " bytes");
    }
}
