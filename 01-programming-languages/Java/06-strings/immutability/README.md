# String Immutability Deep Dive

## How is String Made Immutable?

String is made immutable through two key design decisions:

### 1. Final Class

```java
public final class String { ... }
```

The `final` keyword prevents subclassing. No class can extend String or override its methods.

### 2. Final Internal Array

```java
// Java 9+
private final byte[] value;
private final byte coder;

// Before Java 9
private final char[] value;
```

The `final` keyword on the array means the reference cannot be reassigned. Combined with providing no methods to modify the array contents, String becomes truly immutable.

### 3. No Mutating Methods

String provides no methods that modify the internal array. Methods like `concat()`, `replace()`, and `substring()` always return **new** String objects rather than modifying the original.

```java
String s = "hello";
String s2 = s.concat(" world"); // Returns new String
System.out.println(s); // "hello" — unchanged
System.out.println(s2); // "hello world" — new object
```

## Why is String Immutable?

### 1. Thread Safety

Immutable objects are inherently thread-safe. Multiple threads can read the same String without synchronization, and the value will never change unexpectedly.

```java
final String shared = "Thread Safe";
// Any number of threads can safely read 'shared' without locks
```

### 2. Security

Java uses Strings extensively for security-sensitive operations:
- **Class loading**: Class names and package names are Strings
- **Network connections**: Hostnames and URLs
- **File paths**: Access control decisions
- **Database connections**: URLs and credentials

If Strings were mutable, an attacker could modify a class name after validation, potentially loading malicious classes.

### 3. Hash Code Caching

String computes its hash code once and caches it. This makes HashMap and HashSet operations extremely fast:

```java
String key = "important";
int hash = key.hashCode(); // Computed once, cached forever
// Subsequent calls return the cached value
```

This caching is only safe because String is immutable — the hash code will never change.

### 4. String Pool Safety

The String pool allows multiple references to share the same String object. This is only safe because Strings cannot be modified. If `pooled1 = "hello"` and `pooled2 = "hello"` share the same object, modifying one would affect the other.

### 5. Compile-Time Constants

String literals are compile-time constants. The compiler can optimize them through constant folding:

```java
final String a = "Hello";
final String b = " World";
String c = a + b; // Compiler optimizes to "Hello World"
```

This optimization is only possible because the values cannot change at runtime.

## Benefits of Immutability

| Benefit | Description |
|---|---|
| Thread Safety | No synchronization needed for read access |
| Security | Prevents modification of security-sensitive strings |
| Hash Code Caching | Single computation, reused forever |
| Pool Safety | Safe sharing of identical strings |
| Constant Folding | Compiler optimization for compile-time constants |

## Costs of Immutability

### 1. Memory Waste from Concatenation

Each concatenation creates a new String object. Old objects become garbage:

```java
String result = "";
for (int i = 0; i < 1000; i++) {
    result = result + i; // Creates 1000 temporary String objects!
}
```

**Solution**: Use `StringBuilder` for loops and repeated concatenation:

```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i); // Modifies internal buffer, no new objects
}
String result = sb.toString();
```

### 2. StringBuilder/StringBuffer Overhead

For frequent modifications, you must use `StringBuilder` or `StringBuffer`, adding complexity:

```java
// StringBuilder (not thread-safe, faster)
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
sb.insert(5, ",");

// StringBuffer (thread-safe, slower)
StringBuffer buf = new StringBuffer("Hello");
buf.append(" World");
buf.insert(5, ",");
```

## How to Create Immutable Classes

### Pattern 1: Final Class with Final Fields

```java
public final class Money {
    private final String currency;
    private final double amount;

    public Money(String currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() { return currency; }
    public double getAmount() { return amount; }
    // No setters
}
```

### Pattern 2: Defensive Copies

For mutable fields, use defensive copies to preserve immutability:

```java
public final class DateRange {
    private final List<String> dates;

    public DateRange(List<String> dates) {
        // Defensive copy
        this.dates = new ArrayList<>(dates);
    }

    public List<String> getDates() {
        // Return copy, not original
        return new ArrayList<>(dates);
    }
}
```

### Pattern 3: Unmodifiable Collections

```java
// Java 9+: Truly immutable
List<String> immutable = List.of("a", "b", "c");

// Pre-Java 9: Unmodifiable view (not truly immutable)
List<String> view = Collections.unmodifiableList(mutableList);
```

## Common Interview Questions

### 1. How is String Immutable?

String is immutable through:
- `final class` — cannot be subclassed
- `final byte[] value` — internal array reference cannot be reassigned
- No methods that modify the internal array
- All modification methods return new String objects

### 2. Why is String Immutable?

Five primary reasons:
1. **Thread safety** — safe to share across threads
2. **Security** — prevents modification of security-sensitive strings
3. **Hash code caching** — single computation, reused forever
4. **Pool safety** — safe sharing of identical strings
5. **Constant folding** — enables compiler optimization

### 3. What Are the Benefits of Immutability?

- Thread safety without synchronization
- Security for class loading, network connections, file paths
- Efficient hash code caching for HashMap/HashSet
- Memory savings through String pool
- Compiler optimization through constant folding

### 4. What Are the Costs?

- **Memory waste**: Concatenation creates new objects
- **Modification overhead**: Must use StringBuilder/StringBuffer
- **Garbage pressure**: Temporary objects increase GC overhead

### 5. How to Create an Immutable Class?

1. Make the class `final`
2. Make all fields `private final`
3. Provide no setters
4. Use defensive copies for mutable fields
5. Return copies from getter methods

### 6. Is StringBuilder Mutable?

Yes. `StringBuilder` is mutable — its `append()`, `insert()`, `delete()` methods modify the internal buffer without creating new objects. This makes it efficient for string manipulation.

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World"); // Same object, modified
System.out.println(sb); // "Hello World"
```

### 7. Is StringBuffer Mutable?

Yes. `StringBuffer` is also mutable, similar to `StringBuilder` but with synchronized methods. Use `StringBuffer` when multiple threads need to modify the same buffer; use `StringBuilder` for single-threaded scenarios (faster).

```java
StringBuffer buf = new StringBuffer("Hello");
buf.append(" World"); // Same object, synchronized
System.out.println(buf); // "Hello World"
```

## String vs StringBuilder vs StringBuffer

| Feature | String | StringBuilder | StringBuffer |
|---|---|---|---|
| Mutable | No | Yes | Yes |
| Thread-safe | Yes (immutable) | No | Yes (synchronized) |
| Performance | Slow for concatenation | Fast | Slower (sync overhead) |
| Use case | Constant values | Single-thread modification | Multi-thread modification |

## Key Takeaways

1. **String is immutable** via `final class` + `final byte[] value`
2. **Immutability provides** thread safety, security, caching, pool safety
3. **Costs include** memory waste and modification overhead
4. **Use StringBuilder** for string concatenation in loops
5. **Use StringBuffer** when thread-safe modification is needed
6. **Create immutable classes** with final fields, no setters, defensive copies
7. **Use `List.of()`** (Java 9+) for truly immutable collections
