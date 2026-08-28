# Switch Expressions - Decision Guide

## Use Switch Expressions When

### You Need to Return a Value
```java
// Before
String result;
switch (day) {
    case "MON":
        result = "Monday";
        break;
    case "TUE":
        result = "Tuesday";
        break;
    default:
        result = "Unknown";
}

// After
String result = switch (day) {
    case "MON" -> "Monday";
    case "TUE" -> "Tuesday";
    default -> "Unknown";
};
```

### You Want Concise Code
Arrow syntax reduces boilerplate significantly.

### You Want Exhaustive Checking
Compiler ensures all cases are handled.

## Don't Use Switch Expressions When

### Fall-through is Needed
Use traditional switch with colon syntax.

### Complex Side Effects
When cases need to perform multiple operations.

### Legacy Code
When working with Java < 14.

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Switch Expression | Concise, safe | Requires Java 14+ |
| Traditional Switch | Compatible | Verbose, error-prone |
| If-else chain | Simple | Less expressive |
| Map lookup | Flexible | More complex |

## Best Practices

1. **Use arrow syntax by default** - It's safer and more concise
2. **Use yield for complex logic** - When you need multiple statements
3. **Always handle null** - Or use default
4. **Keep cases simple** - Complex logic belongs in methods
