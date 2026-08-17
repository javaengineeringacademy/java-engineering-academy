# When to Use Wrapper Classes

## Decision Guide

### Primitives vs Wrapper Classes

| Use Primitives When | Use Wrapper Classes When |
|---------------------|-------------------------|
| Performance-critical code | Collections (Generics require objects) |
| Local variables in methods | Nullable fields |
| Mathematical computations | API boundaries |
| Loop counters | When null indicates "no value" |
| Known values | Boxing/unboxing is acceptable |

### Wrapper Class Selection

| Wrapper | Use When | Range |
|---------|----------|-------|
| `Byte` | Small integer values | -128 to 127 |
| `Short` | Medium integers | -32,768 to 32,767 |
| `Integer` | Most integer operations | ±2 billion |
| `Long` | Large integers, timestamps | ±9 quintillion |
| `Float` | Graphics, memory-critical | ~7 decimal digits |
| `Double` | Most decimal operations | ~15 decimal digits |
| `Boolean` | True/false flags | true or false |
| `Character` | Single Unicode characters | 0 to 65,535 |

### Autoboxing Decision Tree

| Scenario | Recommendation |
|----------|----------------|
| Adding to Collection | Autoboxing is fine |
| Frequent conversion in loop | Use primitives |
| Method parameter accepts Wrapper | Autobox if caller has primitive |
| Method returns Wrapper | Return primitive if possible |
| Cache values -128 to 127 | Use `valueOf()` for caching |

### Boxing Methods

| Method | Behavior | Use When |
|--------|----------|----------|
| `valueOf(primitive)` | Returns cached (for some ranges) | Preferred over constructor |
| `new Wrapper(primitive)` | Always creates new object | Avoid (deprecated in Java 9+) |
| `parseXxx(String)` | Returns primitive | Parsing user input |
| `toString(primitive)` | Returns String | Converting to text |

### Cache Ranges

| Wrapper | Cached Range | Method |
|---------|--------------|--------|
| `Boolean` | All (TRUE, FALSE) | `Boolean.valueOf()` |
| `Byte` | All (-128 to 127) | `Byte.valueOf()` |
| `Short` | -128 to 127 | `Short.valueOf()` |
| `Integer` | -128 to 127 | `Integer.valueOf()` |
| `Long` | -128 to 127 | `Long.valueOf()` |
| `Character` | 0 to 127 | `Character.valueOf()` |
| `Float` | None | `Float.valueOf()` |
| `Double` | None | `Double.valueOf()` |

## Production Guidelines

### Null Safety with Wrappers
```java
// AVOID: NPE risk
Map<String, Integer> map = new HashMap<>();
int value = map.get("key");  // NPE if key missing

// PREFER: Null-safe approach
Integer value = map.get("key");
if (value != null) {
    int primitive = value;
}

// BEST: Use getOrDefault
int value = map.getOrDefault("key", 0);
```

### Comparison Pitfalls
```java
// WRONG: Reference comparison
Integer a = 200;
Integer b = 200;
if (a == b) { ... }  // false (outside cached range)

// CORRECT: Value comparison
if (a.equals(b)) { ... }  // true

// BETTER: Use Objects.equals()
if (Objects.equals(a, b)) { ... }
```
