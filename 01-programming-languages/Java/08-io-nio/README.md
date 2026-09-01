# Module 08: Java I/O and NIO

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 30 min | **Practice:** 45 min | **Total:** 75 min

## Overview
Applications need to read and write files, handle network requests, and serialize data. Java's I/O and NIO APIs manage encoding, buffering, and resource cleanup automatically — so you can focus on your data, not the plumbing. This module covers byte and character streams, NIO channels and buffers, file operations, and serialization.

## Learning Objectives
- Read and write files using both traditional streams and modern NIO.2 APIs
- Choose between byte streams and character streams based on data type
- Use NIO channels and buffers for efficient, non-blocking I/O
- Manage resources safely with try-with-resources to prevent leaks
- Implement serialization for object persistence and network transfer

## Prerequisites
- Basic Java knowledge
- Exception handling
- File system concepts

## History
- **1995** — Java 1.0 introduced stream-based I/O (`InputStream`, `OutputStream`)
- **1998** — Java 1.2 added `Reader`/`Writer` for character streams
- **2001** — Java 1.4 introduced NIO (`ByteBuffer`, `Channel`, `Selector`)
- **2004** — Java 5 added `File` convenience methods and `Scanner`
- **2011** — Java 7 introduced NIO.2 (`Path`, `Files`, `FileVisitor`) and try-with-resources
- **2014** — Java 8 added `Files.lines()`, `Files.list()`, `Files.walk()` returning streams
- **2017** — Java 9 added `InputStream.transferTo()`, `Files.readString()`, `Files.writeString()`
- **2021** — Java 17 added `Files.mismatch()`

## Why This Concept Exists
File and network operations require:
- Data reading/writing
- Stream processing
- Buffer management
- Resource handling

I/O provides:
- Stream abstraction
- Buffer management
- Character encoding
- Resource management

## Problem Statement
How do you efficiently read, write, and transfer data in Java?

## Core Concepts

### I/O Types

| Type | Description | Use Case |
|------|-------------|----------|
| Byte Stream | Raw bytes | Binary files |
| Character Stream | Characters | Text files |
| Buffered | Performance | Large files |
| NIO Channels | Non-blocking | Network I/O |

### Stream Classes

| Byte Stream | Character Stream |
|-------------|------------------|
| InputStream | Reader |
| OutputStream | Writer |
| FileInputStream | FileReader |
| FileOutputStream | FileWriter |
| BufferedInputStream | BufferedReader |
| BufferedOutputStream | BufferedWriter |

### NIO Components

| Component | Purpose |
|-----------|---------|
| Channel | I/O connection |
| Buffer | Data container |
| Selector | Multiplexer |

## Internal Working

### Stream Processing
```
Source → Buffer → Process → Buffer → Destination
```

### Buffer Operations
```
Write: clear → put → flip
Read: flip → get → clear
```

## JVM Perspective

### Resource Management
- File descriptors are limited
- Streams must be closed
- Try-with-resources ensures cleanup
- NIO channels are non-blocking

### Memory Mapping
- MappedByteBuffer for large files
- OS manages memory
- Faster than stream I/O

## Architecture Diagram

```mermaid
graph TD
    A[Java I/O] --> B[Byte Streams]
    A --> C[Character Streams]
    A --> D[NIO]
    
    B --> E[InputStream]
    B --> F[OutputStream]
    
    C --> G[Reader]
    C --> H[Writer]
    
    D --> I[Channel]
    D --> J[Buffer]
    D --> K[Selector]
```

## Syntax

### Byte Streams
```java
// FileInputStream
try (FileInputStream fis = new FileInputStream("file.txt")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        // Process bytes
    }
}

// FileOutputStream
try (FileOutputStream fos = new FileOutputStream("file.txt")) {
    fos.write("Hello".getBytes());
}
```

### Character Streams
```java
// BufferedReader
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}

// BufferedWriter
try (BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"))) {
    bw.write("Hello");
    bw.newLine();
}
```

### NIO
```java
// FileChannel
try (FileChannel channel = FileChannel.open(Path.of("file.txt"), 
        StandardOpenOption.READ)) {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    channel.read(buffer);
}

// Path
Path path = Path.of("file.txt");
Files.readString(path);
Files.writeString(path, "Hello");
Files.copy(source, target);
```

## Easy Example
```java
import java.nio.file.*;
import java.io.*;

public class EasyExample {
    public static void main(String[] args) throws IOException {
        // Write file
        Files.writeString(Path.of("test.txt"), "Hello World");
        
        // Read file
        String content = Files.readString(Path.of("test.txt"));
        System.out.println(content);
        
        // Copy file
        Files.copy(Path.of("test.txt"), Path.of("copy.txt"));
    }
}
```

## Medium Example
```java
import java.io.*;
import java.nio.file.*;

public class MediumExample {
    // Process file line by line
    public static long countLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(filename))) {
            return lines.count();
        }
    }
    
    // Copy directory
    public static void copyDir(Path source, Path target) throws IOException {
        Files.walk(source).forEach(path -> {
            try {
                Path dest = target.resolve(source.relativize(path));
                if (Files.isDirectory(path)) {
                    Files.createDirectories(dest);
                } else {
                    Files.copy(path, dest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    public static void main(String[] args) throws IOException {
        long lines = countLines("test.txt");
        System.out.println("Lines: " + lines);
    }
}
```

## Hard Example
```java
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;

public class HardExample {
    // Memory-mapped file
    public static String readMapped(String filename) throws IOException {
        try (FileChannel channel = FileChannel.open(Path.of(filename), 
                StandardOpenOption.READ)) {
            MappedByteBuffer buffer = channel.map(
                FileChannel.MapMode.READ_ONLY, 0, channel.size());
            
            StringBuilder sb = new StringBuilder();
            while (buffer.hasRemaining()) {
                sb.append((char) buffer.get());
            }
            return sb.toString();
        }
    }
    
    // Async file operations
    public static void main(String[] args) throws Exception {
        String content = readMapped("large-file.txt");
        System.out.println("Read " + content.length() + " characters");
    }
}
```

## Enterprise Example
```java
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class EnterpriseExample {
    // Parallel file processing
    public static void processFiles(Path dir) throws IOException {
        try (Stream<Path> files = Files.walk(dir)) {
            files.parallel()
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        long lines = Files.lines(p).count();
                        System.out.println(p.getFileName() + ": " + lines + " lines");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }
    
    public static void main(String[] args) throws IOException {
        processFiles(Path.of("."));
    }
}
```

## Performance Considerations
- Use buffered streams for large files
- NIO is faster for large files
- Memory mapping for very large files
- Try-with-resources for cleanup

## Time & Space Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Read file | O(n) | O(buffer) |
| Write file | O(n) | O(1) |
| Copy | O(n) | O(buffer) |
| Line count | O(n) | O(1) |

## Thread Safety
- Streams are not thread-safe
- Files can be shared read-only
- Use synchronization for writes
- NIO channels are thread-safe

## Best Practices
1. Use try-with-resources
2. Use NIO.2 for modern code
3. Buffer large operations
4. Handle exceptions properly
5. Use appropriate stream type

## Common Mistakes
1. Not closing streams
2. Using wrong encoding
3. Not buffering large files
4. Ignoring exceptions

## Comparison Table

| Feature | I/O Streams | NIO |
|---------|-------------|-----|
| Blocking | Yes | No |
| Buffer | Manual | Built-in |
| Channels | No | Yes |
| Performance | Good | Better |

## Interview Questions

### Q1: What is the difference between InputStream and Reader?
**Answer:** InputStream handles bytes, Reader handles characters.

### Q2: What is try-with-resources?
**Answer:** Auto-closes resources implementing AutoCloseable.

### Q3: What is the difference between File and Path?
**Answer:** Path is modern NIO.2, File is legacy.

### Q4: What is buffering?
**Answer:** Storing data in memory to reduce I/O operations.

### Q5: What is NIO?
**Answer:** New I/O with channels, buffers, and selectors.

## Summary
Java I/O and NIO provide detailed data handling capabilities. Use NIO for modern applications.

## Cross-References

- **Previous Module:** [07 - Functional Programming](../07-functional-programming/)
- **Next Module:** [09 - Multithreading](../09-multithreading-&-concurrency/)
- **Related:** [05 - Text Processing](../05-text-processing/) — character encoding and text manipulation
- **Related:** [10 - JVM Internals](../10-jvm-internals/) — file descriptors, memory mapping
- **Related:** [09 - Multithreading](../09-multithreading-&-concurrency/) — async I/O and NIO selectors
- **External:** [Oracle Java I/O Tutorial](https://docs.oracle.com/javase/tutorial/essential/io/)
- **External:** [Java NIO Tutorial - Baeldung](https://www.baeldung.com/java-nio)

## Prerequisites

- [Fundamentals](../01-fundamentals/README.md)

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| File descriptor leak | `lsof -p <pid>` or `jstack` | Monitor open file descriptors; identify unclosed streams in code |
| Encoding mismatch (garbled output) | Hex dump comparison | Compare file bytes with expected encoding; verify `StandardCharsets` usage |
| Memory-mapped file not released | Process monitor + explicit close | Use `FileChannel` with explicit close; avoid `MappedByteBuffer` for temp files |
| Slow I/O performance | Async-profiler + JFR | Profile I/O wait times; identify unbuffered operations |
| NIO channel not closing properly | Try-with-resources refactor | Replace manual `close()` calls with try-with-resources |

## Code Review Checklist

- [ ] All streams/channels use try-with-resources
- [ ] Charset explicitly specified (`StandardCharsets.UTF_8`)
- [ ] NIO.2 (`Path`, `Files`) used instead of `File` class
- [ ] Buffered streams used for large file operations
- [ ] `IOException` properly handled with meaningful messages
- [ ] No memory-mapped files for temporary data
- [ ] File descriptor limits configured in monitoring

## Architecture Considerations

I/O architecture determines how data flows between components. At scale, the choice between blocking I/O (traditional streams) and non-blocking I/O (NIO channels) affects thread utilization and throughput. For high-concurrency servers, NIO selectors enable handling thousands of connections with few threads. For batch processing, memory-mapped files provide efficient random access to large datasets.

In microservices, file I/O architecture affects service boundaries — shared file systems vs. object storage vs. streaming. For event-driven systems, NIO channels enable efficient network I/O for message brokers and API gateways.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Try-with-resources | All resource management | Pros: Guaranteed cleanup; Cons: Nesting can be verbose |
| NIO channels for networking | High-concurrency servers | Pros: Non-blocking, scalable; Cons: Complexity, harder to debug |
| Memory-mapped files | Large random-access files | Pros: OS-managed caching; Cons: Resource cleanup complexity |
| Streaming with `Files.lines()` | Large file processing | Pros: Memory-efficient; Cons: Must close stream, exception handling |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Path traversal attacks | Unauthorized file access | Validate paths; use `Path.normalize()`; check against allowed directories |
| Symlink following vulnerabilities | File system escape | Disable symlink following in `Files.walk()` options |
| Resource exhaustion via file descriptor leak | Denial of service | Use try-with-resources; configure file descriptor limits |
| Encoding injection | Data corruption, security bypass | Always specify `StandardCharsets.UTF_8` explicitly |
| Temporary file race conditions | Privilege escalation | Use `Files.createTempFile()` with secure permissions; delete after use |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | `InputStream`, `OutputStream` | Replace with NIO.2 for modern applications |
| Java 1.2 | `Reader`/`Writer` | Use for character-based I/O with explicit encoding |
| Java 1.4 | NIO (`ByteBuffer`, `Channel`) | Adopt for non-blocking I/O and large file operations |
| Java 7 | NIO.2 (`Path`, `Files`, try-with-resources) | Replace `File` class with `Path` and `Files` |
| Java 8 | `Files.lines()`, `Files.list()` returning streams | Use for memory-efficient file processing |
| Java 9 | `Files.readString()`, `Files.writeString()` | Replace `BufferedReader`/`BufferedWriter` for simple operations |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Try-with-resources | Java 7 | Stable |
| `Path`, `Files` (NIO.2) | Java 7 | Stable |
| `Files.lines()` returning Stream | Java 8 | Stable |
| `Files.readString()`, `Files.writeString()` | Java 9 | Stable |
| `Files.mismatch()` | Java 12 | Stable |
| `InputStream.transferTo()` | Java 9 | Stable |

## Production Incidents

### Incident 1: Resource Leak from Unclosed Streams

**Problem:** A file processing application ran out of file descriptors after processing 5,000 files, crashing the JVM.
**Cause:** `FileInputStream` wasn't closed in finally block; exceptions during processing left streams open.
**Impact:** Application crashed every 4 hours; required manual restart; 2-hour recovery time.
**Detection:** `java.io.IOException: Too many open files` in logs; JVM crash dumps.
**Solution:** Refactored to use try-with-resources for automatic resource management.
**Prevention:** Always use try-with-resources for AutoCloseable resources; configure file descriptor limits in monitoring.

### Incident 2: Encoding Mismatch in File Processing

**Problem:** A CSV import tool failed to parse international characters correctly, corrupting user data.
**Cause:** Used `FileReader` without specifying encoding; platform default (ISO-8859-1) used instead of UTF-8.
**Impact:** 30% of imported records had corrupted characters; data cleanup required; 2-day delay.
**Detection:** User reports of garbled characters; investigation revealed encoding mismatch.
**Solution:** Used `Files.newBufferedReader(path, StandardCharsets.UTF_8)` for explicit encoding.
**Prevention:** Always specify charset explicitly; use `StandardCharsets` constants; add encoding validation tests.

### Incident 3: Memory-Mapped File Not Released

**Problem:** A large file processing service couldn't delete processed files because they were still memory-mapped.
**Cause:** `MappedByteBuffer` held reference to file; JVM hadn't unmapped buffer after processing.
**Impact:** Disk space filled up; couldn't delete old files; manual intervention required.
**Detection:** `java.io.IOException: The process cannot access the file because it is being used by another process`
**Solution:** Used `FileChannel` with explicit closing; invoked `Cleaner` via reflection for immediate cleanup.
**Prevention:** Avoid memory-mapped files for temporary data; use `FileChannel` with explicit close; monitor disk usage.

## Production Checklist

- [ ] Always use try-with-resources for AutoCloseable resources
- [ ] Specify charset explicitly when reading/writing text files
- [ ] Use NIO.2 (`Path`, `Files`) for modern file operations
- [ ] Buffer large file operations for performance
- [ ] Handle `IOException` properly with meaningful messages
- [ ] Don't use `File` class — prefer `Path` and `Files`
- [ ] Don't ignore exceptions during file operations
- [ ] Don't memory-map temporary files
- [ ] Test file operations with different file sizes
- [ ] Monitor file descriptor usage in production

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses `File` class; doesn't close resources; ignores exceptions |
| Intermediate | Uses try-with-resources; handles exceptions; uses NIO.2 APIs |
| Advanced | Uses memory-mapped files; implements async I/O; optimizes performance |
| Expert | Designs file processing systems; teaches I/O patterns; contributes to I/O libraries |

## Common Myths

1. **Myth**: NIO is always faster than traditional I/O
   **Truth**: NIO adds overhead for small files; traditional I/O is often faster for simple operations. Use NIO for large files or non-blocking I/O.

2. **Myth**: `File` class is deprecated
   **Truth**: `File` is not deprecated but is less capable than `Path` and `Files`. Prefer NIO.2 for new code.

3. **Myth**: `BufferedReader` is always necessary
   **Truth**: `Files.readString()` and `Files.readAllLines()` handle buffering internally; explicit buffering is only needed for large files.

4. **Myth**: Memory-mapped files are always faster
   **Truth**: Memory mapping adds overhead for small files; it's only beneficial for random access on large files.

5. **Myth**: Character encoding doesn't matter for English text
   **Truth**: Even English text can have encoding issues; always specify charset explicitly to avoid platform-dependent behavior.

## Related Topics

- [Multithreading](../09-multithreading-&-concurrency/README.md)

## Next

- [Multithreading](../09-multithreading-&-concurrency/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | File and network I/O |
| Complexity | Varies |
| Thread Safe | No (by default) |
| Ordered | Yes (byte order) |
| Allows Null | No |
| Best Alternative | Files API (for simple ops) |
| When to Use | Low-level I/O |
| When to Avoid | Simple file operations |
