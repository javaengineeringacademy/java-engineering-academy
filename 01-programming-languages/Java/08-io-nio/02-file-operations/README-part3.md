# 02 - File Operations in Java IO (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

### Performance Tips

1. **Use NIO.2 Files API** over java.io.File for better performance
2. **Buffer reads/writes** for large files
3. **Use parallel streams** for bulk file operations
4. **Avoid scanning directories** - use WatchService for monitoring
5. **Cache file attributes** if accessed frequently
6. **Use memory-mapped files** for random access patterns
7. **Minimize file opens/closes** - reuse streams when possible

## 15. Best Practices

1. **Always use try-with-resources** for streams
2. **Use NIO.2 Path/Files API** over java.io.File
3. **Specify charset explicitly** when reading/writing text
4. **Handle InterruptedException properly** in concurrent operations
5. **Use StandardCopyOption.REPLACE_EXISTING** explicitly when needed
6. **Validate file paths** before operations
7. **Use Files.exists()** before operations that require files to exist
8. **Prefer Files.readString/readAllLines** for simple reads
9. **Use Stream API** for memory-efficient directory traversal
10. **Log file operations** for audit trails

## 16. Common Mistakes

1. **Not closing file handles** → Resource leaks
2. **Hardcoding paths** → Platform dependency
3. **Ignoring charset** → Encoding issues
4. **Not checking return values** → Silent failures
5. **Using File.delete()** → No exception on failure
6. **Mixing File and Path** → Inconsistent behavior
7. **Not handling symbolic links** → Unexpected behavior
8. **Buffering already buffered streams** → Wasted resources

## 17. Pitfalls

1. **File path separators** → Use `File.separator` or Path API
2. **Relative vs absolute paths** → Always clarify
3. **File locking conflicts** → Understand lock semantics
4. **Memory-mapped file limits** → Cannot exceed file size
5. **POSIX permissions** → Not available on all platforms
6. **Concurrent file modifications** → Use proper synchronization
7. **Temporary file cleanup** → Use deleteOnExit or scheduled cleanup

## 18. Debugging Tips

1. **Use `Files.exists()`** before operations
2. **Print absolute paths** when debugging
3. **Check file permissions** with `Files.getPosixFilePermissions()`
4. **Use `lsof`** on Unix to check open file descriptors
5. **Monitor with JFR** for file IO events
6. **Enable Java IO logging**: `-Djava.io.debug=true`
7. **Use `strace`** for system-level file operations

## 19. Comparison Table

| Feature | java.io.File | java.nio.file.Path | java.nio.file.Files |
|---------|--------------|--------------------|--------------------|
| Path representation | ✓ | ✓ | - |
| File operations | Limited | - | ✓ |
| Attribute access | Limited | - | ✓ |
| Exception handling | Poor | - | Good |
| File watching | - | - | ✓ |
| Symbolic links | Limited | ✓ | ✓ |
| File locking | - | - | ✓ |
| Stream API support | - | - | ✓ |
| Recommended | No | Yes | Yes |

## 20. Decision Tree

```
Need to work with files?
├── Just need path manipulation? → Use Path
├── Need to read/write content? → Use Files utility
├── Need file attributes? → Use Files + BasicFileAttributes
├── Need file watching? → Use WatchService
├── Need file locking? → Use FileChannel + FileLock
├── Need random access? → Use RandomAccessFile
└── Need memory-mapped files? → Use FileChannel.map()
```

## 21. Interview Questions

### Q1: What is the difference between java.io.File and java.nio.file.Path?
**Answer:** `File` is a class representing file/directory paths with limited functionality and poor error handling (returns boolean). `Path` is an interface with richer functionality, better error handling (throws exceptions), and supports features like symbolic links and file watching.

### Q2: How do you list all files in a directory recursively?
**Answer:** Use `Files.walk()` or `Files.walkFileTree()`. `Files.walk()` returns a Stream for functional-style processing. `Files.walkFileTree()` uses the Visitor pattern for more control.

### Q3: What is the difference between mkdir() and mkdirs()?
**Answer:** `mkdir()` creates a single directory and returns false if parent directories don't exist. `mkdirs()` creates the directory and all necessary parent directories.

### Q4: How do you handle file permissions in Java?
**Answer:** Use `Files.getPosixFilePermissions()` and `Files.setPosixFilePermissions()` for POSIX systems. Use `Files.setAttribute()` for platform-specific attributes.

### Q5: What is the best way to read a large file?
**Answer:** Use `Files.lines()` for streaming line-by-line without loading entire file into memory. Use `BufferedReader` for buffered reading. Avoid `Files.readAllLines()` for large files.

## 22. Exercises

### Exercise 1: File Backup Tool
Create a program that backs up files from a source directory to a destination directory, preserving the directory structure and file attributes.

### Exercise 2: File Search Utility
Implement a file search utility that finds files by:
- Name pattern (glob)
- Size range
- Date range
- Content search (grep)

### Exercise 3: File Organizer
Write a program that organizes files in a directory by:
- File type (extensions)
- Creation date
- Size

### Exercise 4: Directory Statistics
Create a utility that provides statistics about a directory:
- Total files and directories
- Total size
- Average file size
- Most common file types

## 23. Assignments

### Assignment 1: File Synchronization Tool
Create a file synchronization tool that:
1. Compares two directories
2. Identifies new, modified, and deleted files
3. Synchronizes changes (one-way or two-way)
4. Handles conflicts

### Assignment 2: File Encryption Tool
Implement a file encryption/decryption tool using:
- AES encryption for file content
- Secure file deletion
- Checksum verification
- Progress reporting

## 24. Mini Project

**File Management System**

Create a detailed file management system that:
1. Provides CLI interface for file operations
2. Supports CRUD operations on files and directories
3. Implements file search with multiple criteria
4. Generates file system reports
5. Supports file compression (zip)
6. Implements file versioning

Requirements:
- Use NIO.2 API
- Implement proper error handling
- Add logging
- Support concurrent operations

## 25. Summary

| Concept | Key Point |
|---------|-----------|
| java.io.File | Legacy API, limited functionality |
| java.nio.file.Path | Modern path representation |
| java.nio.file.Files | Utility class for file operations |
| Try-with-resources | Ensures proper resource cleanup |
| Stream API | Memory-efficient file traversal |
| File attributes | Metadata access via BasicFileAttributes |
| File locking | Concurrent access control |

## 26. References

1. **Official Documentation**: [Java NIO.2 File API](https://docs.oracle.com/en/java/javase/21/essential/io/fileio.html)
2. **Baeldung**: [Java NIO File](https://www.baeldung.com/java-nio-file)
3. **Books**:
   - "Java I/O, NIO and NIO.2" by Joseph Dallmeier
   - "Java 7 Recipes" by Josh Juneau
4. **Related Topics**:
   - [01 - Introduction](../01-introduction/README.md)
   - [08 - NIO Basics](../../../../README.md)
   - [11 - NIO File System](../../../../README.md)

---

**Next Topic**: [03 - Byte Streams](../03-byte-streams/README.md)
