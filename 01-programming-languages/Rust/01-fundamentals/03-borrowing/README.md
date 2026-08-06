# Borrowing in Rust

## Overview
Borrowing allows you to use a value without taking ownership.

## References
- `&T`: Immutable reference (can have multiple)
- `&mut T`: Mutable reference (only one at a time)

## Immutable References
```rust
let s = String::from("hello");
let len = calculate_length(&s); // borrow s
println!("'{}' has length {}", s, len); // s is still valid
```

## Mutable References
```rust
let mut s = String::from("hello");
change(&mut s); // can modify s
```

## Rules
1. Can have many `&T` OR one `&mut T`
2. References must always be valid (no dangling references)

## Lifetimes
References have a lifetime - how long they're valid:
```rust
fn longest<'a>(x: &'a str, y: &'a str) -> &'a str {
    if x.len() > y.len() { x } else { y }
}
```

## Common Issues
- Dangling references (compile error)
- Multiple mutable references (compile error)
- Lifetime mismatches

## Resources
- [The Rust Book - References](https://doc.rust-lang.org/book/ch04-02-references-and-borrowing.html)
