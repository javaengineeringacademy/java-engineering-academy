# Visitor Pattern in Rust

The Visitor pattern represents an operation to be performed on elements of an object structure. In Rust, this is implemented using enums with `match` or traits with accept methods.

## When to Use

- Operations over heterogeneous data structures
- AST traversal and compilation
- Serialization of complex structures
- Adding operations without modifying element classes
- File system traversal

## Implementation

### Enum-Based Visitor

```rust
enum Expr {
    Number(f64),
    Add(Box<Expr>, Box<Expr>),
    Multiply(Box<Expr>, Box<Expr>),
}

trait Visitor<T> {
    fn visit_number(&mut self, value: f64) -> T;
    fn visit_add(&mut self, left: &Expr, right: &Expr) -> T;
    fn visit_multiply(&mut self, left: &Expr, right: &Expr) -> T;
}

struct Evaluator;

impl Visitor<f64> for Evaluator {
    fn visit_number(&mut self, value: f64) -> f64 { value }
    fn visit_add(&mut self, left: &Expr, right: &Expr) -> f64 {
        left.accept(self) + right.accept(self)
    }
    fn visit_multiply(&mut self, left: &Expr, right: &Expr) -> f64 {
        left.accept(self) * right.accept(self)
    }
}

struct Printer;

impl Visitor<String> for Printer {
    fn visit_number(&mut self, value: f64) -> String {
        format!("{}", value)
    }
    fn visit_add(&mut self, left: &Expr, right: &Expr) -> String {
        format!("({} + {})", left.accept(self), right.accept(self))
    }
    fn visit_multiply(&mut self, left: &Expr, right: &Expr) -> String {
        format!("({} * {})", left.accept(self), right.accept(self))
    }
}

impl Expr {
    fn accept<T>(&self, visitor: &mut dyn Visitor<T>) -> T {
        match self {
            Expr::Number(n) => visitor.visit_number(*n),
            Expr::Add(l, r) => visitor.visit_add(l, r),
            Expr::Multiply(l, r) => visitor.visit_multiply(l, r),
        }
    }
}
```

### Match-Based Visitor

```rust
enum Shape {
    Circle(f64),
    Rectangle(f64, f64),
    Triangle(f64, f64, f64),
}

fn area(shape: &Shape) -> f64 {
    match shape {
        Shape::Circle(r) => std::f64::consts::PI * r * r,
        Shape::Rectangle(w, h) => w * h,
        Shape::Triangle(a, b, c) => {
            let s = (a + b + c) / 2.0;
            (s * (s - a) * (s - b) * (s - c)).sqrt()
        }
    }
}

fn perimeter(shape: &Shape) -> f64 {
    match shape {
        Shape::Circle(r) => 2.0 * std::f64::consts::PI * r,
        Shape::Rectangle(w, h) => 2.0 * (w + h),
        Shape::Triangle(a, b, c) => a + b + c,
    }
}
```

### AST Visitor

```rust
enum AST {
    Literal(i32),
    BinaryOp {
        op: char,
        left: Box<AST>,
        right: Box<AST>,
    },
}

struct ASTVisitor;

impl ASTVisitor {
    fn evaluate(&self, node: &AST) -> i32 {
        match node {
            AST::Literal(value) => *value,
            AST::BinaryOp { op, left, right } => {
                let l = self.evaluate(left);
                let r = self.evaluate(right);
                match op {
                    '+' => l + r,
                    '-' => l - r,
                    '*' => l * r,
                    '/' => l / r,
                    _ => 0,
                }
            }
        }
    }

    fn count_nodes(&self, node: &AST) -> usize {
        match node {
            AST::Literal(_) => 1,
            AST::BinaryOp { left, right, .. } => {
                1 + self.count_nodes(left) + self.count_nodes(right)
            }
        }
    }
}
```

## Best Practices

- Use enums when the element types are fixed and known at compile time
- Use traits when you need to add new visitor implementations independently
- Document which operations each visitor performs
- Consider using `&mut self` for visitors that maintain state
- Use the accept method pattern for extensible visitor hierarchies

## Interview Questions

1. How does Rust's pattern matching simplify the visitor pattern?
2. What is the difference between enum-based and trait-based visitors?
3. How do you add a new operation without modifying existing visitors?
4. How do you handle cyclic structures in visitors?
5. What are the performance implications of the visitor pattern?

## References

- [Rust Design Patterns - Visitor](https://rust-unofficial.github.io/patterns/)
- [Pattern Matching](https://doc.rust-lang.org/book/ch18-00-patterns.html)
- [Rust by Example - Match](https://doc.rust-lang.org/rust-by-example/control_flow/match.html)
