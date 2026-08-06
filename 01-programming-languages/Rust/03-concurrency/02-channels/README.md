# Channels in Rust

## Overview
Channels enable message passing between threads. Rust uses multi-producer, single-consumer (MPSC) channels.

## Creating Channels
```rust
use std::sync::mpsc;

let (tx, rx) = mpsc::channel();
```

## Sending and Receiving
```rust
tx.send("hello").unwrap();
let message = rx.recv().unwrap();
```

## Multiple Producers
```rust
let tx2 = tx.clone();
```

## Iterating Receiver
```rust
for received in rx {
    println!("{}", received);
}
```

## Resources
- [The Rust Book - Channels](https://doc.rust-lang.org/book/ch16-02-message-passing.html)
