"""Generators, yield, and lazy evaluation."""

# ── Generator Function ───────────────────────────────────────────────
def countdown(n):
    """Yield values one at a time — lazy evaluation."""
    print("Starting countdown")
    while n > 0:
        yield n
        n -= 1
    print("Done!")

# Generator object — code doesn't run until iterated
gen = countdown(5)
print(next(gen))  # 5
print(next(gen))  # 4

# Or iterate fully
for num in countdown(3):
    print(num, end=" ")

# ── Generator Expression ─────────────────────────────────────────────
# Like list comprehension but with () — lazy
squares = (x**2 for x in range(1000000))
print(next(squares))  # 0
print(next(squares))  # 1

# Memory efficient — doesn't create full list
import sys
list_comp = [x**2 for x in range(1000)]
gen_exp = (x**2 for x in range(1000))
print(sys.getsizeof(list_comp))  # ~8856 bytes
print(sys.getsizeof(gen_exp))    # ~200 bytes

# ── Infinite Generators ──────────────────────────────────────────────
def fibonacci():
    """Infinite Fibonacci sequence."""
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b

# Take first 10
from itertools import islice
first_10 = list(islice(fibonacci(), 10))
print(first_10)  # [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]

# ── Generator Pipeline ───────────────────────────────────────────────
def read_large_file(file_path):
    """Read file line by line — doesn't load entire file."""
    with open(file_path, "r") as f:
        for line in f:
            yield line.strip()

def filter_comments(lines):
    """Filter out comment lines."""
    for line in lines:
        if not line.startswith("#"):
            yield line

def parse_csv(lines):
    """Parse CSV lines."""
    for line in lines:
        yield line.split(",")

# Pipeline: file → filter → parse (each step is lazy)
# lines = read_large_file("data.csv")
# comments_removed = filter_comments(lines)
# parsed = parse_csv(comments_removed)

# ── yield from ───────────────────────────────────────────────────────
def flatten(nested_list):
    """Recursively flatten nested lists."""
    for item in nested_list:
        if isinstance(item, list):
            yield from flatten(item)  # Delegate to sub-generator
        else:
            yield item

flat = list(flatten([1, [2, 3], [4, [5, 6]], 7]))
print(flat)  # [1, 2, 3, 4, 5, 6, 7]

# ── Send Values to Generator ─────────────────────────────────────────
def accumulator():
    """Receive values via send() and maintain running total."""
    total = 0
    while True:
        value = yield total
        if value is None:
            break
        total += value

acc = accumulator()
next(acc)            # Prime the generator
print(acc.send(10))  # 10
print(acc.send(20))  # 30
print(acc.send(5))   # 35

# ── Generator as Coroutine ──────────────────────────────────────────
def averager():
    """Running average using send."""
    count = 0
    total = 0
    average = None
    while True:
        value = yield average
        count += 1
        total += value
        average = total / count

avg = averager()
next(avg)
print(avg.send(10))   # 10.0
print(avg.send(20))   # 15.0
print(avg.send(30))   # 20.0

# ── itertools Patterns ──────────────────────────────────────────────
from itertools import chain, islice, groupby, count

# chain — combine iterables
combined = list(chain([1, 2], [3, 4], [5]))  # [1, 2, 3, 4, 5]

# islice — slice any iterable
first_five = list(islice(count(), 5))  # [0, 1, 2, 3, 4]

# groupby — group consecutive elements
data = [("A", 1), ("A", 2), ("B", 3), ("B", 4)]
for key, group in groupby(data, key=lambda x: x[0]):
    print(key, list(group))
