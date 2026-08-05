# 03 - Byte Streams in Java IO

## 1. Introduction

Byte streams are the most fundamental IO mechanism in Java. They handle raw binary data—bytes—without any interpretation or transformation. Every piece of data in a computer is ultimately stored as bytes, making byte streams the foundation upon which all other IO operations are built. Java provides a comprehensive hierarchy of byte stream classes for reading and writing binary data.

## 2. Learning Objectives

By the end of this topic, you will be able:

- Understand the byte stream class hierarchy
- Read and write binary data using byte streams
- Use stream decorators to add functionality
- Handle large binary files efficiently
- Implement custom byte streams
- Understand the difference between byte and character streams

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of binary data representation
- Familiarity with exception handling
- Basic knowledge of streams (Topic 01)

## 4. Why This Concept Exists

Many types of data cannot be represented as text:
- Images (JPEG, PNG, GIF)
- Audio and video files
- Compressed archives (ZIP, JAR)
- Serialized objects
- Database files
- Compiled bytecode

Byte streams provide a way to handle this raw binary data without any encoding or decoding.

## 5. Problem Statement

Consider these scenarios:
1. Reading a JPEG image file to display in a web application
2. Copying a ZIP archive from one location to another
3. Streaming audio data from a file to a player
4. Reading serialized Java objects from a file
5. Processing binary sensor data

All these require handling raw bytes, not characters. Byte streams provide the mechanism.

## 6. Theory

### 6.1 The Byte Stream Hierarchy

```
java.io.InputStream (abstract)
├── ByteArrayInputStream
├── FileInputStream
├── FilterInputStream
│   ├── BufferedInputStream
│   ├── DataInputStream
│   └── PushbackInputStream
├── ObjectInputStream
├── PipedInputStream
├── SequenceInputStream
└── StringBufferInputStream (deprecated)

java.io.OutputStream (abstract)
├── ByteArrayOutputStream
├── FileOutputStream
├── FilterOutputStream
│   ├── BufferedOutputStream
│   ├── DataOutputStream
│   └── PrintStream
├── ObjectOutputStream
└── PipedOutputStream
```

### 6.2 InputStream vs OutputStream

| Feature | InputStream | OutputStream |
|---------|-------------|--------------|
| Direction | Reading | Writing |
| Method | `read()` | `write()` |
| Returns | -1 on EOF | void |
| Blocking | Yes | Yes |
| Buffer | Internal | Internal |

### 6.3 The Decorator Pattern

Byte streams use the Decorator pattern extensively:

```java
// Basic file reading
FileInputStream fis = new FileInputStream("data.bin");

// Add buffering
BufferedInputStream bis = new BufferedInputStream(fis);

// Add data type reading
DataInputStream dis = new DataInputStream(bis);
```

Each wrapper adds functionality while maintaining the same interface.

## 7. Internal Working

### 7.1 How FileInputStream Works

```
Application calls read()
    ↓
FileInputStream.read()
    ↓
JNI (Java Native Interface)
    ↓
OS system call (read())
    ↓
File system reads from disk
    ↓
Data copied to Java buffer
    ↓
Byte returned to application
```

### 7.2 Buffered Stream Internals

```
BufferedInputStream:
┌─────────────────────────────────────────┐
│ Internal buffer: byte[8192]             │
│ ┌───┬───┬───┬───┬───┬───┬───┬───┐     │
│ │ B │ B │ B │ B │ B │ B │ B │ B │ ... │
│ └───┴───┴───┴───┴───┴───┴───┴───┘     │
│ Position: 0                            │
│ Count: 8192 (bytes in buffer)          │
└─────────────────────────────────────────┘
    ↓
When buffer empty, refill from underlying stream
    ↓
Application gets bytes from buffer (fast)
```

### 7.3 DataInputStream Protocol

DataInputStream uses a specific byte order protocol:
- Integers: 4 bytes, big-endian
- Longs: 8 bytes, big-endian
- Floats: 4 bytes, IEEE 754
- Doubles: 8 bytes, IEEE 754
- UTF strings: Modified UTF-8 encoding

## 8. JVM Perspective

### 8.1 Memory Allocation

```
JVM Heap:
├── Stream wrapper objects (small)
├── Internal byte arrays (buffer size)
│   └── Default: 8192 bytes for BufferedInputStream
└── Exception objects (on error)

Native Memory:
├── File descriptors (OS resources)
├── OS file buffers
└── Network socket buffers
```

### 8.2 Garbage Collection

- Stream objects are typically short-lived
- Internal buffers may live longer (promoted to Old Gen)
- Closing streams releases native resources
- Unclosed streams → native memory leaks

## 9. Memory Representation

### Byte Array Layout

```java
byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
// Memory: [72][101][108][108][111]
// ASCII:  [ H ][ e ][ l ][ l ][ o ]
```

### Buffered Stream Memory

```
BufferedInputStream (8KB buffer):
┌────────────────────────────────────────┐
│ byte[] buf = new byte[8192];           │
│ int pos = 0;    // current position    │
│ int count = 0;  // bytes in buffer     │
│ int markPos = -1;                      │
│ int markLimit = -1;                    │
└────────────────────────────────────────┘
```

## 10. Syntax

### 10.1 Basic Byte Stream Operations

```java
// Reading a single byte
try (FileInputStream fis = new FileInputStream("data.bin")) {
    int byteValue = fis.read();  // Returns -1 at EOF
    while (byteValue != -1) {
        processByte((byte) byteValue);
        byteValue = fis.read();
    }
}

// Reading into byte array
try (FileInputStream fis = new FileInputStream("data.bin")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        processBytes(buffer, 0, bytesRead);
    }
}

// Writing bytes
try (FileOutputStream fos = new FileOutputStream("output.bin")) {
    byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
    fos.write(data);
    fos.flush();
}
```

### 10.2 Using Decorators

```java
// Buffered byte streams
try (BufferedInputStream bis = new BufferedInputStream(
        new FileInputStream("large.bin"));
     BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream("copy.bin"))) {
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
    }
}

// Data streams for primitives
try (DataInputStream dis = new DataInputStream(
        new BufferedInputStream(new FileInputStream("data.bin")))) {
    int intValue = dis.readInt();
    double doubleValue = dis.readDouble();
    String utfString = dis.readUTF();
}

// PrintStream for formatted output
PrintStream ps = new PrintStream(new FileOutputStream("output.txt"));
ps.println("Hello, World!");
ps.printf("Number: %d%n", 42);
```

## 11. Easy Example

```java
import java.io.*;

public class ByteStreamBasic {

    public static void main(String[] args) {
        String filename = "test-byte-stream.bin";

        try {
            // Write bytes to file
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                fos.write(72);   // 'H'
                fos.write(101);  // 'e'
                fos.write(108);  // 'l'
                fos.write(108);  // 'l'
                fos.write(111);  // 'o'
            }

            // Read bytes from file
            try (FileInputStream fis = new FileInputStream(filename)) {
                int b;
                while ((b = fis.read()) != -1) {
                    System.out.print((char) b);
                }
                System.out.println();
            }

            // Write byte array
            byte[] data = "Hello, Byte Streams!".getBytes();
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                fos.write(data);
            }

            // Read byte array
            try (FileInputStream fis = new FileInputStream(filename);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                System.out.println(baos.toString());
            }

            // Cleanup
            new File(filename).delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 12. Medium Example

```java
import java.io.*;
import java.util.zip.*;

public class BinaryFileProcessor {

    /**
     * Copies a file using buffered byte streams.
     */
    public static void copyFile(String source, String destination)
            throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(source), 16384);
             BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destination), 16384)) {

            byte[] buffer = new byte[16384];
            int bytesRead;
            long totalBytes = 0;

            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }

            System.out.printf("Copied %,d bytes%n", totalBytes);
        }
    }

    /**
     * Creates a simple checksum for a file.
     */
    public static int calculateChecksum(String filename)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            int checksum = 0;
            int b;
            while ((b = fis.read()) != -1) {
                checksum ^= b;
            }
            return checksum;
        }
    }

    /**
     * Finds a byte pattern in a file.
     */
    public static long findPattern(String filename, byte[] pattern)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            long position = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead - pattern.length + 1; i++) {
                    boolean found = true;
                    for (int j = 0; j < pattern.length; j++) {
                        if (buffer[i + j] != pattern[j]) {
                            found = false;
                            break;
                        }
                    }
                    if (found) {
                        return position + i;
                    }
                }
                position += bytesRead;
            }
            return -1;
        }
    }

    /**
     * Reads a file and displays hex dump.
     */
    public static void hexDump(String filename, int maxLines)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] buffer = new byte[16];
            int bytesRead;
            int lineCount = 0;

            while ((bytesRead = fis.read(buffer)) != -1 &&
                    lineCount < maxLines) {
                // Offset
                System.out.printf("%08X: ", lineCount * 16);

                // Hex bytes
                for (int i = 0; i < 16; i++) {
                    if (i < bytesRead) {
                        System.out.printf("%02X ", buffer[i]);
                    } else {
                        System.out.print("   ");
                    }
                    if (i == 7) System.out.print(" ");
                }

                // ASCII
                System.out.print(" |");
                for (int i = 0; i < bytesRead; i++) {
                    char c = (char) (buffer[i] & 0xFF);
                    System.out.print(
                        (c >= 32 && c < 127) ? c : '.');
                }
                System.out.println("|");

                lineCount++;
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Create test file
            String testFile = "test-binary.bin";
            byte[] testData = new byte[256];
            for (int i = 0; i < 256; i++) {
                testData[i] = (byte) i;
            }
            try (FileOutputStream fos = new FileOutputStream(testFile)) {
                fos.write(testData);
            }

            // Hex dump
            System.out.println("Hex dump of test file:");
            hexDump(testFile, 16);

            // Checksum
            System.out.printf("%nChecksum: 0x%04X%n",
                calculateChecksum(testFile));

            // Find pattern
            byte[] pattern = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
            long pos = findPattern(testFile, pattern);
            System.out.printf("Pattern found at position: %d%n", pos);

            // Cleanup
            new File(testFile).delete();

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

public class AsyncFileCopier {

    private static final int BUFFER_SIZE = 64 * 1024; // 64KB
    private final ExecutorService executor;

    public AsyncFileCopier() {
        this.executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
    }

    /**
     * Copies a file using multiple threads.
     */
    public CompletableFuture<Long> copyFileParallel(String source,
            String destination) throws IOException {

        File sourceFile = new File(source);
        long fileSize = sourceFile.length();
        int chunkCount = (int) Math.ceil((double) fileSize / BUFFER_SIZE);

        CompletableFuture<Long> future = new CompletableFuture<>();
        AtomicInteger completedChunks = new AtomicInteger(0);
        AtomicLong totalBytesCopied = new AtomicLong(0);

        try (RandomAccessFile srcRaf = new RandomAccessFile(sourceFile, "r");
             RandomAccessFile dstRaf = new RandomAccessFile(
                new File(destination), "rw")) {

            dstRaf.setLength(fileSize);

            for (int i = 0; i < chunkCount; i++) {
                final long startPos = (long) i * BUFFER_SIZE;
                final int chunkSize = (int) Math.min(BUFFER_SIZE,
                    fileSize - startPos);

                executor.submit(() -> {
                    try {
                        byte[] buffer = new byte[chunkSize];

                        // Read chunk
                        synchronized (srcRaf) {
                            srcRaf.seek(startPos);
                            srcRaf.readFully(buffer);
                        }

                        // Write chunk
                        synchronized (dstRaf) {
                            dstRaf.seek(startPos);
                            dstRaf.write(buffer);
                        }

                        totalBytesCopied.addAndGet(chunkSize);

                        if (completedChunks.incrementAndGet() == chunkCount) {
                            future.complete(totalBytesCopied.get());
                        }
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                });
            }

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    /**
     * Compresses a file using GZIP.
     */
    public void gzipFile(String source, String destination)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination);
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                gzos.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Decompresses a GZIP file.
     */
    public void gunzipFile(String source, String destination)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             GZIPInputStream gzis = new GZIPInputStream(fis);
             FileOutputStream fos = new FileOutputStream(destination)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gzis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
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
        AsyncFileCopier copier = new AsyncFileCopier();

        try {
            // Create test file
            String sourceFile = "large-test.bin";
            byte[] data = new byte[10 * 1024 * 1024]; // 10MB
            new java.util.Random().nextBytes(data);
            try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
                fos.write(data);
            }

            // Parallel copy
            long start = System.nanoTime();
            String copyFile = "large-test-copy.bin";
            long bytesCopied = copier.copyFileParallel(
                sourceFile, copyFile).join();
            long elapsed = System.nanoTime() - start;

            System.out.printf("Copied %,d bytes in %,d ms%n",
                bytesCopied, elapsed / 1_000_000);
            System.out.printf("Speed: %.2f MB/s%n",
                (bytesCopied / 1024.0 / 1024.0) / (elapsed / 1e9));

            // GZIP compression
            start = System.nanoTime();
            String gzFile = "large-test.gz";
            copier.gzipFile(sourceFile, gzFile);
            elapsed = System.nanoTime() - start;

            long originalSize = new File(sourceFile).length();
            long compressedSize = new File(gzFile).length();
            System.out.printf("%nCompression: %,d → %,d bytes (%.1f%%)%n",
                originalSize, compressedSize,
                (1.0 - (double) compressedSize / originalSize) * 100);

            // Cleanup
            new File(sourceFile).delete();
            new File(copyFile).delete();
            new File(gzFile).delete();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            copier.shutdown();
        }
    }
}
```

## 14. Performance

### Byte Stream Performance Characteristics

| Stream Type | Throughput | Memory Usage | Use Case |
|-------------|------------|--------------|----------|
| FileInputStream | Medium | Low | Small files |
| BufferedInputStream | High | 8KB buffer | General purpose |
| BufferedInputStream (large buffer) | Higher | 64KB+ buffer | Large file copies |
| DataInputStream | Medium | Low | Primitive reading |
| FileChannel | Very High | Configurable | Large file transfers |

### Performance Tips

1. **Always use buffered streams** for file operations
2. **Use larger buffers** (64KB-256KB) for large file copies
3. **Avoid byte-by-byte reading** - always use arrays
4. **Use FileChannel.transferTo()** for large file copies
5. **Flush at appropriate intervals** for write operations
6. **Close streams promptly** to release resources

## 15. Best Practices

1. **Always use try-with-resources** for automatic cleanup
2. **Buffer appropriately** - 8KB for general use, larger for bulk operations
3. **Check read return values** - may return fewer bytes than requested
4. **Handle EOF properly** - read() returns -1
5. **Flush output streams** when needed
6. **Use appropriate buffer sizes** for your use case
7. **Close streams in reverse order** of opening
8. **Don't mix byte and character operations** on the same stream

## 16. Common Mistakes

1. **Not checking read() return value** → Incomplete reads
2. **Using byte streams for text** → Encoding issues
3. **Not flushing before close** → Data loss
4. **Buffering already buffered streams** → Wasted memory
5. **Reading byte-by-byte** → Extremely slow
6. **Not closing streams** → Resource leaks
7. **Mixing signed/unsigned bytes** → Incorrect values

## 17. Pitfalls

1. **Signed bytes in Java** → Range -128 to 127, use & 0xFF for unsigned
2. **Platform line endings** → Use System.lineSeparator() or \n
3. **Buffer overflow** → Check buffer capacity
4. **File encoding** → Byte streams don't handle encoding
5. **Concurrent access** → Streams are not thread-safe
6. **Large file memory** → Don't load entire file into memory

## 18. Debugging Tips

1. **Print byte values** as hex: `System.out.printf("%02X ", b)`
2. **Check stream position** with mark/reset
3. **Use available()** to check buffered data
4. **Enable IO logging** for debugging
5. **Use hex dump tools** for binary file analysis
6. **Monitor memory usage** for large buffer operations

## 19. Comparison Table

| Feature | FileInputStream | BufferedInputStream | DataInputStream | FileChannel |
|---------|-----------------|---------------------|-----------------|-------------|
| Buffering | No | Yes | Optional | Yes |
| Primitive reading | No | No | Yes | No |
| Random access | No | No | No | Yes |
| Memory-mapped | No | No | No | Yes |
| Thread-safe | No | No | No | With locking |
| Performance | Low | High | Medium | Very High |

## 20. Decision Tree

```
Need to read/write binary data?
├── Small data (< 1KB)? → Use basic streams
├── Large data (> 1KB)? → Use buffered streams
├── Need primitive types? → Use DataInputStream/DataOutputStream
├── Need random access? → Use RandomAccessFile or FileChannel
├── Large file copy? → Use FileChannel.transferTo()
└── Need compression? → Use GZIPOutputStream/GZIPInputStream
```

## 21. Interview Questions

### Q1: What is the difference between read() and read(byte[])?
**Answer:** `read()` reads a single byte and returns it as an int (0-255) or -1 at EOF. `read(byte[])` reads multiple bytes into the array and returns the number of bytes actually read (may be less than array length).

### Q2: Why do we need buffered streams?
**Answer:** Unbuffered streams make a system call for every read/write operation, which is expensive. Buffered streams reduce system calls by reading/writing larger chunks at once, significantly improving performance.

### Q3: What happens if you don't flush a BufferedOutputStream?
**Answer:** Data remaining in the buffer will be lost when the stream is closed. Always flush before closing if you need to ensure all data is written.

### Q4: How do you convert a byte to an unsigned value?
**Answer:** Use bitwise AND with 0xFF: `int unsigned = byteValue & 0xFF;`. This converts the signed byte (-128 to 127) to an unsigned int (0 to 255).

### Q5: What is the difference between close() and flush()?
**Answer:** `flush()` forces any buffered data to be written to the underlying stream. `close()` flushes and then releases system resources (file descriptors, memory).

## 22. Exercises

### Exercise 1: File Copy Program
Write a program that copies a file using byte streams with buffering. Measure and compare performance with different buffer sizes (1KB, 8KB, 64KB, 256KB).

### Exercise 2: Hex Dump Tool
Create a hex dump utility that displays file contents in hexadecimal format, similar to the `xxd` command.

### Exercise 3: Simple Encryption
Implement a simple XOR encryption/decryption using byte streams.

### Exercise 4: File Merger
Write a program that merges multiple files into a single output file, with a header indicating file names and sizes.

## 23. Assignments

### Assignment 1: Binary File Processor
Create a program that:
1. Reads a binary file containing integer records
2. Sorts the records
3. Writes the sorted records to a new file
4. Handles files larger than available memory

### Assignment 2: Network Packet Simulator
Implement a packet simulator that:
1. Creates binary packets with headers
2. Serializes packets to files
3. Reads and validates packets
4. Handles packet loss and retransmission

## 24. Mini Project

**File Encryption System**

Create a file encryption system that:
1. Reads files using byte streams
2. Applies AES encryption
3. Writes encrypted files
4. Supports decryption
5. Handles large files efficiently
6. Provides progress reporting

Requirements:
- Use buffered streams
- Implement proper key management
- Handle exceptions gracefully
- Add logging

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| Byte Streams | Handle raw binary data |
| InputStream/OutputStream | Abstract base classes |
| Buffered Streams | Reduce system calls |
| Data Streams | Read/write primitives |
| Decorator Pattern | Composable stream functionality |
| Try-with-resources | Automatic resource cleanup |

## 26. References

1. **Official Documentation**: [Byte Streams](https://docs.oracle.com/en/java/javase/21/essential/io/bytestreams.html)
2. **Baeldung**: [Java InputStream](https://www.baeldung.com/java-io-inputstream)
3. **Books**:
   - "Java I/O" by Elliotte Rusty Harold
   - "Java Performance" by Scott Oaks
4. **Related Topics**:
   - [04 - Character Streams](../04-character-streams/README.md)
   - [05 - Buffered Streams](../05-buffered-streams/README.md)
   - [09 - NIO Channels](../09-nio-channels/README.md)

---

**Next Topic**: [04 - Character Streams](../04-character-streams/README.md)
