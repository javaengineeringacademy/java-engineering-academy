# Quiz: NIO Channels (Continued)

## Multiple Choice Questions

1. What is memory-mapped file I/O?
   - A) File mapped to memory
   - B) Memory mapped to file
   - C) File in memory
   - D) Memory in file

2. Which method creates memory-mapped buffer?
   - A) `map()`
   - B) `mmap()`
   - C) `mapFile()`
   - D) `memoryMap()`

3. What is MappedByteBuffer?
   - A) Direct buffer for memory-mapped file
   - B) Heap buffer for memory-mapped file
   - C) Char buffer for memory-mapped file
   - D) Int buffer for memory-mapped file

4. What is the advantage of memory-mapped files?
   - A) Faster I/O
   - B) Less memory
   - C) Simpler code
   - D) Better security

5. When should you use memory-mapped files?
   - A) Small files
   - B) Large files
   - C) Text files
   - D) Binary files

## True/False Questions

6. Memory-mapped files use direct buffers.
   - True / False

7. Memory-mapped files are always faster.
   - True / False

8. Memory-mapped files can be shared between processes.
   - True / False

## Code Output Questions

9. What will this code print?
```java
try (FileChannel channel = FileChannel.open(Path.of("/tmp/test.txt"),
        StandardOpenOption.READ, StandardOpenOption.WRITE)) {
    MappedByteBuffer buffer = channel.map(
        FileChannel.MapMode.READ_WRITE, 0, channel.size());
    System.out.println(buffer.isDirect());
}
```

10. What will this code print?
```java
ByteBuffer buf = ByteBuffer.allocateDirect(1024);
System.out.println(buf.isDirect());
```

## Answers

1. A - Memory-mapped file is file mapped to memory
2. A - map() creates memory-mapped buffer
3. A - MappedByteBuffer is direct buffer
4. A - Memory-mapped files are faster for I/O
5. B - Use for large files
6. True - Memory-mapped files use direct buffers
7. False - Depends on access pattern
8. True - Can be shared between processes
9. Output:
```
true
```
10. Output:
```
true
```
