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
