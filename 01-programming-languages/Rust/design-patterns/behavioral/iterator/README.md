# Iterator Pattern in Rust

The Iterator pattern provides a way to access elements of a collection sequentially without exposing its underlying representation. Rust has a built-in `Iterator` trait that makes this pattern idiomatic.

## When to Use

- Traversing collections
- Lazy evaluation of sequences
- Custom data structure traversal
- Filtering and transforming collections
- Implementing range-based operations

## Implementation

### Custom Iterator

```rust
struct Counter {
    count: u32,
    max: u32,
}

impl Counter {
    fn new(max: u32) -> Self {
        Counter { count: 0, max }
    }
}

impl Iterator for Counter {
    type Item = u32;

    fn next(&mut self) -> Option<Self::Item> {
        if self.count < self.max {
            self.count += 1;
            Some(self.count)
        } else {
            None
        }
    }
}

fn main() {
    let counter = Counter::new(5);
    for i in counter {
        println!("{}", i);
    }
}
```

### Iterator Adapter

```rust
struct EvenFilter<I> {
    iter: I,
}

impl<I: Iterator> Iterator for EvenFilter<I>
where
    I::Item: Even,
{
    type Item = I::Item;

    fn next(&mut self) -> Option<Self::Item> {
        self.iter.find(|x| x.is_even())
    }
}

trait Even {
    fn is_even(&self) -> bool;
}

impl Even for i32 {
    fn is_even(&self) -> bool { self % 2 == 0 }
}

struct FilteredIterator<'a, T> {
    data: &'a [T],
    index: usize,
}

impl<'a, T: PartialEq> FilteredIterator<'a, T> {
    fn new(data: &'a [T]) -> Self {
        FilteredIterator { data, index: 0 }
    }
}

impl<'a, T: PartialEq> Iterator for FilteredIterator<'a, T> {
    type Item = &'a T;

    fn next(&mut self) -> Option<Self::Item> {
        while self.index < self.data.len() {
            let item = &self.data[self.index];
            self.index += 1;
            return Some(item);
        }
        None
    }
}
```

### Infinite Iterator

```rust
struct Fibonacci {
    a: u64,
    b: u64,
}

impl Fibonacci {
    fn new() -> Self {
        Fibonacci { a: 0, b: 1 }
    }
}

impl Iterator for Fibonacci {
    type Item = u64;

    fn next(&mut self) -> Option<Self::Item> {
        let result = self.a;
        let new_b = self.a + self.b;
        self.a = self.b;
        self.b = new_b;
        Some(result)
    }
}

fn main() {
    let fibs: Vec<u64> = Fibonacci::new().take(10).collect();
    println!("{:?}", fibs);
}
```

## Best Practices

- Implement `Iterator` for custom collections to enable `for` loops
- Use adapter methods (`map`, `filter`, `take`) for lazy processing
- Implement `IntoIterator` for types that can be consumed into iterators
- Use `collect` with type annotation when specific collection types are needed
- Document iterator item type and yield order

## Interview Questions

1. What is the difference between `Iterator` and `IntoIterator`?
2. How do you implement a bidirectional iterator?
3. What are iterator adapters vs consumer adapters?
4. How do you handle errors in iterators?
5. What are the performance characteristics of lazy vs eager iterators?

## References

- [Iterator trait](https://doc.rust-lang.org/std/iter/trait.Iterator.html)
- [Rust Design Patterns - Iterator](https://rust-unofficial.github.io/patterns/)
- [Rust by Example - Iterator](https://doc.rust-lang.org/rust-by-example/fn/iterator.html)
