"""
Module 03 - Advanced: Comprehensions Exercises
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: List Comprehensions (Difficulty: Beginner)
# =============================================================================
# Create various list comprehensions.

# TODO: Create list comprehensions
def squares(n):
    """Return list of squares from 1 to n."""
    pass

def filter_evens(numbers):
    """Return only even numbers from list."""
    pass

def matrix_transpose(matrix):
    """Transpose a matrix using list comprehension."""
    pass

# Test cases
# print(squares(5))  # Expected: [1, 4, 9, 16, 25]
# print(filter_evens([1, 2, 3, 4, 5, 6]))  # Expected: [2, 4, 6]
# matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
# print(matrix_transpose(matrix))  # Expected: [[1, 4, 7], [2, 5, 8], [3, 6, 9]]


# =============================================================================
# Exercise 2: Dictionary Comprehensions (Difficulty: Beginner)
# =============================================================================
# Create dictionary comprehensions.

# TODO: Create dictionary comprehensions
def word_lengths(words):
    """Create dict mapping words to their lengths."""
    pass

def invert_dict(d):
    """Invert a dictionary (swap keys and values)."""
    pass

def filter_dict(d, condition):
    """Filter dictionary by condition on values."""
    pass

# Test cases
# print(word_lengths(["hello", "world", "python"]))
# # Expected: {'hello': 5, 'world': 5, 'python': 6}
# print(invert_dict({'a': 1, 'b': 2, 'c': 3}))
# # Expected: {1: 'a', 2: 'b', 3: 'c'}
# print(filter_dict({'a': 1, 'b': 2, 'c': 3, 'd': 4}, lambda x: x > 2))
# # Expected: {'c': 3, 'd': 4}


# =============================================================================
# Exercise 3: Set Comprehensions (Difficulty: Beginner)
# =============================================================================
# Create set comprehensions.

# TODO: Create set comprehensions
def unique_chars(text):
    """Return set of unique characters in text."""
    pass

def common_elements(list1, list2):
    """Find common elements using set comprehension."""
    pass

def powers_of_two(n):
    """Generate set of powers of 2 up to n."""
    pass

# Test cases
# print(unique_chars("hello world"))  # Expected: {'h', 'e', 'l', 'o', ' ', 'w', 'r', 'd'}
# print(common_elements([1, 2, 3, 4], [3, 4, 5, 6]))  # Expected: {3, 4}
# print(powers_of_two(100))  # Expected: {1, 2, 4, 8, 16, 32, 64}


# =============================================================================
# Exercise 4: Nested Comprehensions (Difficulty: Intermediate)
# =============================================================================
# Work with nested comprehensions.

# TODO: Flatten nested list
def flatten(nested_list):
    """Flatten a nested list using comprehension."""
    pass

# TODO: Create multiplication table
def multiplication_grid(n):
    """Create n x n multiplication table."""
    pass

# TODO: Group by first letter
def group_by_first_letter(words):
    """Group words by first letter."""
    pass

# Test cases
# print(flatten([[1, 2], [3, 4], [5, 6]]))  # Expected: [1, 2, 3, 4, 5, 6]
# grid = multiplication_grid(3)
# for row in grid:
#     print(row)
# # Expected:
# # [1, 2, 3]
# # [2, 4, 6]
# # [3, 6, 9]
# print(group_by_first_letter(["apple", "banana", "avocado", "cherry"]))
# # Expected: {'a': ['apple', 'avocado'], 'b': ['banana'], 'c': ['cherry']}


# =============================================================================
# Exercise 5: Generator Comprehensions (Difficulty: Intermediate)
# =============================================================================
# Use generator comprehensions for memory efficiency.

# TODO: Implement generator comprehensions
def sum_of_squares_gen(n):
    """Sum of squares using generator comprehension."""
    pass

def fibonacci_gen(limit):
    """Fibonacci numbers using generator comprehension."""
    pass

# Test cases
# print(sum_of_squares_gen(10))  # Expected: 385
# print(list(fibonacci_gen(50)))  # Expected: [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
