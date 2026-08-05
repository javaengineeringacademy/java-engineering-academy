# Observer Pattern

## Overview

Observer defines a one-to-many dependency where changes notify dependents. Go's channels provide concurrent implementation.

## When to Use

- Event-driven systems
- Real-time notifications
- Decoupling producers from consumers

## Go Implementation

```go
type Event struct {
    Type    string
    Payload interface{}
}

type Observer struct{ Ch chan Event }

type Subject struct{ observers []*Observer }

func (s *Subject) Subscribe() *Observer {
    obs := &Observer{Ch: make(chan Event, 10)}
    s.observers = append(s.observers, obs)
    return obs
}

func (s *Subject) Notify(event Event) {
    for _, obs := range s.observers { obs.Ch <- event }
}
```

## Go-Idiomatic Alternative

Channel-based pub/sub:

```go
type EventBus struct {
    subscribers map[string][]chan Event
    mu          sync.RWMutex
}

func (b *Bus) Publish(topic string, event Event) {
    b.mu.RLock()
    defer b.mu.RUnlock()
    for _, ch := range b.subscribers[topic] { ch <- event }
}
```

## Real-World Example

```go
type UserService struct {
    listeners []func(UserCreatedEvent)
}

func (s *UserService) CreateUser(name string) {
    event := UserCreatedEvent{User: User{Name: name}}
    for _, fn := range s.listeners { go fn(event) }
}
```

## Best Practices

- Use buffered channels to prevent blocking
- Provide unsubscribe mechanism
- Avoid holding locks during notification

## Interview Questions

1. How do channels implement Observer?
2. What happens if an observer blocks?
3. How do you implement unsubscribe?
4. What is the difference between Observer and Pub/Sub?
5. How do you handle observer errors?

## References

- "Design Patterns" - GoF Chapter 5
- "Concurrency in Go" - Chapter 5
