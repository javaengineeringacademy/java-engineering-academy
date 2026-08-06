# Enums in Rust

## Overview
Enums define a type by enumerating its possible variants. They're powerful with pattern matching.

## Defining Enums
```rust
enum IpAddr {
    V4(u8, u8, u8, u8),
    V6(String),
}
```

## Method on Enums
```rust
impl IpAddr {
    fn is_ipv4(&self) -> bool {
        matches!(self, IpAddr::V4(..))
    }
}
```

## Option<T>
Represents optional values:
```rust
let some_number: Option<i32> = Some(5);
let no_number: Option<i32> = None;
```

## Result<T, E>
Represents success or failure:
```rust
let result: Result<i32, String> = Ok(42);
```

## match Expression
Pattern matching:
```rust
match ip {
    IpAddr::V4(a, b, c, d) => println!("{}.{}.{}.{}", a, b, c, d),
    IpAddr::V6(s) => println!("{}", s),
}
```

## Resources
- [The Rust Book - Enums](https://doc.rust-lang.org/book/ch06-00-enums.html)
