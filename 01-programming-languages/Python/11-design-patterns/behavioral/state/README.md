# State Pattern in Python

The State pattern allows an object to alter its behavior when its internal state changes. The object will appear to change its class. Python's dynamic typing makes state transitions clean and explicit.

## When to Use

- Object behavior depends on its state
- Complex conditional statements based on state
- State transitions need to be explicit
- Finite state machines
- Document workflow, order processing, game states

## Python Implementation

### Class-Based State
```python
from abc import ABC, abstractmethod

class State(ABC):
    @abstractmethod
    def handle(self, context):
        pass

class IdleState(State):
    def handle(self, context):
        print("Starting process...")
        context.state = ProcessingState()

class ProcessingState(State):
    def handle(self, context):
        print("Processing...")
        context.state = CompletedState()

class CompletedState(State):
    def handle(self, context):
        print("Already completed")

class Workflow:
    def __init__(self):
        self._state = IdleState()

    @property
    def state(self):
        return self._state

    @state.setter
    def state(self, state: State):
        self._state = state

    def proceed(self):
        self._state.handle(self)

# Usage
workflow = Workflow()
workflow.proceed()  # Starting process...
workflow.proceed()  # Processing...
workflow.proceed()  # Already completed
```

### Dictionary-Based State
```python
class TrafficLight:
    def __init__(self):
        self._state = "red"
        self._transitions = {
            "red": "green",
            "green": "yellow",
            "yellow": "red"
        }
        self._actions = {
            "red": lambda: "Stop",
            "green": lambda: "Go",
            "yellow": lambda: "Caution"
        }

    def next(self):
        self._state = self._transitions[self._state]

    def action(self):
        return self._actions[self._state]()

# Usage
light = TrafficLight()
print(light.action())  # Stop
light.next()
print(light.action())  # Go
```

### State Machine with Transitions
```python
class StateMachine:
    def __init__(self, initial_state: str):
        self._state = initial_state
        self._transitions = {}

    def add_transition(self, from_state: str, to_state: str, event: str):
        if from_state not in self._transitions:
            self._transitions[from_state] = {}
        self._transitions[from_state][event] = to_state

    def trigger(self, event: str):
        if (self._state in self._transitions and
                event in self._transitions[self._state]):
            self._state = self._transitions[self._state][event]
            return True
        return False

    @property
    def state(self):
        return self._state

# Usage
sm = StateMachine("idle")
sm.add_transition("idle", "running", "start")
sm.add_transition("running", "stopped", "stop")
sm.add_transition("stopped", "idle", "reset")

print(sm.state)  # idle
sm.trigger("start")
print(sm.state)  # running
```

## Pythonic Alternative

Use enums and match statements (Python 3.10+):
```python
from enum import Enum

class OrderState(Enum):
    PENDING = "pending"
    PROCESSING = "processing"
    SHIPPED = "shipped"
    DELIVERED = "delivered"

def handle_state(state: OrderState):
    match state:
        case OrderState.PENDING:
            return "Awaiting payment"
        case OrderState.PROCESSING:
            return "Preparing order"
        case OrderState.SHIPPED:
            return "In transit"
        case OrderState.DELIVERED:
            return "Complete"
```

## Real-World Example

```python
class Order:
    def __init__(self):
        self._state = "pending"
        self._handlers = {
            "pending": self._handle_pending,
            "paid": self._handle_paid,
            "shipped": self._handle_shipped
        }

    def process(self):
        handler = self._handlers.get(self._state)
        if handler:
            return handler()
        return f"No handler for state: {self._state}"

    def _handle_pending(self):
        self._state = "paid"
        return "Payment received"

    def _handle_paid(self):
        self._state = "shipped"
        return "Order shipped"

    def _handle_shipped(self):
        return "Order delivered"
```

## Best Practices

1. Make state transitions explicit and documented
2. Keep state logic within state classes
3. Validate transitions to prevent invalid states
4. Consider using enums for simple states
5. Test all state transition paths

## Interview Questions

1. How does State differ from Strategy pattern?
2. When would you use State over conditional logic?
3. How would you implement state persistence?
4. How do you handle invalid state transitions?
5. What are the testing strategies for state machines?

## References

- *Design Patterns* - GoF, Chapter 5
- Python `enum` documentation
- *Fluent Python* - Luciano Ramalho
