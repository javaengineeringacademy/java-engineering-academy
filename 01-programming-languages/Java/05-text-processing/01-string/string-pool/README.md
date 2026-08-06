# String Pool Deep Dive

## What is the String Pool?

The String Pool (also called the String Table) is a special memory area in the Java heap where string literals are stored. It is a hash table that enables string interning — the process of reusing identical string objects instead of creating new ones.

When you write `"hello"` in Java, the JVM does not create a new object every time. Instead, it looks up the pool and returns a reference to the existing string if it already exists. If not, it creates a new entry in the pool.

```java
String a = "hello";
String b = "hello";
System.out.println(a == b); // true — same reference from the pool
```

## Where Does the String Pool Live?

| Java Version | Pool Location |
|---|---|
| Before Java 7 | PermGen (part of Method Area, fixed size) |
| Java 7+ | Heap (normal garbage-collected memory) |
| Java 8+ | Heap (PermGen replaced by Metaspace, but pool stays on heap) |

**Why the change?** PermGen had a fixed size and could cause `OutOfMemoryError: PermGen space`. Moving the pool to the heap allows it to grow dynamically and be garbage collected normally.

## How Strings Enter the Pool

There are two ways strings enter the pool:

### 1. Literal Declaration (Automatic)

```java
String s = "hello"; // Automatically added to pool
```

When the class is loaded, the JVM places `"hello"` in the pool. Every subsequent literal `"hello"` references the same object.

### 2. String.intern() (Explicit)

```java
String s = new String("hello");
String poolRef = s.intern(); // Adds "hello" to pool, returns pool reference
```

`intern()` checks if the string already exists in the pool:
- If yes → returns the existing pool reference
- If no → adds the string to the pool and returns the new reference

## How Strings Leave the Pool

Since Java 7, pool entries are eligible for garbage collection when no references exist outside the pool. This means:

- The pool can shrink over time as unreferenced strings are collected
- The pool does not cause permanent memory growth
- `System.gc()` (if executed) can trigger collection of pool entries

```java
for (int i = 0; i < 1000; i++) {
    String temp = new String("unique_" + i);
    temp.intern();
    // After this loop, these strings have no external references
}
// GC can now collect these pool entries
```

## Performance Implications

### O(1) Lookup

The String pool uses a hash table internally. Both `intern()` and literal pool lookups are O(1) on average, making them very fast even with millions of strings.

### Memory Savings

Without pooling, repeated string values create duplicate objects:

| Scenario | Without Pool | With Pool |
|---|---|---|
| 1000 references to "DATABASE_HOST" | 1000 objects (~48KB) | 1 object + 1000 refs (~80 bytes) |
| Repeated enum values | Many duplicates | Single shared object |
| Config keys | Redundant allocations | Shared references |

### Benchmark Example

```
No pool (new String): 450ms
With pool (literal):  50ms
Speedup: ~9x faster
```

## Memory Implications

### Compact Strings (Java 9+)

Java 9 introduced compact strings which use different byte representations:

- **LATIN1** (1 byte per char): For strings containing only Latin-1 characters
- **UTF16** (2 bytes per char): For strings requiring Unicode support

This reduces memory usage for ASCII-heavy strings in the pool by ~50%.

### Pool Size Tuning

The JVM parameter `-XX:StringTableSize` controls the number of hash buckets:

```bash
# Default is 60013 (prime number)
java -XX:StringTableSize=100003 -jar app.jar

# Larger table = fewer collisions = faster lookups
# Smaller table = less memory overhead
```

## Common Interview Questions

### 1. What is the String Pool?

The String Pool is a memory area in the Java heap where string literals are stored and shared. It enables string interning, where identical strings reference the same object, saving memory and improving performance.

### 2. How Does intern() Work?

`intern()` checks if the string exists in the pool:
- If it exists → returns the existing reference
- If it doesn't → adds it to the pool and returns the new reference

```java
String a = new String("hello");
String b = a.intern();
String c = "hello";
System.out.println(b == c); // true
```

### 3. When is a String in the Pool vs Heap?

| Created With | Location |
|---|---|
| `"hello"` (literal) | Pool |
| `new String("hello")` | Heap (not in pool) |
| `StringBuilder.toString()` | Heap (not in pool) |
| `"hel" + "lo"` (constants) | Pool (constant folding) |
| `a + b` (variables) | Heap |

### 4. Does String Pool Cause Memory Leaks?

No. Since Java 7, pool entries are on the heap and subject to normal garbage collection. When no references exist to a pool entry, it becomes eligible for GC.

However, excessive use of `intern()` on unique strings can temporarily increase heap usage before GC runs.

### 5. How to Tune String Pool Size?

Use `-XX:StringTableSize=N` where N is the number of hash buckets:

```bash
# Increase for applications with many unique strings
java -XX:StringTableSize=200003 -jar app.jar

# Decrease for memory-constrained environments
java -XX:StringTableSize=10009 -jar app.jar
```

Default value is 60013. Larger values improve lookup speed but use more memory.

### 6. What is the Difference Between == and .equals() for Strings?

- `==` compares references (memory addresses)
- `.equals()` compares content (character sequence)

```java
String a = new String("hello");
String b = new String("hello");
System.out.println(a == b);      // false (different objects)
System.out.println(a.equals(b)); // true (same content)
```

### 7. Why is the String Pool on the Heap Since Java 7?

PermGen had a fixed size (default ~80MB) and could not be garbage collected efficiently. Moving the pool to the heap:
- Allows dynamic sizing
- Enables normal garbage collection
- Prevents `OutOfMemoryError: PermGen space`

### 8. Can You Force Garbage Collection of the String Pool?

You cannot force GC, but you can suggest it with `System.gc()`. The JVM decides when to actually run GC. Pool entries without external references become eligible for collection automatically.

## Key Takeaways

1. **Literal strings** are automatically pooled by the JVM
2. **`new String()`** creates heap objects outside the pool
3. **`intern()`** explicitly adds strings to the pool
4. **Pool lives on heap** since Java 7, entries are GC'd when unreferenced
5. **Always use `.equals()`** for string content comparison
6. **Compact strings** (Java 9+) reduce memory for ASCII strings
7. **Tune with `-XX:StringTableSize`** for performance optimization

## Related Topics
- [Immutability](../immutability/) — Why String is immutable
- Java Memory Model — Where String pool lives
- String Source Code — How String is implemented
- Wrapper Classes — Boxing with strings
