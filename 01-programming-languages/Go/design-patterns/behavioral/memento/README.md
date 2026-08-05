# Memento Pattern

## Overview

Memento captures object state for later restoration using value copying.

## When to Use

- Undo/redo functionality
- Checkpointing
- Transaction rollback

## Go Implementation

```go
type Memento struct{ state map[string]string }

type Editor struct {
    content string
    history []Memento
}

func (e *Editor) Save() Memento {
    return Memento{state: map[string]string{"content": e.content}}
}

func (e *Editor) Restore(m Memento) { e.content = m.state["content"] }
```

## Go-Idiomatic Alternative

```go
type Snapshot struct {
    Content string
    Cursor  int
}

type Editor struct {
    content   string
    cursor    int
    snapshots []Snapshot
}

func (e *Editor) Snapshot() Snapshot {
    return Snapshot{Content: e.content, Cursor: e.cursor}
}

func (e *Editor) Restore(s Snapshot) {
    e.content = s.Content
    e.cursor = s.Cursor
}
```

## Real-World Example

```go
type DocumentMemento struct {
    Title, Body string
    Modified    time.Time
}

type Document struct {
    title, body string
    history     []DocumentMemento
}

func (d *Document) Save() {
    d.history = append(d.history, DocumentMemento{
        Title: d.title, Body: d.body, Modified: d.modified,
    })
}

func (d *Document) Undo() bool {
    if len(d.history) == 0 { return false }
    last := d.history[len(d.history)-1]
    d.history = d.history[:len(d.history)-1]
    d.title, d.body = last.Title, last.Body
    return true
}
```

## Best Practices

- Keep mementos small and immutable
- Use deep copy for slices and maps
- Limit history size

## Interview Questions

1. How do you deep copy a memento with maps?
2. What is the difference between Memento and Prototype?
3. How do you limit history size?
4. Can mementos be shared across objects?
5. How would you implement persistent storage?

## References

- "Design Patterns" - GoF Chapter 5
- Go Dev: Effective Go - Allocation with new
