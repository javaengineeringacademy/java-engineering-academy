"""
Module 03 - Advanced: Comprehensions Solutions
Difficulty: Beginner to Intermediate
"""

# =============================================================================
# Exercise 1: List Comprehensions - Solution
# =============================================================================
def squares(n):
    """Return list of squares from 1 to n."""
    return [x * x for x in range(1, n + 1)]

def filter_evens(numbers):
    """Return only even numbers from list."""
    return [x for x in numbers if x % 2 == 0]

def matrix_transpose(matrix):
    """Transpose a matrix using list comprehension."""
    return [[row[i] for row in matrix] for i in range(len(matrix[0]))]

print(squares(5))  # [1, 4, 9, 16, 25]
print(filter_evens([1, 2, 3, 4, 5, 6]))  # [2, 4, 6]
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
print(matrix_transpose(matrix))  # [[1, 4, 7], [2, 5, 8], [3, 6, 9]]


# =============================================================================
# Exercise 2: Dictionary Comprehensions - Solution
# =============================================================================
def word_lengths(words):
    """Create dict mapping words to their lengths."""
    return {word: len(word) for word in words}

def invert_dict(d):
    """Invert a dictionary (swap keys and values)."""
    return {v: k for k, v in d.items()}

def filter_dict(d, condition):
    """Filter dictionary by condition on values."""
    return {k: v for k, v in d.items() if condition(v)}

print(word_lengths(["hello", "world", "python"]))  # {'hello': 5, 'world': 5, 'python': 6}
print(invert_dict({'a': 1, 'b': 2, 'c': 3}))  # {1: 'a', 2: 'b', 3: 'c'}
print(filter_dict({'a': 1, 'b': 2, 'c': 3, 'd': 4}, lambda x: x > 2))  # {'c': 3, 'd': 4}


# =============================================================================
# Exercise 3: Set Comprehensions - Solution
# =============================================================================
def unique_chars(text):
    """Return set of unique characters in text."""
    return {char for char in text}

def common_elements(list1, list2):
    """Find common elements using set comprehension."""
    return {x for x in list1 if x in list2}

def powers_of_two(n):
    """Generate set of powers of 2 up to n."""
    return {2 ** i for i in range(n.bit_length()) if 2 ** i <= n}

print(unique_chars("hello world"))  # {'h', 'e', 'l', 'o', ' ', 'w', 'r', 'd'}
print(common_elements([1, 2, 3, 4], [3, 4, 5, 6]))  # {3, 4}
print(powers_of_two(100))  # {1, 2, 4, 8, 16, 32, 64}


# =============================================================================
# Exercise 4: Nested Comprehensions - Solution
# =============================================================================
def flatten(nested_list):
    """Flatten a nested list using comprehension."""
    return [item for sublist in nested_list for item in sublist]

def multiplication_grid(n):
    """Create n x n multiplication table."""
    return [[i * j for j in range(1, n + 1)] for i in range(1, n + 1)]

def group_by_first_letter(words):
    """Group words by first letter."""
    return {letter: [word for word in words if word[0] == letter]
            for letter in set(word[0] for word in words)}

print(flatten([[1, 2], [3, 4], [5, 6]]))  # [1, 2, 3, 4, 5, 6]
grid = multiplication_grid(3)
for row in grid:
    print(row)
# [1, 2, 3]
# [2, 4, 6]
# [3, 6, 9]
print(group_by_first_letter(["apple", "banana", "avocado", "cherry"]))
# {'a': ['apple', 'avocado'], 'b': ['banana'], 'c': ['cherry']}


# =============================================================================
# Exercise 5: Generator Comprehensions - Solution
# =============================================================================
def sum_of_squares_gen(n):
    """Sum of squares using generator comprehension."""
    return sum(x * x for x in range(1, n + 1))

def fibonacci_gen(limit):
    """Fibonacci numbers using generator comprehension."""
    a, b = 0, 1
    while a <= limit:
        yield a
        a, b = b, a + b

print(sum_of_squares_gen(10))  # 385
print(list(fibonacci_gen(50)))  # [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
