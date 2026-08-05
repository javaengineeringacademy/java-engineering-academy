# Abstract Factory Pattern

## Overview

Abstract Factory creates families of related objects without specifying concrete classes.

## When to Use

- UI themes with multiple components
- Database drivers with connections and queries
- Supporting multiple platforms

## Go Implementation

```go
type Button interface { Render() string }
type TextField interface { Render() string }

type DialogFactory interface {
    CreateButton() Button
    CreateTextField() TextField
}

type WindowsFactory struct{}
func (f *WindowsFactory) CreateButton() Button     { return &WindowsButton{} }
func (f *WindowsFactory) CreateTextField() TextField { return &WindowsTextField{} }
```

## Go-Idiomatic Alternative

Function groups returning interfaces:

```go
type UIFactory struct {
    NewButton    func() Button
    NewTextField func() TextField
}
```

## Real-World Example

```go
type DBFactory interface {
    NewDatabase() Database
    NewMigration() Migration
}

type PostgresFactory struct{}
func (f *PostgresFactory) NewDatabase() Database   { return &PostgresDB{} }
func (f *PostgresFactory) NewMigration() Migration { return &PostgresMigration{} }
```

## Best Practices

- Define small, focused interfaces for each product
- Return interfaces from factory methods
- Document which products belong to which family

## Interview Questions

1. What is the difference between Factory and Abstract Factory?
2. How do you handle new product types without breaking factories?
3. When would you use Abstract Factory over simple Factory?
4. How does Go's implicit interfaces affect Abstract Factory?
5. Can you compose multiple Abstract Factories?

## References

- "Design Patterns" - GoF Chapter 3
- Go Dev: Effective Go - Interfaces
