# Abstract Factory Pattern in Python

The Abstract Factory pattern provides an interface for creating families of related or dependent objects without specifying their concrete classes. It's useful when a system must be independent of how its objects are created.

## When to Use

- Creating families of related objects that must be used together
- Ensuring consistency across a product family
- Providing a library of products revealing only interfaces, not implementations
- When object creation involves multiple configuration options
- Cross-platform UI or database abstraction layers

## Python Implementation

### Using ABC and Protocols
```python
from abc import ABC, abstractmethod
from typing import Protocol

class Button(ABC):
    @abstractmethod
    def render(self):
        pass

class Checkbox(ABC):
    @abstractmethod
    def render(self):
        pass

class WindowsButton(Button):
    def render(self):
        return "Windows Button"

class WindowsCheckbox(Checkbox):
    def render(self):
        return "Windows Checkbox"

class MacButton(Button):
    def render(self):
        return "Mac Button"

class MacCheckbox(Checkbox):
    def render(self):
        return "Mac Checkbox"

class GUIFactory(ABC):
    @abstractmethod
    def create_button(self) -> Button:
        pass
    
    @abstractmethod
    def create_checkbox(self) -> Checkbox:
        pass

class WindowsFactory(GUIFactory):
    def create_button(self):
        return WindowsButton()
    
    def create_checkbox(self):
        return WindowsCheckbox()

class MacFactory(GUIFactory):
    def create_button(self):
        return MacButton()
    
    def create_checkbox(self):
        return MacCheckbox()
```

### Using Dictionaries (Pythonic)
```python
class UIFactory:
    _factories = {
        "windows": {
            "button": lambda: "Windows Button",
            "checkbox": lambda: "Windows Checkbox"
        },
        "mac": {
            "button": lambda: "Mac Button",
            "checkbox": lambda: "Mac Checkbox"
        }
    }
    
    def __init__(self, platform: str):
        self._factory = self._factories[platform]
    
    def create_button(self):
        return self._factory["button"]()
    
    def create_checkbox(self):
        return self._factory["checkbox"]()
```

## Pythonic Alternative

Use configuration dictionaries for simple product families:
```python
DATABASES = {
    "postgres": {
        "connection": PostgresConnection,
        "query": PostgresQuery
    },
    "mysql": {
        "connection": MySQLConnection,
        "query": MySQLQuery
    }
}

def get_database(db_type: str):
    return DATABASES[db_type]
```

## Real-World Example

```python
import sqlite3
import psycopg2

class DatabaseFactory:
    def __init__(self, db_config: dict):
        self.config = db_config
    
    def create_connection(self):
        if self.config["type"] == "sqlite":
            return sqlite3.connect(self.config["path"])
        elif self.config["type"] == "postgres":
            return psycopg2.connect(self.config["dsn"])
    
    def create_cursor(self):
        conn = self.create_connection()
        return conn.cursor()
```

## Best Practices

1. Use Abstract Base Classes to define product interfaces
2. Consider factory registries for extensibility
3. Keep product families small and focused
4. Use dependency injection to provide factories to clients
5. Document which products belong to which family

## Interview Questions

1. How does Abstract Factory differ from Factory Method?
2. When would you use Abstract Factory over simple Factory?
3. How would you add a new product family without modifying existing code?
4. What role do Abstract Base Classes play in this pattern?
5. How would you handle optional products in a family?

## References

- *Design Patterns* - GoF, Chapter 3
- Python ABC documentation
- *Python Design Patterns* - Brandon Rhodes
- PEP 3119 - Abstract Base Classes
