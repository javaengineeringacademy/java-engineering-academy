"""
Module 03 - Advanced: Decorators Exercises
Difficulty: Intermediate to Advanced
"""

# =============================================================================
# Exercise 1: Function Decorators (Difficulty: Intermediate)
# =============================================================================
# Create a decorator that measures execution time.

import time
from functools import wraps

# TODO: Implement the timer decorator
def timer(func):
    """Decorator that measures and prints execution time."""
    @wraps(func)
    def wrapper(*args, **kwargs):
        pass
    return wrapper

# Test cases
# @timer
# def slow_function(n):
#     """Simulate slow function."""
#     total = 0
#     for i in range(n):
#         total += i * i
#     return total
#
# result = slow_function(1000000)
# print(f"Result: {result}")
# # Expected: Prints execution time


# =============================================================================
# Exercise 2: Class Decorators (Difficulty: Intermediate)
# =============================================================================
# Create a class decorator that adds functionality.

# TODO: Implement the singleton decorator
def singleton(cls):
    """Decorator that ensures only one instance of a class exists."""
    pass

# TODO: Implement the cached class
def cached(cls):
    """Decorator that caches method results."""
    pass

# Test cases
# @singleton
# class Database:
#     def __init__(self):
#         self.connection = "Connected"
#
# db1 = Database()
# db2 = Database()
# print(db1 is db2)  # Expected: True (same instance)
#
# @cached
# class Calculator:
#     def expensive_calculation(self, n):
#         print(f"Computing for {n}...")
#         return n * n
#
# calc = Calculator()
# print(calc.expensive_calculation(5))  # Prints "Computing for 5..." then 25
# print(calc.expensive_calculation(5))  # Returns 25 without computing


# =============================================================================
# Exercise 3: Decorators with Arguments (Difficulty: Advanced)
# =============================================================================
# Create decorators that accept arguments.

# TODO: Implement the retry decorator
def retry(max_attempts=3, delay=1):
    """Decorator that retries function on failure."""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            pass
        return wrapper
    return decorator

# TODO: Implement the rate_limit decorator
def rate_limit(calls_per_second=1):
    """Decorator that limits function calls."""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            pass
        return wrapper
    return decorator

# Test cases
# @retry(max_attempts=3, delay=0.1)
# def unreliable_function():
#     import random
#     if random.random() < 0.7:
#         raise ValueError("Random failure")
#     return "Success!"
#
# try:
#     result = unreliable_function()
#     print(result)
# except ValueError as e:
#     print(f"Failed after retries: {e}")


# =============================================================================
# Exercise 4: Decorator Stacking (Difficulty: Intermediate)
# =============================================================================
# Stack multiple decorators.

# TODO: Implement decorators
def log_calls(func):
    """Decorator that logs function calls."""
    @wraps(func)
    def wrapper(*args, **kwargs):
        pass
    return wrapper

def validate_types(*types):
    """Decorator that validates argument types."""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            pass
        return wrapper
    return decorator

def cache_result(func):
    """Decorator that caches results."""
    cache = {}
    @wraps(func)
    def wrapper(*args):
        pass
    return wrapper

# Test cases
# @log_calls
# @validate_types(int, int)
# @cache_result
# def add(a, b):
#     return a + b
#
# print(add(2, 3))  # Expected: Logs call, validates types, returns 5
# print(add(2, 3))  # Expected: Returns cached result


# =============================================================================
# Exercise 5: Decorator Pattern (Difficulty: Advanced)
# =============================================================================
# Implement the decorator pattern for real-world use.

# TODO: Implement the authorization decorator
def require_permission(permission):
    """Decorator that checks user permissions."""
    def decorator(func):
        @wraps(func)
        def wrapper(user, *args, **kwargs):
            pass
        return wrapper
    return decorator

# TODO: Implement the audit decorator
def audit(action):
    """Decorator that logs actions for audit trail."""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            pass
        return wrapper
    return decorator

# Test cases
# class User:
#     def __init__(self, name, permissions):
#         self.name = name
#         self.permissions = permissions
#
# @require_permission("admin")
# @audit("delete_user")
# def delete_user(current_user, target_user):
#     return f"Deleted {target_user}"
#
# admin = User("Alice", ["admin", "read", "write"])
# regular = User("Bob", ["read"])
#
# print(delete_user(admin, "Charlie"))  # Expected: "Deleted Charlie"
# try:
#     print(delete_user(regular, "Charlie"))  # Expected: PermissionError
# except PermissionError as e:
#     print(e)
