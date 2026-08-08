"""
Module 01 - Fundamentals: Collections Solutions
Difficulty: Beginner
"""

# =============================================================================
# Exercise 1: Lists - Solution
# =============================================================================
def list_operations():
    """Perform various list operations."""
    fruits = ["apple", "banana", "cherry"]
    fruits.append("date")          # Add to end
    fruits.insert(1, "elderberry") # Insert at position 1
    fruits.remove("banana")        # Remove "banana"
    fruits.sort()                  # Sort the list
    return fruits

def find_duplicates(lst):
    """Return a list of elements that appear more than once."""
    seen = set()
    duplicates = set()
    for item in lst:
        if item in seen:
            duplicates.add(item)
        seen.add(item)
    return list(duplicates)

print(list_operations())  # ["apple", "cherry", "date", "elderberry"]
print(find_duplicates([1, 2, 3, 2, 4, 3, 5]))  # [2, 3]


# =============================================================================
# Exercise 2: Dictionaries - Solution
# =============================================================================
def word_frequency(text):
    """Return a dictionary with word counts."""
    freq = {}
    for word in text.split():
        freq[word] = freq.get(word, 0) + 1
    return freq

def merge_dicts(dict1, dict2):
    """Merge two dictionaries. If keys conflict, use the higher value."""
    result = dict1.copy()
    for key, value in dict2.items():
        if key in result:
            result[key] = max(result[key], value)
        else:
            result[key] = value
    return result

freq = word_frequency("the cat sat on the mat the cat")
print(freq)  # {'the': 3, 'cat': 2, 'sat': 1, 'on': 1, 'mat': 1}
merged = merge_dicts({'a': 1, 'b': 2}, {'b': 3, 'c': 4})
print(merged)  # {'a': 1, 'b': 3, 'c': 4}


# =============================================================================
# Exercise 3: Sets - Solution
# =============================================================================
def set_operations():
    """Perform union, intersection, and difference."""
    set1 = {1, 2, 3, 4, 5}
    set2 = {4, 5, 6, 7, 8}
    return set1 | set2, set1 & set2, set1 - set2

def remove_duplicates(lst):
    """Remove duplicates while preserving original order."""
    seen = set()
    result = []
    for item in lst:
        if item not in seen:
            seen.add(item)
            result.append(item)
    return result

union, intersection, difference = set_operations()
print(f"Union: {union}")           # {1, 2, 3, 4, 5, 6, 7, 8}
print(f"Intersection: {intersection}")  # {4, 5}
print(f"Difference: {difference}")  # {1, 2, 3}
print(remove_duplicates([1, 2, 2, 3, 3, 3, 4]))  # [1, 2, 3, 4]


# =============================================================================
# Exercise 4: Tuples - Solution
# =============================================================================
from collections import namedtuple

def count_elements(tuples_list, index):
    """Count occurrences of element at given index."""
    counts = {}
    for t in tuples_list:
        key = t[index]
        counts[key] = counts.get(key, 0) + 1
    return counts

# Create and use named tuple
Point = namedtuple('Point', ['x', 'y'])
p1 = Point(1, 2)
p2 = Point(3, 4)
print(f"Point 1: {p1}, Point 2: {p2}")
print(f"p1.x = {p1.x}, p1.y = {p1.y}")

data = [(1, 'a'), (2, 'b'), (1, 'c'), (3, 'a')]
print(count_elements(data, 0))  # {1: 2, 2: 1, 3: 1}
print(count_elements(data, 1))  # {'a': 2, 'b': 1, 'c': 1}


# =============================================================================
# Exercise 5: Deques - Solution
# =============================================================================
from collections import deque

def sliding_window(lst, window_size):
    """Return list of sliding windows."""
    if window_size > len(lst):
        return []
    windows = []
    window = deque(lst[:window_size])
    windows.append(list(window))
    for i in range(window_size, len(lst)):
        window.popleft()
        window.append(lst[i])
        windows.append(list(window))
    return windows

class BoundedQueue:
    """A queue that removes oldest item when full."""
    def __init__(self, max_size):
        self.queue = deque(maxlen=max_size)

    def enqueue(self, item):
        self.queue.append(item)

    def dequeue(self):
        return self.queue.popleft()

print(sliding_window([1, 2, 3, 4, 5], 3))
# [[1, 2, 3], [2, 3, 4], [3, 4, 5]]

q = BoundedQueue(3)
q.enqueue(1)
q.enqueue(2)
q.enqueue(3)
print(q.dequeue())  # 1
q.enqueue(4)
print(q.dequeue())  # 2
