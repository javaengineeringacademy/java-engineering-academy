# Interpreter Pattern in Rust

The Interpreter pattern defines a grammar for a language and provides an interpreter for it. In Rust, this is implemented using enums for AST nodes and pattern matching for evaluation.

## When to Use

- Simple language parsing
- Expression evaluation
- Configuration file parsing
- Query languages
- Template engines

## Implementation

### Expression Interpreter

```rust
#[derive(Debug)]
enum Expr {
    Number(f64),
    Add(Box<Expr>, Box<Expr>),
    Subtract(Box<Expr>, Box<Expr>),
    Multiply(Box<Expr>, Box<Expr>),
    Divide(Box<Expr>, Box<Expr>),
}

impl Expr {
    fn evaluate(&self) -> f64 {
        match self {
            Expr::Number(n) => *n,
            Expr::Add(l, r) => l.evaluate() + r.evaluate(),
            Expr::Subtract(l, r) => l.evaluate() - r.evaluate(),
            Expr::Multiply(l, r) => l.evaluate() * r.evaluate(),
            Expr::Divide(l, r) => l.evaluate() / r.evaluate(),
        }
    }

    fn to_string(&self) -> String {
        match self {
            Expr::Number(n) => format!("{}", n),
            Expr::Add(l, r) => format!("({} + {})", l.to_string(), r.to_string()),
            Expr::Subtract(l, r) => format!("({} - {})", l.to_string(), r.to_string()),
            Expr::Multiply(l, r) => format!("({} * {})", l.to_string(), r.to_string()),
            Expr::Divide(l, r) => format!("({} / {})", l.to_string(), r.to_string()),
        }
    }
}

fn main() {
    let expr = Expr::Add(
        Box::new(Expr::Number(5.0)),
        Box::new(Expr::Multiply(
            Box::new(Expr::Number(3.0)),
            Box::new(Expr::Number(2.0)),
        )),
    );
    println!("{} = {}", expr.to_string(), expr.evaluate());
}
```

### Rule Interpreter

```rust
trait Rule {
    fn evaluate(&self, context: &Context) -> bool;
}

struct Context {
    temperature: f64,
    humidity: f64,
    wind_speed: f64,
}

struct AndRule {
    left: Box<dyn Rule>,
    right: Box<dyn Rule>,
}

impl Rule for AndRule {
    fn evaluate(&self, context: &Context) -> bool {
        self.left.evaluate(context) && self.right.evaluate(context)
    }
}

struct TemperatureRule {
    min: f64,
    max: f64,
}

impl Rule for TemperatureRule {
    fn evaluate(&self, context: &Context) -> bool {
        context.temperature >= self.min && context.temperature <= self.max
    }
}

struct HumidityRule {
    threshold: f64,
}

impl Rule for HumidityRule {
    fn evaluate(&self, context: &Context) -> bool {
        context.humidity >= self.threshold
    }
}
```

### Simple Calculator

```rust
fn parse_and_evaluate(input: &str) -> f64 {
    let tokens: Vec<&str> = input.split_whitespace().collect();
    let mut stack: Vec<f64> = Vec::new();

    for token in tokens {
        match token {
            "+" => {
                let b = stack.pop().unwrap();
                let a = stack.pop().unwrap();
                stack.push(a + b);
            }
            "-" => {
                let b = stack.pop().unwrap();
                let a = stack.pop().unwrap();
                stack.push(a - b);
            }
            "*" => {
                let b = stack.pop().unwrap();
                let a = stack.pop().unwrap();
                stack.push(a * b);
            }
            "/" => {
                let b = stack.pop().unwrap();
                let a = stack.pop().unwrap();
                stack.push(a / b);
            }
            n => {
                stack.push(n.parse().unwrap());
            }
        }
    }
    stack.pop().unwrap()
}

fn main() {
    let result = parse_and_evaluate("3 4 + 2 *");
    println!("Result: {}", result);
}
```

## Best Practices

- Use enums for simple grammars; use traits for extensible grammars
- Implement both parsing and evaluation in the interpreter
- Document the grammar syntax and supported operations
- Consider using a parser combinator library for complex grammars
- Add error handling for malformed expressions

## Interview Questions

1. When should you use the interpreter pattern vs a parser library?
2. How do you handle operator precedence?
3. How do you implement error recovery in interpreters?
4. What are the limitations of the interpreter pattern?
5. How do you optimize interpreter performance?

## References

- [Rust Design Patterns - Interpreter](https://rust-unofficial.github.io/patterns/)
- [Enums](https://doc.rust-lang.org/book/ch06-00-enums.html)
- [Pattern Matching](https://doc.rust-lang.org/book/ch18-00-patterns.html)
