## Events in C#

Events provide a publish-subscribe mechanism for communication between objects using delegates.

## Overview

Events are a special kind of delegate that allow a class to notify other classes when something of interest happens. They are the foundation of GUI programming, async notifications, and observer patterns.

## Why It Matters

- Decouple publishers from subscribers
- Enable the observer pattern throughout .NET
- Foundation for GUI event handling
- Used in async event patterns
- Essential for framework extensibility

## Key Concepts

- **Event**: A delegate member with add/remove accessors
- **EventHandler<T>**: Standard event delegate pattern
- **Event Args**: Data passed with event notifications
- **Pub/Sub Pattern**: Publisher emits events, subscribers react
- **Weak Events**: Events that do not prevent garbage collection
- **Event Accessors**: Custom add/remove logic for events

## Core Topics

- Event declaration and raising events
- EventHandler and EventHandler<T> patterns
- Custom EventArgs classes
- Event subscription and unsubscription
- Static events and their implications
- Thread-safe event invocation
- Weak event pattern for memory management
- Event-based async pattern

## Best Practices

- Always use EventHandler<T> or a custom EventArgs derivative
- Use null-conditional operator (?) when raising events
- Unsubscribe events to prevent memory leaks
- Use weak events for long-lived subscribers
- Make events virtual to allow overriding in derived classes

## Hands-on Labs

- Build an event-driven progress reporting system
- Implement a custom EventArgs class
- Create a thread-safe event aggregator
- Build a weak event subscription system
- Implement the observer pattern with events

## Interview Questions

1. What is the difference between a delegate and an event?
2. How do you safely raise an event in a multi-threaded environment?
3. What is the weak event pattern and when should you use it?
4. Explain the EventHandler<T> pattern.
5. How do events relate to the observer pattern?

## References

- https://learn.microsoft.com/dotnet/csharp/programming-guide/events/
- https://learn.microsoft.com/dotnet/api/system.eventhandler
- https://learn.microsoft.com/dotnet/csharp/event-pattern/
