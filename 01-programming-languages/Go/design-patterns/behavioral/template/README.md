# Template Method Pattern

## Overview

Template Method defines algorithm skeleton, deferring steps to subclasses via interface composition.

## When to Use

- Common algorithm with varying steps
- Code reuse across implementations
- Enforcing fixed operation sequence

## Go Implementation

```go
type DataMiner interface {
    Mine()
    ExtractData(file string) string
    ParseData(raw string) interface{}
    Analyze(data interface{})
}

type BaseMiner struct{}

func (m *BaseMiner) Mine() {
    raw := m.ExtractData("input.csv")
    data := m.ParseData(raw)
    m.Analyze(data)
}

type CSVMiner struct{ BaseMiner }

func (m *CSVMiner) ExtractData(f string) string       { return "csv data" }
func (m *CSVMiner) ParseData(raw string) interface{}  { return raw }
func (m *CSVMiner) Analyze(data interface{})           { fmt.Println("CSV analysis") }
```

## Go-Idiomatic Alternative

```go
type Pipeline struct {
    Extract func(string) string
    Parse   func(string) interface{}
    Analyze func(interface{})
}

func (p *Pipeline) Run(file string) {
    raw := p.Extract(file)
    data := p.Parse(raw)
    p.Analyze(data)
}
```

## Real-World Example

```go
type Notifier interface {
    FormatMessage(msg string) string
    Send(formatted string) error
}

type BaseNotifier struct{}

func (n *BaseNotifier) Notify(msg string) error {
    formatted := n.FormatMessage(msg)
    return n.Send(formatted)
}

type EmailNotifier struct{ BaseNotifier }
func (e *EmailNotifier) FormatMessage(msg string) string {
    return fmt.Sprintf("<h1>%s</h1>", msg)
}
```

## Best Practices

- Keep template method in base type
- Document extension points
- Prefer composition for reuse

## Interview Questions

1. How does Go's composition replace inheritance?
2. What is the difference between Template and Strategy?
3. Can you override the template method itself?
4. How do you handle optional steps?
5. How would you make a template composable?

## References

- "Design Patterns" - GoF Chapter 5
- Go Dev: Effective Go - Embedding
