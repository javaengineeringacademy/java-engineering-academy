"""
Module 03 - Advanced: Generators Exercises
Difficulty: Intermediate to Advanced
"""

# =============================================================================
# Exercise 1: Basic Generators (Difficulty: Beginner)
# =============================================================================
# Create generator functions for common patterns.

# TODO: Implement fibonacci generator
def fibonacci(limit):
    """Generate Fibonacci numbers up to limit."""
    pass

# TODO: Implement range generator
def custom_range(start, stop=None, step=1):
    """Implement custom range using generator."""
    pass

# Test cases
# print(list(fibonacci(50)))  # Expected: [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
# print(list(custom_range(5)))  # Expected: [0, 1, 2, 3, 4]
# print(list(custom_range(1, 10, 2)))  # Expected: [1, 3, 5, 7, 9]


# =============================================================================
# Exercise 2: Generator Expressions (Difficulty: Beginner)
# =============================================================================
# Use generator expressions for memory-efficient operations.

# TODO: Implement using generator expressions
def sum_of_squares(n):
    """Calculate sum of squares using generator expression."""
    pass

# TODO: Flatten nested list
def flatten_generator(nested_list):
    """Flatten a nested list using generator."""
    pass

# Test cases
# print(sum_of_squares(10))  # Expected: 385
# nested = [[1, 2], [3, 4], [5, 6]]
# print(list(flatten_generator(nested)))  # Expected: [1, 2, 3, 4, 5, 6]


# =============================================================================
# Exercise 3: Generator Pipelines (Difficulty: Intermediate)
# =============================================================================
# Chain generators for data processing.

# TODO: Implement generator pipeline
def read_data(source):
    """Read data from source (simulated)."""
    for item in source:
        yield item

def filter_data(data, condition):
    """Filter data based on condition."""
    pass

def transform_data(data, func):
    """Transform data using function."""
    pass

# Test cases
# source = range(100)
# pipeline = transform_data(
#     filter_data(read_data(source), lambda x: x % 2 == 0),
#     lambda x: x * x
# )
# print(list(pipeline)[:5])  # Expected: [0, 4, 16, 36, 64]


# =============================================================================
# Exercise 4: Infinite Generators (Difficulty: Intermediate)
# =============================================================================
# Create infinite generators with send() and throw().

# TODO: Implement running average
def running_average():
    """Calculate running average of values sent to generator."""
    pass

# TODO: Implement state machine
def traffic_light():
    """Simulate traffic light state machine."""
    pass

# Test cases
# avg = running_average()
# avg.send(None)  # Initialize
# print(avg.send(10))   # Expected: 10.0
# print(avg.send(20))   # Expected: 15.0
# print(avg.send(30))   # Expected: 20.0
#
# light = traffic_light()
# next(light)  # Initialize
# print(next(light))  # Expected: "Green"
# print(next(light))  # Expected: "Yellow"
# print(next(light))  # Expected: "Red"


# =============================================================================
# Exercise 5: Generator Performance (Difficulty: Advanced)
# =============================================================================
# Use generators for memory-efficient processing.

# TODO: Implement chunked reading
def chunked_file_reader(file_path, chunk_size=1024):
    """Read file in chunks using generator."""
    pass

# TODO: Implement lazy evaluation
class LazyRange:
    """A lazy range that generates values on demand."""

    def __init__(self, start, stop=None, step=1):
        pass

    def __iter__(self):
        pass

    def __len__(self):
        pass

# Test cases
# lazy = LazyRange(1000000)
# print(sum(lazy))  # Expected: 499999500000 (memory efficient)
# print(lazy[:5])   # Expected: LazyRange slice
