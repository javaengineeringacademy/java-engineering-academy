# Kotlin Advanced

## Overview
Advanced Kotlin covers lambdas, higher-order functions, extension functions, and coroutines for building modern, concurrent applications.

## Key Concepts

### Lambdas
- Anonymous functions with concise syntax
- Lambda with receiver for DSL creation
- Closure capture and non-local returns
- SAM conversion for Java interop

### Higher-Order Functions
- Functions that take or return functions
- `map`, `filter`, `reduce`, `fold` operations
- Function composition and currying
- Sequence for lazy evaluation

### Extension Functions
- Add functions to existing classes
- Extension properties
- Generic extensions
- Inline extensions for performance

### Coroutines
- Lightweight concurrent programming
- Structured concurrency with scopes
- Channels for communication
- Flows for reactive streams

## Code Reference
| File | Lines | Focus |
|------|-------|-------|
| `lambdas.kt` | 40-80 | Syntax, closures, references |
| `higher-order-functions.kt` | 40-80 | Collection ops, composition |
| `extension-functions.kt` | 40-80 | Properties, generic, inline |
| `coroutines.kt` | 40-80 | async/await, flow, mutex |

## Common Mistakes
1. Not using `inline` for small lambdas
2. Overusing `runBlocking` in production code
3. Forgetting to handle coroutine exceptions
4. Confusing `launch` with `async`
5. Not using structured concurrency properly

## Interview Questions
1. What is the difference between `launch` and `async`?
2. How do extension functions differ from regular functions?
3. Explain the concept of coroutine scopes and structured concurrency.
4. What are Flows and how do they differ from Channels?
5. When would you use `Mutex` instead of `AtomicReference`?
