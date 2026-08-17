# Strings Internals

## How Java Stores Strings

### String Object Layout

```java
String str = "Hello";

// Object layout:
// Object Header: 16 bytes (mark word + class pointer)
// Hash field: 4 bytes (cached hash code)
// Count field: 4 bytes (string length)
// Offset field: 4 bytes (offset into char array)
// Padding: 4 bytes
// char[] reference: 8 bytes (points to char array on heap)
// Total: ~40 bytes + char array
```

### String Pool Internals

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");

// s1 and s2 reference same pool entry
// s3 is a separate object on heap

// Pool lookup:
// 1. Compute hash of "Hello"
// 2. Find bucket in pool hash table
// 3. Return existing reference or create new entry
```

### String Concatenation

```java
String result = "Hello" + " " + "World";

// Compiler optimization:
// Result: String result = "Hello World"; (single pool entry)

// Dynamic concatenation:
String dynamic = "Hello" + System.currentTimeMillis();
// Creates StringBuilder internally:
// new StringBuilder().append("Hello").append(value).toString()
```

### String Immutability

```java
String original = "Hello";
String modified = original.toUpperCase();

// original: still "Hello" (unchanged)
// modified: new String object "HELLO"
// No modification to original String object
```

### String Comparison

```java
String a = "Hello";
String b = "Hello";
String c = new String("Hello");

a == b      // true (same pool entry)
a == c      // false (different objects)
a.equals(c) // true (same content)
```

### String Formatting

```java
String formatted = String.format("Name: %s, Age: %d", name, age);

// Internally creates:
// 1. StringBuilder with capacity estimate
// 2. Appends formatted values
// 3. Returns new String object
```

### String Hash Code

```java
String str = "Hello";

// Hash code computation:
// s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
// 'H'*31^4 + 'e'*31^3 + 'l'*31^2 + 'l'*31^1 + 'o'*31^0

// Cached after first call
private int hash; // Default is 0
```
