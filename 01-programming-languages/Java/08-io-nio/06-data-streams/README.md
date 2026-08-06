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

---

[📖 Continue to Part 2](README-part2.md)
