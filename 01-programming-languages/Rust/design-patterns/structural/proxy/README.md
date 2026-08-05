# Proxy Pattern in Rust

The Proxy pattern provides a surrogate or placeholder for another object to control access. In Rust, this is implemented as wrapper structs that implement the same trait as the real object.

## When to Use

- Lazy initialization of expensive objects
- Access control and permission checks
- Logging and monitoring
- Remote or virtual proxies
- Caching layer

## Implementation

### Basic Proxy

```rust
trait Database {
    fn query(&self, sql: &str) -> Vec<String>;
}

struct RealDatabase {
    connection_string: String,
}

impl RealDatabase {
    fn connect(&self) {
        println!("Connecting to {}", self.connection_string);
    }
}

impl Database for RealDatabase {
    fn query(&self, sql: &str) -> Vec<String> {
        println!("Executing: {}", sql);
        vec!["result1".to_string(), "result2".to_string()]
    }
}

struct DatabaseProxy {
    real_db: Option<RealDatabase>,
    connection_string: String,
    cache: Vec<(String, Vec<String>)>,
}

impl DatabaseProxy {
    fn new(connection_string: &str) -> Self {
        DatabaseProxy {
            real_db: None,
            connection_string: connection_string.to_string(),
            cache: Vec::new(),
        }
    }

    fn connect(&mut self) {
        if self.real_db.is_none() {
            let db = RealDatabase {
                connection_string: self.connection_string.clone(),
            };
            db.connect();
            self.real_db = Some(db);
        }
    }
}

impl Database for DatabaseProxy {
    fn query(&self, sql: &str) -> Vec<String> {
        if let Some((_, result)) = self.cache.iter().find(|(q, _)| q == sql) {
            println!("Cache hit for: {}", sql);
            return result.clone();
        }

        if let Some(ref db) = self.real_db {
            let result = db.query(sql);
            println!("Caching result for: {}", sql);
            result
        } else {
            vec!["error: not connected".to_string()]
        }
    }
}
```

### Access Control Proxy

```rust
trait Service {
    fn execute(&self, command: &str) -> String;
}

struct AdminProxy {
    user_role: String,
}

impl Service for AdminProxy {
    fn execute(&self, command: &str) -> String {
        if self.user_role == "admin" {
            format!("Executing: {}", command)
        } else {
            "Access denied".to_string()
        }
    }
}
```

## Best Practices

- Keep the proxy interface identical to the real object
- Use lazy initialization for expensive resources
- Implement caching at the proxy level for read-heavy workloads
- Log access patterns through the proxy for monitoring
- Consider using trait objects for dynamic proxy composition

## Interview Questions

1. What are the different types of proxies and their use cases?
2. How does a proxy differ from a decorator in Rust?
3. When would you use a virtual proxy vs a protection proxy?
4. How do you implement a transparent proxy that the client cannot distinguish?
5. How do proxies interact with Rust's ownership system?

## References

- [Rust Design Patterns - Proxy](https://rust-unofficial.github.io/patterns/)
- [Trait Objects](https://doc.rust-lang.org/book/ch17-02-trait-objects-dynamic-dispatch.html)
- [Rust by Example - Traits](https://doc.rust-lang.org/rust-by-example/trait.html)
