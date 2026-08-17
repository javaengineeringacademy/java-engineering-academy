# I/O and NIO - Decision Guide

## File I/O Strategy

### When to Use Traditional I/O (java.io)
- Simple file read/write operations
- Processing data line-by-line with BufferedReader
- Working with existing APIs that require InputStream/OutputStream
- Text-based file processing

### When to Use NIO (java.nio)
- High-performance file operations (channels, buffers)
- Asynchronous I/O with AsynchronousFileChannel
- Memory-mapped files for large files
- Non-blocking network I/O (SocketChannel)
- File system metadata operations (Files class)
- Watching file system changes (WatchService)

## File Operations

### Reading Files
| Method | Use Case | Buffering |
|--------|----------|-----------|
| `Files.readString()` | Small files (Java 11+) | None needed |
| `Files.readAllLines()` | Line-by-line into List | Internal |
| `Files.lines()` | Streaming lines (lazy) | Internal |
| `Files.newBufferedReader()` | Manual buffering | Explicit |
| `Files.newInputStream()` | Raw byte reading | Manual |

### Writing Files
| Method | Use Case | Append |
|--------|----------|--------|
| `Files.writeString()` | Small files (Java 11+) | No |
| `Files.write()` | Write byte/list of lines | Yes option |
| `Files.newBufferedWriter()` | Streaming writes | Yes option |
| `Files.newOutputStream()` | Raw byte writing | Yes option |

## Streams API vs Traditional I/O
- **Streams API** (`Files.lines()`) - Lazy, composable, memory-efficient for large files
- **BufferedReader** - Good for line-by-line with explicit control
- **Scanner** - Good for parsing structured input

## NIO Channels and Buffers
- **FileChannel** - Random access, memory-mapped files, locking
- **SocketChannel** - Non-blocking TCP connections
- **ByteBuffer** - Core buffer for all channel operations
- **DirectByteBuffer** - Native memory, faster for large I/O but expensive to allocate

## When to Use Asynchronous I/O
- High-latency file operations
- Network servers handling many connections
- When you need callback-based or Future-based processing
- Use `AsynchronousFileChannel` or `AsynchronousSocketChannel`

## Common Pitfalls
- Always close resources (use try-with-resources)
- Don't mix text and binary streams carelessly
- Specify charset explicitly (never use default)
- Don't buffer if already buffered
- Use `Files.copy()`/`Files.move()` instead of manual stream copying
- Be aware of platform line separators; use `System.lineSeparator()`
