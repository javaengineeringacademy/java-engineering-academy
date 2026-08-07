# Module 07: Functional Programming

> "Functional programming in Python isn't about rejecting classes. It's about choosing the right tool for the job."

---

## First-Class Functions

In Python, functions are first-class citizens — they can be assigned to variables, passed as arguments, and returned from other functions.

```python
# Functions as variables
def greet(name: str) -> str:
    return f"Hello, {name}!"

say_hello = greet  # Assign function to variable
print(say_hello("Alice"))  # "Hello, Alice!"

# Functions as arguments
def apply(func, value):
    return func(value)

print(apply(str.upper, "hello"))  # "HELLO"

# Functions as return values
def create_multiplier(factor: int):
    def multiply(x: int) -> int:
        return x * factor
    return multiply

double = create_multiplier(2)
print(double(5))  # 10
```

**Key insight:** First-class functions are the foundation of functional programming in Python. Everything else builds on this.

---

## Lambda Functions

Anonymous functions for short, throwaway operations.

```python
# Lambda syntax
square = lambda x: x ** 2
add = lambda a, b: a + b

# Practical uses
users = [{"name": "Alice", "age": 30}, {"name": "Bob", "age": 25}]
sorted_users = sorted(users, key=lambda u: u["age"])

# Lambda is best for short, inline operations
# BAD: Lambda with complex logic
process = lambda x: x ** 2 + 3 * x - 5  # Hard to read

# GOOD: Use a def for anything non-trivial
def process(x: int) -> int:
    return x ** 2 + 3 * x - 5
```

**When to use lambda:**
- `sorted(key=lambda x: ...)`
- `map(lambda x: ...)`
- `filter(lambda x: ...)`
- Short callbacks

**When NOT to use lambda:**
- Multi-line logic
- Need docstrings
- Assigning to a variable (use `def` instead)

---

## map, filter, reduce

### map — Transform Every Element

```python
# Basic usage
numbers = [1, 2, 3, 4, 5]
squared = list(map(lambda x: x ** 2, numbers))
# [1, 4, 9, 16, 25]

# With built-in functions
strings = ["hello", "world"]
upper = list(map(str.upper, strings))
# ["HELLO", "WORLD"]

# Multiple iterables
a = [1, 2, 3]
b = [10, 20, 30]
sums = list(map(lambda x, y: x + y, a, b))
# [11, 22, 33]
```

### filter — Keep Elements That Match

```python
# Basic usage
numbers = [1, 2, 3, 4, 5, 6]
evens = list(filter(lambda x: x % 2 == 0, numbers))
# [2, 4, 6]

# With None — remove falsy values
data = [0, 1, "", "hello", None, True]
cleaned = list(filter(None, data))
# [1, "hello", True]

# Custom predicate
def is_adult(user: dict) -> bool:
    return user["age"] >= 18

adults = list(filter(is_adult, users))
```

### reduce — Accumulate Into Single Value

```python
from functools import reduce

numbers = [1, 2, 3, 4, 5]
total = reduce(lambda acc, x: acc + x, numbers, 0)
# 15

# Practical: flatten nested lists
nested = [[1, 2], [3, 4], [5, 6]]
flat = reduce(lambda acc, lst: acc + lst, nested, [])
# [1, 2, 3, 4, 5, 6]

# Find maximum
max_val = reduce(lambda a, b: a if a > b else b, numbers)
```

### List Comprehensions vs map/filter

```python
# map + filter
result = list(map(lambda x: x ** 2, filter(lambda x: x > 2, numbers)))

# List comprehension — usually more Pythonic
result = [x ** 2 for x in numbers if x > 2]

# Generator expression for memory efficiency
result = (x ** 2 for x in numbers if x > 2)
```

**Rule of thumb:** Use comprehensions for simple cases. Use `map`/`filter` when the transformation function is already defined.

---

## functools

### partial — Fix Some Arguments

```python
from functools import partial

def power(base: int, exponent: int) -> int:
    return base ** exponent

square = partial(power, exponent=2)
cube = partial(power, exponent=3)

print(square(5))  # 25
print(cube(5))    # 125

# Practical: create specialized functions
def connect(host: str, port: int, protocol: str) -> str:
    return f"{protocol}://{host}:{port}"

connect_local = partial(connect, host="localhost", port=8080, protocol="http")
print(connect_local())  # "http://localhost:8080"
```

### lru_cache — Memoization

```python
from functools import lru_cache, cache

@lru_cache(maxsize=128)
def fibonacci(n: int) -> int:
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

print(fibonacci(100))  # Fast! Cached results

# Python 3.9+ — unbounded cache
@cache
def expensive_computation(x: int) -> int:
    return sum(i ** 2 for i in range(x))

# Cache management
print(fibonacci.cache_info())  # hits, misses, size
fibonacci.cache_clear()        # Clear cache
```

### wraps — Preserve Function Metadata

```python
from functools import wraps

def log_calls(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        print(f"Calling {func.__name__} with {args}, {kwargs}")
        result = func(*args, **kwargs)
        print(f"{func.__name__} returned {result}")
        return result
    return wrapper

@log_calls
def add(a: int, b: int) -> int:
    """Add two numbers."""
    return a + b

print(add.__name__)  # "add" (not "wrapper")
print(add.__doc__)   # "Add two numbers."
```

### Other Useful functools

```python
from functools import reduce, total_ordering

# total_ordering — define __eq__ and one comparison, get the rest
@total_ordering
class Student:
    def __init__(self, name: str, gpa: float):
        self.name = name
        self.gpa = gpa

    def __eq__(self, other):
        return self.gpa == other.gpa

    def __lt__(self, other):
        return self.gpa < other.gpa

# Now you get __le__, __gt__, __ge__ for free
```

---

## itertools

### chain — Combine Iterables

```python
from itertools import chain

# Flatten multiple lists
list1 = [1, 2]
list2 = [3, 4]
list3 = [5, 6]
combined = list(chain(list1, list2, list3))
# [1, 2, 3, 4, 5, 6]

# Chain from iterable
lists = [[1, 2], [3, 4], [5, 6]]
flat = list(chain.from_iterable(lists))
# [1, 2, 3, 4, 5, 6]
```

### islice — Slice Iterators

```python
from itertools import islice

# Get first 5 items from a generator
def infinite_counter():
    n = 0
    while True:
        yield n
        n += 1

first_five = list(islice(infinite_counter(), 5))
# [0, 1, 2, 3, 4]

# Skip and take
data = range(100)
result = list(islice(data, 10, 20))  # items 10-19
```

### groupby — Group Consecutive Elements

```python
from itertools import groupby

# Group sorted data
data = [
    {"type": "fruit", "name": "apple"},
    {"type": "fruit", "name": "banana"},
    {"type": "vegetable", "name": "carrot"},
    {"type": "vegetable", "name": "daikon"},
]

# IMPORTANT: Data MUST be sorted by key first
data.sort(key=lambda x: x["type"])

for type_name, items in groupby(data, key=lambda x: x["type"]):
    print(f"{type_name}: {[i['name'] for i in items]}")
# fruit: ['apple', 'banana']
# vegetable: ['carrot', 'daikon']
```

### Other Useful itertools

```python
from itertools import product, permutations, combinations, zip_longest

# Cartesian product
suits = ["hearts", "diamonds"]
ranks = ["A", "K"]
cards = list(product(suits, ranks))
# [('hearts', 'A'), ('hearts', 'K'), ('diamonds', 'A'), ('diamonds', 'K')]

# Permutations
perms = list(permutations("ABC", 2))
# [('A', 'B'), ('A', 'C'), ('B', 'A'), ('B', 'C'), ('C', 'A'), ('C', 'B')]

# Combinations
combs = list(combinations("ABCD", 2))
# [('A', 'B'), ('A', 'C'), ('A', 'D'), ('B', 'C'), ('B', 'D'), ('C', 'D')]

# Zip with fill value
a = [1, 2, 3]
b = ["a", "b"]
result = list(zip_longest(a, b, fillvalue=None))
# [(1, 'a'), (2, 'b'), (3, None)]
```

---

## Immutability

Python doesn't enforce immutability, but you can embrace the pattern.

```python
# Immutable types
x = 42         # int — immutable
name = "Alice" # str — immutable
point = (1, 2) # tuple — immutable
frozen = frozenset([1, 2, 3])  # frozenset — immutable

# Mutable state with functional patterns
def update_user(user: dict, **changes) -> dict:
    """Return new dict without mutating original."""
    return {**user, **changes}

original = {"name": "Alice", "age": 30}
updated = update_user(original, age=31)

# Named tuples for immutable records
from typing import NamedTuple

class Point(NamedTuple):
    x: float
    y: float

p1 = Point(1, 2)
# p1.x = 3  # Error! Immutable

# Dataclasses with frozen=True
from dataclasses import dataclass

@dataclass(frozen=True)
class Config:
    host: str
    port: int
    debug: bool = False
```

**Why immutability matters:**
- Safer concurrent access
- Predictable behavior
- Easier debugging
- Hashable (can be dict keys, set members)

---

## When to Use Functional Style

**Strongly recommended for:**
- Data transformations (ETL pipelines)
- Filtering and mapping collections
- Event-driven architectures
- Composition-heavy code
- Concurrent/async operations (fewer side effects)
- Testing (pure functions are trivially testable)

```python
# ETL pipeline — functional style excels
def extract(source: str) -> list[dict]:
    ...

def transform(records: list[dict]) -> list[dict]:
    ...

def load(records: list[dict], target: str) -> None:
    ...

# Clear data flow
pipeline = compose(load, transform, extract)
pipeline("source.csv")
```

---

## When NOT to Use Functional Style

**Avoid when:**
- Code needs mutable state (counters, accumulators with side effects)
- Object-oriented models are more natural (domain entities)
- Performance critical — comprehensions often outperform `map`/`filter`
- Team isn't familiar with functional patterns
- Heavy I/O operations (immutability overhead without benefit)

```python
# BAD: Forcing functional style on inherently stateful code
def process_orders(orders, total=0):
    if not orders:
        return total
    return process_orders(orders[1:], total + orders[0]["amount"])

# GOOD: Simple loop
def process_orders(orders):
    return sum(order["amount"] for order in orders)

# BAD: Overusing reduce for simple operations
total = reduce(lambda a, b: a + b, amounts)

# GOOD: Built-in sum
total = sum(amounts)
```

---

## Production Checklist

- [ ] **Use comprehensions over map/filter for simple cases** — More Pythonic and usually faster
- [ ] **Memoize expensive functions** — Use `@lru_cache` or `@cache`
- [ ] **Always use `@wraps` in decorators** — Preserve function metadata
- [ ] **Prefer `itertools` over manual iteration** — Well-optimized, readable
- [ ] **Use `partial` for callback specialization** — Cleaner than lambdas
- [ ] **Make functions pure when possible** — Same input → same output, no side effects
- [ ] **Document side effects** — If a function mutates state, say so
- [ ] **Consider generator expressions** — Memory efficient for large datasets
- [ ] **Test pure functions easily** — No mocking needed
- [ ] **Profile before optimizing** — Functional vs imperative may not matter

```python
# Good: Clear data pipeline
def process_data(raw: list[dict]) -> list[dict]:
    return [
        normalize(record)
        for record in filter(None, map(validate, raw))
        if record is not None
    ]
```

---

## Maturity Levels

| Level | What It Looks Like | Indicators |
|-------|-------------------|------------|
| **Beginner** | Basic loops only | `for x in items: result.append(f(x))` |
| **Intermediate** | List comprehensions | `[f(x) for x in items if condition]` |
| **Advanced** | map/filter/reduce, itertools | Functional pipeline, composition |
| **Expert** | Custom decorators, monads | Functor patterns, pure function architecture |
| **Master** | Appropriate use | Knows when functional helps AND when it hurts |

### Progression Path

1. **Start:** Use list comprehensions instead of loops
2. **Then:** Learn `map`, `filter`, `reduce` for simple cases
3. **Then:** Explore `itertools` for iteration patterns
4. **Then:** Master `functools` (partial, lru_cache, wraps)
5. **Finally:** Understand when NOT to be functional

---

## Common Myths

**Myth: "Functional programming is always better than OOP"**
> Reality: Different paradigms solve different problems. Use functional for data transformations, OOP for domain modeling. Python supports both for a reason.

**Myth: "List comprehensions are always faster than map/filter"**
> Reality: It depends. `map` can be faster with built-in functions (like `str.upper`), comprehensions are faster with complex expressions. Profile first.

**Myth: "Pure functions are always better"**
> Reality: Real programs need side effects (I/O, database writes). The goal is to *minimize* and *isolate* side effects, not eliminate them.

**Myth: "Functional means no classes"**
> Reality: Functional programming uses data classes, named tuples, and dataclasses all the time. It's about behavior, not data representation.

**Myth: "reduce is always better than loops"**
> Reality: `sum()`, `min()`, `max()` are clearer than `reduce` for simple accumulation. `reduce` shines when the accumulation logic is complex.

---

## One-Minute Revision

- **First-class functions:** Assign to variables, pass as args, return from functions
- **Lambda:** Anonymous functions — use for short callbacks, complex logic use `def`
- **map:** Transform every element — `map(func, iterable)`
- **filter:** Keep matching elements — `filter(predicate, iterable)`
- **reduce:** Accumulate into single value — `reduce(func, iterable, initial)`
- **Comprehensions:** `[f(x) for x in items if condition]` — usually preferred
- **functools.partial:** Fix arguments, create specialized functions
- **functools.lru_cache:** Memoize expensive function calls
- **functools.wraps:** Preserve metadata in decorators
- **itertools:** `chain`, `islice`, `groupby`, `product`, `permutations`
- **Immutability:** Prefer tuples, frozensets, frozen dataclasses when possible
- **Use functional for:** Data pipelines, transformations, pure computations
- **Avoid functional for:** Heavy I/O, mutable state, team unfamiliar with patterns
