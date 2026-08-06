# Rust Patterns

## Builder Pattern
```rust
struct QueryBuilder {
    table: String,
    conditions: Vec<String>,
}

impl QueryBuilder {
    fn new(table: &str) -> Self {
        Self { table: table.to_string(), conditions: vec![] }
    }
    fn where_clause(mut self, condition: &str) -> Self {
        self.conditions.push(condition.to_string());
        self
    }
}
```

## Newtype Pattern
```rust
struct Meters(f64);
struct Kilometers(f64);
```

## RAII Pattern
Resource Acquisition Is Initialization:
```rust
struct Connection;
impl Drop for Connection {
    fn drop(&mut self) { println!("Connection closed"); }
}
```

## Typestate Pattern
```rust
struct Request<State> { /* ... */ }
struct Created;
struct Sent;
```

## Strategy Pattern (via Traits)
```rust
trait SortStrategy {
    fn sort(&self, data: &mut [i32]);
}
```

## Observer Pattern (via Channels)
```rust
use std::sync::mpsc;
```
