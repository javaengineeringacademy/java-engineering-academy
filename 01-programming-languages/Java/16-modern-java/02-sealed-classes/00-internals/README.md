# Sealed Classes Internals

## Bytecode Changes

Sealed classes introduce new bytecode attributes:

### NestMembers Attribute
Sealed classes record their permitted subclasses in the class file.

### PermittedSubclasses Attribute
Contains the list of classes that are allowed to extend the sealed class.

## Runtime Reflection

Java 17+ provides new reflection APIs:

```java
// Get permitted subclasses
Class<?>[] permitted = Shape.class.getPermittedSubclasses();

// Check if a class is sealed
boolean isSealed = Shape.class.isSealed();

// Check if a class is final
boolean isFinal = Circle.class.isFinal();
```

## Compiler Checks

The compiler enforces:

1. **All permitted subclasses must exist** and be accessible
2. **Subclasses must directly extend/implement** the sealed class
3. **Subclasses must be in the same module** or package
4. **Subclass modifiers must be valid** (final, sealed, or non-sealed)

## Module System Integration

For cross-module sealed classes:

```java
// In module A
module a {
    opens com.example.a to b;
}

// In module B
module b {
    requires a;
}
```

## Pattern Matching Optimization

The compiler can optimize pattern matching switches on sealed types because it knows all possible subtypes.
