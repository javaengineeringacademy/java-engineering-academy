# Command Pattern

## 1. Introduction

The Command Pattern is a behavioral design pattern that encapsulates a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations. It turns a request into a stand-alone object that contains all information about the request.

The Command pattern is particularly useful for implementing undo/redo functionality, transaction systems, job queues, and macro recording.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Command pattern with undo/redo support
- Understand command queuing and logging
- Recognize command usage in Java (Runnable, Callable)
- Implement transaction-like behavior with commands
- Build macro recording systems

---

## 3. Prerequisites

- Understanding of interfaces and abstract classes
- Knowledge of stacks for undo/redo
- Familiarity with encapsulation
- Understanding of object-oriented design

---

## 4. Why This Concept Exists

The Command pattern exists because:

- **Undo/Redo**: Commands can be reversed
- **Queuing**: Commands can be queued and executed later
- **Logging**: Commands can be logged for audit
- **Decoupling**: Sender doesn't know receiver
- **Composite commands**: Commands can be combined

Without Command, you would have direct method calls that cannot be undone or queued.

---

## 5. Problem Statement

Consider a text editor:

```java
public class TextEditor {
    private StringBuilder text = new StringBuilder();

    public void insert(String text, int position) {
        this.text.insert(position, text);
    }

    public void delete(int position, int length) {
        this.text.delete(position, position + length);
    }

    // No undo support!
    // How to reverse an insert or delete?
}
```

---

## 6. Theory

### 6.1 Command Structure

1. **Command**: Interface for executing operations
2. **ConcreteCommand**: Implements command, knows receiver
3. **Invoker**: Asks command to carry out request
4. **Receiver**: Knows how to perform operations

### 6.2 Command Features

| Feature | Description |
|---------|-------------|
| Undo | Reverse the command |
| Redo | Re-execute undone command |
| Queue | Delay execution |
| Log | Record for audit |
| Composite | Combine multiple commands |

---

## 7. Internal Working

```
Client -> Invoker -> Command -> Receiver
                      |
                  execute()
                  undo()
```

---

## 8. JVM Perspective

- Commands stored in stack for undo/redo
- Command objects can be serialized
- Reflection may be used for generic commands

---

## 9. Memory Representation

```
Invoker
  |-- command: Command
        |-- ConcreteCommand
              |-- receiver: Receiver
              |-- state: Object (for undo)
```

---

## 10. Syntax

```java
public interface Command {
    void execute();
    void undo();
}

public class ConcreteCommand implements Command {
    private final Receiver receiver;
    private final String state;

    public ConcreteCommand(Receiver receiver, String state) {
        this.receiver = receiver;
        this.state = state;
    }

    @Override
    public void execute() {
        receiver.action(state);
    }

    @Override
    public void undo() {
        receiver.reverseAction(state);
    }
}
```

---

## 11. Easy Example

### Light Switch

```java
public interface Command {
    void execute();
    void undo();
}

public class Light {
    private boolean on = false;

    public void toggle() {
        on = !on;
        System.out.println("Light is " + (on ? "ON" : "OFF"));
    }

    public boolean isOn() {
        return on;
    }
}

public class LightOnCommand implements Command {
    private final Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        if (!light.isOn()) {
            light.toggle();
        }
    }

    @Override
    public void undo() {
        if (light.isOn()) {
            light.toggle();
        }
    }
}

public class RemoteControl {
    private Command command;
    private final Stack<Command> history = new Stack<>();

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
        history.push(command);
    }

    public void pressUndo() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }
}

// Usage
Light light = new Light();
RemoteControl remote = new RemoteControl();
remote.setCommand(new LightOnCommand(light));
remote.pressButton(); // Light is ON
remote.pressUndo();   // Light is OFF
```

---

## 12. Medium Example

### Text Editor Commands

```java
public interface EditorCommand {
    void execute();
    void undo();
}

public class InsertCommand implements EditorCommand {
    private final TextEditor editor;
    private final String text;
    private final int position;
    private String deletedText;

    public InsertCommand(TextEditor editor, String text, int position) {
        this.editor = editor;
        this.text = text;
        this.position = position;
    }

    @Override
    public void execute() {
        editor.insert(text, position);
    }

    @Override
    public void undo() {
        editor.delete(position, text.length());
    }
}

public class DeleteCommand implements EditorCommand {
    private final TextEditor editor;
    private final int position;
    private final int length;
    private String deletedText;

    public DeleteCommand(TextEditor editor, int position, int length) {
        this.editor = editor;
        this.position = position;
        this.length = length;
    }

    @Override
    public void execute() {
        deletedText = editor.getText().substring(position, position + length);
        editor.delete(position, length);
    }

    @Override
    public void undo() {
        editor.insert(deletedText, position);
    }
}

public class TextEditor {
    private final StringBuilder text = new StringBuilder();

    public void insert(String text, int position) {
        this.text.insert(position, text);
    }

    public void delete(int position, int length) {
        this.text.delete(position, position + length);
    }

    public String getText() {
        return text.toString();
    }
}

public class CommandHistory {
    private final Stack<EditorCommand> undoStack = new Stack<>();
    private final Stack<EditorCommand> redoStack = new Stack<>();

    public void executeCommand(EditorCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            EditorCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            EditorCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
}
```

---

## 13. Hard Example

### Transaction-like Command

```java
public interface TransactionalCommand {
    void execute();
    void rollback();
    boolean isExecuted();
}

public class CompositeCommand implements TransactionalCommand {
    private final List<TransactionalCommand> commands = new ArrayList<>();
    private final List<TransactionalCommand> executedCommands = new ArrayList<>();

    public void addCommand(TransactionalCommand command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        for (TransactionalCommand command : commands) {
            try {
                command.execute();
                executedCommands.add(command);
            } catch (Exception e) {
                rollback();
                throw new RuntimeException("Command failed, rolling back", e);
            }
        }
    }

    @Override
    public void rollback() {
        for (int i = executedCommands.size() - 1; i >= 0; i--) {
            executedCommands.get(i).rollback();
        }
        executedCommands.clear();
    }

    @Override
    public boolean isExecuted() {
        return !executedCommands.isEmpty();
    }
}

public class BankAccount {
    private BigDecimal balance;

    public BankAccount(BigDecimal balance) {
        this.balance = balance;
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public BigDecimal getBalance() {
        return balance;
    }
}

public class TransferCommand implements TransactionalCommand {
    private final BankAccount from;
    private final BankAccount to;
    private final BigDecimal amount;
    private boolean executed = false;

    public TransferCommand(BankAccount from, BankAccount to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void execute() {
        from.debit(amount);
        to.credit(amount);
        executed = true;
    }

    @Override
    public void rollback() {
        if (executed) {
            from.credit(amount);
            to.debit(amount);
            executed = false;
        }
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
}

// Usage
BankAccount account1 = new BankAccount(BigDecimal.valueOf(1000));
BankAccount account2 = new BankAccount(BigDecimal.valueOf(500));

CompositeCommand transaction = new CompositeCommand();
transaction.addCommand(new TransferCommand(account1, account2, BigDecimal.valueOf(200)));
transaction.addCommand(new TransferCommand(account2, account1, BigDecimal.valueOf(50)));

transaction.execute();
// account1: 850, account2: 650

transaction.rollback();
// account1: 1000, account2: 500
```

---

## 14. Enterprise Example

### Job Queue System

```java
public interface Job {
    void execute();
    void undo();
    String getDescription();
    boolean isReversible();
}

public class EmailJob implements Job {
    private final String to;
    private final String subject;
    private final String body;
    private boolean sent = false;

    public EmailJob(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public void execute() {
        System.out.println("Sending email to " + to);
        sent = true;
    }

    @Override
    public void undo() {
        if (sent) {
            System.out.println("Recalling email to " + to);
            sent = false;
        }
    }

    @Override
    public String getDescription() {
        return "Email to " + to;
    }

    @Override
    public boolean isReversible() {
        return true;
    }
}

public class ReportJob implements Job {
    private final String reportType;
    private File generatedFile;

    public ReportJob(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public void execute() {
        System.out.println("Generating " + reportType + " report");
        generatedFile = new File(reportType + "_report.pdf");
    }

    @Override
    public void undo() {
        if (generatedFile != null && generatedFile.exists()) {
            generatedFile.delete();
            System.out.println("Deleted " + reportType + " report");
        }
    }

    @Override
    public String getDescription() {
        return "Generate " + reportType + " report";
    }

    @Override
    public boolean isReversible() {
        return true;
    }
}

public class JobQueue {
    private final Queue<Job> pendingJobs = new LinkedList<>();
    private final Stack<Job> completedJobs = new Stack<>();
    private final List<Job> failedJobs = new ArrayList<>();

    public void addJob(Job job) {
        pendingJobs.add(job);
    }

    public void processNext() {
        if (!pendingJobs.isEmpty()) {
            Job job = pendingJobs.poll();
            try {
                job.execute();
                completedJobs.push(job);
            } catch (Exception e) {
                failedJobs.add(job);
                System.err.println("Job failed: " + job.getDescription());
            }
        }
    }

    public void undoLast() {
        if (!completedJobs.isEmpty()) {
            Job job = completedJobs.pop();
            if (job.isReversible()) {
                job.undo();
            }
        }
    }

    public void processAll() {
        while (!pendingJobs.isEmpty()) {
            processNext();
        }
    }
}

// Usage
JobQueue queue = new JobQueue();
queue.addJob(new EmailJob("user@example.com", "Welcome", "Hello!"));
queue.addJob(new ReportJob("Sales"));
queue.processAll();
queue.undoLast();
```

---

## 15. Performance

| Operation | Complexity | Notes |
|-----------|------------|-------|
| execute() | O(1) | Depends on command |
| undo() | O(1) | Depends on command |
| Queue add | O(1) | LinkedList |
| Stack push/pop | O(1) | Stack operations |

---

## 16. Best Practices

1. Keep commands small and focused
2. Make commands immutable when possible
3. Use composite commands for complex operations
4. Implement undo carefully to avoid inconsistencies
5. Consider serialization for command logging

---

## 17. Common Mistakes

1. Making commands too complex
2. Not handling undo correctly
3. Forgetting to clear redo stack on new command
4. Not validating command state before execution

---

## 18. Pitfalls

- Increased number of classes
- Undo can be complex for side effects
- Memory usage for command history
- Thread safety concerns in concurrent systems

---

## 19. Debugging Tips

1. Log command execution
2. Add command description for debugging
3. Test undo/redo thoroughly
4. Verify state after each operation

---

## 20. Comparison Table

| Feature | Command | Strategy | Observer |
|---------|---------|----------|----------|
| Purpose | Encapsulate request | Encapsulate algorithm | Notify objects |
| Undo | Yes | No | No |
| Queuing | Yes | No | No |
| Complexity | Medium | Low | Medium |

---

## 21. Decision Tree

```
Need undo/redo? -> Command
Need to queue requests? -> Command
Need to log requests? -> Command
Need algorithm selection? -> Strategy
Need to notify objects? -> Observer
```

---

## 22. Interview Questions

### Q1: What is the Command pattern?
A behavioral pattern that encapsulates requests as objects, enabling undo, redo, queuing, and logging.

### Q2: How does Command support undo?
By storing the previous state or reverse operation in the command object.

### Q3: Command vs. Strategy?
Command encapsulates a request with undo support. Strategy encapsulates an algorithm without undo.

### Q4: Real-world examples?
GUI buttons, macro recording, transaction systems, job queues.

---

## 23. Exercises

1. Implement undo/redo for a drawing application
2. Create a macro recording system
3. Build a transaction system with rollback

---

## 24. Assignments

1. Implement a text editor with command pattern
2. Create a remote control with undo support
3. Build a job queue with command pattern

---

## 25. Mini Project

### Task Manager
Build a task manager with add, delete, update commands that support undo/redo and command history.

---

## 26. Summary

- Command encapsulates requests as objects
- Supports undo, redo, queuing, and logging
- Promotes loose coupling between sender and receiver
- Use composite commands for complex operations
- Essential for transaction-like systems

---

## 27. References

1. Gamma, E., et al. (1994). Design Patterns, Chapter 5
2. Bloch, J. (2018). Effective Java
3. Refactoring Guru: https://refactoring.guru/design-patterns/command
