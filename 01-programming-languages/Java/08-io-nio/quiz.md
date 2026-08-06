# Java I/O and NIO Quiz

## Question 1 (MCQ)
What is the primary difference between InputStream and Reader?
- A) InputStream is faster than Reader
- B) InputStream handles raw bytes, while Reader handles characters with encoding
- C) They are identical
- D) Reader only works with files

**Answer: B**
**Explanation:** InputStream and its subclasses deal with raw byte data. Reader and its subclasses handle character data, converting bytes to characters using a specified charset encoding.

---

## Question 2 (MCQ)
What is the key advantage of NIO Channels over traditional I/O Streams?
- A) Channels are simpler to use
- B) Channels support non-blocking I/O operations
- C) Channels are only for reading
- D) Channels don't need buffers

**Answer: B**
**Explanation:** NIO Channels can operate in non-blocking mode, allowing a single thread to manage multiple I/O operations. Traditional Streams are always blocking, requiring dedicated threads for each connection.

---

## Question 3 (MCQ)
What is the correct order of buffer operations when reading from a buffer?
- A) clear → get → flip
- B) flip → get → clear
- C) put → flip → get
- D) clear → put → flip

**Answer: B**
**Explanation:** When reading: `flip()` switches the buffer from write mode to read mode (sets limit to position, position to 0), `get()` reads data, and `clear()` resets the buffer for writing again.

---

## Question 4 (MCQ)
Which class is used to read a file as a Stream of lines in Java NIO.2?
- A) FileReader
- B) BufferedReader
- C) Files.lines()
- D) FileChannel

**Answer: C**
**Explanation:** `Files.lines(Path)` returns a `Stream<String>` of lines from a file, which is lazily loaded and can be processed using the Stream API. It's the modern approach introduced in Java 8.

---

## Question 5 (Code Output)
What does this code print?

```java
import java.nio.file.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("test.txt");
        Files.writeString(path, "Line1\nLine2\nLine3");

        try (Stream<String> lines = Files.lines(path)) {
            long count = lines.count();
            System.out.println(count);
        }

        Files.delete(path);
    }
}
```

**Answer:** 3
**Explanation:** The file contains 3 lines. `Files.lines()` returns a Stream of lines, and `count()` returns the total number of lines. The file is deleted after counting.

---

## Question 6 (Code Output)
What does this code print?

```java
import java.nio.file.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Path dir = Path.of("temp_dir");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("file1.txt"), "Hello");
        Files.writeString(dir.resolve("file2.txt"), "World");

        try (Stream<Path> files = Files.list(dir)) {
            files.forEach(System.out::println);
        }

        Files.delete(dir.resolve("file1.txt"));
        Files.delete(dir.resolve("file2.txt"));
        Files.delete(dir);
    }
}
```

**Answer:** temp_dir/file1.txt and temp_dir/file2.txt (two file paths)
**Explanation:** `Files.list()` returns a Stream of Path objects in the directory. Each path is printed. The order may vary. Files are cleaned up after listing.

---

## Question 7 (Bug Finding)
Find the bug:

```java
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("data.bin");
        byte[] buffer = new byte[1024];
        int bytesRead = fis.read(buffer);
        System.out.println("Read " + bytesRead + " bytes");
        fis.close();
    }
}
```

**Bug:** If an exception occurs before `fis.close()`, the file handle leaks. The resource is not properly managed in a finally block or try-with-resources.
**Fix:** Use try-with-resources:
```java
try (FileInputStream fis = new FileInputStream("data.bin")) {
    byte[] buffer = new byte[1024];
    int bytesRead = fis.read(buffer);
    System.out.println("Read " + bytesRead + " bytes");
}
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws IOException {
        FileChannel channel = FileChannel.open(Path.of("file.txt"), StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        // Process buffer...
        channel.close();
    }
}
```

**Bug:** The channel is not closed in a finally block or try-with-resources. If an exception occurs during `channel.read()`, the channel remains open, leaking a file descriptor.
**Fix:** Use try-with-resources:
```java
try (FileChannel channel = FileChannel.open(Path.of("file.txt"), StandardOpenOption.READ)) {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    channel.read(buffer);
    buffer.flip();
    // Process buffer...
}
```

---

## Question 9 (Scenario-based)
You need to read a 5GB log file and count lines containing "ERROR". Which approach is most memory-efficient?

- A) Read the entire file into a String and count occurrences
- B) Use BufferedReader to read line by line and count matches
- C) Use FileChannel with a large ByteBuffer
- D) Use Scanner with a regex pattern

**Answer: B**
**Explanation:** BufferedReader reads one line at a time into memory, using constant O(1) memory regardless of file size. This is the most memory-efficient approach for processing large files line by line.

---

## Question 10 (Architecture Decision)
You are building a file upload service that needs to: (1) receive large files (up to 10GB), (2) process them without loading entirely into memory, (3) support concurrent uploads, and (4) write to distributed storage. How should you design the I/O layer?

- A) Use FileInputStream to read entire file, then write to storage
- B) Use NIO Channels with memory-mapped files for efficient large file handling, combined with a thread pool for concurrent processing
- C) Use BufferedReader for all file operations
- D) Use Socket channels for file transfer

**Answer: B**
**Explanation:** NIO Channels with memory-mapped files allow efficient large file handling without loading the entire file into heap memory. A thread pool manages concurrent uploads. This design scales to large files while maintaining throughput.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("test.txt"))) {
            bw.write("First");
            bw.newLine();
            bw.write("Second");
        }

        try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

        new File("test.txt").delete();
    }
}
```

A) First Second
B) First\nSecond
C) First
   Second
D) FirstSecond

**Answer: C**
**Explanation:** try-with-resources ensures both writer and reader are closed properly. `bw.newLine()` writes a platform-specific line separator. `br.readLine()` reads lines one at a time. The output prints each line on a separate line: `First` then `Second`. `newLine()` writes `\n` on Unix/macOS.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import java.nio.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.put((byte) 'H');
        buffer.put((byte) 'i');

        System.out.println("Position: " + buffer.position());
        System.out.println("Limit: " + buffer.limit());

        buffer.flip();

        System.out.println("Position after flip: " + buffer.position());
        System.out.println("Limit after flip: " + buffer.limit());

        System.out.println((char) buffer.get());
        System.out.println("Position after get: " + buffer.position());
    }
}
```

A) Position: 2 Limit: 20 Position after flip: 0 Limit after flip: 2 Position after get: 1
B) Position: 2 Limit: 20 Position after flip: 2 Limit after flip: 20 Position after get: 3
C) Position: 2 Limit: 20 Position after flip: 0 Limit after flip: 2 Position after get: 2
D) Compilation error

**Answer: A**
**Explanation:** After two `put()` calls, position=2, limit=20 (capacity). `flip()` sets limit to position (2) and position to 0 — switching from write to read mode. `get()` reads one byte ('H'), advancing position to 1. Output: `Position: 2 Limit: 20 Position after flip: 0 Limit after flip: 2 Position after get: 1`.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
import java.nio.file.*;
import java.io.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Path dir = Path.of("test_nio_dir");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("a.txt"), "content");
        Files.writeString(dir.resolve("b.java"), "content");
        Files.writeString(dir.resolve("c.txt"), "content");

        try (Stream<Path> files = Files.list(dir)) {
            long count = files
                .filter(p -> p.toString().endsWith(".txt"))
                .count();
            System.out.println("TXT files: " + count);
        }

        Files.walk(dir).forEach(p -> {
            try { Files.delete(p); } catch (IOException e) {}
        });
    }
}
```

A) TXT files: 2
B) TXT files: 3
C) TXT files: 1
D) Compilation error

**Answer: A**
**Explanation:** Three files are created: a.txt, b.java, c.txt. `Files.list()` returns all files in the directory. The filter counts only `.txt` files: a.txt and c.txt = 2. `Files.walk()` deletes all files and directories recursively for cleanup. Output: `TXT files: 2`.

