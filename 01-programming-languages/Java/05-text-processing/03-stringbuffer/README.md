# StringBuffer in Java

`StringBuffer` is a thread-safe, mutable sequence of characters.
It is similar to `StringBuilder` but provides synchronization for
safe use in multi-threaded environments.

## Key Characteristics

### Thread-Safe (Synchronized)

All methods in StringBuffer are synchronized, making it safe to use
from multiple threads without external synchronization.

```java
StringBuffer sb = new StringBuffer("Hello");
// Safe to call from multiple threads
sb.append(" World"); // Internally synchronized
```

### Mutable

Like StringBuilder, StringBuffer is mutable. Modifications occur on
the same object without creating new String instances.

```java
StringBuffer sb = new StringBuffer("Hello");
sb.append(" World"); // Same object, modified in place
```

## Common Operations

### Creating StringBuffer

```java
StringBuffer sb1 = new StringBuffer();           // Default
StringBuffer sb2 = new StringBuffer("Hello");    // With string
StringBuffer sb3 = new StringBuffer(100);         // With capacity
```

### Modifying Content

```java
StringBuffer sb = new StringBuffer("Hello");

sb.append(" World");     // "Hello World"
sb.insert(5, "!");       // "Hello! World"
sb.delete(5, 6);         // "Hello World"
sb.replace(6, 11, "Java"); // "Hello Java"
sb.reverse();             // "avaJ olleH"
```

### Accessing Content

```java
StringBuffer sb = new StringBuffer("Hello");

sb.charAt(0);            // 'H'
sb.indexOf("ll");        // 2
sb.substring(2);         // "llo"
sb.length();             // 5
sb.capacity();           // 21 (default 16 + initial string length)
```

## When to Use StringBuffer vs StringBuilder

### Use StringBuilder When:

- **Single-threaded application** (most common case)
- **Performance is critical** (no synchronization overhead)
- **Building strings in loops**
- **No concurrent access**

```java
// Single-threaded: StringBuilder is faster
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
```

### Use StringBuffer When:

- **Multi-threaded application**
- **Shared mutable string state**
- **Thread safety is required**
- **Concurrent access to same buffer**

```java
// Multi-threaded: StringBuffer is safe
StringBuffer shared = new StringBuffer();
// Thread 1 and Thread 2 can safely modify 'shared'
```

## Performance Implications

### Synchronization Overhead

StringBuffer's synchronization adds overhead. For single-threaded
code, StringBuilder is typically 2-5x faster.

```java
// StringBuilder (no sync)
long start = System.nanoTime();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100000; i++) {
    sb.append("a");
}
long builderTime = System.nanoTime() - start;

// StringBuffer (synchronized)
start = System.nanoTime();
StringBuffer buffer = new StringBuffer();
for (int i = 0; i < 100000; i++) {
    buffer.append("a");
}
long bufferTime = System.nanoTime() - start;
```

### Capacity Management

Both StringBuilder and StringBuffer manage capacity automatically.
You can optimize by pre-allocating capacity.

```java
// Pre-allocate for better performance
StringBuffer sb = new StringBuffer(1024);
// Avoids resizing during append operations
```

## Summary

- StringBuffer is thread-safe (all methods synchronized)
- Use StringBuffer for multi-threaded string building
- Use StringBuilder for single-threaded applications (faster)
- Pre-allocate capacity when you know the approximate size
- StringBuffer is slower due to synchronization overhead
