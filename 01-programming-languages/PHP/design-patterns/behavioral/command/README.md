# Command Pattern in PHP

The Command pattern encapsulates a request as an object. In PHP, this is implemented using interfaces, classes, or callables.

## When to Use

- Undo/redo functionality
- Task queueing and scheduling
- Transaction systems
- Macro recording
- Decoupling invoker from receiver

## Implementation

### Interface-Based Command

```php
interface Command
{
    public function execute(): void;
    public function undo(): void;
}

class TextEditor
{
    private string $content = '';

    public function getContent(): string { return $this->content; }

    public function insert(int $position, string $text): void
    {
        $this->content = substr($this->content, 0, $position) . $text . substr($this->content, $position);
    }

    public function delete(int $position, int $length): void
    {
        $this->content = substr($this->content, 0, $position) . substr($this->content, $position + $length);
    }
}

class InsertCommand implements Command
{
    private string $deletedText = '';

    public function __construct(
        private TextEditor $editor,
        private int $position,
        private string $text
    ) {}

    public function execute(): void
    {
        $this->editor->insert($this->position, $this->text);
    }

    public function undo(): void
    {
        $this->editor->delete($this->position, strlen($this->text));
    }
}

class DeleteCommand implements Command
{
    private string $deletedText = '';

    public function __construct(
        private TextEditor $editor,
        private int $position,
        private int $length
    ) {}

    public function execute(): void
    {
        $this->deletedText = substr($this->editor->getContent(), $this->position, $this->length);
        $this->editor->delete($this->position, $this->length);
    }

    public function undo(): void
    {
        $this->editor->insert($this->position, $this->deletedText);
    }
}
```

### Command History

```php
class CommandHistory
{
    private array $history = [];
    private array $redoStack = [];

    public function execute(Command $command): void
    {
        $command->execute();
        $this->history[] = $command;
        $this->redoStack = [];
    }

    public function undo(): void
    {
        if (!empty($this->history)) {
            $command = array_pop($this->history);
            $command->undo();
            $this->redoStack[] = $command;
        }
    }

    public function redo(): void
    {
        if (!empty($this->redoStack)) {
            $command = array_pop($this->redoStack);
            $command->execute();
            $this->history[] = $command;
        }
    }
}
```

### Task Queue

```php
class TaskQueue
{
    private array $tasks = [];

    public function enqueue(callable $task): void
    {
        $this->tasks[] = $task;
    }

    public function executeAll(): void
    {
        foreach ($this->tasks as $task) {
            $task();
        }
        $this->tasks = [];
    }
}

// Usage
$queue = new TaskQueue();
$queue->enqueue(fn() => echo "Task 1\n");
$queue->enqueue(fn() => echo "Task 2\n");
$queue->executeAll();
```

### Macro Command

```php
class MacroCommand implements Command
{
    private array $commands = [];

    public function add(Command $command): void
    {
        $this->commands[] = $command;
    }

    public function execute(): void
    {
        foreach ($this->commands as $command) {
            $command->execute();
        }
    }

    public function undo(): void
    {
        foreach (array_reverse($this->commands) as $command) {
            $command->undo();
        }
    }
}
```

## Best Practices

- Use interfaces for command contracts
- Implement both `execute` and `undo` for reversible commands
- Use `Closure::bind` for command context binding
- Document command sequencing requirements
- Consider using readonly properties for command data

## Interview Questions

1. How does the command pattern enable undo/redo?
2. What is the difference between a command and a closure?
3. How do you serialize commands for persistence?
4. How do you handle command failures?
5. When should you use commands vs direct method calls?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
