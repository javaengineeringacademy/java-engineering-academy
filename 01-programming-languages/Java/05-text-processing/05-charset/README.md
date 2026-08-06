# Charset and Encoding in Java

A `Charset` defines how characters are encoded into bytes and decoded
from bytes. Java provides detailed charset support for working
with different character encodings.

## What are Charsets?

A charset is a mapping between a sequence of characters and a sequence
of bytes. Different charsets use different rules for this mapping.

```java
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// Get charset by name
Charset utf8 = Charset.forName("UTF-8");

// Using StandardCharsets constants (preferred)
Charset charset = StandardCharsets.UTF_8;
```

## Common Charsets

### UTF-8

Variable-length encoding (1-4 bytes per character). Most common
on the web. ASCII compatible.

```java
String text = "Hello";
byte[] utf8 = text.getBytes(StandardCharsets.UTF_8);
// Each ASCII character uses 1 byte
```

### UTF-16

Fixed or variable-length encoding (2 or 4 bytes). Java's internal
string representation uses UTF-16.

```java
byte[] utf16 = text.getBytes(StandardCharsets.UTF_16);
// Characters typically use 2 bytes
```

### US-ASCII

7-bit encoding for basic English characters only.

```java
byte[] ascii = "Hello".getBytes(StandardCharsets.US_ASCII);
// Each character uses 1 byte
```

### ISO-8859-1

8-bit encoding for Western European languages.

```java
byte[] latin1 = "Café".getBytes(StandardCharsets.ISO_8859_1);
// Uses 1 byte per character
```

## Encoding and Decoding

### String to Bytes (Encoding)

```java
String text = "Hello, 世界";

// Using getBytes with charset
byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
byte[] utf16Bytes = text.getBytes(StandardCharsets.UTF_16);

// Using Charset.encode
ByteBuffer buffer = StandardCharsets.UTF_8.encode(text);
```

### Bytes to String (Decoding)

```java
byte[] bytes = {72, 101, 108, 108, 111};

// Using String constructor
String str1 = new String(bytes, StandardCharsets.UTF_8);

// Using Charset.decode
ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
String str2 = charBuffer.toString();
```

## ByteBuffer and CharBuffer

### CharBuffer

```java
CharBuffer charBuffer = CharBuffer.allocate(100);
charBuffer.put("Hello");
charBuffer.flip(); // Prepare for reading
String text = charBuffer.toString();
```

### ByteBuffer

```java
ByteBuffer byteBuffer = ByteBuffer.allocate(100);
byteBuffer.put("Hello".getBytes());
byteBuffer.flip(); // Prepare for reading
```

## When to Specify Charset

### Always specify charset explicitly

```java
// BAD: Uses platform default charset (may vary)
byte[] bytes = text.getBytes();

// GOOD: Explicit charset
byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
```

### Common scenarios

```java
// File I/O
new InputStreamReader(stream, StandardCharsets.UTF_8)

// Network I/O
new BufferedReader(new InputStreamReader(
    socket.getInputStream(), StandardCharsets.UTF_8))

// String construction
new String(bytes, StandardCharsets.UTF_8)
```

## Summary

- Always specify charset explicitly when encoding/decoding
- Use StandardCharsets constants instead of string names
- UTF-8 is the most common charset for web applications
- UTF-16 is Java's internal string representation
- ByteBuffer and CharBuffer are used for encoding/decoding
- Check canEncode() before encoding non-ASCII characters
