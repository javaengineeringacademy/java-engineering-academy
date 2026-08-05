# Facade Design Pattern

## Overview
Facade pattern provides a unified interface to a set of interfaces in a subsystem. It defines a higher-level interface that makes the subsystem easier to use.

## When to Use
- You want to provide a simple interface to a complex subsystem
- There are many dependencies between clients and the implementation classes
- You want to layer your subsystems and define entry points to each level

## Code Example

```java
public class Computer {
    private final CPU cpu;
    private final Memory memory;
    private final HardDrive hardDrive;

    public Computer() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    public void start() {
        System.out.println("Computer: Starting up...");
        cpu.freeze();
        memory.load(0L, "boot");
        cpu.jump(0L);
        cpu.execute();
    }
}
```

## Common Mistakes
- Putting too much logic in the facade itself
- Making the facade a god class that knows everything
- Not allowing direct access to subsystem classes when needed

## Interview Questions
1. What is the difference between Facade and Mediator patterns?
2. Does Facade add new functionality or just simplify existing?
3. Can Facade be combined with other patterns?
