# Record Patterns References

## Official Documentation

- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [JEP 427: Pattern Matching for switch (Third Preview)](https://openjdk.org/jeps/427)
- [Java Language Specification - Pattern Matching](https://docs.oracle.com/javase/specs/jls/se17/html/jls-14.html#jls-14.30)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Record Pattern | Pattern that deconstructs a record |
| Deconstruction | Extracting record components |
| Nested Pattern | Deconstructing nested records |
| Guarded Pattern | Pattern with additional condition |

## Code Examples

### Basic Record Pattern
```java
record Point(int x, int y) {}
Point point = new Point(10, 20);

if (point instanceof Point(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);
}
```

### Nested Record Pattern
```java
record Line(Point start, Point end) {}
Line line = new Line(new Point(0, 0), new Point(10, 10));

if (line instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
    System.out.println("Line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
}
```

### Record Pattern in Switch
```java
String description = switch (shape) {
    case Circle(double r) -> "Circle with radius " + r;
    case Rectangle(double w, double h) -> "Rectangle " + w + "x" + h;
    case Triangle(double b, double h) -> "Triangle " + b + "x" + h;
};
```

### Partial Deconstruction
```java
// Only extract what you need
if (point instanceof Point(int x, var y)) {
    // x is extracted, y is kept as Point
}
```

## Common Patterns

1. **Simple deconstruction:** `case Point(int x, int y)`
2. **Nested deconstruction:** `case Line(Point(int x1, int y1), Point(int x2, int y2))`
3. **With guards:** `case Point(int x, int y) && x > 0`
4. **In switch:** `case Circle(double r) -> ...`
