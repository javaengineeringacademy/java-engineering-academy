# IO/NIO Exercises

Practice Java IO and NIO through hands-on exercises.

## Exercise 1: File Read/Write

**Problem Statement:**
Write a program that creates a text file, writes multiple lines to it using different approaches (BufferedWriter, FileWriter, and Files.write), then reads the file back line-by-line using BufferedReader and Files.readAllLines.

**Expected Behavior:**
- A new file `output.txt` is created with at least 10 lines of content.
- Each writing approach produces the same file content.
- Reading returns all lines in the correct order.
- File is properly closed even if an exception occurs during writing.
- The program handles `IOException` for missing files or permission issues.

**Hints:**
- Use `Files.write(Path, Iterable<String>)` for the simplest approach.
- Use try-with-resources to ensure streams are closed.
- Use `Files.readAllLines(Path)` to read all lines into a `List<String>`.

---

## Exercise 2: Buffered Streams

**Problem Statement:**
Compare file copy performance using three approaches: raw FileInputStream/FileOutputStream, buffered streams with 1KB buffer, and buffered streams with 8KB buffer. Copy a large file (generate a test file of 10MB) and measure the time for each approach.

**Expected Behavior:**
- The raw stream copy takes significantly longer than buffered copies.
- The 8KB buffer copy is faster than the 1KB buffer copy.
- The copied file's content matches the original byte-for-byte.
- Timing results are displayed in a formatted comparison table.
- All streams are properly closed in finally blocks or try-with-resources.

**Hints:**
- Generate a test file by writing repeated data to reach 10MB.
- Use `System.nanoTime()` for precise timing measurement.
- Use `BufferedInputStream` and `BufferedOutputStream` with specified buffer sizes.
- Verify file equality using `Files.mismatch()` or byte-by-byte comparison.

---

## Exercise 3: NIO Channels

**Problem Statement:**
Implement a file copy utility using `FileChannel`. Use both the traditional transferFrom/transferTo approach and the ByteBuffer-based approach. Compare the results and performance of both methods.

**Expected Behavior:**
- `transferTo` copies the file using OS-level optimization.
- `ByteBuffer` copy reads chunks into a buffer and writes them out.
- Both methods produce identical copies of the source file.
- The `transferTo` approach is generally faster for large files.
- Memory usage is monitored using Runtime.getRuntime().totalMemory().

**Hints:**
- Open source and destination FileChannel using `FileChannel.open()`.
- Use `ByteBuffer.allocateDirect()` for potentially better performance.
- Call `buffer.flip()` between writing to and reading from the buffer.
- Use `channel.size()` to determine total bytes to transfer.

---

## Exercise 4: Memory-Mapped Files

**Problem Statement:**
Write a program that uses memory-mapped files (`MappedByteBuffer`) to read and modify a file. Create a file with integer data, map it into memory, modify values, and verify the changes are persisted to disk.

**Expected Behavior:**
- A file with 1000 integers is created and memory-mapped.
- Integers at specific positions are modified through the mapped buffer.
- Changes are visible in the buffer immediately.
- After forcing the buffer to disk, changes persist after reopening.
- The program demonstrates random access read/write capabilities.

**Hints:**
- Use `FileChannel.map(MapMode.READ_WRITE, 0, size)` to create a mapped buffer.
- Use `buffer.putInt(offset, value)` and `buffer.getInt(offset)` for direct access.
- Call `buffer.force()` to ensure modifications are written to disk.
- Use `RandomAccessFile` to verify modifications outside the mapping.

---

## Exercise 5: Asynchronous IO

**Problem Statement:**
Implement an asynchronous file reader using `AsynchronousFileChannel`. Read a large file in chunks using completion handlers, processing each chunk as it arrives. Write results to an output file asynchronously.

**Expected Behavior:**
- The file is read in non-blocking chunks of 1024 bytes.
- Each chunk triggers a CompletionHandler callback upon completion.
- Processed chunks are written to an output file asynchronously.
- The program does not block the main thread during IO operations.
- A CountDownLatch or CompletableFuture signals completion.

**Hints:**
- Use `AsynchronousFileChannel.open()` with `StandardOpenOption.READ`.
- Implement `CompletionHandler<Integer, Attachment>` with custom attachment data.
- Use `attachment.buffer.clear()` and `channel.read()` in the completed() callback.
- Chain `AsynchronousFileChannel.open()` for the output with `StandardOpenOption.CREATE`.

---

## Exercise 6: File Watcher

**Problem Statement:**
Implement a file system watcher using `WatchService` that monitors a directory for file creation, deletion, and modification events. Log each event with a timestamp and the affected file name.

**Expected Behavior:**
- The watcher monitors the specified directory (and subdirectories if recursive).
- File creation events are logged with the new file name.
- File deletion events are logged with the deleted file name.
- File modification events are logged with the modified file name.
- The watcher runs continuously until stopped by the user.
- Events include timestamps formatted as HH:mm:ss.

**Hints:**
- Register the directory with `dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)`.
- Use `watcher.poll()` or `watcher.take()` to wait for events.
- Cast `WatchEvent<Path>` to get the file name via `event.context()`.
- Use `Key.reset()` to continue watching after processing an event.

---

## Exercise 7: NIO Path Operations

**Problem Statement:**
Write a utility that performs various path operations using `java.nio.file.Path`: resolve relative paths, normalize paths, convert between relative and absolute, list directory contents, and create temporary files/directories.

**Expected Behavior:**
- `Path.resolve()` correctly joins relative paths to a base path.
- `Path.normalize()` removes redundant elements like `.` and `..`.
- `Path.toAbsolutePath()` converts any path to an absolute path.
- Directory listing returns all entries sorted by name.
- Temporary files and directories are created and deleted on exit.

**Hints:**
- Use `Paths.get()` or `Path.of()` to create Path objects.
- Use `Files.list()` for lazy directory listing (returns `Stream<Path>`).
- Use `Files.createTempFile()` and `Files.createTempDirectory()`.
- Use a shutdown hook or try-with-resources for cleanup.

---

## Exercise 8: Serialization with NIO

**Problem Statement:**
Write a program that serializes a Java object to a file using `ObjectOutputStream` and deserializes it back using `ObjectInputStream`. Then rewrite the serialization to use NIO `ByteBuffer` for writing individual fields manually, comparing both approaches.

**Expected Behavior:**
- The object is serialized to a file and deserialized with identical field values.
- The manual NIO approach writes each field type-safely to a ByteBuffer.
- The manual approach reads fields back in the same order they were written.
- Both approaches handle the case where the file does not exist.
- The manual NIO approach demonstrates control over byte layout.

**Hints:**
- Implement `Serializable` on your data class and add a `serialVersionUID`.
- For manual NIO, use `ByteBuffer.putInt()`, `putDouble()`, `putUTF()` etc.
- Use `ByteBuffer.flip()` before reading back from a written buffer.
- Compare the file sizes of both approaches.
