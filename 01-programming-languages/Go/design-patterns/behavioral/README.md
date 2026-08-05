# Behavioral Design Patterns

## Overview

Behavioral patterns deal with communication between objects, defining how they interact and distribute responsibility. Go's channels and first-class functions make many behavioral patterns especially concise.

## Patterns

| Pattern | Go Idiom | Key Mechanism |
|---------|----------|---------------|
| Observer | Channels | Goroutines receiving on channels |
| Strategy | Functional options | Functions as interchangeable algorithms |
| Command | Functions/interfaces | Callable objects with state |
| Iterator | Channels, closures | Yielding values via channels or callbacks |
| State | State machine | Transitions via function maps |
| Template | Interface composition | Defining skeleton with overridable methods |
| Chain | Middleware | Handlers calling next in chain |
| Mediator | Channels/structs | Central coordinator for communication |
| Memento | Value copying | Snapshot and restore state |
| Visitor | Type switches | Double dispatch via interface methods |
| Interpreter | Recursive descent | Grammar parsing with structs |

## Go Advantages

- **Channels**: Natural Observer and Iterator patterns
- **First-class functions**: Strategy and Command become trivial
- **Interfaces**: Clean decoupling for all behavioral patterns
- **Goroutines**: Non-blocking communication between objects

## When to Use

- **Observer**: Event-driven systems, real-time notifications
- **Strategy**: Algorithm selection at runtime
- **Command**: Undo/redo, task queuing
- **Iterator**: Traversing collections
- **State**: Object behavior changes based on state
- **Template**: Defining algorithm skeletons
- **Chain**: Processing requests through a pipeline

## References

- "Design Patterns" - GoF Chapter 5: Behavioral Patterns
- "Concurrency in Go" - Katherine Cox-Buday
- Go Blog: "Go Proverbs"
