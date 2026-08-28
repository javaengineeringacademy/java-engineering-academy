# Record Patterns (Java 21)

Record Patterns allow you to deconstruct records in pattern matching, extracting components directly without explicit accessor calls.

## Key Features

- **Deconstruction patterns** - Extract record components directly
- **Nested patterns** - Deconstruct nested records
- **Type checking** - Verify type while extracting components
- **Exhaustive matching** - Compiler ensures all cases are handled

## Syntax

```java
// Basic record pattern
record Point(int x, int y) {}
Point point = new Point(10, 20);

if (point instanceof Point(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);
}

// Nested record patterns
record Line(Point start, Point end) {}
Line line = new Line(new Point(0, 0), new Point(10, 10));

if (line instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
    System.out.println("Line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
}

// With guards
if (point instanceof Point(int x, int y) && x > 0 && y > 0) {
    System.out.println("Point in first quadrant");
}
```

## Rules

1. Pattern components are effectively final
2. Components must match record structure
3. Can be used with type patterns
4. Supports nested deconstruction

## When to Use

- Extracting multiple components from records
- Complex pattern matching with records
- When you need multiple fields at once
- With sealed hierarchies for exhaustive matching
