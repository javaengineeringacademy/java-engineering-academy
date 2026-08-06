# Java I/O and NIO Quiz

## Question 1
What is the primary difference between `InputStream` and `Reader`?
- A) `InputStream` is faster than `Reader`
- B) `InputStream` handles raw bytes, while `Reader` handles characters with encoding
- C) They are identical
- D) `Reader` only works with files

**Answer: B**
**Explanation:** `InputStream` and its subclasses deal with raw byte data. `Reader` and its subclasses handle character data, converting bytes to characters using a specified charset encoding.

## Question 2
What does try-with-resources do in Java?
- A) It creates resources automatically
- B) It automatically closes resources that implement `AutoCloseable` when the block exits
- C) It prevents exceptions from occurring
- D) It duplicates resources for parallel processing

**Answer: B**
**Explanation:** try-with-resources ensures that any resource implementing `AutoCloseable` (like streams, connections) is automatically closed after the try block, even if an exception occurs. This prevents resource leaks.

## Question 3
What is the key advantage of NIO Channels over traditional I/O Streams?
- A) Channels are simpler to use
- B) Channels support non-blocking I/O operations
- C) Channels are only for reading
- D) Channels don't need buffers

**Answer: B**
**Explanation:** NIO Channels can operate in non-blocking mode, allowing a single thread to manage multiple I/O operations. Traditional Streams are always blocking, requiring dedicated threads for each connection.

## Question 4
What is the correct order of buffer operations when reading from a buffer?
- A) clear → get → flip
- B) flip → get → clear
- C) put → flip → get
- D) clear → put → flip

**Answer: B**
**Explanation:** When reading: `flip()` switches the buffer from write mode to read mode (sets limit to position, position to 0), `get()` reads data, and `clear()` resets the buffer for writing again.

## Question 5
Which class is used to read a file as a Stream of lines in Java NIO.2?
- A) `FileReader`
- B) `BufferedReader`
- C) `Files.lines()`
- D) `FileChannel`

**Answer: C**
**Explanation:** `Files.lines(Path)` returns a `Stream<String>` of lines from a file, which is lazily loaded and can be processed using the Stream API. It's the modern approach introduced in Java 8.