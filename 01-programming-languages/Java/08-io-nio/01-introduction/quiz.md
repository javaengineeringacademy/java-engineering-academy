# Quiz: I/O Introduction

## Multiple Choice Questions

1. What does I/O stand for?
   - A) Input/Output
   - B) Integer/Overflow
   - C) Internal/External
   - D) Index/Offset

2. What are the two main types of streams?
   - A) Byte and Character
   - B) Input and Output
   - C) Read and Write
   - D) File and Network

3. What is the difference between I/O and NIO?
   - A) I/O is newer
   - B) NIO is buffer-based
   - C) I/O is non-blocking
   - D) NIO is older

4. Which class is for byte input?
   - A) InputStream
   - B) Reader
   - C) Writer
   - D) OutputStream

5. What is a stream?
   - A) Sequence of data
   - B) File
   - C) Directory
   - D) Connection

## True/False Questions

6. I/O operations can throw exceptions.
   - True / False

7. NIO stands for New I/O.
   - True / False

8. Streams are always sequential.
   - True / False

## Code Output Questions

9. What will this code print?
```java
InputStream is = new ByteArrayInputStream("Hello".getBytes());
System.out.println(is.read());
```

10. What will this code print?
```java
String s = "Hello World";
byte[] bytes = s.getBytes();
System.out.println(bytes.length);
```

## Answers

1. A - Input/Output
2. A - Byte and Character streams
3. B - NIO is buffer-based
4. A - InputStream is for byte input
5. A - Stream is a sequence of data
6. True - I/O operations throw IOException
7. True - NIO stands for New I/O
8. True - Streams are sequential
9. Output:
```
72
```
10. Output:
```
11
```
