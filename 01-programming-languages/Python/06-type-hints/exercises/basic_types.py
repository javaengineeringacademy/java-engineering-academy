"""
Module 06 - Type Hints: Basic Types Exercises
Difficulty: Beginner to Intermediate
"""

from typing import List, Dict, Tuple, Optional, Union, Any, Set

# =============================================================================
# Exercise 1: Function Type Hints (Difficulty: Beginner)
# =============================================================================
# Add type hints to functions.

# TODO: Add type hints to these functions
def greet(name):
    """Return a greeting message."""
    return f"Hello, {name}!"

def add_numbers(a, b):
    """Add two numbers and return result."""
    return a + b

def get_user_info(user_id):
    """Get user information by ID."""
    users = {1: {"name": "Alice", "email": "alice@example.com"},
             2: {"name": "Bob", "email": "bob@example.com"}}
    return users.get(user_id)

# Test cases
# print(greet("Alice"))           # Expected: "Hello, Alice!"
# print(add_numbers(5, 3))        # Expected: 8
# print(get_user_info(1))         # Expected: {'name': 'Alice', 'email': 'alice@example.com'}


# =============================================================================
# Exercise 2: Collection Types (Difficulty: Beginner)
# =============================================================================
# Use collection type hints.

# TODO: Add type hints to these functions
def process_items(items):
    """Process a list of items and return filtered results."""
    return [item for item in items if item > 0]

def merge_dicts(dict1, dict2):
    """Merge two dictionaries."""
    return {**dict1, **dict2}

def unique_elements(lst):
    """Return unique elements from list."""
    return list(set(lst))

# Test cases
# print(process_items([-1, 2, -3, 4]))  # Expected: [2, 4]
# print(merge_dicts({"a": 1}, {"b": 2}))  # Expected: {"a": 1, "b": 2}
# print(unique_elements([1, 2, 2, 3, 3]))  # Expected: [1, 2, 3]


# =============================================================================
# Exercise 3: Optional and Union Types (Difficulty: Intermediate)
# =============================================================================
# Use Optional and Union types.

# TODO: Add type hints
def find_user(user_id):
    """Find user by ID, return None if not found."""
    users = {1: "Alice", 2: "Bob"}
    return users.get(user_id)

def process_value(value):
    """Process value that can be int or string."""
    if isinstance(value, int):
        return value * 2
    return value.upper()

def safe_divide(a, b):
    """Divide a by b, return None if b is zero."""
    if b == 0:
        return None
    return a / b

# Test cases
# print(find_user(1))      # Expected: "Alice"
# print(find_user(999))    # Expected: None
# print(process_value(5))  # Expected: 10
# print(process_value("hello"))  # Expected: "HELLO"
# print(safe_divide(10, 2))  # Expected: 5.0
# print(safe_divide(10, 0))  # Expected: None


# =============================================================================
# Exercise 4: Return Type Hints (Difficulty: Intermediate)
# =============================================================================
# Use proper return type hints.

# TODO: Add return type hints
def get_statistics(numbers):
    """Calculate statistics for a list of numbers."""
    return {
        "mean": sum(numbers) / len(numbers),
        "min": min(numbers),
        "max": max(numbers),
        "count": len(numbers)
    }

def split_and_join(text, delimiter=" "):
    """Split text and join with different delimiter."""
    return delimiter.join(text.split())

def create_matrix(rows, cols, fill_value=0):
    """Create a matrix filled with fill_value."""
    return [[fill_value for _ in range(cols)] for _ in range(rows)]

# Test cases
# stats = get_statistics([1, 2, 3, 4, 5])
# print(stats)  # Expected: {'mean': 3.0, 'min': 1, 'max': 5, 'count': 5}
# print(split_and_join("hello world", "-"))  # Expected: "hello-world"
# print(create_matrix(2, 3, 1))  # Expected: [[1, 1, 1], [1, 1, 1]]


# =============================================================================
# Exercise 5: Type Aliases (Difficulty: Intermediate)
# =============================================================================
# Create and use type aliases.

# TODO: Create type aliases
# UserID = None  # Type alias for user ID
# UserDict = None  # Type alias for user dictionary
# Matrix = None  # Type alias for matrix

# TODO: Use type aliases
def get_user(user_id):
    """Get user by ID."""
    pass

def process_matrix(matrix):
    """Process a matrix."""
    pass

# Test cases
# user = get_user(1)
# matrix = [[1, 2, 3], [4, 5, 6]]
# print(process_matrix(matrix))
