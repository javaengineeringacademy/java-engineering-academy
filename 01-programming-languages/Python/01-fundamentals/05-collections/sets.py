"""Set operations, methods, and patterns."""

# ── Creating Sets ────────────────────────────────────────────────────
empty = set()                    # {} is an empty dict, not set
numbers = {1, 2, 3, 4, 5}
from_list = set([1, 2, 2, 3])  # {1, 2, 3} — removes duplicates
from_string = set("hello")     # {'h', 'e', 'l', 'o'}
frozen = frozenset([1, 2, 3])  # Immutable set

# ── Set Methods ──────────────────────────────────────────────────────
s = {1, 2, 3}
s.add(4)                # {1, 2, 3, 4}
s.update([5, 6])        # {1, 2, 3, 4, 5, 6}
s.discard(6)            # Remove if exists (no error)
s.remove(5)             # Remove (KeyError if missing)
popped = s.pop()        # Remove and return arbitrary element
s.clear()               # Empty the set

# ── Set Operations ───────────────────────────────────────────────────
a = {1, 2, 3, 4}
b = {3, 4, 5, 6}

# Union — all elements from both
print(a | b)            # {1, 2, 3, 4, 5, 6}
print(a.union(b))

# Intersection — common elements
print(a & b)            # {3, 4}
print(a.intersection(b))

# Difference — elements in a but not b
print(a - b)            # {1, 2}
print(a.difference(b))

# Symmetric difference — elements in either but not both
print(a ^ b)            # {1, 2, 5, 6}
print(a.symmetric_difference(b))

# ── In-Place Operations ─────────────────────────────────────────────
a = {1, 2, 3}
a |= {4, 5}        # Union in place
a &= {3, 4, 5}     # Intersection in place
a -= {3}            # Difference in place
a ^= {4, 6}         # Symmetric difference in place

# ── Subset/Superset ──────────────────────────────────────────────────
small = {1, 2}
large = {1, 2, 3, 4}
print(small.issubset(large))       # True  — small ⊆ large
print(large.issuperset(small))     # True  — large ⊇ small
print(small <= large)              # True  — subset
print(large >= small)              # True  — superset

# ── Membership Testing ───────────────────────────────────────────────
# Sets have O(1) lookup vs O(n) for lists
big_set = set(range(1000000))
big_list = list(range(1000000))
# 1 in big_set  → O(1)
# 1 in big_list → O(n)

# ── Useful Patterns ──────────────────────────────────────────────────
# Remove duplicates while preserving order (3.7+)
items = [3, 1, 4, 1, 5, 9, 2, 6, 5, 3]
unique_ordered = list(dict.fromkeys(items))

# Find common elements
list1 = [1, 2, 3, 4, 5]
list2 = [3, 4, 5, 6, 7]
common = list(set(list1) & set(list2))  # [3, 4, 5]

# Set difference for "in list but not in other"
only_in_first = list(set(list1) - set(list2))  # [1, 2]

# Check if all items from one collection are in another
required = {"name", "email", "age"}
provided = {"name": "Alice", "email": "a@b.com", "age": 30, "city": "NYC"}
missing = required - provided.keys()  # set() — all provided

# Frozen sets as dict keys (hashable)
cache = {}
fs = frozenset([1, 2, 3])
cache[fs] = "result"
