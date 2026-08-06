"""List, dict, set comprehensions and generator expressions."""

# ── List Comprehension ───────────────────────────────────────────────
# [expression for item in iterable if condition]

squares = [x**2 for x in range(10)]
print(squares)  # [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]

evens = [x for x in range(20) if x % 2 == 0]
print(evens)  # [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]

# Nested loops
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
flat = [num for row in matrix for num in row]
print(flat)  # [1, 2, 3, 4, 5, 6, 7, 8, 9]

# Conditional expression
labels = ["even" if x % 2 == 0 else "odd" for x in range(5)]
print(labels)  # ['even', 'odd', 'even', 'odd', 'even']

# ── Dict Comprehension ───────────────────────────────────────────────
# {key_expr: value_expr for item in iterable if condition}

word_lengths = {word: len(word) for word in ["hello", "world", "hi"]}
print(word_lengths)  # {'hello': 5, 'world': 5, 'hi': 2}

# Invert dict
original = {"a": 1, "b": 2, "c": 3}
inverted = {v: k for k, v in original.items()}
print(inverted)  # {1: 'a', 2: 'b', 3: 'c'}

# Filter and transform
scores = {"Alice": 95, "Bob": 87, "Charlie": 62, "Diana": 91}
honors = {name: score for name, score in scores.items() if score >= 90}
print(honors)  # {'Alice': 95, 'Diana': 91}

# ── Set Comprehension ────────────────────────────────────────────────
# {expression for item in iterable if condition}

words = ["hello", "world", "hello", "hi", "world"]
unique_lengths = {len(word) for word in words}
print(unique_lengths)  # {2, 5}

# Unique characters
text = "hello world"
unique_chars = {char for char in text if char.isalpha()}
print(unique_chars)  # {'h', 'e', 'l', 'o', 'w', 'r', 'd'}

# ── Generator Expression ─────────────────────────────────────────────
# (expression for item in iterable if condition)
# Lazy — produces values one at a time

sum_of_squares = sum(x**2 for x in range(1000000))
print(sum_of_squares)  # 333332833333500000

# ── Nested Comprehensions ────────────────────────────────────────────
# Transpose matrix
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
transposed = [[row[i] for row in matrix] for i in range(3)]
print(transposed)  # [[1, 4, 7], [2, 5, 8], [3, 6, 9]]

# Flatten deeply nested
deep = [[[1, 2], [3]], [[4, 5], [6]]]
flat = [num for sublist in deep for group in sublist for num in group]
print(flat)  # [1, 2, 3, 4, 5, 6]

# ── Walrus Operator in Comprehensions ───────────────────────────────
# := assigns and uses in same expression
import re

texts = ["hello123", "world", "python3", "code"]
numbers_in_text = [
    match.group()
    for text in texts
    if (match := re.search(r'\d+', text))
]
print(numbers_in_text)  # ['123', '3']

# ── Performance Comparison ──────────────────────────────────────────
import timeit

# List comprehension vs map+lambda
# List comp is generally faster and more readable

# timeit.timeit('[x**2 for x in range(100)]')   # ~3.5µs
# timeit.timeit('list(map(lambda x: x**2, range(100)))')  # ~7.5µs

# ── Advanced Patterns ───────────────────────────────────────────────
# Flatten with chain.from_iterable
from itertools import chain
nested = [[1, 2], [3, 4], [5]]
flat = list(chain.from_iterable(nested))

# Group by condition
def partition(pred, iterable):
    from itertools import tee
    t1, t2 = tee(iterable)
    return filter(pred, t1), filter(lambda x: not pred(x), t2)

# Multi-condition
result = [
    "small" if x < 5 else "medium" if x < 10 else "large"
    for x in range(15)
]
