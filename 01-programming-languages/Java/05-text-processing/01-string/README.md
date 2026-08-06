# String in Java

The `String` class is one of the most commonly used classes in Java.
It represents a sequence of characters and provides numerous methods
for text manipulation.

## Key Characteristics

### Immutability

Strings in Java are immutable. Once a String object is created, its
value cannot be changed. Any operation that appears to modify a String
actually creates a new String object.

```java
String str = "Hello";
str = str + " World"; // Creates a new String object
// Original "Hello" is still in memory until garbage collected
```

**Why Immutability?**
- Thread safety (no synchronization needed)
- Security (String is used for class loading, network connections)
- Hash code caching (hash code can be computed once and cached)
- String pool optimization

### String Pool

Java maintains a special memory area called the String Pool (or
intern pool) where String literals are stored. This allows JVM to
reuse String objects for identical string literals.

```java
String s1 = "Hello"; // Stored in String Pool
String s2 = "Hello"; // Reuses existing "Hello" from pool

String s3 = new String("Hello"); // Creates new object in heap
String s4 = s3.intern(); // Returns reference to pool
```

## Common Methods

### Length and Empty Check

```java
String str = "Hello, World!";

str.length()        // 13
str.isEmpty()       // false
str.isBlank()       // false (Java 11+)
```

### Character Access

```java
String str = "Hello";

str.charAt(0)       // 'H'
str.charAt(4)       // 'o'
str.indexOf('l')    // 2
str.lastIndexOf('l') // 3
```

### Substring and Extraction

```java
String str = "Hello, World!";

str.substring(7)        // "World!"
str.substring(0, 5)     // "Hello"
str.substring(7, 12)    // "World"
```

### Search and Contains

```java
String str = "Hello, World!";

str.contains("World")      // true
str.startsWith("Hello")    // true
str.endsWith("World!")     // true
str.indexOf("World")       // 7
str.lastIndexOf("l")       // 10
```

### Modify and Transform

```java
String str = "  Hello, World!  ";

str.trim()              // "Hello, World!"
str.strip()             // "Hello, World!" (Java 11+)
str.toLowerCase()       // "  hello, world!  "
str.toUpperCase()       // "  HELLO, WORLD!  "
str.replace('l', 'L')  // "  HeLLo, WorLd!  "
```

### Split and Join

```java
// Split
String csv = "apple,banana,cherry";
String[] fruits = csv.split(",");
// ["apple", "banana", "cherry"]

// Join
String joined = String.join(" - ", fruits);
// "apple - banana - cherry"
```

### Format

```java
String name = "Alice";
int age = 30;

// String.format
String formatted = String.format("Name: %s, Age: %d", name, age);
// "Name: Alice, Age: 30"

// format specifiers
String.format("%10s", "right")    // "     right" (right-aligned)
String.format("%-10s", "left")    // "left      " (left-aligned)
String.format("%05d", 42)         // "00042"
```

## Comparison

### == vs equals()

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");
String s4 = s3.intern();

// == compares references (memory addresses)
s1 == s2             // true (same pool reference)
s1 == s3             // false (different objects)
s1 == s4             // true (intern returns pool reference)

// equals() compares content
s1.equals(s2)        // true
s1.equals(s3)        // true
s1.equals(s4)        // true
```

### Case-Insensitive Comparison

```java
String s1 = "Hello";
String s2 = "hello";

s1.equalsIgnoreCase(s2)    // true
s1.compareToIgnoreCase(s2) // 0
```

### Comparing with compareTo

```java
String a = "apple";
String b = "banana";

a.compareTo(b)          // negative (apple < banana)
b.compareTo(a)          // positive (banana > apple)
a.compareToIgnoreCase(b) // negative (case-insensitive)
```

## Performance Considerations

### String Concatenation

```java
// BAD: Creates multiple intermediate String objects
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i; // Each + creates a new String
}

// GOOD: Uses StringBuilder internally
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

### String.valueOf vs toString

```java
// String.valueOf(null) returns "null"
String.valueOf(null)        // "null"

// null.toString() throws NullPointerException
// null.toString()          // NullPointerException
```

### intern() Method

```java
// Using intern() can save memory for repeated strings
// But can cause memory leaks if overused

String s1 = new String("Hello").intern();
String s2 = "Hello";
s1 == s2    // true
```

## Useful Static Methods

```java
// String to array
char[] chars = "Hello".toCharArray();
// ['H', 'e', 'l', 'l', 'o']

// Array to String
String str = new String(new char[]{'H', 'i'});

// Null-safe
String.valueOf(42)           // "42"
String.valueOf(3.14)         // "3.14"
String.valueOf(true)         // "true"

// Join
String.join(", ", "a", "b", "c")  // "a, b, c"
```

## Summary

- Strings are immutable in Java
- String literals are stored in the String Pool
- Use equals() for content comparison, not ==
- Use StringBuilder for concatenation in loops
- Be mindful of performance with string operations
- Use String.valueOf() for null-safe conversions
