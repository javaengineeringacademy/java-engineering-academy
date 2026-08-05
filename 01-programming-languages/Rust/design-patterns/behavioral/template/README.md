# Template Method Pattern in Rust

The Template Method pattern defines the skeleton of an algorithm in a base trait, allowing subclasses to override specific steps. In Rust, this is implemented using default trait methods.

## When to Use

- Algorithms with invariant structure but variant steps
- Framework design with customizable hooks
- Code reuse across similar operations
- Building parsers or processors
- Reducing code duplication

## Implementation

### Basic Template Method

```rust
trait DataProcessor {
    fn read_data(&self) -> Vec<String>;
    fn process_item(&self, item: &str) -> String;
    fn write_data(&self, data: &[String]);

    fn run(&self) {
        let raw = self.read_data();
        let processed: Vec<String> = raw.iter()
            .map(|item| self.process_item(item))
            .collect();
        self.write_data(&processed);
    }
}

struct CSVProcessor;
struct JSONProcessor;

impl DataProcessor for CSVProcessor {
    fn read_data(&self) -> Vec<String> {
        vec!["a,b".to_string(), "c,d".to_string()]
    }

    fn process_item(&self, item: &str) -> String {
        item.replace(',', " | ")
    }

    fn write_data(&self, data: &[String]) {
        for item in data {
            println!("CSV: {}", item);
        }
    }
}

impl DataProcessor for JSONProcessor {
    fn read_data(&self) -> Vec<String> {
        vec!["{\"a\":1}".to_string(), "{\"b\":2}".to_string()]
    }

    fn process_item(&self, item: &str) -> String {
        item.to_uppercase()
    }

    fn write_data(&self, data: &[String]) {
        for item in data {
            println!("JSON: {}", item);
        }
    }
}
```

### Template with Default Hooks

```rust
trait Game {
    fn initialize(&self) { println!("Default initialization"); }
    fn play_turn(&self);
    fn check_win(&self) -> bool;
    fn end(&self) { println!("Default ending"); }

    fn play(&self) {
        self.initialize();
        loop {
            self.play_turn();
            if self.check_win() {
                break;
            }
        }
        self.end();
    }
}

struct Chess;
struct TicTacToe;

impl Game for Chess {
    fn play_turn(&self) { println!("Chess turn"); }
    fn check_win(&self) -> bool { false }
}

impl Game for TicTacToe {
    fn initialize(&self) { println!("TicTacToe initialized"); }
    fn play_turn(&self) { println!("TicTacToe turn"); }
    fn check_win(&self) -> bool { true }
}
```

## Best Practices

- Use default methods for invariant steps in the template
- Document which methods are hooks (optional overrides) vs required
- Keep the template method focused; extract complex logic into helper methods
- Consider using the strategy pattern when variation points are few
- Name template methods to reflect the algorithm, not implementation

## Interview Questions

1. What is the difference between template method and strategy pattern?
2. How does Rust's trait system implement template methods?
3. When should you use default methods vs required methods?
4. How do you handle template method error handling?
5. Can template methods call other template methods?

## References

- [Rust Design Patterns - Template Method](https://rust-unofficial.github.io/patterns/)
- [Default Methods](https://doc.rust-lang.org/book/ch10-02-traits.html)
- [Rust by Example - Traits](https://doc.rust-lang.org/rust-by-example/trait.html)
