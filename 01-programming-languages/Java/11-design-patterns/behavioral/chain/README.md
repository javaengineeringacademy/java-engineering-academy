# Chain of Responsibility Pattern

## Overview
The Chain of Responsibility pattern passes a request along a chain of handlers. Each handler decides either to process the request or to pass it to the next handler in the chain.

## When to Use
- Multiple objects can handle a request, but handler not known at runtime
- Want to send request to one of several handlers without specifying receiver
- Set of handlers should be specified dynamically
- Logging, authentication, validation pipelines

## Code Structure
```
Handler (interface)           BaseHandler (abstract)
    |                              |
setNext()                   handles chain logic
handle()
    |
AuthHandler, LoggingHandler, ValidationHandler
```

## Key Benefits
- Decouples sender from receiver
- Multiple handlers get a chance to process request
- Handlers can be added/removed dynamically
- Single Responsibility Principle

## Common Mistakes
- Chain too long causing performance issues
- Not handling the case where no handler processes request
- Circular chains causing infinite loops

## Interview Questions
1. What is the difference between Chain of Responsibility and Decorator?
2. How do you prevent infinite loops in the chain?
3. Can a handler process and pass the request?
4. When would you use Chain over direct conditional logic?

## Performance

Chain traversal is O(n) in the worst case (no handler matches). For short chains (3-5 handlers), the overhead is negligible. Long chains benefit from short-circuiting — stop at the first handler that processes the request. Caching handler lookup results can reduce repeated traversals. Servlet filters and Spring Interceptors use this pattern efficiently.

## Examples

```java
// Support ticket escalation chain
abstract class SupportHandler {
    protected SupportHandler next;
    
    SupportHandler setNext(SupportHandler next) {
        this.next = next;
        return next;
    }
    
    abstract void handle(String issue, int severity);
}

class L1Support extends SupportHandler {
    @Override
    void handle(String issue, int severity) {
        if (severity <= 3) {
            System.out.println("L1 resolved: " + issue);
        } else if (next != null) {
            System.out.println("L1 escalating: " + issue);
            next.handle(issue, severity);
        }
    }
}

class L2Support extends SupportHandler {
    @Override
    void handle(String issue, int severity) {
        if (severity <= 6) {
            System.out.println("L2 resolved: " + issue);
        } else if (next != null) {
            System.out.println("L2 escalating: " + issue);
            next.handle(issue, severity);
        }
    }
}

class L3Support extends SupportHandler {
    @Override
    void handle(String issue, int severity) {
        System.out.println("L3 (engineering) resolved: " + issue);
    }
}

// Usage
SupportHandler chain = new L1Support();
chain.setNext(new L2Support()).setNext(new L3Support());

chain.handle("Password reset", 2);   // L1 resolved
chain.handle("Server crash", 8);     // L1 → L2 → L3
```

## Internal Working

Each handler in the chain holds a reference to the next handler. When a request arrives, the handler checks if it can process it. If yes, it handles the request. If no, it passes the request to the next handler. The chain can be built dynamically at runtime. This is the pattern behind servlet filters, Spring Security filters, and logging appenders.

## Why This Concept Exists

When multiple objects may handle a request, but the handler is not known at runtime, chain of responsibility decouples the sender from the receiver. Each handler decides whether to process or pass along. This enables flexible processing pipelines — handlers can be added, removed, or reordered without changing the sender. Authentication, logging, validation, and error handling are natural fits.

## Pitfalls

1. **No guarantee of handling**: Request may reach the end of the chain without being processed
2. **Performance**: Long chains add latency — each handler adds a method call
3. **Debugging**: Tracing which handler processed a request is harder than conditional logic
4. **Circular chains**: If handler A calls handler B which calls A, infinite loop occurs
5. **Ordering sensitivity**: The order of handlers matters — wrong order produces wrong behavior

## References

- [Refactoring.Guru - Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility)
- [Java Servlet Filter](https://docs.oracle.com/javaee/7/api/javax/servlet/Filter.html)
- [Head First Design Patterns - Chain of Responsibility](https://www.oreilly.com/library/view/head-first-design/0596007124/)
