# Visitor Pattern

## Overview

Visitor adds operations to objects without modifying them. Go uses type switches or interface visitors.

## When to Use

- Adding operations to complex object structures
- Compilers and AST processing
- Serialization of object graphs

## Go Implementation

```go
type Visitor interface {
    VisitCircle(c *Circle)
    VisitSquare(s *Square)
}

type Shape interface{ Accept(v Visitor) }

type Circle struct{ Radius float64 }
func (c *Circle) Accept(v Visitor) { v.VisitCircle(c) }

type Square struct{ Side float64 }
func (s *Square) Accept(v Visitor) { v.VisitSquare(s) }

type AreaCalculator struct{ Area float64 }

func (a *AreaCalculator) VisitCircle(c *Circle) {
    a.Area += 3.14 * c.Radius * c.Radius
}

func (a *AreaCalculator) VisitSquare(s *Square) {
    a.Area += s.Side * s.Side
}
```

## Go-Idiomatic Alternative

```go
func CalculateArea(shape Shape) float64 {
    switch s := shape.(type) {
    case *Circle: return 3.14 * s.Radius * s.Radius
    case *Square: return s.Side * s.Side
    default:      return 0
    }
}
```

## Real-World Example

```go
type ASTNode interface{ Accept(NodeVisitor) }

type BinaryExpr struct {
    Left  ASTNode
    Op    string
    Right ASTNode
}

func (b *BinaryExpr) Accept(v NodeVisitor) {
    b.Left.Accept(v)
    b.Right.Accept(v)
    v.VisitBinary(b)
}

type TypeChecker struct{ types map[string]string }

func (tc *TypeChecker) VisitBinary(b *BinaryExpr) { /* check types */ }
```

## Best Practices

- Use when operations change more than types
- Keep visitors in separate files
- Use type switches for simple cases

## Interview Questions

1. How does double dispatch work in Go?
2. When would you use type switches over Visitor?
3. How do you add a new type to existing visitors?
4. What problems does Visitor solve?
5. Can visitors maintain state?

## References

- "Design Patterns" - GoF Chapter 5
- "Compilers: Principles, Techniques, and Tools"
