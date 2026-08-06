"""for, while loops, and comprehensions."""

# ── For Loop ─────────────────────────────────────────────────────────
# Iterates over any iterable (list, str, dict, range, generator)
fruits = ["apple", "banana", "cherry"]
for fruit in fruits:
    print(fruit)

# With index
for i, fruit in enumerate(fruits):
    print(f"{i}: {fruit}")

# ── Range ────────────────────────────────────────────────────────────
# range(stop), range(start, stop), range(start, stop, step)
for i in range(5):          # 0, 1, 2, 3, 4
    print(i, end=" ")

for i in range(0, 10, 2):   # 0, 2, 4, 6, 8
    print(i, end=" ")

# ── While Loop ───────────────────────────────────────────────────────
count = 0
while count < 5:
    print(count, end=" ")
    count += 1

# ── Break, Continue, Else ───────────────────────────────────────────
# break — exit loop early
# continue — skip to next iteration
# else — runs if loop completes without break

for n in range(2, 10):
    for i in range(2, n):
        if n % i == 0:
            break
    else:  # This runs if the for loop didn't break
        print(f"{n} is prime", end=" ")

# ── Nested Loops ─────────────────────────────────────────────────────
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
flat = []
for row in matrix:
    for val in row:
        flat.append(val)

# ── Zip and Unzip ────────────────────────────────────────────────────
names = ["Alice", "Bob", "Charlie"]
scores = [95, 87, 92]
paired = list(zip(names, scores))  # [('Alice', 95), ...]

unzipped_names, unzipped_scores = zip(*paired)

# ── Loop Else ────────────────────────────────────────────────────────
# else clause runs when loop finishes normally (no break)
target = 7
for n in range(2, 10):
    if n == target:
        print(f"Found {n}")
        break
else:
    print(f"{target} not found")

# ── Iterating Dicts ─────────────────────────────────────────────────
person = {"name": "Alice", "age": 30, "city": "NYC"}

for key in person:              # keys (default)
    print(key)

for key, value in person.items():  # key-value pairs
    print(f"{key}: {value}")

for value in person.values():      # values only
    print(value)

# ── List Comprehension ───────────────────────────────────────────────
squares = [x**2 for x in range(10)]
evens = [x for x in range(20) if x % 2 == 0]
matrix_transpose = [[row[i] for row in matrix] for i in range(3)]

# ── Dict Comprehension ───────────────────────────────────────────────
word_lengths = {word: len(word) for word in ["hello", "world"]}
squares_dict = {x: x**2 for x in range(5)}

# ── Set Comprehension ────────────────────────────────────────────────
unique_lengths = {len(word) for word in ["hello", "world", "hi"]}

# ── Generator Expression ─────────────────────────────────────────────
# Uses () instead of [] — lazy evaluation
sum_of_squares = sum(x**2 for x in range(1000000))
