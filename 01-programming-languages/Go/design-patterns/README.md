# Go Design Patterns

## Overview

Design patterns are reusable solutions to common software design problems. Go's simplicity, first-class functions, interfaces, and goroutines make many patterns more elegant and idiomatic than in traditional OOP languages.

## Pattern Categories

| Category | Patterns | Focus |
|----------|----------|-------|
| Creational | Singleton, Factory, Abstract Factory, Builder, Prototype | Object creation |
| Structural | Adapter, Decorator, Facade, Proxy, Composite, Bridge, Flyweight | Object composition |
| Behavioral | Observer, Strategy, Command, Iterator, State, Template, Chain, Mediator, Memento, Visitor, Interpreter | Object communication |
| Concurrency | Fan-in/Fan-out, Pipeline, Worker Pool, Goroutine | Go-specific patterns |

## Go-Specific Characteristics

- **Interfaces are implicit**: No `implements` keyword needed
- **First-class functions**: Enable functional patterns like Strategy and Factory
- **Goroutines and channels**: Natural Observer and Pipeline patterns
- **Composition over embedding**: Struct embedding replaces deep inheritance
- **`sync` package**: Provides Singleton via `sync.Once`

## Directory Structure

```
design-patterns/
  creational/       - Object creation patterns
  structural/       - Object composition patterns
  behavioral/       - Object interaction patterns
  concurrency/      - Go-specific concurrent patterns
```

## References

- "Design Patterns: Elements of Reusable Object-Oriented Software" - GoF
- "Concurrency in Go" - Katherine Cox-Buday
- Go Blog: "Go Proverbs" - Rob Pike
- Go Dev: https://go.dev/doc/effective_go
