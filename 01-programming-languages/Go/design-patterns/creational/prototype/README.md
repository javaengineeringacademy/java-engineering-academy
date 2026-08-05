# Prototype Pattern

## Overview

Prototype creates new objects by copying an existing instance. Go's value semantics make this straightforward.

## When to Use

- Expensive object creation that can be cloned
- Creating objects with similar state
- Undo/redo systems requiring state snapshots

## Go Implementation

```go
type Cloneable interface {
    Clone() Cloneable
}

type Employee struct {
    Name    string
    Address Address
}

func (e *Employee) Clone() Cloneable {
    cloned := *e
    return &cloned
}
```

## Go-Idiomatic Alternative

```go
func CloneEmployee(e *Employee) *Employee {
    cloned := *e
    return &cloned
}

func CloneSlice(src []int) []int {
    dst := make([]int, len(src))
    copy(dst, src)
    return dst
}
```

## Real-World Example

```go
type Config struct {
    DB       DBConfig
    Features map[string]bool
}

func (c *Config) Clone() *Config {
    cloned := *c
    cloned.Features = make(map[string]bool)
    for k, v := range c.Features { cloned.Features[k] = v }
    return &cloned
}
```

## Best Practices

- Deep copy slices and maps to avoid shared state
- Use value types for independent fields
- Document shallow vs deep copy behavior

## Interview Questions

1. What is the difference between shallow and deep copy?
2. How do you clone a struct with maps or slices?
3. When would you use Prototype over Factory?
4. How does Go's value semantics affect Prototype?
5. Can you implement Prototype using reflection?

## References

- "Design Patterns" - GoF Chapter 3
- Go Blog: "Go slices: usage and internals"
