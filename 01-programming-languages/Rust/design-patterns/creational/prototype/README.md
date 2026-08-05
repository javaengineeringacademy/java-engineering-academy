# Prototype Pattern in Rust

The Prototype pattern creates new objects by cloning existing instances. In Rust, this maps directly to the `Clone` trait, which provides deep copy semantics.

## When to Use

- Creating copies of expensive-to-build objects
- Avoiding expensive initialization when similar objects are needed
- Preserving object state without re-initialization
- Template-based object creation

## Implementation

### Basic Clone

```rust
#[derive(Clone, Debug)]
struct Document {
    title: String,
    content: String,
    metadata: Vec<String>,
}

fn main() {
    let template = Document {
        title: "Template".to_string(),
        content: "Default content".to_string(),
        metadata: vec!["draft".to_string()],
    };

    let doc1 = template.clone();
    let doc2 = Document {
        content: "Custom content".to_string(),
        ..template.clone()
    };
}
```

### Custom Clone Implementation

```rust
#[derive(Debug)]
struct DatabaseConnection {
    host: String,
    pool: Vec<Connection>,
}

struct Connection;

impl Clone for DatabaseConnection {
    fn clone(&self) -> Self {
        DatabaseConnection {
            host: self.host.clone(),
            pool: Vec::new(),
        }
    }
}
```

### Prototype Registry

```rust
use std::collections::HashMap;

#[derive(Clone)]
struct Shape {
    kind: String,
    color: String,
    size: f64,
}

struct PrototypeRegistry {
    prototypes: HashMap<String, Shape>,
}

impl PrototypeRegistry {
    fn new() -> Self {
        PrototypeRegistry {
            prototypes: HashMap::new(),
        }
    }

    fn register(&mut self, name: &str, shape: Shape) {
        self.prototypes.insert(name.to_string(), shape);
    }

    fn clone_prototype(&self, name: &str) -> Option<Shape> {
        self.prototypes.get(name).cloned()
    }
}
```

## Best Practices

- Derive `Clone` when all fields implement `Clone`
- Implement `Clone` manually when deep copies require special handling
- Use `Rc<RefCell<T>>` or `Arc<Mutex<T>>` for shared ownership clones
- Document whether `clone()` is cheap (shallow) or expensive (deep)
- Consider `Cow<str>` for lazy cloning of string data

## Interview Questions

1. What is the difference between `Clone` and `Copy` in Rust?
2. How do you implement `Clone` for types with non-cloneable fields?
3. When is a clone shallow vs deep in Rust?
4. What are the performance implications of the `Clone` trait?
5. How does `Rc<T>` affect cloning behavior?

## References

- [Clone documentation](https://doc.rust-lang.org/std/clone/trait.Clone.html)
- [Rust Design Patterns - Prototype](https://rust-unofficial.github.io/patterns/)
- [Understanding Clone](https://doc.rust-lang.org/book/deriving.html)
