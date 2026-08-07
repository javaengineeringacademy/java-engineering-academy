# Control Flow — C Language

## What it is
Control flow statements determine the order in which code executes.

## Why it exists
To make decisions and repeat actions based on conditions.

## When to use it
Whenever you need conditional execution or loops.

## How it works

### if-else

```c
if (score >= 90) {
    grade = 'A';
} else if (score >= 80) {
    grade = 'B';
} else {
    grade = 'C';
}
```

### switch

```c
switch (day) {
    case 1:
        printf("Monday");
        break;
    case 2:
        printf("Tuesday");
        break;
    default:
        printf("Other day");
}
```

### for loop

```c
for (int i = 0; i < 10; i++) {
    printf("%d\n", i);
}
```

### while loop

```c
while (count < 10) {
    printf("%d\n", count);
    count++;
}
```

### do-while loop

```c
do {
    printf("%d\n", count);
    count++;
} while (count < 10);
```

### break and continue

```c
for (int i = 0; i < 10; i++) {
    if (i == 5) break;      // Exit loop
    if (i % 2 == 0) continue; // Skip iteration
    printf("%d\n", i);
}
```

### goto (use sparingly)

```c
for (...) {
    for (...) {
        if (error) goto cleanup;
    }
}
cleanup:
    // handle cleanup
```

## Production Checklist

- [ ] Always use braces for multi-line blocks
- [ ] Avoid deep nesting (max 3 levels)
- [ ] Use switch for multiple conditions on same variable
- [ ] Initialize loop variables properly
- [ ] Avoid infinite loops

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Knows if-else and basic loops |
| Intermediate | Uses switch and nested loops |
| Advanced | Optimizes loops and uses goto strategically |

## Common Myths

1. **Myth**: goto is always bad
   **Truth**: goto is useful for error handling and breaking out of nested loops

2. **Myth**: for and while are interchangeable
   **Truth**: for is better for counted loops, while for condition-based

## One-Minute Revision

| Statement | Purpose |
|-----------|---------|
| if-else | Conditional execution |
| switch | Multi-way branch |
| for | Counted loop |
| while | Condition-based loop |
| do-while | Execute at least once |
| break | Exit loop/switch |
| continue | Skip iteration |
| goto | Jump to label |

## Related Topics

- [Operators](../02-operators/README.md)
- [Functions](../04-functions/README.md)
