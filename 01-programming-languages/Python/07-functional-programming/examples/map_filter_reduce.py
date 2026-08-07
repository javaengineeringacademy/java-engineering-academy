"""
Map, Filter, and Reduce in Python
Demonstrates functional programming with built-in higher-order functions
"""

from functools import reduce
from typing import List, Callable, Any

# ============================================
# Map Function
# ============================================

def double(x: int) -> int:
    """Double a number."""
    return x * 2

def square(x: int) -> int:
    """Square a number."""
    return x ** 2

def to_upper(s: str) -> str:
    """Convert string to uppercase."""
    return s.upper()

# Map with function
numbers = [1, 2, 3, 4, 5]
doubled = list(map(double, numbers))
squared = list(map(square, numbers))

# Map with lambda
tripled = list(map(lambda x: x * 3, numbers))
names = ["alice", "bob", "charlie"]
capitalized = list(map(to_upper, names))

# Map with multiple iterables
list1 = [1, 2, 3]
list2 = [10, 20, 30]
summed = list(map(lambda x, y: x + y, list1, list2))

# ============================================
# Filter Function
# ============================================

def is_even(x: int) -> bool:
    """Check if number is even."""
    return x % 2 == 0

def is_positive(x: int) -> bool:
    """Check if number is positive."""
    return x > 0

def is_long_word(word: str) -> bool:
    """Check if word is longer than 4 characters."""
    return len(word) > 4

# Filter with function
evens = list(filter(is_even, numbers))
positives = list(filter(is_positive, [-2, -1, 0, 1, 2]))

# Filter with lambda
odds = list(filter(lambda x: x % 2 != 0, numbers))
greater_than_3 = list(filter(lambda x: x > 3, numbers))

# Filter strings
words = ["hi", "hello", "hey", "howdy", "yo"]
long_words = list(filter(is_long_word, words))

# ============================================
# Reduce Function
# ============================================

def add(x: int, y: int) -> int:
    """Add two numbers."""
    return x + y

def multiply(x: int, y: int) -> int:
    """Multiply two numbers."""
    return x * y

def max_value(x: int, y: int) -> int:
    """Return the larger value."""
    return x if x > y else y

# Reduce with function
sum_result = reduce(add, numbers)
product = reduce(multiply, numbers)
largest = reduce(max_value, numbers)

# Reduce with lambda
factorial_5 = reduce(lambda x, y: x * y, range(1, 6))
concatenated = reduce(lambda x, y: f"{x}-{y}", ["a", "b", "c", "d"])

# ============================================
# Combining Map, Filter, Reduce
# ============================================

# Squares of even numbers
squares_of_evens = list(map(
    square,
    filter(is_even, numbers)
))
# Same as: [x**2 for x in numbers if x % 2 == 0]

# Sum of squares of even numbers
sum_of_squares = reduce(
    add,
    map(square, filter(is_even, numbers))
)

# ============================================
# List Comprehension Equivalents
# ============================================

# Map equivalent
doubled_comp = [x * 2 for x in numbers]

# Filter equivalent
evens_comp = [x for x in numbers if x % 2 == 0]

# Combined
result_comp = [x**2 for x in numbers if x % 2 == 0]

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("=== Map ===")
    print(f"Original: {numbers}")       # [1, 2, 3, 4, 5]
    print(f"Doubled: {doubled}")        # [2, 4, 6, 8, 10]
    print(f"Squared: {squared}")        # [1, 4, 9, 16, 25]
    print(f"Tripled: {tripled}")        # [3, 6, 9, 12, 15]
    print(f"Capitalized: {capitalized}")  # ['ALICE', 'BOB', 'CHARLIE']
    print(f"Summed: {summed}")          # [11, 22, 33]
    
    print("\n=== Filter ===")
    print(f"Even: {evens}")                  # [2, 4]
    print(f"Odds: {odds}")                   # [1, 3, 5]
    print(f"Positive: {positives}")          # [1, 2]
    print(f"Greater than 3: {greater_than_3}")  # [4, 5]
    print(f"Long words: {long_words}")       # ['hello', 'howdy']
    
    print("\n=== Reduce ===")
    print(f"Sum: {sum_result}")          # 15
    print(f"Product: {product}")         # 120
    print(f"Largest: {largest}")         # 5
    print(f"5! = {factorial_5}")         # 120
    print(f"Concatenated: {concatenated}")  # a-b-c-d
    
    print("\n=== Combined ===")
    print(f"Squares of evens: {squares_of_evens}")  # [4, 16]
    print(f"Sum of squares of evens: {sum_of_squares}")  # 20
    
    print("\n=== List Comprehension Equivalents ===")
    print(f"Doubled (comp): {doubled_comp}")  # [2, 4, 6, 8, 10]
    print(f"Even (comp): {evens_comp}")       # [2, 4]
    print(f"Combined (comp): {result_comp}")  # [4, 16]
