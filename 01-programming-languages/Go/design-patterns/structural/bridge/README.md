# Bridge Pattern

## Overview

Bridge decouples abstraction from implementation so both vary independently using interfaces.

## When to Use

- Abstraction and implementation should be decoupled
- Avoiding permanent binding
- Sharing implementation across abstractions

## Go Implementation

```go
type Renderer interface {
    RenderCircle(radius float64) string
}

type SVGRenderer struct{}
func (s *SVGRenderer) RenderCircle(r float64) string {
    return fmt.Sprintf("<circle r='%v'/>", r)
}

type Shape interface{ Draw() string }

type Circle struct {
    renderer Renderer
    radius   float64
}

func (c *Circle) Draw() string { return c.renderer.RenderCircle(c.radius) }
```

## Go-Idiomatic Alternative

```go
type Shape struct {
    Renderer
    radius float64
}

func (s *Shape) Draw() string { return s.RenderCircle(s.radius) }
```

## Real-World Example

```go
type Database interface {
    Query(sql string) ([]Row, error)
    Insert(table string, data Row) error
}

type Repository struct {
    Database
    tableName string
}

func (r *Repository) FindAll() ([]Row, error) {
    return r.Query("SELECT * FROM " + r.tableName)
}
```

## Best Practices

- Keep the bridge interface minimal
- Use dependency injection for implementations
- Document which implementations support which abstractions

## Interview Questions

1. What is the difference between Bridge and Adapter?
2. When would you choose Bridge over Strategy?
3. How does Go's implicit interfaces affect Bridge?
4. Can you switch implementations at runtime?
5. How do you test both sides independently?

## References

- "Design Patterns" - GoF Chapter 4
- Go Dev: Effective Go - Interfaces
