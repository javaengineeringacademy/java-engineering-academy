# Composite Design Pattern

## Overview
Composite pattern composes objects into tree structures to represent part-whole hierarchies. It lets clients treat individual objects and compositions uniformly.

## When to Use
- You want to represent part-whole hierarchies of objects
- You want clients to be able to ignore differences between compositions and individual objects
- You need to structure a system as a recursive composition

## Code Example

```java
public abstract class FileSystemItem {
    protected String name;

    public FileSystemItem(String name) {
        this.name = name;
    }

    public abstract int getSize();
}

public class Directory extends FileSystemItem {
    private final List<FileSystemItem> children = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public void add(FileSystemItem item) {
        children.add(item);
    }

    @Override
    public int getSize() {
        return children.stream().mapToInt(FileSystemItem::getSize).sum();
    }
}
```

## Common Mistakes
- Not defining common operations in the component interface
- Making leaf nodes implement unnecessary operations
- Not handling null children or invalid operations gracefully

## Interview Questions
1. What is the difference between Composite and Decorator patterns?
2. When would you use a leaf vs a composite node?
3. How do you prevent adding children to leaf nodes?

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
