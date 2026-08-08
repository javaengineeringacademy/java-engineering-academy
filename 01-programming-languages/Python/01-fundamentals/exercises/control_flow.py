"""
Module 01 - Fundamentals: Control Flow Exercises
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: If/Elif/Else (Difficulty: Beginner)
# =============================================================================
# Create a grade calculator based on score.

# TODO: Implement the grade calculator
def calculate_grade(score):
    """Return letter grade based on score.
    A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: below 60
    Return 'Invalid' for scores outside 0-100.
    """
    pass

# Test cases
# print(calculate_grade(95))   # Expected: "A"
# print(calculate_grade(85))   # Expected: "B"
# print(calculate_grade(72))   # Expected: "C"
# print(calculate_grade(65))   # Expected: "D"
# print(calculate_grade(45))   # Expected: "F"
# print(calculate_grade(105))  # Expected: "Invalid"


# =============================================================================
# Exercise 2: For Loops (Difficulty: Beginner)
# =============================================================================
# Create various loop patterns.

# TODO: Sum all even numbers from 1 to n
def sum_even_numbers(n):
    """Return sum of all even numbers from 1 to n."""
    pass

# TODO: Generate Fibonacci sequence
def fibonacci(n):
    """Return first n Fibonacci numbers as a list."""
    pass

# Test cases
# print(sum_even_numbers(10))  # Expected: 30 (2+4+6+8+10)
# print(fibonacci(8))          # Expected: [0, 1, 1, 2, 3, 5, 8, 13]


# =============================================================================
# Exercise 3: While Loops (Difficulty: Beginner)
# =============================================================================
# Use while loops for specific scenarios.

# TODO: Find the largest power of 2 less than n
def largest_power_of_2(n):
    """Find the largest power of 2 that is less than n."""
    pass

# TODO: Reverse a number
def reverse_number(n):
    """Reverse the digits of a number."""
    pass

# Test cases
# print(largest_power_of_2(100))   # Expected: 64
# print(largest_power_of_2(1024))  # Expected: 512
# print(reverse_number(12345))     # Expected: 54321
# print(reverse_number(9876))      # Expected: 6789


# =============================================================================
# Exercise 4: Break and Continue (Difficulty: Intermediate)
# =============================================================================
# Use break and continue to control loop execution.

# TODO: Find first number divisible by both 3 and 5
def first_divisible_by_3_and_5(start, end):
    """Find the first number in range [start, end) divisible by both 3 and 5."""
    pass

# TODO: Skip multiples of 3
def sum_non_multiples_of_3(n):
    """Sum numbers from 1 to n, skipping multiples of 3."""
    pass

# Test cases
# print(first_divisible_by_3_and_5(1, 100))   # Expected: 15
# print(first_divisible_by_3_and_5(20, 50))   # Expected: 30
# print(sum_non_multiples_of_3(10))           # Expected: 37 (1+2+4+5+7+8+10)


# =============================================================================
# Exercise 5: Nested Loops (Difficulty: Intermediate)
# =============================================================================
# Work with nested loop patterns.

# TODO: Create multiplication table
def multiplication_table(n):
    """Return a list of strings for multiplication table from 1 to n.
    Each string should be: "1 x 1 = 1"
    """
    pass

# TODO: Find common elements
def common_elements(list1, list2):
    """Find elements that appear in both lists (without using set)."""
    pass

# Test cases
# table = multiplication_table(3)
# for row in table:
#     print(row)
# # Expected:
# # 1 x 1 = 1
# # 1 x 2 = 2
# # 1 x 3 = 3
# # 2 x 1 = 2
# # 2 x 2 = 4
# # 2 x 3 = 6
# # 3 x 1 = 3
# # 3 x 2 = 6
# # 3 x 3 = 9

# print(common_elements([1, 2, 3, 4, 5], [3, 4, 5, 6, 7]))  # Expected: [3, 4, 5]
