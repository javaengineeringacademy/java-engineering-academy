# Structural Design Patterns

## Overview

Structural patterns deal with object composition, describing how to assemble objects into larger structures. Go's composition-first approach and interfaces make structural patterns particularly elegant.

## Patterns

| Pattern | Go Idiom | Key Mechanism |
|---------|----------|---------------|
| Adapter | Interface wrapping | New interface wrapping existing type |
| Decorator | Middleware | Function wrapping function |
| Facade | Package-level functions | Simplified API over complex subsystems |
| Proxy | Interface delegation | Intermediary controlling access |
| Composite | Interfaces + slices | Tree structures via recursive interfaces |
| Bridge | Interface embedding | Separating abstraction from implementation |
| Flyweight | Map caching | Shared state via map storage |

## Go Advantages

- **Implicit interfaces**: Adapting types requires no inheritance changes
- **Struct embedding**: Natural delegation for Proxy and Decorator
- **Composition over inheritance**: Structural patterns feel natural
- **First-class functions**: Decorator becomes simple function wrapping

## When to Use

- **Adapter**: Integrating incompatible interfaces
- **Decorator**: Adding behavior without modifying objects
- **Facade**: Simplifying complex subsystems
- **Proxy**: Controlling access, lazy loading, caching
- **Composite**: Treating individual and composed objects uniformly
- **Bridge**: Decoupling abstraction from implementation
- **Flyweight**: Memory optimization for large numbers of similar objects

## References

- "Design Patterns" - GoF Chapter 4: Structural Patterns
- Go Dev: Effective Go - Interfaces
- Go Blog: "Go Proverbs"
