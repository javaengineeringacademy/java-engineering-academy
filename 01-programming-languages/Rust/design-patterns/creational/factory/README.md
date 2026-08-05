# Factory Pattern in Rust

The Factory pattern creates objects without exposing the instantiation logic. In Rust, this is implemented using trait objects, enums, or associated functions.

## When to Use

- Creating objects based on runtime configuration
- Decoupling creation logic from usage
- Supporting multiple concrete types behind a common interface
- Plugin or driver architectures

## Implementation

### Trait Object Factory

```rust
trait Animal {
    fn speak(&self) -> &str;
}

struct Dog;
struct Cat;

impl Animal for Dog {
    fn speak(&self) -> &str { "Woof" }
}

impl Animal for Cat {
    fn speak(&self) -> &str { "Meow" }
}

enum AnimalType {
    Dog,
    Cat,
}

fn create_animal(animal_type: AnimalType) -> Box<dyn Animal> {
    match animal_type {
        AnimalType::Dog => Box::new(Dog),
        AnimalType::Cat => Box::new(Cat),
    }
}

fn main() {
    let animal = create_animal(AnimalType::Dog);
    println!("{}", animal.speak());
}
```

### Enum-Based Factory

```rust
enum Shape {
    Circle(f64),
    Rectangle(f64, f64),
    Triangle(f64, f64, f64),
}

impl Shape {
    fn area(&self) -> f64 {
        match self {
            Shape::Circle(r) => std::f64::consts::PI * r * r,
            Shape::Rectangle(w, h) => w * h,
            Shape::Triangle(a, b, c) => {
                let s = (a + b + c) / 2.0;
                (s * (s - a) * (s - b) * (s - c)).sqrt()
            }
        }
    }
}
```

### Associated Function Factory

```rust
struct Connection {
    host: String,
    port: u16,
}

impl Connection {
    fn new(host: &str, port: u16) -> Self {
        Connection {
            host: host.to_string(),
            port,
        }
    }

    fn from_url(url: &str) -> Self {
        let parts: Vec<&str> = url.split(':').collect();
        Connection {
            host: parts[0].to_string(),
            port: parts[1].parse().unwrap_or(3306),
        }
    }
}
```

## Best Practices

- Use trait objects when you need runtime polymorphism
- Use enums when the set of types is fixed and known at compile time
- Prefer associated functions over standalone factory functions
- Document which type each factory variant produces
- Consider using the builder pattern for complex construction

## Interview Questions

1. What is the difference between a factory and a builder pattern?
2. When would you use an enum-based factory over trait objects?
3. How does Rust's ownership model affect factory design?
4. What are the performance implications of using `Box<dyn Trait>`?
5. How do you extend a factory without modifying existing code?

## References

- [Rust Design Patterns - Creational](https://rust-unofficial.github.io/patterns/)
- [Trait Objects vs Enums](https://doc.rust-lang.org/book/ch17-02-trait-objects-dynamic-dispatch.html)
- [The Rust Programming Language](https://doc.rust-lang.org/book/)
