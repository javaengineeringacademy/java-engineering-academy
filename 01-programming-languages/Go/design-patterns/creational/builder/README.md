# Builder Pattern

## Overview

Builder separates complex object construction from its representation using fluent API with method chaining.

## When to Use

- Structs with many optional fields
- Complex initialization logic
- Multiple representations of the same object

## Go Implementation

```go
type ServerBuilder struct{ server Server }

func NewServerBuilder() *ServerBuilder { return &ServerBuilder{} }

func (b *ServerBuilder) Host(h string) *ServerBuilder {
    b.server.Host = h; return b
}
func (b *ServerBuilder) Port(p int) *ServerBuilder {
    b.server.Port = p; return b
}
func (b *ServerBuilder) EnableTLS() *ServerBuilder {
    b.server.TLS = true; return b
}
func (b *ServerBuilder) Build() Server { return b.server }
```

## Go-Idiomatic Alternative

Functional options:

```go
type Option func(*Server)

func WithPort(p int) Option { return func(s *Server) { s.Port = p } }
func WithTLS() Option       { return func(s *Server) { s.TLS = true } }

func NewServer(host string, opts ...Option) Server {
    s := Server{Host: host, Port: 8080}
    for _, opt := range opts { opt(&s) }
    return s
}
```

## Real-World Example

```go
type QueryBuilder struct {
    table string; where []string; orderBy string
}

func (q *QueryBuilder) From(t string) *QueryBuilder { q.table = t; return q }
func (q *QueryBuilder) Where(c string) *QueryBuilder {
    q.where = append(q.where, c); return q
}
```

## Best Practices

- Return `*Builder` from all methods for chaining
- Validate in `Build()`, not intermediate methods
- Provide sensible defaults

## Interview Questions

1. How does Builder differ from functional options?
2. When would you choose Builder over struct literals?
3. How do you handle validation errors in Build?
4. Can you implement a thread-safe builder?
5. How do you add optional fields without breaking code?

## References

- "Design Patterns" - GoF Chapter 3
- Go Blog: "Functional options for friendly APIs"
