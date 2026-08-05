# Decorator Pattern

## Overview

Decorator adds behavior to objects dynamically. Go's middleware pattern using function wrapping is the common implementation.

## When to Use

- Cross-cutting concerns (logging, auth, caching)
- HTTP middleware chains
- Extending functionality without subclassing

## Go Implementation

```go
type Handler interface {
    Handle(request string) string
}

type LoggingHandler struct{ wrapped Handler }

func (l *LoggingHandler) Handle(request string) string {
    log.Printf("Request: %s", request)
    result := l.wrapped.Handle(request)
    log.Printf("Response: %s", result)
    return result
}
```

## Go-Idiomatic Alternative

Function decorators (middleware):

```go
type HandlerFunc func(string) string

func Logging(next HandlerFunc) HandlerFunc {
    return func(request string) string {
        log.Printf("Request: %s", request)
        result := next(request)
        log.Printf("Response: %s", result)
        return result
    }
}
```

## Real-World Example

```go
func Log(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        start := time.Now()
        next.ServeHTTP(w, r)
        log.Printf("%s %s %v", r.Method, r.URL.Path, time.Since(start))
    })
}
```

## Best Practices

- Use function wrapping for simple decorators
- Use struct embedding for stateful decorators
- Keep decorators composable and independent

## Interview Questions

1. How does Go's middleware relate to Decorator?
2. What is the difference between Decorator and Adapter?
3. How do you handle error propagation in chains?
4. Can you remove a decorator at runtime?
5. How would you implement conditional decoration?

## References

- "Design Patterns" - GoF Chapter 4
- Go Blog: "How to write middleware"
