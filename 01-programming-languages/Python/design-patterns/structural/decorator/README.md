# Decorator Pattern in Python

The Decorator pattern attaches additional responsibilities to an object dynamically. Python provides native decorator syntax (`@decorator`) that makes this pattern particularly elegant and widely used.

## When to Use

- Adding responsibilities to objects dynamically
- Extending functionality without subclassing
- Supporting multiple combinations of behavior
- When extension by subclassing is impractical
- Logging, caching, authentication, validation

## Python Implementation

### Function Decorators
```python
import functools
import time

def timer(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"{func.__name__} took {end - start:.4f}s")
        return result
    return wrapper

def retry(max_attempts: int = 3):
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(max_attempts):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_attempts - 1:
                        raise
                    print(f"Attempt {attempt + 1} failed: {e}")
        return wrapper
    return decorator

@timer
@retry(max_attempts=3)
def fetch_data(url):
    # Simulate network call
    return f"Data from {url}"
```

### Class-Based Decorators
```python
class CountCalls:
    def __init__(self, func):
        functools.update_wrapper(self, func)
        self.func = func
        self.call_count = 0
    
    def __call__(self, *args, **kwargs):
        self.call_count += 1
        print(f"Call #{self.call_count}")
        return self.func(*args, **kwargs)
    
    def reset(self):
        self.call_count = 0

@CountCalls
def greet(name):
    return f"Hello, {name}"
```

### Stacking Decorators
```python
def bold(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        return f"<b>{func(*args, **kwargs)}</b>"
    return wrapper

def italic(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        return f"<i>{func(*args, **kwargs)}</i>"
    return wrapper

@bold
@italic
def emphasize(text):
    return text

# emphasize("Hello") returns "<b><i>Hello</i></b>"
```

## Pythonic Alternative

Use `functools.wraps` to preserve metadata:
```python
import functools

def my_decorator(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        """Wrapper docstring"""
        return func(*args, **kwargs)
    return wrapper
```

## Real-World Example

```python
from functools import wraps
import logging

def log_errors(logger):
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            try:
                return func(*args, **kwargs)
            except Exception as e:
                logger.error(f"Error in {func.__name__}: {e}")
                raise
        return wrapper
    return decorator

# Usage
logger = logging.getLogger(__name__)

@log_errors(logger)
def process_order(order_id):
    if order_id < 0:
        raise ValueError("Invalid order ID")
    return f"Processed order {order_id}"
```

## Best Practices

1. Always use `functools.wraps` to preserve function metadata
2. Keep decorators focused on single responsibility
3. Document decorator behavior clearly
4. Use parameterized decorators for configurable behavior
5. Consider class-based decorators for stateful behavior

## Interview Questions

1. What is the difference between function and class-based decorators?
2. Why is `functools.wraps` important?
3. How do decorators differ from the Decorator pattern in GoF?
4. How would you create a decorator with optional arguments?
5. What are common use cases for decorators in Python?

## References

- Python documentation - Decorators
- PEP 318 - Decorators for Functions and Methods
- *Fluent Python* - Luciano Ramalho
- *Python Cookbook* - Alex Martelli
