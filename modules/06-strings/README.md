# Module 06: Strings in Java

## Table of Contents

1. [Introduction](#1-introduction)
2. [Learning Objectives](#2-learning-objectives)
3. [Prerequisites](#3-prerequisites)
4. [Why This Concept Exists](#4-why-this-concept-exists)
5. [Problem Statement](#5-problem-statement)
6. [Theory](#6-theory)
7. [Internal Working](#7-internal-working)
8. [JVM Perspective](#8-jvm-perspective)
9. [Memory Representation](#9-memory-representation)
10. [Architecture Diagram](#10-architecture-diagram)
11. [Flow Diagram](#11-flow-diagram)
12. [Syntax](#12-syntax)
13. [Easy Example](#13-easy-example)
14. [Medium Example](#14-medium-example)
15. [Hard Example](#15-hard-example)
16. [Enterprise Example](#16-enterprise-example)
17. [Performance](#17-performance)
18. [Time & Space Complexity](#18-time--space-complexity)
19. [Thread Safety](#19-thread-safety)
20. [Best Practices](#20-best-practices)
21. [Common Mistakes](#21-common-mistakes)
22. [Pitfalls](#22-pitfalls)
23. [Debugging Tips](#23-debugging-tips)
24. [Comparison Table](#24-comparison-table)
25. [Decision Tree](#25-decision-tree)
26. [Interview Questions](#26-interview-questions)
27. [Exercises](#27-exercises)
28. [Summary](#28-summary)
29. [References](#29-references)

---

## 1. Introduction

Strings are one of the most commonly used data types in Java. A `String` in Java represents a sequence of characters and is an object of the `String` class. Unlike primitive types, strings are immutable objects, meaning once created, their values cannot be changed.

Java provides three main classes for handling strings:
- **`String`** - Immutable sequence of characters
- **`StringBuilder`** - Mutable sequence of characters (not thread-safe)
- **`StringBuffer`** - Mutable sequence of characters (thread-safe)

Understanding how strings work internally is crucial for writing efficient Java applications. The Java String pool, immutability, and memory management of strings are fundamental concepts every Java developer must master.

---

## 2. Learning Objectives

By the end of this module, you will be able to:

- Understand String immutability and its implications
- Explain the String pool and how it optimizes memory
- Use `StringBuilder` and `StringBuffer` effectively
- Apply common String methods for manipulation
- Understand regular expressions in Java
- Choose the right string handling approach for performance
- Avoid common String-related pitfalls
- Optimize string operations in production code

---

## 3. Prerequisites

- Basic Java programming knowledge
- Understanding of object-oriented concepts
- Familiarity with JVM basics
- Knowledge of basic data types

---

## 4. Why This Concept Exists

Strings are essential because:

1. **Text Processing**: Almost every application needs to process text data
2. **Data Exchange**: JSON, XML, CSV - all text-based formats use strings
3. **User Interface**: Displaying text to users requires string handling
4. **File Operations**: File paths, content, and metadata are strings
5. **Network Communication**: HTTP headers, URLs, and payloads are strings

Java's approach to strings with immutability and the string pool provides:
- **Memory Efficiency**: Reusing identical strings saves memory
- **Security**: Immutable strings are safe for sensitive operations
- **Thread Safety**: Immutable objects are inherently thread-safe
- **Hash Code Caching**: Enables fast hash-based collections

---

## 5. Problem Statement

Consider these common challenges:

1. **Memory Waste**: Creating millions of duplicate string objects wastes heap space
2. **Performance**: String concatenation in loops creates excessive garbage
3. **Security Risks**: Mutable strings can be modified by malicious code
4. **Concurrency**: Multiple threads accessing shared strings safely
5. **Memory Leaks**: String references preventing garbage collection

---

## 6. Theory

### 6.1 String Immutability

A `String` object is immutable, meaning its value cannot be changed after creation. When you "modify" a string, you actually create a new `String` object.

```java
String s1 = "Hello";
String s2 = s1.concat(" World"); // Creates new String object
// s1 is still "Hello", s2 is "Hello World"
```

**Why Immutability?**
- Enables the String pool mechanism
- Makes strings thread-safe
- Allows safe caching of hash codes
- Enables secure class loading

### 6.2 String Pool

The String pool (also called intern pool) is a special memory area within the heap where Java stores unique string literals. When a string literal is created, JVM checks the pool first:

- If the string exists, a reference to the existing instance is returned
- If not, a new string is created and added to the pool

```java
String s1 = "Hello"; // Created in pool
String s2 = "Hello"; // Reference to existing "Hello" in pool
String s3 = new String("Hello"); // Creates new object outside pool

System.out.println(s1 == s2);      // true (same reference)
System.out.println(s1 == s3);      // false (different objects)
System.out.println(s1.equals(s3));  // true (same content)
```

### 6.3 StringBuilder and StringBuffer

Both are mutable alternatives to `String` for building strings efficiently:

- **`StringBuilder`**: Not thread-safe, faster performance
- **`StringBuffer`**: Thread-safe (synchronized), slower performance

### 6.4 String Methods

Java provides extensive methods for string manipulation:

| Method | Description |
|--------|-------------|
| `length()` | Returns the length |
| `charAt(int)` | Returns character at index |
| `substring(int, int)` | Returns substring |
| `indexOf(String)` | Returns index of substring |
| `toLowerCase()` | Converts to lowercase |
| `toUpperCase()` | Converts to uppercase |
| `trim()` | Removes leading/trailing whitespace |
| `replace(char, char)` | Replaces characters |
| `split(String)` | Splits string into array |
| `contains(String)` | Checks if string contains substring |
| `startsWith(String)` | Checks prefix |
| `endsWith(String)` | Checks suffix |
| `isEmpty()` | Checks if empty |
| `equals(Object)` | Compares content |
| `compareTo(String)` | Lexicographic comparison |
| `intern()` | Returns canonical representation |

---

## 7. Internal Working

### 7.1 String Creation Process

When you write:
```java
String s = "Hello";
```

1. JVM checks if "Hello" exists in the String pool
2. If yes, returns reference to existing object
3. If no, creates new String object and adds to pool
4. Reference is assigned to variable `s`

### 7.2 String Concatenation

```java
String result = "Hello" + " " + "World";
```

The compiler optimizes this to:
```java
String result = "Hello World";
```

But for variable concatenation:
```java
String result = s1 + s2;
```

Compiler creates `StringBuilder`, appends values, calls `toString()`.

### 7.3 String.intern() Method

The `intern()` method adds a string to the pool and returns the canonical reference:

```java
String s1 = new String("Hello");
String s2 = s1.intern(); // Added to pool
String s3 = "Hello";     // Already in pool

System.out.println(s2 == s3); // true
```

---

## 8. JVM Perspective

### 8.1 Memory Areas

- **String Pool**: Located in Heap (PermGen/Metaspace in older JVMs)
- **String Objects**: Stored in the Heap
- **String References**: Stored in Stack (for local variables)

### 8.2 JVM Internals

```
┌─────────────────────────────────────────┐
│                 HEAP                     │
│  ┌─────────────────────────────────┐   │
│  │         STRING POOL             │   │
│  │  ┌───────┐ ┌───────┐ ┌──────┐  │   │
│  │  │"Hello"│ │"World"│ │"Java"│  │   │
│  │  └───────┘ └───────┘ └──────┘  │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │      OTHER OBJECTS              │   │
│  │  ┌─────────────────────┐       │   │
│  │  │new String("Hello")  │       │   │
│  │  └─────────────────────┘       │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

---

## 9. Memory Representation

### 9.1 String Object Structure

```
String Object (on Heap)
┌──────────────────────────────┐
│  Object Header               │
│  ┌────────────────────────┐  │
│  │ Mark Word (8 bytes)    │  │
│  │ Klass Pointer (4 bytes)│  │
│  └────────────────────────┘  │
│  ┌────────────────────────┐  │
│  │ char[] value           │  │
│  │ int hash               │  │
│  │ int hash32             │  │
│  └────────────────────────┘  │
└──────────────────────────────┘
```

### 9.2 String Pool Storage

Before Java 7: PermGen space (limited size)
After Java 7: Heap space (configurable, larger)

---

## 10. Architecture Diagram

```mermaid
graph TB
    A[Java Source Code] --> B[Compiler]
    B --> C[.class File]
    C --> D[ClassLoader]
    D --> E[JVM]
    
    E --> F[Method Area]
    E --> G[Heap]
    E --> H[Stack]
    
    F --> F1[Class Metadata]
    F --> F2[Constant Pool]
    
    G --> G1[String Pool]
    G --> G2[Regular Objects]
    
    H --> H1[Local Variables]
    H --> H2[Method Calls]
    
    G1 --> G1a[Literal "Hello"]
    G1 --> G1b[Literal "World"]
    G1 --> G1c[Literal "Java"]
    
    G2 --> G2a[new String - Hello]
    G2 --> G2b[new String - World]
```

---

## 11. Flow Diagram

```mermaid
flowchart TD
    A[Start: Create String] --> B{Literal or new?}
    
    B -->|Literal: "Hello"| C{In String Pool?}
    B -->|new String\("Hello"\)| D[Create New Object on Heap]
    
    C -->|Yes| E[Return Pool Reference]
    C -->|No| F[Create in Pool]
    
    F --> E
    D --> G[Return Heap Reference]
    
    E --> H[Use String]
    G --> H
    
    H --> I{Operation Type?}
    I -->|Concatenation| J[Create StringBuilder]
    I -->|Comparison| K[Use equals method]
    I -->|Modification| L[Create New String]
    
    J --> J1[Append Values]
    J1 --> J2[Call toString]
    J2 --> L
    
    L --> M[Return New String Object]
```

---

## 12. Syntax

### 12.1 String Declaration

```java
// String literals (stored in pool)
String s1 = "Hello";
String s2 = "World";

// String objects (stored on heap)
String s3 = new String("Hello");
String s4 = new String("Hello");

// Empty string
String s5 = "";
String s6 = new String();

// char array to string
char[] chars = {'H', 'e', 'l', 'l', 'o'};
String s7 = new String(chars);
String s8 = String.valueOf(chars);
```

### 12.2 StringBuilder

```java
StringBuilder sb = new StringBuilder();       // empty
StringBuilder sb2 = new StringBuilder(50);   // initial capacity
StringBuilder sb3 = new StringBuilder("Hello"); // with content

sb.append("Hello");
sb.append(" ");
sb.append("World");
String result = sb.toString(); // "Hello World"
```

### 12.3 StringBuffer

```java
StringBuffer sbf = new StringBuffer();
sbf.append("Hello");
sbf.append(" ");
sbf.append("World");
String result = sbf.toString(); // "Hello World"
```

---

## 13. Easy Example

```java
public class StringBasicsEasy {
    public static void main(String[] args) {
        // Creating strings
        String greeting = "Hello";
        String name = "Java";
        
        // Concatenation
        String message = greeting + " " + name + "!";
        System.out.println(message); // Hello Java!
        
        // Length
        System.out.println("Length: " + message.length()); // 11
        
        // Access characters
        char firstChar = message.charAt(0);
        System.out.println("First char: " + firstChar); // H
        
        // Substring
        String sub = message.substring(0, 5);
        System.out.println("Substring: " + sub); // Hello
        
        // Case conversion
        System.out.println("Upper: " + greeting.toUpperCase()); // HELLO
        System.out.println("Lower: " + greeting.toLowerCase()); // hello
        
        // Comparison
        String a = "Hello";
        String b = "Hello";
        String c = new String("Hello");
        
        System.out.println("a == b: " + (a == b)); // true
        System.out.println("a == c: " + (a == c)); // false
        System.out.println("a.equals(c): " + a.equals(c)); // true
    }
}
```

---

## 14. Medium Example

```java
public class StringBasicsMedium {
    public static void main(String[] args) {
        // StringBuilder for efficient concatenation
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("Line ").append(i).append("\n");
        }
        String result = sb.toString();
        
        // String methods
        String text = "  Hello, World!  ";
        System.out.println("Trimmed: '" + text.trim() + "'");
        System.out.println("Contains 'World': " + text.contains("World"));
        System.out.println("Starts with '  H': " + text.startsWith("  H"));
        System.out.println("Ends with '!  ': " + text.endsWith("!  "));
        System.out.println("Index of 'World': " + text.indexOf("World"));
        
        // Replace
        String replaced = text.replace("World", "Java");
        System.out.println("Replaced: " + replaced.trim());
        
        // Split
        String csv = "apple,banana,cherry";
        String[] fruits = csv.split(",");
        for (String fruit : fruits) {
            System.out.println("Fruit: " + fruit);
        }
        
        // Regular expression
        String email = "user@example.com";
        boolean isValid = email.matches("[\\w.]+@[\\w.]+\\.[a-z]+");
        System.out.println("Valid email: " + isValid);
        
        // String pool demonstration
        String s1 = "Programming";
        String s2 = "Programming";
        String s3 = s1.intern();
        
        System.out.println("s1 == s2: " + (s1 == s2)); // true
        System.out.println("s1 == s3: " + (s1 == s3)); // true
    }
}
```

---

## 15. Hard Example

```java
public class StringBasicsHard {
    public static void main(String[] args) {
        // String immutability demonstration
        String original = "Hello";
        String modified = modifyString(original);
        System.out.println("Original: " + original); // Hello
        System.out.println("Modified: " + modified); // Hello Modified
        
        // String pool vs heap
        String literal1 = "Java";
        String literal2 = "Java";
        String heap1 = new String("Java");
        String heap2 = heap1.intern();
        
        System.out.println("literal1 == literal2: " + (literal1 == literal2)); // true
        System.out.println("literal1 == heap1: " + (literal1 == heap1)); // false
        System.out.println("literal1 == heap2: " + (literal1 == heap2)); // true
        
        // Complex string operations
        String input = "  Hello   World   Java  ";
        String[] words = input.trim().split("\\s+");
        StringBuilder output = new StringBuilder();
        for (String word : words) {
            output.append(word.substring(0, 1).toUpperCase())
                   .append(word.substring(1).toLowerCase())
                   .append(" ");
        }
        System.out.println("Title Case: " + output.toString().trim());
        
        // String concatenation performance comparison
        long start = System.currentTimeMillis();
        String concat = "";
        for (int i = 0; i < 100000; i++) {
            concat += "a";
        }
        long stringTime = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append("a");
        }
        String builderResult = sb.toString();
        long builderTime = System.currentTimeMillis() - start;
        
        System.out.println("String concat time: " + stringTime + "ms");
        System.out.println("StringBuilder time: " + builderTime + "ms");
        
        // Regex patterns
        String phone = "+1-555-123-4567";
        String phoneRegex = "\\+?\\d{1,3}-?\\d{3}-?\\d{3}-?\\d{4}";
        System.out.println("Valid phone: " + phone.matches(phoneRegex));
        
        // Advanced: String interning in collections
        String[] wordsArray = {"hello", "world", "hello", "java", "hello"};
        String[] interned = new String[wordsArray.length];
        for (int i = 0; i < wordsArray.length; i++) {
            interned[i] = wordsArray[i].intern();
        }
        
        System.out.println("Interned strings equal: " + (interned[0] == interned[2]));
    }
    
    static String modifyString(String s) {
        return s + " Modified"; // Creates new String object
    }
}
```

---

## 16. Enterprise Example

```java
public class StringBasicsEnterprise {
    
    // StringBuilder for log formatting
    public static String formatLogEntry(String level, String message, long timestamp) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("[")
                .append(timestamp)
                .append("] [")
                .append(level.toUpperCase())
                .append("] ")
                .append(message)
                .append("\n");
        return logEntry.toString();
    }
    
    // String pooling for configuration keys
    private static final String CONFIG_KEY_PREFIX = "app.config.";
    
    public static String getConfigKey(String key) {
        return (CONFIG_KEY_PREFIX + key).intern();
    }
    
    // Efficient CSV parsing
    public static String[] parseCSVLine(String line) {
        if (line == null || line.isEmpty()) {
            return new String[0];
        }
        
        java.util.List<String> fields = new java.util.ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString().trim());
        
        return fields.toArray(new String[0]);
    }
    
    // String validation utilities
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }
    
    // String manipulation for data cleaning
    public static String cleanWhitespace(String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }
        return input.trim().replaceAll("\\s+", " ");
    }
    
    public static void main(String[] args) {
        // Log formatting
        String log = formatLogEntry("INFO", "User logged in successfully", 
                                     System.currentTimeMillis());
        System.out.println(log);
        
        // Config keys
        String dbKey = getConfigKey("database.url");
        String cacheKey = getConfigKey("cache.ttl");
        System.out.println("DB Key: " + dbKey);
        System.out.println("Cache Key: " + cacheKey);
        
        // CSV parsing
        String csvLine = "\"John Doe\",30,\"New York\",\"Software Engineer\"";
        String[] fields = parseCSVLine(csvLine);
        System.out.println("Parsed fields:");
        for (String field : fields) {
            System.out.println("  - " + field);
        }
        
        // Data cleaning
        String dirtyData = "   Hello    World   Java   ";
        String cleanData = cleanWhitespace(dirtyData);
        System.out.println("Cleaned: '" + cleanData + "'");
        
        // Validation
        System.out.println("Is null or empty: " + isNullOrEmpty(null));
        System.out.println("Is null or blank: " + isNullOrBlank("   "));
    }
}
```

---

## 17. Performance

### 17.1 String Concatenation

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| `+` operator (compile-time) | O(1) | Optimized to single string |
| `+` operator (runtime) | O(n²) | Creates StringBuilder internally |
| StringBuilder.append() | O(1) amortized | Best for loops |
| String.concat() | O(n) | Creates new string |

### 17.2 String Methods

| Method | Time Complexity |
|--------|----------------|
| `length()` | O(1) |
| `charAt(int)` | O(1) |
| `substring(int, int)` | O(n) |
| `indexOf(String)` | O(n*m) |
| `contains(String)` | O(n*m) |
| `replace(char, char)` | O(n) |
| `split(String)` | O(n) |
| `trim()` | O(n) |
| `toLowerCase()` | O(n) |
| `toUpperCase()` | O(n) |
| `equals(Object)` | O(n) |
| `compareTo(String)` | O(n) |

### 17.3 Memory Usage

- String object overhead: ~16-24 bytes
- char[] array: 2 bytes per character + 16 bytes overhead
- StringBuilder/StringBuffer: Initial capacity + 16 bytes

---

## 18. Time & Space Complexity

### 18.1 String Operations

| Operation | Time | Space |
|-----------|------|-------|
| Creation from literal | O(1) | O(1) (shared) |
| Creation from new | O(n) | O(n) |
| Concatenation (n strings) | O(n²) | O(n) |
| StringBuilder (n appends) | O(n) | O(n) |
| Search substring | O(n*m) | O(1) |
| Replace all | O(n) | O(n) |
| Split | O(n) | O(n) |
| Join | O(n) | O(n) |

---

## 19. Thread Safety

### 19.1 String

Strings are immutable, making them inherently thread-safe. Multiple threads can read the same string without synchronization.

### 19.2 StringBuilder

StringBuilder is NOT thread-safe. Do not use across threads:

```java
// WRONG - Not thread-safe
StringBuilder sharedBuilder = new StringBuilder();
// Multiple threads accessing sharedBuilder = CRASH

// CORRECT - Use StringBuffer for thread safety
StringBuffer safeBuffer = new StringBuffer();
// Or create separate StringBuilder per thread
```

### 19.3 StringBuffer

StringBuffer is thread-safe via synchronization:

```java
StringBuffer sb = new StringBuffer();
synchronized(sb) {
    sb.append("Hello");
    sb.append(" World");
}
```

### 19.4 String Pool Thread Safety

The String pool is thread-safe. String literals are shared safely across threads.

---

## 20. Best Practices

1. **Use string literals when possible**: Take advantage of the String pool
2. **Use StringBuilder for loops**: Avoid string concatenation in loops
3. **Use equals() for comparison**: Never use `==` to compare string content
4. **Check for null before operations**: Prevent NullPointerException
5. **Use String.join() for joining**: More readable than StringBuilder
6. **Prefer toLowerCase(Locale)**: Use locale-specific conversion when needed
7. **Intern strings carefully**: Only intern frequently used, known-at-compile-time strings
8. **Use String.valueOf() for conversion**: More null-safe than null.toString()
9. **Use isEmpty() or isBlank()**: More readable than length() == 0
10. **Cache frequently used patterns**: Compile regex patterns once

---

## 21. Common Mistakes

### 21.1 Using == for String Comparison

```java
String s1 = "Hello";
String s2 = new String("Hello");

// WRONG
if (s1 == s2) { ... } // May fail

// CORRECT
if (s1.equals(s2)) { ... }
```

### 21.2 String Concatenation in Loops

```java
// WRONG - O(n²) performance
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i;
}

// CORRECT - O(n) performance
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

### 21.3 Forgetting Immutability

```java
String s = "Hello";
s.toUpperCase(); // WRONG - doesn't change s
System.out.println(s); // Still "Hello"

// CORRECT
s = s.toUpperCase();
```

### 21.4 Not Handling Null

```java
String s = null;
// s.length(); // NullPointerException

// CORRECT
if (s != null) {
    s.length();
}
// Or
if (s != null && !s.isEmpty()) { ... }
```

---

## 22. Pitfalls

1. **String.intern() can cause PermGen/Metaspace overflow** if used excessively
2. **String concatenation with null** converts null to "null" string
3. **Regular expressions are expensive** to compile - cache Pattern objects
4. **String.getBytes()** uses platform default encoding - specify charset explicitly
5. **String.substring()** in Java 7+ creates new array (different from Java 6)
6. **String.split()** with limit parameter behaves differently than expected
7. **String.replace()** with regex is slower than replaceAll with literal string
8. **String.toLowerCase()** without locale can cause issues with Turkish locale

---

## 23. Debugging Tips

1. **Use equals() not ==** to compare strings in debugger
2. **Check String pool** with `System.identityHashCode()`
3. **Monitor heap usage** when using intern()
4. **Use VisualVM** to inspect string objects in heap
5. **Check for string concatenation** in profiler output
6. **Use StringBuilder.toString()** to verify builder contents
7. **Log string length** when debugging concatenation issues
8. **Check encoding** when converting bytes to strings

---

## 24. Comparison Table

| Feature | String | StringBuilder | StringBuffer |
|---------|--------|---------------|--------------|
| Mutable | No | Yes | Yes |
| Thread-safe | Yes (immutable) | No | Yes |
| Performance | Read-only | Fastest | Slower |
| Use case | Constants | Single-thread | Multi-thread |
| Memory | Pool optimization | No pooling | No pooling |
| Synchronization | N/A | No | Yes |
| Best for | Keys, literals | String building | Shared buffers |

---

## 25. Decision Tree

```
Need to handle text?
│
├─ Need immutable string?
│  ├─ Yes → Use String
│  └─ No → Need thread safety?
│     ├─ Yes → Use StringBuffer
│     └─ No → Use StringBuilder
│
├─ Building string in loop?
│  └─ Use StringBuilder
│
├─ Comparing strings?
│  └─ Use equals()
│
├─ Need to intern?
│  └─ Use String.intern() (carefully)
│
└─ Joining multiple strings?
   └─ Use String.join() or Stream API
```

---

## 26. Interview Questions

### Q1: Why is String immutable in Java?

**Answer**: Strings are immutable for:
1. **String pool optimization**: Allows reuse of identical strings
2. **Thread safety**: Immutable objects are inherently thread-safe
3. **Security**: Prevents modification of sensitive data
4. **Hash code caching**: Enables efficient hash-based collections
5. **Class loading**: Safe for dynamic class loading

### Q2: What is the difference between == and equals() for strings?

**Answer**: 
- `==` compares references (memory addresses)
- `equals()` compares content (character sequences)

```java
String s1 = "Hello";
String s2 = new String("Hello");
s1 == s2      // false (different objects)
s1.equals(s2) // true (same content)
```

### Q3: When should you use StringBuilder vs StringBuffer?

**Answer**:
- **StringBuilder**: Single-threaded string building (better performance)
- **StringBuffer**: Multi-threaded string building (thread-safe via synchronization)

### Q4: How does String pool work?

**Answer**:
1. String literals are added to pool automatically
2. JVM checks pool before creating new string
3. `intern()` method manually adds string to pool
4. Pool is located in heap (after Java 7)
5. Pool is garbage collected like other heap objects

### Q5: What are the memory implications of String.intern()?

**Answer**:
- Adds string to pool (permanent until GC)
- Returns canonical reference
- Can cause memory leaks if overused
- Good for repeated string values
- Bad for unique/dynamic strings

### Q6: How do you split a string efficiently?

**Answer**:
```java
// For simple splits
String[] parts = csv.split(",");

// For performance-critical code
String[] parts = csv.split(",", -1); // Preserve trailing empty strings

// For large strings
Pattern pattern = Pattern.compile(",");
String[] parts = pattern.split(csv);
```

### Q7: What is the difference between substring() in Java 6 vs 7+?

**Answer**:
- **Java 6**: Shares char[] array (memory efficient but potential leak)
- **Java 7+**: Creates new char[] array (safer, but more memory)

### Q8: How do you check if a string is empty or blank?

**Answer**:
```java
// Empty string
if (str.isEmpty()) { ... } // str.length() == 0

// Blank string (whitespace only)
if (str.isBlank()) { ... } // Java 11+

// Manual check
if (str.trim().isEmpty()) { ... }
```

### Q9: What is the performance difference between String concatenation and StringBuilder?

**Answer**:
- String concatenation: O(n²) in loops (creates new string each time)
- StringBuilder: O(n) amortized (resizes internally)

Example: 10,000 concatenations
- String: ~100 million operations
- StringBuilder: ~10,000 operations

### Q10: How do you convert between String and other types?

**Answer**:
```java
// String to int
int i = Integer.parseInt("123");

// int to String
String s = String.valueOf(123);
String s2 = Integer.toString(123);
String s3 = "" + 123;

// String to char[]
char[] chars = str.toCharArray();

// char[] to String
String s = new String(chars);
String s2 = String.valueOf(chars);
```

### Q11: What is the difference between replace() and replaceAll()?

**Answer**:
- `replace(char, char)`: Simple character replacement, no regex
- `replace(CharSequence, CharSequence)`: Simple text replacement
- `replaceAll(String, String)`: Regex-based replacement

```java
str.replace('a', 'b');      // No regex
str.replaceAll("a+", "b");  // Regex
str.replaceAll("[aeiou]", "*"); // Regex
```

### Q12: How does String.equals() handle null?

**Answer**:
- `str.equals(null)` returns false
- `null.equals(str)` throws NullPointerException

```java
String s = "Hello";
s.equals(null); // false
null.equals(s); // NullPointerException

// Safe comparison
Objects.equals(s, null); // false
```

### Q13: What is the String pool size limit?

**Answer**:
- Pre-Java 7: PermGen space (default 32MB, max 128MB)
- Java 7+: Heap space (configurable via -XX:StringTableSize)
- Default: 60,013 buckets (Java 8+)
- Can be increased for better performance with many interned strings

### Q14: How do you handle Unicode in Java strings?

**Answer**:
- Java strings are UTF-16 internally
- Use Character.isHighSurrogate() for supplementary characters
- Use codePointAt() and Character.toCodePoint()
- String.length() returns char count, not code point count

```java
String emoji = "😀";
emoji.length(); // 2 (surrogate pair)
emoji.codePointCount(0, emoji.length()); // 1
```

### Q15: What are the best practices for string comparison?

**Answer**:
1. Always use `equals()` for content comparison
2. Use `equalsIgnoreCase()` for case-insensitive
3. Use `Objects.equals()` for null-safe comparison
4. Use `compareTo()` for sorting
5. Never use `==` for content comparison (except interned strings)
6. Consider `String.contentEquals()` for StringBuilder comparison

---

## 27. Exercises

### Beginner

1. Write a method to count vowels and consonants in a string
2. Write a method to reverse a string without using StringBuilder.reverse()
3. Write a method to check if a string is a palindrome
4. Write a method to count occurrences of a character in a string
5. Write a method to remove duplicates from a string

### Intermediate

1. Write a method to compress a string (e.g., "aabbbcc" → "a2b3c2")
2. Write a method to find the first non-repeating character
3. Write a method to check if two strings are anagrams
4. Write a method to find all permutations of a string
5. Write a method to implement basic string rotation check

### Advanced

1. Write a custom string builder with chaining support
2. Write a string interning utility that monitors memory usage
3. Write a CSV parser that handles quoted fields and escape characters
4. Write a regex engine that supports basic patterns
5. Write a string compression algorithm (like LZ77)

---

## 28. Summary

Strings in Java are fundamental to text processing. Key takeaways:

1. **Immutability**: Strings cannot be modified after creation
2. **String Pool**: Optimizes memory by reusing identical strings
3. **StringBuilder**: Efficient for building strings in single-threaded code
4. **StringBuffer**: Thread-safe alternative to StringBuilder
5. **equals()**: Use for content comparison, never `==`
6. **Performance**: Use StringBuilder for loops, avoid concatenation
7. **Thread Safety**: Strings are thread-safe, StringBuilder is not
8. **Memory**: Be cautious with intern() and string pooling

Mastering string handling is essential for writing efficient, secure Java applications.

---

## 29. References

1. Oracle Java Documentation - String Class
2. Effective Java by Joshua Bloch - Item 17: Minimize Mutability
3. Java Performance by Scott Oaks - String Performance
4. Java Concurrency in Practice - Thread Safety
5. OpenJDK Source Code - String.java
6. JVM Specification - String Interning
7. Java Memory Model - Immutability Guarantees
