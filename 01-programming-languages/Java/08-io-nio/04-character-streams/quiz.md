# Quiz: Character Streams

## Multiple Choice Questions

1. What is a character stream?
   - A) Sequence of characters
   - B) Sequence of bytes
   - C) Sequence of bits
   - D) Sequence of strings

2. Which class reads characters?
   - A) Reader
   - B) InputStream
   - C) Writer
   - D) OutputStream

3. What is the difference between Reader and InputStream?
   - A) Reader is for characters
   - B) InputStream is for characters
   - C) No difference
   - D) Reader is faster

4. Which method reads a character?
   - A) `read()`
   - B) `readChar()`
   - C) `get()`
   - D) `input()`

5. What does `readLine()` do?
   - A) Reads a line
   - B) Reads a character
   - C) Reads a byte
   - D) Reads a string

## True/False Questions

6. Character streams are for binary data.
   - True / False

7. Reader is abstract.
   - True / False

8. Character streams handle encoding.
   - True / False

## Code Output Questions

9. What will this code print?
```java
Reader reader = new StringReader("Hello");
int c;
while ((c = reader.read()) != -1) {
    System.out.print((char) c);
}
```

10. What will this code print?
```java
String s = "Hello World";
System.out.println(s.length());
```

## Answers

1. A - Character stream is a sequence of characters
2. A - Reader reads characters
3. A - Reader is for characters
4. A - read() reads a character
5. A - readLine() reads a line
6. False - Character streams are for text data
7. True - Reader is abstract
8. True - Character streams handle encoding
9. Output:
```
Hello
```
10. Output:
```
11
```
