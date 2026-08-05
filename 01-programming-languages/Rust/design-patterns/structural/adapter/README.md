# Adapter Pattern in Rust

The Adapter pattern converts the interface of a class into another interface clients expect. In Rust, this is implemented via trait implementations that bridge incompatible interfaces.

## When to Use

- Integrating third-party libraries with different interfaces
- Making existing types work with new code without modification
- Unifying multiple interfaces into a single common interface
- Legacy system integration

## Implementation

### Trait-Based Adapter

```rust
trait MediaPlayer {
    fn play(&self, file: &str);
}

struct VlcPlayer;

impl VlcPlayer {
    fn play_vlc(&self, path: &str) {
        println!("Playing VLC file: {}", path);
    }
}

struct VlcAdapter {
    player: VlcPlayer,
}

impl MediaPlayer for VlcAdapter {
    fn play(&self, file: &str) {
        self.player.play_vlc(file);
    }
}
```

### Generic Adapter

```rust
trait Target {
    fn request(&self) -> String;
}

struct LegacyService;

impl LegacyService {
    fn legacy_method(&self, input: i32) -> String {
        format!("Legacy result: {}", input)
    }
}

struct Adapter<T> {
    inner: T,
}

impl<T> Target for Adapter<T>
where
    T: LegacyServiceTrait,
{
    fn request(&self) -> String {
        self.inner.execute()
    }
}
```

### Multiple Adapter Implementations

```rust
trait Logger {
    fn log(&self, message: &str);
}

struct ConsoleLogger;
struct FileLogger;
struct NetworkLogger;

impl Logger for ConsoleLogger {
    fn log(&self, message: &str) { println!("[CONSOLE] {}", message); }
}

impl Logger for FileLogger {
    fn log(&self, message: &str) { println!("[FILE] {}", message); }
}

impl Logger for NetworkLogger {
    fn log(&self, message: &str) { println!("[NETWORK] {}", message); }
}
```

## Best Practices

- Implement the target trait on the adapter struct
- Keep adapters lightweight; prefer composition over inheritance
- Use generics when adapting multiple types to the same interface
- Document the mapping between old and new interfaces
- Test adapters with both source and target interface contracts

## Interview Questions

1. How does Rust's trait system simplify the adapter pattern?
2. What is the difference between an adapter and a facade?
3. When would you use a generic adapter vs a specific adapter?
4. How do you handle adapters that need to maintain state?
5. Can you combine the adapter pattern with the decorator pattern?

## References

- [Rust Design Patterns - Adapter](https://rust-unofficial.github.io/patterns/)
- [Trait Implementations](https://doc.rust-lang.org/book/ch10-02-traits.html)
- [Rust by Example - Traits](https://doc.rust-lang.org/rust-by-example/trait.html)
