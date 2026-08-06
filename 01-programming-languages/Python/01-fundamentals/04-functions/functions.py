"""Function definitions, arguments, return values, and closures."""

# ── Basic Function ───────────────────────────────────────────────────
def greet(name):
    """Return a greeting string."""
    return f"Hello, {name}!"

print(greet("Alice"))

# ── Default Arguments ────────────────────────────────────────────────
def greet_with_title(name, title="Mr."):
    return f"Hello, {title} {name}"

print(greet_with_title("Alice"))          # Hello, Mr. Alice
print(greet_with_title("Alice", "Dr."))  # Hello, Dr. Alice

# ── *args and **kwargs ──────────────────────────────────────────────
def flexible(*args, **kwargs):
    """Accept any number of positional and keyword arguments."""
    print(f"args: {args}")
    print(f"kwargs: {kwargs}")

flexible(1, 2, 3, name="Alice", age=30)

# ── Keyword-Only Arguments ───────────────────────────────────────────
def create_user(name, *, email, age):
    """Arguments after * must be passed as keywords."""
    return {"name": name, "email": email, "age": age}

user = create_user("Alice", email="alice@example.com", age=30)

# ── Return Values ────────────────────────────────────────────────────
def get_stats(numbers):
    """Return multiple values (as a tuple)."""
    return min(numbers), max(numbers), sum(numbers) / len(numbers)

lo, hi, avg = get_stats([1, 2, 3, 4, 5])

# Return None implicitly if no return statement
def side_effect(x):
    print(x)

result = side_effect(42)  # result is None

# ── First-Class Functions ────────────────────────────────────────────
# Functions are objects — can be assigned, passed, returned

def apply(func, value):
    return func(value)

print(apply(str.upper, "hello"))  # HELLO
print(apply(len, [1, 2, 3]))     # 3

# ── Lambda Functions ─────────────────────────────────────────────────
# Anonymous functions — limited to single expression
square = lambda x: x ** 2
add = lambda a, b: a + b

print(square(5))  # 25
print(add(2, 3))  # 5

# ── Closures ─────────────────────────────────────────────────────────
def make_counter(start=0):
    """Inner function captures 'count' from enclosing scope."""
    count = start
    def increment():
        nonlocal count   # Modify enclosing scope variable
        count += 1
        return count
    return increment

counter = make_counter(10)
print(counter())  # 11
print(counter())  # 12

# ── Decorator Basics ─────────────────────────────────────────────────
def timer(func):
    import time
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        elapsed = time.time() - start
        print(f"{func.__name__} took {elapsed:.4f}s")
        return result
    return wrapper

@timer
def slow_function():
    import time
    time.sleep(0.1)
    return "done"

# ── Type Hints (Python 3.5+) ────────────────────────────────────────
def add_numbers(a: int, b: int) -> int:
    return a + b

from typing import Optional, Union

def find_item(items: list[str], target: str) -> Optional[str]:
    """Return item or None if not found."""
    return target if target in items else None

# ── Docstrings ───────────────────────────────────────────────────────
def documented_func(param: str) -> str:
    """
    Short one-line summary.

    Longer description if needed.

    Args:
        param: Description of param.

    Returns:
        Description of return value.

    Raises:
        ValueError: When param is invalid.
    """
    return param
