"""
Module 03: Advanced - Decorators Solutions
Practice decorator implementation in Python.
"""

import time
import functools
from collections import defaultdict


def timer(func):
    """Decorator that measures and prints function execution time."""
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"{func.__name__} took {(end - start) * 1000:.2f}ms")
        return result
    return wrapper


def timer_with_units(unit="ms"):
    """Decorator factory that measures execution time with configurable units."""
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            start = time.time()
            result = func(*args, **kwargs)
            end = time.time()
            elapsed = end - start
            if unit == "ms":
                elapsed *= 1000
            print(f"{func.__name__} took {elapsed:.2f}{unit}")
            return result
        return wrapper
    return decorator


def memoize(func):
    """Decorator that caches function results."""
    cache = {}

    @functools.wraps(func)
    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]

    wrapper.clear_cache = cache.clear
    return wrapper


def memoize_with_limit(max_size=128):
    """Decorator factory that caches with a maximum size limit (LRU)."""
    def decorator(func):
        cache = {}
        order = []

        @functools.wraps(func)
        def wrapper(*args):
            if args in cache:
                order.remove(args)
                order.append(args)
                return cache[args]

            if len(order) >= max_size:
                oldest = order.pop(0)
                del cache[oldest]

            result = func(*args)
            cache[args] = result
            order.append(args)
            return result

        wrapper.clear_cache = lambda: (cache.clear(), order.clear())
        return wrapper
    return decorator


def rate_limit(max_calls, period):
    """Decorator that limits function calls."""
    def decorator(func):
        calls = []

        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            now = time.time()
            calls.append(now)

            # Remove old timestamps
            while calls and calls[0] < now - period:
                calls.pop(0)

            if len(calls) > max_calls:
                raise RuntimeError(f"Rate limit exceeded: {max_calls} calls per {period}s")

            return func(*args, **kwargs)
        return wrapper
    return decorator


def validate_return_type(expected_type):
    """Decorator that validates function return type."""
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            result = func(*args, **kwargs)
            if not isinstance(result, expected_type):
                raise TypeError(
                    f"Expected return type {expected_type.__name__}, "
                    f"got {type(result).__name__}"
                )
            return result
        return wrapper
    return decorator


def validate_input_types(*types):
    """Decorator that validates input argument types."""
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            for i, (arg, expected) in enumerate(zip(args, types)):
                if not isinstance(arg, expected):
                    raise TypeError(
                        f"Argument {i} must be {expected.__name__}, "
                        f"got {type(arg).__name__}"
                    )
            return func(*args, **kwargs)
        return wrapper
    return decorator


def add_repr(cls):
    """Class decorator that adds __repr__ method if not present."""
    if '__repr__' not in cls.__dict__:
        def __repr__(self):
            attrs = ', '.join(f"{k}={v!r}" for k, v in self.__dict__.items())
            return f"{cls.__name__}({attrs})"
        cls.__repr__ = __repr__
    return cls


def singleton(cls):
    """Class decorator that implements Singleton pattern."""
    instances = {}

    @functools.wraps(cls)
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return get_instance


def auto_log_methods(cls):
    """Class decorator that adds logging to all methods."""
    for attr_name, attr_value in cls.__dict__.items():
        if callable(attr_value) and not attr_name.startswith('__'):
            def make_logged(func, name):
                def logged_method(self, *args, **kwargs):
                    print(f"Calling {name}")
                    return func(self, *args, **kwargs)
                return logged_method
            setattr(cls, attr_name, make_logged(attr_value, attr_name))
    return cls


if __name__ == "__main__":
    print("Testing Decorators Solutions...")

    # Test timer
    @timer
    def slow_function():
        time.sleep(0.01)
        return "done"
    result = slow_function()
    assert result == "done"

    # Test memoize
    call_count = [0]

    @memoize
    def expensive_function(n):
        call_count[0] += 1
        return n * n

    assert expensive_function(5) == 25
    assert expensive_function(5) == 25
    assert call_count[0] == 1

    # Test rate_limit
    @rate_limit(max_calls=3, period=1)
    def limited_function():
        return "called"

    assert limited_function() == "called"
    assert limited_function() == "called"
    assert limited_function() == "called"
    try:
        limited_function()
        assert False, "Should have raised RuntimeError"
    except RuntimeError:
        pass

    # Test validate_return_type
    @validate_return_type(int)
    def add_numbers(a, b):
        return a + b

    assert add_numbers(1, 2) == 3

    @validate_return_type(int)
    def wrong_return():
        return "not an int"

    try:
        wrong_return()
        assert False, "Should have raised TypeError"
    except TypeError:
        pass

    # Test class decorators
    @add_repr
    class Point:
        def __init__(self, x, y):
            self.x = x
            self.y = y

    p = Point(1, 2)
    assert "Point" in repr(p)
    assert "x=1" in repr(p)

    @singleton
    class Database:
        pass

    db1 = Database()
    db2 = Database()
    assert db1 is db2

    print("All Decorators solutions passed!")
