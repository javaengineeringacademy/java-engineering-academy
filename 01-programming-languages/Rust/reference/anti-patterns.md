# Rust Anti-Patterns

## Unwrap Everywhere
```rust
// Bad
let value = some_option.unwrap();

// Good
let value = some_option.unwrap_or_default();
```

## Cloning to Avoid Ownership
```rust
// Bad
let data = expensive_clone();
let data2 = data.clone();

// Good
let data = Rc::new(expensive_data());
let data2 = data.clone();
```

## Deeply Nested match
```rust
// Bad
match a {
    Some(x) => match b {
        Some(y) => match c {
            Some(z) => { /* ... */ }
            None => { /* ... */ }
        }
        None => { /* ... */ }
    },
    None => { /* ... */ }
}

// Good
if let (Some(x), Some(y), Some(z)) = (a, b, c) {
    /* ... */
}
```

## Stringly-Typed APIs
```rust
// Bad
fn process(data: &str) -> bool { /* ... */ }

// Good
fn process(data: &MyStruct) -> bool { /* ... */ }
```

## Ignoring Errors
```rust
// Bad
let _ = file.read(&mut buffer);

// Good
file.read(&mut buffer).context("Failed to read")?;
```
