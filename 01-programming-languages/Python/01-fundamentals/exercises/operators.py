"""
Module 01 - Fundamentals: Operators Exercises
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Arithmetic Operators (Difficulty: Beginner)
# =============================================================================
# Perform various arithmetic operations.

# TODO: Complete the calculations
a = 17
b = 5

addition = None       # a + b
subtraction = None    # a - b
multiplication = None # a * b
division = None       # a / b (float division)
floor_division = None # a // b (integer division)
modulus = None        # a % b (remainder)
exponent = None       # a ** b (power)

# Test cases
# print(f"Addition: {addition}")           # Expected: 22
# print(f"Subtraction: {subtraction}")     # Expected: 12
# print(f"Multiplication: {multiplication}")  # Expected: 85
# print(f"Division: {division}")           # Expected: 3.4
# print(f"Floor Division: {floor_division}")  # Expected: 3
# print(f"Modulus: {modulus}")             # Expected: 2
# print(f"Exponent: {exponent}")           # Expected: 1419857


# =============================================================================
# Exercise 2: Comparison Operators (Difficulty: Beginner)
# =============================================================================
# Compare values using comparison operators.

# TODO: Write comparison expressions
x = 10
y = 20
z = 10

# Write boolean expressions (True/False)
is_equal = None           # x == z
is_not_equal = None       # x != y
is_greater = None         # x > y
is_less = None            # x < y
is_greater_equal = None   # x >= z
is_less_equal = None      # y <= z

# Test cases
# print(f"x == z: {is_equal}")         # Expected: True
# print(f"x != y: {is_not_equal}")     # Expected: True
# print(f"x > y: {is_greater}")        # Expected: False
# print(f"x < y: {is_less}")           # Expected: True
# print(f"x >= z: {is_greater_equal}") # Expected: True
# print(f"y <= z: {is_less_equal}")    # Expected: False


# =============================================================================
# Exercise 3: Logical Operators (Difficulty: Beginner)
# =============================================================================
# Combine boolean expressions using logical operators.

# TODO: Complete the logical expressions
age = 25
has_license = True
is_student = False

can_drive = None      # age >= 16 and has_license
gets_discount = None  # is_student or age < 18
is_adult = None       # not is_student and age >= 18

# Test cases
# print(f"Can drive: {can_drive}")        # Expected: True
# print(f"Gets discount: {gets_discount}")  # Expected: False
# print(f"Is adult: {is_adult}")          # Expected: True


# =============================================================================
# Exercise 4: Membership Operators (Difficulty: Beginner)
# =============================================================================
# Check if values exist in sequences.

# TODO: Write membership checks
fruits = ["apple", "banana", "cherry", "date"]
numbers = (1, 2, 3, 4, 5)
text = "Hello, World!"

has_apple = None       # "apple" in fruits
has_grape = None       # "grape" not in fruits
has_three = None       # 3 in numbers
has_world = None       # "World" in text

# Test cases
# print(f"Has apple: {has_apple}")   # Expected: True
# print(f"Has grape: {has_grape}")   # Expected: True
# print(f"Has three: {has_three}")   # Expected: True
# print(f"Has World: {has_world}")   # Expected: True


# =============================================================================
# Exercise 5: Identity vs Equality (Difficulty: Intermediate)
# =============================================================================
# Understand the difference between '==' and 'is'.

# TODO: Explain the difference
list1 = [1, 2, 3]
list2 = [1, 2, 3]
list3 = list1

# Compare values and identity
value_equal = None     # list1 == list2
identity_equal = None  # list1 is list2
same_reference = None  # list1 is list3

# Test cases
# print(f"list1 == list2: {value_equal}")      # Expected: True (same value)
# print(f"list1 is list2: {identity_equal}")    # Expected: False (different objects)
# print(f"list1 is list3: {same_reference}")    # Expected: True (same object)
