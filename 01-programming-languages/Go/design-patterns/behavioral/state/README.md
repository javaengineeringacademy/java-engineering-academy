# State Pattern

## Overview

State allows behavior changes based on internal state. Go uses function maps or interface state objects.

## When to Use

- Object behavior depends on state
- Complex state transitions
- Replacing large conditional blocks

## Go Implementation

```go
type State interface{ Handle() string }

type RedLight struct{}
func (r *RedLight) Handle() string { return "Stop" }

type GreenLight struct{}
func (g *GreenLight) Handle() string { return "Go" }

type TrafficLight struct{ current State }

func (t *TrafficLight) Next() {
    switch t.current.(type) {
    case *RedLight:  t.current = &GreenLight{}
    case *GreenLight: t.current = &RedLight{}
    }
}
```

## Go-Idiomatic Alternative

```go
type StateFunc func(*Machine) StateFunc

type Machine struct{ handler StateFunc }

func (m *Machine) Run() {
    for m.handler != nil { m.handler = m.handler(m) }
}

func RedState(m *Machine) StateFunc   { return GreenState }
func GreenState(m *Machine) StateFunc { return RedState }
```

## Real-World Example

```go
type OrderState interface {
    Process(order *Order) OrderState
    Cancel(order *Order) OrderState
}

type PendingState struct{}
func (p *PendingState) Process(o *Order) OrderState {
    o.Status = "processing"
    return &ProcessingState{}
}
func (p *PendingState) Cancel(o *Order) OrderState {
    o.Status = "cancelled"
    return &CancelledState{}
}
```

## Best Practices

- Keep state transitions explicit
- Use state diagrams for visualization
- Encapsulate state-specific behavior

## Interview Questions

1. How do you prevent invalid transitions?
2. What is the difference between State and Strategy?
3. How do you handle concurrent state changes?
4. Can you persist state machine state?
5. How do you test all transitions?

## References

- "Design Patterns" - GoF Chapter 5
- "Domain-Driven Design" - Eric Evans
