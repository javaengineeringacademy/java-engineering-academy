# Mediator Pattern

## Overview

Mediator encapsulates object interaction, promoting loose coupling via channels or central structs.

## When to Use

- Chat systems with multiple participants
- UI components that interact
- Complex event coordination

## Go Implementation

```go
type Mediator interface {
    Notify(sender string, event string)
}

type ChatRoom struct{ users map[string]*User }

func (c *ChatRoom) Notify(sender, event string) {
    for name, user := range c.users {
        if name != sender { user.Receive(event) }
    }
}

type User struct {
    name     string
    mediator Mediator
}

func (u *User) Send(msg string) { u.mediator.Notify(u.name, msg) }
func (u *User) Receive(msg string) {
    fmt.Printf("%s received: %s\n", u.name, msg)
}
```

## Go-Idiomatic Alternative

```go
type EventBus struct{ ch chan Message }

type Message struct {
    From    string
    Payload string
}

func (e *EventBus) Start() {
    for msg := range e.ch { /* broadcast */ }
}
```

## Real-World Example

```go
type AirTrafficControl struct{ planes map[string]*Plane }

func (atc *AirTrafficControl) Notify(planeID, event string) {
    for id, plane := range atc.planes {
        if id != planeID {
            plane.ReceiveStatus(fmt.Sprintf("%s is %s", planeID, event))
        }
    }
}
```

## Best Practices

- Keep mediator focused on coordination
- Avoid God object mediators
- Use channels for async communication

## Interview Questions

1. What is the difference between Mediator and Observer?
2. How do you prevent mediator becoming a God object?
3. When would you use channels vs structs?
4. How do you test components depending on mediator?
5. Can you have distributed mediators?

## References

- "Design Patterns" - GoF Chapter 5
- "Concurrency in Go" - Chapter 6
