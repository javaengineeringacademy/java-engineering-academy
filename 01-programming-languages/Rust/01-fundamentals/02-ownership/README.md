# Ownership in Rust

## Overview
Ownership is Rust's most unique feature. It enables memory safety without garbage collection.

## Ownership Rules
1. Each value has exactly one owner
2. When the owner goes out of scope, the value is dropped

## Move Semantics
When you assign a value to another variable, ownership moves:
```rust
let s1 = String::from("hello");
let s2 = s1; // s1 is no longer valid
// println!("{}", s1); // error!
```

## Clone
Clone creates a deep copy:
```rust
let s1 = String::from("hello");
let s2 = s1.clone(); // both are valid
```

## Ownership with Functions
- Passing to a function moves ownership
- Returning a value transfers ownership back

## Drop
The `Drop` trait is called when a value goes out of scope:
```rust
struct MyStruct;
impl Drop for MyStruct {
    fn drop(&mut self) {
        println!("Dropped!");
    }
}
```

## Common Patterns
1. Return values to transfer ownership
2. Use references for borrowing
3. Clone when needed

## Resources
- [The Rust Book - Ownership](https://doc.rust-lang.org/book/ch04-00-understanding-ownership.html)
