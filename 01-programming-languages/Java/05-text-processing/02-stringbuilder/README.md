# StringBuilder in Java

`StringBuilder` is a mutable sequence of characters that provides an
efficient way to build strings when you need to make frequent
modifications without creating new String objects.

## Key Characteristics

### Mutable

Unlike String, StringBuilder is mutable. Operations modify the same
object rather than creating new ones.

```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World"); // Same object modified
sb.insert(5, ",");    // Same object modified
sb.toString();        // "Hello, World!"
```

### Not Thread-Safe

StringBuilder is NOT thread-safe. Do not use it when multiple threads
access the same StringBuilder instance. Use StringBuffer instead.

## Common Operations

### Creating StringBuilder

```java
// Default capacity (16 characters)
StringBuilder sb1 = new StringBuilder();

// With initial string
StringBuilder sb2 = new StringBuilder("Hello");

// With initial capacity
StringBuilder sb3 = new StringBuilder(100);
```

### Append

```java
StringBuilder sb = new StringBuilder();

sb.append("Hello");         // "Hello"
sb.append(' ');             // "Hello "
sb.append(42);              // "Hello 42"
sb.append(3.14);            // "Hello 423.14"
sb.append(true);            // "Hello 423.14true"
sb.append(new char[]{'H','i'}); // "Hello 423.14trueHi"
```

### Insert

```java
StringBuilder sb = new StringBuilder("HelloWorld");

sb.insert(5, " ");          // "Hello World"
sb.insert(0, ">>>");        // ">>>Hello World"
sb.insert(8, 123);          // ">>>Hello 123World"
```

### Delete

```java
StringBuilder sb = new StringBuilder("Hello, World!");

sb.delete(5, 6);           // "Hello World!" (removed comma)
sb.deleteCharAt(0);         // "ello World!"
sb.delete(0, sb.length());  // "" (cleared)
```

### Replace and Reverse

```java
StringBuilder sb = new StringBuilder("Hello");

sb.replace(0, 5, "World");  // "World"
sb.reverse();               // "dlroW"
```

### Character Operations

```java
StringBuilder sb = new StringBuilder("Hello");

sb.charAt(0);              // 'H'
sb.setCharAt(0, 'J');      // "Jello"
sb.indexOf("llo");          // 2
```

### Capacity and Length

```java
StringBuilder sb = new StringBuilder(100);

sb.capacity();             // 100
sb.length();               // 0

sb.append("Hello");
sb.capacity();             // 100
sb.length();               // 5

sb.trimToSize();           // Optimizes capacity to length
sb.capacity();             // 5
```

## StringBuilder vs StringBuffer

| Feature | StringBuilder | StringBuffer |
|---------|---------------|--------------|
| Thread Safety | No | Yes |
| Performance | Faster | Slower |
| Synchronization | None | All methods |
| Use Case | Single-threaded | Multi-threaded |

### When to Use StringBuilder

- Single-threaded applications
- Loop string concatenation
- Building SQL queries
- Constructing HTML/XML

```java
// Good: StringBuilder in single-threaded loop
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(" ");
}
```

### When to Use StringBuffer

- Multi-threaded applications
- Shared mutable string state
- When thread safety is required

```java
// Good: StringBuffer for shared state
StringBuffer sharedBuffer = new StringBuffer();
// Access from multiple threads (synchronized)
```

## Performance Comparison

### String vs StringBuilder

```java
// BAD: String concatenation
long start = System.nanoTime();
String s = "";
for (int i = 0; i < 100000; i++) {
    s += "a";
}
long stringTime = System.nanoTime() - start;

// GOOD: StringBuilder
start = System.nanoTime();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100000; i++) {
    sb.append("a");
}
String result = sb.toString();
long builderTime = System.nanoTime() - start;

// StringBuilder is typically 10-100x faster
```

### StringBuilder vs StringBuffer

```java
// StringBuilder (no synchronization overhead)
long start = System.nanoTime();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100000; i++) {
    sb.append("a");
}
long builderTime = System.nanoTime() - start;

// StringBuffer (synchronization overhead)
start = System.nanoTime();
StringBuffer buffer = new StringBuffer();
for (int i = 0; i < 100000; i++) {
    buffer.append("a");
}
long bufferTime = System.nanoTime() - start;

// StringBuilder is typically 2-5x faster
```

## Best Practices

1. **Specify initial capacity** if you know approximate size
2. **Use StringBuilder** for loop concatenation
3. **Use StringBuffer** only when thread safety is needed
4. **Avoid unnecessary conversions** between String and StringBuilder
5. **Call toString()** only when you need the final String

```java
// Good: Pre-allocate capacity
StringBuilder sb = new StringBuilder(1024);

// Good: Chain operations
String result = new StringBuilder()
    .append("Hello")
    .append(" ")
    .append("World")
    .toString();
```

## Summary

- StringBuilder is mutable and faster than String for modifications
- StringBuilder is NOT thread-safe
- Use StringBuffer when thread safety is required
- Specify initial capacity for better performance
- Use StringBuilder for single-threaded string building
