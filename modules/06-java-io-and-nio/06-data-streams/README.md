# 06 - Data Streams in Java IO

## 1. Introduction

Data streams are specialized streams for reading and writing primitive Java data types (int, double, boolean, String, etc.) in a portable, binary format. They are essential when you need to write structured data to files and read it back reliably. Java provides `DataInputStream` and `DataOutputStream` for this purpose, along with `RandomAccessFile` which combines data stream capabilities with random access.

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Read and write primitive data types using data streams
- Understand binary data representation formats
- Use RandomAccessFile for random access operations
- Implement structured file formats
- Handle endianness and data portability
- Build binary file processors

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of byte streams (Topic 03)
- Familiarity with primitive data types
- Basic understanding of binary representation

## 4. Why This Concept Exists

When you need to store structured data in files, you have several options:

| Format | Pros | Cons |
|--------|------|------|
| Text | Human-readable, simple | Large, parsing overhead |
| JSON/XML | Standard, self-describing | Verbose, parsing required |
| Binary (Data Streams) | Compact, fast, type-safe | Not human-readable |

Data streams provide:
- Type-safe binary serialization
- Portable data formats (big-endian by default)
- Efficient storage and retrieval
- Direct support for Java primitives

## 5. Problem Statement

Consider an application that needs to:
1. Store user records (ID, name, email, balance)
2. Read records by position (random access)
3. Update specific fields without rewriting entire file
4. Handle different data types efficiently
5. Ensure data portability across platforms

Data streams and RandomAccessFile solve these problems.

## 6. Theory

### 6.1 DataInputStream/DataOutputStream

| Method | Data Type | Bytes | Format |
|--------|-----------|-------|--------|
| `writeInt/readInt` | int | 4 | Big-endian |
| `writeLong/readLong` | long | 8 | Big-endian |
| `writeFloat/readFloat` | float | 4 | IEEE 754 |
| `writeDouble/readDouble` | double | 8 | IEEE 754 |
| `writeBoolean/readBoolean` | boolean | 1 | 0x00/0xFF |
| `writeByte/readByte` | byte | 1 | Direct |
| `writeShort/readShort` | short | 2 | Big-endian |
| `writeChar/readChar` | char | 2 | Big-endian |
| `writeUTF/readUTF` | String | 2 + bytes | Modified UTF-8 |

### 6.2 RandomAccessFile

RandomAccessFile provides both read and write capabilities with random access:

```
File: [Rec1][Rec2][Rec3][Rec4][Rec5]
            ↑
            seek(128) → Read/Write Record3
```

### 6.3 Endianness

- **Big-endian** (Java default): Most significant byte first
- **Little-endian**: Least significant byte first (x86, ARM)

```java
// Big-endian (default)
int value = 0x12345678;
bytes: [0x12][0x34][0x56][0x78]

// Little-endian
bytes: [0x78][0x56][0x34][0x12]
```

## 7. Internal Working

### 7.1 DataOutputStream Write Operations

```
writeInt(0x12345678):
    ↓
Write 4 bytes in big-endian order:
    [0x12][0x34][0x56][0x78]
    ↓
Buffered to reduce system calls
    ↓
Flushed when buffer full or flush() called
```

### 7.2 DataInputStream Read Operations

```
readInt():
    ↓
Read 4 bytes from stream:
    [0x12][0x34][0x56][0x78]
    ↓
Combine bytes in big-endian order:
    (0x12 << 24) | (0x34 << 16) | (0x56 << 8) | 0x78
    ↓
Return int value: 0x12345678
```

### 7.3 RandomAccessFile File Pointer

```
RandomAccessFile:
┌────────────────────────────────────────┐
│ File descriptor                         │
│ File pointer position (long)           │
│ Length (long)                           │
│ Mode ("r" or "rw")                     │
└────────────────────────────────────────┘

seek(position): Sets file pointer to position
getFilePointer(): Returns current position
length(): Returns file size
```

## 8. JVM Perspective

### 8.1 Memory Allocation

```
JVM Heap:
├── DataInputStream object (48 bytes)
├── DataOutputStream object (48 bytes)
├── RandomAccessFile object (64 bytes)
└── Internal buffers (if using Buffered streams)

Native Memory:
├── File descriptor
├── OS file buffers
└── Memory-mapped regions (if using map())
```

### 8.2 Data Alignment

Java data streams don't require data alignment:
- int can be at any byte position
- long can be at any byte position
- This differs from some native formats that require alignment

## 9. Memory Representation

### Integer Representation

```java
int value = 42;
// Big-endian: [0x00][0x00][0x00][0x2A]
// Little-endian: [0x2A][0x00][0x00][0x00]
```

### Double Representation (IEEE 754)

```java
double value = 3.14;
// Sign: 0 (positive)
// Exponent: 10000000000 (1027)
// Mantissa: 1001000111101011100001010001111010111000010100011111
// Bytes: [0x40][0x09][0x1E][0xB8][0x51][0xEB][0x85][0x1F]
```

### String (Modified UTF-8)

```java
String value = "Hello";
// Length (2 bytes): [0x00][0x05]
// UTF-8 bytes: [0x48][0x65][0x6C][0x6C][0x6F]
```

## 10. Syntax

### 10.1 Basic Data Stream Operations

```java
// Writing primitive types
try (DataOutputStream dos = new DataOutputStream(
        new BufferedOutputStream(
            new FileOutputStream("data.bin")))) {
    dos.writeInt(42);
    dos.writeLong(123456789L);
    dos.writeDouble(3.14159);
    dos.writeBoolean(true);
    dos.writeUTF("Hello, World!");
}

// Reading primitive types
try (DataInputStream dis = new DataInputStream(
        new BufferedInputStream(
            new FileInputStream("data.bin")))) {
    int intValue = dis.readInt();
    long longValue = dis.readLong();
    double doubleValue = dis.readDouble();
    boolean boolValue = dis.readBoolean();
    String stringValue = dis.readUTF();
}
```

### 10.2 RandomAccessFile Operations

```java
// Random access read/write
try (RandomAccessFile raf = new RandomAccessFile("data.dat", "rw")) {
    // Write data
    raf.writeInt(42);
    raf.writeDouble(3.14);

    // Seek to position
    raf.seek(0);

    // Read data
    int value = raf.readInt();
    double pi = raf.readDouble();

    // Get file pointer position
    long position = raf.getFilePointer();

    // Get file length
    long length = raf.length();

    // Set file length
    raf.setLength(1024);
}
```

### 10.3 Structured Record Operations

```java
// Write a record
void writeRecord(DataOutputStream dos, int id, String name,
        double balance) throws IOException {
    dos.writeInt(id);
    dos.writeUTF(name);
    dos.writeDouble(balance);
}

// Read a record
Record readRecord(DataInputStream dis) throws IOException {
    int id = dis.readInt();
    String name = dis.readUTF();
    double balance = dis.readDouble();
    return new Record(id, name, balance);
}
```

## 11. Easy Example

```java
import java.io.*;

public class DataStreamBasic {

    public static void main(String[] args) {
        String filename = "test-data.bin";

        try {
            // Write data
            try (DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(
                        new FileOutputStream(filename)))) {
                dos.writeInt(42);
                dos.writeDouble(3.14159);
                dos.writeBoolean(true);
                dos.writeUTF("Hello, Data Streams!");
            }

            // Read data
            try (DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(
                        new FileInputStream(filename)))) {
                int intValue = dis.readInt();
                double doubleValue = dis.readDouble();
                boolean boolValue = dis.readBoolean();
                String stringValue = dis.readUTF();

                System.out.printf("int: %d%n", intValue);
                System.out.printf("double: %.5f%n", doubleValue);
                System.out.printf("boolean: %b%n", boolValue);
                System.out.printf("String: %s%n", stringValue);
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
import java.util.*;

public class RecordManager {

    private final String filename;

    public RecordManager(String filename) {
        this.filename = filename;
    }

    /**
     * Writes a list of records to file.
     */
    public void writeRecords(List<Record> records) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream(filename)))) {
            dos.writeInt(records.size());
            for (Record record : records) {
                dos.writeInt(record.id);
                dos.writeUTF(record.name);
                dos.writeDouble(record.balance);
                dos.writeBoolean(record.active);
            }
        }
    }

    /**
     * Reads all records from file.
     */
    public List<Record> readRecords() throws IOException {
        List<Record> records = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(
                    new FileInputStream(filename)))) {
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                int id = dis.readInt();
                String name = dis.readUTF();
                double balance = dis.readDouble();
                boolean active = dis.readBoolean();
                records.add(new Record(id, name, balance, active));
            }
        }

        return records;
    }

    /**
     * Updates a specific record by ID.
     */
    public boolean updateRecord(int id, Record newRecord)
            throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "rw")) {
            int count = raf.readInt();

            for (int i = 0; i < count; i++) {
                long startPos = raf.getFilePointer();
                int recordId = raf.readInt();

                if (recordId == id) {
                    raf.seek(startPos);
                    raf.writeInt(newRecord.id);
                    raf.writeUTF(newRecord.name);
                    raf.writeDouble(newRecord.balance);
                    raf.writeBoolean(newRecord.active);
                    return true;
                } else {
                    // Skip this record
                    raf.readUTF();
                    raf.readDouble();
                    raf.readBoolean();
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        RecordManager manager = new RecordManager("records.dat");

        try {
            // Create records
            List<Record> records = List.of(
                new Record(1, "Alice", 1000.00, true),
                new Record(2, "Bob", 2500.50, true),
                new Record(3, "Charlie", 500.75, false),
                new Record(4, "Diana", 3200.00, true)
            );

            // Write records
            manager.writeRecords(records);
            System.out.println("Written " + records.size() + " records");

            // Read records
            List<Record> readRecords = manager.readRecords();
            System.out.println("\nRead records:");
            readRecords.forEach(r ->
                System.out.printf("  %d: %s - $%.2f (%s)%n",
                    r.id, r.name, r.balance,
                    r.active ? "Active" : "Inactive"));

            // Update record
            Record updated = new Record(2, "Bob Smith", 3000.00, true);
            manager.updateRecord(2, updated);
            System.out.println("\nUpdated record 2");

            // Read again
            readRecords = manager.readRecords();
            System.out.println("\nAfter update:");
            readRecords.forEach(r ->
                System.out.printf("  %d: %s - $%.2f%n",
                    r.id, r.name, r.balance));

            // Cleanup
            new File("records.dat").delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static class Record {
        final int id;
        final String name;
        final double balance;
        final boolean active;

        Record(int id, String name, double balance, boolean active) {
            this.id = id;
            this.name = name;
            this.balance = balance;
            this.active = active;
        }
    }
}
```

## 13. Hard Example

```java
import java.io.*;
import java.util.concurrent.atomic.*;

public class BinaryFileDatabase {

    private final String filename;
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final AtomicLong fileSize = new AtomicLong(0);

    private static final int RECORD_SIZE = 64; // Fixed-size records

    public BinaryFileDatabase(String filename) throws IOException {
        this.filename = filename;
        File file = new File(filename);
        if (file.exists()) {
            fileSize.set(file.length());
            // Find next ID
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                while (raf.getFilePointer() < raf.length()) {
                    raf.skipBytes(RECORD_SIZE);
                    nextId.incrementAndGet();
                }
            }
        }
    }

    /**
     * Inserts a fixed-size record.
     */
    public synchronized int insert(String name, double value)
            throws IOException {

        int id = nextId.getAndIncrement();

        try (RandomAccessFile raf = new RandomAccessFile(filename, "rw")) {
            raf.seek(fileSize.get());

            // Write fixed-size record
            byte[] nameBytes = new byte[48]; // 48 bytes for name
            byte[] srcBytes = name.getBytes();
            System.arraycopy(srcBytes, 0, nameBytes, 0,
                Math.min(srcBytes.length, 48));

            raf.write(nameBytes);
            raf.writeDouble(value);
            raf.writeInt(id);

            fileSize.addAndGet(RECORD_SIZE);
        }

        return id;
    }

    /**
     * Reads a record by ID.
     */
    public synchronized String[] read(int id) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
            long pos = (long) (id - 1) * RECORD_SIZE;
            if (pos >= raf.length()) {
                return null;
            }

            raf.seek(pos);

            byte[] nameBytes = new byte[48];
            raf.readFully(nameBytes);
            String name = new String(nameBytes).trim();
            double value = raf.readDouble();
            int recordId = raf.readInt();

            return new String[]{name, String.valueOf(value),
                String.valueOf(recordId)};
        }
    }

    /**
     * Lists all records.
     */
    public synchronized void listAll() throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                byte[] nameBytes = new byte[48];
                raf.readFully(nameBytes);
                String name = new String(nameBytes).trim();
                double value = raf.readDouble();
                int id = raf.readInt();

                System.out.printf("  ID: %d, Name: %-20s, Value: %.2f%n",
                    id, name, value);
            }
        }
    }

    public static void main(String[] args) {
        try {
            BinaryFileDatabase db =
                new BinaryFileDatabase("binary-db.dat");

            // Insert records
            System.out.println("Inserting records:");
            int id1 = db.insert("Alice", 100.50);
            int id2 = db.insert("Bob", 200.75);
            int id3 = db.insert("Charlie", 300.00);
            System.out.printf("  Inserted: %d, %d, %d%n", id1, id2, id3);

            // Read record
            System.out.println("\nReading record 2:");
            String[] record = db.read(2);
            if (record != null) {
                System.out.printf("  Name: %s, Value: %s%n",
                    record[0], record[1]);
            }

            // List all
            System.out.println("\nAll records:");
            db.listAll();

            // Cleanup
            new File("binary-db.dat").delete();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 14. Performance

### Data Stream Performance

| Operation | DataInputStream | BufferedReader | Improvement |
|-----------|-----------------|----------------|-------------|
| Read int | 0.001ms | 0.1ms | 100x faster |
| Read String | 0.01ms | 0.05ms | 5x faster |
| Read double | 0.001ms | 0.1ms | 100x faster |

### RandomAccessFile Performance

| Operation | Sequential | Random (1000 records) |
|-----------|------------|----------------------|
| Read | 1ms | 5ms |
| Write | 2ms | 8ms |
| Update | N/A | 10ms |

### Performance Tips

1. **Use BufferedInputStream/BufferedOutputStream** with data streams
2. **Use fixed-size records** for random access
3. **Batch operations** when possible
4. **Use memory-mapped files** for very large files
5. **Cache frequently accessed records**

## 15. Best Practices

1. **Always use try-with-resources** for data streams
2. **Use BufferedInputStream/BufferedOutputStream** for performance
3. **Document your binary format** clearly
4. **Use fixed-size records** for random access
5. **Handle endianness** explicitly if cross-platform
6. **Validate data** when reading
7. **Use version numbers** in file formats for evolution

## 16. Common Mistakes

1. **Not using buffering** → Poor performance
2. **Wrong read/write order** → Data corruption
3. **Not checking EOF** → Incorrect data
4. **Mixing read/write modes** → Confusion
5. **Fixed-size assumptions** → File corruption
6. **Ignoring endianness** → Cross-platform issues
7. **Not closing resources** → File handle leaks

## 17. Pitfalls

1. **Big-endian default** → May not match native format
2. **Modified UTF-8** → Different from standard UTF-8
3. **No schema validation** → Data corruption possible
4. **File locking** → Concurrent access issues
5. **Memory-mapped limits** → Cannot exceed file size
6. **Platform differences** → Byte order, alignment

## 18. Debugging Tips

1. **Print hex dump** of binary files
2. **Validate record structure** when reading
3. **Use version numbers** in file format
4. **Log read/write operations**
5. **Test with different platforms**
6. **Use checksums** for data integrity

## 19. Comparison Table

| Feature | DataInputStream | BufferedReader | RandomAccessFile |
|---------|-----------------|----------------|------------------|
| Data types | Primitives + String | Text only | Primitives + String |
| Random access | No | No | Yes |
| Write capability | Via DataOutputStream | Via BufferedWriter | Yes |
| Performance | High | High | Medium |
| Use case | Binary files | Text files | Database-like |

## 20. Decision Tree

```
Need to read/write structured data?
├── Just text? → Use BufferedReader/BufferedWriter
├── Need primitives? → Use DataInputStream/DataOutputStream
├── Need random access? → Use RandomAccessFile
├── Need cross-platform? → Document endianness
├── Large file? → Use memory-mapped files
└── Database-like? → Use RandomAccessFile with fixed records
```

## 21. Interview Questions

### Q1: What is the difference between DataInputStream and BufferedReader?
**Answer:** `DataInputStream` reads primitive types (int, double, boolean) and strings in binary format. `BufferedReader` reads text data line-by-line or character-by-character. Data streams are more efficient for structured data.

### Q2: What is the default endianness of Java data streams?
**Answer:** Java data streams use big-endian (most significant byte first) by default. This is the network byte order and ensures cross-platform compatibility.

### Q3: What is the difference between writeUTF and writeBytes?
**Answer:** `writeUTF` writes a string with a 2-byte length prefix followed by modified UTF-8 bytes. `writeBytes` writes the raw bytes without length prefix. `writeUTF` is safer because you know the length when reading.

### Q4: How does RandomAccessFile differ from FileChannel?
**Answer:** `RandomAccessFile` provides a simpler API for random access with read/write methods. `FileChannel` provides more advanced features like memory-mapped files, file locking, and atomic operations, but requires buffers.

### Q5: Why use fixed-size records with RandomAccessFile?
**Answer:** Fixed-size records allow direct calculation of record position: `position = (id - 1) * recordSize`. This enables O(1) access time without scanning the file.

## 22. Exercises

### Exercise 1: Student Record System
Create a student record system using DataInputStream/DataOutputStream that stores student ID, name, GPA, and enrollment status.

### Exercise 2: Binary Log File
Implement a binary log file that stores timestamp, log level, and message using data streams.

### Exercise 3: Random Access Inventory
Build an inventory system using RandomAccessFile that supports add, update, delete, and search operations.

### Exercise 4: Cross-Endian Converter
Write a program that converts binary files between big-endian and little-endian formats.

## 23. Assignments

### Assignment 1: Simple Database
Create a simple database using RandomAccessFile that supports:
1. Insert records
2. Read by ID
3. Update records
4. Delete records (mark as deleted)
5. List all records

### Assignment 2: Binary File Format
Design and implement a binary file format for storing a contact list with:
1. Header with version and record count
2. Fixed-size records
3. Index file for fast lookup

## 24. Mini Project

**Binary File Database**

Create a binary file database that:
1. Stores records in fixed-size binary format
2. Supports CRUD operations
3. Uses RandomAccessFile for random access
4. Implements record locking for concurrent access
5. Provides query capabilities
6. Generates reports

Requirements:
- Use data streams for binary I/O
- Implement proper error handling
- Add logging
- Support concurrent access

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| DataInputStream | Read primitives in binary format |
| DataOutputStream | Write primitives in binary format |
| RandomAccessFile | Random access with read/write |
| Big-endian | Default byte order (most significant first) |
| Fixed records | Enable direct position calculation |
| Buffered I/O | Always use for performance |

## 26. References

1. **Official Documentation**: [Data Streams](https://docs.oracle.com/en/java/javase/21/essential/io/datastreams.html)
2. **Baeldung**: [Java DataInputStream](https://www.baeldung.com/java-data-input-stream)
3. **Books**:
   - "Java I/O" by Elliotte Rusty Harold
   - "File Structures" by Michael J. Folk
4. **Related Topics**:
   - [03 - Byte Streams](../03-byte-streams/README.md)
   - [07 - Object Streams](../07-object-streams/README.md)
   - [12 - Serialization](../12-serialization/README.md)

---

**Next Topic**: [07 - Object Streams](../07-object-streams/README.md)
