# Decorators — Senior-Level Depth

Decorators are one of Python's most powerful metaprogramming tools. They let you modify or extend function and class behavior without changing the original code. But most developers only scratch the surface. This guide takes you deep — from function objects to production-grade patterns.

---

## 1. Function Objects

Functions are first-class objects in Python. This is the foundation everything else builds on.

```python
def greet(name):
    """Return a greeting."""
    return f"Hello, {name}"

# Functions have attributes
print(greet.__name__)   # 'greet'
print(greet.__doc__)    # 'Return a greeting.'
print(greet.__module__) # '__main__'

# Functions can be passed as arguments
def apply(func, value):
    return func(value)

print(apply(greet, "Alice"))  # 'Hello, Alice'

# Functions can be returned from other functions
def create_greeting(prefix):
    def greet(name):
        return f"{prefix}, {name}"
    return greet

hello = create_greeting("Hi")
print(hello("Bob"))  # 'Hi, Bob'
```

### Why This Matters

Understanding function objects is key to decorators. A decorator is just a function that takes a function and returns a new function. If you don't grasp that functions are objects, decorators will always feel like magic.

### Common Misconception

> "Functions are just blocks of code."

No. Functions are objects with attributes, identity, and lifetime. They can be stored in data structures, passed around, and dynamically created. This is what makes decorators possible.

### Production Implication

Function objects are used everywhere — callbacks, event handlers, async continuations. Understanding them means understanding how Python's entire async/await and event-driven ecosystem works.

---

## 2. Closures

A closure captures variables from its enclosing scope. The closure keeps references to free variables even after the outer function has returned.

```python
def make_multiplier(factor):
    def multiplier(x):
        return x * factor  # 'factor' is a free variable
    return multiplier

double = make_multiplier(2)
triple = make_multiplier(3)

print(double(5))   # 10
print(triple(5))   # 15

# The closure remembers 'factor' even though make_multiplier has returned
print(double.__closure__[0].cell_contents)  # 2
```

### Why Closures Matter for Decorators

Closures are how decorators remember configuration. When you write `@retry(max_attempts=3)`, the `retry` function returns a closure that remembers `max_attempts=3` and applies it every time the decorated function is called.

### Production Implication

Closures capture variables **by reference**, not by value. This causes subtle bugs in production:

```python
# BAD: All wrappers share the same mutable reference
def bad_decorator(func):
    def wrapper(*args, **kwargs):
        print(f"Config: {config}")  # Captures reference — may be stale
        return func(*args, **kwargs)
    return wrapper

# GOOD: Capture value at definition time
def good_decorator(func):
    def wrapper(*args, **kwargs):
        captured_config = config  # Snapshot the value now
        print(f"Config: {captured_config}")
        return func(*args, **kwargs)
    return wrapper
```

### Common Misconception

> "Closures copy variables."

No. Closures capture variables by reference. The cell object holds a reference to the variable, not a copy. This is efficient but can cause bugs if the variable is mutated after the closure is created.

---

## 3. Decorator Syntax

`@decorator` is syntactic sugar for `func = decorator(func)`.

```python
# These are equivalent
@timer
def slow_function():
    pass

def slow_function():
    pass
slow_function = timer(slow_function)
```

### Multiple Decorators

Decorators are applied bottom-up:

```python
@bold
@italic
def greet(name):
    return f"Hello, {name}"

# Equivalent to:
greet = bold(italic(greet))
# italic is applied first, then bold
# Result: <b><i>Hello, Alice</i></b>
```

### Decorators with Arguments

When a decorator takes arguments, you need an extra layer of nesting:

```python
@retry(max_attempts=3, delay=1)
def unreliable():
    pass

# Equivalent to:
unreliable = retry(max_attempts=3, delay=1)(unreliable)
# retry(max_attempts=3, delay=1) returns a decorator
# That decorator is called with 'unreliable' as argument
```

### Why This Matters

Understanding the sugar helps you debug. When something goes wrong, desugar the `@` syntax and you'll see exactly what's happening.

### Common Misconception

> "Decorators are a special language feature."

They're not. They're just function calls. The `@` syntax is syntactic sugar that makes the code cleaner. There's no special magic — just functions calling functions.

### Production Implication

When you see `@app.route("/api/users")`, it's just `app.route("/api/users")(view_function)`. The route method returns a decorator, which is applied to the view. This is how Flask, FastAPI, and Django work internally.

---

## 4. How Decorators Work Internally

The execution flow:

```python
import functools

def timer(func):
    @functools.wraps(func)
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
```

Step by step:

1. Python encounters `def timer(func)` — `timer` is defined
2. Python encounters `@timer` on `slow_add`
3. Python calls `timer(slow_add)` — the original `slow_add` is passed as `func`
4. Inside `timer`, `wrapper` is defined (a closure over `func`)
5. `timer` returns `wrapper`
6. The name `slow_add` now points to `wrapper`
7. When you call `slow_add(1, 2)`, you're actually calling `wrapper(1, 2)`

```python
# You can verify this:
print(slow_add.__name__)      # 'slow_add' (thanks to @wraps)
print(slow_add.__wrapped__)   # <function slow_add at 0x...> (the original)
```

### Why Understanding This Prevents Mistakes

Many bugs come from misunderstanding this flow:

- Forgetting to return `wrapper` → decorated function becomes `None`
- Not using `*args, **kwargs` → wrapper can't handle all signatures
- Not using `@wraps` → metadata is lost, debugging becomes painful

### Common Misconception

> "Decorators change the function itself."

No. Decorators create a new function that wraps the original. The original function still exists (accessible via `__wrapped__`), but the name now points to the wrapper.

### Production Implication

Understanding the internal flow is critical for debugging. When you see `AttributeError: 'NoneType' object has no attribute 'something'`, check if your decorator forgot to return the wrapper.

---

## 5. functools.wraps

Without `@wraps`, decorated functions lose their metadata:

```python
# WITHOUT @wraps
def bad_decorator(func):
    def wrapper(*args, **kwargs):
        return func(*args, **kwargs)
    return wrapper

@bad_decorator
def greet(name):
    """Return a greeting."""
    return f"Hello, {name}"

print(greet.__name__)  # 'wrapper' — WRONG
print(greet.__doc__)   # None — WRONG
```

```python
# WITH @wraps
import functools

def good_decorator(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        return func(*args, **kwargs)
    return wrapper

@good_decorator
def greet(name):
    """Return a greeting."""
    return f"Hello, {name}"

print(greet.__name__)  # 'greet' — CORRECT
print(greet.__doc__)   # 'Return a greeting.' — CORRECT
```

### How @wraps Works Under the Hood

`functools.wraps(func)` calls `functools.update_wrapper(wrapper, func)`. It copies these attributes from `func` to `wrapper`:

- `__module__`
- `__name__`
- `__qualname__`
- `__doc__`
- `__dict__`
- `__wrapped__`

It also adds `__wrapped__` to `wrapper`, pointing to the original function. This lets you "unwrap" the decorator:

```python
# Access the original function
original = greet.__wrapped__
print(original.__name__)  # 'greet'
```

### Why This Matters in Production

- **Debugging**: Stack traces show the real function name, not "wrapper"
- **Documentation**: `help()` and Sphinx show correct docstrings
- **Introspection**: Tools like `inspect.signature()` work correctly
- **Serialization**: Some serializers use `__name__` and `__module__`

### Common Misconception

> "@wraps is optional — it's just for convenience."

No. It's essential. Without it, your debugging tools, documentation generators, and introspection tools all break. It's not convenience — it's correctness.

---

## 6. Descriptor Interaction

Decorators interact with Python's descriptor protocol. When you access a decorated method on an instance, `__get__` is called.

```python
import functools

def log_calls(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        print(f"Calling {func.__name__}")
        return func(*args, **kwargs)
    return wrapper

class MyClass:
    @log_calls
    def method(self):
        return "executed"

obj = MyClass()
obj.method()  # Works fine
# The descriptor protocol ensures 'method' is correctly bound
# when accessed on an instance
```

### Why Descriptors Matter

- `@property` works because `property` is a descriptor
- `@classmethod` and `@staticmethod` work because they're descriptors
- Understanding descriptors explains why decorated methods work on instances but not on classes (in some cases)

### Production Implication

If your decorator breaks `self` binding, methods won't work on instances. Understanding the descriptor protocol helps you write decorators that work correctly with methods, classmethods, and staticmethods.

### Common Misconception

> "Decorators and descriptors are unrelated."

No. Decorators that return functions are implicitly creating descriptors. When you access a decorated function on an instance, Python's descriptor protocol kicks in.

---

## 7. Class-based Decorators

Class decorators use `__call__` to make instances callable:

```python
import functools

class CountCalls:
    """Decorator that counts how many times a function is called."""
    
    def __init__(self, func):
        functools.update_wrapper(self, func)
        self.func = func
        self.call_count = 0
    
    def __call__(self, *args, **kwargs):
        self.call_count += 1
        print(f"Call {self.call_count} to {self.func.__name__}")
        return self.func(*args, **kwargs)
    
    def reset(self):
        self.call_count = 0

@CountCalls
def say_hello():
    print("Hello!")

say_hello()  # Call 1 to say_hello
say_hello()  # Call 2 to say_hello
say_hello.reset()
```

### When to Use Class vs Function Decorators

**Use function decorators when:**
- The decorator is simple and stateless
- You don't need to maintain state between calls
- Performance matters (function call overhead is slightly less)

**Use class decorators when:**
- You need to maintain state between calls
- You need multiple methods on the decorator
- The logic is complex and benefits from being organized into methods

### Class Decorators That Return Classes

```python
def add_repr(cls):
    """Class decorator that adds __repr__ based on __init__."""
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
```

### Common Misconception

> "Class decorators are always more powerful than function decorators."

No. They're different tools for different problems. Class decorators shine when you need state; function decorators are simpler for stateless transformations.

---

## 8. Common Patterns

### Logging

```python
import functools
import logging

logger = logging.getLogger(__name__)

def log_execution(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        logger.info(f"Calling {func.__name__} with args={args}, kwargs={kwargs}")
        result = func(*args, **kwargs)
        logger.info(f"{func.__name__} returned {result!r}")
        return result
    return wrapper
```

### Timing

```python
import functools
import time

def timer(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        start = time.perf_counter()
        result = func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        print(f"{func.__name__} took {elapsed:.6f}s")
        return result
    return wrapper
```

### Retry with Backoff

```python
import functools
import time
import random

def retry(max_attempts=3, base_delay=1.0, max_delay=30.0):
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(1, max_attempts + 1):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_attempts:
                        raise
                    delay = min(base_delay * (2 ** (attempt - 1)), max_delay)
                    delay *= random.uniform(0.5, 1.5)  # Jitter
                    print(f"Attempt {attempt} failed: {e}. Retrying in {delay:.1f}s...")
                    time.sleep(delay)
        return wrapper
    return decorator
```

### Memoization

```python
import functools

def memoize(func):
    cache = {}
    @functools.wraps(func)
    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]
    wrapper.cache = cache
    wrapper.cache_clear = cache.clear
    return wrapper

@memoize
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)
```

### Rate Limiting

```python
import functools
import time

def rate_limit(calls_per_second=10):
    min_interval = 1.0 / calls_per_second
    last_called = [0.0]
    
    def decorator(func):
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
```

### Why These Patterns Matter

These patterns appear everywhere in production:

- **Logging**: Essential for observability
- **Timing**: Performance monitoring and SLA compliance
- **Retry**: Resilience against transient failures
- **Memoization**: Expensive computation optimization
- **Rate limiting**: API compliance and resource protection

### Production Implication

Don't roll your own for production use. Use battle-tested libraries:

- `tenacity` for retry
- `functools.lru_cache` for memoization
- `ratelimit` for rate limiting
- Structured logging libraries for logging

---

## 9. Debugging Decorators

### functools.wraps Helps Debugging

```python
import functools

def debug(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        print(f"DEBUG: {func.__name__} called with {args}, {kwargs}")
        result = func(*args, **kwargs)
        print(f"DEBUG: {func.__name__} returned {result!r}")
        return result
    return wrapper

@debug
def add(a, b):
    return a + b

add(1, 2)
# DEBUG: add called with (1, 2), {}
# DEBUG: add returned 3
```

### inspect Module for Introspection

```python
import inspect
import functools

def log_signature(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        sig = inspect.signature(func)
        bound = sig.bind(*args, **kwargs)
        bound.apply_defaults()
        print(f"Arguments: {bound.arguments}")
        return func(*args, **kwargs)
    return wrapper

@log_signature
def complex_func(a, b=10, *args, **kwargs):
    pass

complex_func(1, 2, 3, key="value")
# Arguments: {'a': 1, 'b': 2, 'args': (3,), 'kwargs': {'key': 'value'}}
```

### Common Mistakes and Fixes

```python
# MISTAKE 1: Forgetting to return wrapper
def bad_decorator(func):
    def wrapper(*args, **kwargs):
        return func(*args, **kwargs)
    # FORGOT: return wrapper  →  decorated function becomes None

# FIX:
def good_decorator(func):
    def wrapper(*args, **kwargs):
        return func(*args, **kwargs)
    return wrapper  # Always return wrapper


# MISTAKE 2: Not using *args, **kwargs
def bad_decorator(func):
    @functools.wraps(func)
    def wrapper(a, b):  # Only works for 2-arg functions
        return func(a, b)
    return wrapper

# FIX:
def good_decorator(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):  # Works for any signature
        return func(*args, **kwargs)
    return wrapper


# MISTAKE 3: Mutable default in closure
def bad_decorator(value=[]):
    def wrapper(*args, **kwargs):
        value.append(1)  # Mutates shared default!
        return func(*args, **kwargs)
    return wrapper

# FIX:
def good_decorator(value=None):
    if value is None:
        value = []
    def wrapper(*args, **kwargs):
        value.append(1)  # Safe — each call gets its own list
        return func(*args, **kwargs)
    return wrapper
```

### Why Good Debugging Skills Matter

A single missing `return wrapper` can cost hours of debugging. Knowing these patterns saves time:

- **Stack traces**: `@wraps` ensures meaningful names
- **IDE support**: `__wrapped__` lets IDEs resolve the original function
- **Testing**: `inspect.signature()` verifies decorator compatibility
- **Profiling**: Profilers need correct function names

### Common Misconception

> "Debugging decorators is hard."

It's not — if you understand the basics. The most common issues are:

1. Missing `return wrapper` → function becomes `None`
2. Missing `@wraps` → metadata lost
3. Missing `*args, **kwargs` → signature mismatch
4. Mutable closure state → shared bugs

---

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Modify or extend function/class behavior |
| Foundation | Functions are first-class objects |
| Key Mechanism | Closures capture enclosing scope |
| Syntax | `@decorator` is sugar for `func = decorator(func)` |
| Metadata | Always use `@functools.wraps` |
| Internal Flow | decorator(func) → wrapper replaces func |
| Stateful Decorators | Use classes with `__call__` |
| Common Patterns | Logging, timing, retry, memoization, rate limiting |
| Debugging | `@wraps`, `inspect` module, `__wrapped__` |
| Thread Safety | Decorator logic is usually stateless; closures are not |
| Performance | Adds call overhead; avoid in hot paths |
| Best Alternative | Context managers for resource management |
| When to Use | Cross-cutting concerns, DRY principle |
| When to Avoid | Overuse makes code harder to debug |
| Production | Use battle-tested libraries (tenacity, lru_cache) |

---

## Interview Questions

1. How does `@functools.wraps` work internally?
2. Write a decorator that logs function arguments and return values.
3. What is the difference between a decorator and a context manager?
4. How would you create a thread-safe caching decorator?
5. Explain the descriptor protocol and how it relates to decorators.
6. What are the performance implications of using decorators?
7. How would you write a decorator that works on both functions and methods?

---

## Production Checklist

- ☐ I know the time/space complexity of my decorators
- ☐ I know thread safety guarantees
- ☐ I know memory impact (closures hold references)
- ☐ I know common mistakes (missing return, missing wraps)
- ☐ I know alternatives (context managers, metaclasses)
- ☐ I know limitations (signature changes, stack depth)
- ☐ I know how to debug (inspect, __wrapped__, stack traces)

---

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic `@decorator` syntax
- Can write simple decorators

### Level 2: Understands
- Knows time/space complexity
- Understands closure capture semantics
- Uses `@functools.wraps` consistently

### Level 3: Deep Knowledge
- Understands descriptor protocol interaction
- Can write class-based decorators
- Knows when to use function vs class decorators

### Level 4: Expert
- Can write production-grade decorators (retry, rate limiting)
- Understands thread safety implications
- Can optimize decorator overhead

### Level 5: Master
- Can debug decorator issues in production
- Can design custom decorator frameworks
- Understands interaction with async/await, generators, and context managers
