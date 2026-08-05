# Rust Anti-Patterns

## 1. Clone Abuse
**Description:** Using `.clone()` everywhere to avoid ownership issues.

**Why it's bad:** Defeats Rust's ownership system, increases memory usage, hurts performance.

**Example (bad code):**
```rust
fn process(data: String) -> String {
    let clone = data.clone();
    format!("Processing: {}", clone)
}
```

**Better approach:** Use references or restructure:
```rust
fn process(data: &str) -> String {
    format!("Processing: {}", data)
}

// Or take ownership when needed
fn process(data: String) -> String {
    format!("Processing: {}", data)
}
```

**Impact:** Better performance, proper ownership usage.

---

## 2. unwrap() Everywhere
**Description:** Using `.unwrap()` on Result/Option types.

**Why it's bad:** Panics on error, not production-safe.

**Example (bad code):**
```rust
let file = File::open("data.txt").unwrap();
let content = fs::read_to_string("data.txt").unwrap();
```

**Better approach:** Handle errors properly:
```rust
let file = File::open("data.txt").map_err(|e| format!("Failed: {}", e))?;
let content = fs::read_to_string("data.txt")?;
```

**Impact:** Graceful error handling, no panics.

---

## 3. Fighting the Borrow Checker
**Description:** Working around borrow checker with unnecessary cloning or RefCell.

**Why it's bad:** Indicates design issues, adds runtime overhead.

**Example (bad code):**
```rust
struct Graph {
    nodes: RefCell<Vec<Node>>,
}

impl Graph {
    fn add_node(&self, node: Node) {
        self.nodes.borrow_mut().push(node);
    }
}
```

**Better approach:** Restructure to work with ownership:
```rust
struct Graph {
    nodes: Vec<Node>,
}

impl Graph {
    fn add_node(&mut self, node: Node) {
        self.nodes.push(node);
    }
}
```

**Impact:** Better design, no runtime overhead.

---

## 4. Not Using_iter() Properly
**Description:** Using index-based loops instead of iterators.

**Why it's bad:** Less idiomatic, more error-prone, misses optimizations.

**Example (bad code):**
```rust
let mut sum = 0;
for i in 0..vec.len() {
    sum += vec[i];
}
```

**Better approach:** Use iterators:
```rust
let sum: i32 = vec.iter().sum();
```

**Impact:** More idiomatic, potential optimizations.

---

## 5. String vs &str Confusion
**Description:** Using String where &str would suffice.

**Why it's bad:** Unnecessary allocations, less flexible APIs.

**Example (bad code):**
```rust
fn greet(name: String) -> String {
    format!("Hello, {}", name)
}
```

**Better approach:** Use &str for input:
```rust
fn greet(name: &str) -> String {
    format!("Hello, {}", name)
}
```

**Impact:** Fewer allocations, more flexible.

---

## 6. Not Using Option Properly
**Description:** Using null pointers or sentinel values instead of Option.

**Why it's bad:** Unsafe, not idiomatic, can cause UB.

**Example (bad code):**
```rust
// C-style null check
let ptr: *const i32 = std::ptr::null();
if !ptr.is_null() {
    // use ptr
}
```

**Better approach:** Use Option:
```rust
let value: Option<&i32> = None;
if let Some(v) = value {
    // use v
}
```

**Impact:** Safe, idiomatic Rust.

---

## 7. Ignoring Compiler Warnings
**Description:** Not addressing compiler warnings.

**Why it's bad:** Warnings often indicate bugs or suboptimal code.

**Example (bad code):**
```rust
// Warning: unused variable
let x = 5;

// Warning: unused result
fs::write("file.txt", "content");
```

**Better approach:** Fix warnings:
```rust
let _x = 5;  // or remove if unused
fs::write("file.txt", "content").unwrap();  // or handle error
```

**Impact:** Cleaner code, fewer bugs.

---

## 8. Not Using trait Objects Properly
**Description:** Using Box<dyn Trait> when generics would be better.

**Why it's bad:** Dynamic dispatch overhead, less optimization.

**Example (bad code):**
```rust
fn process(items: Vec<Box<dyn Shape>>) {
    for item in items {
        item.draw();
    }
}
```

**Better approach:** Use generics when possible:
```rust
fn process<T: Shape>(items: &[T]) {
    for item in items {
        item.draw();
    }
}
```

**Impact:** Static dispatch, better performance.

---

## 9. Overusing unsafe
**Description:** Using unsafe blocks unnecessarily.

**Why it's bad:** Defeats Rust's safety guarantees, potential UB.

**Example (bad code):**
```rust
unsafe {
    let ptr = &mut value as *mut i32;
    *ptr = 10;
}
```

**Better approach:** Use safe alternatives:
```rust
value = 10;  // if you own it
```

**Impact:** Maintains safety guarantees.

---

## 10. Not Using Cargo.toml Properly
**Description:** Not using features, over-depending on crates.

**Why it's bad:** Larger binaries, more dependencies, slower builds.

**Example (bad code):**
```toml
[dependencies]
tokio = { version = "1", features = ["full"] }
```

**Better approach:** Use minimal features:
```toml
[dependencies]
tokio = { version = "1", features = ["rt", "macros"] }
```

**Impact:** Smaller binaries, faster builds.

---

## 11. Ignoring Lifetime Annotations
**Description:** Adding unnecessary lifetime annotations.

**Why it's bad:** Clutters code, can indicate design issues.

**Example (bad code):**
```rust
struct Foo<'a> {
    data: &'a str,
}

impl<'a> Foo<'a> {
    fn get_data(&'a self) -> &'a str {
        self.data
    }
}
```

**Better approach:** Let lifetime elision work:
```rust
struct Foo<'a> {
    data: &'a str,
}

impl<'a> Foo<'a> {
    fn get_data(&self) -> &str {
        self.data
    }
}
```

**Impact:** Cleaner code.

---

## 12. Not Using Error Context
**Description:** Not providing context for errors.

**Why it's bad:** Hard to debug, unclear error chains.

**Example (bad code):**
```rust
fn read_config() -> Result<Config, io::Error> {
    let content = fs::read_to_string("config.toml")?;
    // ...
}
```

**Better approach:** Add context:
```rust
use anyhow::{Context, Result};

fn read_config() -> Result<Config> {
    let content = fs::read_to_string("config.toml")
        .context("Failed to read config file")?;
    // ...
}
```

**Impact:** Better debugging, clearer error messages.