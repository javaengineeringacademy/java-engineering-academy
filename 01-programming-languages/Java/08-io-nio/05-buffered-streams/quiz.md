# Quiz: Buffered Streams

## Multiple Choice Questions

1. What is the purpose of buffering?
   - A) Reduce I/O operations
   - B) Increase speed
   - C) Save memory
   - D) All of the above

2. Which class provides buffered input?
   - A) BufferedInputStream
   - B) BufferedOutputStream
   - C) BufferedReader
   - D) BufferedWriter

3. What is the default buffer size?
   - A) 1024
   - B) 2048
   - C) 4096
   - D) 8192

4. Which method flushes the buffer?
   - A) `flush()`
   - B) `clear()`
   - C) `reset()`
   - D) `empty()`

5. What happens when buffer is full?
   - A) Exception thrown
   - B) Buffer expands
   - C) Data written to disk
   - D) Data lost

## True/False Questions

6. Buffered streams are faster than unbuffered.
   - True / False

7. You should always flush after writing.
   - True / False

8. Buffered streams are thread-safe.
   - True / False

## Code Output Questions

9. What will this code print?
```java
BufferedReader br = new BufferedReader(new StringReader("Hello\nWorld"));
System.out.println(br.readLine());
System.out.println(br.readLine());
```

10. What will this code print?
```java
String s = "Hello";
System.out.println(s.length());
```

## Answers

1. D - Buffering reduces I/O, increases speed, saves memory
2. A - BufferedInputStream provides buffered input
3. D - Default buffer size is 8192 bytes
4. A - flush() writes buffer to disk
5. C - Data is written to disk when buffer is full
6. True - Buffered streams are faster
7. True - Always flush after writing
8. False - Buffered streams are not thread-safe
9. Output:
```
Hello
World
```
10. Output:
```
5
```
