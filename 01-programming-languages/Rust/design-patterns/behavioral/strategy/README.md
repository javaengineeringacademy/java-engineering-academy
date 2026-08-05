# Strategy Pattern in Rust

The Strategy pattern defines a family of algorithms and makes them interchangeable. In Rust, this is implemented using closures, trait objects, or generics for compile-time polymorphism.

## When to Use

- Multiple sorting or filtering algorithms
- Payment processing strategies
- Validation rules
- Compression algorithms
- Route planning algorithms

## Implementation

### Closure-Based Strategy

```rust
struct Sorter<T> {
    data: Vec<T>,
    strategy: Box<dyn Fn(&[T]) -> Vec<T>>,
}

impl<T: Clone + Ord> Sorter<T> {
    fn new(data: Vec<T>, strategy: impl Fn(&[T]) -> Vec<T> + 'static) -> Self {
        Sorter {
            data,
            strategy: Box::new(strategy),
        }
    }

    fn sort(&self) -> Vec<T> {
        (self.strategy)(&self.data)
    }
}

fn bubble_sort<T: Clone + Ord>(data: &[T]) -> Vec<T> {
    let mut result = data.to_vec();
    let len = result.len();
    for i in 0..len {
        for j in 0..len - 1 - i {
            if result[j] > result[j + 1] {
                result.swap(j, j + 1);
            }
        }
    }
    result
}

fn main() {
    let data = vec![5, 3, 1, 4, 2];
    let sorter = Sorter::new(data, bubble_sort);
    println!("{:?}", sorter.sort());
}
```

### Trait Object Strategy

```rust
trait CompressionStrategy {
    fn compress(&self, data: &[u8]) -> Vec<u8>;
}

struct GzipCompression;
struct Lz4Compression;

impl CompressionStrategy for GzipCompression {
    fn compress(&self, data: &[u8]) -> Vec<u8> {
        println!("Compressing with Gzip");
        data.to_vec()
    }
}

impl CompressionStrategy for Lz4Compression {
    fn compress(&self, data: &[u8]) -> Vec<u8> {
        println!("Compressing with LZ4");
        data.to_vec()
    }
}

struct FileProcessor {
    strategy: Box<dyn CompressionStrategy>,
}

impl FileProcessor {
    fn new(strategy: Box<dyn CompressionStrategy>) -> Self {
        FileProcessor { strategy }
    }

    fn process(&self, data: &[u8]) -> Vec<u8> {
        self.strategy.compress(data)
    }
}
```

### Generic Strategy

```rust
trait Validator {
    fn validate(&self, input: &str) -> bool;
}

struct EmailValidator;
struct PhoneValidator;

impl Validator for EmailValidator {
    fn validate(&self, input: &str) -> bool { input.contains('@') }
}

impl Validator for PhoneValidator {
    fn validate(&self, input: &str) -> bool { input.chars().all(|c| c.is_digit() || c == '+') }
}

fn validate_all<V: Validator>(validators: &[V], input: &str) -> bool {
    validators.iter().all(|v| v.validate(input))
}
```

## Best Practices

- Use closures for simple, single-use strategies
- Use trait objects when strategies need state or complex behavior
- Use generics when strategy types are known at compile time
- Implement `Fn` traits for strategy structs to enable closure-like usage
- Document strategy selection criteria and performance characteristics

## Interview Questions

1. What is the difference between strategy and command patterns?
2. When should you use closures vs trait objects for strategies?
3. How do you implement strategy selection at runtime?
4. How do you handle strategy state and configuration?
5. Can strategies be composed? How?

## References

- [Rust Design Patterns - Strategy](https://rust-unofficial.github.io/patterns/)
- [Closures](https://doc.rust-lang.org/book/ch13-01-closures.html)
- [Trait Objects](https://doc.rust-lang.org/book/ch17-02-trait-objects-dynamic-dispatch.html)
