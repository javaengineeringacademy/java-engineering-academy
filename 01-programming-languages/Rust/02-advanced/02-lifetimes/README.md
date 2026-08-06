# Lifetimes in Rust

## Overview
Lifetimes ensure that references are always valid. They're编译器 annotations that describe the scope of references.

## Basic Syntax
```rust
&i32        // a reference
&'a i32     // a reference with an explicit lifetime
&'a mut i32 // a mutable reference with an explicit lifetime
```

## Lifetime Annotations
```rust
fn longest<'a>(x: &'a str, y: &'a str) -> &'a str {
    if x.len() > y.len() { x } else { y }
}
```

## Lifetime Elision Rules
1. Each reference parameter gets its own lifetime
2. If there's exactly one input lifetime, it's assigned to all outputs
3. If there's a `&self` or `&mut self`, its lifetime is assigned to outputs

## Struct Lifetimes
```rust
struct ImportantExcerpt<'a> {
    part: &'a str,
}
```

## Static Lifetimes
```rust
let s: &'static str = "I live forever";
```

## Resources
- [The Rust Book - Lifetimes](https://doc.rust-lang.org/book/ch10-03-lifetime-syntax.html)
