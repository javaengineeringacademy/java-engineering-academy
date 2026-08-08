"""
Module 03 - Advanced: Decorators Solutions
Difficulty: Intermediate to Advanced
"""

# =============================================================================
# Exercise 1: Function Decorators - Solution
# =============================================================================
import time
from functools import wraps

def timer(func):
    """Decorator that measures and prints execution time."""
    @wraps(func)
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"{func.__name__} took {end - start:.4f} seconds")
        return result
    return wrapper

@timer
def slow_function(n):
    """Simulate slow function."""
    total = 0
    for i in range(n):
        total += i * i
    return total

result = slow_function(1000000)
print(f"Result: {result}")


# =============================================================================
# Exercise 2: Class Decorators - Solution
# =============================================================================
def singleton(cls):
    """Decorator that ensures only one instance of a class exists."""
    instances = {}
    @wraps(cls)
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return get_instance

def cached(cls):
    """Decorator that caches method results."""
    cache = {}
    original_init = cls.__init__

    def new_init(self, *args, **kwargs):
        original_init(self, *args, **kwargs)
        self._cache = {}

    cls.__init__ = new_init

    for attr_name in dir(cls):
        if not attr_name.startswith('_'):
            attr = getattr(cls, attr_name)
            if callable(attr):
                def make_cached(method):
                    @wraps(method)
                    def cached_method(self, *args):
                        key = (method.__name__, args)
                        if key not in self._cache:
                            self._cache[key] = method(self, *args)
                        return self._cache[key]
                    return cached_method
                setattr(cls, attr_name, make_cached(attr))

    return cls

@singleton
class Database:
    def __init__(self):
        self.connection = "Connected"

db1 = Database()
db2 = Database()
print(db1 is db2)  # True

@cached
class Calculator:
    def expensive_calculation(self, n):
        print(f"Computing for {n}...")
        return n * n

calc = Calculator()
print(calc.expensive_calculation(5))  # Prints "Computing for 5..." then 25
print(calc.expensive_calculation(5))  # Returns 25 without computing


# =============================================================================
# Exercise 3: Decorators with Arguments - Solution
# =============================================================================
def retry(max_attempts=3, delay=1):
    """Decorator that retries function on failure."""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            import time
            last_exception = None
            for attempt in range(max_attempts):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    last_exception = e
                    if attempt < max_attempts - 1:
                        time.sleep(delay)
            raise last_exception
        return wrapper
    return decorator

def rate_limit(calls_per_second=1):
    """Decorator that limits function calls."""
    min_interval = 1.0 / calls_per_second
    last_called = [0.0]

    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            import time
            elapsed = time.time() - last_called[0]
            if elapsed < min_interval:
                time.sleep(min_interval - elapsed)
            last_called[0] = time.time()
            return func(*args, **kwargs)
        return wrapper
    return decorator

@retry(max_attempts=3, delay=0.1)
def unreliable_function():
    import random
    if random.random() < 0.7:
        raise ValueError("Random failure")
    return "Success!"

try:
    result = unreliable_function()
    print(result)
except ValueError as e:
    print(f"Failed after retries: {e}")


# =============================================================================
# Exercise 4: Decorator Stacking - Solution
# =============================================================================
def log_calls(func):
    """Decorator that logs function calls."""
    @wraps(func)
    def wrapper(*args, **kwargs):
        print(f"Calling {func.__name__} with args={args}, kwargs={kwargs}")
        result = func(*args, **kwargs)
        print(f"{func.__name__} returned {result}")
        return result
    return wrapper

def validate_types(*types):
    """Decorator that validates argument types."""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            for i, (arg, expected_type) in enumerate(zip(args, types)):
                if not isinstance(arg, expected_type):
                    raise TypeError(f"Argument {i} must be {expected_type.__name__}")
            return func(*args, **kwargs)
        return wrapper
    return decorator

def cache_result(func):
    """Decorator that caches results."""
    cache = {}
    @wraps(func)
    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]
    return wrapper

@log_calls
@validate_types(int, int)
@cache_result
def add(a, b):
    return a + b

print(add(2, 3))  # Logs call, validates types, returns 5
print(add(2, 3))  # Returns cached result


# =============================================================================
# Exercise 5: Decorator Pattern - Solution
# =============================================================================
def require_permission(permission):
    """Decorator that checks user permissions."""
    def decorator(func):
        @wraps(func)
        def wrapper(user, *args, **kwargs):
            if permission not in user.permissions:
                raise PermissionError(f"User {user.name} lacks '{permission}' permission")
            return func(user, *args, **kwargs)
        return wrapper
    return decorator

def audit(action):
    """Decorator that logs actions for audit trail."""
    audit_log = []

    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            import datetime
            entry = {
                "action": action,
                "timestamp": datetime.datetime.now().isoformat(),
                "function": func.__name__
            }
            audit_log.append(entry)
            print(f"AUDIT: {action} at {entry['timestamp']}")
            return func(*args, **kwargs)
        wrapper.audit_log = audit_log
        return wrapper
    return decorator

class User:
    def __init__(self, name, permissions):
        self.name = name
        self.permissions = permissions

@require_permission("admin")
@audit("delete_user")
def delete_user(current_user, target_user):
    return f"Deleted {target_user}"

admin = User("Alice", ["admin", "read", "write"])
regular = User("Bob", ["read"])

print(delete_user(admin, "Charlie"))  # "Deleted Charlie"
try:
    print(delete_user(regular, "Charlie"))
except PermissionError as e:
    print(e)  # "User Bob lacks 'admin' permission"
