# Adapter Pattern

## Overview

Adapter converts one interface into another clients expect. Go's implicit interfaces make adapters trivial.

## When to Use

- Integrating third-party libraries
- Wrapping legacy code
- Combining types under a common interface

## Go Implementation

```go
type OldPrinter interface {
    PrintOld(text string) string
}

type NewPrinter interface {
    Print(text string) string
}

type PrinterAdapter struct{ legacy *LegacyPrinter }

func (a *PrinterAdapter) Print(text string) string {
    return a.legacy.PrintOld(text)
}
```

## Go-Idiomatic Alternative

Function adapter:

```go
type PrintFunc func(string) string

func (f PrintFunc) Print(text string) string { return f(text) }

func AdaptLegacy(fn func(string) string) NewPrinter {
    return PrintFunc(fn)
}
```

## Real-World Example

```go
type LogrusAdapter struct{ zap *ZapLogger }

func (a *LogrusAdapter) Info(msg string)  { a.zap.Log("INFO: " + msg) }
func (a *LogrusAdapter) Error(msg string) { a.zap.Log("ERROR: " + msg) }
```

## Best Practices

- Keep adapters thin and focused
- Place adapters in separate packages
- Use interfaces to define the target contract

## Interview Questions

1. How does Go's implicit interface satisfaction simplify Adapter?
2. When should you use Adapter vs refactoring the original?
3. Can you stack multiple adapters?
4. How do you test adapters in isolation?
5. What is the difference between Adapter and Decorator?

## References

- "Design Patterns" - GoF Chapter 4
- Go Dev: Effective Go - Interfaces
