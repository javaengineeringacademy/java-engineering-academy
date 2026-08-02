package academy.javaengineering.fileio;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.stream.*;

/**
 * File I/O - Comprehensive coverage of Java File Operations.
 *
 * Covers: File class, Path class, Files utility, BasicFileAttributes,
 * FileTime, copy/move/delete, readAllLines, walkFileTree, FileVisitor,
 * WatchService, file permissions,磁盘空间, temporary files, and more.
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class Example {

    public static void main(String[] args) throws Exception {
        System.out.println("=== File I/O Comprehensive Example ===\n");
        demonstrateFileClass();
        demonstratePathClass();
        demonstrateFilesUtility();
        demonstrateFileAttributes();
        demonstrateFilePermissions();
        demonstrateTemporaryFiles();
        demonstrateDiskSpace();
        demonstrateLineNumberReader();
        demonstrateBufferedReadWrite();
        demonstrateDataStreams();
    }

    // =========================================================================
    // 1. FILE CLASS (Legacy java.io.File)
    // =========================================================================

    /**
     * Demonstrates the legacy File class operations.
     * File class predates NIO.2 and is still widely used.
     */
    public static void demonstrateFileClass() {
        System.out.println("--- 1. File Class (Legacy) ---");

        File file = new File("example.txt");
        File dir = new File("testDir");

        // Create file and directory
        try {
            file.createNewFile();
            dir.mkdir();

            // File metadata
            System.out.println("Exists: " + file.exists());
            System.out.println("Is File: " + file.isFile());
            System.out.println("Is Directory: " + file.isDirectory());
            System.out.println("Name: " + file.getName());
            System.out.println("Absolute Path: " + file.getAbsolutePath());
            System.out.println("Parent: " + file.getParent());
            System.out.println("Can Read: " + file.canRead());
            System.out.println("Can Write: " + file.canWrite());
            System.out.println("Hidden: " + file.isHidden());
            System.out.println("Length: " + file.length() + " bytes");
            System.out.println("Last Modified: " + new Date(file.lastModified()));

            // Rename
            File renamed = new File("renamed.txt");
            file.renameTo(renamed);
            System.out.println("Renamed to: " + renamed.getName());

            // Delete
            renamed.delete();
            dir.delete();

            // List root directories
            File[] roots = File.listRoots();
            System.out.println("Root directories: " + roots.length);

        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        }

        System.out.println();
    }

    // =========================================================================
    // 2. PATH CLASS (NIO.2)
    // =========================================================================

    /**
     * Demonstrates Path class operations from java.nio.file.
     * Path is the modern replacement for File for path manipulation.
     */
    public static void demonstratePathClass() {
        System.out.println("--- 2. Path Class (NIO.2) ---");

        // Create paths
        Path currentPath = Path.of(".");
        Path filePath = Path.of("src", "main", "java", "Example.java");
        Path absolutePath = Path.of("/Users/pooja/project/file.txt");

        // Path navigation
        System.out.println("Current: " + currentPath);
        System.out.println("File Path: " + filePath);
        System.out.println("File Name: " + filePath.getFileName());
        System.out.println("Name Count: " + filePath.getNameCount());
        System.out.println("Name(0): " + filePath.getName(0));
        System.out.println("Parent: " + filePath.getParent());
        System.out.println("Root: " + absolutePath.getRoot());
        System.out.println("Is Absolute: " + filePath.isAbsolute());

        // Path resolution
        Path resolved = currentPath.resolve("src/main");
        System.out.println("Resolved: " + resolved);

        // Relativize
        Path base = Path.of("/Users/pooja/project");
        Path target = Path.of("/Users/pooja/project/src/main/Example.java");
        Path relative = base.relativize(target);
        System.out.println("Relative: " + relative);

        // Normalize
        Path messy = Path.of("/Users/pooja/project/../project/./src/./main");
        System.out.println("Normalized: " + messy.normalize());

        // To absolute
        System.out.println("Absolute: " + currentPath.toAbsolutePath());

        // To real path (resolves symlinks)
        try {
            System.out.println("Real Path: " + currentPath.toRealPath());
        } catch (IOException e) {
            System.err.println("Cannot resolve real path: " + e.getMessage());
        }

        // Compare paths
        Path p1 = Path.of("/a/b/c");
        Path p2 = Path.of("/a/b/d");
        System.out.println("Compare: " + p1.compareTo(p2));

        // StartsWith / EndsWith
        System.out.println("Starts with /a: " + p1.startsWith("/a"));
        System.out.println("Ends with c: " + p1.endsWith("c"));

        System.out.println();
    }

    // =========================================================================
    // 3. FILES UTILITY CLASS
    // =========================================================================

    /**
     * Demonstrates the Files utility class for common file operations.
     * Files is the primary class for NIO.2 file operations.
     */
    public static void demonstrateFilesUtility() throws IOException {
        System.out.println("--- 3. Files Utility Class ---");

        Path tempDir = Files.createTempDirectory("demo");
        Path source = tempDir.resolve("source.txt");
        Path target = tempDir.resolve("target.txt");
        Path subDir = tempDir.resolve("subdir");

        // Create file with content
        List<String> lines = List.of(
            "Line 1: Hello World",
            "Line 2: Java File I/O",
            "Line 3: NIO.2 API",
            "Line 4: Files utility",
            "Line 5: Path operations"
        );
        Files.write(source, lines);
        System.out.println("Created: " + source);

        // Read all lines
        List<String> readBack = Files.readAllLines(source);
        System.out.println("Read lines: " + readBack.size());

        // Read as string
        String content = Files.readString(source);
        System.out.println("Content length: " + content.length());

        // Read all bytes
        byte[] bytes = Files.readAllBytes(source);
        System.out.println("Bytes read: " + bytes.length);

        // Copy file
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Copied to: " + target);

        // Move file
        Path moved = tempDir.resolve("moved.txt");
        Files.move(target, moved, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Moved to: " + moved);

        // Create directories
        Files.createDirectories(subDir.resolve("nested").resolve("deep"));
        System.out.println("Created nested dirs");

        // List directory
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDir)) {
            System.out.println("Directory entries:");
            for (Path entry : stream) {
                System.out.println("  " + entry.getFileName());
            }
        }

        // Walk file tree
        System.out.println("Walking file tree:");
        Files.walk(tempDir)
             .limit(10)
             .forEach(p -> System.out.println("  " + tempDir.relativize(p)));

        // Find files
        System.out.println("Finding .txt files:");
        Files.find(tempDir, 5, (p, attr) -> p.toString().endsWith(".txt"))
             .forEach(p -> System.out.println("  " + tempDir.relativize(p)));

        // Read lines with stream
        System.out.println("Streamed lines:");
        try (Stream<String> stream = Files.lines(source)) {
            stream.filter(l -> l.contains("Line"))
                  .forEach(l -> System.out.println("  " + l));
        }

        // Write with options
        Path appendFile = tempDir.resolve("append.txt");
        Files.writeString(appendFile, "First line\n", StandardOpenOption.CREATE);
        Files.writeString(appendFile, "Second line\n", StandardOpenOption.APPEND);
        System.out.println("Append file created");

        // Delete
        Files.deleteIfExists(moved);
        Files.deleteIfExists(source);
        System.out.println("Files deleted");

        // Delete recursively
        deleteDirectory(tempDir);
        System.out.println("Temp dir cleaned up");

        System.out.println();
    }

    // =========================================================================
    // 4. FILE ATTRIBUTES
    // =========================================================================

    /**
     * Demonstrates reading file attributes using NIO.2.
     */
    public static void demonstrateFileAttributes() throws IOException {
        System.out.println("--- 4. File Attributes ---");

        Path tempFile = Files.createTempFile("attrDemo", ".txt");
        Files.writeString(tempFile, "Attribute demo content");

        // BasicFileAttributes
        BasicFileAttributes basicAttr = Files.readAttributes(tempFile, BasicFileAttributes.class);
        System.out.println("Creation Time: " + basicAttr.creationTime());
        System.out.println("Last Access Time: " + basicAttr.lastAccessTime());
        System.out.println("Last Modified Time: " + basicAttr.lastModifiedTime());
        System.out.println("Size: " + basicAttr.size() + " bytes");
        System.out.println("Is Regular File: " + basicAttr.isRegularFile());
        System.out.println("Is Directory: " + basicAttr.isDirectory());
        System.out.println("Is Symbolic Link: " + basicAttr.isSymbolicLink());
        System.out.println("Is Other: " + basicAttr.isOther());

        // DosFileAttributes (Windows-specific, limited on Unix)
        try {
            DosFileAttributes dosAttr = Files.readAttributes(tempFile, DosFileAttributes.class);
            System.out.println("ReadOnly: " + dosAttr.isReadOnly());
            System.out.println("Hidden: " + dosAttr.isHidden());
            System.out.println("System: " + dosAttr.isSystem());
            System.out.println("Archive: " + dosAttr.isArchive());
        } catch (UnsupportedOperationException e) {
            System.out.println("DosFileAttributes not supported on this OS");
        }

        // PosixFileAttributes (Unix/Linux)
        try {
            PosixFileAttributes posixAttr = Files.readAttributes(tempFile, PosixFileAttributes.class);
            System.out.println("Owner: " + posixAttr.owner().getName());
            System.out.println("Group: " + posixAttr.group().getName());
            System.out.println("Permissions: " + posixAttr.permissions());
        } catch (UnsupportedOperationException e) {
            System.out.println("PosixFileAttributes not supported on this OS");
        }

        // FileTime
        FileTime now = FileTime.fromMillis(System.currentTimeMillis());
        Files.setLastModifiedTime(tempFile, now);
        System.out.println("Modified time set to: " + now);

        // Check individual attributes
        System.out.println("isRegularFile (Files): " + Files.isRegularFile(tempFile));
        System.out.println("isDirectory (Files): " + Files.isDirectory(tempFile));
        System.out.println("isSymbolicLink (Files): " + Files.isSymbolicLink(tempFile));
        System.out.println("isReadable (Files): " + Files.isReadable(tempFile));
        System.out.println("isWritable (Files): " + Files.isWritable(tempFile));
        System.out.println("isExecutable (Files): " + Files.isExecutable(tempFile));

        Files.delete(tempFile);
        System.out.println();
    }

    // =========================================================================
    // 5. FILE PERMISSIONS
    // =========================================================================

    /**
     * Demonstrates file permission management.
     */
    public static void demonstrateFilePermissions() throws IOException {
        System.out.println("--- 5. File Permissions ---");

        Path tempFile = Files.createTempFile("permDemo", ".txt");

        // Set permissions (PosixFilePermissions)
        Set<PosixFilePermission> perms = EnumSet.of(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE,
            PosixFilePermission.GROUP_READ,
            PosixFilePermission.OTHERS_READ
        );
        Files.setPosixFilePermissions(tempFile, perms);
        System.out.println("Permissions set: " + Files.getPosixFilePermissions(tempFile));

        // String representation
        String permString = PosixFilePermissions.toString(perms);
        System.out.println("Permission string: " + permString);

        // Parse from string
        Set<PosixFilePermission> parsed = PosixFilePermissions.fromString("rwxr-xr--");
        System.out.println("Parsed permissions: " + parsed);

        // FileAttribute for creation
        FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
        Path newFile = Files.createTempFile("attrPerm", ".txt", attr);
        System.out.println("New file permissions: " + Files.getPosixFilePermissions(newFile));

        Files.delete(tempFile);
        Files.delete(newFile);
        System.out.println();
    }

    // =========================================================================
    // 6. TEMPORARY FILES AND DIRECTORIES
    // =========================================================================

    /**
     * Demonstrates temporary file and directory creation.
     */
    public static void demonstrateTemporaryFiles() throws IOException {
        System.out.println("--- 6. Temporary Files ---");

        // Create temp file
        Path tempFile = Files.createTempFile("demo", ".tmp");
        System.out.println("Temp file: " + tempFile);

        // Create temp file in specific directory
        Path customDir = Files.createTempDirectory("custom");
        Path customTemp = Files.createTempFile(customDir, "prefix_", ".suffix");
        System.out.println("Custom temp: " + customTemp);

        // Create temp directory
        Path tempDir = Files.createTempDirectory("demoDir");
        System.out.println("Temp dir: " + tempDir);

        // Delete on JVM exit
        File tempJavaIo = File.createTempFile("javaio", ".tmp");
        tempJavaIo.deleteOnExit();
        System.out.println("DeleteOnExit temp: " + tempJavaIo.getAbsolutePath());

        // Clean up
        Files.delete(tempFile);
        Files.delete(customTemp);
        Files.delete(customDir);
        Files.delete(tempDir);
        System.out.println();
    }

    // =========================================================================
    // 7. DISK SPACE
    // =========================================================================

    /**
     * Demonstrates disk space information retrieval.
     */
    public static void demonstrateDiskSpace() {
        System.out.println("--- 7. Disk Space ---");

        Path root = Path.of("/");

        try {
            FileStore store = Files.getFileStore(root);
            System.out.println("Store Name: " + store.name());
            System.out.println("Type: " + store.type());
            System.out.println("Total Space: " + formatBytes(store.getTotalSpace()));
            System.out.println("Usable Space: " + formatBytes(store.getUsableSpace()));
            System.out.println("Unallocated: " + formatBytes(store.getUnallocatedSpace()));
            System.out.println("Supports File Attribute View: " +
                store.supportsFileAttributeView("basic"));
        } catch (IOException e) {
            System.err.println("Cannot access file store: " + e.getMessage());
        }

        System.out.println();
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    // =========================================================================
    // 8. LINE NUMBER READER
    // =========================================================================

    /**
     * Demonstrates LineNumberReader for line-by-line reading with line numbers.
     */
    public static void demonstrateLineNumberReader() throws IOException {
        System.out.println("--- 8. LineNumberReader ---");

        Path tempFile = Files.createTempFile("lineNum", ".txt");
        Files.writeString(tempFile, "First line\nSecond line\nThird line\nFourth line\n");

        try (LineNumberReader reader = new LineNumberReader(
                new BufferedReader(new FileReader(tempFile.toFile())))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line " + reader.getLineNumber() + ": " + line);
            }
            System.out.println("Total lines: " + reader.getLineNumber());
        }

        Files.delete(tempFile);
        System.out.println();
    }

    // =========================================================================
    // 9. BUFFERED READ/WRITE
    // =========================================================================

    /**
     * Demonstrates BufferedReader and BufferedWriter for efficient I/O.
     */
    public static void demonstrateBufferedReadWrite() throws IOException {
        System.out.println("--- 9. Buffered Read/Write ---");

        Path tempFile = Files.createTempFile("buffered", ".txt");

        // BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            for (int i = 1; i <= 5; i++) {
                writer.write("Buffered line " + i);
                writer.newLine();
            }
        }

        // BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        }

        // StringBuilder read
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.toFile()))) {
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, charsRead);
            }
        }
        System.out.println("Full content: " + sb.length() + " chars");

        Files.delete(tempFile);
        System.out.println();
    }

    // =========================================================================
    // 10. DATA INPUT/OUTPUT STREAMS
    // =========================================================================

    /**
     * Demonstrates DataInputStream and DataOutputStream for typed I/O.
     */
    public static void demonstrateDataStreams() throws IOException {
        System.out.println("--- 10. Data Streams ---");

        Path tempFile = Files.createTempFile("dataStream", ".dat");

        // Write typed data
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(tempFile.toFile()))) {
            dos.writeInt(42);
            dos.writeDouble(3.14159);
            dos.writeUTF("Hello, Data Streams!");
            dos.writeBoolean(true);
            dos.writeLong(System.currentTimeMillis());
        }

        // Read typed data
        try (DataInputStream dis = new DataInputStream(
                new FileInputStream(tempFile.toFile()))) {
            System.out.println("Int: " + dis.readInt());
            System.out.println("Double: " + dis.readDouble());
            System.out.println("UTF: " + dis.readUTF());
            System.out.println("Boolean: " + dis.readBoolean());
            System.out.println("Long: " + dis.readLong());
        }

        Files.delete(tempFile);
        System.out.println();
    }

    // =========================================================================
    // UTILITY: Recursive directory deletion
    // =========================================================================

    /**
     * Recursively deletes a directory and all its contents.
     */
    private static void deleteDirectory(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walk(dir)
                 .sorted(Comparator.reverseOrder())
                 .forEach(path -> {
                     try {
                         Files.deleteIfExists(path);
                     } catch (IOException e) {
                         System.err.println("Cannot delete: " + path);
                     }
                 });
        }
    }
}
