# Proxy Design Pattern

## Overview
Proxy pattern provides a surrogate or placeholder for another object to control access to it. It adds a layer of indirection to support distributed, lazy, or controlled access.

## When to Use
- Lazy initialization (virtual proxy)
- Access control (protection proxy)
- Logging or auditing (logging proxy)
- Caching requests (caching proxy)

## Code Example

```java
public class ProxyImage implements Image {
    private final String fileName;
    private RealImage realImage;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
}
```

## Common Mistakes
- Adding too much logic in the proxy that belongs in the real subject
- Not implementing the same interface as the real subject
- Forgetting that proxy adds overhead to every method call

## Interview Questions
1. What are the different types of proxies?
2. How does Proxy pattern differ from Decorator pattern?
3. When would you use a virtual proxy vs a protection proxy?

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
