"""
Module 01 - Fundamentals: Variables Exercises
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Variable Assignment (Difficulty: Beginner)
# =============================================================================
# Create variables of different types and swap their values.

# TODO: Create variables and swap them
a = None  # Set to 10
b = None  # Set to 20
# TODO: Swap the values of a and b without using a temporary variable

# Test cases
# print(f"a: {a}, b: {b}")  # Expected: a: 20, b: 10


# =============================================================================
# Exercise 2: Type Checking (Difficulty: Beginner)
# =============================================================================
# Create variables and use type() to verify their types.

# TODO: Create variables of each type
my_int = None      # Set to 42
my_float = None    # Set to 3.14
my_string = None   # Set to "hello"
my_bool = None     # Set to True
my_list = None     # Set to [1, 2, 3]
my_dict = None     # Set to {"key": "value"}
my_none = None     # Set to None

# Test cases
# print(type(my_int))      # Expected: <class 'int'>
# print(type(my_float))    # Expected: <class 'float'>
# print(type(my_string))   # Expected: <class 'str'>


# =============================================================================
# Exercise 3: Multiple Assignment (Difficulty: Beginner)
# =============================================================================
# Use Python's multiple assignment feature.

# TODO: Assign multiple variables at once
x, y, z = None, None, None  # Set to 1, 2, 3

# TODO: Unpack a list into variables
colors = ["red", "green", "blue"]
# Unpack colors into first, second, third

# TODO: Use extended unpacking
numbers = [1, 2, 3, 4, 5]
# first, *middle, last = None  # Unpack so first=1, middle=[2,3,4], last=5

# Test cases
# print(f"x: {x}, y: {y}, z: {z}")  # Expected: x: 1, y: 2, z: 3
# print(f"Colors: {first}, {second}, {third}")  # Expected: red, green, blue
# print(f"Numbers: first={first}, middle={middle}, last={last}")  # Expected: first=1, middle=[2,3,4], last=5


# =============================================================================
# Exercise 4: Variable Scope (Difficulty: Intermediate)
# =============================================================================
# Understand local vs global scope.

counter = 0

# TODO: Create a function that modifies the global variable
def increment_global():
    """Increment the global counter variable."""
    pass

# TODO: Create a function with a local variable
def local_only():
    """Create a local variable 'x' and return it."""
    x = 100
    return x

# Test cases
# increment_global()
# increment_global()
# print(f"Global counter: {counter}")  # Expected: 2
# print(f"Local: {local_only()}")  # Expected: 100


# =============================================================================
# Exercise 5: Mutable vs Immutable (Difficulty: Intermediate)
# =============================================================================
# Demonstrate the difference between mutable and immutable types.

# TODO: Show immutable behavior
def modify_string(s):
    """Try to modify the string (immutable)."""
    s = "modified"
    return s

# TODO: Show mutable behavior
def modify_list(lst):
    """Modify the list in-place (mutable)."""
    lst.append(4)
    return lst

# Test cases
original_str = "hello"
result_str = modify_string(original_str)
# print(f"Original string: {original_str}")  # Expected: "hello" (unchanged)
# print(f"Result string: {result_str}")      # Expected: "modified"

original_list = [1, 2, 3]
result_list = modify_list(original_list)
# print(f"Original list: {original_list}")   # Expected: [1, 2, 3, 4] (changed!)
# print(f"Result list: {result_list}")       # Expected: [1, 2, 3, 4]
