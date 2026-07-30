# Inheritance

## What is Inheritance?
Mechanism where a new class (subclass) derives from an existing class (superclass), inheriting fields and methods.

## Syntax
```java
public class SubClass extends SuperClass { ... }
```

## What is Inherited?
| Member | Inherited? |
|--------|------------|
| `public` fields/methods | ✓ |
| `protected` fields/methods | ✓ |
| Package-private (same package) | ✓ |
| `private` fields/methods | ✗ (accessible via getters) |
| Constructors | ✗ (but `super()` calls parent) |

## `super` Keyword
```java
public class Child extends Parent {
    public Child() {
        super(); // Call parent no-arg constructor
    }

    @Override
    public void method() {
        super.method(); // Call parent implementation
        // child-specific logic
    }
}
```

## Rules
- Java supports **single inheritance** (one parent)
- Multiple inheritance via interfaces
- All classes implicitly extend `Object`

## `final` Keyword
| Applied To | Effect |
|------------|--------|
| Class | Cannot be extended |
| Method | Cannot be overridden |
| Field | Value cannot change after initialization |
| Variable | Reference cannot change |