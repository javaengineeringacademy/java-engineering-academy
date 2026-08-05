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
