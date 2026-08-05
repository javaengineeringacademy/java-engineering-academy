# Mediator Pattern in Python

The Mediator pattern defines an object that encapsulates how a set of objects interact. It promotes loose coupling by preventing objects from referring to each other explicitly and allows their interaction to vary independently.

## When to Use

- Set of objects communicate in well-defined but complex ways
- Reusing an object is difficult because it refers to many other objects
- Behavior distributed among several classes should be customizable
- Chat systems, air traffic control, event buses
- GUI component coordination

## Python Implementation

### Event-Based Mediator
```python
from typing import Dict, List, Callable

class EventMediator:
    def __init__(self):
        self._listeners: Dict[str, List[Callable]] = {}

    def subscribe(self, event: str, callback: Callable):
        if event not in self._listeners:
            self._listeners[event] = []
        self._listeners[event].append(callback)

    def unsubscribe(self, event: str, callback: Callable):
        self._listeners[event].remove(callback)

    def emit(self, event: str, data=None):
        for callback in self._listeners.get(event, []):
            callback(data)

class ChatRoom:
    def __init__(self):
        self.mediator = EventMediator()
        self.users = {}

    def register_user(self, username: str):
        self.users[username] = User(username, self.mediator)

    def send_message(self, sender: str, message: str):
        self.mediator.emit("message", {"sender": sender, "message": message})

class User:
    def __init__(self, username: str, mediator: EventMediator):
        self.username = username
        self.mediator = mediator
        self.mediator.subscribe("message", self.on_message)

    def on_message(self, data):
        if data["sender"] != self.username:
            print(f"[{self.username}] Received: {data['message']}")

    def send(self, message: str):
        self.mediator.emit("message", {"sender": self.username, "message": message})

# Usage
room = ChatRoom()
room.register_user("Alice")
room.register_user("Bob")

room.users["Alice"].send("Hello everyone!")
```

### Class-Based Mediator
```python
from abc import ABC, abstractmethod

class Mediator(ABC):
    @abstractmethod
    def notify(self, sender: object, event: str):
        pass

class ConcreteMediator(Mediator):
    def __init__(self):
        self._components = []

    def add_component(self, component):
        self._components.append(component)
        component.mediator = self

    def notify(self, sender: object, event: str):
        for component in self._components:
            if component != sender:
                component.receive(event)

class Component:
    def __init__(self, name: str):
        self.name = name
        self.mediator = None

    def send(self, event: str):
        self.mediator.notify(self, event)

    def receive(self, event: str):
        print(f"{self.name} received: {event}")

# Usage
mediator = ConcreteMediator()
comp_a = Component("A")
comp_b = Component("B")

mediator.add_component(comp_a)
mediator.add_component(comp_b)

comp_a.send("Hello")  # B receives: Hello
```

## Pythonic Alternative

Use simple event emitter:
```python
class EventEmitter:
    def __init__(self):
        self._events = {}

    def on(self, event, callback):
        self._events.setdefault(event, []).append(callback)

    def emit(self, event, *args, **kwargs):
        for callback in self._events.get(event, []):
            callback(*args, **kwargs)

# Usage
emitter = EventEmitter()
emitter.on("data", lambda d: print(f"Got: {d}"))
emitter.emit("data", "test")
```

## Real-World Example

```python
class SmartHome:
    def __init__(self):
        self._devices = {}
        self._rules = []

    def register(self, name: str, device):
        self._devices[name] = device

    def add_rule(self, trigger: str, action: str):
        self._rules.append((trigger, action))

    def trigger(self, event: str):
        for trigger, action in self._rules:
            if trigger == event:
                device_name, method = action.split(".")
                if device_name in self._devices:
                    getattr(self._devices[device_name], method)()

class Light:
    def turn_on(self):
        print("Light on")

    def turn_off(self):
        print("Light off")

# Usage
home = SmartHome()
home.register("living_room", Light())
home.add_rule("motion", "living_room.turn_on")
home.trigger("motion")  # Light on
```

## Best Practices

1. Keep mediator focused on coordination, not business logic
2. Define clear communication protocols
3. Use event-based approach for scalability
4. Document component interactions
5. Consider async mediators for non-blocking communication

## Interview Questions

1. How does Mediator differ from Observer?
2. When would you use Mediator over direct communication?
3. How would you test mediator-based systems?
4. What are the scalability concerns with Mediator?
5. How do you prevent mediator from becoming a god object?

## References

- *Design Patterns* - GoF, Chapter 5
- `asyncio` documentation for async mediators
- *Python Design Patterns* - Brandon Rhodes
