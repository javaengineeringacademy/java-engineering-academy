# Observer Pattern in Rust

The Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all dependents are notified. In Rust, this is implemented using channels, closures, or event emitter patterns.

## When to Use

- Event-driven architectures
- UI notification systems
- Model-view separation
- Distributed systems communication
- Decoupling publishers from subscribers

## Implementation

### Closure-Based Observer

```rust
struct EventEmitter<T> {
    listeners: Vec<Box<dyn Fn(&T)>>,
}

impl<T> EventEmitter<T> {
    fn new() -> Self {
        EventEmitter { listeners: Vec::new() }
    }

    fn on(&mut self, listener: impl Fn(&T) + 'static) {
        self.listeners.push(Box::new(listener));
    }

    fn emit(&self, event: &T) {
        for listener in &self.listeners {
            listener(event);
        }
    }
}

fn main() {
    let mut emitter = EventEmitter::new();
    emitter.on(|event: &String| println!("Listener 1: {}", event));
    emitter.on(|event: &String| println!("Listener 2: {}", event));

    emitter.emit(&"Hello".to_string());
}
```

### Channel-Based Observer

```rust
use std::sync::mpsc;
use std::thread;

struct ChannelObserver {
    sender: mpsc::Sender<String>,
}

impl ChannelObserver {
    fn new(sender: mpsc::Sender<String>) -> Self {
        ChannelObserver { sender }
    }

    fn notify(&self, message: &str) {
        self.sender.send(message.to_string()).unwrap();
    }
}

fn main() {
    let (tx, rx) = mpsc::channel();
    let observer = ChannelObserver::new(tx);

    let handle = thread::spawn(move || {
        while let Ok(msg) = rx.recv() {
            println!("Received: {}", msg);
        }
    });

    observer.notify("Event 1");
    observer.notify("Event 2");
    drop(observer);
    handle.join().unwrap();
}
```

### State Change Observer

```rust
struct Observable {
    value: i32,
    observers: Vec<Box<dyn Fn(i32)>>,
}

impl Observable {
    fn new(initial: i32) -> Self {
        Observable {
            value: initial,
            observers: Vec::new(),
        }
    }

    fn subscribe(&mut self, observer: impl Fn(i32) + 'static) {
        self.observers.push(Box::new(observer));
    }

    fn set_value(&mut self, value: i32) {
        self.value = value;
        for observer in &self.observers {
            observer(self.value);
        }
    }
}
```

## Best Practices

- Use channels for cross-thread observer notifications
- Use closures for in-process lightweight observers
- Consider using `Arc<Mutex<T>>` for thread-safe shared state
- Implement drop to clean up observers automatically
- Document thread-safety guarantees of observer implementations

## Interview Questions

1. How does Rust's ownership model affect the observer pattern?
2. What is the difference between channels and closures for observers?
3. How do you handle observer cleanup when subscribers are dropped?
4. How do you implement thread-safe observers in Rust?
5. When should you use the observer pattern vs direct function calls?

## References

- [Rust Design Patterns - Observer](https://rust-unofficial.github.io/patterns/)
- [Channels](https://doc.rust-lang.org/book/ch16-02-message-passing.html)
- [Closures](https://doc.rust-lang.org/book/ch13-01-closures.html)
