# Generics in Rust

## Overview
Generics allow writing flexible, reusable code that works with multiple types.

## Generic Functions
```rust
fn largest<T: PartialOrd>(list: &[T]) -> &T {
    let mut largest = &list[0];
    for item in &list[1..] {
        if item > largest {
            largest = item;
        }
    }
    largest
}
```

## Generic Structs
```rust
struct Point<T> {
    x: T,
    y: T,
}
```

## Generic Enums
```rust
enum Option<T> {
    Some(T),
    None,
}
```

## Generic Implementations
```rust
impl<T> Point<T> {
    fn x(&self) -> &T {
        &self.x
    }
}
```

## Traits Bounds
```rust
fn print_item<T: std::fmt::Display>(item: &T) {
    println!("{}", item);
}
```

## Resources
- [The Rust Book - Generics](https://doc.rust-lang.org/book/ch10-00-generics.html)
