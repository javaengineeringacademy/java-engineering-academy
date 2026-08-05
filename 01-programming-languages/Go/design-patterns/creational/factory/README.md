# Factory Pattern

## Overview

Factory provides an interface for creating objects without specifying their concrete types. Go uses functions returning interfaces.

## When to Use

- Creating different types based on configuration
- Decoupling creation logic from usage
- Supporting multiple implementations of an interface

## Go Implementation

```go
type Transport interface {
    Deliver() string
}

type Truck struct{}
func (t *Truck) Deliver() string { return "Delivering by land" }

type Ship struct{}
func (s *Ship) Deliver() string { return "Delivering by sea" }

func CreateTransport(t string) (Transport, error) {
    switch t {
    case "truck": return &Truck{}, nil
    case "ship":  return &Ship{}, nil
    default:      return nil, fmt.Errorf("unknown: %s", t)
    }
}
```

## Go-Idiomatic Alternative

Registry map for dynamic registration:

```go
type Creator func() Transport
var registry = map[string]Creator{
    "truck": func() Transport { return &Truck{} },
    "ship":  func() Transport { return &Ship{} },
}
```

## Real-World Example

```go
func NewLogger(logType string) Logger {
    switch logType {
    case "file":    return &FileLogger{}
    default:        return &ConsoleLogger{}
    }
}
```

## Best Practices

- Return interfaces, not concrete types
- Use descriptive names: `NewXxx`, `CreateXxx`
- Return errors for invalid types

## Interview Questions

1. How does Factory differ from Abstract Factory in Go?
2. Why should factories return interfaces?
3. How would you make the factory extensible?
4. When would you use a factory function vs a constructor?
5. How do you handle factory errors in Go?

## References

- "Design Patterns" - GoF Chapter 3
- Go Blog: "Go Proverbs"
