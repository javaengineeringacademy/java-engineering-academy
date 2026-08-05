# Command Pattern in Rust

The Command pattern encapsulates a request as an object, allowing parameterization and queueing. In Rust, this is implemented using enums, trait objects, or closures.

## When to Use

- Undo/redo functionality
- Task queueing and scheduling
- Transaction systems
- Macro recording
- Decoupling invoker from receiver

## Implementation

### Enum-Based Command

```rust
#[derive(Debug)]
enum Command {
    Insert { position: usize, text: String },
    Delete { position: usize, length: usize },
    Replace { position: usize, old: String, new: String },
}

struct TextEditor {
    content: String,
    history: Vec<Command>,
}

impl TextEditor {
    fn new() -> Self {
        TextEditor {
            content: String::new(),
            history: Vec::new(),
        }
    }

    fn execute(&mut self, command: Command) {
        match &command {
            Command::Insert { position, text } => {
                self.content.insert_str(*position, text);
            }
            Command::Delete { position, length } => {
                self.content.replace_range(*position..*position + length, "");
            }
            Command::Replace { position, old, new } => {
                let start = *position;
                let end = start + old.len();
                self.content.replace_range(start..end, new);
            }
        }
        self.history.push(command);
    }
}
```

### Trait Object Command

```rust
trait Command {
    fn execute(&mut self);
    fn undo(&mut self);
}

struct LightOnCommand {
    light: String,
}

impl Command for LightOnCommand {
    fn execute(&mut self) {
        println!("{}: ON", self.light);
    }
    fn undo(&mut self) {
        println!("{}: OFF", self.light);
    }
}

struct RemoteControl {
    history: Vec<Box<dyn Command>>,
}

impl RemoteControl {
    fn new() -> Self {
        RemoteControl { history: Vec::new() }
    }

    fn press_button(&mut self, mut command: Box<dyn Command>) {
        command.execute();
        self.history.push(command);
    }

    fn press_undo(&mut self) {
        if let Some(mut command) = self.history.pop() {
            command.undo();
        }
    }
}
```

### Closure Command

```rust
struct TaskQueue {
    tasks: Vec<Box<dyn FnOnce()>>,
}

impl TaskQueue {
    fn new() -> Self {
        TaskQueue { tasks: Vec::new() }
    }

    fn enqueue(&mut self, task: impl FnOnce() + 'static) {
        self.tasks.push(Box::new(task));
    }

    fn execute_all(&mut self) {
        for task in self.tasks.drain(..) {
            task();
        }
    }
}
```

## Best Practices

- Use enums when the command set is fixed and known at compile time
- Use trait objects when commands need polymorphic behavior
- Implement both `execute` and `undo` for reversible commands
- Use `FnOnce` for commands that consume captured state
- Document command sequencing and ordering requirements

## Interview Questions

1. How does Rust's ownership model affect command pattern implementation?
2. What is the difference between `Fn`, `FnMut`, and `FnOnce` for commands?
3. How do you implement undo/redo with the command pattern?
4. How do you serialize commands for persistence?
5. When should you use enums vs trait objects for commands?

## References

- [Rust Design Patterns - Command](https://rust-unofficial.github.io/patterns/)
- [Fn Traits](https://doc.rust-lang.org/book/ch13-01-closures.html)
- [Rust by Example - Closures](https://doc.rust-lang.org/rust-by-example/fn/closure.html)
