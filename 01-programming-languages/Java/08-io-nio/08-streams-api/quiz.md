# Quiz: NIO Buffers

## Multiple Choice Questions

1. What is a Buffer in NIO?
   - A) Container for data
   - B) Stream wrapper
   - C) File handler
   - D) Network connector

2. What are the buffer attributes?
   - A) Capacity, position, limit
   - B) Size, length, count
   - C) Start, end, middle
   - D) Read, write, execute

3. What does `flip()` do?
   - A) Prepares for reading
   - B) Prepares for writing
   - C) Clears buffer
   - D) Reverses buffer

4. What does `clear()` do?
   - A) Prepares for writing
   - B) Prepares for reading
   - C) Deletes buffer
   - D) Resets buffer

5. What is the difference between heap and direct buffers?
   - A) Heap is faster
   - B) Direct is faster for I/O
   - C) No difference
   - D) Heap is larger

## True/False Questions

6. Buffer position starts at 0.
   - True / False

7. Buffer capacity cannot change.
   - True / False

8. Direct buffers are garbage collected.
   - True / False

## Code Output Questions

9. What will this code print?
```java
ByteBuffer buf = ByteBuffer.allocate(10);
System.out.println(buf.capacity());
System.out.println(buf.position());
System.out.println(buf.limit());
```

10. What will this code print?
```java
ByteBuffer buf = ByteBuffer.allocate(10);
buf.put((byte) 1);
buf.put((byte) 2);
buf.flip();
System.out.println(buf.get());
System.out.println(buf.position());
```

## Answers

1. A - Buffer is a container for data
2. A - Capacity, position, limit
3. A - flip() prepares for reading
4. A - clear() prepares for writing
5. B - Direct buffers are faster for I/O
6. True - Buffer position starts at 0
7. True - Buffer capacity cannot change
8. False - Direct buffers are not garbage collected
9. Output:
```
10
0
10
```
10. Output:
```
1
1
```
