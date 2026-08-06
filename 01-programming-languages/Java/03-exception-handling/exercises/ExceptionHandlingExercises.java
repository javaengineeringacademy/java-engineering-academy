package academy.javaengineering.exercises;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Exercises: Exception Handling Best Practices and Try-with-Resources
 *
 * Complete the TODO sections below.
 */
public class ExceptionHandlingExercises {

    // TODO 1: Implement readFirstLine using try-with-resources
    // Read the first line of a file and return it
    // If file doesn't exist, return "FILE_NOT_FOUND"
    // If file is empty, return "EMPTY_FILE"
    public String readFirstLine(String filePath) {
        // TODO: implement using try-with-resources
        return "";
    }

    // TODO 2: Implement writeToFile safely
    // Write content to a file, creating parent directories if needed
    // Return true on success, false on any IOException
    public boolean writeToFile(String filePath, String content) {
        // TODO: implement
        return false;
    }

    // TODO 3: Implement multiResourceDemo
    // Open two BufferedReader resources (two different files)
    // Read first line from each and return as a String array [line1, line2]
    // If either file fails, return null
    public String[] multiResourceDemo(String file1, String file2) {
        // TODO: implement using try-with-resources
        return null;
    }

    // TODO 4: Implement safeMapGet that handles NullPointerException gracefully
    // Use the map parameter to get value for key
    // If key doesn't exist, return defaultValue
    // If map is null, return null
    public <K, V> V safeMapGet(java.util.Map<K, V> map, K key, V defaultValue) {
        // TODO: implement
        return null;
    }

    // TODO 5: Implement exceptionTransparency
    // Catch Exception, log the exception type, and rethrow as RuntimeException
    // The RuntimeException should have the original exception as cause
    public <T> T exceptionTransparency(ThrowingSupplier<T> supplier) {
        // TODO: implement
        return null;
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    // TODO 6: Implement handleCheckedException with proper wrapping
    // Call a method that throws IOException, wrap it in RuntimeException
    // and include a meaningful message
    public String handleCheckedException(String filePath) {
        // TODO: implement
        return "";
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        ExceptionHandlingExercises exercises = new ExceptionHandlingExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ExceptionHandlingExercises Tests ===\n");

        // Test 1
        total++;
        String result = exercises.readFirstLine("/nonexistent/file.txt");
        if ("FILE_NOT_FOUND".equals(result)) {
            System.out.println("Test 1 PASSED: readFirstLine - file not found");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: readFirstLine - expected FILE_NOT_FOUND, got " + result);
        }

        // Test 2
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_write_" + System.nanoTime() + ".txt";
            boolean success = exercises.writeToFile(tempFile, "Hello World");
            String content = Files.readString(Path.of(tempFile));
            if (success && "Hello World".equals(content)) {
                System.out.println("Test 2 PASSED: writeToFile");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: writeToFile - success=" + success + " content=" + content);
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: writeToFile - " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            String file1 = tempDir + "/test_multi1_" + System.nanoTime() + ".txt";
            String file2 = tempDir + "/test_multi2_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(file1), "Line1");
            Files.writeString(Path.of(file2), "Line2");
            String[] lines = exercises.multiResourceDemo(file1, file2);
            if (lines != null && "Line1".equals(lines[0]) && "Line2".equals(lines[1])) {
                System.out.println("Test 3 PASSED: multiResourceDemo");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: multiResourceDemo");
            }
            Files.deleteIfExists(Path.of(file1));
            Files.deleteIfExists(Path.of(file2));
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: multiResourceDemo - " + e.getMessage());
        }

        // Test 4
        total++;
        java.util.Map<String, Integer> map = java.util.Map.of("a", 1, "b", 2);
        Integer val = exercises.safeMapGet(map, "a", 99);
        Integer missing = exercises.safeMapGet(map, "c", 99);
        Integer nullMap = exercises.safeMapGet(null, "a", 99);
        if (val == 1 && missing == 99 && nullMap == null) {
            System.out.println("Test 4 PASSED: safeMapGet");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: safeMapGet");
        }

        // Test 5
        total++;
        try {
            Integer res = exercises.exceptionTransparency(() -> 42);
            if (res != null && res == 42) {
                System.out.println("Test 5 PASSED: exceptionTransparency success");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: exceptionTransparency - expected 42");
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: exceptionTransparency - " + e.getMessage());
        }

        // Test 6
        total++;
        String fileResult = exercises.handleCheckedException("/nonexistent.txt");
        if (fileResult == null || fileResult.isEmpty() || fileResult.contains("Error")) {
            System.out.println("Test 6 PASSED: handleCheckedException");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: handleCheckedException");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
