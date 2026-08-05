# Facade Pattern in Rust

The Facade pattern provides a simplified interface to a complex subsystem. In Rust, this is implemented as a struct that wraps multiple components and exposes a clean API.

## When to Use

- Simplifying complex library APIs
- Providing a unified interface to subsystems
- Reducing coupling between client code and subsystems
- Creating layer architectures

## Implementation

### Basic Facade

```rust
struct CPU;
struct Memory;
struct HardDrive;

impl CPU {
    fn freeze(&self) { println!("CPU: Freezing"); }
    fn jump(&self, address: u32) { println!("CPU: Jumping to {}", address); }
    fn execute(&self) { println!("CPU: Executing"); }
}

impl Memory {
    fn load(&self, address: u32, data: &str) {
        println!("Memory: Loading {} at {}", data, address);
    }
}

impl HardDrive {
    fn read(&self, sector: u32, size: u32) -> String {
        println!("HardDrive: Reading {} bytes from sector {}", size, sector);
        "boot_data".to_string()
    }
}

struct ComputerFacade {
    cpu: CPU,
    memory: Memory,
    hard_drive: HardDrive,
}

impl ComputerFacade {
    fn new() -> Self {
        ComputerFacade {
            cpu: CPU,
            memory: Memory,
            hard_drive: HardDrive,
        }
    }

    fn start(&self) {
        self.cpu.freeze();
        let data = self.hard_drive.read(0, 1024);
        self.memory.load(0, &data);
        self.cpu.jump(0);
        self.cpu.execute();
    }
}
```

### Generic Facade

```rust
trait SubsystemA {
    fn operation_a(&self) -> String;
}

trait SubsystemB {
    fn operation_b(&self) -> String;
}

struct Facade<A: SubsystemA, B: SubsystemB> {
    a: A,
    b: B,
}

impl<A: SubsystemA, B: SubsystemB> Facade<A, B> {
    fn new(a: A, b: B) -> Self {
        Facade { a, b }
    }

    fn simplified_operation(&self) -> String {
        format!("{} + {}", self.a.operation_a(), self.b.operation_b())
    }
}
```

## Best Practices

- Keep the facade lightweight; it should delegate, not implement
- Name facade methods to reflect the operation, not the subsystem
- Allow direct subsystem access for advanced use cases
- Document which subsystems the facade coordinates
- Use generics when the subsystem types are known at compile time

## Interview Questions

1. What is the difference between a facade and an adapter?
2. When should you expose subsystem internals through the facade?
3. How do you handle facade method failures from subsystem errors?
4. Can a facade be used as a decorator? When?
5. How do you test code that depends on a facade?

## References

- [Rust Design Patterns - Facade](https://rust-unofficial.github.io/patterns/)
- [Struct Composition](https://doc.rust-lang.org/book/ch05-01-defining-structs.html)
- [Rust by Example - Structs](https://doc.rust-lang.org/rust-by-example/custom_types/structs.html)
