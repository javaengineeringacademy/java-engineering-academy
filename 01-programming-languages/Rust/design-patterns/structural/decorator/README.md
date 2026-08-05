# Decorator Pattern in Rust

The Decorator pattern adds responsibilities to objects dynamically. In Rust, this is achieved through wrapper structs that implement the same trait as the wrapped type.

## When to Use

- Adding behavior to objects without modifying their code
- Layering cross-cutting concerns (logging, caching, validation)
- Composition over inheritance hierarchies
- Runtime behavior modification

## Implementation

### Basic Decorator

```rust
trait DataSource {
    fn write_data(&self, data: &str);
    fn read_data(&self) -> String;
}

struct FileDataSource {
    filename: String,
}

impl DataSource for FileDataSource {
    fn write_data(&self, data: &str) {
        println!("Writing {} to {}", data, self.filename);
    }

    fn read_data(&self) -> String {
        format!("Data from {}", self.filename)
    }
}

struct EncryptionDecorator {
    wrapped: Box<dyn DataSource>,
}

impl DataSource for EncryptionDecorator {
    fn write_data(&self, data: &str) {
        let encrypted = format!("ENCRYPTED({})", data);
        self.wrapped.write_data(&encrypted);
    }

    fn read_data(&self) -> String {
        let data = self.wrapped.read_data();
        data.trim_start_matches("ENCRYPTED(").trim_end_matches(')').to_string()
    }
}

struct CompressionDecorator {
    wrapped: Box<dyn DataSource>,
}

impl DataSource for CompressionDecorator {
    fn write_data(&self, data: &str) {
        let compressed = format!("COMPRESSED[{}]", data);
        self.wrapped.write_data(&compressed);
    }

    fn read_data(&self) -> String {
        let data = self.wrapped.read_data();
        data.trim_start_matches("COMPRESSED[").trim_end_matches(']').to_string()
    }
}
```

### Stacking Decorators

```rust
fn main() {
    let source = FileDataSource { filename: "data.txt".to_string() };
    let decorated = CompressionDecorator {
        wrapped: Box::new(EncryptionDecorator {
            wrapped: Box::new(source),
        }),
    };

    decorated.write_data("Hello, World!");
    println!("{}", decorated.read_data());
}
```

## Best Practices

- Ensure the decorator implements the same trait as the wrapped type
- Keep decorators single-responsibility; stack multiple for multiple concerns
- Use generics instead of trait objects when the type is known at compile time
- Document which decorators are applied and in what order
- Consider using the newtype pattern for simple decorators

## Interview Questions

1. What is the difference between a decorator and a proxy?
2. How do you prevent decorators from being applied multiple times?
3. How does Rust's ownership model affect decorator design?
4. When should you use trait objects vs generics for decorators?
5. How do you implement a decorator that can modify the return type?

## References

- [Rust Design Patterns - Decorator](https://rust-unofficial.github.io/patterns/)
- [Newtype Pattern](https://doc.rust-lang.org/book/ch19-04-newtype.html)
- [Trait Objects](https://doc.rust-lang.org/book/ch17-02-trait-objects-dynamic-dispatch.html)
