# Text Processing in Java

> **Difficulty:** ⭐⭐ Easy  
> **Reading:** 25 min | **Practice:** 40 min | **Total:** 65 min

## Overview
Applications constantly handle text — user input, file content, network data, configuration, and output display. Java's text processing APIs let you manipulate, validate, and transform text without worrying about encoding, memory management, or immutability pitfalls. This module covers String, StringBuilder, regular expressions, text blocks, and internationalization.

## Why This Concept Exists
Applications constantly handle text — user input, file content, network data, configuration, and output display. Java's text processing APIs provide:
- Efficient string manipulation without manual memory management
- Character encoding/decoding across different formats
- Pattern matching for validation and extraction
- Internationalization for multilingual support
- Immutability guarantees for thread-safe text handling

## History
- **1995** — Java 1.0 introduced `String`, `StringBuffer`, `Character` to provide immutable and mutable text handling with Unicode support
- **1997** — Java 1.1 added `Reader`/`Writer` for character-based I/O to support internationalization and proper encoding handling
- **2002** — Java 1.4 introduced `java.util.regex` to provide powerful pattern matching and text manipulation capabilities
- **2004** — Java 5 added `Scanner` to simplify reading and parsing text from various input sources
- **2011** — Java 7 added `Files` methods for reading/writing text files to provide concise, efficient file operations with NIO.2
- **2014** — Java 8 added `String.join()`, `String.format()` improvements to simplify string concatenation and formatting
- **2020** — Java 15 introduced text blocks (`"""`) to simplify multi-line strings and reduce escape sequences
- **2021** — Java 17 added `String.stripIndent()`, `translateEscapes()`, `formatted()` to enhance text block and string processing capabilities
- **2023** — Java 21 added `String.contains()`, `String.strip()` enhancements to provide more convenient text search and trimming operations

## Production Notes
- **Where is it used?** In all Java applications that process text, handle user input, read/write files, or perform pattern matching
- **Why is it useful?** Provides efficient, secure, and internationalized text manipulation with immutable strings and mutable builders
- **When should it be avoided?** For simple concatenation in loops (use StringBuilder), or when performance is critical and custom text processing is needed
- **Alternative?** StringBuilder for mutable strings, char arrays for low-level processing, or third-party libraries for complex parsing

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
reliable charset support for encoding and decoding text data.

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

Java provides detailed support for internationalization, allowing
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

## Prerequisites

- [Fundamentals](../01-fundamentals/README.md)

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Encoding mismatch (garbled characters) | Charset detection + explicit encoding | Use `StandardCharsets.UTF_8` explicitly; verify with hex dump of bytes |
| String concatenation in loops | JMH benchmarking | Benchmark `+=` vs `StringBuilder`; profile with async-profiler |
| Regex pattern compilation overhead | JFR + code review | Cache compiled `Pattern` objects; move `Pattern.compile()` outside loops |
| String pool memory issues | Heap dump analysis | Use `jmap -histo` to count String instances; identify `intern()` abuse |
| Text block indentation issues | IDE preview + manual inspection | Verify leading whitespace stripping; check `\s` escape in text blocks |

## Code Review Checklist

- [ ] `.equals()` used for String comparison, never `==`
- [ ] `StringBuilder` used for concatenation in loops
- [ ] Compiled `Pattern` objects cached and reused
- [ ] Charset explicitly specified when reading/writing files
- [ ] Text blocks (`"""`) used for multi-line strings (Java 15+)
- [ ] `String.isBlank()` used instead of `trim().isEmpty()` (Java 11+)
- [ ] No `new String("literal")` — use literals directly

## Architecture Considerations

Text processing touches every layer of a system — from HTTP request parsing to database storage to UI rendering. At scale, string handling choices affect memory usage (string interning policies), performance (StringBuilder vs concatenation), and correctness (encoding consistency). For internationalized systems, character encoding must be consistent across all components — database, application, API gateway, and frontend.

In distributed systems, text encoding mismatches between services cause silent data corruption. Standardizing on UTF-8 across the entire stack prevents these issues. For high-throughput text processing (log analysis, data pipelines), streaming approaches (NIO `CharsetDecoder`) avoid loading entire files into memory.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Explicit charset everywhere | Cross-service data exchange | Pros: Prevents encoding mismatches; Cons: More verbose code |
| StringBuilder for text building | Log message construction, SQL building | Pros: Efficient concatenation; Cons: Less readable than `+` |
| Text blocks for templates | SQL queries, JSON templates, HTML | Pros: Readable multi-line strings; Cons: Java 15+ only |
| Compiled Pattern caching | Regex-heavy processing | Pros: Avoids repeated compilation; Cons: Memory usage for cached patterns |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Regex denial of service (ReDoS) | Application hangs, CPU exhaustion | Use atomic regex engines; limit input length; test with adversarial patterns |
| Encoding injection attacks | Data corruption, cross-site scripting | Validate and normalize input encoding; escape output appropriately |
| Log injection via user input | Log forgery, log analysis disruption | Sanitize user input before logging; use structured logging |
| Sensitive data in string literals | Secret exposure in memory dumps | Use `char[]` or `ByteBuffer` for sensitive data; zero arrays after use |
| Unicode normalization attacks | Authentication bypass, file path traversal | Normalize Unicode input; use canonical comparison |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | `String`, `StringBuffer` | Replace `StringBuffer` with `StringBuilder` where thread safety not needed |
| Java 1.4 | `java.util.regex` | Use `Pattern`/`Matcher` for complex text processing |
| Java 7 | `Files` methods for text I/O | Replace `FileReader`/`FileWriter` with `Files.newBufferedReader()` |
| Java 11 | `String.isBlank()`, `strip()` | Replace `trim().isEmpty()` with `isBlank()` |
| Java 15 | Text blocks (`"""`) | Replace escaped multi-line strings with text blocks |
| Java 17 | `String.stripIndent()`, `formatted()` | Use `stripIndent()` for text block processing |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| `String.isBlank()` | Java 11 | Stable |
| `String.strip()`, `String.stripLeading()` | Java 11 | Stable |
| Text blocks (`"""`) | Java 15 | Stable |
| `String.formatted()` | Java 15 | Stable |
| `String.translateEscapes()` | Java 15 | Stable |
| `Stream.toList()` | Java 16 | Stable |

## Production Incidents

### Incident 1: String Concatenation in Loop Causing Performance Degradation

**Problem:** A report generation service took 30 minutes to generate a 10,000-line CSV file, exceeding the 5-minute SLA.
**Cause:** Used `String +=` in a loop to build CSV rows, creating millions of intermediate String objects.
**Impact:** Reports timed out; users couldn't access critical business data; manual workarounds required.
**Detection:** Performance profiling showed 95% of time spent in String concatenation; heap showed millions of String objects.
**Solution:** Replaced `String +=` with `StringBuilder`, reducing generation time to 30 seconds.
**Prevention:** Use `StringBuilder` for string concatenation in loops; profile string operations in hot paths.

### Incident 2: Encoding Mismatch Corrupting User Data

**Problem:** A user profile system displayed garbled characters for international names (e.g., "Müller" showed as "MÃ¼ller").
**Cause:** Database stored UTF-8 encoded strings, but application read files using platform default encoding (ISO-8859-1).
**Impact:** 20% of user profiles had corrupted names; customer complaints; data correction effort required.
**Detection:** User reports of garbled characters; investigation revealed encoding mismatch in file reading code.
**Solution:** Specified `StandardCharsets.UTF_8` explicitly in all file reading/writing operations.
**Prevention:** Always specify charset explicitly; use `StandardCharsets` constants; add encoding validation tests.

### Incident 3: Regex Pattern Compilation Causing Memory Leak

**Problem:** A log analysis service consumed 8GB of memory and crashed after processing 1 million log entries.
**Cause:** Compiled `Pattern` object inside a loop; each iteration created a new `Pattern` instance that wasn't garbage collected.
**Impact:** Service crashed every 4 hours; required restart; affected log monitoring for entire cluster.
**Detection:** Heap dumps showed millions of `Pattern` objects; GC logs showed constant full GC cycles.
**Solution:** Moved `Pattern.compile()` outside the loop; reused compiled pattern for all log entries.
**Prevention:** Compile regex patterns once and reuse; cache patterns in static fields; profile regex operations.

## Production Checklist

- [ ] Use `StringBuilder` for string concatenation in loops
- [ ] Reuse compiled `Pattern` objects for regex
- [ ] Use `String.equals()` for comparison, never `==`
- [ ] Use text blocks (`"""`) for multi-line strings (Java 15+)
- [ ] Use `String.isBlank()` (Java 11+) instead of `trim().isEmpty()`
- [ ] Specify charset explicitly when reading/writing files
- [ ] Don't create `new String("literal")` — use literals directly
- [ ] Don't use `String.intern()` without understanding memory implications
- [ ] Don't use `StringBuffer` unless thread safety is required
- [ ] Validate input strings before processing

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses String concatenation; doesn't think about encoding; uses `==` for comparison |
| Intermediate | Uses StringBuilder appropriately; specifies charset; understands immutability |
| Advanced | Optimizes regex performance; handles internationalization; designs text processing APIs |
| Expert | Builds parsers; optimizes for memory and performance; teaches text processing patterns |

## Common Myths

1. **Myth**: String concatenation with `+` is always fast
   **Truth**: The compiler optimizes simple concatenation, but loops create O(n²) complexity. Use `StringBuilder` in loops.

2. **Myth**: `String.intern()` always saves memory
   **Truth**: `String.intern()` can cause memory leaks if many unique strings are interned; it also has O(n) lookup cost.

3. **Myth**: `StringBuffer` is always safer than `StringBuilder`
   **Truth**: `StringBuffer` synchronization adds overhead; use `StringBuilder` in single-threaded code for better performance.

4. **Myth**: Regular expressions are always the best solution for text parsing
   **Truth**: Regex can be slow for simple operations; `String.split()`, `substring()`, and `indexOf()` are often faster.

5. **Myth**: Unicode is just about characters
   **Truth**: Unicode includes characters, code points, combining marks, and emoji; `char` is only 16 bits and can't represent all Unicode.

## Related Topics

- [Generics](../06-generics/README.md)

## Next

- [Generics](../06-generics/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | String manipulation |
| Complexity | O(n) for most operations |
| Thread Safe | Yes (String is immutable) |
| Ordered | Yes (character order) |
| Allows Null | No (String methods) |
| Best Alternative | StringBuilder (for mutations) |
| When to Use | Text operations |
| When to Avoid | Large text processing |
