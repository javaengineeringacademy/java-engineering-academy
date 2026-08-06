# Threads in Rust

## Overview
Rust provides OS threads via `std::thread`. The ownership system prevents data races at compile time.

## Spawning Threads
```rust
use std::thread;

let handle = thread::spawn(|| {
    for i in 1..10 {
        println!("spawned thread: {}", i);
        thread::sleep(std::time::Duration::from_millis(1));
    }
});
```

## Joining Threads
```rust
handle.join().unwrap();
```

## move Closures
Move data into threads:
```rust
let v = vec![1, 2, 3];
let handle = thread::spawn(move || {
    println!("vector: {:?}", v);
});
```

## Common Patterns
1. `move` to transfer ownership to thread
2. `join` to wait for completion
3. Use channels for communication

## Resources
- [The Rust Book - Threads](https://doc.rust-lang.org/book/ch16-01-threads.html)
