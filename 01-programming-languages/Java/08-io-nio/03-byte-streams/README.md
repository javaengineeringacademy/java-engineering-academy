# 03 - Byte Streams in Java IO

## 1. Introduction

Byte streams are the most fundamental IO mechanism in Java. They handle raw binary data—bytes—without any interpretation or transformation. Every piece of data in a computer is ultimately stored as bytes, making byte streams the foundation upon which all other IO operations are built. Java provides a detailed hierarchy of byte stream classes for reading and writing binary data.

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

---

[📖 Continue to Part 2](README-part2.md)
