# Chain of Responsibility Pattern in Rust

The Chain of Responsibility pattern passes a request along a chain of handlers until one handles it. In Rust, this is implemented using enums, trait objects, or linked structures.

## When to Use

- Request processing pipelines
- Middleware stacks (web frameworks)
- Event handling chains
- Logging levels
- Approval workflows

## Implementation

### Trait-Based Chain

```rust
trait Handler {
    fn handle(&self, request: &str) -> Option<String>;
    fn set_next(&mut self, next: Box<dyn Handler>);
}

struct AuthHandler {
    next: Option<Box<dyn Handler>>,
}

impl Handler for AuthHandler {
    fn handle(&self, request: &str) -> Option<String> {
        if request.contains("auth") {
            Some("AuthHandler processed".to_string())
        } else if let Some(ref next) = self.next {
            next.handle(request)
        } else {
            None
        }
    }

    fn set_next(&mut self, next: Box<dyn Handler>) {
        self.next = Some(next);
    }
}

struct ValidationHandler {
    next: Option<Box<dyn Handler>>,
}

impl Handler for ValidationHandler {
    fn handle(&self, request: &str) -> Option<String> {
        if request.contains("valid") {
            Some("ValidationHandler processed".to_string())
        } else if let Some(ref next) = self.next {
            next.handle(request)
        } else {
            None
        }
    }

    fn set_next(&mut self, next: Box<dyn Handler>) {
        self.next = Some(next);
    }
}
```

### Enum-Based Chain

```rust
enum LogLevel {
    Debug,
    Info,
    Warning,
    Error,
}

enum LogHandler {
    Console { min_level: LogLevel },
    File { path: String, min_level: LogLevel },
    Email { to: String, min_level: LogLevel },
}

impl LogHandler {
    fn handle(&self, level: &LogLevel, message: &str) -> bool {
        match self {
            LogHandler::Console { min_level } => {
                if self.level_sufficient(level, min_level) {
                    println!("[CONSOLE] {:?}", message);
                    return true;
                }
            }
            LogHandler::File { path, min_level } => {
                if self.level_sufficient(level, min_level) {
                    println!("[FILE:{}] {:?}", path, message);
                    return true;
                }
            }
            LogHandler::Email { to, min_level } => {
                if self.level_sufficient(level, min_level) {
                    println!("[EMAIL:{} -> {}] {:?}", message, to, message);
                    return true;
                }
            }
        }
        false
    }

    fn level_sufficient(&self, level: &LogLevel, min: &LogLevel) -> bool {
        let level_val = match level {
            LogLevel::Debug => 0,
            LogLevel::Info => 1,
            LogLevel::Warning => 2,
            LogLevel::Error => 3,
        };
        let min_val = match min {
            LogLevel::Debug => 0,
            LogLevel::Info => 1,
            LogLevel::Warning => 2,
            LogLevel::Error => 3,
        };
        level_val >= min_val
    }
}
```

### Middleware Chain

```rust
struct MiddlewareChain {
    middlewares: Vec<Box<dyn Fn(&str) -> Option<String>>>,
}

impl MiddlewareChain {
    fn new() -> Self {
        MiddlewareChain { middlewares: Vec::new() }
    }

    fn add(&mut self, middleware: impl Fn(&str) -> Option<String> + 'static) {
        self.middlewares.push(Box::new(middleware));
    }

    fn execute(&self, request: &str) -> Option<String> {
        for middleware in &self.middlewares {
            if let Some(result) = middleware(request) {
                return Some(result);
            }
        }
        None
    }
}
```

## Best Practices

- Keep handlers independent; avoid coupling between chain elements
- Document the chain order and handler responsibilities
- Use `Option<Box<dyn Handler>>` for optional next handlers
- Implement `Default` for handlers to simplify chain construction
- Consider using a builder to construct complex chains

## Interview Questions

1. How does the chain of responsibility differ from the observer pattern?
2. When should you break the chain vs returning None?
3. How do you handle circular chains in Rust?
4. How do you test individual handlers in a chain?
5. What are the performance implications of long chains?

## References

- [Rust Design Patterns - Chain of Responsibility](https://rust-unofficial.github.io/patterns/)
- [Trait Objects](https://doc.rust-lang.org/book/ch17-02-trait-objects-dynamic-dispatch.html)
- [Rust by Example - Traits](https://doc.rust-lang.org/rust-by-example/trait.html)
