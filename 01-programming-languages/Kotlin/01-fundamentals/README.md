# Kotlin Fundamentals

## Overview
Kotlin fundamentals cover the core building blocks: variables, control flow, functions, and null safety. These concepts form the foundation for all Kotlin development.

## Key Concepts

### Variables
- `val` for immutable (read-only) variables
- `var` for mutable variables
- Type inference reduces boilerplate
- String templates with `$` and `${}`

### Control Flow
- `if` as expression or statement
- `when` replaces switch with pattern matching
- `for`, `while`, `do-while` loops
- Ranges with `..`, `until`, `downTo`, `step`

### Functions
- Named and default parameters
- Single-expression functions
- Higher-order functions and lambdas
- Extension functions

### Null Safety
- Nullable types with `?`
- Safe call operator `?.`
- Elvis operator `?:`
- `let`, `also`, `run`, `apply`, `with`

## Code Reference
| File | Lines | Focus |
|------|-------|-------|
| `variables.kt` | 40-80 | Types, val/var, templates |
| `control-flow.kt` | 40-80 | if, when, for, while |
| `functions.kt` | 40-80 | Parameters, expressions |
| `null-safety.kt` | 40-80 | ?, ?:., let, also |

## Common Mistakes
1. Using `var` when `val` suffices
2. Forgetting null safety with `!!`
3. Not using named arguments for clarity
4. Mixing `when` branches incorrectly
5. Missing `tailrec` for recursive functions

## Interview Questions
1. What is the difference between `val` and `var`?
2. How does Kotlin handle null safety differently from Java?
3. What are extension functions and when would you use them?
4. Explain the difference between `let`, `also`, `run`, `apply`, and `with`.
5. How do ranges work in Kotlin and what are their use cases?
