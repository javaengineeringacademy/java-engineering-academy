# java.nio — Non-Blocking I/O

The `java.nio` module provides buffers, channels, charset support, and file system APIs for high-performance I/O.

## Buffers

A buffer is a container for data of a specific primitive type:

```java
ByteBuffer buf = ByteBuffer.allocate(1024);
buf.put((byte) 42);
buf.flip();          // Switch from write to read mode
byte b = buf.get();  // Read the byte
buf.clear();         // Reset for writing
```

### Buffer Types

| Buffer | Primitive Type |
|--------|---------------|
| `ByteBuffer` | byte |
| `CharBuffer` | char |
| `ShortBuffer` | short |
| `IntBuffer` | int |
| `LongBuffer` | long |
| `FloatBuffer` | float |
| `DoubleBuffer` | double |

### Buffer Operations

```
Allocate → Put → Flip → Get → Clear/Compact
                          ↑
                    Read position
```

- **allocate()**: Create buffer with capacity
- **put()**: Write data
- **flip()**: Switch to read mode (limit = position, position = 0)
- **get()**: Read data
- **clear()**: Reset for writing
- **compact()**: Keep unread data, reset position for writing

### Direct Buffers

```java
ByteBuffer direct = ByteBuffer.allocateDirect(4096);
// Allocated in native memory, not on Java heap
// Avoids copying during I/O operations
```

Direct buffers are allocated outside the heap. They are faster for I/O but more expensive to allocate and are not subject to GC in the normal way.

## Channels

Channels represent connections to I/O operations:

### FileChannel

```java
FileChannel channel = FileChannel.open(
    Path.of("data.bin"),
    StandardOpenOption.READ,
    StandardOpenOption.WRITE
);

ByteBuffer buf = ByteBuffer.allocate(1024);
int bytesRead = channel.read(buf);
channel.write(buf, 0);
channel.close();
```

### Socket Channels (Non-blocking)

```java
SocketChannel sc = SocketChannel.open();
sc.configureBlocking(false);
sc.connect(new InetSocketAddress("example.com", 80));

// Poll for readiness
while (!sc.finishConnect()) {
    // Do other work
}
```

### Server Socket Channel

```java
ServerSocketChannel ssc = ServerSocketChannel.open();
ssc.bind(new InetSocketAddress(8080));
ssc.configureBlocking(false);

while (true) {
    SocketChannel client = ssc.accept(); // Non-blocking
    if (client != null) {
        handleClient(client);
    }
}
```

### Selector (Multiplexing)

```java
Selector selector = Selector.open();
ServerSocketChannel ssc = ServerSocketChannel.open();
ssc.bind(new InetSocketAddress(8080));
ssc.configureBlocking(false);
ssc.register(selector, SelectionKey.OP_ACCEPT);

while (true) {
    selector.select(); // Block until events
    Set<SelectionKey> keys = selector.selectedKeys();
    for (SelectionKey key : keys) {
        if (key.isAcceptable()) { /* accept */ }
        if (key.isReadable())   { /* read */ }
        if (key.isWritable())   { /* write */ }
    }
}
```

## Charset

```java
Charset utf8 = StandardCharsets.UTF_8;
CharsetEncoder encoder = utf8.newEncoder();
CharsetDecoder decoder = utf8.newDecoder();

ByteBuffer encoded = encoder.encode(CharBuffer.wrap("Hello"));
CharBuffer decoded = decoder.decode(ByteBuffer.wrap(encoded.array()));
```

## File System (java.nio.file)

```java
Path path = Path.of("/tmp/data.txt");

// Read
String content = Files.readString(path);
List<String> lines = Files.readAllLines(path);

// Write
Files.writeString(path, "Hello, world!");
Files.write(path, List.of("line1", "line2"));

// Directory operations
Files.createDirectory(Path.of("/tmp/newdir"));
Files.list(Path.of("/tmp")).forEach(System.out::println);
Files.walk(Path.of("/tmp")).forEach(System.out::println);

// File attributes
long size = Files.size(path);
boolean exists = Files.exists(path);
FileTime lastModified = Files.getLastModifiedTime(path);
```

## Memory-Mapped Files

```java
FileChannel channel = FileChannel.open(Path.of("large.bin"), StandardOpenOption.READ);
MappedByteBuffer mapped = channel.map(
    FileChannel.MapMode.READ_ONLY, 0, channel.size());

// Access bytes directly from memory
byte b = mapped.get(1000);
```

## Key Source Files

| Path | Contents |
|------|----------|
| `src/java.base/share/classes/java/nio/` | Buffer, ByteBuffer, CharBuffer |
| `src/java.base/share/classes/java/nio/channels/` | Channels, Selectors |
| `src/java.base/share/classes/java/nio/file/` | Path, Files, FileSystem |
| `src/java.base/share/classes/java/nio/charset/` | Charset, Encoder, Decoder |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
