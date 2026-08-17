# When to Use Strings

## Decision Guide

### String vs StringBuilder vs StringBuffer

| Use String When | Use StringBuilder When | Use StringBuffer When |
|-----------------|----------------------|---------------------|
| Immutable text needed | Building strings in loops | Thread-safe string building |
| Simple concatenation | Performance-sensitive paths | Shared mutable string |
| String pool optimization | Large string construction | Legacy code compatibility |
| Map keys, constants | Single-threaded use | Multi-threaded access |

### String Operations Decision Tree

| Operation | Method | Time |
|-----------|--------|------|
| Concatenation (+) | `str1 + str2` | O(n + m) |
| Concatenation (loop) | `StringBuilder.append()` | O(n) total |
| Search | `indexOf()` | O(n * m) |
| Contains | `contains()` | O(n * m) |
| Replace | `replace()` | O(n) |
| Split | `split()` | O(n) |
| Trim | `trim()` | O(n) |
| Case | `toUpperCase()` | O(n) |

### When to Use String Methods

| Method | Use When | Example |
|--------|----------|---------|
| `equals()` | Content comparison | `str.equals("target")` |
| `compareTo()` | Alphabetical ordering | `str1.compareTo(str2)` |
| `indexOf()` | Find substring position | `str.indexOf("find")` |
| `substring()` | Extract portion | `str.substring(start, end)` |
| `split()` | Tokenize by delimiter | `str.split(",")` |
| `join()` | Combine with separator | `String.join(",", parts)` |
| `format()` | Formatted output | `String.format("Name: %s", name)` |

### String Pool Strategy

| Use Pool When | Avoid Pool When |
|---------------|-----------------|
| String literals | Dynamic string construction |
| Constants | Large strings |
| Repeated values | One-time strings |
| Enum-like values | User input |

## Production Guidelines

### Null-Safe String Operations
```java
// AVOID: NPE risk
if (str.equals("target")) { ... }

// PREFER: Null-safe comparison
if ("target".equals(str)) { ... }

// BETTER: Objects.equals()
if (Objects.equals(str, "target")) { ... }

// BEST: Optional
Optional.ofNullable(str)
    .filter(s -> s.equals("target"))
    .ifPresent(s -> process());
```

### StringBuilder for Loops
```java
// AVOID: O(n²) concatenation
String result = "";
for (String item : list) {
    result += item;  // Creates new String each iteration
}

// PREFER: O(n) StringBuilder
StringBuilder sb = new StringBuilder();
for (String item : list) {
    sb.append(item);
}
String result = sb.toString();
```

### String Immutability Awareness
```java
// This creates a new String object
String trimmed = str.trim();  // Original str unchanged

// This replaces the reference
str = str.trim();  // Now str points to new object
```
