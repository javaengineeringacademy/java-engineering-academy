"""Dictionary operations, methods, and patterns."""

# ── Creating Dicts ───────────────────────────────────────────────────
empty = {}
person = {"name": "Alice", "age": 30, "city": "NYC"}
from_pairs = dict([("a", 1), ("b", 2)])
from_kwargs = dict(name="Alice", age=30)
default = dict.fromkeys(["x", "y", "z"], 0)  # {'x': 0, 'y': 0, 'z': 0}

# ── Accessing Values ─────────────────────────────────────────────────
print(person["name"])           # KeyError if missing
print(person.get("name"))       # None if missing
print(person.get("email", "N/A"))  # Default value

# Check key existence
print("name" in person)         # True

# ── Dict Methods ─────────────────────────────────────────────────────
person["email"] = "alice@example.com"   # Add/update
person.update({"age": 31, "phone": "555"})  # Bulk update
popped = person.pop("phone")            # Remove and return
popped = person.pop("missing", None)    # Safe pop with default
last = person.popitem()                 # Remove last inserted

# setdefault — get or set
person.setdefault("email", "unknown")   # Returns existing value
person.setdefault("role", "admin")      # Sets and returns default

# ── Iterating ────────────────────────────────────────────────────────
for key in person:
    print(key, person[key])

for key, value in person.items():
    print(f"{key}: {value}")

for key in person.keys():
    print(key)

for value in person.values():
    print(value)

# ── Merging ──────────────────────────────────────────────────────────
dict1 = {"a": 1, "b": 2}
dict2 = {"b": 3, "c": 4}

# Python 3.9+
merged = dict1 | dict2

# Pre-3.9
merged = {**dict1, **dict2}

# update (modifies in place)
dict1.update(dict2)

# ── Dictionary Comprehensions ────────────────────────────────────────
squares = {x: x**2 for x in range(6)}
filtered = {k: v for k, v in squares.items() if v > 10}
inverted = {v: k for k, v in squares.items()}

# ── Useful Patterns ──────────────────────────────────────────────────
# Count occurrences
words = ["apple", "banana", "apple", "cherry", "banana", "apple"]
counts = {}
for word in words:
    counts[word] = counts.get(word, 0) + 1

# Using Counter (from collections)
from collections import Counter
counts = Counter(words)

# Group by key
from itertools import groupby
data = [("A", 1), ("B", 2), ("A", 3)]
grouped = {}
for key, val in data:
    grouped.setdefault(key, []).append(val)

# Nested access with defaultdict
from collections import defaultdict
tree = defaultdict(list)
tree["fruits"].append("apple")
tree["fruits"].append("banana")

# Sort dict by value
sorted_by_value = dict(sorted(counts.items(), key=lambda x: x[1], reverse=True))

# Merge with Counter
c1 = Counter(a=1, b=2)
c2 = Counter(b=3, c=4)
combined = c1 + c2  # Counter({'b': 5, 'c': 4, 'a': 1})
