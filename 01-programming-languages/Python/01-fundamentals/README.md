# Python Fundamentals

## Why Fundamentals Matter

Every Python application — from a simple CLI tool to a distributed microservice — depends on the same core building blocks: variables that store data, operators that transform it, control flow that directs it, functions that encapsulate it, and collections that organize it. Mastering these fundamentals is not about memorizing syntax; it's about internalizing the mental models that let you reason about what your code is actually doing. When you understand how Python stores variables, how closures capture scope, and why `list.sort()` is faster than `sorted()` for large lists, you stop writing code that merely works and start writing code that is predictable, performant, and maintainable. Without this foundation, even experienced developers fall into traps — mutable defaults causing silent bugs, shallow copies producing unexpected shared state, or f-strings being used where `str.format()` would be clearer.

## What You'll Learn

By the end of this module, you'll be able to:

- Declare variables and understand Python's dynamic typing and name binding
- Use arithmetic, comparison, logical, and bitwise operators correctly
- Write clean control flow with `if`/`elif`/`else`, `for`/`while`, and comprehensions
- Define functions with positional, keyword, `*args`, `**kwargs`, and default arguments
- Manipulate lists, tuples, dicts, and sets with awareness of performance characteristics
- Format and process strings using f-strings, methods, and slicing
- Read from and write to files safely using context managers and error handling
- Recognize and avoid the most common beginner and intermediate mistakes

## Core Concepts

### 1. Variables and Names

In Python, variables are **names bound to objects** — they don't have types; the objects they reference do.

```python
x = 10          # x is a name bound to an int object
y = x           # y is bound to the SAME int object (not a copy)
x = 20          # x now points to a new int; y still points to 10

# Multiple assignment
a, b, c = 1, 2, 3
a, b = b, a     # Swap without a temporary variable

# Type is on the object, not the variable
print(type(x))  # <class 'int'>
```

**Internal working:** Python uses a name table (namespace) for each scope. The `id()` function reveals the memory address of an object — two variables with the same `id()` point to the same object.

```python
a = [1, 2, 3]
b = a
print(id(a) == id(b))  # True — same list in memory

b = b.copy()
print(id(a) == id(b))  # False — different list objects
```

**Common mistake:** Assuming assignment copies the value. Assignment in Python copies the *reference*, not the object.

### 2. Operators

Python provides arithmetic, comparison, logical, bitwise, identity, and membership operators.

```python
# Arithmetic
print(7 // 2)   # 3 (floor division)
print(7 % 2)    # 1 (modulo)
print(2 ** 10)  # 1024 (exponentiation)

# Comparison — chainable
x = 5
print(1 < x < 10)  # True

# Logical — short-circuit
a = [] or [1]  # [1] — stops at first truthy
b = [] and [1] # []  — stops at first falsy

# Identity vs Equality
a = [1, 2, 3]
b = [1, 2, 3]
print(a == b)   # True  (equal values)
print(a is b)   # False (different objects)
print(a is a)   # True  (same object)
```

**Common mistake:** Using `is` to compare values. Always use `==` for value comparison; reserve `is` for `None`, `True`, `False`, and sentinel checks.

**Gotcha with `or`:** The `or` operator returns the *first truthy value*, not necessarily `True`.

```python
result = "" or "fallback"  # "fallback"
result = 0 or 42           # 42
```

### 3. Control Flow

```python
# if/elif/else
score = 85
if score >= 90:
    grade = "A"
elif score >= 80:
    grade = "B"
else:
    grade = "C"

# for loop with enumerate
items = ["a", "b", "c"]
for index, value in enumerate(items):
    print(f"{index}: {value}")

# while with else
n = 5
while n > 0:
    n -= 1
else:
    print("Loop completed normally")  # Runs when condition is False

# Ternary expression
status = "active" if user.is_valid else "inactive"

# List comprehension (preferred over map/filter for readability)
squares = [x**2 for x in range(10) if x % 2 == 0]

# Dictionary comprehension
word_lengths = {word: len(word) for word in ["hello", "world"]}

# Set comprehension
unique_chars = {c for c in "mississippi"}
```

**Production pattern:** Use `for...else` to detect whether a loop found what it was looking for:

```python
def find_item(items, target):
    for item in items:
        if item == target:
            return item
    return None  # Only reached if loop didn't return
```

**Common mistake:** Mutating a list while iterating over it. Use a list comprehension or copy the list first.

```python
# Bad — skips elements
for item in items:
    if item.should_remove:
        items.remove(item)

# Good
items = [item for item in items if not item.should_remove]
```

### 4. Functions

```python
# Basic function with type hints
def greet(name: str, greeting: str = "Hello") -> str:
    return f"{greeting}, {name}!"

# *args and **kwargs
def log_message(level, *args, **kwargs):
    timestamp = kwargs.get("timestamp", "now")
    print(f"[{timestamp}] {level}: {' '.join(args)}")

log_message("INFO", "Server", "started", timestamp="2026-01-01")

# Keyword-only arguments (after *)
def create_user(name, *, email, role="user"):
    return {"name": name, "email": email, "role": role}

# Lambda (use sparingly)
square = lambda x: x ** 2
```

**Mutable default argument gotcha:**

```python
# BAD — mutable default is shared across calls
def append_to(item, lst=[]):
    lst.append(item)
    return lst

print(append_to(1))  # [1]
print(append_to(2))  # [1, 2] — SURPRISE!

# GOOD — use None as sentinel
def append_to(item, lst=None):
    if lst is None:
        lst = []
    lst.append(item)
    return lst
```

**Scope rules (LEGB):** Python resolves names in this order: **L**ocal → **E**nclosing → **G**lobal → **B**uilt-in.

```python
x = "global"

def outer():
    x = "enclosing"
    
    def inner():
        x = "local"
        print(x)  # "local"
    
    inner()
    print(x)  # "enclosing"

outer()
print(x)  # "global"
```

**Best practice:** Keep functions small, pure (same input → same output), and avoid side effects. Use type hints consistently.

### 5. Collections

```python
# List — ordered, mutable, O(1) index access, O(n) membership test
numbers = [1, 2, 3, 4, 5]
numbers.append(6)
numbers.pop()           # Remove last element
numbers[0]              # O(1) access
99 in numbers           # O(n) membership test

# Tuple — ordered, immutable, faster than lists
point = (3, 4)
# point[0] = 5  # TypeError

# Named tuple — lightweight immutable record
from collections import namedtuple
Point = namedtuple("Point", ["x", "y"])
p = Point(3, 4)
print(p.x, p.y)

# Dict — key-value pairs, O(1) average lookup
config = {"host": "localhost", "port": 8080}
config.setdefault("timeout", 30)  # Set only if key missing

# Set — unordered, unique elements, O(1) membership test
seen = {1, 2, 3}
seen.add(4)
1 in seen  # O(1) — much faster than list for membership tests
```

**Performance comparison:**

| Operation | list | dict | set |
|-----------|------|------|-----|
| Lookup by index/key | O(1) | O(1) avg | N/A |
| Membership test | O(n) | O(1) avg | O(1) avg |
| Insert | O(1) amortized | O(1) avg | O(1) avg |
| Delete | O(n) | O(1) avg | O(1) avg |

**Common mistake:** Using a list when you need fast membership tests. Use a `set` — the difference between `item in list` (O(n)) and `item in set` (O(1)) is enormous for large collections.

**Production pattern:** Use `collections.defaultdict` and `collections.Counter` to avoid boilerplate:

```python
from collections import Counter

words = ["apple", "banana", "apple", "cherry", "apple"]
counts = Counter(words)  # {'apple': 3, 'banana': 1, 'cherry': 1}
```

### 6. Strings

Strings in Python are **immutable sequences** of Unicode characters.

```python
# f-strings (preferred since Python 3.6)
name = "Alice"
print(f"Hello, {name}!")           # Hello, Alice!
print(f"{3.14159:.2f}")            # 3.14
print(f"{'centered':^20}")         #       centered       
print(f"{'left':<20}end")          # left                end

# String methods
text = "  Hello, World!  "
text.strip()           # "Hello, World!"
text.lower()           # "  hello, world!  "
"hello world".title()  # "Hello World"
"a,b,c".split(",")    # ["a", "b", "c"]
"-".join(["a", "b"])  # "a-b"

# Slicing
s = "Python"
s[0:3]     # "Pyt"
s[::-1]    # "nohtyP" (reversed)
s[-2:]     # "on"
```

**Formatting comparison:**

```python
name, age = "Bob", 30

# f-string (preferred)
f"{name} is {age} years old"

# str.format()
"{} is {} years old".format(name, age)

# %-formatting (legacy)
"%s is %d years old" % (name, age)
```

**Best practice:** Always use f-strings for readability. Use `str.format()` only when you need format specification at runtime. Avoid `%` formatting in new code.

### 7. File Handling

```python
# Reading a file — context manager ensures file is closed
with open("data.txt", "r", encoding="utf-8") as f:
    content = f.read()

# Reading line by line (memory efficient for large files)
with open("data.txt", "r", encoding="utf-8") as f:
    for line in f:
        print(line.strip())

# Writing
with open("output.txt", "w", encoding="utf-8") as f:
    f.write("Hello, World!\n")

# Append mode
with open("log.txt", "a", encoding="utf-8") as f:
    f.write(f"Entry at {time.time()}\n")

# Reading JSON
import json

with open("config.json", "r") as f:
    config = json.load(f)

# Writing JSON
with open("output.json", "w") as f:
    json.dump({"key": "value"}, f, indent=2)
```

**Error handling pattern:**

```python
from pathlib import Path

def read_file_safe(filepath: str) -> str | None:
    """Read file with proper error handling."""
    try:
        path = Path(filepath)
        if not path.exists():
            raise FileNotFoundError(f"File not found: {filepath}")
        with open(path, "r", encoding="utf-8") as f:
            return f.read()
    except PermissionError:
        print(f"Permission denied: {filepath}")
        return None
    except UnicodeDecodeError:
        print(f"Cannot decode file: {filepath}")
        return None
    except Exception as e:
        print(f"Unexpected error reading {filepath}: {e}")
        return None
```

**Common mistake:** Forgetting to specify `encoding="utf-8"`. On Windows, the default encoding varies by locale, which can corrupt data silently.

## Interview Questions

### Q1: What is the difference between a list and a tuple?
**Answer:** Lists are mutable (can be modified), tuples are immutable (fixed). Lists use more memory, tuples are faster and can be dictionary keys.

### Q2: Explain mutable default argument gotcha.
**Answer:** Default arguments are evaluated once at function definition. If mutable (list, dict), they're shared across calls. Use None as default and create inside function.

### Q3: What is LEGB scope?
**Answer:** Local → Enclosing → Global → Built-in. Python looks up variables in this order. Understanding LEGB prevents NameError and unexpected behavior.

### Q4: What is the difference between `*args` and `**kwargs`?
**Answer:** `*args` collects positional arguments as tuple, `**kwargs` collects keyword arguments as dict. They can be combined in that order.

### Q5: How do f-strings work internally?
**Answer:** f-strings are compiled to bytecode that calls __format__ on each expression. They're faster than .format() and % formatting.

## Production Checklist

### ✅ Before using fundamentals in production:

☐ I know mutable default argument gotcha and use `None` sentinel  
☐ I know shallow vs deep copy and when each is needed  
☐ I know scope rules (LEGB) and avoid global state  
☐ I know f-string best practices and formatting options  
☐ I know the time/space complexity of built-in collections  
☐ I know common mistakes (mutable default args, shallow vs deep copy, late binding closures)  
☐ I know alternatives (numpy arrays vs lists, named tuples vs dicts)  
☐ I know limitations (GIL, dynamic typing overhead)  
☐ I know how to debug it (pdb, logging, traceback module)  
☐ I've tested with realistic data volume  
☐ I've profiled for performance  
☐ I always specify `encoding` when opening files  
☐ I use context managers (`with`) for all file operations  

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax for variables, control flow, functions
- Can write working code for simple tasks
- Understands that `=` assigns, not copies

### Level 2: Understands
- Knows LEGB scope and variable lifetime
- Knows time/space complexity of collections
- Understands mutable default argument trap
- Uses f-strings and list comprehensions idiomatically

### Level 3: Deep Knowledge
- Knows CPython's variable storage and reference counting
- Can explain shallow vs deep copy at the object level
- Understands closure late binding and how to fix it
- Knows when to use `is` vs `==`, `or` vs `if`

### Level 4: Expert
- Can optimize for performance using appropriate data structures
- Can debug reference leaks and unintended shared state
- Knows when to use generators over lists for memory efficiency
- Understands GIL implications and uses multiprocessing/threading accordingly

### Level 5: Master
- Can design custom collection classes with appropriate dunder methods
- Can teach others and write style guides
- Knows CPython internals well enough to predict performance characteristics
- Can contribute to Python language discussions (PEPs)

## Common Myths

### ❌ Myth 1: Python is slow because it's dynamic
**Reality:** Python's dynamic typing adds overhead for interpreted loops, but most real-world bottlenecks come from I/O, network calls, or algorithmic choices — not the language itself. C extensions (NumPy, pandas), PyPy JIT compilation, and async I/O eliminate most performance concerns. Profile before optimizing.

### ❌ Myth 2: Lists and arrays are the same
**Reality:** Python lists are heterogeneous collections of object references; NumPy arrays are homogeneous, contiguous blocks of raw data. NumPy arrays are orders of magnitude faster for numerical operations because they avoid per-element Python object overhead. Use lists for mixed-type collections; use arrays for numerical computation.

### ❌ Myth 3: Global variables are always bad
**Reality:** Module-level constants (e.g., `MAX_RETRIES = 3`) are perfectly acceptable and often preferred over passing constants through function arguments. The real issue is mutable global state shared across threads without synchronization. Immutable globals are safe; mutable globals require careful management.

### ❌ Myth 4: More code means better code
**Reality:** Concise, readable Python code is preferred. List comprehensions, f-strings, and built-in functions (like `sum`, `any`, `all`) reduce boilerplate while improving clarity. Write for the reader, not the compiler.

## Common Mistakes

### Mistake 1: Mutable Default Arguments

```python
# WRONG — default list persists across calls
def add_item(item, items=[]):
    items.append(item)
    return items

# RIGHT — new list created each call
def add_item(item, items=None):
    if items is None:
        items = []
    items.append(item)
    return items
```

### Mistake 2: Shallow Copy Surprise

```python
import copy

original = [[1, 2], [3, 4]]
shallow = original.copy()       # or list(original) or original[:]
deep = copy.deepcopy(original)

shallow[0][0] = 99
print(original[0][0])  # 99 — original affected!

deep[0][0] = 99
print(original[0][0])  # Still 99 — but deep is independent now
```

### Mistake 3: Late Binding Closures

```python
# WRONG — all lambdas share the same 'i'
functions = [lambda: i for i in range(5)]
print([f() for f in functions])  # [4, 4, 4, 4, 4]

# RIGHT — capture 'i' at definition time
functions = [lambda i=i: i for i in range(5)]
print([f() for f in functions])  # [0, 1, 2, 3, 4]
```

### Mistake 4: Comparing with `is` Instead of `==`

```python
# WRONG
if x is 10:  # SyntaxWarning in Python 3.8+
    ...

# RIGHT
if x == 10:
    ...
```

### Mistake 5: Not Using Context Managers for Files

```python
# WRONG — file may not close on exception
f = open("data.txt", "r")
data = f.read()
f.close()

# RIGHT — guaranteed to close
with open("data.txt", "r") as f:
    data = f.read()
```

## Performance Considerations

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| List index access | O(1) | |
| List append | O(1) amortized | May trigger reallocation |
| List insert(0, x) | O(n) | Use `collections.deque` for left operations |
| List membership | O(n) | Use `set` for O(1) lookups |
| Dict lookup | O(1) average | O(n) worst case (rare) |
| Set membership | O(1) average | |
| String concatenation | O(n²) | Use `''.join(parts)` in loops |
| f-string | O(n) | Faster than `.format()` and `%` |

**Optimization tips:**

- Use generators instead of lists when you only need to iterate once (saves memory)
- Pre-compute values outside loops rather than recalculating each iteration
- Use `collections.deque` when you need frequent insertions/deletions at both ends
- Use `join()` to build strings in loops instead of `+=`
- Profile with `cProfile` or `timeit` before optimizing

## Debugging Tips

```python
# 1. Use pdb for interactive debugging
import pdb; pdb.set_trace()

# Or in Python 3.7+, use breakpoint()
breakpoint()

# 2. Inspect variable types and values
print(type(x), repr(x))

# 3. Check scope with dir() and locals()
print(locals())  # Current local scope
print(globals()) # Current global scope

# 4. Trace reference identity
print(f"id(x)={id(x)}, id(y)={id(y)}")

# 5. Check if a variable exists
if 'my_var' in dir():
    print("exists")

# 6. Use traceback for exception info
import traceback
try:
    1 / 0
except Exception:
    traceback.print_exc()
```

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Core building blocks of Python programming |
| Variable model | Names bound to objects (reference semantics) |
| Scope resolution | LEGB (Local, Enclosing, Global, Built-in) |
| List complexity | O(1) index, O(n) search, O(1) amortized append |
| Dict/Set complexity | O(1) average lookup, insert, and delete |
| String type | Immutable sequence of Unicode characters |
| File safety | Always use `with` context manager |
| Default args | Never use mutable defaults; use `None` sentinel |
| String building | Use `''.join()` in loops, f-strings for formatting |
| Copy behavior | Assignment copies reference; `.copy()` is shallow |
| Best Alternative | NumPy for numeric, TypedDict for structured dicts |
| When to Use | Rapid prototyping, scripting, data pipelines, web backends |
| When to Avoid | Real-time systems, heavy numeric computation without NumPy |

## References

- [Python Tutorial: Data Structures](https://docs.python.org/3/tutorial/datastructures.html)
- [PEP 8: Style Guide for Python Code](https://peps.python.org/pep-0008/)
- [Python Docs: Built-in Types](https://docs.python.org/3/library/stdtypes.html)
- [PEP 484: Type Hints](https://peps.python.org/pep-0484/)
- [Python Docs: `collections` module](https://docs.python.org/3/library/collections.html)
- [Python Docs: `pathlib` module](https://docs.python.org/3/library/pathlib.html)
- [Effective Python by Brett Slatkin](https://effectivepython.com/)

## Version Validation

- Verified against: Python 3.12+
- Syntax features used: f-strings (3.6+), walrus operator examples (3.8+), `match` statement awareness (3.10+)
- Recommended: Python 3.11+ for best error messages and performance improvements
