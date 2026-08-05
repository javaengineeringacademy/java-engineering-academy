# Singleton Pattern in Rust

The Singleton pattern ensures a class has only one instance and provides a global point of access. In Rust, this is achieved using `lazy_static`, `OnceCell`, or `std::sync::OnceLock`.

## When to Use

- Global configuration or settings
- Database connection pools
- Logging instances
- Thread-safe shared state

## Implementation

### Using `OnceLock` (std, Rust 1.80+)

```rust
use std::sync::{Mutex, OnceLock};

struct Config {
    database_url: String,
    max_connections: u32,
}

static CONFIG: OnceLock<Mutex<Config>> = OnceLock::new();

fn get_config() -> &'static Mutex<Config> {
    CONFIG.get_or_init(|| {
        Mutex::new(Config {
            database_url: "localhost:5432".to_string(),
            max_connections: 10,
        })
    })
}

fn main() {
    let config = get_config().lock().unwrap();
    println!("Database URL: {}", config.database_url);
}
```

### Using `lazy_static`

```rust
use lazy_static::lazy_static;
use std::sync::Mutex;

lazy_static! {
    static ref CONFIG: Mutex<Vec<String>> = Mutex::new(Vec::new());
}

fn main() {
    CONFIG.lock().unwrap().push("setting1".to_string());
}
```

### Thread-Safe Initialization

```rust
use std::sync::Once;

static INIT: Once = Once::new();
static mut INSTANCE: Option<String> = None;

fn get_instance() -> &'static str {
    unsafe {
        INIT.call_once(|| {
            INSTANCE = Some("initialized".to_string());
        });
        INSTANCE.as_ref().unwrap()
    }
}
```

## Best Practices

- Prefer `OnceLock` over `lazy_static` for new code
- Keep singleton state minimal and well-documented
- Avoid global mutable state when possible; prefer dependency injection
- Use `Mutex` or `RwLock` for mutable shared state
- Consider whether a singleton is truly needed; often a regular struct suffices

## Interview Questions

1. Why does Rust not have a built-in singleton keyword like C# or Java?
2. What is the difference between `OnceLock` and `lazy_static`?
3. How do you handle mutable state in a Rust singleton?
4. What are the thread-safety guarantees of `OnceLock`?
5. When should you avoid using a singleton in Rust?

## References

- [OnceLock documentation](https://doc.rust-lang.org/std/sync/struct.OnceLock.html)
- [lazy_static crate](https://docs.rs/lazy_static/)
- [Rust Design Patterns - Singleton](https://rust-unofficial.github.io/patterns/patterns/creational/singleton.html)
