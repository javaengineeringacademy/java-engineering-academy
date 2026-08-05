# Composite Pattern in Rust

The Composite pattern composes objects into tree structures and treats individual objects and compositions uniformly. In Rust, this is implemented using enums for sum types or trait objects for dynamic trees.

## When to Use

- Representing hierarchical structures (file systems, UI components, ASTs)
- Treating single and composite objects uniformly
- Building tree-like data structures
- Recursive data structures

## Implementation

### Enum-Based Composite

```rust
#[derive(Debug)]
enum FileTree {
    File { name: String, size: u64 },
    Directory { name: String, children: Vec<FileTree> },
}

impl FileTree {
    fn size(&self) -> u64 {
        match self {
            FileTree::File { size, .. } => *size,
            FileTree::Directory { children, .. } => {
                children.iter().map(|c| c.size()).sum()
            }
        }
    }

    fn display(&self, indent: usize) {
        let prefix = " ".repeat(indent);
        match self {
            FileTree::File { name, size } => {
                println!("{}{} ({} bytes)", prefix, name, size);
            }
            FileTree::Directory { name, children } => {
                println!("{}/", name);
                for child in children {
                    child.display(indent + 2);
                }
            }
        }
    }
}
```

### Trait-Based Composite

```rust
trait Component {
    fn name(&self) -> &str;
    fn size(&self) -> u64;
    fn display(&self, indent: usize);
}

struct Leaf {
    name: String,
    size: u64,
}

impl Component for Leaf {
    fn name(&self) -> &str { &self.name }
    fn size(&self) -> u64 { self.size }
    fn display(&self, indent: usize) {
        println!("{}{} ({} bytes)", " ".repeat(indent), self.name, self.size);
    }
}

struct Composite {
    name: String,
    children: Vec<Box<dyn Component>>,
}

impl Component for Composite {
    fn name(&self) -> &str { &self.name }
    fn size(&self) -> u64 { self.children.iter().map(|c| c.size()).sum() }
    fn display(&self, indent: usize) {
        println!("{}/", self.name);
        for child in &self.children {
            child.display(indent + 2);
        }
    }
}

impl Composite {
    fn add(&mut self, component: Box<dyn Component>) {
        self.children.push(component);
    }
}
```

### Expression Tree

```rust
#[derive(Debug)]
enum Expr {
    Number(f64),
    Add(Box<Expr>, Box<Expr>),
    Multiply(Box<Expr>, Box<Expr>),
}

impl Expr {
    fn evaluate(&self) -> f64 {
        match self {
            Expr::Number(n) => *n,
            Expr::Add(l, r) => l.evaluate() + r.evaluate(),
            Expr::Multiply(l, r) => l.evaluate() * r.evaluate(),
        }
    }
}
```

## Best Practices

- Use enums when the tree structure is fixed and known at compile time
- Use trait objects when the component types may vary at runtime
- Implement display/traversal methods on the composite trait
- Consider using `Rc<RefCell<T>>` for shared ownership in mutable trees
- Document the expected tree depth and structure constraints

## Interview Questions

1. What is the difference between enum-based and trait-based composites?
2. How do you implement iteration over a composite tree?
3. How do you handle mutable trees in Rust safely?
4. When should you use the composite pattern vs a simple vector?
5. How do you prevent cycles in a composite tree?

## References

- [Rust Design Patterns - Composite](https://rust-unofficial.github.io/patterns/)
- [Enums and Pattern Matching](https://doc.rust-lang.org/book/ch06-00-enums.html)
- [Rust by Example - Enums](https://doc.rust-lang.org/rust-by-example/custom_types/enum.html)
