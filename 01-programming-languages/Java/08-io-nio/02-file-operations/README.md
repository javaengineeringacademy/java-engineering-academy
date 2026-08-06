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

---

[📖 Continue to Part 2](README-part2.md)
