# Command Pattern

The Command pattern encapsulates a request as an object, allowing parameterization, queuing, logging, and undo/redo operations.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Command](#basic-command)
3. [Undo/Redo](#undoredo)
4. [Command Queue](#command-queue)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Command?

Command encapsulates an action as an object, separating invocation from execution.

```
Invoker ──▶ Command ──▶ Receiver
           (execute)
```

### When to Use

- Undo/redo functionality
- Queue or schedule commands
- Log operations
- Decouple sender from receiver

---

## Basic Command

### Text Editor

```java
// Command interface
public interface Command {
    void execute();
    void undo();
}

// Receiver
public class TextEditor {
    private final StringBuilder content = new StringBuilder();

    public void insert(int position, String text) {
        content.insert(position, text);
    }

    public void delete(int start, int length) {
        content.delete(start, start + length);
    }

    public String getContent() { return content.toString(); }
}

// Concrete commands
public class InsertCommand implements Command {
    private final TextEditor editor;
    private final int position;
    private final String text;

    public InsertCommand(TextEditor editor, int position, String text) {
        this.editor = editor;
        this.position = position;
        this.text = text;
    }

    @Override
    public void execute() { editor.insert(position, text); }

    @Override
    public void undo() { editor.delete(position, text.length()); }
}

public class DeleteCommand implements Command {
    private final TextEditor editor;
    private final int start;
    private final int length;
    private String deletedText;

    public DeleteCommand(TextEditor editor, int start, int length) {
        this.editor = editor;
        this.start = start;
        this.length = length;
    }

    @Override
    public void execute() {
        deletedText = editor.getContent().substring(start, start + length);
        editor.delete(start, length);
    }

    @Override
    public void undo() { editor.insert(start, deletedText); }
}

// Invoker
public class CommandManager {
    private final Stack<Command> history = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            history.push(command);
        }
    }
}

// Usage
TextEditor editor = new TextEditor();
CommandManager manager = new CommandManager();

manager.executeCommand(new InsertCommand(editor, 0, "Hello"));
manager.executeCommand(new InsertCommand(editor, 5, " World"));
System.out.println(editor.getContent());  // "Hello World"

manager.undo();
System.out.println(editor.getContent());  // "Hello"

manager.redo();
System.out.println(editor.getContent());  // "Hello World"
```

---

## Undo/Redo

### Drawing Application

```java
public interface DrawCommand {
    void execute();
    void undo();
}

public class DrawCircleCommand implements DrawCommand {
    private final Canvas canvas;
    private final int x, y, radius;
    private final Color color;

    public DrawCircleCommand(Canvas canvas, int x, int y, int radius, Color color) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void execute() { canvas.drawCircle(x, y, radius, color); }

    @Override
    public void undo() { canvas.removeCircle(x, y, radius); }
}

// History with full undo/redo support
public class DrawingHistory {
    private final List<DrawCommand> commands = new ArrayList<>();
    private int current = -1;

    public void execute(DrawCommand command) {
        // Remove any commands after current position
        commands.subList(current + 1, commands.size()).clear();
        command.execute();
        commands.add(command);
        current++;
    }

    public void undo() {
        if (current >= 0) {
            commands.get(current).undo();
            current--;
        }
    }

    public void redo() {
        if (current < commands.size() - 1) {
            current++;
            commands.get(current).execute();
        }
    }
}
```

---

## Command Queue

### Async Command Processing

```java
public class CommandQueue {
    private final Queue<Command> queue = new LinkedList<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = true;

    public CommandQueue() {
        executor.submit(this::processCommands);
    }

    public void enqueue(Command command) {
        synchronized (queue) {
            queue.add(command);
            queue.notify();
        }
    }

    private void processCommands() {
        while (running) {
            Command command;
            synchronized (queue) {
                while (queue.isEmpty() && running) {
                    try { queue.wait(); } catch (InterruptedException e) { break; }
                }
                if (!running) break;
                command = queue.poll();
            }
            command.execute();
        }
    }

    public void shutdown() {
        running = false;
        synchronized (queue) { queue.notify(); }
        executor.shutdown();
    }
}

// Usage
CommandQueue queue = new CommandQueue();
queue.enqueue(new InsertCommand(editor, 0, "Hello"));
queue.enqueue(new InsertCommand(editor, 5, " World"));
```

---

## Best Practices

### Do

```java
// 1. Keep commands small and focused
public class SimpleCommand implements Command {
    private final Runnable action;
    private final Runnable undoAction;

    public SimpleCommand(Runnable action, Runnable undoAction) {
        this.action = action;
        this.undoAction = undoAction;
    }

    @Override
    public void execute() { action.run(); }

    @Override
    public void undo() { undoAction.run(); }
}

// 2. Make commands reversible when needed
@Override
public void undo() { /* reverse the operation */ }
```

### Don't

```java
// 1. Don't make commands too complex
// One command = one action

// 2. Don't forget undo implementation
// If undo is needed, implement it properly
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Command** | Encapsulate request as object |
| **Execute** | Perform the action |
| **Undo** | Reverse the action |
| **Invoker** | Triggers commands |
| **Receiver** | Performs actual work |
| **Queue** | Schedule commands |
| **Use Cases** | Undo/redo, macros, async processing |
