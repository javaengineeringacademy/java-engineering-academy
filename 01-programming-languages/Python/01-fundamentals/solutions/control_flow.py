"""
Module 01 - Fundamentals: Control Flow Solutions
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: If/Elif/Else - Solution
# =============================================================================
def calculate_grade(score):
    """Return letter grade based on score."""
    if not isinstance(score, (int, float)) or score < 0 or score > 100:
        return "Invalid"
    if score >= 90:
        return "A"
    elif score >= 80:
        return "B"
    elif score >= 70:
        return "C"
    elif score >= 60:
        return "D"
    else:
        return "F"

print(calculate_grade(95))   # A
print(calculate_grade(85))   # B
print(calculate_grade(72))   # C
print(calculate_grade(65))   # D
print(calculate_grade(45))   # F
print(calculate_grade(105))  # Invalid


# =============================================================================
# Exercise 2: For Loops - Solution
# =============================================================================
def sum_even_numbers(n):
    """Return sum of all even numbers from 1 to n."""
    total = 0
    for i in range(1, n + 1):
        if i % 2 == 0:
            total += i
    return total

def fibonacci(n):
    """Return first n Fibonacci numbers as a list."""
    if n <= 0:
        return []
    if n == 1:
        return [0]
    fib = [0, 1]
    for i in range(2, n):
        fib.append(fib[i-1] + fib[i-2])
    return fib

print(sum_even_numbers(10))  # 30
print(fibonacci(8))          # [0, 1, 1, 2, 3, 5, 8, 13]


# =============================================================================
# Exercise 3: While Loops - Solution
# =============================================================================
def largest_power_of_2(n):
    """Find the largest power of 2 that is less than n."""
    power = 1
    while power * 2 < n:
        power *= 2
    return power

def reverse_number(n):
    """Reverse the digits of a number."""
    reversed_num = 0
    temp = n
    while temp > 0:
        reversed_num = reversed_num * 10 + temp % 10
        temp //= 10
    return reversed_num

print(largest_power_of_2(100))   # 64
print(largest_power_of_2(1024))  # 512
print(reverse_number(12345))     # 54321
print(reverse_number(9876))      # 6789


# =============================================================================
# Exercise 4: Break and Continue - Solution
# =============================================================================
def first_divisible_by_3_and_5(start, end):
    """Find the first number in range [start, end) divisible by both 3 and 5."""
    for num in range(start, end):
        if num % 3 == 0 and num % 5 == 0:
            return num
    return None

def sum_non_multiples_of_3(n):
    """Sum numbers from 1 to n, skipping multiples of 3."""
    total = 0
    for i in range(1, n + 1):
        if i % 3 == 0:
            continue
        total += i
    return total

print(first_divisible_by_3_and_5(1, 100))   # 15
print(first_divisible_by_3_and_5(20, 50))   # 30
print(sum_non_multiples_of_3(10))           # 37


# =============================================================================
# Exercise 5: Nested Loops - Solution
# =============================================================================
def multiplication_table(n):
    """Return a list of strings for multiplication table from 1 to n."""
    table = []
    for i in range(1, n + 1):
        for j in range(1, n + 1):
            table.append(f"{i} x {j} = {i * j}")
    return table

def common_elements(list1, list2):
    """Find elements that appear in both lists (without using set)."""
    common = []
    for item in list1:
        if item in list2 and item not in common:
            common.append(item)
    return common

table = multiplication_table(3)
for row in table:
    print(row)

print(common_elements([1, 2, 3, 4, 5], [3, 4, 5, 6, 7]))  # [3, 4, 5]
