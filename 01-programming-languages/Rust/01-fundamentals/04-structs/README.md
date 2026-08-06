# Structs in Rust

## Overview
Structs group related data together. They're Rust's primary way to create custom data types.

## Defining Structs
```rust
struct User {
    username: String,
    email: String,
    active: bool,
}
```

## Creating Instances
```rust
let user = User {
    username: String::from("someone"),
    email: String::from("someone@example.com"),
    active: true,
};
```

## Struct Update Syntax
```rust
let user2 = User {
    email: String::from("another@example.com"),
    ..user
};
```

## Tuple Structs
```rust
struct Color(i32, i32, i32);
struct Point(i32, i32, i32);
```

## Methods with impl
```rust
impl Rectangle {
    fn area(&self) -> u32 {
        self.width * self.height
    }
}
```

## Associated Functions
```rust
impl Rectangle {
    fn new(width: u32, height: u32) -> Self {
        Self { width, height }
    }
}
```

## Resources
- [The Rust Book - Structs](https://doc.rust-lang.org/book/ch05-00-structs.html)
