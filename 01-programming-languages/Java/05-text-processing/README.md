# Text Processing in Java

## Overview
Java provides a rich set of APIs for text processing, string manipulation,
and working with character data. This module covers the essential text
processing capabilities available in the Java standard library.

## Why This Concept Exists
Applications constantly handle text — user input, file content, network data, configuration, and output display. Java's text processing APIs provide:
- Efficient string manipulation without manual memory management
- Character encoding/decoding across different formats
- Pattern matching for validation and extraction
- Internationalization for multilingual support
- Immutability guarantees for thread-safe text handling

## History
- **1995** — Java 1.0 introduced `String`, `StringBuffer`, `Character`
- **1997** — Java 1.1 added `Reader`/`Writer` for character-based I/O
- **2002** — Java 1.4 introduced `java.util.regex` for regular expressions
- **2004** — Java 5 added `Scanner` for text parsing
- **2011** — Java 7 added `Files` methods for reading/writing text files
- **2014** — Java 8 added `String.join()`, `String.format()` improvements
- **2020** — Java 15 introduced text blocks (`"""`) for multi-line strings
- **2021** — Java 17 added `String.stripIndent()`, `translateEscapes()`, `formatted()`
- **2023** — Java 21 added `String.contains()`, `String.strip()` enhancements

## Core Concepts

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

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| `==` for string comparison | Compares references, not content | Use `.equals()` |
| Concatenation in loops | O(n²) performance | Use `StringBuilder` |
| Ignoring encoding | Corrupted text data | Specify charset explicitly |
| Not closing resources | Memory leaks | Use try-with-resources |
| `new String("literal")` | Unnecessary heap object | Use string literal directly |
| Using `StringBuffer` everywhere | Unnecessary synchronization | Use `StringBuilder` in single-threaded code |

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

## Internal Working

### String Pool
- String literals are interned in a shared pool
- `String s = "hello"` creates one object (pool lookup or create)
- `String s = new String("hello")` always creates a new object
- `s.intern()` returns the pool reference for equal content

### String Immutability
- `String` objects are stored in a `final char[]` (Java 8) or `byte[]` (Java 9+)
- Every `replace()`, `substring()`, `toUpperCase()` creates a new String
- The JVM optimizes: `String.concat()` may use `StringConcatFactory` (Java 9+)

### StringBuilder Internals
- Backed by a `char[]` (Java 8) or `byte[]` (Java 9+) that grows dynamically
- `append()` modifies the array in-place (mutable)
- Default capacity: 16, doubles on resize (`newCapacity = (capacity + 1) * 2`)

### Regex Engine
- Java uses a NFA-based regex engine (backtracking)
- `Pattern` compiles regex to a state machine
- `Matcher` performs the match against input
- `Pattern.compile()` is expensive — reuse compiled patterns

### Character Encoding
- Java uses UTF-16 internally for `char`
- `Charset` handles encoding/decoding between bytes and characters
- UTF-8 is the default for file I/O (Java 18+)

## Examples

### String Manipulation
```java
public class StringExamples {
    public static void main(String[] args) {
        String name = "Hello, World!";
        
        // Common operations
        System.out.println(name.length());          // 13
        System.out.println(name.substring(0, 5));   // Hello
        System.out.println(name.toUpperCase());     // HELLO, WORLD!
        System.out.println(name.replace('l', 'L')); // HeLLo, WorLd!
        System.out.println(name.contains("World")); // true
        
        // Splitting
        String csv = "apple,banana,cherry";
        String[] fruits = csv.split(",");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
        
        // Joining
        String joined = String.join(" - ", "A", "B", "C");
        System.out.println(joined); // A - B - C
    }
}
```

### StringBuilder for Efficiency
```java
public class BuilderExample {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("item").append(i).append(" ");
        }
        String result = sb.toString();
        System.out.println("Built string of length: " + result.length());
    }
}
```

### Regular Expressions
```java
import java.util.regex.*;

public class RegexExample {
    public static void main(String[] args) {
        String text = "Order #12345 placed on 2024-01-15";
        
        // Extract order number
        Pattern pattern = Pattern.compile("#(\\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            System.out.println("Order: " + matcher.group(1));
        }
        
        // Validate email
        String email = "user@example.com";
        boolean valid = email.matches("[\\w.-]+@[\\w.-]+\\.\\w+");
        System.out.println("Valid email: " + valid);
    }
}
```

## Best Practices

**Do's:**
- Use `StringBuilder` in loops for string concatenation
- Reuse compiled `Pattern` objects for regex
- Use `String.equals()` for comparison, never `==`
- Use text blocks (`"""`) for multi-line strings (Java 15+)
- Use `String.isBlank()` (Java 11+) instead of `trim().isEmpty()`

**Don'ts:**
- Don't concatenate strings with `+` in loops
- Don't use `String.intern()` without understanding memory implications
- Don't create `new String("literal")` — use literals directly
- Don't ignore encoding when reading/writing files
- Don't use `StringBuffer` unless thread safety is required

## Interview Questions

### Q1: Why is String immutable in Java?
**Answer:** Strings are stored in the string pool for memory optimization. Immutability ensures thread safety, security (class loading, network connections), and allows `hashCode()` caching.

### Q2: What is the difference between `String`, `StringBuilder`, and `StringBuffer`?
**Answer:** `String` is immutable. `StringBuilder` is mutable and not thread-safe (fastest). `StringBuffer` is mutable and synchronized (thread-safe but slower).

### Q3: What is string interning?
**Answer:** String literals are stored in a pool. `s.intern()` returns the pool reference if the string exists, otherwise adds it. Reduces memory for repeated literals.

### Q4: How does `split()` handle trailing empty strings?
**Answer:** By default, trailing empty strings are removed. Use `split(regex, -1)` to keep all parts including trailing empties.

### Q5: What is the `StringBuilder` capacity vs length?
**Answer:** `length()` is the number of characters. `capacity()` is the total buffer size. `capacity()` grows automatically via `ensureCapacity()`.

### Q6: What are text blocks in Java 15+?
**Answer:** Multi-line string literals delimited by `"""`. They preserve formatting, strip common leading whitespace, and support escape sequences.

### Q7: What is the difference between `contains()` and `matches()`?
**Answer:** `contains()` checks if a substring exists. `matches()` checks if the entire string matches a regex pattern.

## Cross-References

- **Previous Module:** [04 - Collections Framework](../04-collections/)
- **Next Module:** [06 - Generics](../06-generics/)
- **Related:** [01 - Fundamentals](../01-fundamentals/) — basic String usage
- **Related:** [08 - I/O and NIO](../08-io-nio/) — reading/writing text files
- **Related:** [06 - Generics](../06-generics/) — type-safe text processing
- **External:** [Java Documentation - String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)
- **External:** [Java Documentation - StringBuilder](https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html)
