"""Function decorators, class decorators, and patterns."""

import time
import functools

# ── Basic Decorator ──────────────────────────────────────────────────
def timer(func):
    """Measure function execution time."""
    @functools.wraps(func)  # Preserves original function metadata
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        elapsed = time.time() - start
        print(f"{func.__name__} took {elapsed:.4f}s")
        return result
    return wrapper

@timer
def slow_add(a, b):
    time.sleep(0.1)
    return a + b

print(slow_add(1, 2))  # 3 (with timing output)

# ── Decorator with Arguments ─────────────────────────────────────────
def retry(max_attempts=3, delay=1):
    """Retry decorator with configurable attempts."""
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(1, max_attempts + 1):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_attempts:
                        raise
                    print(f"Attempt {attempt} failed: {e}. Retrying...")
                    time.sleep(delay)
        return wrapper
    return decorator

@retry(max_attempts=3, delay=0.5)
def unreliable():
    import random
    if random.random() < 0.7:
        raise ValueError("Random failure")
    return "success"

# ── Stacking Decorators ──────────────────────────────────────────────
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
def greet(name):
    return f"Hello, {name}"

# Applied bottom-up: italic first, then bold
print(greet("Alice"))  # <b><i>Hello, Alice</i></b>

# ── Class Decorator ──────────────────────────────────────────────────
def add_repr(cls):
    """Add __repr__ based on __init__ parameters."""
    import inspect
    sig = inspect.signature(cls.__init__)
    params = [p for p in sig.parameters if p != 'self']

    def __repr__(self):
        args = ", ".join(f"{p}={getattr(self, p)!r}" for p in params)
        return f"{cls.__name__}({args})"

    cls.__repr__ = __repr__
    return cls

@add_repr
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

print(Point(1, 2))  # Point(x=1, y=2)

# ── functools.lru_cache ─────────────────────────────────────────────
@functools.lru_cache(maxsize=128)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

print(fibonacci(50))  # Fast due to caching

# ── Property Decorator ──────────────────────────────────────────────
class Circle:
    def __init__(self, radius):
        self._radius = radius

    @property
    def radius(self):
        return self._radius

    @radius.setter
    def radius(self, value):
        if value < 0:
            raise ValueError("Radius must be non-negative")
        self._radius = value

    @property
    def area(self):
        import math
        return math.pi * self._radius ** 2

# ── Staticmethod and Classmethod ────────────────────────────────────
class Date:
    def __init__(self, year, month, day):
        self.year = year
        self.month = month
        self.day = day

    @classmethod
    def from_string(cls, date_str):
        year, month, day = map(int, date_str.split("-"))
        return cls(year, month, day)

    @staticmethod
    def is_valid(year, month, day):
        return 1 <= month <= 12 and 1 <= day <= 31

# ── Dataclass Decorators ────────────────────────────────────────────
from dataclasses import dataclass, field

@dataclass(frozen=True)  # Immutable
class Config:
    host: str = "localhost"
    port: int = 8080
    debug: bool = False
    tags: list = field(default_factory=list, repr=False)

config = Config(host="example.com", port=443)
print(config)  # Config(host='example.com', port=443, debug=False)

# ── Singletone Pattern ──────────────────────────────────────────────
def singleton(cls):
    instances = {}
    @functools.wraps(cls)
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return get_instance

@singleton
class Database:
    def __init__(self):
        self.connection = "connected"

db1 = Database()
db2 = Database()
print(db1 is db2)  # True — same instance
