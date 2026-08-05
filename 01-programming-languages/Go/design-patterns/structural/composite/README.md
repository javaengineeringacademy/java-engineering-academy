# Composite Pattern

## Overview

Composite composes objects into tree structures, letting clients treat individual and composed objects uniformly.

## When to Use

- File systems, UI components
- Tree structures with uniform interface
- Expression trees or ASTs

## Go Implementation

```go
type Component interface {
    Execute() string
    Name() string
}

type File struct{ name string }
func (f *File) Name() string    { return f.name }
func (f *File) Execute() string { return f.name }

type Directory struct {
    name     string
    children []Component
}

func (d *Directory) Add(c Component) { d.children = append(d.children, c) }
func (d *Directory) Execute() string {
    result := d.name + ": ["
    for i, child := range d.children {
        if i > 0 { result += ", " }
        result += child.Execute()
    }
    return result + "]"
}
```

## Go-Idiomatic Alternative

```go
type Node interface{ Value() int }

type Leaf struct{ val int }
func (l *Leaf) Value() int { return l.val }

type Branch struct{ children []Node }
func (b *Branch) Value() int {
    sum := 0
    for _, c := range b.children { sum += c.Value() }
    return sum
}
```

## Real-World Example

```go
type Group struct{ shapes []Shape }

func (g *Group) Add(s Shape) { g.shapes = append(g.shapes, s) }
func (g *Group) Draw() {
    for _, s := range g.shapes { s.Draw() }
}
```

## Best Practices

- Define a common interface
- Implement leaf and composite separately
- Handle empty composites gracefully

## Interview Questions

1. How does Go's interface simplify Composite?
2. What is the difference between Composite and Decorator?
3. How do you iterate over a composite tree?
4. Can you have mixed types in a composite?
5. How would you implement DFS vs BFS?

## References

- "Design Patterns" - GoF Chapter 4
- "Head First Design Patterns"
