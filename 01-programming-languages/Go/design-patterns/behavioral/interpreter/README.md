# Interpreter Pattern

## Overview

Interpreter defines a grammar and provides an interpreter using recursive descent parsing.

## When to Use

- DSL implementation
- Mathematical expression evaluation
- Rule engines

## Go Implementation

```go
type Expression interface{ Interpret() int }

type Number struct{ value int }
func (n *Number) Interpret() int { return n.value }

type Add struct{ left, right Expression }
func (a *Add) Interpret() int {
    return a.left.Interpret() + a.right.Interpret()
}

type Multiply struct{ left, right Expression }
func (m *Multiply) Interpret() int {
    return m.left.Interpret() * m.right.Interpret()
}
```

## Go-Idiomatic Alternative

```go
type Expr func(map[string]int) int

func Num(n int) Expr {
    return func(_ map[string]int) int { return n }
}

func Add(a, b Expr) Expr {
    return func(env map[string]int) int { return a(env) + b(env) }
}

func Var(name string) Expr {
    return func(env map[string]int) int { return env[name] }
}
```

## Real-World Example

```go
type Rule interface {
    Evaluate(context map[string]interface{}) bool
}

type AndRule struct{ rules []Rule }

func (r *AndRule) Evaluate(ctx map[string]interface{}) bool {
    for _, rule := range r.rules {
        if !rule.Evaluate(ctx) { return false }
    }
    return true
}

type EqualsRule struct {
    Field  string
    Target interface{}
}

func (r *EqualsRule) Evaluate(ctx map[string]interface{}) bool {
    return ctx[r.Field] == r.Target
}
```

## Best Practices

- Use recursive descent for simple grammars
- Build an AST before interpreting
- Keep grammar rules composable

## Interview Questions

1. When would you use Interpreter over a parser generator?
2. How do you handle left recursion?
3. What is the difference between Interpreter and Visitor?
4. How would you add variables?
5. Can you compile to bytecode?

## References

- "Design Patterns" - GoF Chapter 5
- "Writing An Interpreter In Go" - Thorsten Ball
