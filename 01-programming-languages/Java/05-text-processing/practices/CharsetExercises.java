package academy.javaengineering.exercises;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Exercises: Charset and Encoding Operations
 *
 * Complete the TODO sections below.
 */
public class CharsetExercises {

    // TODO 1: Convert a string to bytes using UTF-8 charset
    public byte[] toUtf8Bytes(String input) {
        // TODO: implement using getBytes(StandardCharsets.UTF_8)
        return new byte[0];
    }

    // TODO 2: Convert bytes back to a string using UTF-8
    public String fromUtf8Bytes(byte[] bytes) {
        // TODO: implement using new String(bytes, StandardCharsets.UTF_8)
        return "";
    }

    // TODO 3: Check if two byte arrays are equal when decoded from different charsets
    public boolean areEncodingsEquivalent(String input, Charset charset1, Charset charset2) {
        // TODO: encode with both charsets and compare
        return false;
    }

    // TODO 4: Calculate the byte length of a string in a specific charset
    public int byteLength(String input, Charset charset) {
        // TODO: implement using getBytes(charset).length
        return 0;
    }

    // TODO 5: Safely decode bytes, replacing malformed sequences with a replacement char
    public String safeDecode(byte[] bytes, Charset charset) {
        // TODO: implement using decoder with CodingErrorAction.REPLACE
        return "";
    }

    // TODO 6: Detect if a byte array looks like UTF-8 (has BOM or valid UTF-8 sequences)
    public boolean looksLikeUtf8(byte[] bytes) {
        // TODO: check for UTF-8 BOM (0xEF, 0xBB, 0xBF) or valid sequences
        return false;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        CharsetExercises exercises = new CharsetExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== CharsetExercises Tests ===\n");

        // Test 1
        total++;
        byte[] utf8 = exercises.toUtf8Bytes("Hello");
        if (utf8.length == 5 && utf8[0] == 'H') {
            System.out.println("Test 1 PASSED: toUtf8Bytes");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: toUtf8Bytes");
        }

        // Test 2
        total++;
        byte[] bytes = "World".getBytes(StandardCharsets.UTF_8);
        String decoded = exercises.fromUtf8Bytes(bytes);
        if ("World".equals(decoded)) {
            System.out.println("Test 2 PASSED: fromUtf8Bytes");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: fromUtf8Bytes");
        }

        // Test 3
        total++;
        boolean equiv = exercises.areEncodingsEquivalent("ASCII text", StandardCharsets.UTF_8, StandardCharsets.US_ASCII);
        if (equiv) {
            System.out.println("Test 3 PASSED: areEncodingsEquivalent");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: areEncodingsEquivalent");
        }

        // Test 4
        total++;
        int len = exercises.byteLength("Hello", StandardCharsets.UTF_8);
        if (len == 5) {
            System.out.println("Test 4 PASSED: byteLength");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: byteLength");
        }

        // Test 5
        total++;
        String safe = exercises.safeDecode(new byte[]{72, 101, 108, 108, 111}, StandardCharsets.UTF_8);
        if ("Hello".equals(safe)) {
            System.out.println("Test 5 PASSED: safeDecode");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: safeDecode");
        }

        // Test 6
        total++;
        byte[] utf8Bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 72, 105};
        if (exercises.looksLikeUtf8(utf8Bom)) {
            System.out.println("Test 6 PASSED: looksLikeUtf8");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: looksLikeUtf8");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
