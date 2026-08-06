"""Lambda functions, map, filter, and reduce."""

# ── Lambda Basics ────────────────────────────────────────────────────
# lambda args: expression
square = lambda x: x ** 2
add = lambda a, b: a + b
identity = lambda x: x

print(square(5))    # 25
print(add(2, 3))    # 5

# ── map() — Apply Function to Every Item ────────────────────────────
numbers = [1, 2, 3, 4, 5]
squared = list(map(lambda x: x**2, numbers))
print(squared)  # [1, 4, 9, 16, 25]

# With multiple iterables
a = [1, 2, 3]
b = [10, 20, 30]
sums = list(map(lambda x, y: x + y, a, b))
print(sums)  # [11, 22, 33]

# Equivalent list comprehension
squared = [x**2 for x in numbers]

# ── filter() — Keep Items Where Condition is True ───────────────────
numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
evens = list(filter(lambda x: x % 2 == 0, numbers))
print(evens)  # [2, 4, 6, 8, 10]

# Equivalent list comprehension
evens = [x for x in numbers if x % 2 == 0]

# filter(None, ...) — removes falsy values
mixed = [0, 1, "", "hello", None, [], [1, 2]]
truthy = list(filter(None, mixed))
print(truthy)  # [1, 'hello', [1, 2]]

# ── reduce() — Accumulate to Single Value ────────────────────────────
from functools import reduce

numbers = [1, 2, 3, 4, 5]
total = reduce(lambda acc, x: acc + x, numbers)
print(total)  # 15

product = reduce(lambda acc, x: acc * x, numbers)
print(product)  # 120

# With initial value
total = reduce(lambda acc, x: acc + x, numbers, 100)
print(total)  # 115

# ── Practical Examples ──────────────────────────────────────────────
students = [
    {"name": "Alice", "score": 95},
    {"name": "Bob", "score": 87},
    {"name": "Charlie", "score": 92},
    {"name": "Diana", "score": 78},
]

# Sort by score
sorted_students = sorted(students, key=lambda s: s["score"], reverse=True)

# Get names
names = list(map(lambda s: s["name"], students))

# Filter high scores
honors = list(filter(lambda s: s["score"] >= 90, students))

# Average score
avg = reduce(lambda acc, s: acc + s["score"], students) / len(students)

# ── operator Module Alternatives ────────────────────────────────────
import operator

# operator.itemgetter — replaces lambda x: x[key]
get_name = operator.itemgetter("name")
print(get_name(students[0]))  # "Alice"

# operator.attrgetter — replaces lambda x: x.attr
# get_name = operator.attrgetter("name")

# operator.methodcaller — replaces lambda x: x.method()
upper = operator.methodcaller("upper")
print(upper("hello"))  # "HELLO"

# ── sorted() with key ──────────────────────────────────────────────
words = ["banana", "Apple", "cherry", "date"]
case_insensitive = sorted(words, key=str.lower)
print(case_insensitive)  # ['Apple', 'banana', 'cherry', 'date']

# Sort by multiple criteria
students = [("Alice", 95), ("Bob", 87), ("Charlie", 95)]
by_score_name = sorted(students, key=lambda s: (-s[1], s[0]))

# ── any() and all() ─────────────────────────────────────────────────
numbers = [2, 4, 6, 8, 10]
print(all(x % 2 == 0 for x in numbers))  # True — all even
print(any(x > 8 for x in numbers))        # True — at least one > 8

# ── Chaining Operations ─────────────────────────────────────────────
from functools import reduce
from operator import mul

# Pipeline: filter → map → reduce
result = reduce(
    mul,
    map(lambda x: x**2,
        filter(lambda x: x > 3, range(10)))
)
print(result)  # 4*5*6*7*8*9 = 60480
