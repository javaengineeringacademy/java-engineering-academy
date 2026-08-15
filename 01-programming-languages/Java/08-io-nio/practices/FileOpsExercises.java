package academy.javaengineering.exercises;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Exercises: File Operations and Paths
 *
 * Complete the TODO sections below.
 */
public class FileOpsExercises {

    // TODO 1: Count lines in a file
    // Return -1 if file doesn't exist
    public long countLines(String filePath) {
        // TODO: implement
        return -1;
    }

    // TODO 2: Find all files with a given extension in a directory (non-recursive)
    // Return list of file names (not full paths)
    public List<String> findFilesByExtension(String dirPath, String extension) {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 3: Read a file and return a map of word frequencies
    // Words are lowercase, ignore punctuation
    public Map<String, Integer> wordFrequency(String filePath) {
        // TODO: implement
        return new LinkedHashMap<>();
    }

    // TODO 4: Copy a file to a new location
    // Create parent directories if needed
    // Return true on success, false on failure
    public boolean copyFile(String source, String destination) {
        // TODO: implement
        return false;
    }

    // TODO 5: Get file size in bytes, or -1 if not found
    public long getFileSize(String filePath) {
        // TODO: implement
        return -1;
    }

    // TODO 6: List directory contents sorted by size (largest first)
    // Return list of file names
    public List<String> listBySize(String dirPath) {
        // TODO: implement
        return new ArrayList<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        FileOpsExercises exercises = new FileOpsExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== FileOpsExercises Tests ===\n");

        // Test 1
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_lines_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "line1\nline2\nline3\n");
            long count = exercises.countLines(tempFile);
            if (count == 3) {
                System.out.println("Test 1a PASSED: countLines");
                passed++;
            } else {
                System.out.println("Test 1a FAILED: countLines - expected 3, got " + count);
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 1a FAILED: " + e.getMessage());
        }

        total++;
        long missing = exercises.countLines("/nonexistent/file.txt");
        if (missing == -1) {
            System.out.println("Test 1b PASSED: countLines missing file");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: countLines should return -1");
        }

        // Test 2
        total++;
        try {
            String tempDir = System.getProperty("java.io.tmpdir") + "/test_ext_" + System.nanoTime();
            Files.createDirectories(Path.of(tempDir));
            Files.writeString(Path.of(tempDir + "/a.java"), "code");
            Files.writeString(Path.of(tempDir + "/b.txt"), "text");
            Files.writeString(Path.of(tempDir + "/c.java"), "code2");
            List<String> javaFiles = exercises.findFilesByExtension(tempDir, ".java");
            if (javaFiles.size() == 2 && javaFiles.contains("a.java") && javaFiles.contains("c.java")) {
                System.out.println("Test 2 PASSED: findFilesByExtension");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: findFilesByExtension - " + javaFiles);
            }
            Files.deleteIfExists(Path.of(tempDir + "/a.java"));
            Files.deleteIfExists(Path.of(tempDir + "/b.txt"));
            Files.deleteIfExists(Path.of(tempDir + "/c.java"));
            Files.deleteIfExists(Path.of(tempDir));
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_freq_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "hello world hello java world");
            Map<String, Integer> freq = exercises.wordFrequency(tempFile);
            if (freq.get("hello") == 2 && freq.get("world") == 2 && freq.get("java") == 1) {
                System.out.println("Test 3 PASSED: wordFrequency");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: wordFrequency - " + freq);
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            String src = System.getProperty("java.io.tmpdir") + "/test_copy_src_" + System.nanoTime() + ".txt";
            String dst = System.getProperty("java.io.tmpdir") + "/test_copy_dst_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(src), "copy me");
            boolean success = exercises.copyFile(src, dst);
            String content = Files.readString(Path.of(dst));
            if (success && "copy me".equals(content)) {
                System.out.println("Test 4 PASSED: copyFile");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: copyFile");
            }
            Files.deleteIfExists(Path.of(src));
            Files.deleteIfExists(Path.of(dst));
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_size_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "12345");
            long size = exercises.getFileSize(tempFile);
            if (size == 5) {
                System.out.println("Test 5 PASSED: getFileSize");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: getFileSize - expected 5, got " + size);
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
