# State Pattern (C#)

## Overview

The State pattern allows an object to alter its behavior when its internal state changes.
C# uses interfaces and polymorphism to implement state transitions cleanly.

## When to Use

- Object behavior depends on its state
- Complex conditional statements based on state
- State transitions are explicit
- Large number of states

## C# Implementation

### Basic State

```csharp
public interface IState
{
    void Handle(VendingMachine machine);
}

public class IdleState : IState
{
    public void Handle(VendingMachine machine)
    {
        Console.WriteLine("Inserting coin...");
        machine.SetState(new HasCoinState());
    }
}

public class HasCoinState : IState
{
    public void Handle(VendingMachine machine)
    {
        Console.WriteLine("Dispensing product...");
        machine.SetState(new IdleState());
    }
}

public class VendingMachine
{
    private IState _state;

    public VendingMachine()
    {
        _state = new IdleState();
    }

    public void SetState(IState state) => _state = state;

    public void Request() => _state.Handle(this);
}
```

### State with Transitions

```csharp
public abstract class State
{
    protected readonly Order _context;

    protected State(Order context) => _context = context;

    public abstract void Next();
    public abstract string Status { get; }
}

public class NewOrderState : State
{
    public NewOrderState(Order context) : base(context) { }

    public override void Next() => _context.State = new ProcessingState(_context);

    public override string Status => "New";
}
```

### State Machine

```csharp
public class StateMachine<TState, TTrigger>
{
    private readonly Dictionary<(TState, TTrigger), TState> _transitions = new();
    private TState _currentState;

    public void AddTransition(TState state, TTrigger trigger, TState next)
    {
        _transitions[(state, trigger)] = next;
    }

    public void Fire(TTrigger trigger)
    {
        if (_transitions.TryGetValue((_currentState, trigger), out var next))
            _currentState = next;
    }
}
```

## Best Practices

- Keep state classes small and focused
- Make state transitions explicit
- Document state diagrams
- Consider using state machine libraries for complex states
- Handle invalid transitions gracefully

## Interview Questions

1. How does State differ from Strategy?
2. Can states contain behavior?
3. How do you handle invalid state transitions?
4. When should you use State vs conditional logic?
5. How do you implement state entry/exit actions?

## References

- Microsoft Docs: State Pattern
- "Design Patterns" by Gamma et al.
- Stateless library documentation
