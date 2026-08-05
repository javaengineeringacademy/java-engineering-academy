# 01 - Introduction to Java IO/NIO

## 1. Introduction

Java Input/Output (IO) and New Input/Output (NIO) form the backbone of data handling in Java applications. Every application, from a simple console program to a massive enterprise microservice, needs to read data from external sources and write data to destinations. Java provides a comprehensive IO ecosystem that has evolved from the original blocking IO (BIO) introduced in JDK 1.0 to the non-blocking NIO introduced in JDK 1.4, and further to NIO.2 in JDK 7.

This module provides a foundational understanding of how Java handles input and output operations, the architecture behind the IO libraries, and sets the stage for deep dives into each component.

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Understand the core concepts of Input and Output in Java
- Differentiate between BIO (Blocking IO) and NIO (Non-blocking IO)
- Identify the layered architecture of Java IO streams
- Understand the role of buffers, channels, and selectors in NIO
- Recognize when to use IO vs NIO for different scenarios
- Set up a proper Java IO project structure

## 3. Prerequisites

- Basic Java programming knowledge (classes, objects, exception handling)
- Understanding of binary and text data representation
- Familiarity with file systems (directories, file paths)
- Basic knowledge of threads and concurrency (helpful for NIO)

## 4. Why This Concept Exists

Before Java IO existed, programmers had to rely on platform-specific APIs for file and network operations. Java IO was designed to provide a **platform-independent**, **stream-based** approach to input and output.

### Key Problems Java IO Solves:

| Problem | Solution |
|---------|----------|
| Platform dependency | Abstract stream classes that work on any OS |
| Complex low-level APIs | Simple, composable stream decorators |
| Binary vs Text data | Separate byte and character stream hierarchies |
| Performance bottlenecks | Buffered streams for efficient I/O |
| Network communication | Socket streams for TCP/IP communication |

## 5. Problem Statement

Consider a real-world scenario: An enterprise application needs to:
1. Read configuration files (text data)
2. Process binary data from sensors (binary data)
3. Communicate with databases (network IO)
4. Write audit logs (file IO)
5. Handle thousands of concurrent connections (non-blocking IO)

Without a structured IO framework, developers would need to write platform-specific code for each scenario. Java IO/NIO provides a unified, layered approach to handle all these use cases.

## 6. Theory

### 6.1 The IO Classification Model

Java IO is classified along two dimensions:

**Dimension 1: Data Type**
- **Byte Streams**: Handle raw binary data (images, audio, serialized objects)
- **Character Streams**: Handle text data with character encoding support

**Dimension 2: Direction**
- **Input Streams**: Read data from a source
- **Output Streams**: Write data to a destination

### 6.2 BIO vs NIO vs AIO

| Feature | BIO (java.io) | NIO (java.nio) | AIO (java.nio.channels) |
|---------|---------------|-----------------|--------------------------|
| **Introduced** | JDK 1.0 | JDK 1.4 | JDK 7 |
| **Blocking** | Blocking | Non-blocking | Asynchronous |
| **Thread Model** | One thread per connection | Single thread handles multiple | Callback-based |
| **Data Handling** | Stream-based | Buffer/Channel-based | Buffer/Channel-based |
| **Best For** | Small-scale apps | High-concurrency servers | Large file operations |
| **Complexity** | Low | Medium | High |

### 6.3 The Decorator Pattern in Java IO

Java IO extensively uses the Decorator pattern. You wrap streams to add functionality:

```
FileInputStream (reads bytes from file)
  └── BufferedInputStream (adds buffering)
       └── DataInputStream (adds primitive type reading)
```

## 7. Internal Working

### 7.1 BIO Internal Flow

```
Application → InputStream/OutputStream → Native OS I/O → Device/Network
                     ↑
              Blocking call (thread waits)
```

When a `read()` or `write()` is called:
1. The thread makes a system call to the OS
2. The OS waits for data availability
3. The thread is **blocked** until data arrives or operation completes
4. Data flows from OS buffer → Java stream → Application

### 7.2 NIO Internal Flow

```
Application → Channel ← Buffer ← Selector (multiplexing)
                ↑                    ↑
         Non-blocking I/O    Single thread monitors multiple channels
```

When using NIO:
1. Data is read into a **Buffer** through a **Channel**
2. A **Selector** monitors multiple channels on a single thread
3. The thread is never blocked; it checks readiness
4. Operations are performed only when data is ready

## 8. JVM Perspective

### 8.1 Memory Allocation for IO Operations

```
JVM Heap Memory:
├── Stream Objects (wrapper objects)
├── Buffered Arrays (internal byte/char arrays)
└── Character Decoders/Encoders

Native Memory (off-heap):
├── OS File Descriptors
├── Socket Buffers
├── NIO Direct Buffers (ByteBuffer.allocateDirect())
└── Memory-mapped files
```

### 8.2 Garbage Collection Impact

- Stream wrapper objects are short-lived → Minor GC impact
- Large buffer arrays may promote to Old Gen → Major GC impact
- NIO Direct Buffers are allocated outside heap → Not managed by GC directly
- Proper closing of streams is critical to avoid native memory leaks

## 9. Memory Representation

### Byte Stream Memory Layout:
```
byte[] buffer = new byte[1024];
┌────────────────────────────────────────────────────┐
│ 0x48 │ 0x65 │ 0x6C │ 0x6C │ 0x6F │ 0x00 │ ...  │
│  'H'  │  'e'  │  'l'  │  'l'  │  'o'  │ null │      │
└────────────────────────────────────────────────────┘
```

### Character Stream Memory Layout:
```
char[] buffer = new char[1024];
┌────────────────────────────────────────────────────┐
│ 0x0048 │ 0x0065 │ 0x006C │ 0x006C │ 0x006F │ ... │
│   'H'   │   'e'   │   'l'   │   'l'   │   'o'   │     │
└────────────────────────────────────────────────────┘
```

### NIO Buffer Memory Layout:
```
ByteBuffer (heap):
┌─────────────────────────────────────┐
│ Array: [H][e][l][l][o]              │
│ Position: 5                         │
│ Limit: 1024                         │
│ Capacity: 1024                      │
└─────────────────────────────────────┘

DirectByteBuffer (off-heap):
┌─────────────────────────────────────┐
│ Native Memory: [H][e][l][l][o]      │
│ Address pointer                     │
│ Cleaner for deallocation            │
└─────────────────────────────────────┘
```

## 10. Syntax

### 10.1 Basic BIO Syntax

```java
// Byte streams
InputStream inputStream = new FileInputStream("data.bin");
OutputStream outputStream = new FileOutputStream("output.bin");

// Character streams
Reader reader = new FileReader("text.txt");
Writer writer = new FileWriter("output.txt");

// Decorated streams
BufferedInputStream bis = new BufferedInputStream(
    new FileInputStream("data.bin")
);
```

### 10.2 Basic NIO Syntax

```java
// Buffer operations
ByteBuffer buffer = ByteBuffer.allocate(1024);
FileChannel channel = FileChannel.open(
    Paths.get("file.txt"),
    StandardOpenOption.READ
);

// Channel operations
int bytesRead = channel.read(buffer);
buffer.flip();  // Switch from write to read mode
channel.write(buffer);
```

### 10.3 Try-with-resources (Java 7+)

```java
try (InputStream is = new FileInputStream("file.txt");
     BufferedInputStream bis = new BufferedInputStream(is)) {
    byte[] data = bis.readAllBytes();
}
```

## 11. Easy Example

```java
import java.io.*;

public class BioBasicExample {
    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        // Write to file using character stream
        try (Writer writer = new FileWriter(outputFile)) {
            writer.write("Hello, Java IO!");
            writer.write("\nThis is a simple IO example.");
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }

        // Read from file using character stream
        try (Reader reader = new FileReader(outputFile);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
```

**Output:**
```
Hello, Java IO!
This is a simple IO example.
```

## 12. Medium Example

```java
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WordCountExample {

    public static Map<String, Integer> countWords(String filePath)
            throws IOException {
        Map<String, Integer> wordCount = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase()
                    .replaceAll("[^a-zA-Z\\s]", "")
                    .split("\\s+");

                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCount.merge(word, 1, Integer::sum);
                    }
                }
            }
        }
        return wordCount;
    }

    public static void main(String[] args) {
        try {
            Map<String, Integer> result = countWords("input.txt");
            result.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(entry ->
                    System.out.printf("%-15s %d%n",
                        entry.getKey(), entry.getValue()));
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 13. Hard Example

```java
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ParallelFileProcessor {

    private final ExecutorService executor;
    private final AtomicLong totalBytes = new AtomicLong(0);

    public ParallelFileProcessor(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    public CompletableFuture<Long> processDirectory(Path dir)
            throws IOException {
        CompletableFuture<Long> future = new CompletableFuture<>();

        executor.submit(() -> {
            try (DirectoryStream<Path> stream =
                    Files.newDirectoryStream(dir)) {
                long dirBytes = 0;

                for (Path entry : stream) {
                    if (Files.isDirectory(entry)) {
                        dirBytes += processDirectory(dir).join();
                    } else {
                        dirBytes += processFile(entry);
                    }
                }
                future.complete(dirBytes);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    private long processFile(Path file) throws IOException {
        long fileSize = Files.size(file);
        totalBytes.addAndGet(fileSize);

        // Simulate processing
        try (InputStream is = Files.newInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(is)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                // Process bytes...
            }
        }
        return fileSize;
    }

    public void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        ParallelFileProcessor processor =
            new ParallelFileProcessor(Runtime.getRuntime()
                .availableProcessors());

        try {
            Path startDir = Paths.get("/path/to/directory");
            long total = processor.processDirectory(startDir).join();
            System.out.printf("Total bytes processed: %,d%n", total);
        } finally {
            processor.shutdown();
        }
    }
}
```

## 14. Performance

### BIO vs NIO Performance Characteristics

| Aspect | BIO | NIO |
|--------|-----|-----|
| **Small files (< 1MB)** | Fast (simpler code path) | Slightly slower (buffer management) |
| **Large files (> 1MB)** | Slow (no direct memory access) | Fast (memory-mapped files) |
| **Many connections (1K+)** | Poor (thread per connection) | Excellent (single thread multiplexing) |
| **Few connections** | Excellent (low overhead) | Good (selector overhead) |
| **Sequential reads** | Good | Good |
| **Random access** | Poor (sequential streams) | Good (FileChannel + ByteBuffer) |

### Performance Tips:
1. Use **buffered streams** for BIO to reduce system calls
2. Use **DirectByteBuffer** for large file transfers
3. Use **memory-mapped files** for random access
4. Avoid mixing BIO and NIO in the same pipeline
5. Choose appropriate buffer sizes (typically 4KB-8KB for general use)

## 15. Best Practices

1. **Always use try-with-resources** to ensure streams are closed
2. **Choose the right stream type** (byte vs character) for your data
3. **Buffer appropriately** - don't read byte-by-byte
4. **Handle exceptions properly** - IOException and its subclasses
5. **Use NIO for high-concurrency** scenarios (1000+ connections)
6. **Close resources in reverse order** of opening
7. **Use NIO.2 Path API** instead of File class for file operations
8. **Consider memory-mapped files** for large file processing
9. **Profile before optimizing** - BIO is often faster for simple cases

## 16. Common Mistakes

1. **Not closing streams** → Resource leaks, file handle exhaustion
2. **Using byte streams for text** → Encoding issues, garbled output
3. **Using character streams for binary** → Data corruption
4. **Forgetting to flush** → Data not written to destination
5. **Ignoring exceptions** → Silent failures, data loss
6. **Buffering already buffered streams** → Wasted memory, no benefit
7. **Not checking return values** → Incomplete reads/writes

## 17. Pitfalls

1. **Platform-specific line separators** → Use `\n` or `System.lineSeparator()`
2. **Character encoding mismatches** → Always specify charset explicitly
3. **Large file memory issues** → Don't load entire file into memory
4. **NIO DirectBuffer memory leaks** → Always release via Cleaner
5. **File locking conflicts** → Understand lock semantics
6. **Thread safety in NIO** → Channels are not thread-safe

## 18. Debugging Tips

1. **Enable IO logging**: `-Djava.io.debug=true`
2. **Use strace/dtrace** for system-level IO tracing
3. **Monitor file descriptors**: `lsof -p <pid>`
4. **Check buffer state** in NIO: `buffer.position()`, `buffer.limit()`
5. **Use JFR** (Java Flight Recorder) for IO event recording
6. **Profile with async-profiler** for IO hotspots

## 19. Comparison Table

| Component | Package | Type | Blocking | Use Case |
|-----------|---------|------|----------|----------|
| InputStream | java.io | Byte | Yes | Reading binary data |
| OutputStream | java.io | Byte | Yes | Writing binary data |
| Reader | java.io | Char | Yes | Reading text data |
| Writer | java.io | Char | Yes | Writing text data |
| FileChannel | java.nio | Byte | Yes/No | File operations |
| SocketChannel | java.nio | Byte | No | Network I/O |
| Selector | java.nio | - | No | Multiplexing |

## 20. Decision Tree

```
Do you need to read/write data?
├── Is it text data?
│   ├── Yes → Use Character Streams (Reader/Writer)
│   │   ├── Need buffering? → BufferedReader/BufferedWriter
│   │   └── Simple reading? → FileReader/FileWriter
│   └── No → Use Byte Streams (InputStream/OutputStream)
│       ├── Need buffering? → BufferedInputStream/BufferedOutputStream
│       └── Simple reading? → FileInputStream/FileOutputStream
│
├── Do you need non-blocking I/O?
│   ├── Yes → Use NIO (java.nio)
│   │   ├── File operations? → FileChannel + ByteBuffer
│   │   ├── Network? → SocketChannel + Selector
│   │   └── File system? → java.nio.file (NIO.2)
│   └── No → Use BIO (java.io)
│
└── Do you need async operations?
    ├── Yes → Use AIO (java.nio.channels.AsynchronousFileChannel)
    └── No → Use BIO or NIO
```

## 21. Interview Questions

### Q1: What is the difference between Byte and Character streams?
**Answer:** Byte streams handle raw binary data (8-bit bytes), used for images, audio, and serialized objects. Character streams handle text data (16-bit Unicode characters), providing automatic charset encoding/decoding.

### Q2: What is the difference between BIO and NIO?
**Answer:** BIO is blocking - a thread waits until IO operation completes. NIO is non-blocking - a thread can perform other work while IO is pending. NIO uses buffers and channels, while BIO uses streams. NIO is better for high-concurrency scenarios.

### Q3: Why do we need both BIO and NIO?
**Answer:** BIO is simpler and sufficient for low-concurrency applications. NIO provides better resource utilization for high-concurrency scenarios. BIO has lower overhead for simple operations, while NIO excels at multiplexing many connections.

### Q4: What happens if you don't close a stream?
**Answer:** Resources (file descriptors, memory) are leaked. Eventually, the application may run out of file handles or memory, causing `IOException` or `OutOfMemoryError`. Always use try-with-resources.

### Q5: What is the Decorator pattern in Java IO?
**Answer:** Java IO wraps streams to add functionality. For example, `BufferedInputStream(FileInputStream)` decorates a file input stream with buffering. This allows flexible composition of stream behaviors.

## 22. Exercises

### Exercise 1: Basic File Copy
Write a program that copies a file from one location to another using both BIO and NIO approaches. Compare the code complexity and performance.

### Exercise 2: Stream Decorators
Create a pipeline that reads a text file, converts all characters to uppercase, adds line numbers, and writes the result to a new file.

### Exercise 3: NIO Channel Demo
Use `FileChannel` to read a file into a `ByteBuffer` and print its contents. Experiment with buffer sizes.

### Exercise 4: Directory Listing
Write a program that lists all files in a directory recursively using both `java.io.File` and `java.nio.file.Files.walk()`.

## 23. Assignments

### Assignment 1: IO Framework Comparison
Create a benchmarking tool that compares BIO and NIO performance for:
- Copying a 100MB file
- Reading 1000 small files (1KB each)
- Writing 1000 small files (1KB each)

Document the results in a table.

### Assignment 2: File Monitoring Service
Design a simple file monitoring service that watches a directory for changes. Use NIO's `WatchService` API.

## 24. Mini Project

**File Processing Pipeline**

Create a Java application that:
1. Reads files from an input directory
2. Processes each file (count lines, words, characters)
3. Generates a summary report
4. Moves processed files to an archive directory

Requirements:
- Use BIO for file reading (with buffered streams)
- Use NIO for directory monitoring
- Implement proper error handling
- Add logging

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| Java IO | Stream-based, blocking, platform-independent |
| Java NIO | Buffer/Channel-based, non-blocking, multiplexed |
| Byte Streams | Handle binary data (InputStream/OutputStream) |
| Character Streams | Handle text data (Reader/Writer) |
| Decorator Pattern | Wraps streams to add functionality |
| Try-with-resources | Ensures proper resource cleanup |
| Direct Buffers | Off-heap memory for NIO performance |

## 26. References

1. **Official Documentation**: [Java IO Tutorial](https://docs.oracle.com/en/java/javase/21/essential/io/)
2. **Java NIO Guide**: [NIO by Example](https://jenkov.com/tutorials/java-nio/index.html)
3. **Books**:
   - "Java I/O, NIO and NIO.2" by Joseph Dallmeier
   - "Java Performance" by Scott Oaks
4. **OpenJDK Source**: [java.base module](https://github.com/openjdk/jdk/tree/master/src/java.base)
5. **Related Modules**:
   - [02-file-operations](../02-file-operations/README.md)
   - [03-byte-streams](../03-byte-streams/README.md)
   - [08-nio-basics](../08-nio-basics/README.md)

---

**Next Topic**: [02 - File Operations](../02-file-operations/README.md)
