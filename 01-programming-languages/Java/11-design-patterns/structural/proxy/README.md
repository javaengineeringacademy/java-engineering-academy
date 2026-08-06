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

Proxy adds one method delegation (~5-10ns) per call. In access-controlled or logging proxies, the overhead includes the additional logic (permission check, log write). Virtual proxies defer expensive initialization — the first call pays the cost, subsequent calls are fast. Caching proxies amortize the real object cost over multiple requests.

## Examples

```java
// Access-controlled proxy
interface Document {
    String readContent();
    void writeContent(String content);
}

class RealDocument implements Document {
    private String content;
    
    RealDocument(String content) { this.content = content; }
    
    @Override
    public String readContent() { return content; }
    
    @Override
    public void writeContent(String content) { this.content = content; }
}

class ProtectionProxy implements Document {
    private final RealDocument document;
    private final String userRole;
    
    ProtectionProxy(RealDocument document, String userRole) {
        this.document = document;
        this.userRole = userRole;
    }
    
    @Override
    public String readContent() {
        System.out.println("Access check for: " + userRole);
        return document.readContent();
    }
    
    @Override
    public void writeContent(String content) {
        if (!"admin".equals(userRole)) {
            throw new SecurityException("Write access denied");
        }
        document.writeContent(content);
    }
}

// Usage
Document doc = new ProtectionProxy(new RealDocument("Secret"), "admin");
System.out.println(doc.readContent()); // Secret
doc.writeContent("Updated"); // OK

Document guest = new ProtectionProxy(new RealDocument("Secret"), "guest");
guest.writeContent("Hack"); // SecurityException
```

## Internal Working

The proxy implements the same interface as the real subject. It holds a reference to the real subject and controls access to it. The client interacts with the proxy as if it were the real subject. The proxy can perform pre-processing (permission check, logging, lazy initialization) before delegating to the real subject. Types include virtual (lazy loading), protection (access control), caching, and logging proxies.

## Why This Concept Exists

Direct access to objects is not always desirable. You may need to defer expensive initialization until actually needed (virtual proxy), enforce access control (protection proxy), cache results (caching proxy), or log every operation (logging proxy). Proxy adds this control without modifying the real subject. It is the foundation of RMI stubs, Spring AOP, and ORM lazy loading.

## Pitfalls

1. **Transparency**: Clients may not realize they are talking to a proxy — can cause confusion
2. **Overhead**: Every call goes through the proxy — performance-sensitive paths need careful design
3. **Leaky abstraction**: Proxy may expose behavior the real subject does not have
4. **Complexity**: Multiple proxy types (virtual, protection, caching) can be combined, making debugging hard
5. **Memory leaks**: Virtual proxies that cache expensive objects may hold references unnecessarily

## References

- [Refactoring.Guru - Proxy Pattern](https://refactoring.guru/design-patterns/proxy)
- [Java RMI Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.rmi/java/rmi/package-summary.html)
- [Spring AOP Documentation](https://docs.spring.io/spring-framework/reference/core/aop.html)
