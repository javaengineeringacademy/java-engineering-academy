# Mediator Pattern

## Overview
The Mediator pattern defines an object that encapsulates how a set of objects interact. It promotes loose coupling by preventing objects from referring to each other explicitly.

## When to Use
- Multiple objects communicate in well-defined but complex ways
- Want to reduce direct dependencies between communicating objects
- Reusable components that shouldn't depend on concrete classes
- Chat systems, air traffic control, UI component coordination

## Code Structure
```
Mediator (interface)        ChatRoom (concrete mediator)
    |                           |
register()                 manages Users
sendMessage()              coordinates communication
    |
User (colleague)
```

## Key Benefits
- Reduces dependencies between communicating objects
- Centralizes control over interactions
- Easy to add new mediators and colleagues
- Follows Single Responsibility Principle

## Common Mistakes
- Mediator becoming too complex (God Object)
- Not defining clear communication protocols
- Tight coupling between mediator and colleagues

## Interview Questions
1. What is the difference between Mediator and Observer?
2. How does Mediator compare to MVC architecture?
3. What happens when the mediator becomes too complex?
4. When would you use Mediator over direct communication?

## Performance

Mediator adds O(1) per interaction (lookup and delegate). For many-to-many communication, mediator reduces complexity from O(n²) pairwise connections to O(n) mediator registrations. The overhead is minimal compared to the communication cost itself (network, UI rendering). Event-driven mediators (pub/sub) add queue overhead but enable async processing.

## Examples

```java
// Chat room mediator
interface ChatRoomMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

class ChatRoom implements ChatRoomMediator {
    private final List<User> users = new ArrayList<>();
    
    @Override
    public void addUser(User user) {
        users.add(user);
    }
    
    @Override
    public void sendMessage(String message, User sender) {
        users.stream()
             .filter(u -> u != sender)
             .forEach(u -> u.receive(message, sender.getName()));
    }
}

class User {
    private final String name;
    private final ChatRoomMediator mediator;
    
    User(String name, ChatRoomMediator mediator) {
        this.name = name;
        this.mediator = mediator;
        mediator.addUser(this);
    }
    
    void send(String message) {
        mediator.sendMessage(message, this);
    }
    
    void receive(String message, String from) {
        System.out.println(name + " received from " + from + ": " + message);
    }
    
    String getName() { return name; }
}

// Usage
ChatRoomMediator room = new ChatRoom();
User alice = new User("Alice", room);
User bob = new User("Bob", room);
alice.send("Hello everyone!");
// Bob receives: "Alice: Hello everyone!"
```

## Internal Working

The mediator encapsulates all interactions between colleague objects. Colleagues communicate only through the mediator — they do not hold references to each other. When a colleague needs to communicate, it calls a mediator method. The mediator routes the message to the appropriate colleagues. This centralizes control and reduces coupling. Spring's ApplicationEventPublisher is a mediator-like mechanism.

## Why This Concept Exists

When multiple objects communicate directly, you get a web of dependencies: A talks to B, C, D; B talks to A, C; and so on. Changing one object affects all others. Mediator centralizes communication — each object talks only to the mediator. Adding a new object means registering with the mediator, not modifying every existing object. Chat rooms, air traffic control, and UI form validation are natural fits.

## Pitfalls

1. **God object**: Mediator that handles too many interactions becomes a maintenance burden
2. **Single point of failure**: If the mediator breaks, all communication breaks
3. **Complexity migration**: The complexity moves from distributed objects to a single mediator
4. **Testing**: Mediator logic is centralized — needs thorough testing of all interaction paths
5. **Overuse**: Simple 2-3 object interactions don't need a mediator

## References

- [Refactoring.Guru - Mediator Pattern](https://refactoring.guru/design-patterns/mediator)
- [Head First Design Patterns - Mediator Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Spring ApplicationEventPublisher](https://docs.spring.io/spring-framework/reference/core/events.html)
