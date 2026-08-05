# Singleton Pattern in Python

The Singleton pattern ensures a class has only one instance and provides a global point of access to it. Python offers multiple implementation approaches, from metaclasses to simple modules.

## When to Use

- Managing shared resources (database connections, file handlers)
- Maintaining consistent state across application
- Controlling access to shared configuration
- Logging services
- Thread pools or connection pools

## Python Implementations

### Metaclass Approach
```python
class SingletonMeta(type):
    _instances = {}
    
    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            instance = super().__call__(*args, **kwargs)
            cls._instances[cls] = instance
        return cls._instances[cls]

class Database(metaclass=SingletonMeta):
    def __init__(self):
        self.connection = None
    
    def connect(self):
        if self.connection is None:
            self.connection = "Connected"
        return self.connection
```

### Decorator Approach
```python
import functools

def singleton(cls):
    instances = {}
    
    @functools.wraps(cls)
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return get_instance

@singleton
class Logger:
    def __init__(self):
        self.logs = []
    
    def log(self, message):
        self.logs.append(message)
```

### Module-Level Singleton
```python
# config.py - Simplest Pythonic approach
class _Config:
    def __init__(self):
        self.settings = {}
    
    def get(self, key, default=None):
        return self.settings.get(key, default)

config = _Config()  # Module-level instance
```

## Thread-Safe Singleton
```python
import threading

class ThreadSafeSingleton:
    _lock = threading.Lock()
    _instance = None
    
    def __new__(cls):
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance
```

## Pythonic Alternative

Python modules are天然 singletons. Prefer module-level state for most use cases:
```python
# database.py
_connection = None

def get_connection():
    global _connection
    if _connection is None:
        _connection = create_connection()
    return _connection
```

## Real-World Example

```python
class AppConfig:
    _instance = None
    
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._load_config()
        return cls._instance
    
    def _load_config(self):
        self.database_url = "postgresql://localhost/mydb"
        self.debug = False
        self.secret_key = "dev-key"
```

## Best Practices

1. Prefer module-level singletons over class-based implementations
2. Consider dependency injection over global state
3. Make singleton behavior explicit in documentation
4. Ensure thread safety if used in concurrent applications
5. Test with mocked instances to avoid state leakage

## Interview Questions

1. What problems does the Singleton pattern solve?
2. Name three different ways to implement Singleton in Python
3. Why are Python modules considered natural singletons?
4. What are the drawbacks of using Singleton pattern?
5. How would you make a thread-safe Singleton in Python?

## References

- *Design Patterns* - GoF, Chapter 3
- PEP 8 - Singleton naming conventions
- Python documentation - `__new__` method
- Alex Martelli - *Python Cookbook*
