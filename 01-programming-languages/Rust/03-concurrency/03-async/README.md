# Async Programming in Rust

## Overview
Rust's `async/await` provides efficient asynchronous programming without runtime overhead.

## Async Functions
```rust
async fn fetch_data() -> String {
    String::from("data")
}
```

## Await
```rust
let data = fetch_data().await;
```

## Tokio Runtime
```rust
#[tokio::main]
async fn main() {
    let data = fetch_data().await;
}
```

## Spawning Tasks
```rust
tokio::spawn(async {
    // task code
});
```

## Resources
- [Tokio Documentation](https://tokio.rs/tokio/tutorial)
