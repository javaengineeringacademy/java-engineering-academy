import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.stream.*;

/**
 * File Operations in Java IO - Demonstrates both legacy and modern APIs.
 *
 * <p>This class provides comprehensive examples of file operations using
 * java.io.File, java.nio.file.Path, and java.nio.file.Files.</p>
 *
 * @author JavaEngineering Academy
 * @version 1.0
 */
public final class FileOperationsExample {

    private FileOperationsExample() {
        // Utility class
    }

    // ==================== Basic Operations ====================

    /**
     * Creates a file using java.io.File (legacy approach).
     *
     * @param filePath the file path
     * @return true if file was created
     * @throws IOException if creation fails
     */
    public static boolean createFileWithFileClass(String filePath)
            throws IOException {
        File file = new File(filePath);
        return file.createNewFile();
    }

    /**
     * Creates a file using NIO Path (modern approach).
     *
     * @param path the file path
     * @return the created file path
     * @throws IOException if creation fails
     */
    public static Path createFileWithNio(Path path) throws IOException {
        return Files.createFile(path);
    }

    /**
     * Creates directories recursively.
     *
     * @param path the directory path to create
     * @return the created directory path
     * @throws IOException if creation fails
     */
    public static Path createDirectories(Path path) throws IOException {
        return Files.createDirectories(path);
    }

    // ==================== Read Operations ====================

    /**
     * Reads entire file content as string.
     *
     * @param path the file to read
     * @return file content as string
     * @throws IOException if read fails
     */
    public static String readAsString(Path path) throws IOException {
        return Files.readString(path);
    }

    /**
     * Reads file lines into a list.
     *
     * @param path the file to read
     * @return list of lines
     * @throws IOException if read fails
     */
    public static List<String> readAsLines(Path path) throws IOException {
        return Files.readAllLines(path);
    }

    /**
     * Reads file as byte array.
     *
     * @param path the file to read
     * @return byte array content
     * @throws IOException if read fails
     */
    public static byte[] readAsBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    /**
     * Streams file lines for memory-efficient processing.
     *
     * @param path the file to read
     * @return stream of lines
     * @throws IOException if read fails
     */
    public static Stream<String> streamLines(Path path) throws IOException {
        return Files.lines(path);
    }

    // ==================== Write Operations ====================

    /**
     * Writes string to file.
     *
     * @param path the file path
     * @param content the content to write
     * @throws IOException if write fails
     */
    public static void writeString(Path path, String content)
            throws IOException {
        Files.writeString(path, content);
    }

    /**
     * Writes lines to file.
     *
     * @param path the file path
     * @param lines the lines to write
     * @throws IOException if write fails
     */
    public static void writeLines(Path path, List<String> lines)
            throws IOException {
        Files.write(path, lines);
    }

    /**
     * Appends content to existing file.
     *
     * @param path the file path
     * @param content the content to append
     * @throws IOException if write fails
     */
    public static void appendToFile(Path path, String content)
            throws IOException {
        Files.writeString(path, content,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
    }

    // ==================== File Operations ====================

    /**
     * Copies file from source to destination.
     *
     * @param source source file
     * @param destination destination file
     * @throws IOException if copy fails
     */
    public static void copyFile(Path source, Path destination)
            throws IOException {
        Files.copy(source, destination,
            StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Moves file from source to destination.
     *
     * @param source source file
     * @param destination destination file
     * @throws IOException if move fails
     */
    public static void moveFile(Path source, Path destination)
            throws IOException {
        Files.move(source, destination,
            StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Deletes file if it exists.
     *
     * @param path the file to delete
     * @return true if file was deleted
     * @throws IOException if deletion fails
     */
    public static boolean deleteFile(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    // ==================== File Information ====================

    /**
     * Gets basic file attributes.
     *
     * @param path the file path
     * @return map of attribute names to values
     * @throws IOException if attribute access fails
     */
    public static Map<String, Object> getFileAttributes(Path path)
            throws IOException {
        Map<String, Object> attrs = new LinkedHashMap<>();

        BasicFileAttributes basic = Files.readAttributes(path,
            BasicFileAttributes.class);

        attrs.put("size", basic.size());
        attrs.put("isRegularFile", basic.isRegularFile());
        attrs.put("isDirectory", basic.isDirectory());
        attrs.put("isSymbolicLink", basic.isSymbolicLink());
        attrs.put("creationTime", basic.creationTime());
        attrs.put("lastModifiedTime", basic.lastModifiedTime());
        attrs.put("lastAccessTime", basic.lastAccessTime());

        return attrs;
    }

    /**
     * Gets POSIX file permissions (Unix-like systems).
     *
     * @param path the file path
     * @return permission string (e.g., "rwxr-xr--")
     * @throws IOException if permission access fails
     */
    public static String getPermissions(Path path) throws IOException {
        Set<PosixFilePermission> perms =
            Files.getPosixFilePermissions(path);
        return PosixFilePermissions.toString(perms);
    }

    // ==================== Directory Operations ====================

    /**
     * Lists all files in a directory.
     *
     * @param dir the directory to list
     * @return list of file paths
     * @throws IOException if listing fails
     */
    public static List<Path> listDirectory(Path dir) throws IOException {
        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(dir)) {
            List<Path> result = new ArrayList<>();
            for (Path entry : stream) {
                result.add(entry);
            }
            return result;
        }
    }

    /**
     * Lists files matching a glob pattern.
     *
     * @param dir the directory to search
     * @param glob the glob pattern
     * @return list of matching file paths
     * @throws IOException if listing fails
     */
    public static List<Path> listDirectory(Path dir, String glob)
            throws IOException {
        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(dir, glob)) {
            List<Path> result = new ArrayList<>();
            for (Path entry : stream) {
                result.add(entry);
            }
            return result;
        }
    }

    /**
     * Recursively walks directory tree.
     *
     * @param root the root directory
     * @return stream of all paths
     * @throws IOException if walk fails
     */
    public static Stream<Path> walkDirectory(Path root)
            throws IOException {
        return Files.walk(root);
    }

    /**
     * Calculates total directory size.
     *
     * @param dir the directory
     * @return total size in bytes
     * @throws IOException if calculation fails
     */
    public static long calculateDirectorySize(Path dir)
            throws IOException {
        try (Stream<Path> walk = Files.walk(dir)) {
            return walk
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try { return Files.size(path); }
                    catch (IOException e) { return 0L; }
                })
                .sum();
        }
    }

    // ==================== File Search ====================

    /**
     * Finds files by extension.
     *
     * @param root the root directory
     * @param extension the file extension (without dot)
     * @return stream of matching paths
     * @throws IOException if search fails
     */
    public static Stream<Path> findByExtension(Path root,
            String extension) throws IOException {
        String glob = "*." + extension;
        return Files.walk(root)
            .filter(path -> Files.isRegularFile(path) &&
                path.toString().endsWith("." + extension));
    }

    /**
     * Finds files by name pattern.
     *
     * @param root the root directory
     * @param pattern the name pattern
     * @return stream of matching paths
     * @throws IOException if search fails
     */
    public static Stream<Path> findByName(Path root, String pattern)
            throws IOException {
        return Files.walk(root)
            .filter(path -> Files.isRegularFile(path) &&
                path.getFileName().toString().contains(pattern));
    }

    // ==================== Main Method ====================

    /**
     * Demonstrates file operations.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== File Operations Demo ===");

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"),
            "file-ops-demo");

        try {
            // Create directory
            createDirectories(tempDir);
            System.out.println("Created directory: " + tempDir);

            // Create and write file
            Path testFile = tempDir.resolve("test.txt");
            writeString(testFile, "Hello, File Operations!\nLine 2\nLine 3");
            System.out.println("\nCreated file: " + testFile);

            // Read file
            System.out.println("\nFile content:");
            System.out.println(readAsString(testFile));

            // Read as lines
            System.out.println("As lines:");
            readAsLines(testFile).forEach(line ->
                System.out.println("  | " + line));

            // Append to file
            appendToFile(testFile, "Appended line 4\n");
            System.out.println("\nAfter append:");
            System.out.println(readAsString(testFile));

            // File attributes
            System.out.println("File attributes:");
            getFileAttributes(testFile).forEach((k, v) ->
                System.out.println("  " + k + ": " + v));

            // Copy file
            Path copyFile = tempDir.resolve("test-copy.txt");
            copyFile(testFile, copyFile);
            System.out.println("\nCopied to: " + copyFile);

            // Move file
            Path movedFile = tempDir.resolve("test-moved.txt");
            moveFile(copyFile, movedFile);
            System.out.println("Moved to: " + movedFile);

            // Directory listing
            System.out.println("\nDirectory contents:");
            listDirectory(tempDir).forEach(path ->
                System.out.println("  " + path.getFileName()));

            // Directory size
            System.out.println("\nDirectory size: " +
                calculateDirectorySize(tempDir) + " bytes");

            // Search for .txt files
            System.out.println("\nFound .txt files:");
            findByExtension(tempDir, "txt")
                .forEach(path ->
                    System.out.println("  " + path.getFileName()));

            // Cleanup
            deleteFile(movedFile);
            deleteFile(testFile);
            Files.deleteIfExists(tempDir);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
