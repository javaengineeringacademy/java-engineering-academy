# StringBuilder

## Overview
This module covers StringBuilder and StringBuffer classes for efficient string manipulation in Java.

## Key Concepts

### 1. StringBuilder vs String
- **String**: Immutable, each modification creates new object
- **StringBuilder**: Mutable, modifies in place (not thread-safe)
- **StringBuffer**: Mutable, thread-safe (synchronized)

### 2. When to Use StringBuilder
- Building strings in loops
- Multiple concatenations
- String manipulation operations
- Performance-critical code

### 3. Common Operations
```java
StringBuilder sb = new StringBuilder();
sb.append("Hello");         // Add to end
sb.insert(0, "Start");     // Insert at index
sb.delete(0, 5);           // Delete range
sb.replace(0, 5, "Hi");    // Replace range
sb.reverse();              // Reverse string
sb.toString();             // Convert to String
```

### 4. Capacity Management
- Initial capacity: 16 characters
- Grows automatically (capacity * 2 + 2)
- Pre-allocate if size is known

## Code References
- `StringBuilderDemo.java` - Detailed examples

## Common Mistakes
1. Using String concatenation in loops instead of StringBuilder
2. Not pre-allocating capacity when size is known
3. Forgetting to call `toString()` when needed
4. Using StringBuilder when thread-safety is required (use StringBuffer)

## Interview Questions
1. What is the difference between StringBuilder and StringBuffer?
2. When should you use StringBuilder over String concatenation?
3. How does StringBuilder handle capacity growth?
4. What is the time complexity of append operation?
5. How do you reverse a string efficiently?
