# Command Pattern

## Overview
The Command pattern encapsulates a request as an object, allowing parameterization of clients with different requests, queueing of requests, and support for undoable operations. It decouples the invoker from the receiver.

## When to Use
- Need to queue, delay, or execute requests at different times
- Undo/Redo functionality is required
- Operations need to be logged or serialized
- GUI buttons, menu items, macro recording

## Code Structure
```
Command (interface)       RemoteControl (Invoker)
    |                          |
LightOnCommand         holds Command references
LightOffCommand
    |
Light (Receiver)
```

## Key Benefits
- Decouples invoker from receiver
- Supports undo/redo operations
- Commands can be serialized and queued
- Composite commands (macro commands) possible

## Common Mistakes
- Creating too many command classes for simple operations
- Not implementing undo when required
- Tight coupling between command and receiver

## Interview Questions
1. How does Command pattern support undo operations?
2. What is the difference between Command and Strategy patterns?
3. How would you implement macro commands?
4. When would you use Command over direct method calls?
