# Facade Pattern (C#)

## Overview

The Facade pattern provides a unified interface to a set of interfaces in a subsystem.
C# classes can wrap complex subsystems to provide simpler APIs.

## When to Use

- Simplifying complex library or framework usage
- Providing layered architecture
- Decoupling subsystems from clients
- Creating service layers

## C# Implementation

### Basic Facade

```csharp
public class CPU
{
    public void Freeze() => Console.WriteLine("Freezing CPU");
    public void Jump(long address) => Console.WriteLine($"Jumping to {address}");
    public void Execute() => Console.WriteLine("Executing");
}

public class Memory
{
    public void Load(long address, string data) =>
        Console.WriteLine($"Loading data at {address}");
}

public class HardDrive
{
    public string Read(long sector, int size) =>
        $"Data from sector {sector}";
}

public class ComputerFacade
{
    private readonly CPU _cpu = new CPU();
    private readonly Memory _memory = new Memory();
    private readonly HardDrive _hd = new HardDrive();

    public void Start()
    {
        _cpu.Freeze();
        _memory.Load(0, _hd.Read(0, 1024));
        _cpu.Jump(0);
        _cpu.Execute();
    }
}
```

### With Dependency Injection

```csharp
public interface IComputer
{
    void Start();
    void Shutdown();
}

public class Computer : IComputer
{
    private readonly CPU _cpu;
    private readonly Memory _memory;

    public Computer(CPU cpu, Memory memory)
    {
        _cpu = cpu;
        _memory = memory;
    }

    public void Start() { /* ... */ }
    public void Shutdown() { /* ... */ }
}
```

## Best Practices

- Keep facade focused and minimal
- Don't add business logic to facade
- Use interfaces for facade abstraction
- Consider facade as thin layer only
- Document subsystem dependencies

## Interview Questions

1. What is the difference between Facade and Adapter?
2. Does Facade add new functionality?
3. When should you use Facade vs direct subsystem access?
4. Can Facade be combined with other patterns?
5. How do you test code using Facade?

## References

- Microsoft Docs: Facade Pattern
- "Design Patterns" by Gamma et al.
- "Clean Architecture" by Robert C. Martin
