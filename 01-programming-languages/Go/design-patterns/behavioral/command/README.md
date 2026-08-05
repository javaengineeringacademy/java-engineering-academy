# Command Pattern

## Overview

Command encapsulates a request as an object for parameterization and queuing.

## When to Use

- Undo/redo functionality
- Task queuing and scheduling
- Transaction systems

## Go Implementation

```go
type Command interface {
    Execute() error
    Undo() error
}

type LightOnCommand struct{ light *Light }

func (c *LightOnCommand) Execute() error { c.light.on = true; return nil }
func (c *LightOnCommand) Undo() error    { c.light.on = false; return nil }
```

## Go-Idiomatic Alternative

```go
type Action struct {
    Execute func() error
    Undo    func() error
}

type History struct{ actions []Action }

func (h *History) Do(a Action) error {
    if err := a.Execute(); err != nil { return err }
    h.actions = append(h.actions, a)
    return nil
}

func (h *History) Undo() error {
    if len(h.actions) == 0 { return nil }
    last := h.actions[len(h.actions)-1]
    h.actions = h.actions[:len(h.actions)-1]
    return last.Undo()
}
```

## Real-World Example

```go
type EditCommand struct {
    document *Document
    position int
    text     string
    previous string
}

func (c *EditCommand) Execute() error {
    c.previous = c.document.GetRange(c.position, c.position+len(c.text))
    c.document.Insert(c.position, c.text)
    return nil
}

func (c *EditCommand) Undo() error {
    c.document.Replace(c.position, len(c.text), c.previous)
    return nil
}
```

## Best Practices

- Include both Execute and Undo
- Make commands serializable
- Keep commands small and focused

## Interview Questions

1. How do you implement multi-level undo?
2. What is the difference between Command and Strategy?
3. How do you handle command failures?
4. Can commands be composed?
5. How would you serialize commands?

## References

- "Design Patterns" - GoF Chapter 5
- Go Dev: Effective Go - Methods on functions
