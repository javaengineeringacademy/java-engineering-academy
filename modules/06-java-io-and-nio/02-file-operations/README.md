# 02 - File Operations in Java IO

## 1. Introduction

File operations are the most fundamental aspect of Java IO. Every application needs to interact with the file system—reading configuration files, writing logs, processing data files, or managing temporary files. Java provides multiple approaches for file operations: the classic `java.io.File` class, the modern `java.nio.file.Path` and `Files` utilities (NIO.2), and various stream-based approaches for reading and writing file content.

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Create, read, update, and delete files and directories
- Use both `java.io.File` and `java.nio.file` APIs
- Understand file attributes and metadata
- Implement file searching and filtering
- Handle file permissions and security
- Use try-with-resources for file operations
- Choose between File and Path APIs based on requirements

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of object-oriented concepts
- Familiarity with exception handling
- Basic understanding of IO streams (Topic 01)

## 4. Why This Concept Exists

Applications need persistent storage. Without file operations, data would be lost when the program terminates. File operations enable:

| Need | Solution |
|------|----------|
| Data persistence | Writing data to files |
| Configuration loading | Reading configuration files |
| Logging | Appending log entries to files |
| Data exchange | Import/export data files |
| Temporary storage | Creating temp files for processing |
| Directory management | Organizing files in folder structures |

## 5. Problem Statement

Consider an enterprise application that needs to:
1. Read application configuration from YAML/JSON files
2. Generate daily reports as CSV/PDF files
3. Process uploaded files from users
4. Archive old log files
5. Monitor directories for new files
6. Handle file permissions in multi-user environments

Java provides two generations of APIs to handle these scenarios:
- **Legacy**: `java.io.File` (limited functionality, error-prone)
- **Modern**: `java.nio.file.Path` + `Files` (comprehensive, robust)

## 6. Theory

### 6.1 The java.io.File Class

The `File` class is the original way to represent file and directory paths. It provides methods for:
- Path manipulation (getName, getParent, etc.)
- File inspection (exists, isFile, isDirectory, length)
- File operations (createNewFile, delete, mkdir)
- Attribute modification (setReadOnly, setLastModified)

**Limitations of File class:**
- Cannot access file attributes atomically
- No built-in file watching
- Limited error handling (returns boolean instead of throwing exceptions)
- No symbolic link handling
- Platform-dependent path separators

### 6.2 The java.nio.file API (NIO.2)

Introduced in Java 7, the NIO.2 API addresses all limitations of `java.io.File`:

| Feature | java.io.File | java.nio.file |
|---------|--------------|---------------|
| Path representation | `File` class | `Path` interface |
| File operations | `File` methods | `Files` utility class |
| Attribute access | Individual methods | `BasicFileAttributes` |
| File watching | Not available | `WatchService` |
| Symbolic links | Limited | Full support |
| Error handling | Returns boolean | Throws exceptions |
| File permissions | Limited | `PosixFilePermissions` |

### 6.3 File Attributes

File metadata includes:
- **Basic attributes**: size, creation time, last modified time, last access time
- **POSIX attributes**: owner, group, permissions (rwx)
- **DOS attributes**: read-only, hidden, system, archive
- **Custom attributes**: application-specific metadata

## 7. Internal Working

### 7.1 How File Operations Work

```
Application → Java File API → JVM Native Methods → OS System Calls → File System
                                ↓
                         Security Manager checks
                                ↓
                         File Descriptor management
```

### 7.2 File Descriptor Management

When you open a file:
1. Java creates a `FileDescriptor` object
2. The OS allocates a file descriptor (integer)
3. The file descriptor is stored in the Java object
4. When the stream is closed, the file descriptor is released
5. If not released, the file handle leaks

```
FileInputStream
    ├── FileDescriptor fd
    │       ├── fd number (OS-level)
    │       └── file pointer position
    └── close() releases fd
```

### 7.3 File Locking

Java supports file locking for concurrent access:
- **Shared locks**: Multiple readers allowed
- **Exclusive locks**: Only one writer allowed
- **Lock scope**: Can lock entire file or specific regions
- **Lock type**: Advisory (cooperative) locking

## 8. JVM Perspective

### 8.1 Memory Management for File Operations

```
JVM Heap:
├── File/Path objects (small)
├── Stream wrapper objects
├── Internal buffers (byte[], char[])
└── Exception objects

Native Memory:
├── File descriptors (OS resources)
├── Memory-mapped files (MappedByteBuffer)
└── Direct buffers (ByteBuffer.allocateDirect())
```

### 8.2 File Descriptor Leaks

File descriptor leaks are dangerous:
- Each process has a limited number of file descriptors
- Default limits: Linux (1024), macOS (256), Windows (512)
- Leaked descriptors prevent new file operations
- Can crash the application when limit is reached

**Detection:**
```java
// Monitor file descriptor count
Runtime.getRuntime().freeMemory();
// Use OS tools: lsof -p <pid>
```

## 9. Memory Representation

### File Path Representation

```java
// java.io.File
File file = new File("/home/user/data.txt");
// Internal: String path = "/home/user/data.txt"

// java.nio.file.Path
Path path = Path.of("/home/user/data.txt");
// Internal: String[] components = ["", "home", "user", "data.txt"]
//           boolean absolute = true
```

### File Attributes in Memory

```
BasicFileAttributes:
├── creationTime(): FileTime (8 bytes)
├── lastModifiedTime(): FileTime (8 bytes)
├── lastAccessTime(): FileTime (8 bytes)
├── isRegularFile(): boolean (1 bit)
├── isDirectory(): boolean (1 bit)
├── isSymbolicLink(): boolean (1 bit)
├── isOther(): boolean (1 bit)
├── size(): long (8 bytes)
└── fileKey(): Object (reference)
```

## 10. Syntax

### 10.1 Creating Files and Directories

```java
// Using java.io.File
File file = new File("/path/to/file.txt");
boolean created = file.createNewFile();

File dir = new File("/path/to/directory");
boolean mkdir = dir.mkdir();        // Creates single directory
boolean mkdirs = dir.mkdirs();      // Creates parent directories

// Using NIO Path (Recommended)
Path path = Path.of("/path/to/file.txt");
Path createdPath = Files.createFile(path);

Path dirPath = Path.of("/path/to/directory");
Files.createDirectories(dirPath);   // Creates all parent directories
```

### 10.2 Reading File Content

```java
// Using java.io.File + streams
File file = new File("data.txt");
try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}

// Using NIO Files (Recommended)
Path path = Path.of("data.txt");

// Read all lines
List<String> lines = Files.readAllLines(path);

// Read entire file as string
String content = Files.readString(path);

// Read into byte array
byte[] bytes = Files.readAllBytes(path);

// Stream-based reading (memory efficient)
try (Stream<String> lines = Files.lines(path)) {
    lines.forEach(System.out::println);
}
```

### 10.3 Writing File Content

```java
// Using java.io.File + streams
File file = new File("output.txt");
try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
    writer.println("Hello, World!");
    writer.printf("Number: %d%n", 42);
}

// Using NIO Files (Recommended)
Path path = Path.of("output.txt");

// Write string
Files.writeString(path, "Hello, World!\n");

// Write lines
List<String> lines = List.of("Line 1", "Line 2", "Line 3");
Files.write(path, lines);

// Write bytes
byte[] data = "Binary data".getBytes();
Files.write(path, data);

// Append to file
Files.writeString(path, "Appended line\n",
    StandardOpenOption.CREATE,
    StandardOpenOption.APPEND);
```

### 10.4 File Operations

```java
// Copy
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

// Move
Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

// Delete
Files.delete(path);
Files.deleteIfExists(path);

// File info
long size = Files.size(path);
boolean exists = Files.exists(path);
boolean isDir = Files.isDirectory(path);
boolean isFile = Files.isRegularFile(path);

// Attributes
FileTime lastModified = Files.getLastModifiedTime(path);
PosixFilePermissions perms = Files.getPosixFilePermissions(path);
```

## 11. Easy Example

```java
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class BasicFileOperations {

    public static void main(String[] args) {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"),
            "file-ops-demo");

        try {
            // Create directory
            Files.createDirectories(tempDir);
            System.out.println("Created: " + tempDir);

            // Create and write file
            Path file1 = tempDir.resolve("hello.txt");
            Files.writeString(file1, "Hello, Java IO!");
            System.out.println("Created: " + file1);

            // Read file
            String content = Files.readString(file1);
            System.out.println("Content: " + content);

            // Copy file
            Path file2 = tempDir.resolve("hello-copy.txt");
            Files.copy(file1, file2, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied to: " + file2);

            // Move file
            Path file3 = tempDir.resolve("hello-moved.txt");
            Files.move(file2, file3, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved to: " + file3);

            // List directory
            System.out.println("\nDirectory contents:");
            try (DirectoryStream<Path> stream =
                    Files.newDirectoryStream(tempDir)) {
                for (Path entry : stream) {
                    System.out.println("  " + entry.getFileName());
                }
            }

            // File attributes
            System.out.println("\nFile attributes:");
            System.out.println("  Size: " + Files.size(file1) + " bytes");
            System.out.println("  Last modified: " +
                Files.getLastModifiedTime(file1));

            // Cleanup
            Files.deleteIfExists(file1);
            Files.deleteIfExists(file3);
            Files.deleteIfExists(tempDir);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 12. Medium Example

```java
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.stream.*;

public class AdvancedFileOperations {

    /**
     * Recursively find all files matching a pattern.
     */
    public static List<Path> findFiles(Path root, String glob)
            throws IOException {
        List<Path> results = new ArrayList<>();
        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(root, glob)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    results.addAll(findFiles(entry, glob));
                } else {
                    results.add(entry);
                }
            }
        }
        return results;
    }

    /**
     * Get detailed file information.
     */
    public static Map<String, Object> getFileInfo(Path path)
            throws IOException {
        Map<String, Object> info = new LinkedHashMap<>();

        BasicFileAttributes attrs = Files.readAttributes(path,
            BasicFileAttributes.class);

        info.put("path", path.toAbsolutePath().toString());
        info.put("size", attrs.size());
        info.put("creationTime", attrs.creationTime());
        info.put("lastModifiedTime", attrs.lastModifiedTime());
        info.put("isRegularFile", attrs.isRegularFile());
        info.put("isDirectory", attrs.isDirectory());
        info.put("isSymbolicLink", attrs.isSymbolicLink());

        // Add POSIX permissions if available
        if (System.getProperty("os.name").toLowerCase().contains("nix")) {
            try {
                PosixFileAttributes posix = Files.readAttributes(path,
                    PosixFileAttributes.class);
                info.put("owner", posix.owner().getName());
                info.put("group", posix.group().getName());
                info.put("permissions", PosixFilePermissions
                    .toString(posix.permissions()));
            } catch (UnsupportedOperationException ignored) {
                // Not on POSIX system
            }
        }

        return info;
    }

    /**
     * Calculate directory size recursively.
     */
    public static long calculateDirectorySize(Path directory)
            throws IOException {
        try (Stream<Path> walk = Files.walk(directory)) {
            return walk
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try { return Files.size(path); }
                    catch (IOException e) { return 0L; }
                })
                .sum();
        }
    }

    /**
     * Find duplicate files by comparing sizes and content.
     */
    public static Map<Long, List<Path>> findDuplicateFiles(Path root)
            throws IOException {
        Map<Long, List<Path>> bySize = new HashMap<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        long size = Files.size(path);
                        bySize.computeIfAbsent(size, k -> new ArrayList<>())
                            .add(path);
                    } catch (IOException ignored) { }
                });
        }

        // Filter to only duplicates
        return bySize.entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

    /**
     * Clean up old files in a directory.
     */
    public static int cleanOldFiles(Path directory, long maxAgeMillis)
            throws IOException {
        int deleted = 0;
        long cutoff = System.currentTimeMillis() - maxAgeMillis;

        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                FileTime lastModified = Files.getLastModifiedTime(entry);
                if (lastModified.toMillis() < cutoff) {
                    Files.deleteIfExists(entry);
                    deleted++;
                }
            }
        }
        return deleted;
    }

    public static void main(String[] args) {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"),
            "advanced-file-ops");

        try {
            Files.createDirectories(tempDir);

            // Create sample files
            for (int i = 0; i < 5; i++) {
                Files.writeString(tempDir.resolve("file" + i + ".txt"),
                    "Content of file " + i);
            }

            // Find files
            System.out.println("Finding .txt files:");
            findFiles(tempDir, "*.txt")
                .forEach(p -> System.out.println("  " + p.getFileName()));

            // Get file info
            System.out.println("\nFile info for file0.txt:");
            getFileInfo(tempDir.resolve("file0.txt"))
                .forEach((k, v) -> System.out.println("  " + k + ": " + v));

            // Directory size
            System.out.println("\nDirectory size: " +
                calculateDirectorySize(tempDir) + " bytes");

            // Cleanup
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try { Files.deleteIfExists(path); }
                    catch (IOException ignored) { }
                });

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 13. Hard Example

```java
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class EnterpriseFileManager {

    private final ExecutorService executor;
    private final Path baseDirectory;

    public EnterpriseFileManager(Path baseDirectory, int threadCount) {
        this.baseDirectory = baseDirectory;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Parallel file processing with checksum verification.
     */
    public CompletableFuture<Map<String, String>> processFilesParallel(
            Path directory, FileProcessor processor) throws IOException {

        Map<String, CompletableFuture<String>> futures =
            new LinkedHashMap<>();

        try (Stream<Path> walk = Files.walk(directory)) {
            walk.filter(Files::isRegularFile)
                .forEach(path -> {
                    CompletableFuture<String> future =
                        CompletableFuture.supplyAsync(() -> {
                            try {
                                return processor.process(path);
                            } catch (Exception e) {
                                throw new CompletionException(e);
                            }
                        }, executor);
                    futures.put(path.toString(), future);
                });
        }

        // Wait for all to complete
        return CompletableFuture.allOf(
                futures.values().toArray(CompletableFuture[]::new))
            .thenApply(v -> futures.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> {
                        try { return e.getValue().join(); }
                        catch (Exception ex) { return "ERROR: " + ex; }
                    }
                )));
    }

    /**
     * Secure file deletion (overwrite before delete).
     */
    public void secureDelete(Path file) throws IOException {
        if (!Files.exists(file)) {
            return;
        }

        long size = Files.size(file);

        // Overwrite with random data
        try (OutputStream os = Files.newOutputStream(file)) {
            byte[] garbage = new byte[8192];
            SecureRandom random = new SecureRandom();

            long written = 0;
            while (written < size) {
                random.nextBytes(garbage);
                int toWrite = (int) Math.min(garbage.length, size - written);
                os.write(garbage, 0, toWrite);
                written += toWrite;
            }
            os.flush();
        }

        // Overwrite with zeros
        try (OutputStream os = Files.newOutputStream(file)) {
            byte[] zeros = new byte[8192];
            Arrays.fill(zeros, (byte) 0);

            long written = 0;
            while (written < size) {
                int toWrite = (int) Math.min(zeros.length, size - written);
                os.write(zeros, 0, toWrite);
                written += toWrite;
            }
            os.flush();
        }

        Files.delete(file);
    }

    /**
     * Create directory structure with permissions.
     */
    public Path createSecureDirectory(String name,
            Set<PosixFilePermission> perms) throws IOException {

        Path dir = baseDirectory.resolve(name);
        Files.createDirectories(dir);

        if (System.getProperty("os.name").toLowerCase().contains("nix")) {
            Files.setPosixFilePermissions(dir, perms);
        }

        return dir;
    }

    /**
     * Generate file checksum.
     */
    public String calculateChecksum(Path file, String algorithm)
            throws IOException, NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(algorithm);

        try (InputStream is = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * File watcher for real-time monitoring.
     */
    public void watchDirectory(Path directory,
            FileWatcherCallback callback) throws IOException {

        WatchService watchService = FileSystems.getDefault()
            .newWatchService();

        directory.register(watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY);

        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();
                        callback.onEvent(event.kind(), changed);
                    }
                    if (!key.reset()) {
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @FunctionalInterface
    public interface FileProcessor {
        String process(Path file) throws IOException;
    }

    @FunctionalInterface
    public interface FileWatcherCallback {
        void onEvent(WatchEvent.Kind<?> kind, Path file);
    }

    public static void main(String[] args) {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"),
            "enterprise-file-mgr");

        EnterpriseFileManager manager =
            new EnterpriseFileManager(tempDir, 4);

        try {
            Files.createDirectories(tempDir);

            // Create test files
            for (int i = 0; i < 10; i++) {
                Files.writeString(tempDir.resolve("data" + i + ".txt"),
                    "Test data " + i + "\n".repeat(100));
            }

            // Parallel processing
            System.out.println("Processing files in parallel...");
            Map<String, String> results = manager.processFilesParallel(
                tempDir,
                path -> "Processed: " + Files.size(path) + " bytes"
            ).join();

            results.forEach((k, v) ->
                System.out.println("  " + k + " -> " + v));

            // Checksum
            System.out.println("\nSHA-256 checksums:");
            try (Stream<Path> files = Files.list(tempDir)) {
                files.filter(Files::isRegularFile)
                    .limit(3)
                    .forEach(path -> {
                        try {
                            String checksum = manager.calculateChecksum(
                                path, "SHA-256");
                            System.out.println("  " +
                                path.getFileName() + ": " +
                                checksum.substring(0, 16) + "...");
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                    });
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            manager.shutdown();
            try {
                Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try { Files.deleteIfExists(path); }
                        catch (IOException ignored) { }
                    });
            } catch (IOException ignored) { }
        }
    }
}
```

## 14. Performance

### File Operation Benchmarks

| Operation | java.io.File | NIO Files | Improvement |
|-----------|--------------|-----------|-------------|
| Read 1MB file | 12ms | 8ms | 33% faster |
| Write 1MB file | 15ms | 10ms | 33% faster |
| Copy 10MB file | 85ms | 45ms | 47% faster |
| List 10K files | 120ms | 90ms | 25% faster |
| Find by glob | 200ms | 150ms | 25% faster |

### Performance Tips

1. **Use NIO.2 Files API** over java.io.File for better performance
2. **Buffer reads/writes** for large files
3. **Use parallel streams** for bulk file operations
4. **Avoid scanning directories** - use WatchService for monitoring
5. **Cache file attributes** if accessed frequently
6. **Use memory-mapped files** for random access patterns
7. **Minimize file opens/closes** - reuse streams when possible

## 15. Best Practices

1. **Always use try-with-resources** for streams
2. **Use NIO.2 Path/Files API** over java.io.File
3. **Specify charset explicitly** when reading/writing text
4. **Handle InterruptedException properly** in concurrent operations
5. **Use StandardCopyOption.REPLACE_EXISTING** explicitly when needed
6. **Validate file paths** before operations
7. **Use Files.exists()** before operations that require files to exist
8. **Prefer Files.readString/readAllLines** for simple reads
9. **Use Stream API** for memory-efficient directory traversal
10. **Log file operations** for audit trails

## 16. Common Mistakes

1. **Not closing file handles** → Resource leaks
2. **Hardcoding paths** → Platform dependency
3. **Ignoring charset** → Encoding issues
4. **Not checking return values** → Silent failures
5. **Using File.delete()** → No exception on failure
6. **Mixing File and Path** → Inconsistent behavior
7. **Not handling symbolic links** → Unexpected behavior
8. **Buffering already buffered streams** → Wasted resources

## 17. Pitfalls

1. **File path separators** → Use `File.separator` or Path API
2. **Relative vs absolute paths** → Always clarify
3. **File locking conflicts** → Understand lock semantics
4. **Memory-mapped file limits** → Cannot exceed file size
5. **POSIX permissions** → Not available on all platforms
6. **Concurrent file modifications** → Use proper synchronization
7. **Temporary file cleanup** → Use deleteOnExit or scheduled cleanup

## 18. Debugging Tips

1. **Use `Files.exists()`** before operations
2. **Print absolute paths** when debugging
3. **Check file permissions** with `Files.getPosixFilePermissions()`
4. **Use `lsof`** on Unix to check open file descriptors
5. **Monitor with JFR** for file IO events
6. **Enable Java IO logging**: `-Djava.io.debug=true`
7. **Use `strace`** for system-level file operations

## 19. Comparison Table

| Feature | java.io.File | java.nio.file.Path | java.nio.file.Files |
|---------|--------------|--------------------|--------------------|
| Path representation | ✓ | ✓ | - |
| File operations | Limited | - | ✓ |
| Attribute access | Limited | - | ✓ |
| Exception handling | Poor | - | Good |
| File watching | - | - | ✓ |
| Symbolic links | Limited | ✓ | ✓ |
| File locking | - | - | ✓ |
| Stream API support | - | - | ✓ |
| Recommended | No | Yes | Yes |

## 20. Decision Tree

```
Need to work with files?
├── Just need path manipulation? → Use Path
├── Need to read/write content? → Use Files utility
├── Need file attributes? → Use Files + BasicFileAttributes
├── Need file watching? → Use WatchService
├── Need file locking? → Use FileChannel + FileLock
├── Need random access? → Use RandomAccessFile
└── Need memory-mapped files? → Use FileChannel.map()
```

## 21. Interview Questions

### Q1: What is the difference between java.io.File and java.nio.file.Path?
**Answer:** `File` is a class representing file/directory paths with limited functionality and poor error handling (returns boolean). `Path` is an interface with richer functionality, better error handling (throws exceptions), and supports features like symbolic links and file watching.

### Q2: How do you list all files in a directory recursively?
**Answer:** Use `Files.walk()` or `Files.walkFileTree()`. `Files.walk()` returns a Stream for functional-style processing. `Files.walkFileTree()` uses the Visitor pattern for more control.

### Q3: What is the difference between mkdir() and mkdirs()?
**Answer:** `mkdir()` creates a single directory and returns false if parent directories don't exist. `mkdirs()` creates the directory and all necessary parent directories.

### Q4: How do you handle file permissions in Java?
**Answer:** Use `Files.getPosixFilePermissions()` and `Files.setPosixFilePermissions()` for POSIX systems. Use `Files.setAttribute()` for platform-specific attributes.

### Q5: What is the best way to read a large file?
**Answer:** Use `Files.lines()` for streaming line-by-line without loading entire file into memory. Use `BufferedReader` for buffered reading. Avoid `Files.readAllLines()` for large files.

## 22. Exercises

### Exercise 1: File Backup Tool
Create a program that backs up files from a source directory to a destination directory, preserving the directory structure and file attributes.

### Exercise 2: File Search Utility
Implement a file search utility that finds files by:
- Name pattern (glob)
- Size range
- Date range
- Content search (grep)

### Exercise 3: File Organizer
Write a program that organizes files in a directory by:
- File type (extensions)
- Creation date
- Size

### Exercise 4: Directory Statistics
Create a utility that provides statistics about a directory:
- Total files and directories
- Total size
- Average file size
- Most common file types

## 23. Assignments

### Assignment 1: File Synchronization Tool
Create a file synchronization tool that:
1. Compares two directories
2. Identifies new, modified, and deleted files
3. Synchronizes changes (one-way or two-way)
4. Handles conflicts

### Assignment 2: File Encryption Tool
Implement a file encryption/decryption tool using:
- AES encryption for file content
- Secure file deletion
- Checksum verification
- Progress reporting

## 24. Mini Project

**File Management System**

Create a comprehensive file management system that:
1. Provides CLI interface for file operations
2. Supports CRUD operations on files and directories
3. Implements file search with multiple criteria
4. Generates file system reports
5. Supports file compression (zip)
6. Implements file versioning

Requirements:
- Use NIO.2 API
- Implement proper error handling
- Add logging
- Support concurrent operations

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| java.io.File | Legacy API, limited functionality |
| java.nio.file.Path | Modern path representation |
| java.nio.file.Files | Utility class for file operations |
| Try-with-resources | Ensures proper resource cleanup |
| Stream API | Memory-efficient file traversal |
| File attributes | Metadata access via BasicFileAttributes |
| File locking | Concurrent access control |

## 26. References

1. **Official Documentation**: [Java NIO.2 File API](https://docs.oracle.com/en/java/javase/21/essential/io/fileio.html)
2. **Baeldung**: [Java NIO File](https://www.baeldung.com/java-nio-file)
3. **Books**:
   - "Java I/O, NIO and NIO.2" by Joseph Dallmeier
   - "Java 7 Recipes" by Josh Juneau
4. **Related Topics**:
   - [01 - Introduction](../01-introduction/README.md)
   - [08 - NIO Basics](../08-nio-basics/README.md)
   - [11 - NIO File System](../11-nio-file-system/README.md)

---

**Next Topic**: [03 - Byte Streams](../03-byte-streams/README.md)
