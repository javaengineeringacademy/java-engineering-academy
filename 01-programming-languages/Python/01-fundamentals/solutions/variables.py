"""
Module 01 - Fundamentals: Variables Solutions
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Variable Assignment - Solution
# =============================================================================
# Python supports multiple assignment and tuple unpacking.

a = 10
b = 20
# Swap without temporary variable using tuple unpacking
a, b = b, a

# Test cases
print(f"a: {a}, b: {b}")  # Expected: a: 20, b: 10


# =============================================================================
# Exercise 2: Type Checking - Solution
# =============================================================================
# Python is dynamically typed, but we can check types with type().

my_int = 42
my_float = 3.14
my_string = "hello"
my_bool = True
my_list = [1, 2, 3]
my_dict = {"key": "value"}
my_none = None

# Test cases
print(type(my_int))      # Expected: <class 'int'>
print(type(my_float))    # Expected: <class 'float'>
print(type(my_string))   # Expected: <class 'str'>


# =============================================================================
# Exercise 3: Multiple Assignment - Solution
# =============================================================================
# Python's unpacking syntax makes this elegant.

x, y, z = 1, 2, 3

colors = ["red", "green", "blue"]
first, second, third = colors

numbers = [1, 2, 3, 4, 5]
first, *middle, last = numbers

# Test cases
print(f"x: {x}, y: {y}, z: {z}")  # Expected: x: 1, y: 2, z: 3
print(f"Colors: {first}, {second}, {third}")  # Expected: red, green, blue
print(f"Numbers: first={first}, middle={middle}, last={last}")  # Expected: first=1, middle=[2,3,4], last=5


# =============================================================================
# Exercise 4: Variable Scope - Solution
# =============================================================================
# Use 'global' keyword to modify global variables from inside functions.

counter = 0

def increment_global():
    """Increment the global counter variable."""
    global counter
    counter += 1

def local_only():
    """Create a local variable 'x' and return it."""
    x = 100
    return x

# Test cases
increment_global()
increment_global()
print(f"Global counter: {counter}")  # Expected: 2
print(f"Local: {local_only()}")  # Expected: 100


# =============================================================================
# Exercise 5: Mutable vs Immutable - Solution
# =============================================================================
# Immutable types (int, str, tuple) cannot be changed in-place.
# Mutable types (list, dict, set) can be modified.

def modify_string(s):
    """Try to modify the string (immutable).
    Strings cannot be modified - reassigning creates a new object."""
    s = "modified"
    return s

def modify_list(lst):
    """Modify the list in-place (mutable).
    Lists can be modified - append() changes the original object."""
    lst.append(4)
    return lst

# Test cases
original_str = "hello"
result_str = modify_string(original_str)
print(f"Original string: {original_str}")  # Expected: "hello" (unchanged)
print(f"Result string: {result_str}")      # Expected: "modified"

original_list = [1, 2, 3]
result_list = modify_list(original_list)
print(f"Original list: {original_list}")   # Expected: [1, 2, 3, 4] (changed!)
print(f"Result list: {result_list}")       # Expected: [1, 2, 3, 4]
