# Mediator Pattern in PHP

The Mediator pattern defines an object that encapsulates how a set of objects interact. In PHP, this is implemented using classes that coordinate communication between components.

## When to Use

- Complex interactions between multiple objects
- UI component coordination
- Chat room implementations
- Air traffic control systems
- Event bus systems

## Implementation

### Basic Mediator

```php
interface Mediator
{
    public function notify(object $sender, string $event): void;
}

class ChatRoom implements Mediator
{
    private array $users = [];

    public function addUser(User $user): void
    {
        $this->users[] = $user;
    }

    public function notify(object $sender, string $event): void
    {
        foreach ($this->users as $user) {
            if ($user !== $sender) {
                $user->receive($event);
            }
        }
    }
}

class User
{
    public function __construct(
        private string $name,
        private Mediator $mediator
    ) {}

    public function send(string $message): void
    {
        $this->mediator->notify($this, "{$this->name}: {$message}");
    }

    public function receive(string $message): void
    {
        echo "Received: {$message}\n";
    }
}
```

### Event Bus

```php
class EventBus
{
    private array $handlers = [];

    public function subscribe(string $event, callable $handler): void
    {
        $this->handlers[$event][] = $handler;
    }

    public function publish(string $event, mixed $data = null): void
    {
        foreach ($this->handlers[$event] ?? [] as $handler) {
            $handler($data);
        }
    }
}

// Usage
$bus = new EventBus();
$bus->subscribe('user.created', fn($user) => echo "Send welcome email to {$user}\n");
$bus->subscribe('user.created', fn($user) => echo "Log: User {$user} created\n");
$bus->publish('user.created', 'alice@example.com');
```

### Form Mediator

```php
class FormMediator
{
    private bool $buttonEnabled = false;
    private bool $textValid = false;

    public function textChanged(bool $valid): void
    {
        $this->textValid = $valid;
        $this->updateButton();
    }

    public function checkboxChanged(bool $checked): void
    {
        $this->updateButton();
    }

    private function updateButton(): void
    {
        $this->buttonEnabled = $this->textValid;
        echo "Submit button enabled: " . ($this->buttonEnabled ? 'true' : 'false') . "\n";
    }
}
```

### Mediator with Events

```php
class MediatorEvent
{
    public function __construct(
        public readonly string $type,
        public readonly mixed $data,
        public readonly object $sender
    ) {}
}

class EventMediator
{
    private array $handlers = [];

    public function register(string $event, callable $handler): void
    {
        $this->handlers[$event][] = $handler;
    }

    public function dispatch(MediatorEvent $event): void
    {
        foreach ($this->handlers[$event->type] ?? [] as $handler) {
            $handler($event);
        }
    }
}
```

## Best Practices

- Keep mediators focused on coordination, not business logic
- Use dependency injection for mediator dependencies
- Document the events and their handlers
- Consider using event buses for decoupled communication
- Implement cleanup logic when components are removed

## Interview Questions

1. How does the mediator pattern differ from the observer pattern?
2. When should you use a mediator vs direct communication?
3. How do you handle mediator cleanup when components are destroyed?
4. How do you test components that depend on a mediator?
5. What are the thread-safety considerations for mediators?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
