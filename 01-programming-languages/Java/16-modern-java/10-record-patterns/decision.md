# Record Patterns - Decision Guide

## Use Record Patterns When

### Extracting Multiple Components
```java
// Before
if (obj instanceof Point) {
    Point p = (Point) obj;
    int x = p.x();
    int y = p.y();
    // use x and y
}

// After
if (obj instanceof Point(int x, int y)) {
    // use x and y directly
}
```

### Nested Record Deconstruction
```java
if (line instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
    // Access all four coordinates directly
}
```

### Complex Pattern Matching
When you need to match and destructure in one step.

## Don't Use Record Patterns When

### Only Need One Component
Simple accessor is clearer.

### Record is Simple
For simple records, explicit accessors may be more readable.

### Complex Logic
If deconstruction makes code harder to understand.

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Record patterns | Concise, powerful | Requires Java 21+ |
| Accessor methods | Compatible | Verbose |
| Pattern matching | Flexible | More code |

## Best Practices

1. **Use for clarity** - When deconstruction improves readability
2. **Keep patterns simple** - Complex nesting reduces readability
3. **Combine with guards** - Add conditions when needed
4. **Use with sealed types** - For exhaustive matching
