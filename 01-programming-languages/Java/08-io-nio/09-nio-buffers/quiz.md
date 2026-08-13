# Quiz: NIO Channels

## Multiple Choice Questions

1. What is a Channel in NIO?
   - A) Bidirectional data transfer
   - B) Unidirectional data transfer
   - C) Data buffer
   - D) Data stream

2. Which class is for file channels?
   - A) FileChannel
   - B) SocketChannel
   - C) ServerSocketChannel
   - D) DatagramChannel

3. What is a Selector used for?
   - A) Monitoring multiple channels
   - B) Reading data
   - C) Writing data
   - D) Closing channels

4. What is non-blocking I/O?
   - A) I/O that doesn't block
   - B) I/O that blocks
   - C) Synchronous I/O
   - D) Asynchronous I/O

5. Which channel is for UDP?
   - A) SocketChannel
   - B) ServerSocketChannel
   - C) DatagramChannel
   - D) FileChannel

## True/False Questions

6. Channels are bidirectional.
   - True / False

7. FileChannel is for network I/O.
   - True / False

8. Selectors enable non-blocking I/O.
   - True / False

## Code Output Questions

9. What will this code print?
```java
FileChannel channel = FileChannel.open(Path.of("/tmp/test.txt"), 
    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
System.out.println(channel.isOpen());
channel.close();
```

10. What will this code print?
```java
ByteBuffer buf = ByteBuffer.allocate(1024);
System.out.println(buf.capacity());
```

## Answers

1. A - Channel is bidirectional data transfer
2. A - FileChannel is for file channels
3. A - Selector monitors multiple channels
4. A - Non-blocking I/O doesn't block
5. C - DatagramChannel is for UDP
6. True - Channels are bidirectional
7. False - FileChannel is for file I/O
8. True - Selectors enable non-blocking I/O
9. Output:
```
true
```
10. Output:
```
1024
```
