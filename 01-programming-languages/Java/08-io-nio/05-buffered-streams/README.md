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

---

[📖 Continue to Part 2](README-part2.md)
