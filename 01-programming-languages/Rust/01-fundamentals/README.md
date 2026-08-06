# Rust Fundamentals

## Overview
Rust fundamentals cover ownership, borrowing, structs, and enums. These concepts are unique to Rust and form the foundation for safe, concurrent programming.

## Key Concepts

### Variables
- Immutable by default, use `mut` for mutability
- Shadowing allows re-binding with same name
- Type inference with optional annotations
- Constants with `const` and `static`

### Ownership
- Each value has exactly one owner
- Ownership moves on assignment
- Values dropped when owner goes out of scope
- `Clone` for explicit copying

### Borrowing
- Immutable references (`&T`) for read access
- Mutable references (`&mut T`) for exclusive access
- No dangling references guaranteed
- Lifetimes ensure validity

### Structs
- Named fields for clarity
- Tuple structs for lightweight grouping
- Methods with `impl` blocks
- Derived traits like `Debug`, `Clone`

### Enums
- Variants can hold different data types
- Pattern matching with `match`
- `Option` and `Result` for error handling
- Method implementations on enums

## Code Reference
| File | Lines | Focus |
|------|-------|-------|
| `variables.rs` | 40-80 | Types, mutability, shadowing |
| `ownership.rs` | 40-80 | Move, clone, copy traits |
| `borrowing.rs` | 40-80 | References, lifetimes, slices |
| `structs.rs` | 40-80 | Methods, update syntax, tuples |
| `enums.rs` | 40-80 | Variants, pattern matching |

## Common Mistakes
1. Using `clone()` when borrowing suffices
2. Not understanding move semantics
3. Creating dangling references
4. Fighting the borrow checker
5. Forgetting lifetime annotations

## Interview Questions
1. What are Rust's ownership rules?
2. How does borrowing differ from ownership?
3. Explain the difference between `String` and `&str`.
4. What are lifetimes and why are they needed?
5. How do enums differ from structs in Rust?
