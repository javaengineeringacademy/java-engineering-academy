# Builder Pattern in Rust

The Builder pattern separates object construction from its representation. Rust builders often use the typestate pattern to enforce compile-time correctness of the build process.

## When to Use

- Complex objects with many optional fields
- Objects that require step-by-step construction
- When you want to enforce required fields at compile time
- Configuration objects with sensible defaults

## Implementation

### Basic Builder

```rust
struct Server {
    host: String,
    port: u16,
    max_connections: u32,
    timeout: u64,
}

struct ServerBuilder {
    host: String,
    port: u16,
    max_connections: u32,
    timeout: u64,
}

impl ServerBuilder {
    fn new(host: &str, port: u16) -> Self {
        ServerBuilder {
            host: host.to_string(),
            port,
            max_connections: 100,
            timeout: 30,
        }
    }

    fn max_connections(mut self, max: u32) -> Self {
        self.max_connections = max;
        self
    }

    fn timeout(mut self, seconds: u64) -> Self {
        self.timeout = seconds;
        self
    }

    fn build(self) -> Server {
        Server {
            host: self.host,
            port: self.port,
            max_connections: self.max_connections,
            timeout: self.timeout,
        }
    }
}
```

### Typestate Builder

```rust
use std::marker::PhantomData;

struct NoUrl;
struct HasUrl;

struct RequestBuilder<UrlState> {
    url: Option<String>,
    method: String,
    _state: PhantomData<UrlState>,
}

impl RequestBuilder<NoUrl> {
    fn new() -> Self {
        RequestBuilder {
            url: None,
            method: "GET".to_string(),
            _state: PhantomData,
        }
    }

    fn url(self, url: &str) -> RequestBuilder<HasUrl> {
        RequestBuilder {
            url: Some(url.to_string()),
            method: self.method,
            _state: PhantomData,
        }
    }
}

impl RequestBuilder<HasUrl> {
    fn method(mut self, method: &str) -> Self {
        self.method = method.to_string();
        self
    }

    fn send(self) -> Result<String, String> {
        Ok(format!("Sending {} to {}", self.method, self.url.unwrap()))
    }
}
```

## Best Practices

- Use typestate pattern when fields have required ordering
- Implement `Default` for objects with sensible defaults
- Consume `self` in builder methods to prevent reuse after build
- Use `impl Into<String>` for string parameters to accept multiple types
- Document which fields are required vs optional

## Interview Questions

1. What is the typestate pattern and why is it useful in builders?
2. How does Rust's ownership system benefit the builder pattern?
3. What is the difference between a builder and a factory?
4. When would you use the builder pattern over struct literals?
5. How do you handle builder validation errors?

## References

- [Typestate Pattern](https://rust-unofficial.github.io/patterns/patterns/creational/builder.html)
- [Build Pattern in Rust](https://docs.rs/derive_builder/)
- [Rust by Example - Builder](https://doc.rust-lang.org/rust-by-example/design_patterns/creational/builder.html)
