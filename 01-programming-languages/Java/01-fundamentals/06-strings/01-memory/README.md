# Strings Memory Model

## String Pool Memory

The String Pool (intern pool) is a special memory area within the heap for storing unique string literals.

### Pool Structure

```
String Pool (Heap):
┌─────────────────────────────────────┐
│ "Hello" ← s1, s2                   │
│ "World" ← s3                       │
│ "Java"  ← s4, s5                   │
└─────────────────────────────────────┘
```

### String Object Memory

```java
String str = "Hello";

// Memory breakdown:
// Stack: 8 bytes (reference)
// Heap: String object (~40 bytes)
//   - Object header: 16 bytes
//   - Hash code: 4 bytes
//   - Count: 4 bytes
//   - Offset: 4 bytes
//   - char[] reference: 8 bytes
// Heap: char[] array (~10 bytes)
//   - Object header: 16 bytes
//   - Length: 4 bytes
//   - Characters: 2 bytes × 5 = 10 bytes
// Total: ~58 bytes
```

### StringBuilder Memory

```java
StringBuilder sb = new StringBuilder("Hello");

// Initial capacity: 16 + string length
// Internal char[] grows dynamically

sb.append(" World");

// If capacity exceeded:
// 1. Create new char[] with 2× capacity + 2
// 2. Copy old data
// 3. Append new data
```

### String Concatenation Memory

```java
String result = "Hello" + " " + "World";

// Compiler optimization:
// Single String object in pool: "Hello World"

// Dynamic concatenation:
String dynamic = "Hello" + variable;

// Creates:
// 1. StringBuilder (heap allocation)
// 2. Appends all parts
// 3. toString() creates new String (heap allocation)
```

### String Immutability Memory

```java
String original = "Hello";
String modified = original.toUpperCase();

// Two separate String objects on heap:
// original: "Hello" (unchanged)
// modified: "HELLO" (new object)

// No memory sharing between original and modified
```

### String Comparison Memory

```java
String a = "Hello";
String b = "Hello";
String c = new String("Hello");

// Memory layout:
// Pool: "Hello" ← a, b
// Heap: String object ← c
//   - char[] points to same char data as pool entry
```

### Memory-Efficient Patterns

```java
// Use char[] for performance-critical code
char[] chars = "Hello".toCharArray();

// Use StringBuilder for large concatenations
StringBuilder sb = new StringBuilder(expectedSize);

// Use String.intern() for repeated literals
String key = largeString.intern();
```
