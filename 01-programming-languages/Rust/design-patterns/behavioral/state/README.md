# State Pattern in Rust

The State pattern allows an object to alter its behavior when its internal state changes. In Rust, this is naturally implemented using enums with pattern matching for compile-time safe state machines.

## When to Use

- Object behavior depends on its state
- State transitions are complex
- Avoiding large conditional statements
- Finite state machines
- Request processing pipelines

## Implementation

### Enum-Based State Machine

```rust
#[derive(Debug)]
enum OrderState {
    New,
    Processing,
    Shipped,
    Delivered,
    Cancelled,
}

impl OrderState {
    fn process(&self) -> OrderState {
        match self {
            OrderState::New => OrderState::Processing,
            OrderState::Processing => OrderState::Shipped,
            _ => OrderState::Cancelled,
        }
    }

    fn ship(&self) -> OrderState {
        match self {
            OrderState::Processing => OrderState::Shipped,
            _ => OrderState::Cancelled,
        }
    }

    fn deliver(&self) -> OrderState {
        match self {
            OrderState::Shipped => OrderState::Delivered,
            _ => OrderState::Cancelled,
        }
    }
}

struct Order {
    state: OrderState,
}

impl Order {
    fn new() -> Self {
        Order { state: OrderState::New }
    }

    fn process(&mut self) {
        self.state = self.state.process();
    }

    fn ship(&mut self) {
        self.state = self.state.ship();
    }

    fn deliver(&mut self) {
        self.state = self.state.deliver();
    }
}
```

### Trait-Based State

```rust
trait State {
    fn next(self: Box<Self>) -> Box<dyn State>;
    fn status(&self) -> &str;
}

struct IdleState;
struct RunningState;
struct StoppedState;

impl State for IdleState {
    fn next(self: Box<Self>) -> Box<dyn State> {
        println!("Starting...");
        Box::new(RunningState)
    }
    fn status(&self) -> &str { "idle" }
}

impl State for RunningState {
    fn next(self: Box<Self>) -> Box<dyn State> {
        println!("Stopping...");
        Box::new(StoppedState)
    }
    fn status(&self) -> &str { "running" }
}

impl State for StoppedState {
    fn next(self: Box<Self>) -> Box<dyn State> {
        println!("Resetting to idle...");
        Box::new(IdleState)
    }
    fn status(&self) -> &str { "stopped" }
}

struct Machine {
    state: Box<dyn State>,
}

impl Machine {
    fn new() -> Self {
        Machine { state: Box::new(IdleState) }
    }

    fn transition(&mut self) {
        let old_state = std::mem::replace(&mut self.state, Box::new(IdleState));
        self.state = old_state.next();
    }
}
```

### Game Character State

```rust
#[derive(Debug, Clone, Copy)]
enum CharacterState {
    Idle,
    Walking,
    Running,
    Jumping,
    Attacking,
}

struct Character {
    state: CharacterState,
    speed: f64,
    position: f64,
}

impl Character {
    fn update(&mut self) {
        match self.state {
            CharacterState::Idle => { self.speed = 0.0; }
            CharacterState::Walking => {
                self.speed = 5.0;
                self.position += self.speed;
            }
            CharacterState::Running => {
                self.speed = 15.0;
                self.position += self.speed;
            }
            CharacterState::Jumping => { self.speed = 10.0; }
            CharacterState::Attacking => { self.speed = 0.0; }
        }
    }

    fn transition(&mut self, new_state: CharacterState) {
        self.state = new_state;
    }
}
```

## Best Practices

- Use enums when the state set is fixed and known at compile time
- Use trait objects when state implementations need independent evolution
- Document valid state transitions and invalid transitions
- Use `#[derive(Debug)]` to inspect state machine behavior
- Consider using the typestate pattern for compile-time state validation

## Interview Questions

1. What is the difference between the state pattern and a state machine?
2. How does Rust's pattern matching benefit state implementations?
3. When would you use enums vs trait objects for states?
4. How do you handle invalid state transitions?
5. What is the typestate pattern and how does it relate to the state pattern?

## References

- [Rust Design Patterns - State](https://rust-unofficial.github.io/patterns/)
- [Pattern Matching](https://doc.rust-lang.org/book/ch18-00-patterns.html)
- [Rust by Example - Match](https://doc.rust-lang.org/rust-by-example/control_flow/match.html)
