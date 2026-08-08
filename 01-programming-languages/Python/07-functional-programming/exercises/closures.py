"""
Module 07 - Functional Programming: Closures Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Closures (Difficulty: Beginner)
# =============================================================================
# Create and use closures.

# TODO: Implement make_multiplier
def make_multiplier(factor):
    """Return a function that multiplies by factor."""
    pass

# TODO: Implement make_adder
def make_adder(value):
    """Return a function that adds value."""
    pass

# Test cases
# double = make_multiplier(2)
# triple = make_multiplier(3)
# print(double(5))    # Expected: 10
# print(triple(5))    # Expected: 15
#
# add5 = make_adder(5)
# add10 = make_adder(10)
# print(add5(3))      # Expected: 8
# print(add10(3))     # Expected: 13


# =============================================================================
# Exercise 2: Closure for State (Difficulty: Intermediate)
# =============================================================================
# Use closures to maintain state.

# TODO: Implement counter
def make_counter(start=0):
    """Return a counter function."""
    pass

# TODO: Implement accumulator
def make_accumulator(initial=0):
    """Return an accumulator function."""
    pass

# Test cases
# counter = make_counter()
# print(counter())    # Expected: 0
# print(counter())    # Expected: 1
# print(counter())    # Expected: 2
#
# acc = make_accumulator(100)
# print(acc(10))      # Expected: 110
# print(acc(20))      # Expected: 130


# =============================================================================
# Exercise 3: Closure as Decorator (Difficulty: Intermediate)
# =============================================================================
# Implement decorators using closures.

# TODO: Implement call counter decorator
def count_calls(func):
    """Decorator that counts function calls."""
    pass

# TODO: Implement memoization decorator
def memoize(func):
    """Decorator that caches function results."""
    pass

# Test cases
# @count_calls
# def say_hello(name):
#     return f"Hello, {name}!"
#
# print(say_hello("Alice"))  # Expected: "Hello, Alice!"
# print(say_hello("Bob"))    # Expected: "Hello, Bob!"
# print(say_hello.call_count)  # Expected: 2
#
# @memoize
# def expensive_calc(n):
#     print(f"Computing {n}...")
#     return n * n
#
# print(expensive_calc(5))  # Prints "Computing 5..." then 25
# print(expensive_calc(5))  # Returns 25 without printing


# =============================================================================
# Exercise 4: Closure with Encapsulation (Difficulty: Advanced)
# =============================================================================
# Use closures for data encapsulation.

# TODO: Implement bank account
def make_account(balance=0):
    """Return functions for account operations."""
    pass

# TODO: Implement configuration
def make_config(defaults=None):
    """Return functions for configuration management."""
    pass

# Test cases
# deposit, withdraw, get_balance = make_account(1000)
# deposit(500)
# print(get_balance())    # Expected: 1500
# withdraw(200)
# print(get_balance())    # Expected: 1300
#
# get, set_config, reset = make_config({"theme": "dark", "lang": "en"})
# print(get("theme"))     # Expected: "dark"
# set_config("theme", "light")
# print(get("theme"))     # Expected: "light"
# reset()
# print(get("theme"))     # Expected: "dark"


# =============================================================================
# Exercise 5: Functional Patterns with Closures (Difficulty: Advanced)
# =============================================================================
# Implement functional patterns using closures.

# TODO: Implement pipeline
def make_pipeline(*functions):
    """Create a pipeline of functions."""
    pass

# TODO: Implement conditional execution
def when(condition, func):
    """Return function that applies func if condition is true."""
    pass

# Test cases
# pipeline = make_pipeline(
#     lambda x: x + 1,
#     lambda x: x * 2,
#     lambda x: x - 3
# )
# print(pipeline(5))  # Expected: 9 (5+1=6, 6*2=12, 12-3=9)
#
# is_positive = when(lambda x: x > 0, lambda x: x * 2)
# print(is_positive(5))    # Expected: 10
# print(is_positive(-5))   # Expected: -5 (not doubled)
