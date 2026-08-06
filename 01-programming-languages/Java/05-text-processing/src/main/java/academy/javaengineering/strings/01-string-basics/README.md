# String Basics

## Overview
This module covers fundamental string concepts in Java including string creation, immutability, string pool, and comparison operations.

## Key Concepts

### 1. String Creation
```java
// String literal (stored in string pool)
String literal = "Hello";

// String object (stored on heap)
String object = new String("Hello");

// From char array
String fromChars = new String(new char[]{'H', 'e', 'l', 'l', 'o'});
```

### 2. String Immutability
- Strings in Java are **immutable** - once created, they cannot be modified
- Any operation that appears to modify a string actually creates a new string
- Immutability provides security, thread-safety, and hash stability

### 3. String Pool
- Special memory area in heap for storing string literals
- Ensures string sharing and memory optimization
- `intern()` method adds strings to the pool

### 4. String Comparison
- `==` compares references (memory addresses)
- `.equals()` compares content
- `.equalsIgnoreCase()` compares content ignoring case
- `.compareTo()` lexicographic comparison

## Code References
- `StringBasics.java` - Comprehensive examples

## Common Mistakes
1. Using `==` instead of `.equals()` for string comparison
2. Modifying strings expecting in-place changes
3. Forgetting string pool behavior with `new String()`
4. Not considering memory impact of excessive string concatenation

## Interview Questions
1. Why are strings immutable in Java?
2. Explain the string pool and its benefits.
3. What is the difference between `==` and `.equals()`?
4. When would you use `intern()` method?
5. What are the performance implications of string immutability?
