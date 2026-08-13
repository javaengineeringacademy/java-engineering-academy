# Quiz: Byte Streams

## Multiple Choice Questions

1. What is a byte stream?
   - A) Sequence of bytes
   - B) Sequence of characters
   - C) Sequence of bits
   - D) Sequence of strings

2. Which class reads bytes?
   - A) InputStream
   - B) Reader
   - C) Writer
   - D) OutputStream

3. What is the default buffer size?
   - A) 1024
   - B) 4096
   - C) 8192
   - D) 16384

4. Which method reads a byte?
   - A) `read()`
   - B) `readByte()`
   - C) `get()`
   - D) `input()`

5. What does `available()` return?
   - A) File size
   - B) Bytes available
   - C) Buffer size
   - D) Stream length

## True/False Questions

6. Byte streams are for text data.
   - True / False

7. InputStream is abstract.
   - True / False

8. Byte streams can read binary files.
   - True / False

## Code Output Questions

9. What will this code print?
```java
byte[] data = {72, 101, 108, 108, 111};
InputStream is = new ByteArrayInputStream(data);
int b;
while ((b = is.read()) != -1) {
    System.out.print((char) b);
}
```

10. What will this code print?
```java
byte[] bytes = "Hello".getBytes();
System.out.println(bytes.length);
```

## Answers

1. A - Byte stream is a sequence of bytes
2. A - InputStream reads bytes
3. C - Default buffer size is 8192 bytes
4. A - read() reads a byte
5. B - available() returns bytes available
6. False - Byte streams are for binary data
7. True - InputStream is abstract
8. True - Byte streams can read binary files
9. Output:
```
Hello
```
10. Output:
```
5
```
