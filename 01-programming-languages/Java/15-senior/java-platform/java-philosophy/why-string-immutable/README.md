# Why String is Immutable?

## Security (Class Loading, Network Connections)

### Class Loading

Strings are used extensively in class loading:

```java
// JVM uses strings for:
// - Package names: "java.lang.String"
// - Class names: "com.example.MyClass"
// - Method names: "toString"
// - Field names: "length"

// If strings were mutable, attackers could:
String className = "java.lang.String";
className.charAt(0) = 'j'; // Change to different class
// This would break the entire JVM
```

### Network Connections

```java
// Strings are used in network operations
URL url = new URL("https://example.com");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();

// If URL string was mutable, an attacker could:
String host = "example.com";
// Attacker changes to "evil.com"
// Connection now goes to malicious server
```

### Database Queries

```java
// SQL queries use strings
String query = "SELECT * FROM users WHERE id = ?";
// If mutable, attacker could change to:
// "SELECT * FROM users WHERE id = 1; DROP TABLE users"
```

### File Paths

```java
// File operations use strings
Path path = Paths.get("/safe/directory/file.txt");
// If mutable, attacker could change to:
// "/etc/passwd" or "/etc/shadow"
```

## Caching (String Pool)

### The String Pool

```java
// Java maintains a pool of strings
String s1 = "Hello"; // Creates in pool
String s2 = "Hello"; // Reuses from pool

// This is only possible because strings are immutable
// If mutable, s2 could change s1's value
```

### Interning

```java
// Explicit interning
String s3 = new String("Hello").intern();
// Returns reference from pool

// This saves memory for repeated strings
// But strings must be immutable for this to be safe
```

### Performance Benefits

- **Memory savings**: No duplicate strings
- **Fast comparison**: Can compare references instead of content
- **Cache efficiency**: Hash codes are stable

## Thread Safety (Safe Publication)

### Inherent Thread Safety

```java
// Immutable objects are inherently thread-safe
String message = "Hello";

// Thread 1
System.out.println(message); // Safe

// Thread 2
System.out.println(message); // Safe
// No synchronization needed
```

### Safe Publication

```java
// Safe to share without synchronization
public class Config {
    private final String databaseUrl;
    
    public Config(String url) {
        this.databaseUrl = url; // Safe to share
    }
    
    public String getDatabaseUrl() {
        return databaseUrl; // Safe to return
    }
}
```

### No Defensive Copies

```java
// No need for defensive copies
public class User {
    private final String name;
    
    public User(String name) {
        this.name = name; // No need to copy
    }
    
    public String getName() {
        return name; // No need to copy
    }
}
```

## Hash Code Caching

### Cached Hash Code

```java
// String's hash code is computed once and cached
public final class String {
    private int hash; // Cached hash code
    
    public int hashCode() {
        int h = hash;
        if (h == 0 && !hashIsZero) {
            // Compute hash
            h = computeHashCode();
            hash = h;
        }
        return h;
    }
}
```

### Benefits for HashMap

```java
// HashMap<String, ?> is very fast
Map<String, Integer> map = new HashMap<>();
map.put("Hello", 1);

// When looking up "Hello":
// 1. Hash code is already cached (fast)
// 2. Can compare references for same string (fast)
// 3. Only compare content when hash codes match
```

### Performance Implications

```java
// Without caching: O(n) for every lookup
// With caching: O(1) for cached hash codes

// This makes string-based maps extremely fast
```

## Performance Implications

### String Concatenation

```java
// Immutable strings mean concatenation creates new objects
String s1 = "Hello";
String s2 = "World";
String s3 = s1 + " " + s2; // Creates new String object

// This is inefficient in loops
for (int i = 0; i < 1000; i++) {
    s = s + i; // Creates 1000 new String objects
}
```

### StringBuilder for Loops

```java
// Use StringBuilder for loops
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i); // Modifies same object
}
String result = sb.toString(); // One final String
```

### Compiler Optimization

```java
// Compiler optimizes simple concatenation
String s = "Hello" + " " + "World"; // Becomes "Hello World"

// But complex concatenation is not optimized
String s = "Hello" + variable + "World"; // Creates StringBuilder
```

### Memory Overhead

```java
// Each string has overhead:
// - char[] or byte[] array
// - hash code cache
// - length field
// - object header

// This is why StringBuilder exists for mutable strings
```

## Alternative Designs

### Mutable String (Rejected)

```java
// hypothetical mutable string
class MutableString {
    private char[] value;
    
    public void append(char c) {
        // Could cause problems
        // 1. Hash code changes
        // 2. Thread safety issues
        // 3. Security vulnerabilities
    }
}
```

### StringBuilder (The Solution)

```java
// StringBuilder is the mutable alternative
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
String result = sb.toString(); // Convert to immutable
```

### CharSequence Interface

```java
// CharSequence provides read-only access
CharSequence cs = "Hello";
CharSequence cs2 = new StringBuilder("World");

// Can read but not modify through CharSequence
```

## Common Misconceptions

### 1. "String is immutable, so it's always faster"

**False**: Immutable strings have overhead for concatenation.

### 2. "String pool saves all memory"

**False**: Only saves memory for repeated string literals.

### 3. "String is thread-safe because it's immutable"

**True**: But this doesn't mean all string operations are thread-safe.

### 4. "You can't modify a String"

**True**: You can create new strings based on the original.

## Best Practices

### 1. Use StringBuilder for Loops

```java
// Bad
String result = "";
for (String s : list) {
    result += s;
}

// Good
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();
```

### 2. Use String.intern() Carefully

```java
// Only for known, limited set of strings
String status = status.intern(); // Can save memory

// Don't intern user input - memory leak risk
```

### 3. Consider StringBuffer for Thread Safety

```java
// StringBuffer is synchronized (slower)
StringBuffer sb = new StringBuffer();
// Use only when shared across threads
```

### 4. Use String.join() for Simple Cases

```java
// Modern Java
String result = String.join(", ", list);

// Before Java 8
StringJoiner joiner = new StringJoiner(", ");
for (String s : list) {
    joiner.add(s);
}
String result = joiner.toString();
```

## Overview

String immutability means once a `String` object is created, its content cannot be changed. Every operation (`concat`, `substring`, `replace`) creates a new `String` object. This is a fundamental design decision in Java driven by security (class loading, network connections), performance (string pooling, hash code caching), and thread safety. The trade-off is concatenation overhead, which `StringBuilder` mitigates.

## Why This Concept Exists

String immutability exists because strings are the most security-sensitive data type in Java. They're used in class loading (`Class.forName()`), network connections (URLs), file paths, and database queries. If strings were mutable, an attacker could modify a class name after validation, redirect a URL after authentication, or corrupt a SQL query after parameterization. Immutability also enables the string pool (deduplication), hash code caching (fast `HashMap` lookups), and inherent thread safety.

## Internal Working

### String Pool (Intern Pool)

```java
// String pool is a hash table in native memory (Metaspace)
String s1 = "Hello"; // Creates in pool, returns reference
String s2 = "Hello"; // Returns existing reference from pool

// Pool implementation (simplified):
// - Located in Metaspace (not heap) since Java 7
// - Managed by StringTable (fixed-size hash table)
// - Default size: 60013 buckets (Java 8)
// - Use -XX:StringTableSize=N to tune

// Interning
String s3 = new String("Hello").intern(); // Forces into pool
// s3 == s1 → true (same reference)
```

### String Internal Representation

```java
// Java 8: char[] based
public final class String {
    private final char[] value; // UTF-16 encoded
    private final int hash;    // Cached hash code
}

// Java 9+: Compact strings (byte[] + coder)
public final class String {
    private final byte[] value;  // Latin1 or UTF-16
    private final byte coder;    // LATIN1=0, UTF16=1
    private final int hash;
}

// Compact strings save memory for ASCII-only strings
// Latin1: 1 byte per char (vs 2 bytes for UTF-16)
```

### Immutability Enforcement

```java
// String is final — cannot be extended
public final class String { ... }

// All fields are final — set once in constructor
private final char[] value;
private final int hash;

// No setter methods — no way to modify content
// All "modification" methods return new String objects
public String concat(String str) {
    // Creates and returns new String
    char[] result = ...;
    return new String(result, true);
}
```

## Examples

### String Pool Verification

```java
public class StringPoolDemo {
    public static void main(String[] args) {
        // Compile-time constants: pooled
        String s1 = "Hello";
        String s2 = "Hello";
        System.out.println(s1 == s2); // true (same reference)

        // Runtime concatenation: NOT pooled
        String s3 = "Hel" + "lo";  // Pooled (compiler optimizes)
        String s4 = "Hel";
        String s5 = s4 + "lo";     // NOT pooled (runtime concat)
        System.out.println(s3 == s5); // false

        // intern() forces pooling
        String s6 = s5.intern();
        System.out.println(s1 == s6); // true

        // new String always creates new object
        String s7 = new String("Hello");
        System.out.println(s1 == s7); // false
        System.out.println(s1.equals(s7)); // true
    }
}
```

### Performance: StringBuilder vs String Concatenation

```java
// BAD: O(n²) string concatenation in loop
String result = "";
for (int i = 0; i < 100_000; i++) {
    result += i; // Creates new String each iteration
}
// Time: ~5000ms for 100K iterations

// GOOD: O(n) with StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100_000; i++) {
    sb.append(i); // Modifies same buffer
}
String result = sb.toString();
// Time: ~5ms for 100K iterations

// BETTER: String.join for simple cases
String result = IntStream.range(0, 100_000)
    .mapToObj(String::valueOf)
    .collect(Collectors.joining());
```

### Hash Code Caching Demo

```java
// String hash code is computed once and cached
// This makes HashMap<String, ?> very fast

Map<String, Integer> map = new HashMap<>();
for (int i = 0; i < 1_000_000; i++) {
    map.put("user_" + i, i); // Hash computed once per key
}

// Lookup: hash code already cached
long start = System.nanoTime();
for (int i = 0; i < 1_000_000; i++) {
    map.get("user_" + i);
}
long time = System.nanoTime() - start;
// ~50ms for 1M lookups (hash code cached)
```

### Thread Safety Demonstration

```java
// Immutable String is inherently thread-safe
// No synchronization needed for sharing

public class Config {
    private final String host;  // Immutable, safe to share
    private final int port;

    public Config(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // No defensive copies needed
    public String getHost() {
        return host; // Caller can't modify internal state
    }
}

// Contrast with mutable StringBuilder (NOT thread-safe)
StringBuilder sb = new StringBuilder("Hello");
// Multiple threads modifying sb = data race
// Must use synchronized or StringBuffer
```

## Performance

### Memory Comparison

| Operation | String | StringBuilder | Savings |
|-----------|--------|---------------|---------|
| 100 concatenations | ~100 objects, 20KB | 1 object, ~1KB | 98% |
| 1000 concatenations | ~1000 objects, 200KB | 1 object, ~10KB | 95% |
| 10000 concatenations | ~10000 objects, 2MB | 1 object, ~100KB | 95% |

### String Pool Memory Savings

```java
// Typical application with 100K strings
// Without pooling: 100K * 40 bytes = 4MB
// With pooling: 10K unique * 40 bytes = 400KB (90% savings)

// JVM tuning
-XX:StringTableSize=1000003  // Increase bucket count
-XX:CompactStrings           // Enable compact strings (default: true)
```

### Concatenation Performance

| Scenario | Time (100K items) | GC Pressure |
|----------|-------------------|-------------|
| String += | 5000ms | High (100K objects) |
| StringBuilder | 5ms | Low (1 object) |
| String.join | 8ms | Low |
| StringJoiner | 8ms | Low |

## Pitfalls

### 1. String Concatenation in Loops

```java
// BAD: O(n²) — creates n intermediate String objects
String csv = "";
for (String item : items) {
    csv += item + ","; // Each += creates new String
}

// GOOD: O(n) — single buffer
String csv = items.stream().collect(Collectors.joining(","));

// BETTER: StringBuilder with initial capacity
StringBuilder sb = new StringBuilder(items.size() * 10);
for (String item : items) {
    sb.append(item).append(",");
}
String csv = sb.toString();
```

### 2. Misusing String.intern()

```java
// BAD: Interning user input (memory leak)
String userInput = scanner.nextLine();
String interned = userInput.intern(); // Leaks into pool forever

// GOOD: Only intern known, limited sets
private static final Map<String, String> STATUS_POOL = Map.of(
    "ACTIVE", "ACTIVE",
    "INACTIVE", "INACTIVE",
    "PENDING", "PENDING"
);
String status = STATUS_POOL.getOrDefault(rawStatus, rawStatus);
```

### 3. Comparing Strings with ==

```java
// BAD: Reference comparison
if (status == "ACTIVE") { ... } // Unreliable

// GOOD: Content comparison
if ("ACTIVE".equals(status)) { ... }

// BETTER: Use enum for fixed sets
enum Status { ACTIVE, INACTIVE, PENDING }
```

### 4. Substring Performance Myths

```java
// Java 7u6+: substring() creates new String (no shared buffer)
String sub = longString.substring(10, 20); // New String object

// Pre-Java 7u6: substring shared buffer (memory leak risk)
// The old behavior was removed for safety
```

### 5. Ignoring Compact Strings

```java
// Java 9+ uses compact strings by default
// Latin1 strings: 1 byte per char
// UTF-16 strings: 2 bytes per char

// Check which encoding is used
String s = "Hello";
// In Java 9+: byte[] with LATIN1 coder
// In Java 8: char[] (always 2 bytes per char)

// Disable compact strings (not recommended)
// -XX:-CompactStrings
```

## References

- [Java Language Specification: String](https://docs.oracle.com/javase/specs/jls/se17/html/jls-3.html#jls-3.10.5)
- [OpenJDK Source: String.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/String.java)
- *Effective Java* by Joshua Bloch — Item 17: Minimize mutability
- *Java Performance* by Scott Oaks — Chapter on Strings
- [Oracle: Compact Strings](https://openjdk.org/jeps/254)
