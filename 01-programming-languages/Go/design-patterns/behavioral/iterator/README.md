# Iterator Pattern

## Overview

Iterator provides sequential access without exposing representation. Go uses channels and closures.

## When to Use

- Traversing complex data structures
- Lazy evaluation
- Generator-like behavior

## Go Implementation

```go
type Iterator interface {
    hasNext() bool
    next() int
}

type NumberSlice struct {
    data []int
    pos  int
}

func (n *NumberSlice) hasNext() bool { return n.pos < len(n.data) }
func (n *NumberSlice) next() int {
    val := n.data[n.pos]
    n.pos++
    return val
}
```

## Go-Idiomatic Alternative

Channel-based iterators:

```go
func Filter(data []int, fn func(int) bool) <-chan int {
    ch := make(chan int)
    go func() {
        for _, v := range data {
            if fn(v) { ch <- v }
        }
        close(ch)
    }()
    return ch
}

for v := range Filter(data, func(n int) bool { return n > 5 }) {
    fmt.Println(v)
}
```

## Real-World Example

```go
func WalkTree(node *Node) <-chan *Node {
    ch := make(chan *Node)
    go func() {
        defer close(ch)
        var traverse func(n *Node)
        traverse = func(n *Node) {
            if n == nil { return }
            ch <- n
            traverse(n.Left)
            traverse(n.Right)
        }
        traverse(node)
    }()
    return ch
}
```

## Best Practices

- Use channels for concurrent iteration
- Close channels to signal completion
- Support early termination with select

## Interview Questions

1. How do channels implement Iterator?
2. What is the difference between channel and closure iterators?
3. How do you support early termination?
4. Can you iterate maps in specific order?
5. How do you handle iterator invalidation?

## References

- "Design Patterns" - GoF Chapter 5
- Go Blog: "Range over functions"
