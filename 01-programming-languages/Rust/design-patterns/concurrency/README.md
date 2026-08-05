# Rust Concurrency Patterns

Rust's ownership system provides compile-time guarantees for thread safety. The `Send` and `Sync` traits, along with channels and async/await, enable fearless concurrency.

## Core Concepts

### Send and Sync

```rust
use std::rc::Rc;
use std::sync::{Arc, Mutex};
use std::thread;

// Send: Types that can be transferred between threads
// Sync: Types that can be shared between threads

// Rc is NOT Send or Sync
// Arc is Send and Sync
// Mutex<T> is Send if T is Send
// Mutex<T> is Sync if T is Send
```

### Channel Patterns

```rust
use std::sync::mpsc;
use std::thread;

fn channel_pattern() {
    let (tx, rx) = mpsc::channel();

    thread::spawn(move || {
        tx.send("Hello from thread".to_string()).unwrap();
    });

    let message = rx.recv().unwrap();
    println!("Received: {}", message);
}
```

### Shared State

```rust
use std::sync::{Arc, Mutex};
use std::thread;

fn shared_state_pattern() {
    let counter = Arc::new(Mutex::new(0));
    let mut handles = vec![];

    for _ in 0..10 {
        let counter = Arc::clone(&counter);
        let handle = thread::spawn(move || {
            let mut num = counter.lock().unwrap();
            *num += 1;
        });
        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    }

    println!("Result: {}", *counter.lock().unwrap());
}
```

## Async/Await Patterns

### Basic Async

```rust
use tokio;

async fn fetch_data(url: &str) -> String {
    format!("Data from {}", url)
}

#[tokio::main]
async fn main() {
    let result = fetch_data("https://api.example.com").await;
    println!("{}", result);
}
```

### Concurrent Async Tasks

```rust
use tokio::join;

async fn task_a() -> String { "Task A".to_string() }
async fn task_b() -> String { "Task B".to_string() }

async fn concurrent_tasks() {
    let (a, b) = join!(task_a(), task_b());
    println!("{}, {}", a, b);
}
```

### Async Channels

```rust
use tokio::sync::mpsc;

async fn async_channel() {
    let (tx, mut rx) = mpsc::channel(32);

    tokio::spawn(async move {
        tx.send("async message").await.unwrap();
    });

    while let Some(message) = rx.recv().await {
        println!("Received: {}", message);
    }
}
```

## Actor Model

```rust
use std::sync::mpsc;
use std::thread;

enum ActorMessage {
    Increment,
    GetValue,
    Shutdown,
}

struct CounterActor {
    count: i32,
    receiver: mpsc::Receiver<ActorMessage>,
}

impl CounterActor {
    fn new(receiver: mpsc::Receiver<ActorMessage>) -> Self {
        CounterActor { count: 0, receiver }
    }

    fn run(&mut self) {
        while let Ok(msg) = self.receiver.recv() {
            match msg {
                ActorMessage::Increment => self.count += 1,
                ActorMessage::GetValue => println!("Count: {}", self.count),
                ActorMessage::Shutdown => break,
            }
        }
    }
}

fn main() {
    let (tx, rx) = mpsc::channel();
    let mut actor = CounterActor::new(rx);

    thread::spawn(move || actor.run());

    tx.send(ActorMessage::Increment).unwrap();
    tx.send(ActorMessage::Increment).unwrap();
    tx.send(ActorMessage::GetValue).unwrap();
    tx.send(ActorMessage::Shutdown).unwrap();
}
```

## RwLock Pattern

```rust
use std::sync::{Arc, RwLock};
use std::thread;

fn rwlock_pattern() {
    let data = Arc::new(RwLock::new(vec![1, 2, 3]));
    let mut handles = vec![];

    for i in 0..5 {
        let data = Arc::clone(&data);
        let handle = thread::spawn(move || {
            let read = data.read().unwrap();
            println!("Thread {} read: {:?}", i, *read);
            drop(read);

            let mut write = data.write().unwrap();
            write.push(i);
        });
        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    }
}
```

## Best Practices

- Use `Arc<Mutex<T>>` for shared mutable state across threads
- Use `mpsc` channels for message passing between threads
- Prefer `async/await` for I/O-bound concurrent tasks
- Use `RwLock` when reads vastly outnumber writes
- Keep critical sections short to minimize lock contention
- Document thread-safety guarantees for public APIs

## Interview Questions

1. What is the difference between `Send` and `Sync` traits?
2. When should you use `Mutex` vs `RwLock`?
3. What are the benefits of async/await over threads?
4. How do you handle deadlocks in Rust?
5. What is the actor model and how does it work in Rust?

## References

- [Fearless Concurrency](https://doc.rust-lang.org/book/ch16-00-concurrency.html)
- [Send and Sync](https://doc.rust-lang.org/std/marker/trait.Send.html)
- [Tokio](https://tokio.rs/)
- [Rust Design Patterns - Concurrency](https://rust-unofficial.github.io/patterns/)
