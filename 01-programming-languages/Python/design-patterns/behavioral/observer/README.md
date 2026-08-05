# Observer Pattern in Python

The Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically. Python's callback functions make this pattern natural and flexible.

## When to Use

- When changes to one object require changing others, and you don't know how many objects need to change
- When an object should notify other objects without being tightly coupled
- Event handling systems, GUI frameworks
- Publish-subscribe systems
- Real-time data feeds, notifications

## Python Implementation

### Basic Observer
```python
from typing import Callable, List, Any

class Subject:
    def __init__(self):
        self._observers: List[Callable] = []
        self._state: Any = None
    
    def attach(self, observer: Callable):
        self._observers.append(observer)
    
    def detach(self, observer: Callable):
        self._observers.remove(observer)
    
    def notify(self):
        for observer in self._observers:
            observer(self._state)
    
    @property
    def state(self):
        return self._state
    
    @state.setter
    def state(self, value):
        self._state = value
        self.notify()

# Usage
def observer1(state):
    print(f"Observer 1 received: {state}")

def observer2(state):
    print(f"Observer 2 received: {state}")

subject = Subject()
subject.attach(observer1)
subject.attach(observer2)
subject.state = "New data"  # Notifies both observers
```

### Class-Based Observer
```python
from abc import ABC, abstractmethod
from typing import List

class Observer(ABC):
    @abstractmethod
    def update(self, subject):
        pass

class EventManager:
    def __init__(self):
        self._listeners = {}
    
    def subscribe(self, event_type: str, listener):
        if event_type not in self._listeners:
            self._listeners[event_type] = []
        self._listeners[event_type].append(listener)
    
    def unsubscribe(self, event_type: str, listener):
        self._listeners[event_type].remove(listener)
    
    def notify(self, event_type: str, data):
        for listener in self._listeners.get(event_type, []):
            listener(data)

class Editor:
    def __init__(self):
        self.events = EventManager()
        self._filename = ""
    
    @property
    def filename(self):
        return self._filename
    
    @filename.setter
    def filename(self, value):
        self._filename = value
        self.events.notify("save", {"filename": value})

# Usage
class LoggingListener:
    def log(self, data):
        print(f"Log: {data}")

editor = Editor()
logger = LoggingListener()
editor.events.subscribe("save", logger.log)
editor.filename = "test.txt"  # Triggers notification
```

## Pythonic Alternative

Use signals or simple callbacks:
```python
class Signal:
    def __init__(self):
        self._receivers = []
    
    def connect(self, receiver):
        self._receivers.append(receiver)
    
    def send(self, sender, **kwargs):
        for receiver in self._receivers:
            receiver(sender=sender, **kwargs)

# Usage
class UserModel:
    def __init__(self):
        self.on_change = Signal()
        self._name = ""
    
    @property
    def name(self):
        return self._name
    
    @name.setter
    def name(self, value):
        self._name = value
        self.on_change.send(self, name=value)
```

## Real-World Example

```python
class PriceAlert:
    def __init__(self, symbol: str, target_price: float):
        self.symbol = symbol
        self.target_price = target_price
        self.callbacks = []
    
    def on_price_update(self, callback):
        self.callbacks.append(callback)
    
    def update_price(self, current_price: float):
        if current_price >= self.target_price:
            for callback in self.callbacks:
                callback(self.symbol, current_price)

# Usage
def alert_handler(symbol, price):
    print(f"ALERT: {symbol} hit ${price}")

alert = PriceAlert("AAPL", 150.00)
alert.on_price_update(alert_handler)
alert.update_price(152.50)  # Triggers alert
```

## Best Practices

1. Keep observers simple and focused
2. Avoid circular dependencies
3. Use weak references to prevent memory leaks
4. Consider thread safety for concurrent updates
5. Document notification order guarantees

## Interview Questions

1. What is the difference between Observer and Pub-Sub?
2. How would you handle observer notification failures?
3. What are memory leak concerns with Observer pattern?
4. How would you implement thread-safe observers?
5. When would you use signals over direct callbacks?

## References

- *Design Patterns* - GoF, Chapter 5
- Python `weakref` documentation
- *Fluent Python* - Luciano Ramalho
- PEP 484 - Type Hints
