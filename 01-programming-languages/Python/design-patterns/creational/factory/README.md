# Factory Method Pattern in Python

The Factory Method pattern defines an interface for creating objects, allowing subclasses to alter the type of objects that will be created. Python's dynamic typing and first-class functions make this pattern particularly elegant.

## When to Use

- When object creation logic is complex or needs encapsulation
- When the exact type of object to create is determined at runtime
- When you want to reuse existing objects instead of creating new ones
- When creating objects requires shared logic or resources
- When you need to support multiple product variants

## Python Implementation

### Using Functions (Pythonic)
```python
from abc import ABC, abstractmethod

class Vehicle(ABC):
    @abstractmethod
    def drive(self):
        pass

class Car(Vehicle):
    def drive(self):
        return "Driving a car"

class Truck(Vehicle):
    def drive(self):
        return "Driving a truck"

def vehicle_factory(vehicle_type: str) -> Vehicle:
    factories = {
        "car": Car,
        "truck": Truck
    }
    return factories[vehicle_type]()
```

### Using `__init_subclass__`
```python
class Serializer:
    _formats = {}
    
    def __init_subclass__(cls, format_name=None, **kwargs):
        super().__init_subclass__(**kwargs)
        if format_name:
            Serializer._formats[format_name] = cls
    
    @classmethod
    def create(cls, format_name: str):
        return cls._formats[format_name]()

class JSONSerializer(Serializer, format_name="json"):
    def serialize(self, data):
        import json
        return json.dumps(data)

class XMLSerializer(Serializer, format_name="xml"):
    def serialize(self, data):
        return f"<data>{data}</data>"
```

### Class-Based Factory
```python
class NotificationFactory:
    @staticmethod
    def create(notification_type: str):
        notifications = {
            "email": EmailNotification,
            "sms": SMSNotification,
            "push": PushNotification
        }
        return notifications[notification_type]()

class EmailNotification:
    def send(self, message):
        return f"Email: {message}"

class SMSNotification:
    def send(self, message):
        return f"SMS: {message}"
```

## Pythonic Alternative

For simple cases, use a dictionary mapping:
```python
def get_handler(action: str):
    handlers = {
        "create": create_handler,
        "update": update_handler,
        "delete": delete_handler
    }
    return handlers.get(action, default_handler)
```

## Real-World Example

```python
import json
import csv
from io import StringIO

def parse_data(data: str, format: str):
    parsers = {
        "json": lambda d: json.loads(d),
        "csv": lambda d: list(csv.reader(StringIO(d)))
    }
    return parsers[format](data)
```

## Best Practices

1. Use functions for simple factories; classes for complex ones
2. Leverage Python's dynamic typing - no need for abstract interfaces in simple cases
3. Consider using dictionaries for type mapping
4. Keep factory methods focused on single responsibility
5. Document expected return types clearly

## Interview Questions

1. What is the difference between Factory Method and Abstract Factory?
2. How would you implement a factory that supports new types without modifying existing code?
3. When would you use a function factory versus a class-based factory?
4. How does Python's dynamic typing simplify factory implementations?
5. What are the trade-offs of using `__init_subclass__` for factory registration?

## References

- *Design Patterns* - GoF, Chapter 3
- Python documentation - Abstract Base Classes
- *Fluent Python* - Luciano Ramalho
- PEP 3119 - Abstract Base Classes
