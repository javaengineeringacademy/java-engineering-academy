# Sealed Classes (Java 17)

Sealed classes restrict which other classes or interfaces may extend or implement them. This provides more control over inheritance hierarchies.

## Key Features

- **Restricted hierarchy** - Only permitted subclasses can extend
- **Exhaustive checking** - Compiler knows all possible subtypes
- **permits clause** - Lists allowed subclasses
- **Inheritance control** - Subclasses can be final, sealed, or non-sealed

## Syntax

```java
public sealed interface Shape 
    permits Circle, Rectangle, Triangle {
}

public final class Circle implements Shape {}
public sealed class Rectangle implements Shape 
    permits Square {}
public non-sealed class Triangle implements Shape {}
```

## Subclass Modifiers

| Modifier | Description |
|----------|-------------|
| `final` | Cannot be extended further |
| `sealed` | Can only be extended by permitted classes |
| `non-sealed` | Open to extension by any class |

## Rules

1. All permitted subclasses must be in the same module or package
2. A sealed class must have at least one permitted subclass
3. Permitted subclasses must directly extend/implement the sealed class
4. Subclasses must be accessible (not private)

## When to Use

- Modeling algebraic data types
- API design with known subtypes
- Pattern matching with exhaustive checks
- Domain models with fixed variants
