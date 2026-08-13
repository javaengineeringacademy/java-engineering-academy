# Quiz: Charset

## Multiple Choice Questions

1. What is a Charset in Java?
   - A) A character set
   - B) A mapping between bytes and characters
   - C) A string encoding
   - D) A file format

2. What is the default charset in Java?
   - A) UTF-8
   - B) ASCII
   - C) Platform-dependent
   - D) ISO-8859-1

3. Which method returns the default charset?
   - A) `Charset.defaultCharset()`
   - B) `Charset.getDefault()`
   - C) `System.charset()`
   - D) `Charset.systemCharset()`

4. What does `StandardCharsets.UTF_8` provide?
   - A) A string
   - B) A charset constant
   - C) A file
   - D) A stream

5. Which class is used to encode strings to bytes?
   - A) StringEncoder
   - B) ByteBuffer
   - C) String.getBytes()
   - D) CharsetEncoder

## True/False Questions

6. UTF-8 uses 1-4 bytes per character.
   - True / False

7. ASCII is a subset of UTF-8.
   - True / False

8. Platform default charset is always UTF-8.
   - True / False

## Code Output Questions

9. What will this code print?
```java
String s = "Hello";
byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
System.out.println(bytes.length);
```

10. What will this code print?
```java
String s = "ABC";
byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
String decoded = new String(bytes, StandardCharsets.UTF_8);
System.out.println(decoded.equals(s));
```

## Answers

1. B - Charset maps between bytes and characters
2. C - Default charset is platform-dependent
3. B - Charset.getDefault() returns default charset
4. B - StandardCharsets provides charset constants
5. C - String.getBytes() encodes to bytes
6. True - UTF-8 uses variable length encoding
7. True - ASCII is a subset of UTF-8
8. False - Platform default depends on OS settings
9. Output:
```
5
```
10. Output:
```
true
```
