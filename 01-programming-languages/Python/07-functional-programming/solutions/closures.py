"""
Module 07 - Functional Programming: Closures Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Closures - Solution
# =============================================================================
def make_multiplier(factor):
    """Return a function that multiplies by factor."""
    def multiplier(x):
        return x * factor
    return multiplier

def make_adder(value):
    """Return a function that adds value."""
    def adder(x):
        return x + value
    return adder

double = make_multiplier(2)
triple = make_multiplier(3)
print(double(5))    # 10
print(triple(5))    # 15

add5 = make_adder(5)
add10 = make_adder(10)
print(add5(3))      # 8
print(add10(3))     # 13


# =============================================================================
# Exercise 2: Closure for State - Solution
# =============================================================================
def make_counter(start=0):
    """Return a counter function."""
    count = [start]
    def counter():
        result = count[0]
        count[0] += 1
        return result
    return counter

def make_accumulator(initial=0):
    """Return an accumulator function."""
    total = [initial]
    def accumulator(value):
        total[0] += value
        return total[0]
    return accumulator

counter = make_counter()
print(counter())    # 0
print(counter())    # 1
print(counter())    # 2

acc = make_accumulator(100)
print(acc(10))      # 110
print(acc(20))      # 130


# =============================================================================
# Exercise 3: Closure as Decorator - Solution
# =============================================================================
def count_calls(func):
    """Decorator that counts function calls."""
    def wrapper(*args, **kwargs):
        wrapper.call_count += 1
        return func(*args, **kwargs)
    wrapper.call_count = 0
    return wrapper

def memoize(func):
    """Decorator that caches function results."""
    cache = {}
    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]
    return wrapper

@count_calls
def say_hello(name):
    return f"Hello, {name}!"

print(say_hello("Alice"))  # "Hello, Alice!"
print(say_hello("Bob"))    # "Hello, Bob!"
print(say_hello.call_count)  # 2

@memoize
def expensive_calc(n):
    print(f"Computing {n}...")
    return n * n

print(expensive_calc(5))  # Prints "Computing 5..." then 25
print(expensive_calc(5))  # Returns 25 without printing


# =============================================================================
# Exercise 4: Closure with Encapsulation - Solution
# =============================================================================
def make_account(balance=0):
    """Return functions for account operations."""
    def deposit(amount):
        nonlocal balance
        balance += amount
        return balance

    def withdraw(amount):
        nonlocal balance
        if amount > balance:
            raise ValueError("Insufficient funds")
        balance -= amount
        return balance

    def get_balance():
        return balance

    return deposit, withdraw, get_balance

def make_config(defaults=None):
    """Return functions for configuration management."""
    config = defaults.copy() if defaults else {}
    original = defaults.copy() if defaults else {}

    def get(key):
        return config.get(key)

    def set_config(key, value):
        config[key] = value

    def reset():
        config.update(original)

    return get, set_config, reset

deposit, withdraw, get_balance = make_account(1000)
deposit(500)
print(get_balance())    # 1500
withdraw(200)
print(get_balance())    # 1300

get, set_config, reset = make_config({"theme": "dark", "lang": "en"})
print(get("theme"))     # "dark"
set_config("theme", "light")
print(get("theme"))     # "light"
reset()
print(get("theme"))     # "dark"


# =============================================================================
# Exercise 5: Functional Patterns with Closures - Solution
# =============================================================================
def make_pipeline(*functions):
    """Create a pipeline of functions."""
    def pipeline(x):
        result = x
        for func in functions:
            result = func(result)
        return result
    return pipeline

def when(condition, func):
    """Return function that applies func if condition is true."""
    def wrapper(x):
        if condition(x):
            return func(x)
        return x
    return wrapper

pipeline = make_pipeline(
    lambda x: x + 1,
    lambda x: x * 2,
    lambda x: x - 3
)
print(pipeline(5))  # 9 (5+1=6, 6*2=12, 12-3=9)

is_positive = when(lambda x: x > 0, lambda x: x * 2)
print(is_positive(5))    # 10
print(is_positive(-5))   # -5 (not doubled)
