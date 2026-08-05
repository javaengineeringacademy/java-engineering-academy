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
