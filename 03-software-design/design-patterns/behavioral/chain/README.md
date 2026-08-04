# Chain of Responsibility Pattern

The Chain of Responsibility pattern passes a request along a chain of handlers. Each handler decides either to process the request or pass it to the next handler.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Chain](#basic-chain)
3. [Middleware Pipeline](#middleware-pipeline)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Chain of Responsibility?

Request travels through a chain of handlers. Each handler either handles it or passes it forward.

```
Request ──▶ Handler1 ──▶ Handler2 ──▶ Handler3 ──▶ End
              │              │              │
          (process)     (process)     (pass on)
```

### When to Use

- Multiple objects can handle a request
- Handler should be determined at runtime
- You want to send request to one of multiple handlers
- Pipeline processing (middleware, filters)

---

## Basic Chain

### Logging Levels

```java
// Handler
public abstract class LogHandler {
    private LogHandler next;

    public LogHandler setNext(LogHandler next) {
        this.next = next;
        return next;
    }

    public void handle(LogLevel level, String message) {
        if (canHandle(level)) {
            write(level, message);
        } else if (next != null) {
            next.handle(level, message);
        }
    }

    protected abstract boolean canHandle(LogLevel level);
    protected abstract void write(LogLevel level, String message);
}

// Concrete handlers
public class DebugHandler extends LogHandler {
    @Override
    protected boolean canHandle(LogLevel level) {
        return level == LogLevel.DEBUG;
    }

    @Override
    protected void write(LogLevel level, String message) {
        System.out.println("DEBUG: " + message);
    }
}

public class InfoHandler extends LogHandler {
    @Override
    protected boolean canHandle(LogLevel level) {
        return level == LogLevel.INFO;
    }

    @Override
    protected void write(LogLevel level, String message) {
        System.out.println("INFO: " + message);
    }
}

public class ErrorHandler extends LogHandler {
    @Override
    protected boolean canHandle(LogLevel level) {
        return level == LogLevel.ERROR;
    }

    @Override
    protected void write(LogLevel level, String message) {
        System.err.println("ERROR: " + message);
    }
}

// Build chain
LogHandler chain = new DebugHandler();
chain.setNext(new InfoHandler()).setNext(new ErrorHandler());

// Usage
chain.handle(LogLevel.INFO, "Application started");  // INFO handler
chain.handle(LogLevel.ERROR, "Something failed");    // ERROR handler
chain.handle(LogLevel.DEBUG, "Debug info");          // DEBUG handler
```

---

## Middleware Pipeline

### HTTP Middleware

```java
// Handler interface
public interface Middleware {
    boolean handle(Request request, Response response);
}

// Concrete handlers
public class AuthMiddleware implements Middleware {
    @Override
    public boolean handle(Request request, Response response) {
        String token = request.getHeader("Authorization");
        if (token == null || !isValidToken(token)) {
            response.setStatus(401);
            response.setBody("Unauthorized");
            return false;  // Stop chain
        }
        request.setAttribute("user", getUserFromToken(token));
        return true;  // Continue chain
    }
}

public class RateLimitMiddleware implements Middleware {
    private final RateLimiter limiter = new RateLimiter(100);

    @Override
    public boolean handle(Request request, Response response) {
        if (!limiter.tryAcquire()) {
            response.setStatus(429);
            response.setBody("Too Many Requests");
            return false;
        }
        return true;
    }
}

public class LoggingMiddleware implements Middleware {
    @Override
    public boolean handle(Request request, Response response) {
        long start = System.currentTimeMillis();
        // Continue chain, then log
        return true;
    }
}

// Pipeline builder
public class MiddlewarePipeline {
    private final List<Middleware> middlewares = new ArrayList<>();

    public MiddlewarePipeline add(Middleware middleware) {
        middlewares.add(middleware);
        return this;
    }

    public boolean execute(Request request, Response response) {
        for (Middleware middleware : middlewares) {
            if (!middleware.handle(request, response)) {
                return false;
            }
        }
        return true;
    }
}

// Usage
MiddlewarePipeline pipeline = new MiddlewarePipeline()
    .add(new LoggingMiddleware())
    .add(new RateLimitMiddleware())
    .add(new AuthMiddleware());

boolean success = pipeline.execute(request, response);
```

---

## Best Practices

### Do

```java
// 1. Return whether chain should continue
public boolean handle(Request request) {
    if (canHandle(request)) {
        process(request);
        return true;  // or false to stop
    }
    return true;  // Continue to next
}

// 2. Build chains flexibly
pipeline.add(handler1).add(handler2).add(handler3);
```

### Don't

```java
// 1. Don't create infinite loops
handler1.setNext(handler2);
handler2.setNext(handler1);  // Infinite loop!

// 2. Don't forget terminal handler
// Chain should eventually handle or reject request
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Chain** | Request passes through handlers |
| **Handler** | Process or pass to next |
| **Decoupling** | Sender doesn't know which handler processes |
| **Flexibility** | Add/remove handlers dynamically |
| **Use Cases** | Logging, middleware, validation, filters |
| **Pipeline** | Sequential processing |
