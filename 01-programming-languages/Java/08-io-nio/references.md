# I/O and NIO - References

## Official Documentation
- [Java Tutorials: I/O](https://docs.oracle.com/javase/tutorial/essential/io/index.html)
- [Java Tutorials: NIO](https://docs.oracle.com/javase/tutorial/essential/io/nio.html)
- [Java API: java.io Package](https://docs.oracle.com/javase/8/docs/api/java/io/package-summary.html)
- [Java API: java.nio Package](https://docs.oracle.com/javase/8/docs/api/java/nio/package-summary.html)
- [Java API: java.nio.file Package](https://docs.oracle.com/javase/8/docs/api/java/nio/file/package-summary.html)

## Books
- *Java NIO* (John O'Hanlon) - Comprehensive NIO reference
- *Effective Java* (Joshua Bloch) - Item on try-with-resources and I/O

## I/O Class Hierarchy

```
InputStream
├── FileInputStream
├── ByteArrayInputStream
├── BufferedInputStream
├── DataInputStream
└── ObjectInputStream

OutputStream
├── FileOutputStream
├── ByteArrayOutputStream
├── BufferedOutputStream
├── DataOutputStream
└── ObjectOutputStream

Reader
├── FileReader
├── BufferedReader
├── InputStreamReader
└── StringReader

Writer
├── FileWriter
├── BufferedWriter
├── OutputStreamWriter
└── PrintWriter
```

## NIO Key Classes

| Class | Purpose |
|-------|---------|
| `Path` | File system path representation |
| `Files` | Static utility methods for file operations |
| `FileChannel` | Channel for file reading/writing |
| `ByteBuffer` | Buffer for byte data |
| `CharBuffer` | Buffer for character data |
| `DirectoryStream` | Iterating over directory entries |
| `WatchService` | File system change notification |
| `AsynchronousFileChannel` | Async file operations |
| `FileLock` | File locking mechanism |

## Files Utility Methods
| Method | Description |
|--------|-------------|
| `Files.readString()` | Read entire file as String (Java 11+) |
| `Files.readString()` | Write String to file (Java 11+) |
| `Files.readAllLines()` | Read all lines as List |
| `Files.lines()` | Stream of lines (lazy) |
| `Files.list()` | Stream of directory entries |
| `Files.walk()` | Stream walking file tree |
| `Files.find()` | Stream of files matching predicate |
| `Files.copy()` | Copy file |
| `Files.move()` | Move/rename file |
| `Files.delete()` | Delete file |
| `Files.createDirectories()` | Create directory tree |
| `Files.setAttribute()` | Set file metadata |

## When to Use What
| Scenario | Recommendation |
|----------|----------------|
| Small text file | `Files.readString()` |
| Large text file | `Files.lines()` with streaming |
| Line-by-line processing | `BufferedReader` or `Files.lines()` |
| Binary file reading | `Files.readAllBytes()` or `FileChannel` |
| Random access | `RandomAccessFile` or `FileChannel` |
| Large file processing | Memory-mapped `FileChannel.map()` |
| Network non-blocking | `SocketChannel` with `Selector` |
| Async file I/O | `AsynchronousFileChannel` |
| Watch directory | `WatchService` |

## Related Topics
- [java.nio.file Package](https://docs.oracle.com/javase/8/docs/api/java/nio/file/package-summary.html)
- [Files and Directories Tutorial](https://docs.oracle.com/javase/tutorial/essential/io/fileio.html)
- [Channels and Selectors](https://docs.oracle.com/javase/tutorial/essential/io/channels.html)
