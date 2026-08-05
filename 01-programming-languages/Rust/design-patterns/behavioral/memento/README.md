# Memento Pattern in Rust

The Memento pattern captures and externalizes an object's internal state so it can be restored later. In Rust, this is implemented using structs that store snapshots and undo stacks.

## When to Use

- Undo/redo functionality
- State restoration
- Checkpointing
- Transaction rollback
- Version control systems

## Implementation

### Basic Memento

```rust
#[derive(Clone)]
struct EditorMemento {
    content: String,
    cursor_position: usize,
}

struct Editor {
    content: String,
    cursor_position: usize,
    history: Vec<EditorMemento>,
}

impl Editor {
    fn new() -> Self {
        Editor {
            content: String::new(),
            cursor_position: 0,
            history: Vec::new(),
        }
    }

    fn save(&mut self) {
        self.history.push(EditorMemento {
            content: self.content.clone(),
            cursor_position: self.cursor_position,
        });
    }

    fn type_text(&mut self, text: &str) {
        self.content.insert_str(self.cursor_position, text);
        self.cursor_position += text.len();
    }

    fn undo(&mut self) {
        if let Some(memento) = self.history.pop() {
            self.content = memento.content;
            self.cursor_position = memento.cursor_position;
        }
    }
}
```

### Stack-Based Memento

```rust
#[derive(Clone, Debug)]
struct GameState {
    level: u32,
    score: u64,
    health: f64,
}

struct Game {
    state: GameState,
    save_states: Vec<GameState>,
}

impl Game {
    fn new() -> Self {
        Game {
            state: GameState { level: 1, score: 0, health: 100.0 },
            save_states: Vec::new(),
        }
    }

    fn save(&mut self) {
        self.save_states.push(self.state.clone());
    }

    fn load(&mut self) {
        if let Some(state) = self.save_states.pop() {
            self.state = state;
        }
    }

    fn play(&mut self) {
        self.state.score += 100;
        self.state.health -= 10.0;
        println!("Playing... Score: {}, Health: {}", self.state.score, self.state.health);
    }
}
```

### Typed Memento

```rust
trait Memento {
    fn restore(&self) -> Box<dyn std::any::Any>;
}

struct ConfigMemento {
    database_url: String,
    max_connections: u32,
}

impl ConfigMemento {
    fn new(url: &str, max: u32) -> Self {
        ConfigMemento {
            database_url: url.to_string(),
            max_connections: max,
        }
    }
}

struct Config {
    database_url: String,
    max_connections: u32,
    history: Vec<ConfigMemento>,
}

impl Config {
    fn new() -> Self {
        Config {
            database_url: String::new(),
            max_connections: 10,
            history: Vec::new(),
        }
    }

    fn save(&mut self) {
        self.history.push(ConfigMemento::new(
            &self.database_url,
            self.max_connections,
        ));
    }

    fn update(&mut self, url: &str, max: u32) {
        self.save();
        self.database_url = url.to_string();
        self.max_connections = max;
    }
}
```

## Best Practices

- Use `Clone` for simple mementos
- Store mementos in a stack for undo/redo functionality
- Keep memento data immutable after creation
- Document which state is captured in each memento
- Consider using serialization for persistent mementos

## Interview Questions

1. What is the difference between memento and command pattern?
2. How do you handle large state snapshots efficiently?
3. How do you implement redo functionality?
4. How do you handle concurrent access to mementos?
5. When should you avoid the memento pattern?

## References

- [Rust Design Patterns - Memento](https://rust-unofficial.github.io/patterns/)
- [Clone trait](https://doc.rust-lang.org/std/clone/trait.Clone.html)
- [Rust by Example - Structs](https://doc.rust-lang.org/rust-by-example/custom_types/structs.html)
