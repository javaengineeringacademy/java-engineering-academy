# Memento Pattern in PHP

The Memento pattern captures and externalizes an object's internal state so it can be restored later. In PHP, this is implemented using classes that store snapshots and undo stacks.

## When to Use

- Undo/redo functionality
- State restoration
- Checkpointing
- Transaction rollback
- Version control systems

## Implementation

### Basic Memento

```php
class EditorMemento
{
    public function __construct(
        public readonly string $content,
        public readonly int $cursorPosition
    ) {}
}

class Editor
{
    private string $content = '';
    private int $cursorPosition = 0;
    private array $history = [];

    public function save(): void
    {
        $this->history[] = new EditorMemento($this->content, $this->cursorPosition);
    }

    public function typeText(string $text): void
    {
        $this->content = substr($this->content, 0, $this->cursorPosition) . $text . substr($this->content, $this->cursorPosition);
        $this->cursorPosition += strlen($text);
    }

    public function undo(): void
    {
        if (!empty($this->history)) {
            $memento = array_pop($this->history);
            $this->content = $memento->content;
            $this->cursorPosition = $memento->cursorPosition;
        }
    }

    public function getContent(): string { return $this->content; }
}
```

### Stack-Based Memento

```php
class GameState
{
    public function __construct(
        public readonly int $level,
        public readonly int $score,
        public readonly float $health
    ) {}
}

class Game
{
    private int $level = 1;
    private int $score = 0;
    private float $health = 100.0;
    private array $saveStates = [];

    public function save(): void
    {
        $this->saveStates[] = new GameState($this->level, $this->score, $this->health);
    }

    public function load(): void
    {
        if (!empty($this->saveStates)) {
            $state = array_pop($this->saveStates);
            $this->level = $state->level;
            $this->score = $state->score;
            $this->health = $state->health;
        }
    }

    public function play(): void
    {
        $this->score += 100;
        $this->health -= 10.0;
        echo "Score: {$this->score}, Health: {$this->health}\n";
    }
}
```

### Config Memento

```php
class ConfigMemento
{
    public function __construct(
        public readonly string $databaseUrl,
        public readonly int $maxConnections,
        public readonly int $version
    ) {}
}

class Config
{
    private string $databaseUrl = '';
    private int $maxConnections = 10;
    private int $version = 0;
    private array $history = [];

    public function update(string $url, int $max): void
    {
        $this->history[] = new ConfigMemento($this->databaseUrl, $this->maxConnections, $this->version);
        $this->databaseUrl = $url;
        $this->maxConnections = $max;
        $this->version++;
    }

    public function rollback(): void
    {
        if (!empty($this->history)) {
            $memento = array_pop($this->history);
            $this->databaseUrl = $memento->databaseUrl;
            $this->maxConnections = $memento->maxConnections;
            $this->version = $memento->version;
        }
    }
}
```

### Serializable Memento

```php
class SerializableMemento
{
    public static function save(object $object): string
    {
        return serialize($object);
    }

    public static function restore(string $data): object
    {
        return unserialize($data);
    }
}

// Usage
$editor = new Editor();
$editor->typeText("Hello");
$memento = SerializableMemento::save($editor);
$editor->typeText(" World");
$restored = SerializableMemento::restore($memento);
```

## Best Practices

- Use readonly properties for immutable mementos
- Store mementos in a stack for undo/redo
- Use `serialize`/`unserialize` for persistent mementos
- Document which state is captured in each memento
- Consider using deep copy for complex objects

## Interview Questions

1. What is the difference between memento and command pattern?
2. How do you handle large state snapshots efficiently?
3. How do you implement redo functionality?
4. How do you handle concurrent access to mementos?
5. When should you avoid the memento pattern?

## References

- [PHP Serialization](https://www.php.net/serialize)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
