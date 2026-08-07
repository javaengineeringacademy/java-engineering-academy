"""
Module 07: Functional Programming - Exercises
Practice functional programming concepts in Python.
"""

from functools import reduce


# =============================================================================
# Exercise 1: Lambda Functions (⭐)
# =============================================================================
# Implement various lambda functions for common operations

# TODO: Create a lambda that adds two numbers
add = None  # lambda a, b: ...

# TODO: Create a lambda that checks if a number is positive
is_positive = None  # lambda x: ...

# TODO: Create a lambda that returns the absolute value
absolute = None  # lambda x: ...

# TODO: Create a lambda that returns the larger of two numbers
max_of_two = None  # lambda a, b: ...


# Test Exercise 1
def test_exercise_1():
    print("Exercise 1: Lambda Functions")

    assert add(5, 3) == 8, f"Expected 8, got {add(5, 3)}"
    assert add(-1, 1) == 0, f"Expected 0, got {add(-1, 1)}"

    assert is_positive(5) == True, f"Expected True, got {is_positive(5)}"
    assert is_positive(-3) == False, f"Expected False, got {is_positive(-3)}"
    assert is_positive(0) == False, f"Expected False, got {is_positive(0)}"

    assert absolute(-5) == 5, f"Expected 5, got {absolute(-5)}"
    assert absolute(5) == 5, f"Expected 5, got {absolute(5)}"

    assert max_of_two(5, 3) == 5, f"Expected 5, got {max_of_two(5, 3)}"
    assert max_of_two(3, 5) == 5, f"Expected 5, got {max_of_two(3, 5)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 2: Map and Filter (⭐⭐)
# =============================================================================
# Use map() and filter() to transform and filter data collections

def double_all(numbers):
    """Double each number in the list using map.
    TODO: Use map() with a lambda to double each number
    """
    pass


def get_evens(numbers):
    """Filter and return only even numbers.
    TODO: Use filter() with a lambda to select even numbers
    """
    pass


def get_long_words(words, min_length):
    """Filter words longer than min_length.
    TODO: Use filter() to select words longer than min_length
    """
    pass


def square_and_filter(numbers):
    """Square all numbers, then filter out those less than 10.
    TODO: Use map() to square, then filter() to remove values < 10
    """
    pass


# Test Exercise 2
def test_exercise_2():
    print("\nExercise 2: Map and Filter")

    assert double_all([1, 2, 3, 4, 5]) == [2, 4, 6, 8, 10], f"Expected [2, 4, 6, 8, 10], got {double_all([1, 2, 3, 4, 5])}"
    assert double_all([]) == [], f"Expected [], got {double_all([])}"

    assert get_evens([1, 2, 3, 4, 5, 6]) == [2, 4, 6], f"Expected [2, 4, 6], got {get_evens([1, 2, 3, 4, 5, 6])}"
    assert get_evens([1, 3, 5]) == [], f"Expected [], got {get_evens([1, 3, 5])}"

    words = ["hello", "hi", "wonderful", "hey", "python"]
    assert get_long_words(words, 3) == ["hello", "wonderful", "python"], f"Expected filtered words, got {get_long_words(words, 3)}"

    assert square_and_filter([1, 2, 3, 4, 5]) == [16, 25], f"Expected [16, 25], got {square_and_filter([1, 2, 3, 4, 5])}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 3: Reduce and Accumulation (⭐⭐)
# =============================================================================
# Implement reduce operations to accumulate results from collections

def product_of_all(numbers):
    """Calculate the product of all numbers using reduce.
    TODO: Use reduce() to multiply all numbers together
    """
    pass


def flatten_list(nested_list):
    """Flatten a nested list using reduce.
    TODO: Use reduce() to concatenate sublists
    """
    pass


def group_by_length(words):
    """Group words by their length using reduce.
    TODO: Use reduce() to create a dictionary with lengths as keys
    """
    pass


# Test Exercise 3
def test_exercise_3():
    print("\nExercise 3: Reduce and Accumulation")

    assert product_of_all([1, 2, 3, 4, 5]) == 120, f"Expected 120, got {product_of_all([1, 2, 3, 4, 5])}"
    assert product_of_all([2, 3]) == 6, f"Expected 6, got {product_of_all([2, 3])}"

    assert flatten_list([[1, 2], [3, 4], [5]]) == [1, 2, 3, 4, 5], f"Expected [1, 2, 3, 4, 5], got {flatten_list([[1, 2], [3, 4], [5]])}"
    assert flatten_list([[], [1], []]) == [1], f"Expected [1], got {flatten_list([[], [1], []])}"

    words = ["hi", "hello", "hey", "python", "code"]
    result = group_by_length(words)
    assert result == {2: ["hi", "hey"], 5: ["hello", "python"], 4: ["code"]}, f"Expected grouped dict, got {result}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 4: Closures (⭐⭐⭐)
# =============================================================================
# Create functions that return other functions with captured state

def create_counter(start=0):
    """Create a counter function that increments from start.
    TODO: Return a function that increments and returns the current count
    """
    pass


def create_multiplier(factor):
    """Create a function that multiplies by factor.
    TODO: Return a function that multiplies its argument by factor
    """
    pass


def create_accumulator(initial=0):
    """Create an accumulator function.
    TODO: Return a function that adds to the accumulated total
    """
    pass


# Test Exercise 4
def test_exercise_4():
    print("\nExercise 4: Closures")

    counter = create_counter(10)
    assert counter() == 11, f"Expected 11, got {counter()}"
    assert counter() == 12, f"Expected 12, got {counter()}"
    assert counter() == 13, f"Expected 13, got {counter()}"

    counter2 = create_counter(0)
    assert counter2() == 1, f"Expected 1, got {counter2()}"

    double = create_multiplier(2)
    assert double(5) == 10, f"Expected 10, got {double(5)}"
    assert double(3) == 6, f"Expected 6, got {double(3)}"

    triple = create_multiplier(3)
    assert triple(5) == 15, f"Expected 15, got {triple(5)}"

    acc = create_accumulator(100)
    assert acc(10) == 110, f"Expected 110, got {acc(10)}"
    assert acc(20) == 130, f"Expected 130, got {acc(20)}"

    acc2 = create_accumulator(0)
    assert acc2(5) == 5, f"Expected 5, got {acc2(5)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 5: Decorators (⭐⭐⭐⭐)
# =============================================================================
# Implement various decorators for function enhancement

def timer_decorator(func):
    """Decorator that measures execution time.
    TODO: Import time module, measure start and end time
    """
    import time

    def wrapper(*args, **kwargs):
        # TODO: Record start time
        # Call the function
        # Record end time
        # Print execution time
        # Return the result
        pass

    return wrapper


def retry_decorator(max_attempts=3):
    """Decorator that retries a function on failure.
    TODO: Create a decorator factory that retries on exceptions
    """
    def decorator(func):
        def wrapper(*args, **kwargs):
            # TODO: Implement retry logic
            pass
        return wrapper
    return decorator


def memoize_decorator(func):
    """Decorator that caches function results.
    TODO: Implement caching using a dictionary
    """
    cache = {}

    def wrapper(*args):
        # TODO: Check cache, call function if not cached
        pass

    return wrapper


# Test Exercise 5
def test_exercise_5():
    print("\nExercise 5: Decorators")

    @timer_decorator
    def slow_function(n):
        import time
        time.sleep(0.01)
        return n * 2

    result = slow_function(5)
    assert result == 10, f"Expected 10, got {result}"

    call_count = 0

    @retry_decorator(max_attempts=3)
    def flaky_function():
        nonlocal call_count
        call_count += 1
        if call_count < 3:
            raise ValueError("Not yet")
        return "success"

    result = flaky_function()
    assert result == "success", f"Expected 'success', got {result}"
    assert call_count == 3, f"Expected 3 attempts, got {call_count}"

    @memoize_decorator
    def fibonacci(n):
        if n < 2:
            return n
        return fibonacci(n - 1) + fibonacci(n - 2)

    assert fibonacci(10) == 55, f"Expected 55, got {fibonacci(10)}"
    assert fibonacci(5) == 5, f"Expected 5, got {fibonacci(5)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Main
# =============================================================================
if __name__ == "__main__":
    print("=" * 60)
    print("Module 07: Functional Programming - Exercises")
    print("=" * 60)

    test_exercise_1()
    test_exercise_2()
    test_exercise_3()
    test_exercise_4()
    test_exercise_5()

    print("\n" + "=" * 60)
    print("All exercises completed!")
    print("=" * 60)