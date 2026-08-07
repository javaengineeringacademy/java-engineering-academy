"""
Decorators — Senior-Level Reference

This module demonstrates decorator patterns from basic to production-grade.
Each section builds on the previous one. Run sections individually or as a whole.
"""

import time
import functools
import inspect
import logging
import random
from typing import Any, Callable, TypeVar
from dataclasses import dataclass, field

logger = logging.getLogger(__name__)

F = TypeVar("F", bound=Callable[..., Any])


# ── 1. Function Objects ───────────────────────────────────────────────
# Functions are first-class objects. They have attributes and identity.

def demonstrate_function_objects():
    """Functions are objects with attributes, identity, and lifetime."""

    def greet(name: str) -> str:
        """Return a greeting."""
        return f"Hello, {name}"

    # Functions have attributes
    print(f"__name__: {greet.__name__}")
    print(f"__doc__: {greet.__doc__}")
    print(f"__module__: {greet.__module__}")
    print(f"__qualname__: {greet.__qualname__}")
    print(f"__dict__: {greet.__dict__}")
    print(f"__annotations__: {greet.__annotations__}")

    # Functions can be stored in data structures
    functions = [greet, str.upper, str.lower]
    print(f"First function: {functions[0]('Alice')}")

    # Functions can be passed as arguments
    def apply(func, value):
        return func(value)

    print(f"apply(greet, 'Bob'): {apply(greet, 'Bob')}")

    # Functions can be dynamically created
    def create_adder(n):
        return lambda x: x + n

    add5 = create_adder(5)
    print(f"add5(10) = {add5(10)}")


# ── 2. Closures ───────────────────────────────────────────────────────
# Closures capture variables from enclosing scope by reference.

def make_multiplier(factor: int) -> Callable[[int], int]:
    """Return a function that multiplies by factor."""
    def multiplier(x: int) -> int:
        return x * factor  # 'factor' is a free variable
    return multiplier


double = make_multiplier(2)
triple = make_multiplier(3)

# Verify closure contents
print(f"double(5) = {double(5)}")  # 10
print(f"triple(5) = {triple(5)}")  # 15
print(f"double closure: {double.__closure__[0].cell_contents}")  # 2

# Demonstrate reference capture bug
def demonstrate_closure_bug():
    """Closures capture by reference — this causes subtle bugs."""
    funcs = []
    for i in range(5):
        # BAD: All closures capture the same 'i' by reference
        funcs.append(lambda: i)
    
    # All return 4 (the final value of i)
    print(f"All closures return: {[f() for f in funcs]}")  # [4, 4, 4, 4, 4]

    # GOOD: Capture value using default argument
    funcs_good = []
    for i in range(5):
        funcs_good.append(lambda i=i: i)  # Default arg captures value now
    
    print(f"Good closures return: {[f() for f in funcs_good]}")  # [0, 1, 2, 3, 4]


# ── 3. Basic Decorator ────────────────────────────────────────────────
# @decorator is syntactic sugar for func = decorator(func)

def timer(func: F) -> F:
    """Measure function execution time. Uses @wraps to preserve metadata."""
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        start = time.perf_counter()
        result = func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        print(f"{func.__name__} took {elapsed:.6f}s")
        return result
    return wrapper


@timer
def slow_add(a: int, b: int) -> int:
    """Add two numbers with a delay."""
    time.sleep(0.1)
    return a + b


# Verify metadata preservation
print(f"slow_add.__name__: {slow_add.__name__}")  # 'slow_add'
print(f"slow_add.__doc__: {slow_add.__doc__}")
print(f"slow_add.__wrapped__: {slow_add.__wrapped__}")  # Original function


# ── 4. Decorator with Arguments ───────────────────────────────────────
# Extra nesting layer needed for arguments

def retry(max_attempts: int = 3, delay: float = 1.0, backoff: float = 2.0):
    """Retry decorator with exponential backoff and jitter."""
    def decorator(func: F) -> F:
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            last_exception = None
            for attempt in range(1, max_attempts + 1):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    last_exception = e
                    if attempt == max_attempts:
                        raise
                    wait_time = delay * (backoff ** (attempt - 1))
                    wait_time *= random.uniform(0.5, 1.5)  # Jitter
                    logger.warning(
                        f"Attempt {attempt}/{max_attempts} failed: {e}. "
                        f"Retrying in {wait_time:.1f}s..."
                    )
                    time.sleep(wait_time)
            raise last_exception  # Should never reach here
        return wrapper
    return decorator


@retry(max_attempts=3, delay=0.1, backoff=1.5)
def unreliable_function() -> str:
    """Simulate a function that fails randomly."""
    if random.random() < 0.7:
        raise ValueError("Random failure")
    return "success"


# ── 5. Stacking Decorators ────────────────────────────────────────────
# Applied bottom-up: @bold @italic def f() → bold(italic(f))

def bold(func: F) -> F:
    """Wrap result in bold HTML tags."""
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        return f"<b>{func(*args, **kwargs)}</b>"
    return wrapper


def italic(func: F) -> F:
    """Wrap result in italic HTML tags."""
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        return f"<i>{func(*args, **kwargs)}</i>"
    return wrapper


@bold
@italic
def greet(name: str) -> str:
    """Return a greeting."""
    return f"Hello, {name}"


# Applied bottom-up: italic first, then bold
print(greet("Alice"))  # <b><i>Hello, Alice</i></b>


# ── 6. Class-based Decorators ─────────────────────────────────────────
# Use __call__ for stateful decoration

class CountCalls:
    """Decorator that counts function calls. Useful for profiling."""

    def __init__(self, func: F):
        functools.update_wrapper(self, func)
        self.func = func
        self.call_count = 0
        self.total_time = 0.0

    def __call__(self, *args, **kwargs):
        self.call_count += 1
        start = time.perf_counter()
        result = self.func(*args, **kwargs)
        self.total_time += time.perf_counter() - start
        return result

    def reset(self):
        self.call_count = 0
        self.total_time = 0.0

    @property
    def average_time(self) -> float:
        if self.call_count == 0:
            return 0.0
        return self.total_time / self.call_count

    def __repr__(self):
        return (
            f"<CountCalls {self.func.__name__}: "
            f"calls={self.call_count}, avg_time={self.average_time:.6f}>"
        )


@CountCalls
def process_item(item: int) -> int:
    """Process an item."""
    time.sleep(0.01)
    return item * 2


# ── 7. Class Decorator (modifying classes) ────────────────────────────
# Decorators can modify or replace entire classes

def add_repr(cls):
    """Add __repr__ based on __init__ parameters."""
    sig = inspect.signature(cls.__init__)
    params = [p for p in sig.parameters if p != 'self']

    def __repr__(self):
        args = ", ".join(f"{p}={getattr(self, p)!r}" for p in params)
        return f"{cls.__name__}({args})"

    cls.__repr__ = __repr__
    return cls


@add_repr
class Point:
    def __init__(self, x: float, y: float):
        self.x = x
        self.y = y


print(Point(1, 2))  # Point(x=1, y=2)


# ── 8. Singleton Pattern ──────────────────────────────────────────────
# Class decorator that ensures only one instance exists

def singleton(cls):
    """Ensure only one instance of the class exists."""
    instances = {}

    @functools.wraps(cls)
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]

    get_instance._instances = instances  # Allow access for testing
    return get_instance


@singleton
class Database:
    def __init__(self):
        self.connection = "connected"

    def query(self, sql):
        return f"Executing: {sql}"


db1 = Database()
db2 = Database()
print(f"db1 is db2: {db1 is db2}")  # True — same instance


# ── 9. Production Patterns ────────────────────────────────────────────

# 9a. Memoization
def memoize(func: F) -> F:
    """Cache function results based on arguments. Simpler than lru_cache for demonstration."""
    cache = {}

    @functools.wraps(func)
    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]

    wrapper.cache = cache
    wrapper.cache_info = lambda: {"size": len(cache)}
    wrapper.cache_clear = cache.clear
    return wrapper


@memoize
def fibonacci(n: int) -> int:
    """Calculate fibonacci number with memoization."""
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)


# 9b. Logging decorator
def log_execution(level: int = logging.INFO):
    """Log function calls with arguments and return values."""
    def decorator(func: F) -> F:
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            logger.log(level, f"Calling {func.__name__}")
            logger.log(level, f"  args: {args}")
            logger.log(level, f"  kwargs: {kwargs}")
            result = func(*args, **kwargs)
            logger.log(level, f"  returned: {result!r}")
            return result
        return wrapper
    return decorator


# 9c. Rate limiter
def rate_limit(calls_per_second: float = 10.0):
    """Limit function calls to N per second."""
    min_interval = 1.0 / calls_per_second
    last_called = [0.0]

    def decorator(func: F) -> F:
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            elapsed = time.monotonic() - last_called[0]
            remaining = min_interval - elapsed
            if remaining > 0:
                time.sleep(remaining)
            last_called[0] = time.monotonic()
            return func(*args, **kwargs)
        return wrapper
    return decorator


# 9d. Validation decorator
def validate_types(func: F) -> F:
    """Validate argument types based on type hints."""
    sig = inspect.signature(func)
    hints = func.__annotations__

    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        bound = sig.bind(*args, **kwargs)
        bound.apply_defaults()

        for param_name, value in bound.arguments.items():
            if param_name in hints and param_name != 'return':
                expected = hints[param_name]
                if not isinstance(value, expected):
                    raise TypeError(
                        f"{param_name} must be {expected.__name__}, "
                        f"got {type(value).__name__}"
                    )
        return func(*args, **kwargs)
    return wrapper


@validate_types
def add_numbers(a: int, b: int) -> int:
    """Add two integers."""
    return a + b


# ── 10. Descriptor Interaction ────────────────────────────────────────
# Decorators interact with the descriptor protocol

class ValidatedProperty:
    """A descriptor that validates values on set."""

    def __init__(self, validator: Callable[[Any], bool], error_msg: str):
        self.validator = validator
        self.error_msg = error_msg
        self.name = None

    def __set_name__(self, owner, name):
        self.name = name

    def __get__(self, obj, objtype=None):
        if obj is None:
            return self
        return obj.__dict__.get(self.name)

    def __set__(self, obj, value):
        if not self.validator(value):
            raise ValueError(self.error_msg)
        obj.__dict__[self.name] = value


class Circle:
    """Circle with validated radius."""
    radius = ValidatedProperty(
        lambda x: isinstance(x, (int, float)) and x >= 0,
        "Radius must be a non-negative number"
    )

    def __init__(self, radius: float):
        self.radius = radius

    @property
    def area(self) -> float:
        import math
        return math.pi * self.radius ** 2


# ── 11. Async Decorators ──────────────────────────────────────────────
# Decorators work with async functions too

def async_timer(func):
    """Time async function execution."""
    @functools.wraps(func)
    async def wrapper(*args, **kwargs):
        start = time.perf_counter()
        result = await func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        print(f"{func.__name__} took {elapsed:.6f}s")
        return result
    return wrapper


# ── 12. Decorator with Class Variables ────────────────────────────────
# Decorators can access and modify class attributes

def register_plugin(cls):
    """Register a class as a plugin."""
    if not hasattr(cls, '_plugins'):
        cls._plugins = []
    cls._plugins.append(cls)
    cls.is_plugin = True
    return cls


@register_plugin
class MyPlugin:
    name = "my_plugin"
    def execute(self):
        return f"Executing {self.name}"


# ── 13. Debug Decorator ───────────────────────────────────────────────
# Print detailed debug information

def debug(func: F) -> F:
    """Print debug information for function calls."""
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        args_repr = [repr(a) for a in args]
        kwargs_repr = [f"{k}={v!r}" for k, v in kwargs.items()]
        signature = ", ".join(args_repr + kwargs_repr)
        print(f"DEBUG: {func.__name__}({signature})")
        result = func(*args, **kwargs)
        print(f"DEBUG: {func.__name__} returned {result!r}")
        return result
    return wrapper


@debug
def multiply(a: int, b: int) -> int:
    """Multiply two numbers."""
    return a * b


# ── 14. Combining Patterns ────────────────────────────────────────────
# Real-world: combine multiple decorators

@timer
@retry(max_attempts=2, delay=0.1)
def fetch_data(url: str) -> dict:
    """Fetch data from URL with retry and timing."""
    if random.random() < 0.5:
        raise ConnectionError("Simulated network error")
    return {"status": "ok", "url": url}


# ── Main ──────────────────────────────────────────────────────────────

if __name__ == "__main__":
    # Section 1: Function objects
    print("=== Function Objects ===")
    demonstrate_function_objects()
    print()

    # Section 2: Closures
    print("=== Closures ===")
    demonstrate_closure_bug()
    print()

    # Section 3: Basic decorator
    print("=== Basic Decorator ===")
    result = slow_add(1, 2)
    print(f"Result: {result}")
    print()

    # Section 4: Decorator with args
    print("=== Decorator with Arguments ===")
    for i in range(3):
        try:
            result = unreliable_function()
            print(f"Success: {result}")
        except ValueError as e:
            print(f"Failed after retries: {e}")
    print()

    # Section 5: Stacking
    print("=== Stacking Decorators ===")
    print(greet("Alice"))
    print()

    # Section 6: Class-based decorator
    print("=== Class-based Decorator ===")
    for i in range(5):
        process_item(i)
    print(f"process_item: {process_item}")
    print()

    # Section 7: Class decorator
    print("=== Class Decorator ===")
    print(Point(3, 4))
    print()

    # Section 8: Singleton
    print("=== Singleton ===")
    print(f"db1.query('SELECT *'): {db1.query('SELECT *')}")
    print(f"db1 is db2: {db1 is db2}")
    print()

    # Section 9: Production patterns
    print("=== Production Patterns ===")
    print(f"fibonacci(50) = {fibonacci(50)}")
    print(f"Cache info: {fibonacci.cache_info()}")
    print()

    # Section 10: Descriptor
    print("=== Descriptor Interaction ===")
    c = Circle(5)
    print(f"Circle(5).area = {c.area:.2f}")
    try:
        c.radius = -1
    except ValueError as e:
        print(f"Validation error: {e}")
    print()

    # Section 11: Debug
    print("=== Debug Decorator ===")
    multiply(3, 7)
    print()

    # Section 12: Combining patterns
    print("=== Combining Patterns ===")
    try:
        result = fetch_data("https://api.example.com")
        print(f"Fetched: {result}")
    except Exception as e:
        print(f"Failed: {e}")
