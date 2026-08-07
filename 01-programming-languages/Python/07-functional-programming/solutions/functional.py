"""
Module 07: Functional Programming - Solutions
Practice functional programming concepts in Python.
"""

from functools import reduce
import time


# =============================================================================
# Exercise 1: Lambda Functions (⭐)
# =============================================================================

# Lambda that adds two numbers
add = lambda a, b: a + b

# Lambda that checks if a number is positive
is_positive = lambda x: x > 0

# Lambda that returns the absolute value
absolute = lambda x: abs(x)

# Lambda that returns the larger of two numbers
max_of_two = lambda a, b: a if a > b else b


# =============================================================================
# Exercise 2: Map and Filter (⭐⭐)
# =============================================================================

def double_all(numbers):
    """Double each number in the list using map."""
    return list(map(lambda x: x * 2, numbers))


def get_evens(numbers):
    """Filter and return only even numbers."""
    return list(filter(lambda x: x % 2 == 0, numbers))


def get_long_words(words, min_length):
    """Filter words longer than min_length."""
    return list(filter(lambda w: len(w) > min_length, words))


def square_and_filter(numbers):
    """Square all numbers, then filter out those less than 10."""
    squared = map(lambda x: x ** 2, numbers)
    return list(filter(lambda x: x >= 10, squared))


# =============================================================================
# Exercise 3: Reduce and Accumulation (⭐⭐)
# =============================================================================

def product_of_all(numbers):
    """Calculate the product of all numbers using reduce."""
    return reduce(lambda a, b: a * b, numbers)


def flatten_list(nested_list):
    """Flatten a nested list using reduce."""
    return reduce(lambda a, b: a + b, nested_list)


def group_by_length(words):
    """Group words by their length using reduce."""
    def grouper(acc, word):
        length = len(word)
        if length not in acc:
            acc[length] = []
        acc[length].append(word)
        return acc

    return reduce(grouper, words, {})


# =============================================================================
# Exercise 4: Closures (⭐⭐⭐)
# =============================================================================

def create_counter(start=0):
    """Create a counter function that increments from start."""
    count = [start]

    def counter():
        count[0] += 1
        return count[0]

    return counter


def create_multiplier(factor):
    """Create a function that multiplies by factor."""
    def multiplier(x):
        return x * factor

    return multiplier


def create_accumulator(initial=0):
    """Create an accumulator function."""
    total = [initial]

    def accumulator(value):
        total[0] += value
        return total[0]

    return accumulator


# =============================================================================
# Exercise 5: Decorators (⭐⭐⭐⭐)
# =============================================================================

def timer_decorator(func):
    """Decorator that measures execution time."""
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"{func.__name__} took {(end - start) * 1000:.2f}ms")
        return result
    return wrapper


def retry_decorator(max_attempts=3):
    """Decorator that retries a function on failure."""
    def decorator(func):
        def wrapper(*args, **kwargs):
            last_exception = None
            for attempt in range(max_attempts):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    last_exception = e
            raise last_exception
        return wrapper
    return decorator


def memoize_decorator(func):
    """Decorator that caches function results."""
    cache = {}

    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]

    return wrapper


# =============================================================================
# Main
# =============================================================================

if __name__ == "__main__":
    print("Testing Functional Programming Solutions...")

    # Test Exercise 1
    assert add(5, 3) == 8
    assert add(-1, 1) == 0
    assert is_positive(5) == True
    assert is_positive(-3) == False
    assert is_positive(0) == False
    assert absolute(-5) == 5
    assert absolute(5) == 5
    assert max_of_two(5, 3) == 5
    assert max_of_two(3, 5) == 5

    # Test Exercise 2
    assert double_all([1, 2, 3, 4, 5]) == [2, 4, 6, 8, 10]
    assert double_all([]) == []
    assert get_evens([1, 2, 3, 4, 5, 6]) == [2, 4, 6]
    assert get_evens([1, 3, 5]) == []
    words = ["hello", "hi", "wonderful", "hey", "python"]
    assert get_long_words(words, 3) == ["hello", "wonderful", "python"]
    assert square_and_filter([1, 2, 3, 4, 5]) == [16, 25]

    # Test Exercise 3
    assert product_of_all([1, 2, 3, 4, 5]) == 120
    assert product_of_all([2, 3]) == 6
    assert flatten_list([[1, 2], [3, 4], [5]]) == [1, 2, 3, 4, 5]
    assert flatten_list([[], [1], []]) == [1]
    words = ["hi", "hello", "hey", "python", "code"]
    result = group_by_length(words)
    assert result[2] == ["hi"]
    assert result[3] == ["hey"]
    assert result[4] == ["code"]
    assert result[5] == ["hello"]
    assert result[6] == ["python"]

    # Test Exercise 4
    counter = create_counter(10)
    assert counter() == 11
    assert counter() == 12
    assert counter() == 13
    counter2 = create_counter(0)
    assert counter2() == 1
    double = create_multiplier(2)
    assert double(5) == 10
    assert double(3) == 6
    triple = create_multiplier(3)
    assert triple(5) == 15
    acc = create_accumulator(100)
    assert acc(10) == 110
    assert acc(20) == 130
    acc2 = create_accumulator(0)
    assert acc2(5) == 5

    # Test Exercise 5
    @timer_decorator
    def slow_function(n):
        time.sleep(0.01)
        return n * 2

    result = slow_function(5)
    assert result == 10

    call_count = [0]

    @retry_decorator(max_attempts=3)
    def flaky_function():
        call_count[0] += 1
        if call_count[0] < 3:
            raise ValueError("Not yet")
        return "success"

    result = flaky_function()
    assert result == "success"
    assert call_count[0] == 3

    @memoize_decorator
    def fibonacci(n):
        if n < 2:
            return n
        return fibonacci(n - 1) + fibonacci(n - 2)

    assert fibonacci(10) == 55
    assert fibonacci(5) == 5

    print("All Functional Programming solutions passed!")
