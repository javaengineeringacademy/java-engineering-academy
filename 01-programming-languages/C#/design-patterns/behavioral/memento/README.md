# Memento Pattern (C#)

## Overview

The Memento pattern provides the ability to restore an object to its previous state.
C# uses serialization, records, or explicit memento objects for state capture.

## When to Use

- Need to save and restore object state
- Implementing undo/redo functionality
- Capturing snapshots without exposing internals
- Transaction rollback mechanisms

## C# Implementation

### Basic Memento

```csharp
public class Memento
{
    public string State { get; }
    public DateTime Timestamp { get; }

    public Memento(string state)
    {
        State = state;
        Timestamp = DateTime.Now;
    }
}

public class Originator
{
    public string State { get; set; }

    public Memento CreateMemento() => new Memento(State);

    public void Restore(Memento memento) => State = memento.State;
}

public class Caretaker
{
    private readonly Stack<Memento> _history = new();

    public void Save(Originator originator)
    {
        _history.Push(originator.CreateMemento());
    }

    public void Undo(Originator originator)
    {
        if (_history.Count > 0)
            originator.Restore(_history.Pop());
    }
}
```

### Serializable Memento

```csharp
public class SerializableMemento
{
    public byte[] Serialize(object obj)
    {
        using var ms = new MemoryStream();
        var formatter = new BinaryFormatter();
        formatter.Serialize(ms, obj);
        return ms.ToArray();
    }

    public T Deserialize<T>(byte[] data)
    {
        using var ms = new MemoryStream(data);
        var formatter = new BinaryFormatter();
        return (T)formatter.Deserialize(ms);
    }
}
```

### Record-Based Memento

```csharp
public record GameState(int Health, int Score, string Level);

public class Game
{
    public GameState State { get; set; }

    public GameState Save() => State;
    public void Load(GameState state) => State = state;
}
```

## Best Practices

- Keep memento small and focused
- Consider using serialization for complex states
- Use records for immutable mementos
- Limit history size to prevent memory issues
- Document state capture semantics

## Interview Questions

1. What is the difference between Memento and Command?
2. How do you handle large object states?
3. Can memento be used across sessions?
4. When should you use Memento vs Command for undo?
5. How do you implement memento with serialization?

## References

- Microsoft Docs: Memento Pattern
- "Design Patterns" by Gamma et al.
- "Head First Design Patterns" by Freeman
