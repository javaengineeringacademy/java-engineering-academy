# Traits in Rust

## Overview
Traits define shared behavior. They're similar to interfaces in other languages.

## Defining Traits
```rust
trait Summary {
    fn summarize(&self) -> String;
}
```

## Implementing Traits
```rust
impl Summary for NewsArticle {
    fn summarize(&self) -> String {
        format!("{}, by {}", self.title, self.author)
    }
}
```

## Default Implementations
```rust
trait Summary {
    fn summarize(&self) -> String {
        String::from("(Read more...)")
    }
}
```

## Trait Bounds
```rust
fn notify(item: &impl Summary) {
    println!("Breaking news! {}", item.summarize());
}
```

## Multiple Bounds
```rust
fn notify<T: Summary + Display>(item: &T) {
    println!("{}", item);
}
```

## where Clause
```rust
fn some_function<T, U>(t: &T, u: &U) -> i32
where
    T: Display + Clone,
    U: Clone + Debug,
{
    // implementation
}
```

## Resources
- [The Rust Book - Traits](https://doc.rust-lang.org/book/ch10-02-traits.html)
