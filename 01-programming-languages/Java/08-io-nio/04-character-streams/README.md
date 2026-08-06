# 04 - Character Streams in Java IO

## 1. Introduction

Character streams are specialized IO streams designed for handling text data. Unlike byte streams that work with raw bytes, character streams automatically handle character encoding and decoding, making them essential for working with text files in different encodings (UTF-8, UTF-16, ISO-8859-1, etc.). Java provides a detailed hierarchy of character stream classes for reading and writing text data.

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Understand the character stream class hierarchy
- Read and write text data using character streams
- Handle character encoding properly
- Use stream decorators for character processing
- Understand the difference between byte and character streams
- Implement efficient text file processing

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of character encoding (ASCII, Unicode, UTF-8)
- Familiarity with exception handling
- Basic knowledge of byte streams (Topic 03)

## 4. Why This Concept Exists

Text data requires special handling because:
- Characters can be 1, 2, or 4 bytes depending on encoding
- Different platforms use different default encodings
- Text processing requires character-level operations (lines, words)
- Encoding/decoding must happen automatically

Character streams solve these problems by:
- Providing automatic charset handling
- Supporting line-based reading
- Offering character-level buffering
- Handling encoding conversion transparently

## 5. Problem Statement

Consider these scenarios:
1. Reading a UTF-8 encoded text file on a system with Latin-1 default encoding
2. Processing a CSV file with special characters (accents, symbols)
3. Reading files with different encodings (legacy systems)
4. Writing text files that must be readable across platforms
5. Processing large text files line by line without loading entire file

Character streams provide the solution for all these scenarios.

## 6. Theory

### 6.1 The Character Stream Hierarchy

```
java.io.Reader (abstract)
├── StringReader
├── FileReader
├── FilterReader
│   ├── BufferedReader
│   └── PushbackReader
├── InputStreamReader
├── PipedReader
└── CharArrayReader

java.io.Writer (abstract)
├── StringWriter
├── FileWriter
├── FilterWriter
│   └── BufferedWriter
├── OutputStreamWriter
├── PipedWriter
├── CharArrayWriter
└── PrintWriter
```

### 6.2 Reader vs Writer

| Feature | Reader | Writer |
|---------|--------|--------|
| Direction | Reading | Writing |
| Method | `read()` | `write()` |
| Returns | char or -1 | void |
| Blocking | Yes | Yes |
| Buffer | Internal | Internal |

### 6.3 The Bridge: InputStreamReader/OutputStreamWriter

```
Byte Stream → InputStreamReader → Character Stream
Character Stream → OutputStreamWriter → Byte Stream
```

These classes bridge byte and character streams, handling encoding/decoding.

### 6.4 Character Encoding

| Encoding | Description | Bytes per Character |
|----------|-------------|---------------------|
| ASCII | American Standard | 1 byte |
| ISO-8859-1 | Latin alphabet | 1 byte |
| UTF-8 | Unicode variable | 1-4 bytes |
| UTF-16 | Unicode fixed | 2-4 bytes |
| UTF-32 | Unicode fixed | 4 bytes |

## 7. Internal Working

### 7.1 How InputStreamReader Works

```
Application calls read()
    ↓
InputStreamReader.read()
    ↓
CharsetDecoder.decode()
    ↓
Byte stream provides raw bytes
    ↓
Decoder converts bytes to chars
    ↓
Character returned to application
```

### 7.2 Buffered Character Streams

```
BufferedReader:
┌─────────────────────────────────────────┐
│ Internal buffer: char[8192]             │
│ ┌───┬───┬───┬───┬───┬───┬───┬───┐     │
│ │ C │ C │ C │ C │ C │ C │ C │ C │ ... │
│ └───┴───┴───┴───┴───┴───┴───┴───┘     │
│ Position: 0                            │
│ Count: 8192 (chars in buffer)          │
└─────────────────────────────────────────┘
    ↓
When buffer empty, refill from underlying reader
    ↓
Application gets chars from buffer (fast)
```

### 7.3 Line Reading Algorithm

```
BufferedReader.readLine():
1. Check if buffer has data
2. Scan for '\n' or '\r\n'
3. If found, return substring up to line separator
4. If not found, read more data into buffer
5. Continue scanning
6. Return null at EOF
```

## 8. JVM Perspective

### 8.1 Memory Allocation

```
JVM Heap:
├── Reader/Writer objects (small)
├── Internal char arrays (buffer size)
│   └── Default: 8192 chars for BufferedReader
├── CharsetEncoder/CharsetDecoder objects
└── String objects (created from read data)

Native Memory:
├── File descriptors (OS resources)
└── OS file buffers
```

### 8.2 Character vs Byte Buffer Memory

```
byte[] buffer = new byte[8192];  // 8KB
char[] charBuffer = new char[8192]; // 16KB (2 bytes per char)

Character streams use more memory per buffer unit
but provide character-level operations.
```

## 9. Memory Representation

### Character Array Layout

```java
char[] chars = {'H', 'e', 'l', 'l', 'o'};
// Memory (UTF-16):
// [0x0048][0x0065][0x006C][0x006C][0x006F]
```

### Encoding Conversion

```
UTF-8 bytes: [0x48][0x65][0x6C][0x6C][0x6F]
    ↓ CharsetDecoder.decode()
UTF-16 chars: [0x0048][0x0065][0x006C][0x006C][0x006F]
```

## 10. Syntax

### 10.1 Basic Character Stream Operations

```java
// Reading characters
try (Reader reader = new FileReader("text.txt")) {
    int charValue;
    while ((charValue = reader.read()) != -1) {
        System.out.print((char) charValue);
    }
}

// Reading into char array
try (Reader reader = new FileReader("text.txt")) {
    char[] buffer = new char[1024];
    int charsRead;
    while ((charsRead = reader.read(buffer)) != -1) {
        processChars(buffer, 0, charsRead);
    }
}

// Writing characters
try (Writer writer = new FileWriter("output.txt")) {
    writer.write("Hello, World!");
    writer.write('\n');
    writer.flush();
}
```

### 10.2 Using InputStreamReader with Encoding

```java
// Read with specific encoding
try (Reader reader = new InputStreamReader(
        new FileInputStream("text.txt"), "UTF-8")) {
    // Read characters...
}

// Write with specific encoding
try (Writer writer = new OutputStreamWriter(
        new FileOutputStream("output.txt"), "UTF-8")) {
    writer.write("Hello, UTF-8!");
}
```

### 10.3 Buffered Character Streams

```java
// BufferedReader with line reading
try (BufferedReader reader = new BufferedReader(
        new FileReader("text.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
}

// BufferedWriter
try (BufferedWriter writer = new BufferedWriter(
        new FileWriter("output.txt"))) {
    writer.write("First line");
    writer.newLine();
    writer.write("Second line");
}

// PrintWriter for formatted output
try (PrintWriter pw = new PrintWriter(new FileWriter("output.txt"))) {
    pw.println("Hello");
    pw.printf("Number: %d%n", 42);
}
```

## 11. Easy Example

```java
import java.io.*;

public class CharacterStreamBasic {

    public static void main(String[] args) {
        String filename = "test-chars.txt";

        try {
            // Write characters
            try (Writer writer = new FileWriter(filename)) {
                writer.write("Hello, Character Streams!\n");
                writer.write("Line 2: Testing character writing.\n");
                writer.write("Line 3: Special chars: é, ñ, ü, 中文\n");
            }

            // Read characters one by one
            System.out.println("Reading character by character:");
            try (Reader reader = new FileReader(filename)) {
                int c;
                while ((c = reader.read()) != -1) {
                    System.out.print((char) c);
                }
            }

            // Read with char array
            System.out.println("\nReading with char array:");
            try (Reader reader = new FileReader(filename)) {
                char[] buffer = new char[32];
                int charsRead;
                while ((charsRead = reader.read(buffer)) != -1) {
                    System.out.print(new String(buffer, 0, charsRead));
                }
            }

            // Read lines with BufferedReader
            System.out.println("\nReading lines with BufferedReader:");
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(filename))) {
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    System.out.printf("Line %d: %s%n", lineNumber, line);
                    lineNumber++;
                }
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
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.*;

public class TextFileProcessor {

    /**
     * Reads a file with specified encoding.
     */
    public static String readWithEncoding(String path, String charset)
            throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(path),
                    charset))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * Counts lines, words, and characters in a file.
     */
    public static long[] countStats(String path) throws IOException {
        long lines = 0, words = 0, chars = 0;

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines++;
                words += line.split("\\s+").length;
                chars += line.length();
            }
        }

        return new long[]{lines, words, chars};
    }

    /**
     * Finds lines matching a pattern.
     */
    public static List<String> findLines(String path, String pattern)
            throws IOException {
        List<String> matches = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(pattern)) {
                    matches.add(line);
                }
            }
        }

        return matches;
    }

    /**
     * Replaces text in a file.
     */
    public static void replaceInFile(String path, String target,

---

[📖 Continue to Part 2](README-part2.md)
```
