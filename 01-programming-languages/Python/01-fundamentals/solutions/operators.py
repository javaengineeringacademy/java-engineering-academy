"""
Module 01 - Fundamentals: Operators Solutions
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Arithmetic Operators - Solution
# =============================================================================
a = 17
b = 5

addition = a + b           # 22
subtraction = a - b        # 12
multiplication = a * b     # 85
division = a / b           # 3.4
floor_division = a // b    # 3
modulus = a % b            # 2
exponent = a ** b          # 1419857

print(f"Addition: {addition}")
print(f"Subtraction: {subtraction}")
print(f"Multiplication: {multiplication}")
print(f"Division: {division}")
print(f"Floor Division: {floor_division}")
print(f"Modulus: {modulus}")
print(f"Exponent: {exponent}")


# =============================================================================
# Exercise 2: Comparison Operators - Solution
# =============================================================================
x = 10
y = 20
z = 10

is_equal = x == z           # True
is_not_equal = x != y       # True
is_greater = x > y          # False
is_less = x < y             # True
is_greater_equal = x >= z   # True
is_less_equal = y <= z      # False

print(f"x == z: {is_equal}")
print(f"x != y: {is_not_equal}")
print(f"x > y: {is_greater}")
print(f"x < y: {is_less}")
print(f"x >= z: {is_greater_equal}")
print(f"y <= z: {is_less_equal}")


# =============================================================================
# Exercise 3: Logical Operators - Solution
# =============================================================================
age = 25
has_license = True
is_student = False

can_drive = age >= 16 and has_license       # True
gets_discount = is_student or age < 18      # False
is_adult = not is_student and age >= 18     # True

print(f"Can drive: {can_drive}")
print(f"Gets discount: {gets_discount}")
print(f"Is adult: {is_adult}")


# =============================================================================
# Exercise 4: Membership Operators - Solution
# =============================================================================
fruits = ["apple", "banana", "cherry", "date"]
numbers = (1, 2, 3, 4, 5)
text = "Hello, World!"

has_apple = "apple" in fruits       # True
has_grape = "grape" not in fruits   # True
has_three = 3 in numbers            # True
has_world = "World" in text         # True

print(f"Has apple: {has_apple}")
print(f"Has grape: {has_grape}")
print(f"Has three: {has_three}")
print(f"Has World: {has_world}")


# =============================================================================
# Exercise 5: Identity vs Equality - Solution
# =============================================================================
# == checks if values are equal
# is checks if objects are the same in memory

list1 = [1, 2, 3]
list2 = [1, 2, 3]  # New list with same values
list3 = list1       # Same reference as list1

value_equal = list1 == list2      # True (same value)
identity_equal = list1 is list2   # False (different objects)
same_reference = list1 is list3   # True (same object)

print(f"list1 == list2: {value_equal}")
print(f"list1 is list2: {identity_equal}")
print(f"list1 is list3: {same_reference}")
