# Mediator Pattern in Rust

The Mediator pattern defines an object that encapsulates how a set of objects interact, promoting loose coupling. In Rust, this is implemented using structs that coordinate communication between components.

## When to Use

- Complex interactions between multiple objects
- UI component coordination
- Chat room implementations
- Air traffic control systems
- Event bus systems

## Implementation

### Basic Mediator

```rust
trait Mediator {
    fn notify(&mut self, sender: &str, event: &str);
}

struct ChatRoom {
    users: Vec<String>,
}

impl ChatRoom {
    fn new() -> Self {
        ChatRoom { users: Vec::new() }
    }

    fn add_user(&mut self, user: String) {
        self.users.push(user);
    }
}

impl Mediator for ChatRoom {
    fn notify(&mut self, sender: &str, event: &str) {
        println!("ChatRoom: {} sent '{}' to all users", sender, event);
    }
}

struct User {
    name: String,
}

impl User {
    fn send(&self, mediator: &mut dyn Mediator, message: &str) {
        mediator.notify(&self.name, message);
    }
}
```

### Event Bus Mediator

```rust
use std::collections::HashMap;

struct EventBus {
    handlers: HashMap<String, Vec<Box<dyn Fn(&str)>>>,
}

impl EventBus {
    fn new() -> Self {
        EventBus { handlers: HashMap::new() }
    }

    fn subscribe(&mut self, event: &str, handler: impl Fn(&str) + 'static) {
        self.handlers
            .entry(event.to_string())
            .or_insert_with(Vec::new)
            .push(Box::new(handler));
    }

    fn publish(&self, event: &str, data: &str) {
        if let Some(handlers) = self.handlers.get(event) {
            for handler in handlers {
                handler(data);
            }
        }
    }
}

fn main() {
    let mut bus = EventBus::new();
    bus.subscribe("message", |data| println!("Handler 1: {}", data));
    bus.subscribe("message", |data| println!("Handler 2: {}", data));
    bus.publish("message", "Hello");
}
```

### UI Mediator

```rust
struct FormMediator {
    button_enabled: bool,
    text_valid: bool,
}

impl FormMediator {
    fn new() -> Self {
        FormMediator { button_enabled: false, text_valid: false }
    }

    fn text_changed(&mut self, valid: bool) {
        self.text_valid = valid;
        self.update_button();
    }

    fn checkbox_changed(&mut self, checked: bool) {
        self.update_button();
    }

    fn update_button(&mut self) {
        self.button_enabled = self.text_valid;
        println!("Submit button enabled: {}", self.button_enabled);
    }
}
```

## Best Practices

- Keep mediators focused on coordination, not business logic
- Use channels for cross-thread mediator communication
- Document the events and their handlers clearly
- Consider using an event bus for decoupled communication
- Implement cleanup logic when components are removed

## Interview Questions

1. How does the mediator pattern differ from the observer pattern?
2. When should you use a mediator vs direct communication?
3. How do you handle mediator cleanup when components are dropped?
4. How do you test components that depend on a mediator?
5. What are the thread-safety considerations for mediators?

## References

- [Rust Design Patterns - Mediator](https://rust-unofficial.github.io/patterns/)
- [Channels](https://doc.rust-lang.org/book/ch16-02-message-passing.html)
- [HashMap](https://doc.rust-lang.org/std/collections/struct.HashMap.html)
