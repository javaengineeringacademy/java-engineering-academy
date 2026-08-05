# Strategy Pattern

## Overview

Strategy defines a family of algorithms and makes them interchangeable. Go uses first-class functions for strategies.

## When to Use

- Multiple sorting algorithms
- Different payment methods
- Runtime algorithm selection

## Go Implementation

```go
type SortStrategy func([]int) []int

func BubbleSort(data []int) []int { return data }

type Sorter struct{ strategy SortStrategy }

func NewSorter(s SortStrategy) *Sorter { return &Sorter{strategy: s} }
func (s *Sorter) Sort(data []int) []int { return s.strategy(data) }
```

## Go-Idiomatic Alternative

Direct function passing:

```go
type Processor struct{}

func (p *Processor) Process(data []int, strategy func([]int) []int) []int {
    return strategy(data)
}
```

## Real-World Example

```go
type PaymentStrategy interface {
    Pay(amount float64) error
}

type Checkout struct{ payment PaymentStrategy }

func (c *Checkout) SetPayment(p PaymentStrategy) { c.payment = p }
```

## Best Practices

- Prefer functions over interfaces for simple strategies
- Use interfaces when strategies have multiple methods
- Document strategy contracts

## Interview Questions

1. Functions vs interfaces for Strategy?
2. How does Strategy differ from Command?
3. Can strategies be stateful?
4. How do you test multiple implementations?
5. What are the performance implications?

## References

- "Design Patterns" - GoF Chapter 5
- Go Blog: "Functions are values"
