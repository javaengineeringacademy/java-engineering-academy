# StringBuilder vs StringBuffer

## Overview

StringBuilder and StringBuffer are mutable classes in Java used for efficient string manipulation. Both implement the same `CharSequence` interface and provide similar methods, but differ in thread safety and performance.

## StringBuilder

**Not thread-safe. Faster performance.**

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");
sb.insert(5, ",");
sb.delete(5, 6);
sb.replace(6, 11, "Java");
sb.reverse();
String result = sb.toString();
```

### Key Features:
- Mutable sequence of characters
- No synchronization overhead
- Faster in single-threaded scenarios
- Default capacity: 16 characters
- Grows dynamically (capacity * 2 + 2)

## StringBuffer

**Thread-safe (synchronized). Slower performance.**

```java
StringBuffer sbf = new StringBuffer("Hello");
sbf.append(" World");
sbf.insert(5, ",");
sbf.delete(5, 6);
sbf.replace(6, 11, "Java");
sbf.reverse();
String result = sbf.toString();
```

### Key Features:
- Mutable sequence of characters
- All public methods are synchronized
- Safe for multi-threaded access
- Same growth strategy as StringBuilder
- Slight performance overhead due to synchronization

## Performance Comparison

```java
int iterations = 100000;

// String concatenation (slowest)
long start = System.currentTimeMillis();
String stringConcat = "";
for (int i = 0; i < iterations; i++) {
    stringConcat += "a";
}
long stringTime = System.currentTimeMillis() - start;

// StringBuilder (fastest)
start = System.currentTimeMillis();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < iterations; i++) {
    sb.append("a");
}
long builderTime = System.currentTimeMillis() - start;

// StringBuffer (fast, but slower than StringBuilder)
start = System.currentTimeMillis();
StringBuffer sbf = new StringBuffer();
for (int i = 0; i < iterations; i++) {
    sbf.append("a");
}
long bufferTime = System.currentTimeMillis() - start;
```

### Typical Results:
- String concatenation: ~10,000+ ms
- StringBuilder: ~5-10 ms
- StringBuffer: ~8-15 ms

## When to Use Which

### Use StringBuilder when:
- Building strings in single-threaded applications
- Working with loops that concatenate strings
- Performance is critical
- No concurrent access to the builder expected
- Example: generating HTML, building SQL queries

### Use StringBuffer when:
- Multiple threads access the same buffer
- Thread safety is required
- Can tolerate slight performance overhead
- Example: shared logging buffer, concurrent string building

### Use String when:
- String is immutable and rarely changes
- Using string literals (JVM pool optimization)
- Thread safety is inherently provided by immutability
- Using as HashMap keys or in hash-based collections

## Common Operations

```java
// Creation
StringBuilder sb = new StringBuilder();           // default capacity 16
StringBuilder sb2 = new StringBuilder(100);      // initial capacity 100
StringBuilder sb3 = new StringBuilder("Hello");  // with initial value

// Append
sb.append("Hello");
sb.append(' ');
sb.append(42);           // auto-boxed
sb.append(true);         // auto-boxed

// Insert
sb.insert(0, "Start");   // insert at beginning
sb.insert(5, ",");       // insert at index

// Delete
sb.delete(0, 5);         // delete range
sb.deleteCharAt(0);      // delete single char

// Replace
sb.replace(0, 5, "Hi");  // replace range

// Reverse
sb.reverse();            // reverse entire string

// Search
sb.indexOf("Hello");     // first occurrence
sb.lastIndexOf("l");     // last occurrence
sb.charAt(0);            // character at index

// Capacity
sb.capacity();           // current capacity
sb.length();             // current length
sb.ensureCapacity(200);  // ensure minimum capacity
sb.trimToSize();         // reduce capacity to fit
```

## Capacity vs Length

```java
StringBuilder sb = new StringBuilder(100);
System.out.println(sb.capacity());  // 100
System.out.println(sb.length());    // 0

sb.append("Hello");
System.out.println(sb.capacity());  // 100 (no reallocation needed)
System.out.println(sb.length());    // 5

sb.append(" World Java Programming");
System.out.println(sb.capacity());  // 100 (still fits)
System.out.println(sb.length());    // 28

sb.ensureCapacity(200);
System.out.println(sb.capacity());  // 200 (expanded)

sb.trimToSize();
System.out.println(sb.capacity());  // 28 (trimmed to fit)
```

## Method Chaining

Both StringBuilder and StringBuffer support method chaining:

```java
String result = new StringBuilder()
        .append("Hello")
        .append(" ")
        .append("World")
        .insert(5, ",")
        .replace(6, 11, "Java")
        .delete(0, 5)
        .append("!")
        .toString();
// Result: "Java!"
```

## Common Interview Questions

### 1. What is the difference between StringBuilder and StringBuffer?
**Answer:** StringBuilder is not thread-safe but faster. StringBuffer is thread-safe (synchronized) but slower due to synchronization overhead.

### 2. When should you use StringBuilder over String concatenation?
**Answer:** When building strings in loops or with multiple concatenations. StringBuilder modifies the internal buffer in place, while String concatenation creates new objects.

### 3. How does StringBuilder handle capacity growth?
**Answer:** When capacity is exceeded, it creates a new array with capacity (oldCapacity * 2 + 2) and copies the contents.

### 4. What is the time complexity of append operation?
**Answer:** Amortized O(1) when capacity is sufficient. O(n) when reallocation is needed.

### 5. How do you reverse a string efficiently?
**Answer:** Use StringBuilder.reverse() which operates in O(n) time and modifies the buffer in place.

### 6. Can StringBuilder be used in multi-threaded environments?
**Answer:** It's not recommended. Use StringBuffer for thread safety or synchronize access externally.

### 7. What is the default capacity of StringBuilder?
**Answer:** 16 characters (without initial string) or length of initial string (when initialized with a string).

### 8. How do you avoid capacity reallocation?
**Answer:** Pre-allocate capacity using new StringBuilder(expectedSize) or ensureCapacity().

## Best Practices

1. **Pre-allocate capacity** when you know the approximate final size
2. **Use StringBuilder** in single-threaded applications
3. **Use StringBuffer** only when thread safety is required
4. **Avoid String concatenation** in loops
5. **Use method chaining** for cleaner code
6. **Consider String.intern()** for frequently used strings

## Code References

- `StringBuilderVsBuffer.java` - Comprehensive comparison examples
- `StringBuilderDemo.java` - Basic StringBuilder examples
