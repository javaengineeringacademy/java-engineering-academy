# Proxy Pattern

## Overview

Proxy provides a surrogate for another object to control access. Go uses interface delegation.

## When to Use

- Lazy initialization
- Access control and permissions
- Logging and monitoring
- Caching responses

## Go Implementation

```go
type Service interface {
    Execute(query string) string
}

type ProxyService struct {
    real *RealService
    log  []string
}

func (p *ProxyService) Execute(query string) string {
    p.log = append(p.log, query)
    if p.real == nil { p.real = &RealService{} }
    return p.real.Execute(query)
}
```

## Go-Idiomatic Alternative

```go
type ServiceFunc func(string) string

func WithLogging(svc ServiceFunc) ServiceFunc {
    return func(query string) string {
        log.Printf("query: %s", query)
        result := svc(query)
        log.Printf("result: %s", result)
        return result
    }
}
```

## Real-World Example

```go
type CacheProxy struct {
    real  *APIClient
    cache map[string]string
}

func (c *CacheProxy) Get(key string) (string, error) {
    if val, ok := c.cache[key]; ok { return val, nil }
    result, err := c.real.Get(key)
    if err == nil { c.cache[key] = result }
    return result, err
}
```

## Best Practices

- Implement the same interface as the real object
- Keep proxies transparent
- Use for single concerns

## Interview Questions

1. What is the difference between Proxy and Decorator?
2. When would you use a virtual vs protection proxy?
3. How do you handle proxy chaining?
4. Can proxies introduce memory leaks?
5. How would you implement a proxy with struct embedding?

## References

- "Design Patterns" - GoF Chapter 4
- Go Dev: Effective Go - Interfaces
