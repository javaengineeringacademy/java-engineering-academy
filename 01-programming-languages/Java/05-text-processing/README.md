# Text Processing in Java

Java provides a rich set of APIs for text processing, string manipulation,
and working with character data. This module covers the essential text
processing capabilities available in the Java standard library.

## Topics

### 1. String

The `String` class is immutable and provides extensive methods for
text manipulation. Strings in Java are stored in the string pool for
memory optimization.

**Key Concepts:**
- Immutability and its implications
- String pool and memory sharing
- Common methods (substring, replace, split, trim, etc.)
- Comparison operators (== vs equals)
- String concatenation performance

### 2. StringBuilder

`StringBuilder` is a mutable sequence of characters. It provides an
efficient way to build strings when you need to make frequent modifications.

**Key Concepts:**
- Mutable string operations
- append(), insert(), delete(), reverse()
- StringBuilder vs StringBuffer performance
- Thread safety considerations

### 3. StringBuffer

`StringBuffer` is a thread-safe mutable sequence of characters. It is
similar to StringBuilder but synchronized for thread safety.

**Key Concepts:**
- Thread-safe mutable strings
- synchronized methods
- When to use StringBuffer vs StringBuilder
- Performance implications of synchronization

### 4. Character

The `Character` class provides static methods for performing operations
on individual characters and determining their properties.

**Key Concepts:**
- Character class static methods
- Character type checking (isDigit, isLetter, etc.)
- Case conversion
- Unicode support
- char vs Character

### 5. Charset

Charsets define how characters are encoded into bytes. Java provides
robust charset support for encoding and decoding text data.

**Key Concepts:**
- Standard charsets (UTF-8, UTF-16, ISO-8859-1)
- Charset encoding and decoding
- ByteBuffer and CharBuffer
- Reading files with specific charsets

### 6. Regular Expressions

Java provides a powerful regex engine through the `java.util.regex`
package for pattern matching and text searching.

**Key Concepts:**
- Pattern and Matcher classes
- Common regex syntax
- Named groups and capturing
- Split and replace with patterns

### 7. Text Blocks (Java 15+)

Text blocks provide a multi-line string literal that avoids the need
for most escape sequences and automatically formats the string in a
predictable way.

**Key Concepts:**
- Text block syntax
- Indentation stripping
- Line continuation
- Escape sequences in text blocks

### 8. Unicode

Java has built-in support for Unicode characters, enabling the handling
of international character sets and supplementary characters.

**Key Concepts:**
- Unicode basics and code points
- Code points vs code units
- Supplementary characters
- Unicode escape sequences

### 9. Internationalization (i18n)

Java provides comprehensive support for internationalization, allowing
applications to be adapted to different languages and regions without
code changes.

**Key Concepts:**
- ResourceBundle for localized content
- NumberFormat and DateFormat
- Currency formatting
- Locale handling

## Module Structure

```
05-text-processing/
├── README.md
├── 01-string/
│   ├── README.md
│   ├── StringBasics.java
│   ├── StringMethods.java
│   ├── StringFormatting.java
│   ├── immutability/
│   └── string-pool/
├── 02-stringbuilder/
│   ├── README.md
│   └── StringBuilderDemo.java
├── 03-stringbuffer/
│   ├── README.md
│   └── StringBufferDemo.java
├── 04-character/
│   ├── README.md
│   └── CharacterDemo.java
├── 05-charset/
│   ├── README.md
│   └── CharsetDemo.java
├── 06-regex/
│   ├── README.md
│   ├── RegexBasics.java
│   └── RegexPatterns.java
├── 07-text-blocks/
│   ├── README.md
│   └── TextBlocksDemo.java
├── 08-unicode/
│   ├── README.md
│   └── UnicodeDemo.java
└── 09-internationalization/
    ├── README.md
    └── I18nDemo.java
```

## Performance Considerations

- Use `StringBuilder` over `String` concatenation in loops
- Use `StringBuilder` over `StringBuffer` when thread safety is not needed
- Prefer `StringBuilder` for single-threaded string building
- Consider `StringBuffer` only when shared mutable state is required
- Use `String.intern()` carefully to avoid memory leaks

## Common Patterns

```java
// String concatenation in loop (BAD)
String result = "";
for (String s : list) {
    result += s; // Creates new String object each iteration
}

// String concatenation in loop (GOOD)
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s); // Modifies same StringBuilder
}
String result = sb.toString();

// String comparison (BAD)
if (str1 == str2) { ... }

// String comparison (GOOD)
if (str1.equals(str2)) { ... }
```

## Further Reading

- [Java Documentation - String Class](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)
- [Java Documentation - StringBuilder Class](https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html)
- [Java Documentation - StringBuffer Class](https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuffer.html)
- [Java Documentation - Character Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html)
