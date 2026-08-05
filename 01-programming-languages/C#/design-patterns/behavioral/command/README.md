# Command Pattern (C#)

## Overview

The Command pattern encapsulates a request as an object, letting you parameterize
clients with different requests, queue requests, and support undo operations. C#
leverages interfaces and delegates for command implementations.

## When to Use

- Parameterizing objects with operations
- Queueing, logging, or supporting undo operations
- Decoupling sender from receiver
- Implementing transactional behavior

## C# Implementation

### Basic Command

```csharp
public interface ICommand
{
    void Execute();
    void Undo();
}

public class Light
{
    public void On() => Console.WriteLine("Light on");
    public void Off() => Console.WriteLine("Light off");
}

public class LightOnCommand : ICommand
{
    private readonly Light _light;

    public LightOnCommand(Light light) => _light = light;

    public void Execute() => _light.On();
    public void Undo() => _light.Off();
}

public class RemoteControl
{
    private readonly Stack<ICommand> _history = new();
    private ICommand _command;

    public void SetCommand(ICommand command) => _command = command;

    public void PressButton()
    {
        _command.Execute();
        _history.Push(_command);
    }

    public void PressUndo()
    {
        if (_history.Count > 0)
            _history.Pop().Undo();
    }
}
```

### With Delegates

```csharp
public class CommandWithDelegate
{
    private readonly Action _execute;
    private readonly Action _undo;

    public CommandWithDelegate(Action execute, Action undo)
    {
        _execute = execute;
        _undo = undo;
    }

    public void Execute() => _execute();
    public void Undo() => _undo();
}
```

### Macro Command

```csharp
public class MacroCommand : ICommand
{
    private readonly List<ICommand> _commands = new();

    public void Add(ICommand command) => _commands.Add(command);

    public void Execute()
    {
        foreach (var cmd in _commands)
            cmd.Execute();
    }

    public void Undo()
    {
        foreach (var cmd in _commands.AsEnumerable().Reverse())
            cmd.Undo();
    }
}
```

## Best Practices

- Keep commands focused and single-purpose
- Support undo operations when needed
- Use parameterized commands for variations
- Consider command queueing for async operations
- Document command lifecycle

## Interview Questions

1. How does Command enable undo functionality?
2. What is the difference between Command and Strategy?
3. Can commands be composed into macros?
4. How do you handle command queuing?
5. When should you use Command over direct method calls?

## References

- Microsoft Docs: Command Pattern
- "Design Patterns" by Gamma et al.
- "Head First Design Patterns" by Freeman
