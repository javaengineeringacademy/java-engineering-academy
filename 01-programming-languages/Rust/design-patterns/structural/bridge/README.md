# Bridge Pattern in Rust

The Bridge pattern separates abstraction from implementation so both can vary independently. In Rust, this is achieved using generics with trait bounds or trait objects for dynamic dispatch.

## When to Use

- Avoiding class explosion from multiple dimensions of variation
- Separating platform-specific code from business logic
- When both abstraction and implementation need independent extension
- Cross-platform development

## Implementation

### Trait-Based Bridge

```rust
trait Renderer {
    fn render_circle(&self, x: f64, y: f64, radius: f64);
    fn render_rectangle(&self, x: f64, y: f64, width: f64, height: f64);
}

struct SVGRenderer;
struct CanvasRenderer;

impl Renderer for SVGRenderer {
    fn render_circle(&self, x: f64, y: f64, radius: f64) {
        println!("SVG Circle at ({}, {}) radius {}", x, y, radius);
    }
    fn render_rectangle(&self, x: f64, y: f64, width: f64, height: f64) {
        println!("SVG Rect at ({}, {}) {}x{}", x, y, width, height);
    }
}

impl Renderer for CanvasRenderer {
    fn render_circle(&self, x: f64, y: f64, radius: f64) {
        println!("Canvas Circle at ({}, {}) radius {}", x, y, radius);
    }
    fn render_rectangle(&self, x: f64, y: f64, width: f64, height: f64) {
        println!("Canvas Rect at ({}, {}) {}x{}", x, y, width, height);
    }
}

struct Shape<'a, R: Renderer> {
    renderer: &'a R,
    x: f64,
    y: f64,
}

struct Circle<'a, R: Renderer> {
    shape: Shape<'a, R>,
    radius: f64,
}

impl<'a, R: Renderer> Circle<'a, R> {
    fn draw(&self) {
        self.shape.renderer.render_circle(
            self.shape.x, self.shape.y, self.radius
        );
    }
}
```

### Dynamic Dispatch Bridge

```rust
trait Implementor {
    fn operation_impl(&self) -> String;
}

struct ConcreteImplementorA;
struct ConcreteImplementorB;

impl Implementor for ConcreteImplementorA {
    fn operation_impl(&self) -> String { "A".to_string() }
}

impl Implementor for ConcreteImplementorB {
    fn operation_impl(&self) -> String { "B".to_string() }
}

struct Abstraction {
    implementor: Box<dyn Implementor>,
}

impl Abstraction {
    fn new(implementor: Box<dyn Implementor>) -> Self {
        Abstraction { implementor }
    }

    fn operation(&self) -> String {
        format!("Abstraction({})", self.implementor.operation_impl())
    }
}
```

## Best Practices

- Use generics when implementation types are known at compile time
- Use trait objects when implementations are loaded dynamically
- Keep the abstraction interface stable; vary implementations freely
- Document the relationship between abstraction and implementation dimensions
- Consider using feature flags for platform-specific implementations

## Interview Questions

1. What is the difference between bridge and adapter patterns?
2. When should you use generics vs trait objects for the bridge?
3. How does the bridge pattern reduce code duplication?
4. Can you combine bridge with factory pattern? How?
5. How do you test code that uses the bridge pattern?

## References

- [Rust Design Patterns - Bridge](https://rust-unofficial.github.io/patterns/)
- [Trait Objects vs Generics](https://doc.rust-lang.org/book/ch17-02-trait-objects-dynamic-dispatch.html)
- [Rust by Example - Generics](https://doc.rust-lang.org/rust-by-example/generics.html)
