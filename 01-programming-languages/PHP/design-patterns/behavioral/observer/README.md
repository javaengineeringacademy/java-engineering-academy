# Observer Pattern in PHP

The Observer pattern defines a one-to-many dependency between objects. In PHP, this is implemented using interfaces, SplObserver, or callable arrays.

## When to Use

- Event-driven architectures
- UI notification systems
- Model-view separation
- Distributed systems communication
- Decoupling publishers from subscribers

## Implementation

### Basic Observer

```php
interface Observer
{
    public function update(object $subject): void;
}

class User implements Observer
{
    public function __construct(private string $name) {}

    public function update(object $subject): void
    {
        echo "{$this->name} notified\n";
    }
}

class EventManager
{
    private array $listeners = [];

    public function subscribe(string $event, Observer $observer): void
    {
        $this->listeners[$event][] = $observer;
    }

    public function unsubscribe(string $event, Observer $observer): void
    {
        $this->listeners[$event] = array_filter(
            $this->listeners[$event],
            fn($o) => $o !== $observer
        );
    }

    public function notify(string $event): void
    {
        foreach ($this->listeners[$event] ?? [] as $observer) {
            $observer->update($this);
        }
    }
}
```

### SplObserver Implementation

```php
class UserData implements \SplSubject
{
    private array $observers = [];
    private string $name;

    public function __construct(string $name)
    {
        $this->name = $name;
    }

    public function attach(\SplObserver $observer): void
    {
        $this->observers[] = $observer;
    }

    public function detach(\SplObserver $observer): void
    {
        $this->observers = array_filter($this->observers, fn($o) => $o !== $observer);
    }

    public function notify(): void
    {
        foreach ($this->observers as $observer) {
            $observer->update($this);
        }
    }

    public function getName(): string { return $this->name; }
}
```

### Closure-Based Observer

```php
class EventEmitter
{
    private array $listeners = [];

    public function on(string $event, callable $listener): void
    {
        $this->listeners[$event][] = $listener;
    }

    public function emit(string $event, mixed $data = null): void
    {
        foreach ($this->listeners[$event] ?? [] as $listener) {
            $listener($data);
        }
    }
}

// Usage
$emitter = new EventEmitter();
$emitter->on('message', fn($data) => echo "Handler 1: {$data}\n");
$emitter->on('message', fn($data) => echo "Handler 2: {$data}\n");
$emitter->emit('message', 'Hello');
```

### Property Observer

```php
class ObservableProperty
{
    private array $observers = [];
    private mixed $value;

    public function __construct(mixed $initial = null)
    {
        $this->value = $initial;
    }

    public function subscribe(callable $observer): void
    {
        $this->observers[] = $observer;
    }

    public function get(): mixed { return $this->value; }

    public function set(mixed $value): void
    {
        $this->value = $value;
        $this->notifyObservers($value);
    }

    private function notifyObservers(mixed $value): void
    {
        foreach ($this->observers as $observer) {
            $observer($value);
        }
    }
}
```

## Best Practices

- Use interfaces for observer contracts
- Implement `attach`, `detach`, and `notify` methods
- Use closures for lightweight observers
- Document thread-safety guarantees
- Consider using weak references for automatic cleanup

## Interview Questions

1. What is the difference between observer and event dispatcher?
2. How do you handle observer cleanup when objects are destroyed?
3. How do you implement thread-safe observers in PHP?
4. When should you use closures vs interface-based observers?
5. How do you prioritize observer notification order?

## References

- [SplObserver](https://www.php.net/class.splsubject)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
