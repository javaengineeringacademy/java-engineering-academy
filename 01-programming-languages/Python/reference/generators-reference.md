# Python Generators Reference

## What are Python Generators?

Generators are functions that use the `yield` keyword to produce a sequence of values lazily. They are memory-efficient because they generate values on-demand rather than storing them all in memory.

## Why does Python Generators matter?

Understanding generators helps you:
- Process large datasets efficiently
- Create infinite sequences
- Implement coroutine-like behavior
- Write more Pythonic code

---

## 1. Basic Generators

```python
# Simple generator
def countdown(n):
    while n > 0:
        yield n
        n -= 1

# Use generator
for i in countdown(5):
    print(i)  # 5, 4, 3, 2, 1

# Generator expression
squares = (x**2 for x in range(10))
print(list(squares))  # [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
```

---

## 2. Generator Protocol

```python
# Generator object
def my_generator():
    yield 1
    yield 2
    yield 3

gen = my_generator()

# Next value
print(next(gen))  # 1
print(next(gen))  # 2
print(next(gen))  # 3
# next(gen)  # StopIteration

# Iterate
for value in my_generator():
    print(value)

# Convert to list
print(list(my_generator()))  # [1, 2, 3]
```

---

## 3. Generator Expressions

```python
# List comprehension vs generator expression
# List comprehension: creates entire list in memory
squares_list = [x**2 for x in range(1000000)]

# Generator expression: generates values on-demand
squares_gen = (x**2 for x in range(1000000))

# Memory usage
import sys
print(sys.getsizeof(squares_list))  # ~8 MB
print(sys.getsizeof(squares_gen))   # ~200 bytes

# Use in functions
print(sum(x**2 for x in range(10)))  # 285
print(max(x**2 for x in range(10)))  # 81
```

---

## 4. yield

```python
# Basic yield
def simple_generator():
    yield 1
    yield 2
    yield 3

# Yield from
def delegated_generator():
    yield from [1, 2, 3]
    yield from 'abc'

print(list(delegated_generator()))  # [1, 2, 3, 'a', 'b', 'c']
```

---

## 5. send()

```python
# Send values to generator
def accumulator():
    total = 0
    while True:
        value = yield total
        if value is None:
            break
        total += value

# Create generator
acc = accumulator()
next(acc)  # Prime the generator
print(acc.send(1))   # 1
print(acc.send(2))   # 3
print(acc.send(3))   # 6

# Close generator
acc.close()
```

---

## 6. throw()

```python
# Throw exception into generator
def controlled_generator():
    try:
        while True:
            value = yield
            print(f"Received: {value}")
    except ValueError:
        print("ValueError caught!")
    finally:
        print("Generator closed")

gen = controlled_generator()
next(gen)  # Prime the generator
gen.send(1)  # Received: 1
gen.send(2)  # Received: 2
gen.throw(ValueError)  # ValueError caught! Generator closed
```

---

## 7. yield from

```python
# Delegate to sub-generator
def sub_generator():
    yield 1
    yield 2
    yield 3

def main_generator():
    yield from sub_generator()
    yield 4
    yield 5

print(list(main_generator()))  # [1, 2, 3, 4, 5]

# With return value
def sub_generator():
    yield 1
    yield 2
    return 3

def main_generator():
    result = yield from sub_generator()
    print(f"Sub-generator returned: {result}")
    yield 4

list(main_generator())  # Prints: Sub-generator returned: 3
```

---

## 8. Generator Pipelines

```python
# Pipeline of generators
def read_data(filename):
    with open(filename) as f:
        for line in f:
            yield line.strip()

def filter_comments(lines):
    for line in lines:
        if not line.startswith('#'):
            yield line

def parse_csv(lines):
    for line in lines:
        yield line.split(',')

# Pipeline
data = read_data('data.txt')
filtered = filter_comments(data)
parsed = parse_csv(filtered)

for row in parsed:
    print(row)
```

---

## 9. Infinite Generators

```python
# Infinite Fibonacci
def fibonacci():
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b

# Take first 10
fib = fibonacci()
for _ in range(10):
    print(next(fib))

# Infinite counter
def counter(start=0):
    n = start
    while True:
        yield n
        n += 1
```

---

## 10. itertools Patterns

```python
import itertools

# Chain generators
def gen1():
    yield 1
    yield 2

def gen2():
    yield 3
    yield 4

for value in itertools.chain(gen1(), gen2()):
    print(value)  # 1, 2, 3, 4

# islice
def infinite():
    n = 0
    while True:
        yield n
        n += 1

for value in itertools.islice(infinite(), 5):
    print(value)  # 0, 1, 2, 3, 4

# takewhile
def natural():
    n = 1
    while True:
        yield n
        n += 1

for value in itertools.takewhile(lambda x: x < 5, natural()):
    print(value)  # 1, 2, 3, 4
```

---

## One-Minute Revision Table

| Concept | Description | Example |
|---------|-------------|---------|
| **yield** | Produce value | `yield value` |
| **next()** | Get next value | `next(gen)` |
| **send()** | Send value to generator | `gen.send(value)` |
| **throw()** | Throw exception | `gen.throw(Exception)` |
| **close()** | Close generator | `gen.close()` |
| **yield from** | Delegate to sub-generator | `yield from gen()` |
| **Generator expression** | Lazy list comprehension | `(x for x in range(10))` |
| **Generator pipeline** | Chain of generators | Multiple yield from |

---

## Common Mistakes

### 1. Forgetting to Prime Generator

```python
# WRONG
def my_generator():
    value = yield
    print(value)

gen = my_generator()
gen.send(1)  # TypeError: can't send non-None value to a just-started generator

# RIGHT
gen = my_generator()
next(gen)  # Prime the generator
gen.send(1)
```

### 2. Using Return in Generator

```python
# WRONG (Python 3.2+)
def my_generator():
    yield 1
    return 2  # StopIteration with value 2

# RIGHT (catch StopIteration)
gen = my_generator()
try:
    while True:
        print(next(gen))
except StopIteration as e:
    print(f"Generator returned: {e.value}")
```

### 3. Modifying Generator State

```python
# WRONG
def my_generator():
    n = 0
    while True:
        yield n
        n += 1

gen = my_generator()
print(next(gen))  # 0
# Can't easily reset generator

# RIGHT (create new generator)
gen = my_generator()
print(next(gen))  # 0
gen = my_generator()  # Create new generator
```

### 4. Generator vs Iterator

```python
# Generator is an iterator
def my_generator():
    yield 1
    yield 2

gen = my_generator()
print(hasattr(gen, '__iter__'))  # True
print(hasattr(gen, '__next__'))  # True

# But not all iterators are generators
class MyIterator:
    def __iter__(self):
        return self
    
    def __next__(self):
        raise StopIteration

it = MyIterator()
print(hasattr(it, '__iter__'))  # True
print(hasattr(it, '__next__'))  # True
```

---

## Production Notes

1. **Use generators for large datasets** - Memory efficient
2. **Use generator expressions** - More readable than list comprehensions for large data
3. **Use `yield from` for delegation** - Cleaner code
4. **Use `itertools` for common patterns** - More efficient than manual implementation
5. **Use `send()` for coroutine-like behavior** - More complex but powerful
6. **Use `close()` to clean up** - Release resources
7. **Use `throw()` for error handling** - More control over generator
8. **Use generator pipelines** - Chain processing steps
9. **Profile generator performance** - Sometimes list comprehensions are faster
10. **Document generator behavior** - Especially side effects

---

## Further Reading

- Python documentation on generators
- PEP 255 - Simple Generators
- PEP 342 - Coroutines via Enhanced Generators
- Fluent Python by Luciano Ramalho
