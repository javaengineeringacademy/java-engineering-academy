# Kotlin OOP

## Overview
Kotlin's OOP system builds on Java's foundation with cleaner syntax, data classes, sealed classes, and powerful interface delegation.

## Key Concepts

### Classes
- Primary and secondary constructors
- Init blocks for validation
- Companion objects for factory methods
- Visibility modifiers (public, private, protected, internal)

### Data Classes
- Auto-generated equals, hashCode, toString
- Copy function with named parameters
- Destructuring declarations
- Component functions

### Interfaces
- Multiple interface implementation
- Default method implementations
- Interface delegation with `by`
- Property declarations in interfaces

### Inheritance
- `open` keyword for inheritable classes
- Abstract classes and functions
- Method overriding with `override`
- Super calls for chaining

## Code Reference
| File | Lines | Focus |
|------|-------|-------|
| `classes.kt` | 40-80 | Constructors, visibility, sealed |
| `data-classes.kt` | 40-80 | Auto-generated methods, copy |
| `interfaces.kt` | 40-80 | Delegation, SAM, properties |
| `inheritance.kt` | 40-80 | Open, abstract, override |

## Common Mistakes
1. Forgetting `open` on classes that need inheritance
2. Not using data classes for value objects
3. Overusing inheritance instead of composition
4. Ignoring interface delegation benefits
5. Missing `@JvmStatic` in companion objects for Java interop

## Interview Questions
1. What is the difference between `class` and `data class`?
2. How does interface delegation work in Kotlin?
3. When would you use a sealed class over an enum?
4. Explain the `open` keyword and its implications.
5. What are the benefits of using abstract classes over interfaces?
