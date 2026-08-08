"""
Module 03 - Advanced: Lambda Exercises
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: Lambda Basics (Difficulty: Beginner)
# =============================================================================
# Create lambda functions for simple operations.

# TODO: Create lambda functions
square = None      # Square of a number
add = None         # Add two numbers
is_even = None     # Check if number is even
absolute = None    # Absolute value
power = None       # Raise to power

# Test cases
# print(square(5))      # Expected: 25
# print(add(3, 4))      # Expected: 7
# print(is_even(4))     # Expected: True
# print(absolute(-5))   # Expected: 5
# print(power(2, 3))    # Expected: 8


# =============================================================================
# Exercise 2: Lambda with map() (Difficulty: Beginner)
# =============================================================================
# Use lambda with map() for transformations.

# TODO: Apply transformations
numbers = [1, 2, 3, 4, 5]
doubled = None        # Double each number
cubed = None          # Cube each number
to_strings = None     # Convert to strings

# Test cases
# print(list(doubled))      # Expected: [2, 4, 6, 8, 10]
# print(list(cubed))        # Expected: [1, 8, 27, 64, 125]
# print(list(to_strings))   # Expected: ['1', '2', '3', '4', '5']


# =============================================================================
# Exercise 3: Lambda with filter() (Difficulty: Beginner)
# =============================================================================
# Use lambda with filter() for filtering.

# TODO: Filter data
numbers = range(1, 21)
evens = None           # Filter even numbers
greater_than_10 = None # Filter numbers > 10
multiples_of_3 = None  # Filter multiples of 3

# Test cases
# print(list(evens))           # Expected: [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
# print(list(greater_than_10)) # Expected: [11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
# print(list(multiples_of_3))  # Expected: [3, 6, 9, 12, 15, 18]


# =============================================================================
# Exercise 4: Lambda with sorted() (Difficulty: Intermediate)
# =============================================================================
# Use lambda as sort key.

# TODO: Sort data
students = [
    {"name": "Alice", "grade": 88},
    {"name": "Bob", "grade": 95},
    {"name": "Charlie", "grade": 82},
    {"name": "Diana", "grade": 95}
]

# Sort by grade (ascending)
by_grade_asc = None
# Sort by grade (descending)
by_grade_desc = None
# Sort by name
by_name = None

# Test cases
# print(by_grade_asc)
# # Expected: [{'name': 'Charlie', 'grade': 82}, {'name': 'Alice', 'grade': 88},
# #            {'name': 'Bob', 'grade': 95}, {'name': 'Diana', 'grade': 95}]
# print(by_name)
# # Expected: [{'name': 'Alice', ...}, {'name': 'Bob', ...}, ...]


# =============================================================================
# Exercise 5: Lambda in Functional Patterns (Difficulty: Intermediate)
# =============================================================================
# Use lambda in functional programming patterns.

# TODO: Implement compose function
def compose(f, g):
    """Compose two functions: (f ∘ g)(x) = f(g(x))"""
    return lambda *args, **kwargs: f(g(*args, **kwargs))

# TODO: Implement curry function
def curry(func):
    """Curry a function."""
    pass

# Test cases
# add_one = lambda x: x + 1
# double = lambda x: x * 2
# add_one_then_double = compose(double, add_one)
# print(add_one_then_double(5))  # Expected: 12 (5+1=6, 6*2=12)
#
# add = lambda x, y: x + y
# add5 = curry(add)(5)
# print(add5(3))  # Expected: 8
