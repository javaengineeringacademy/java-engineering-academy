# Bridge Pattern in Python

The Bridge pattern decouples an abstraction from its implementation, allowing both to vary independently. Python's dynamic typing and multiple inheritance make this pattern particularly flexible.

## When to Use

- Wanting to avoid permanent binding between abstraction and implementation
- When both abstraction and implementation should be extensible
- When changes in implementation shouldn't affect clients
- For platform-independent development
- When you need to share implementation among multiple objects

## Python Implementation

### Using ABC and Composition
```python
from abc import ABC, abstractmethod

class Implementation(ABC):
    @abstractmethod
    def operation_impl(self) -> str:
        pass

class ConcreteImplementationA(Implementation):
    def operation_impl(self) -> str:
        return "Implementation A"

class ConcreteImplementationB(Implementation):
    def operation_impl(self) -> str:
        return "Implementation B"

class Abstraction:
    def __init__(self, implementation: Implementation):
        self._impl = implementation
    
    def operation(self) -> str:
        return f"Abstraction: {self._impl.operation_impl()}"

class ExtendedAbstraction(Abstraction):
    def extended_operation(self) -> str:
        return f"Extended: {self._impl.operation_impl()}"

# Usage
impl_a = ConcreteImplementationA()
impl_b = ConcreteImplementationB()

abstraction_a = Abstraction(impl_a)
abstraction_b = Abstraction(impl_b)

print(abstraction_a.operation())
print(abstraction_b.operation())
```

### Using Protocols
```python
from typing import Protocol

class RenderProtocol(Protocol):
    def render(self, shape: str) -> str: ...

class SVGRenderer:
    def render(self, shape: str) -> str:
        return f"SVG: {shape}"

class CanvasRenderer:
    def render(self, shape: str) -> str:
        return f"Canvas: {shape}"

class Shape:
    def __init__(self, renderer: RenderProtocol, name: str):
        self.renderer = renderer
        self.name = name
    
    def draw(self):
        return self.renderer.render(self.name)

# Usage
shape1 = Shape(SVGRenderer(), "Circle")
shape2 = Shape(CanvasRenderer(), "Rectangle")
```

### Dictionary-Based Bridge
```python
class PlatformBridge:
    _implementations = {
        "windows": {
            "file": lambda path: f"Windows file: {path}",
            "process": lambda name: f"Windows process: {name}"
        },
        "linux": {
            "file": lambda path: f"Linux file: {path}",
            "process": lambda name: f"Linux process: {name}"
        }
    }
    
    def __init__(self, platform: str):
        self._impl = self._implementations[platform]
    
    def file_operation(self, path: str):
        return self._impl["file"](path)
    
    def process_operation(self, name: str):
        return self._impl["process"](name)

# Usage
bridge = PlatformBridge("linux")
print(bridge.file_operation("/tmp/file"))
```

## Pythonic Alternative

Use strategy pattern for simple implementation switching:
```python
class Service:
    def __init__(self, backend=None):
        self.backend = backend or DefaultBackend()
    
    def process(self, data):
        return self.backend.handle(data)
```

## Real-World Example

```python
class NotificationSender(ABC):
    @abstractmethod
    def send(self, message: str) -> bool:
        pass

class EmailSender(NotificationSender):
    def send(self, message: str) -> bool:
        print(f"Email: {message}")
        return True

class SMSSender(NotificationSender):
    def send(self, message: str) -> bool:
        print(f"SMS: {message}")
        return True

class Notification:
    def __init__(self, sender: NotificationSender):
        self._sender = sender
    
    def notify(self, user: str, message: str):
        return self._sender.send(f"To {user}: {message}")

# Usage
email_notification = Notification(EmailSender())
sms_notification = Notification(SMSSender())
```

## Best Practices

1. Use composition over inheritance for implementation
2. Define clear interfaces for both abstraction and implementation
3. Keep implementations independent and interchangeable
4. Document which implementations work with which abstractions
5. Consider using Protocol for structural typing

## Interview Questions

1. How does Bridge differ from Adapter?
2. When would you use Bridge over Strategy?
3. How would you add a new implementation without modifying abstraction?
4. What role does composition play in Bridge pattern?
5. How would you test code using Bridge pattern?

## References

- *Design Patterns* - GoF, Chapter 4
- Python `typing.Protocol` documentation
- *Python Design Patterns* - Brandon Rhodes
- PEP 544 - Protocols
