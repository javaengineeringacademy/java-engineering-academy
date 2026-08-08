"""
Module 07 - Functional Programming: Map/Filter Solutions
Difficulty: Beginner to Intermediate
"""

import math

# =============================================================================
# Exercise 1: Basic map() - Solution
# =============================================================================
numbers = [1, 2, 3, 4, 5]

doubled = map(lambda x: x * 2, numbers)
string_numbers = map(str, numbers)
sqrt_numbers = map(math.sqrt, numbers)

print(list(doubled))        # [2, 4, 6, 8, 10]
print(list(string_numbers)) # ['1', '2', '3', '4', '5']
print(list(sqrt_numbers))   # [1.0, 1.414..., 1.732..., 2.0, 2.236...]


# =============================================================================
# Exercise 2: Basic filter() - Solution
# =============================================================================
numbers = range(1, 21)

evens = filter(lambda x: x % 2 == 0, numbers)
greater_than_10 = filter(lambda x: x > 10, numbers)

def is_prime(n):
    if n < 2:
        return False
    for i in range(2, int(n ** 0.5) + 1):
        if n % i == 0:
            return False
    return True

primes = filter(is_prime, numbers)

print(list(evens))          # [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
print(list(greater_than_10)) # [11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
print(list(primes))         # [2, 3, 5, 7, 11, 13, 17, 19]


# =============================================================================
# Exercise 3: Chaining map and filter - Solution
# =============================================================================
words = ["hello", "world", "python", "is", "awesome", "hi"]

long_word_lengths = map(len, filter(lambda w: len(w) > 3, words))
filtered_words = map(str.upper, filter(lambda w: w[0] in 'hp', words))

print(list(long_word_lengths))  # [5, 5, 6, 7]
print(list(filtered_words))    # ['HELLO', 'PYTHON']


# =============================================================================
# Exercise 4: Custom map/filter - Solution
# =============================================================================
def custom_map(func, iterable):
    """Implement map function using list comprehension."""
    return [func(item) for item in iterable]

def custom_filter(func, iterable):
    """Implement filter function using list comprehension."""
    return [item for item in iterable if func(item)]

print(custom_map(lambda x: x * 2, [1, 2, 3]))  # [2, 4, 6]
print(custom_filter(lambda x: x > 2, [1, 2, 3, 4]))  # [3, 4]


# =============================================================================
# Exercise 5: Functional Data Processing - Solution
# =============================================================================
students = [
    {"name": "Alice", "grade": 85, "age": 20},
    {"name": "Bob", "grade": 92, "age": 22},
    {"name": "Charlie", "grade": 78, "age": 21},
    {"name": "Diana", "grade": 95, "age": 20},
    {"name": "Eve", "grade": 88, "age": 23}
]

high_achievers = list(map(lambda s: s["name"], filter(lambda s: s["grade"] > 85, students)))
average_grade = sum(map(lambda s: s["grade"], students)) / len(students)
top_students = sorted(students, key=lambda s: s["grade"], reverse=True)

print(high_achievers)     # ['Bob', 'Diana', 'Eve']
print(average_grade)      # 87.6
print(top_students)       # Sorted by grade descending
