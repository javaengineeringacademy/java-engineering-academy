# Variables in Rust

## Overview
Variables in Rust are immutable by default. Use `mut` to make them mutable.

## Key Concepts

### Variable Declaration
```rust
let x = 5; // immutable
let mut y = 10; // mutable
```

### Type Inference
Rust can infer types from context:
```rust
let x = 5; // i32
let y = 5.0; // f64
let z = "hello"; // &str
```

### Explicit Types
```rust
let x: i32 = 5;
let y: f64 = 5.0;
let z: String = String::from("hello");
```

### Shadowing
Shadowing allows redeclaring a variable with the same name:
```rust
let x = 5;
let x = x + 1; // x is now 6
let x = x * 2; // x is now 12
```

### Constants
Constants are always immutable and must have type annotations:
```rust
const MAX_POINTS: u32 = 100_000;
```

### Scope and Lifetime
Variables have a scope from declaration to the end of the block:
```rust
{
    let x = 5;
    println!("{}", x); // works here
}
// println!("{}", x); // error: x is out of scope
```

## Common Types
- Integers: `i8`, `i16`, `i32`, `i64`, `i128`, `u8`, `u16`, `u32`, `u64`, `u128`
- Floats: `f32`, `f64`
- Boolean: `bool` (`true`, `false`)
- Character: `char`
- String: `String`, `&str`

## Common Mistakes
- Forgetting to add `mut` for mutable variables
- Type mismatches (Rust is strongly typed)
- Shadowing vs mutation confusion

## Next Steps
- [Ownership](../02-ownership/README.md)
- [Borrowing](../03-borrowing/README.md)
