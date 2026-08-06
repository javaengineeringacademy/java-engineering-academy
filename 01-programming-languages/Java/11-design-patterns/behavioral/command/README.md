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

## Performance

Command objects add heap allocation (~32-64 bytes per command). In tight loops, reuse command instances or use flyweight. For undo systems, command history consumes O(n) memory. Batch commands (macro) reduce per-command overhead by grouping operations. The indirection (command → execute) adds ~5ns per call — negligible for UI or network operations.

## Examples

```java
// Text editor with undo support
interface EditCommand {
    void execute();
    void undo();
}

class InsertTextCommand implements EditCommand {
    private final StringBuilder document;
    private final String text;
    private final int position;
    
    InsertTextCommand(StringBuilder document, String text, int position) {
        this.document = document;
        this.text = text;
        this.position = position;
    }
    
    @Override
    public void execute() {
        document.insert(position, text);
        System.out.println("Inserted: '" + text + "'");
    }
    
    @Override
    public void undo() {
        document.delete(position, position + text.length());
        System.out.println("Undid insert: '" + text + "'");
    }
}

class TextEditor {
    private final StringBuilder document = new StringBuilder();
    private final Deque<EditCommand> history = new ArrayDeque<>();
    
    void type(String text) {
        EditCommand cmd = new InsertTextCommand(document, text, document.length());
        cmd.execute();
        history.push(cmd);
    }
    
    void undo() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }
    
    String getContent() { return document.toString(); }
}

// Usage
TextEditor editor = new TextEditor();
editor.type("Hello");
editor.type(" World");
System.out.println(editor.getContent()); // Hello World
editor.undo();
System.out.println(editor.getContent()); // Hello
```

## Internal Working

The command object encapsulates all information needed to perform an action: receiver reference, method name, and parameters. The invoker stores commands and calls execute() at the appropriate time. For undo, each command stores enough state to reverse its effect. Commands can be serialized, queued, logged, and replayed. This is the pattern behind transaction systems, macro recording, and task queues.

## Why This Concept Exists

Direct method calls tie the invoker to the receiver at compile time. Command decouples them — the invoker does not know what execute() does. This enables queuing (execute later), logging (record what was done), undo/redo (reverse the operation), and transaction support (commit or rollback). UI buttons, menu items, and keyboard shortcuts all benefit from command.

## Pitfalls

1. **Class explosion**: Each operation becomes a class — 10 operations = 10 classes (use lambdas in Java 8+)
2. **State management**: Undo requires storing enough state to reverse — complex for side-effecting operations
3. **Memory**: Command history grows unbounded — implement history limits
4. **Testing**: Each command needs its own test — factor out common behavior
5. **Over-engineering**: Simple operations don't need command pattern — direct method calls are clearer

## References

- [Refactoring.Guru - Command Pattern](https://refactoring.guru/design-patterns/command)
- [Head First Design Patterns - Command Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java Runnable as Command](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Runnable.html)
