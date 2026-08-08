"""
Module 07 - Functional Programming: Map/Filter Exercises
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Basic map() (Difficulty: Beginner)
# =============================================================================
# Use map() for transformations.

# TODO: Implement transformations using map()
numbers = [1, 2, 3, 4, 5]

# Double each number
doubled = None
# Convert to strings
string_numbers = None
# Calculate square roots
import math
sqrt_numbers = None

# Test cases
# print(list(doubled))        # Expected: [2, 4, 6, 8, 10]
# print(list(string_numbers)) # Expected: ['1', '2', '3', '4', '5']
# print(list(sqrt_numbers))   # Expected: [1.0, 1.414..., 1.732..., 2.0, 2.236...]


# =============================================================================
# Exercise 2: Basic filter() (Difficulty: Beginner)
# =============================================================================
# Use filter() for filtering.

# TODO: Implement filters
numbers = range(1, 21)

# Filter even numbers
evens = None
# Filter numbers greater than 10
greater_than_10 = None
# Filter prime numbers
def is_prime(n):
    if n < 2:
        return False
    for i in range(2, int(n ** 0.5) + 1):
        if n % i == 0:
            return False
    return True

primes = None

# Test cases
# print(list(evens))          # Expected: [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
# print(list(greater_than_10)) # Expected: [11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
# print(list(primes))         # Expected: [2, 3, 5, 7, 11, 13, 17, 19]


# =============================================================================
# Exercise 3: Chaining map and filter (Difficulty: Intermediate)
# =============================================================================
# Chain map and filter operations.

# TODO: Implement chained operations
words = ["hello", "world", "python", "is", "awesome", "hi"]

# Get lengths of words longer than 3 characters
long_word_lengths = None
# Convert to uppercase and filter words starting with 'H' or 'P'
filtered_words = None

# Test cases
# print(list(long_word_lengths))  # Expected: [5, 5, 6, 7]
# print(list(filtered_words))    # Expected: ['HELLO', 'PYTHON']


# =============================================================================
# Exercise 4: Custom map/filter (Difficulty: Intermediate)
# =============================================================================
# Implement custom map and filter functions.

# TODO: Implement custom map
def custom_map(func, iterable):
    """Implement map function using list comprehension."""
    pass

# TODO: Implement custom filter
def custom_filter(func, iterable):
    """Implement filter function using list comprehension."""
    pass

# Test cases
# print(list(custom_map(lambda x: x * 2, [1, 2, 3])))  # Expected: [2, 4, 6]
# print(list(custom_filter(lambda x: x > 2, [1, 2, 3, 4])))  # Expected: [3, 4]


# =============================================================================
# Exercise 5: Functional Data Processing (Difficulty: Intermediate)
# =============================================================================
# Process data using functional approach.

# TODO: Process student data
students = [
    {"name": "Alice", "grade": 85, "age": 20},
    {"name": "Bob", "grade": 92, "age": 22},
    {"name": "Charlie", "grade": 78, "age": 21},
    {"name": "Diana", "grade": 95, "age": 20},
    {"name": "Eve", "grade": 88, "age": 23}
]

# Get names of students with grade > 85
high_achievers = None
# Calculate average grade
average_grade = None
# Get students sorted by grade (descending)
top_students = None

# Test cases
# print(high_achievers)     # Expected: ['Bob', 'Diana', 'Eve']
# print(average_grade)      # Expected: 87.6
# print(top_students)       # Expected: Sorted by grade descending
