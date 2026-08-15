package academy.javaengineering.exercises;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;

/**
 * Exercises: NIO (Buffers, Channels, Files)
 *
 * Complete the TODO sections below.
 */
public class NioExercises {

    // TODO 1: Read a file using NIO Channels
    // Return file contents as a String
    public String readWithChannel(String filePath) throws IOException {
        // TODO: implement using FileChannel and ByteBuffer
        return "";
    }

    // TODO 2: Write to a file using NIO
    // Return number of bytes written
    public int writeWithChannel(String filePath, String content) throws IOException {
        // TODO: implement using FileChannel and ByteBuffer
        return 0;
    }

    // TODO 3: Copy a file using NIO transferFrom
    // Return true on success
    public boolean nioCopy(String source, String dest) throws IOException {
        // TODO: implement using FileChannel.transferFrom
        return false;
    }

    // TODO 4: Read a file line by line using NIO
    // Return list of lines
    public List<String> readLinesNio(String filePath) throws IOException {
        // TODO: implement using Files.lines or BufferedReader with NIO
        return new ArrayList<>();
    }

    // TODO 5: Find all files in a directory tree matching a pattern
    // Use NIO Files.walk
    public List<Path> findFiles(Path dir, String globPattern) throws IOException {
        // TODO: implement
        return new ArrayList<>();
    }

    // TODO 6: Get file attributes using NIO
    // Return a map with keys: "size", "isDirectory", "lastModified", "isReadable"
    public Map<String, Object> getFileAttributes(String filePath) throws IOException {
        // TODO: implement using Files.readAttributes or Files.getAttribute
        return new LinkedHashMap<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        NioExercises exercises = new NioExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== NioExercises Tests ===\n");

        // Test 1
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_nio_read_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "NIO Channel read test");
            String content = exercises.readWithChannel(tempFile);
            if ("NIO Channel read test".equals(content)) {
                System.out.println("Test 1 PASSED: readWithChannel");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: readWithChannel - '" + content + "'");
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_nio_write_" + System.nanoTime() + ".txt";
            int written = exercises.writeWithChannel(tempFile, "NIO write test");
            String content = Files.readString(Path.of(tempFile));
            if (written == 14 && "NIO write test".equals(content)) {
                System.out.println("Test 2 PASSED: writeWithChannel");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: writeWithChannel - written=" + written);
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            String src = System.getProperty("java.io.tmpdir") + "/test_nio_copy_src_" + System.nanoTime() + ".txt";
            String dst = System.getProperty("java.io.tmpdir") + "/test_nio_copy_dst_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(src), "Copy me with NIO");
            boolean success = exercises.nioCopy(src, dst);
            String content = Files.readString(Path.of(dst));
            if (success && "Copy me with NIO".equals(content)) {
                System.out.println("Test 3 PASSED: nioCopy");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: nioCopy");
            }
            Files.deleteIfExists(Path.of(src));
            Files.deleteIfExists(Path.of(dst));
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_nio_lines_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "alpha\nbeta\ngamma");
            List<String> lines = exercises.readLinesNio(tempFile);
            if (lines.size() == 3 && "alpha".equals(lines.get(0)) && "gamma".equals(lines.get(2))) {
                System.out.println("Test 4 PASSED: readLinesNio");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: readLinesNio - " + lines);
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            Path tempDir = Files.createTempDirectory("test_nio_find");
            Files.writeString(tempDir.resolve("file1.java"), "code");
            Files.writeString(tempDir.resolve("file2.txt"), "text");
            Files.writeString(tempDir.resolve("file3.java"), "code2");
            Files.createDirectories(tempDir.resolve("subdir"));
            Files.writeString(tempDir.resolve("subdir/file4.java"), "code3");
            List<Path> javaFiles = exercises.findFiles(tempDir, "*.java");
            if (javaFiles.size() == 3) {
                System.out.println("Test 5 PASSED: findFiles");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: findFiles - found " + javaFiles.size());
            }
            Files.walk(tempDir).sorted(Comparator.reverseOrder()).forEach(p -> {
                try { Files.deleteIfExists(p); } catch (Exception e) {}
            });
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        // Test 6
        total++;
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/test_nio_attrs_" + System.nanoTime() + ".txt";
            Files.writeString(Path.of(tempFile), "attributes test");
            Map<String, Object> attrs = exercises.getFileAttributes(tempFile);
            if (attrs.containsKey("size") && attrs.containsKey("isDirectory")
                && attrs.containsKey("lastModified") && attrs.containsKey("isReadable")) {
                System.out.println("Test 6 PASSED: getFileAttributes");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: getFileAttributes - " + attrs.keySet());
            }
            Files.deleteIfExists(Path.of(tempFile));
        } catch (Exception e) {
            System.out.println("Test 6 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
