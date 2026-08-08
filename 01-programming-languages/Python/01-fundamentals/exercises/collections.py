"""
Module 01 - Fundamentals: Collections Exercises
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Lists (Difficulty: Beginner)
# =============================================================================
# Practice list operations.

# TODO: Manipulate lists
def list_operations():
    """Perform various list operations."""
    fruits = ["apple", "banana", "cherry"]
    # Add "date" to the end
    # Insert "elderberry" at position 1
    # Remove "banana"
    # Sort the list
    return fruits

# TODO: Find duplicates
def find_duplicates(lst):
    """Return a list of elements that appear more than once."""
    pass

# Test cases
# result = list_operations()
# print(result)  # Expected: ["apple", "cherry", "date", "elderberry"] (sorted)
# print(find_duplicates([1, 2, 3, 2, 4, 3, 5]))  # Expected: [2, 3]


# =============================================================================
# Exercise 2: Dictionaries (Difficulty: Beginner)
# =============================================================================
# Practice dictionary operations.

# TODO: Count word frequencies
def word_frequency(text):
    """Return a dictionary with word counts."""
    pass

# TODO: Merge dictionaries
def merge_dicts(dict1, dict2):
    """Merge two dictionaries. If keys conflict, use the higher value."""
    pass

# Test cases
# freq = word_frequency("the cat sat on the mat the cat")
# print(freq)  # Expected: {'the': 3, 'cat': 2, 'sat': 1, 'on': 1, 'mat': 1}
# merged = merge_dicts({'a': 1, 'b': 2}, {'b': 3, 'c': 4})
# print(merged)  # Expected: {'a': 1, 'b': 3, 'c': 4}


# =============================================================================
# Exercise 3: Sets (Difficulty: Beginner)
# =============================================================================
# Practice set operations.

# TODO: Set operations
def set_operations():
    """Perform union, intersection, and difference."""
    set1 = {1, 2, 3, 4, 5}
    set2 = {4, 5, 6, 7, 8}
    # Find union, intersection, and difference (set1 - set2)
    return None, None, None

# TODO: Remove duplicates preserving order
def remove_duplicates(lst):
    """Remove duplicates while preserving original order."""
    pass

# Test cases
# union, intersection, difference = set_operations()
# print(f"Union: {union}")           # Expected: {1, 2, 3, 4, 5, 6, 7, 8}
# print(f"Intersection: {intersection}")  # Expected: {4, 5}
# print(f"Difference: {difference}")  # Expected: {1, 2, 3}
# print(remove_duplicates([1, 2, 2, 3, 3, 3, 4]))  # Expected: [1, 2, 3, 4]


# =============================================================================
# Exercise 4: Tuples (Difficulty: Beginner)
# =============================================================================
# Practice tuple operations and named tuples.

# TODO: Count elements
def count_elements(tuples_list, index):
    """Count occurrences of element at given index."""
    pass

# TODO: Create named tuple
from collections import namedtuple

# Create a Point namedtuple with x and y fields
# Use it to create points and access fields

# Test cases
# data = [(1, 'a'), (2, 'b'), (1, 'c'), (3, 'a')]
# print(count_elements(data, 0))  # Expected: {1: 2, 2: 1, 3: 1}
# print(count_elements(data, 1))  # Expected: {'a': 2, 'b': 1, 'c': 1}


# =============================================================================
# Exercise 5: Deques (Difficulty: Intermediate)
# =============================================================================
# Practice with collections.deque.

from collections import deque

# TODO: Implement a sliding window
def sliding_window(lst, window_size):
    """Return list of sliding windows."""
    pass

# TODO: Implement a queue with max size
class BoundedQueue:
    """A queue that removes oldest item when full."""
    def __init__(self, max_size):
        pass

    def enqueue(self, item):
        pass

    def dequeue(self):
        pass

# Test cases
# print(sliding_window([1, 2, 3, 4, 5], 3))
# # Expected: [[1, 2, 3], [2, 3, 4], [3, 4, 5]]
# q = BoundedQueue(3)
# q.enqueue(1)
# q.enqueue(2)
# q.enqueue(3)
# print(q.dequeue())  # Expected: 1
# q.enqueue(4)
# print(q.dequeue())  # Expected: 2
