# Quiz

## Multiple Choice Questions

1. What does I/O stand for?
   - A) Input/Output
   - B) Internal/External
   - C) Integer/Overflow
   - D) Index/Offset

2. Which stream is for character data?
   - A) InputStream
   - B) OutputStream
   - C) Reader
   - D) DataInputStream

3. What is the default buffer size?
   - A) 1024 bytes
   - B) 4096 bytes
   - C) 8192 bytes
   - D) 16384 bytes

4. What does NIO stand for?
   - A) New I/O
   - B) Network I/O
   - C) Non-blocking I/O
   - D) All of the above

5. Which method reads a line of text?
   - A) read()
   - B) readLine()
   - C) readUTF()
   - D) readObject()

## True/False Questions

6. Byte streams are for text data.
   - True / False

7. Buffered streams improve performance.
   - True / False

8. NIO uses buffers for data transfer.
   - True / False

## Code Output Questions

9. What will this code print?
```java
BufferedReader br = new BufferedReader(new StringReader("Hello\nWorld"));
System.out.println(br.readLine());
```

10. What will this code print?
```java
ByteBuffer buf = ByteBuffer.allocate(10);
buf.putInt(12345);
buf.flip();
System.out.println(buf.getInt());
```

## Answers

1. A
2. C
3. C
4. D
5. B
6. False
7. True
8. True
9. Hello
10. 12345
