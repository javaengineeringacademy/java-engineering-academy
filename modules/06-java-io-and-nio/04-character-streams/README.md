# 04 - Character Streams in Java IO

## 1. Introduction

Character streams are specialized IO streams designed for handling text data. Unlike byte streams that work with raw bytes, character streams automatically handle character encoding and decoding, making them essential for working with text files in different encodings (UTF-8, UTF-16, ISO-8859-1, etc.). Java provides a comprehensive hierarchy of character stream classes for reading and writing text data.

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
            String replacement) throws IOException {
        File tempFile = File.createTempFile("replace", ".txt");

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path));
             BufferedWriter writer = new BufferedWriter(
                new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line.replace(target, replacement));
                writer.newLine();
            }
        }

        // Replace original with modified
        Files.move(tempFile.toPath(),
            java.nio.file.Paths.get(path),
            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Reads CSV file into list of arrays.
     */
    public static List<String[]> readCsv(String path) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                rows.add(fields);
            }
        }

        return rows;
    }

    public static void main(String[] args) {
        try {
            // Create test file
            String testFile = "test-processor.txt";
            try (PrintWriter pw = new PrintWriter(
                    new FileWriter(testFile))) {
                pw.println("Hello, World!");
                pw.println("Java Character Streams are useful.");
                pw.println("They handle encoding automatically.");
                pw.println("Testing line operations.");
            }

            // Count stats
            long[] stats = countStats(testFile);
            System.out.printf("Lines: %d, Words: %d, Characters: %d%n",
                stats[0], stats[1], stats[2]);

            // Find lines
            System.out.println("\nLines containing 'Stream':");
            findLines(testFile, "Stream")
                .forEach(line -> System.out.println("  " + line));

            // Read with encoding
            System.out.println("\nReading with UTF-8:");
            String content = readWithEncoding(testFile, "UTF-8");
            System.out.println(content);

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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class ConcurrentTextProcessor {

    private final ExecutorService executor;
    private final Charset charset;

    public ConcurrentTextProcessor(int threadCount, Charset charset) {
        this.executor = Executors.newFixedThreadPool(threadCount);
        this.charset = charset;
    }

    /**
     * Processes file lines in parallel.
     */
    public <T> CompletableFuture<List<T>> processLinesParallel(
            String path, java.util.function.Function<String, T> processor)
            throws IOException {

        List<String> lines;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(path), charset))) {
            lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        List<CompletableFuture<T>> futures = lines.stream()
            .map(line -> CompletableFuture.supplyAsync(
                () -> processor.apply(line), executor))
            .toList();

        return CompletableFuture.allOf(
                futures.toArray(CompletableFuture[]::new))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .toList());
    }

    /**
     * Writes multiple strings to a file concurrently.
     */
    public void writeConcurrent(String path,
            List<String> content) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(path), charset))) {

            List<CompletableFuture<Void>> futures = content.stream()
                .map(line -> CompletableFuture.runAsync(() -> {
                    synchronized (writer) {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new CompletionException(e);
                        }
                    }
                }, executor))
                .toList();

            CompletableFuture.allOf(
                futures.toArray(CompletableFuture[]::new)).join();
        }
    }

    /**
     * Merges multiple sorted files into one sorted output.
     */
    public void mergeSortedFiles(List<String> inputPaths,
            String outputPath) throws IOException {

        List<BufferedReader> readers = new ArrayList<>();
        PriorityQueue<String[]> queue = new PriorityQueue<>(
            Comparator.comparing(a -> a[0]));

        try {
            // Open all input files
            for (String path : inputPaths) {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(path), charset));
                readers.add(reader);

                String line = reader.readLine();
                if (line != null) {
                    queue.offer(new String[]{line, String.valueOf(
                        readers.size() - 1)});
                }
            }

            // Merge
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(outputPath), charset))) {

                while (!queue.isEmpty()) {
                    String[] item = queue.poll();
                    writer.write(item[0]);
                    writer.newLine();

                    int readerIndex = Integer.parseInt(item[1]);
                    BufferedReader reader = readers.get(readerIndex);
                    String nextLine = reader.readLine();
                    if (nextLine != null) {
                        queue.offer(new String[]{nextLine,
                            String.valueOf(readerIndex)});
                    }
                }
            }

        } finally {
            for (BufferedReader reader : readers) {
                try { reader.close(); } catch (IOException ignored) { }
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        ConcurrentTextProcessor processor =
            new ConcurrentTextProcessor(4, StandardCharsets.UTF_8);

        try {
            // Create test file
            String testFile = "concurrent-test.txt";
            try (PrintWriter pw = new PrintWriter(
                    new FileWriter(testFile))) {
                for (int i = 0; i < 1000; i++) {
                    pw.println("Line " + i + ": " +
                        "Test data for concurrent processing");
                }
            }

            // Parallel processing
            long start = System.nanoTime();
            List<String> results = processor.processLinesParallel(
                testFile,
                line -> line.toUpperCase()
            ).join();
            long elapsed = System.nanoTime() - start;

            System.out.printf("Processed %,d lines in %,d ms%n",
                results.size(), elapsed / 1_000_000);

            // Cleanup
            new File(testFile).delete();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            processor.shutdown();
        }
    }
}
```

## 14. Performance

### Character Stream Performance

| Stream Type | Throughput | Memory | Use Case |
|-------------|------------|--------|----------|
| FileReader | Medium | Low | Small text files |
| BufferedReader | High | 16KB buffer | General text processing |
| InputStreamReader | Medium | Low | Encoding conversion |
| BufferedReader (large) | Higher | 64KB+ | Large file processing |

### Performance Tips

1. **Always use BufferedReader** for text file reading
2. **Use larger buffers** for large files
3. **Specify charset explicitly** to avoid platform dependency
4. **Use line-based processing** instead of character-by-character
5. **Buffer writes** to reduce system calls
6. **Use PrintWriter** for formatted text output

## 15. Best Practices

1. **Always specify charset** when creating InputStreamReader/OutputStreamWriter
2. **Use StandardCharsets constants** instead of string names
3. **Buffer character streams** for better performance
4. **Use try-with-resources** for automatic cleanup
5. **Prefer BufferedReader.readLine()** for line-based processing
6. **Use PrintWriter** for formatted text output
7. **Handle encoding errors** properly (replace, report, etc.)

## 16. Common Mistakes

1. **Not specifying charset** → Platform-dependent behavior
2. **Using byte streams for text** → Encoding issues
3. **Not buffering** → Poor performance
4. **Reading byte-by-byte** → Extremely slow
5. **Ignoring encoding errors** → Garbled output
6. **Mixing byte and character operations** → Data corruption
7. **Not flushing before close** → Data loss

## 17. Pitfalls

1. **Default charset varies by platform** → Always specify explicitly
2. **BOM (Byte Order Mark)** → Can cause issues with UTF-8
3. **Line separator differences** → Use \n or System.lineSeparator()
4. **Memory usage** → Character buffers use 2x memory of byte buffers
5. **Thread safety** → Character streams are not thread-safe
6. **Mark/reset limitations** → Not all readers support mark()

## 18. Debugging Tips

1. **Print character values** as Unicode: `System.out.printf("%c (U+%04X)%n", c, (int) c)`
2. **Check encoding** with `charset.name()`
3. **Use hex dump** to verify file encoding
4. **Monitor buffer usage** for performance issues
5. **Test with different encodings** to verify behavior

## 19. Comparison Table

| Feature | FileReader | BufferedReader | InputStreamReader | PrintWriter |
|---------|------------|----------------|-------------------|-------------|
| Buffering | No | Yes | No | Yes |
| Line reading | No | Yes | No | Yes |
| Encoding | Platform | Platform | Custom | Platform |
| Formatting | No | No | No | Yes |
| Performance | Low | High | Low | High |

## 20. Decision Tree

```
Need to read/write text?
├── Simple text file? → Use FileReader/FileWriter
├── Need line reading? → Use BufferedReader
├── Need specific encoding? → Use InputStreamReader/OutputStreamWriter
├── Need formatted output? → Use PrintWriter
├── Need performance? → Use BufferedReader with large buffer
└── Need encoding conversion? → Use InputStreamReader/OutputStreamWriter
```

## 21. Interview Questions

### Q1: What is the difference between FileReader and InputStreamReader?
**Answer:** `FileReader` is a convenience class that reads characters from a file using the platform's default charset. `InputStreamReader` is a bridge from byte streams to character streams, allowing you to specify the charset explicitly.

### Q2: Why should we always specify charset explicitly?
**Answer:** The default charset varies by platform (Windows uses Windows-1252, Linux uses UTF-8). Code that relies on default charset may behave differently on different systems. Always use StandardCharsets constants.

### Q3: What is the difference between readLine() and read(char[])?
**Answer:** `readLine()` reads an entire line (terminated by \n, \r, or \r\n) and returns it as a String. `read(char[])` reads characters into an array and returns the number of characters read. `readLine()` is more convenient but creates more String objects.

### Q4: How do you handle encoding errors?
**Answer:** Use `InputStreamReader` with a `CharsetDecoder` that specifies how to handle malformed input. Options include `CodingErrorAction.REPLACE`, `REPORT`, or `IGNORE`.

### Q5: What is the BOM and why is it problematic?
**Answer:** BOM (Byte Order Mark) is a special Unicode character (U+FEFF) at the start of a file indicating encoding. UTF-8 files with BOM can cause issues because many tools expect UTF-8 without BOM. Java's UTF-8 decoder ignores BOM by default.

## 22. Exercises

### Exercise 1: Character Encoding Converter
Write a program that reads a file with one encoding and writes it with another encoding.

### Exercise 2: Line Number Adder
Create a program that adds line numbers to a text file.

### Exercise 3: Word Frequency Counter
Write a program that counts the frequency of each word in a text file and displays the results sorted by frequency.

### Exercise 4: CSV Parser
Implement a CSV parser that handles quoted fields, escaped characters, and different encodings.

## 23. Assignments

### Assignment 1: Log File Processor
Create a log file processor that:
1. Reads log files with mixed encodings
2. Parses log entries (timestamp, level, message)
3. Filters entries by date range and log level
4. Writes filtered entries to a new file

### Assignment 2: Text File Comparator
Implement a text file comparator that:
1. Compares two files line by line
2. Handles different encodings
3. Shows differences with context
4. Supports ignore-whitespace mode

## 24. Mini Project

**Text File Analysis Tool**

Create a text file analysis tool that:
1. Reads files with automatic encoding detection
2. Calculates statistics (lines, words, characters, sentences)
3. Finds most common words and phrases
4. Detects language
5. Generates a summary report

Requirements:
- Use character streams
- Handle multiple encodings
- Implement proper error handling
- Add logging

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| Character Streams | Handle text data with encoding |
| Reader/Writer | Abstract base classes |
| InputStreamReader | Bridge from byte to character streams |
| BufferedReader | Efficient line-based reading |
| Encoding | Always specify explicitly |
| Try-with-resources | Automatic resource cleanup |

## 26. References

1. **Official Documentation**: [Character Streams](https://docs.oracle.com/javase/tutorial/essential/io/charstreams.html)
2. **Baeldung**: [Java Reader](https://www.baeldung.com/java-io-reader)
3. **Books**:
   - "Java I/O" by Elliotte Rusty Harold
   - "Java Performance" by Scott Oaks
4. **Related Topics**:
   - [03 - Byte Streams](../03-byte-streams/README.md)
   - [05 - Buffered Streams](../05-buffered-streams/README.md)
   - [06 - Data Streams](../06-data-streams/README.md)

---

**Next Topic**: [05 - Buffered Streams](../05-buffered-streams/README.md)
