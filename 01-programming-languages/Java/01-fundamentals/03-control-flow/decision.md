# When to Use Control Flow

## Decision Guide

### Choosing the Right Statement

| Statement | Use When | Example |
|-----------|----------|---------|
| `if-else` | Simple binary or ternary decisions | `if (age >= 18) adult(); else minor();` |
| `switch` | Multiple discrete value comparisons | `switch (day) { case MON: ... }` |
| `for` | Known iteration count or range | `for (int i = 0; i < 10; i++)` |
| `while` | Unknown iterations, condition-first | `while (scanner.hasNext())` |
| `do-while` | Must execute at least once | `do { input = read(); } while (input.isEmpty());` |
| `for-each` | Iterating over collections/arrays | `for (String s : list)` |
| `break` | Early exit from loop or switch | Exit when found |
| `continue` | Skip to next iteration | Skip invalid entries |

### If-Else vs Switch

| Scenario | Recommended |
|----------|-------------|
| 2-3 conditions | `if-else` |
| 4+ discrete values | `switch` |
| Range checks | `if-else` |
| String/enum matching | `switch` (Java 7+) |
| Pattern matching | `switch` with `when` (Java 21+) |

### Loop Selection Guide

| Scenario | Loop Type | Why |
|----------|-----------|-----|
| Iterate N times | `for` | Count is known upfront |
| Process until condition | `while` | Iterations unknown |
| Process at least once | `do-while` | Must execute body first |
| Iterate collection | `for-each` | Cleanest syntax, no index management |
| Infinite loop | `while (true)` | Clear intent, exit via `break` |

### Break vs Continue

| Use | When | Example |
|-----|------|---------|
| `break` | Found what you need, stop searching | Find element in list |
| `continue` | Current iteration invalid, skip rest | Skip null entries |
| Labeled break | Exit nested loops | Exit from inner loop to outer |

## Production Guidelines

### Guard Clauses (Early Return)
```java
// AVOID: Deep nesting
public void process(Order order) {
    if (order != null) {
        if (order.isValid()) {
            if (order.isPaid()) {
                // actual logic
            }
        }
    }
}

// PREFER: Guard clauses
public void process(Order order) {
    if (order == null) return;
    if (!order.isValid()) return;
    if (!order.isPaid()) return;

    // actual logic (flat, readable)
}
```

### Switch Expression (Java 14+)
```java
// PREFER: Switch expression for assignments
String result = switch (status) {
    case ACTIVE -> "Active";
    case INACTIVE -> "Inactive";
    case PENDING -> "Pending";
    default -> throw new IllegalArgumentException("Unknown: " + status);
};
```

### Avoiding Common Pitfalls
```java
// PITFALL: Missing break causes fall-through
switch (x) {
    case 1: System.out.println("One");
    case 2: System.out.println("Two");  // Runs for case 1 too!
}

// FIX: Always include break or use ->
switch (x) {
    case 1 -> System.out.println("One");
    case 2 -> System.out.println("Two");
}
```
