package academy.javaengineering.oop.practices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Practice: File Operations in Java IO
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating files and directories with NIO Files API
 * - Reading and writing file content
 * - Copying and moving files
 * - Getting file attributes and metadata
 * - Using try-with-resources for file operations
 */
public class Practices {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Practice: 02-file-operations ===\n");

        Path tempDir = Files.createTempDirectory("file-ops-practice");
        try {
            // Test Exercise 1: createAndWriteFile
            Path file1 = createAndWriteFile(tempDir.resolve("test.txt"), "Hello, World!");
            System.out.println("Exercise 1 - createAndWriteFile: "
                + (Files.exists(file1) && "Hello, World!".equals(Files.readString(file1)) ? "PASS" : "FAIL"));

            // Test Exercise 2: readFileLines
            List<String> lines = readFileLines(file1);
            System.out.println("Exercise 2 - readFileLines: "
                + (lines.size() == 1 && "Hello, World!".equals(lines.get(0)) ? "PASS" : "FAIL"));

            // Test Exercise 3: copyFile
            Path file2 = tempDir.resolve("copy.txt");
            long bytesCopied = copyFile(file1, file2);
            System.out.println("Exercise 3 - copyFile: "
                + (Files.exists(file2) && bytesCopied > 0 ? "PASS" : "FAIL"));

            // Test Exercise 4: getFileInfo
            String info = getFileInfo(file1);
            System.out.println("Exercise 4 - getFileInfo: "
                + (info != null && info.contains("size") && info.contains("regularFile") ? "PASS" : "FAIL"));

            // Test Exercise 5: createNestedDirectories
            Path nested = createNestedDirectories(tempDir, "a/b/c/d");
            System.out.println("Exercise 5 - createNestedDirectories: "
                + (Files.isDirectory(nested) ? "PASS" : "FAIL"));

        } finally {
            // Clean up
            deleteRecursive(tempDir);
        }
    }

    // TODO 1: Create a file and write content to it using Files.writeString()
    // Returns the path of the created file
    static Path createAndWriteFile(Path path, String content) throws IOException {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Read all lines from a file using Files.readAllLines()
    // Return the list of lines
    static List<String> readFileLines(Path path) throws IOException {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Copy a file from source to destination using Files.copy()
    // Use StandardCopyOption.REPLACE_EXISTING
    // Return the number of bytes copied (Files.size of destination)
    static long copyFile(Path source, Path destination) throws IOException {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 4: Get file information as a formatted string
    // Return something like: "size=13, regularFile=true, directory=false"
    // Use Files.isRegularFile(), Files.isDirectory(), Files.size()
    static String getFileInfo(Path path) throws IOException {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Create nested directories recursively using Files.createDirectories()
    // Example: createNestedDirectories(base, "a/b/c/d") should create base/a/b/c/d
    static Path createNestedDirectories(Path base, String nestedPath) throws IOException {
        // YOUR CODE HERE
        return null;
    }

    // Helper method to clean up temp directory recursively
    private static void deleteRecursive(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (var entries = Files.list(path)) {
                for (Path entry : (Iterable<Path>) entries::iterator) {
                    deleteRecursive(entry);
                }
            }
        }
        Files.deleteIfExists(path);
    }
}
