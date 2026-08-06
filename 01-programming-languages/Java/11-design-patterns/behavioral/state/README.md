# State Pattern

## Overview
The State pattern allows an object to alter its behavior when its internal state changes. The object appears to change its class by delegating to different state objects.

## When to Use
- Object behavior depends on its state and must change at runtime
- Operations have large conditional statements based on object state
- State transitions are well-defined and complex
- Order processing, TCP connections, game states

## Code Structure
```
State (interface)          Order (Context)
    |                         |
NewState                 holds State reference
ProcessedState           delegates to current state
ShippedState
```

## Key Benefits
- Eliminates complex conditional statements
- State-specific behavior is localized
- Easy to add new states without modifying existing code
- Clear state transitions

## Common Mistakes
- Creating too many states for simple variations
- Circular state transitions causing infinite loops
- Not handling invalid state transitions

## Interview Questions
1. How does State pattern differ from Strategy pattern?
2. What is the role of the Context in State pattern?
3. How do you handle state transitions?
4. When would you use State over if-else chains?

## Performance

State pattern adds one virtual method call (~5ns) per state operation. State objects are typically lightweight and can be singletons per state. The benefit is eliminating large switch statements — the performance is comparable but the code is far more maintainable. For high-frequency state transitions, reuse state instances instead of creating new ones.

## Examples

```java
// Order processing state machine
interface OrderState {
    void next(Order order);
    void cancel(Order order);
    String getStatus();
}

class NewOrder implements OrderState {
    @Override public void next(Order order) {
        order.setState(new Processing());
        System.out.println("Order moved to Processing");
    }
    @Override public void cancel(Order order) {
        order.setState(new Cancelled());
        System.out.println("Order cancelled");
    }
    @Override public String getStatus() { return "New"; }
}

class Processing implements OrderState {
    @Override public void next(Order order) {
        order.setState(new Shipped());
        System.out.println("Order shipped");
    }
    @Override public void cancel(Order order) {
        order.setState(new Cancelled());
        System.out.println("Order cancelled during processing");
    }
    @Override public String getStatus() { return "Processing"; }
}

class Shipped implements OrderState {
    @Override public void next(Order order) {
        order.setState(new Delivered());
        System.out.println("Order delivered");
    }
    @Override public void cancel(Order order) {
        System.out.println("Cannot cancel: already shipped");
    }
    @Override public String getStatus() { return "Shipped"; }
}

class Delivered implements OrderState {
    @Override public void next(Order order) {
        System.out.println("Already delivered");
    }
    @Override public void cancel(Order order) {
        System.out.println("Cannot cancel: already delivered");
    }
    @Override public String getStatus() { return "Delivered"; }
}

class Cancelled implements OrderState {
    @Override public void next(Order order) {
        System.out.println("Cannot proceed: order cancelled");
    }
    @Override public void cancel(Order order) {
        System.out.println("Already cancelled");
    }
    @Override public String getStatus() { return "Cancelled"; }
}

class Order {
    private OrderState state = new NewOrder();
    
    void setState(OrderState state) { this.state = state; }
    void next() { state.next(this); }
    void cancel() { state.cancel(this); }
    String getStatus() { return state.getStatus(); }
}

// Usage
Order order = new Order();
order.next();      // New → Processing
order.next();      // Processing → Shipped
order.cancel();    // Cannot cancel: already shipped
order.next();      // Shipped → Delivered
```

## Internal Working

The context (Order) holds a reference to the current state object. When an operation is called, the context delegates to the current state. The state object may change the context's state reference to transition to a new state. Each state class implements the state interface with behavior appropriate for that state. Invalid transitions throw exceptions or log warnings.

## Why This Concept Exists

Objects with complex state-dependent behavior accumulate large switch statements or if-else chains. Adding a new state means modifying every method. State pattern encapsulates each state in its own class — adding a new state means adding a new class, not modifying existing ones. This follows open/closed principle and makes state transitions explicit and testable.

## Pitfalls

1. **State explosion**: Many states mean many classes — consider state tables or enum-based states
2. **Transition logic scattered**: State transitions spread across state classes can be hard to follow
3. **Context leakage**: States need access to context internals — design the state API carefully
4. **Over-engineering**: Simple 2-3 state objects don't need the full pattern
5. **Testing complexity**: Each state and each transition needs separate tests

## References

- [Refactoring.Guru - State Pattern](https://refactoring.guru/design-patterns/state)
- [Head First Design Patterns - State Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java Thread States](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.State.html)
