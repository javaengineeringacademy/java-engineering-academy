# State Pattern in PHP

The State pattern allows an object to alter its behavior when its internal state changes. In PHP, this is implemented using interfaces and state classes.

## When to Use

- Object behavior depends on its state
- State transitions are complex
- Avoiding large conditional statements
- Finite state machines
- Request processing pipelines

## Implementation

### Basic State

```php
interface OrderState
{
    public function process(Order $order): void;
    public function ship(Order $order): void;
    public function deliver(Order $order): void;
    public function getStatus(): string;
}

class NewState implements OrderState
{
    public function process(Order $order): void
    {
        $order->setState(new ProcessingState());
    }

    public function ship(Order $order): void
    {
        $order->setState(new CancelledState());
    }

    public function deliver(Order $order): void
    {
        $order->setState(new CancelledState());
    }

    public function getStatus(): string { return 'new'; }
}

class ProcessingState implements OrderState
{
    public function process(Order $order): void {}
    public function ship(Order $order): void
    {
        $order->setState(new ShippedState());
    }
    public function deliver(Order $order): void
    {
        $order->setState(new CancelledState());
    }
    public function getStatus(): string { return 'processing'; }
}

class ShippedState implements OrderState
{
    public function process(Order $order): void {}
    public function ship(Order $order): void {}
    public function deliver(Order $order): void
    {
        $order->setState(new DeliveredState());
    }
    public function getStatus(): string { return 'shipped'; }
}

class DeliveredState implements OrderState
{
    public function process(Order $order): void {}
    public function ship(Order $order): void {}
    public function deliver(Order $order): void {}
    public function getStatus(): string { return 'delivered'; }
}

class CancelledState implements OrderState
{
    public function process(Order $order): void {}
    public function ship(Order $order): void {}
    public function deliver(Order $order): void {}
    public function getStatus(): string { return 'cancelled'; }
}

class Order
{
    private OrderState $state;

    public function __construct()
    {
        $this->state = new NewState();
    }

    public function setState(OrderState $state): void { $this->state = $state; }
    public function process(): void { $this->state->process($this); }
    public function ship(): void { $this->state->ship($this); }
    public function deliver(): void { $this->state->deliver($this); }
    public function getStatus(): string { return $this->state->getStatus(); }
}
```

### Enum State (PHP 8.1+)

```php
enum TrafficLight: string
{
    case Red = 'red';
    case Yellow = 'yellow';
    case Green = 'green';

    public function next(): self
    {
        return match ($this) {
            self::Red => self::Green,
            self::Yellow => self::Red,
            self::Green => self::Yellow,
        };
    }

    public function duration(): int
    {
        return match ($this) {
            self::Red => 30,
            self::Yellow => 5,
            self::Green => 25,
        };
    }
}
```

### State Machine

```php
class StateMachine
{
    private array $transitions = [];
    private string $currentState;

    public function __construct(string $initialState)
    {
        $this->currentState = $initialState;
    }

    public function addTransition(string $from, string $to, string $action): void
    {
        $this->transitions[$from][$action] = $to;
    }

    public function transition(string $action): string
    {
        if (isset($this->transitions[$this->currentState][$action])) {
            $this->currentState = $this->transitions[$this->currentState][$action];
        }
        return $this->currentState;
    }

    public function getState(): string { return $this->currentState; }
}
```

## Best Practices

- Use interfaces for state contracts
- Document valid state transitions
- Use enums for simple state machines (PHP 8.1+)
- Consider using the state pattern for complex state logic
- Implement state entry/exit actions when needed

## Interview Questions

1. What is the difference between the state pattern and a state machine?
2. How do you handle invalid state transitions?
3. When should you use enums vs classes for states?
4. How do you implement state entry/exit actions?
5. How do you test state transitions?

## References

- [PHP Enums](https://www.php.net/language.oop5.basic)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
