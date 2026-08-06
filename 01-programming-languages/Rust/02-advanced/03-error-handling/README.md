# Error Handling in Rust

## Overview
Rust uses `Result<T, E>` for recoverable errors and `panic!` for unrecoverable errors.

## Result<T, E>
```rust
enum Result<T, E> {
    Ok(T),
    Err(E),
}
```

## The ? Operator
Propagates errors up the call stack:
```rust
fn read_file(path: &str) -> Result<String, io::Error> {
    let content = fs::read_to_string(path)?;
    Ok(content)
}
```

## unwrap and expect
```rust
let file = File::open("hello.txt").expect("Failed to open");
```

## Custom Error Types
```rust
#[derive(Debug)]
enum AppError {
    NotFound,
    PermissionDenied,
    NetworkError(String),
}
```

## From Trait for Error Conversion
```rust
impl From<io::Error> for AppError {
    fn from(error: io::Error) -> Self {
        AppError::NetworkError(error.to_string())
    }
}
```

## Resources
- [The Rust Book - Error Handling](https://doc.rust-lang.org/book/ch09-00-error-handling.html)
