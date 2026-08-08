"""
Module 01 - Fundamentals: Functions Exercises
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Basic Functions (Difficulty: Beginner)
# =============================================================================
# Create functions with different parameter types.

# TODO: Function with default parameters
def greet(name, greeting="Hello"):
    """Return a greeting string."""
    pass

# TODO: Function with *args
def calculate_sum(*args):
    """Return the sum of all arguments."""
    pass

# TODO: Function with **kwargs
def create_profile(**kwargs):
    """Return a dictionary of profile information."""
    pass

# Test cases
# print(greet("Alice"))              # Expected: "Hello, Alice!"
# print(greet("Bob", "Hi"))          # Expected: "Hi, Bob!"
# print(calculate_sum(1, 2, 3, 4))   # Expected: 10
# profile = create_profile(name="John", age=30, city="NYC")
# print(profile)                     # Expected: {'name': 'John', 'age': 30, 'city': 'NYC'}


# =============================================================================
# Exercise 2: Return Values (Difficulty: Beginner)
# =============================================================================
# Practice returning different types of values.

# TODO: Return multiple values
def get_stats(numbers):
    """Return min, max, and average of a list."""
    pass

# TODO: Return a dictionary
def analyze_text(text):
    """Return word count, character count, and average word length."""
    pass

# Test cases
# min_val, max_val, avg = get_stats([3, 1, 4, 1, 5, 9, 2, 6])
# print(f"Min: {min_val}, Max: {max_val}, Avg: {avg}")
# # Expected: Min: 1, Max: 9, Avg: 3.875

# stats = analyze_text("Hello World")
# print(stats)
# # Expected: {'words': 2, 'chars': 11, 'avg_length': 5.0}


# =============================================================================
# Exercise 3: Lambda Functions (Difficulty: Intermediate)
# =============================================================================
# Use lambda for simple functions.

# TODO: Sort a list of tuples by second element
def sort_by_second(tuples_list):
    """Sort list of tuples by second element using lambda."""
    pass

# TODO: Filter even numbers
def filter_evens(numbers):
    """Return only even numbers using filter and lambda."""
    pass

# Test cases
# data = [(1, 3), (2, 1), (3, 2)]
# print(sort_by_second(data))  # Expected: [(2, 1), (3, 2), (1, 3)]
# print(filter_evens([1, 2, 3, 4, 5, 6]))  # Expected: [2, 4, 6]


# =============================================================================
# Exercise 4: Recursion (Difficulty: Intermediate)
# =============================================================================
# Implement recursive solutions.

# TODO: Calculate factorial recursively
def factorial(n):
    """Calculate n! recursively."""
    pass

# TODO: Flatten nested list
def flatten(nested_list):
    """Flatten a nested list of arbitrary depth."""
    pass

# Test cases
# print(factorial(5))   # Expected: 120
# print(factorial(0))   # Expected: 1
# print(flatten([1, [2, 3], [4, [5, 6]]]))  # Expected: [1, 2, 3, 4, 5, 6]


# =============================================================================
# Exercise 5: Higher-Order Functions (Difficulty: Intermediate)
# =============================================================================
# Functions that take or return other functions.

# TODO: Create a function decorator (without using @)
def repeat(times):
    """Return a decorator that repeats a function call 'times' times."""
    pass

# TODO: Create a custom map function
def my_map(func, iterable):
    """Apply func to each element in iterable."""
    pass

# Test cases
# @repeat(3)
# def say_hello():
#     return "Hello!"
# print(say_hello())  # Expected: ["Hello!", "Hello!", "Hello!"]

# result = my_map(lambda x: x * 2, [1, 2, 3])
# print(result)  # Expected: [2, 4, 6]
