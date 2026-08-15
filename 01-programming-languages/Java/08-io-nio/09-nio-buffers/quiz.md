# Quiz: NIO Buffers

## Multiple Choice Questions

1. What is a Buffer in NIO?
   - A) Container for data
   - B) Data stream
   - C) Channel
   - D) Selector

2. Which class is for byte buffers?
   - A) ByteBuffer
   - B) CharBuffer
   - C) IntBuffer
   - D) LongBuffer

3. What does flip() do to a buffer?
   - A) Prepares for reading
   - B) Prepares for writing
   - C) Clears the buffer
   - D) Doubles capacity

4. What is buffer capacity?
   - A) Maximum size
   - B) Current position
   - C) Remaining data
   - D) Data limit

5. Which method writes data to buffer?
   - A) put()
   - B) get()
   - C) flip()
   - D) clear()

## True/False Questions

6. Buffers are mutable objects.
   - True / False

7. ByteBuffer is the most commonly used buffer type.
   - True / False

8. clear() actually erases buffer data.
   - True / False

## Code Output Questions

9. What will this code print?
```java
ByteBuffer buf = ByteBuffer.allocate(10);
buf.put((byte)1);
buf.put((byte)2);
buf.flip();
System.out.println(buf.get());
```

10. What will this code print?
```java
ByteBuffer buf = ByteBuffer.allocate(5);
System.out.println(buf.capacity());
System.out.println(buf.position());
```

## Answers

1. A - Buffer is a container for data
2. A - ByteBuffer is for byte data
3. A - flip() prepares buffer for reading
4. A - Capacity is maximum buffer size
5. A - put() writes data to buffer
6. True - Buffers are mutable
7. True - ByteBuffer is most common
8. False - clear() doesn't erase data, just resets position
9. Output:
```
1
```
10. Output:
```
5
0
```
