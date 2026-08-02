# 05 - Buffered Streams in Java IO

## 1. Introduction

Buffered streams are wrapper streams that add buffering capability to other streams. Buffering reduces the number of system calls by reading or writing larger chunks of data at once, significantly improving IO performance. Java provides `BufferedInputStream`, `BufferedOutputStream`, `BufferedReader`, and `BufferedWriter` for this purpose. Understanding buffering is crucial for writing efficient IO code.

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Understand how buffering improves IO performance
- Use all types of buffered streams
- Configure appropriate buffer sizes
- Implement efficient file copying with buffering
- Understand the internal workings of buffered streams
- Handle buffered stream edge cases

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of byte and character streams (Topics 03, 04)
- Familiarity with exception handling
- Basic performance concepts

## 4. Why This Concept Exists

Without buffering, every read/write operation results in a system call:

```
Unbuffered: read() → system call → read() → system call → read() → system call
Buffered:   read() → read() → read() → read() → system call (reads multiple bytes)
```

System calls are expensive (context switches, kernel mode transitions). Buffering reduces them dramatically.

## 5. Problem Statement

Consider copying a 100MB file:
- **Without buffering**: 100MB × 1024 × 1024 = ~100 million system calls
- **With 8KB buffer**: ~12,800 system calls (7,800x fewer!)

The performance difference can be 100x or more.

## 6. Theory

### 6.1 Buffered Stream Types

| Stream | Base Stream | Buffer Size | Use Case |
|--------|-------------|-------------|----------|
| BufferedInputStream | InputStream | 8192 bytes | Binary reading |
| BufferedOutputStream | OutputStream | 8192 bytes | Binary writing |
| BufferedReader | Reader | 8192 chars | Text reading |
| BufferedWriter | Writer | 8192 chars | Text writing |

### 6.2 How Buffering Works

```
Reading with BufferedReader:
1. Application calls read()
2. BufferedReader checks internal buffer
3. If buffer has data, return from buffer (fast)
4. If buffer empty, read large chunk from underlying stream
5. Store chunk in buffer
6. Return requested data from buffer
```

### 6.3 Buffer Size Selection

| Buffer Size | Memory Usage | Performance | Use Case |
|-------------|--------------|-------------|----------|
| 1KB | 1KB | Good | Small files |
| 8KB (default) | 8KB | Very Good | General use |
| 64KB | 64KB | Excellent | Large files |
| 256KB | 256KB | Best | Very large files |

**Rule of thumb**: Start with 8KB, increase for large files, decrease for many small operations.

## 7. Internal Working

### 7.1 BufferedInputStream Internals

```
BufferedInputStream {
    byte[] buf;           // Internal buffer
    int pos;             // Current position in buffer
    int count;           // Number of valid bytes in buffer
    int markPos = -1;    // Mark position
    int markLimit = -1;  // Mark limit

    read() {
        if (pos >= count) {
            fill();  // Refill buffer from underlying stream
            if (pos >= count) return -1;  // EOF
        }
        return buf[pos++] & 0xFF;
    }

    fill() {
        // If marked, keep marked data
        // Read from underlying stream into buffer
        count = in.read(buf, offset, buf.length - offset);
        pos = offset;
    }
}
```

### 7.2 BufferedOutputStream Internals

```
BufferedOutputStream {
    byte[] buf;           // Internal buffer
    int count;           // Current position in buffer

    write(int b) {
        if (count >= buf.length) {
            flushBuffer();  // Write buffer to underlying stream
        }
        buf[count++] = (byte) b;
    }

    flushBuffer() {
        if (count > 0) {
            out.write(buf, 0, count);
            count = 0;
        }
    }

    flush() {
        flushBuffer();
        out.flush();
    }
}
```

### 7.3 BufferedReader Line Reading

```
BufferedReader.readLine() {
    StringBuilder sb = new StringBuilder();
    boolean foundLine = false;

    while (true) {
        int c = read();  // Read single char
        if (c == -1) {
            return sb.length() > 0 ? sb.toString() : null;
        }
        if (c == '\n') {
            foundLine = true;
            break;
        }
        if (c == '\r') {
            foundLine = true;
            int next = read();
            if (next != '\n' && next != -1) {
                // Put back the character (PushbackReader)
            }
            break;
        }
        sb.append((char) c);
    }
    return sb.toString();
}
```

## 8. JVM Perspective

### 8.1 Memory Allocation

```
JVM Heap:
├── BufferedInputStream object (48 bytes)
│   └── byte[] buf (8192 bytes default)
├── BufferedOutputStream object (48 bytes)
│   └── byte[] buf (8192 bytes default)
├── BufferedReader object (48 bytes)
│   └── char[] cb (16384 bytes default = 8192 × 2)
└── BufferedWriter object (48 bytes)
    └── char[] cb (16384 bytes default = 8192 × 2)
```

### 8.2 GC Impact

- Buffer arrays are allocated on heap
- Large buffers may promote to Old Gen
- Closing streams releases buffer memory
- Buffer reuse can reduce GC pressure

## 9. Memory Representation

### BufferedInputStream Memory Layout

```
BufferedInputStream (8KB buffer):
┌────────────────────────────────────────┐
│ Object header (16 bytes)               │
│ InputStream in (reference, 8 bytes)    │
│ byte[] buf (reference, 8 bytes)        │
│ int pos (4 bytes)                      │
│ int count (4 bytes)                    │
│ int markPos (4 bytes)                  │
│ int markLimit (4 bytes)                │
├────────────────────────────────────────┤
│ byte[] buf: [B0][B1][B2]...[B8191]    │
│ (8192 bytes of actual buffer data)     │
└────────────────────────────────────────┘
```

### BufferedReader Memory Layout

```
BufferedReader (8KB char buffer):
┌────────────────────────────────────────┐
│ Object header (16 bytes)               │
│ Reader in (reference, 8 bytes)         │
│ char[] cb (reference, 8 bytes)         │
│ int nextChar (4 bytes)                 │
│ int nChars (4 bytes)                   │
│ boolean markedAtEOF (1 byte)           │
│ int readAheadLimit (4 bytes)           │
├────────────────────────────────────────┤
│ char[] cb: [C0][C1][C2]...[C8191]     │
│ (16384 bytes = 8192 chars × 2 bytes)  │
└────────────────────────────────────────┘
```

## 10. Syntax

### 10.1 Basic Buffered Stream Operations

```java
// Buffered byte streams
try (BufferedInputStream bis = new BufferedInputStream(
        new FileInputStream("input.bin"));
     BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream("output.bin"))) {
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
    }
}

// Buffered character streams
try (BufferedReader reader = new BufferedReader(
        new FileReader("input.txt"));
     BufferedWriter writer = new BufferedWriter(
        new FileWriter("output.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(line.toUpperCase());
        writer.newLine();
    }
}
```

### 10.2 Custom Buffer Sizes

```java
// Large buffer for big files
BufferedInputStream bis = new BufferedInputStream(
    new FileInputStream("large.bin"), 65536);  // 64KB

// Small buffer for small files
BufferedReader br = new BufferedReader(
    new FileReader("small.txt"), 1024);  // 1KB

// PrintWriter with auto-flush
PrintWriter pw = new PrintWriter(
    new BufferedWriter(
        new FileWriter("output.txt")), true);  // Auto-flush
```

### 10.3 Mark and Reset

```java
try (BufferedReader reader = new BufferedReader(
        new FileReader("text.txt"))) {
    reader.mark(1024);  // Mark with read-ahead limit
    String firstLine = reader.readLine();
    System.out.println("First line: " + firstLine);

    reader.reset();  // Return to marked position
    String firstLineAgain = reader.readLine();
    System.out.println("First line again: " + firstLineAgain);
}
```

## 11. Easy Example

```java
import java.io.*;

public class BufferedStreamBasic {

    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outputFile = "output.txt";

        try {
            // Create test file
            try (PrintWriter pw = new PrintWriter(new FileWriter(inputFile))) {
                for (int i = 0; i < 1000; i++) {
                    pw.println("Line " + i + ": " + "Test data ".repeat(10));
                }
            }

            // Copy with buffering
            long start = System.nanoTime();
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(
                    new FileWriter(outputFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            long elapsed = System.nanoTime() - start;

            System.out.printf("Copied with buffering in %,d ms%n",
                elapsed / 1_000_000);

            // Cleanup
            new File(inputFile).delete();
            new File(outputFile).delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 12. Medium Example

```java
import java.io.*;
import java.util.*;

public class BufferedStreamPerformance {

    /**
     * Copies file with different buffer sizes and measures performance.
     */
    public static void benchmarkBufferSizes(String source, String dest,
            int[] bufferSizes) throws IOException {

        System.out.printf("%-12s %-15s %-15s%n",
            "Buffer", "Time (ms)", "Speed (MB/s)");

        for (int bufferSize : bufferSizes) {
            long fileSize = new File(source).length();

            long start = System.nanoTime();

            try (InputStream is = new FileInputStream(source);
                 OutputStream os = new FileOutputStream(dest)) {

                // Wrap with buffering if needed
                InputStream bis = (bufferSize > 0) ?
                    new BufferedInputStream(is, bufferSize) : is;
                OutputStream bos = (bufferSize > 0) ?
                    new BufferedOutputStream(os, bufferSize) : os;

                byte[] buffer = new byte[Math.max(bufferSize, 1024)];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
            }

            long elapsed = System.nanoTime() - start;
            double seconds = elapsed / 1_000_000_000.0;
            double speed = (fileSize / 1024.0 / 1024.0) / seconds;

            String label = (bufferSize > 0) ?
                bufferSize + " bytes" : "No buffer";
            System.out.printf("%-12s %-15d %-15.2f%n",
                label, elapsed / 1_000_000, speed);
        }
    }

    public static void main(String[] args) {
        try {
            // Create test file
            String testFile = "benchmark.bin";
            byte[] data = new byte[10 * 1024 * 1024]; // 10MB
            new java.util.Random().nextBytes(data);
            try (FileOutputStream fos = new FileOutputStream(testFile)) {
                fos.write(data);
            }

            String copyFile = "benchmark-copy.bin";

            System.out.println("Buffer Size Benchmark:");
            System.out.println("-".repeat(45));
            benchmarkBufferSizes(testFile, copyFile,
                new int[]{0, 1024, 4096, 8192, 16384, 65536, 262144});

            // Cleanup
            new File(testFile).delete();
            new File(copyFile).delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 13. Hard Example

```java
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class HighPerformanceCopier {

    private static final int DEFAULT_BUFFER_SIZE = 256 * 1024; // 256KB
    private final int bufferSize;
    private final ExecutorService executor;

    public HighPerformanceCopier(int bufferSize, int threadCount) {
        this.bufferSize = bufferSize;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Copies file using double-buffering technique.
     */
    public CompletableFuture<Long> copyWithDoubleBuffering(
            String source, String destination) throws IOException {

        File sourceFile = new File(source);
        long fileSize = sourceFile.length();

        PipedInputStream pipedIn = new PipedInputStream(bufferSize);
        PipedOutputStream pipedOut = new PipedOutputStream(pipedIn);

        CompletableFuture<Long> future = new CompletableFuture<>();

        // Producer: Read from file into pipe
        executor.submit(() -> {
            try (BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(sourceFile), bufferSize)) {
                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    pipedOut.write(buffer, 0, bytesRead);
                }
                pipedOut.close();
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        // Consumer: Write from pipe to file
        executor.submit(() -> {
            try (BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(destination), bufferSize)) {
                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                long totalBytes = 0;
                while ((bytesRead = pipedIn.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }
                future.complete(totalBytes);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    /**
     * Copies file with progress monitoring.
     */
    public void copyWithProgress(String source, String destination,
            java.util.function.Consumer<Long> progressCallback)
            throws IOException {

        long totalSize = new File(source).length();
        AtomicLong copied = new AtomicLong(0);

        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(source), bufferSize);
             BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destination), bufferSize)) {

            byte[] buffer = new byte[bufferSize];
            int bytesRead;
            long lastReport = 0;
            long reportInterval = totalSize / 10;  // 10% intervals

            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);

                long currentCopied = copied.addAndGet(bytesRead);
                if (currentCopied - lastReport >= reportInterval) {
                    progressCallback.accept(currentCopied);
                    lastReport = currentCopied;
                }
            }
        }
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
        HighPerformanceCopier copier =
            new HighPerformanceCopier(DEFAULT_BUFFER_SIZE, 4);

        try {
            // Create test file
            String testFile = "large-file.bin";
            byte[] data = new byte[50 * 1024 * 1024]; // 50MB
            new java.util.Random().nextBytes(data);
            try (FileOutputStream fos = new FileOutputStream(testFile)) {
                fos.write(data);
            }

            // Copy with progress
            System.out.println("Copying with progress reporting:");
            long start = System.nanoTime();
            copier.copyWithProgress(testFile, "copy.bin",
                copied -> {
                    double percent = copied * 100.0 /
                        new File(testFile).length();
                    System.out.printf("  %.1f%% copied%n", percent);
                });
            long elapsed = System.nanoTime() - start;

            System.out.printf("Completed in %,d ms%n",
                elapsed / 1_000_000);

            // Cleanup
            new File(testFile).delete();
            new File("copy.bin").delete();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            copier.shutdown();
        }
    }
}
```

## 14. Performance

### Buffer Size Impact

| Buffer Size | 10MB Copy Time | Throughput | Memory |
|-------------|----------------|------------|--------|
| No buffer | 850ms | 11.8 MB/s | 1KB |
| 1KB | 320ms | 31.3 MB/s | 1KB |
| 8KB (default) | 85ms | 117.6 MB/s | 8KB |
| 64KB | 42ms | 238.1 MB/s | 64KB |
| 256KB | 35ms | 285.7 MB/s | 256KB |

### Performance Tips

1. **Use 8KB buffer** as default for general use
2. **Use 64KB-256KB** for large file operations
3. **Use 1KB buffer** for many small operations
4. **Don't buffer already buffered streams** (wastes memory)
5. **Flush periodically** for write operations
6. **Use try-with-resources** for automatic cleanup

## 15. Best Practices

1. **Always use try-with-resources** for buffered streams
2. **Choose appropriate buffer size** based on use case
3. **Don't double-buffer** (BufferedInputStream of BufferedInputStream)
4. **Flush before closing** if using manual close
5. **Use mark/reset** for rewinding operations
6. **Consider PrintWriter** for text output with auto-flush
7. **Test with realistic data** sizes for your use case

## 16. Common Mistakes

1. **Not closing streams** → Resource leaks
2. **Double buffering** → Wasted memory, no benefit
3. **Wrong buffer size** → Poor performance
4. **Not flushing** → Data loss
5. **Ignoring exceptions** → Silent failures
6. **Using byte buffer for char operations** → Encoding issues
7. **Not checking read return values** → Incomplete operations

## 17. Pitfalls

1. **Buffer size trade-offs** → Memory vs performance
2. **Mark/reset limitations** → Buffer overflow if read-ahead exceeded
3. **Thread safety** → Buffered streams are not thread-safe
4. **Large buffers** → May cause GC pauses
5. **Flush on every write** → Defeats purpose of buffering
6. **Closing underlying stream** → May leave buffer in inconsistent state

## 18. Debugging Tips

1. **Check buffer size** with available() method
2. **Monitor memory usage** for large buffers
3. **Use profiling tools** to identify IO bottlenecks
4. **Test with different buffer sizes** to find optimal
5. **Check for double buffering** in code
6. **Verify flush behavior** before closing

## 19. Comparison Table

| Feature | Unbuffered | Buffered (8KB) | Buffered (64KB) |
|---------|------------|----------------|-----------------|
| System calls (10MB) | ~10M | ~1,280 | ~160 |
| Memory | Minimal | 8KB | 64KB |
| Throughput | Low | High | Very High |
| Latency | High | Low | Very Low |
| Use case | Tiny ops | General | Large files |

## 20. Decision Tree

```
Need buffering?
├── Reading/writing small data (< 1KB)? → May not need buffering
├── Reading/writing files? → Use buffered streams
├── Large files (> 1MB)? → Use larger buffer (64KB+)
├── Many small operations? → Use default buffer (8KB)
├── Already using BufferedInputStream? → Don't add another
└── Need auto-flush? → Use PrintWriter with auto-flush
```

## 21. Interview Questions

### Q1: What is the purpose of buffered streams?
**Answer:** Buffered streams reduce the number of system calls by reading/writing larger chunks of data at once. This significantly improves performance, especially for file operations where system calls are expensive.

### Q2: What is the default buffer size?
**Answer:** The default buffer size is 8192 bytes (8KB) for byte streams and 8192 characters (16KB) for character streams. This is a good balance between memory usage and performance for most use cases.

### Q3: When should you increase the buffer size?
**Answer:** Increase buffer size when working with large files (> 1MB) or when performance is critical. Larger buffers (64KB-256KB) reduce system calls further but use more memory.

### Q4: What happens if you don't flush a BufferedOutputStream?
**Answer:** Data remaining in the buffer will be lost when the stream is closed. Always flush before closing if you need to ensure all data is written, or use try-with-resources which auto-flushes on close.

### Q5: Can you use mark/reset with buffered streams?
**Answer:** Yes, but with limitations. You must specify a read-ahead limit when calling mark(). If you read more than this limit without resetting, the mark is invalidated.

## 22. Exercises

### Exercise 1: Buffer Size Benchmark
Create a program that benchmarks different buffer sizes (1KB, 4KB, 8KB, 16KB, 64KB, 256KB) for file copying and displays the results.

### Exercise 2: Buffered File Processor
Write a program that processes a large text file using buffered streams, performing operations like line counting, word counting, and pattern matching.

### Exercise 3: Memory Monitor
Create a utility that monitors memory usage while processing files with different buffer sizes.

### Exercise 4: Buffered Stream Wrapper
Implement a custom buffered stream that tracks statistics (bytes read/written, system calls made).

## 23. Assignments

### Assignment 1: High-Performance File Copier
Create a file copier that:
1. Uses optimal buffer size based on file size
2. Supports parallel copying
3. Provides progress reporting
4. Handles large files (> 1GB) efficiently

### Assignment 2: Buffered Network Proxy
Implement a network proxy that:
1. Buffers data from client to server
2. Buffers data from server to client
3. Handles different buffer sizes for different traffic patterns
4. Provides statistics on buffer usage

## 24. Mini Project

**File Processing Pipeline**

Create a file processing pipeline that:
1. Reads files with configurable buffer sizes
2. Processes data in chunks
3. Writes output with buffering
4. Supports multiple processing stages
5. Provides performance metrics

Requirements:
- Use buffered streams throughout
- Implement proper error handling
- Add logging and statistics
- Support concurrent processing

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| Buffered Streams | Reduce system calls via buffering |
| Buffer Size | 8KB default, increase for large files |
| Performance | 10-100x improvement over unbuffered |
| Double Buffering | Avoid - wastes memory |
| Flush | Required before close for writes |
| Mark/Reset | Supported with read-ahead limit |

## 26. References

1. **Official Documentation**: [Buffered Streams](https://docs.oracle.com/javase/tutorial/essential/io/buffers.html)
2. **Baeldung**: [Java BufferedInputStream](https://www.baeldung.com/java-buffered-input-stream)
3. **Books**:
   - "Java I/O" by Elliotte Rusty Harold
   - "Java Performance" by Scott Oaks
4. **Related Topics**:
   - [03 - Byte Streams](../03-byte-streams/README.md)
   - [04 - Character Streams](../04-character-streams/README.md)
   - [06 - Data Streams](../06-data-streams/README.md)

---

**Next Topic**: [06 - Data Streams](../06-data-streams/README.md)
