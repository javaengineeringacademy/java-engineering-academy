"""List operations, methods, and patterns."""

# ── Creating Lists ───────────────────────────────────────────────────
empty = []
numbers = [1, 2, 3, 4, 5]
mixed = [1, "hello", 3.14, True, None]
nested = [[1, 2], [3, 4], [5, 6]]

# From iterable
chars = list("abc")              # ['a', 'b', 'c']
from_range = list(range(5))      # [0, 1, 2, 3, 4]
copied = list(numbers)           # shallow copy

# ── Indexing and Slicing ─────────────────────────────────────────────
fruits = ["apple", "banana", "cherry", "date", "elderberry"]

print(fruits[0])      # "apple" (first)
print(fruits[-1])     # "elderberry" (last)
print(fruits[1:3])    # ["banana", "cherry"] (slice)
print(fruits[::2])    # ["apple", "cherry", "elderberry"] (every other)
print(fruits[::-1])   # reversed copy

# ── List Methods ─────────────────────────────────────────────────────
nums = [3, 1, 4, 1, 5, 9, 2, 6]

nums.append(7)          # Add to end → [3, 1, 4, 1, 5, 9, 2, 6, 7]
nums.insert(0, 0)       # Insert at index → [0, 3, 1, ...]
nums.extend([8, 9])     # Add multiple → [0, 3, 1, ..., 8, 9]

nums.remove(1)          # Remove first occurrence of value
popped = nums.pop()     # Remove and return last
popped = nums.pop(0)    # Remove and return at index
nums.clear()            # Empty the list

# ── Searching ────────────────────────────────────────────────────────
fruits = ["apple", "banana", "cherry", "banana"]
print(fruits.index("banana"))    # 1 (first occurrence)
print(fruits.count("banana"))    # 2
print("banana" in fruits)        # True

# ── Sorting ──────────────────────────────────────────────────────────
numbers = [3, 1, 4, 1, 5, 9, 2, 6]
sorted_nums = sorted(numbers)        # Returns new list, original unchanged
numbers.sort()                        # Sorts in place
numbers.sort(reverse=True)            # Descending

# Sort by key
words = ["banana", "Apple", "cherry"]
words.sort(key=str.lower)            # Case-insensitive sort

# ── List Comprehensions ──────────────────────────────────────────────
squares = [x**2 for x in range(10)]
evens = [x for x in range(20) if x % 2 == 0]
flat = [num for row in nested for num in row]

# ── Unpacking ────────────────────────────────────────────────────────
first, *middle, last = [1, 2, 3, 4, 5]
# first=1, middle=[2, 3, 4], last=5

a, b, *rest = [1, 2, 3, 4, 5]
# a=1, b=2, rest=[3, 4, 5]

# ── Useful Patterns ──────────────────────────────────────────────────
# Remove duplicates (preserves order in 3.7+)
unique = list(dict.fromkeys([1, 2, 2, 3, 3, 3]))

# Chunk a list
def chunk(lst, size):
    return [lst[i:i+size] for i in range(0, len(lst), size)]

print(chunk([1, 2, 3, 4, 5], 2))  # [[1, 2], [3, 4], [5]]

# Flatten nested list
def flatten(lst):
    return [item for sublist in lst for item in sublist]

# Rotate list
def rotate(lst, n):
    return lst[n:] + lst[:n]

# Interleave two lists
def interleave(a, b):
    return [val for pair in zip(a, b) for val in pair]
