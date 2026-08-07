# Python itertools Reference

## What is itertools?

The itertools module provides a collection of tools for handling iterators. It contains standard tools for creating and working with iterators in a memory-efficient way.

## Why does itertools matter?

Understanding itertools helps you:
- Write more efficient iteration code
- Create complex iteration patterns
- Avoid manual loop implementations
- Write more Pythonic code

---

## 1. Infinite Iterators

### count

```python
from itertools import count

# Basic count
for i in count(10):
    if i > 15:
        break
    print(i)  # 10, 11, 12, 13, 14, 15

# With step
for i in count(0, 2):
    if i > 10:
        break
    print(i)  # 0, 2, 4, 6, 8, 10
```

### cycle

```python
from itertools import cycle

# Basic cycle
colors = cycle(['red', 'green', 'blue'])
for _ in range(6):
    print(next(colors))  # red, green, blue, red, green, blue
```

### repeat

```python
from itertools import repeat

# Basic repeat
for i in repeat(10, 3):
    print(i)  # 10, 10, 10
```

---

## 2. Finite Iterators

### chain

```python
from itertools import chain

# Basic chain
for i in chain([1, 2], [3, 4], [5, 6]):
    print(i)  # 1, 2, 3, 4, 5, 6

# chain.from_iterable
lists = [[1, 2], [3, 4], [5, 6]]
for i in chain.from_iterable(lists):
    print(i)  # 1, 2, 3, 4, 5, 6
```

### compress

```python
from itertools import compress

data = ['A', 'B', 'C', 'D', 'E']
selectors = [1, 0, 1, 0, 1]

print(list(compress(data, selectors)))  # ['A', 'C', 'E']
```

### dropwhile and takewhile

```python
from itertools import dropwhile, takewhile

# dropwhile
data = [1, 3, 5, 2, 4, 6]
print(list(dropwhile(lambda x: x < 5, data)))  # [5, 2, 4, 6]

# takewhile
print(list(takewhile(lambda x: x < 5, data)))  # [1, 3]
```

### filterfalse

```python
from itertools import filterfalse

# filterfalse (opposite of filter)
data = [1, 2, 3, 4, 5, 6]
print(list(filterfalse(lambda x: x % 2 == 0, data)))  # [1, 3, 5]
```

### islice

```python
from itertools import islice

# Basic islice
data = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
print(list(islice(data, 5)))  # [0, 1, 2, 3, 4]
print(list(islice(data, 2, 5)))  # [2, 3, 4]
print(list(islice(data, 0, 10, 2)))  # [0, 2, 4, 6, 8]
```

### starmap

```python
from itertools import starmap

# starmap
def add(a, b):
    return a + b

pairs = [(1, 2), (3, 4), (5, 6)]
print(list(starmap(add, pairs)))  # [3, 7, 11]
```

### zip_longest

```python
from itertools import zip_longest

# zip_longest
a = [1, 2, 3]
b = ['a', 'b']
print(list(zip_longest(a, b, fillvalue='-')))  # [(1, 'a'), (2, 'b'), (3, '-')]
```

---

## 3. Combinatoric Iterators

### product

```python
from itertools import product

# Basic product
print(list(product([1, 2], ['a', 'b'])))  # [(1, 'a'), (1, 'b'), (2, 'a'), (2, 'b')]

# With repeat
print(list(product([0, 1], repeat=3)))  # [(0,0,0), (0,0,1), (0,1,0), (0,1,1), (1,0,0), (1,0,1), (1,1,0), (1,1,1)]
```

### permutations

```python
from itertools import permutations

# Basic permutations
print(list(permutations([1, 2, 3])))  # All permutations of length 3

# With r
print(list(permutations([1, 2, 3], 2)))  # [(1,2), (1,3), (2,1), (2,3), (3,1), (3,2)]
```

### combinations

```python
from itertools import combinations

# Basic combinations
print(list(combinations([1, 2, 3, 4], 2)))
# [(1,2), (1,3), (1,4), (2,3), (2,4), (3,4)]
```

### combinations_with_replacement

```python
from itertools import combinations_with_replacement

print(list(combinations_with_replacement([1, 2, 3], 2)))
# [(1,1), (1,2), (1,3), (2,2), (2,3), (3,3)]
```

---

## 4. Grouping

### groupby

```python
from itertools import groupby

# Basic groupby
data = [('A', 1), ('A', 2), ('B', 3), ('B', 4), ('A', 5)]
for key, group in groupby(data, key=lambda x: x[0]):
    print(key, list(group))
# A [('A', 1), ('A', 2)]
# B [('B', 3), ('B', 4)]
# A [('A', 5)]

# With sorted data
data = [('A', 1), ('B', 3), ('A', 2), ('B', 4), ('A', 5)]
data.sort(key=lambda x: x[0])  # Important: groupby requires sorted data
for key, group in groupby(data, key=lambda x: x[0]):
    print(key, list(group))
```

---

## 5. Accumulate

```python
from itertools import accumulate

# Basic accumulate
data = [1, 2, 3, 4, 5]
print(list(accumulate(data)))  # [1, 3, 6, 10, 15]

# With function
import operator
print(list(accumulate(data, operator.mul)))  # [1, 2, 6, 24, 120]

# With initial value
print(list(accumulate(data, initial=10)))  # [10, 11, 13, 16, 20, 25]
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **count** | Infinite counter | `count(10)` |
| **cycle** | Infinite cycle | `cycle([1, 2, 3])` |
| **repeat** | Repeat value | `repeat(10, 3)` |
| **chain** | Chain iterables | `chain([1,2], [3,4])` |
| **compress** | Filter by selectors | `compress(data, selectors)` |
| **dropwhile** | Drop while true | `dropwhile(lambda x: x<5, data)` |
| **takewhile** | Take while true | `takewhile(lambda x: x<5, data)` |
| **filterfalse** | Filter false values | `filterfalse(lambda x: x%2==0, data)` |
| **islice** | Slice iterator | `islice(data, 5)` |
| **starmap** | Apply function | `starmap(add, pairs)` |
| **zip_longest** | Zip with fill | `zip_longest(a, b, fillvalue='-')` |
| **product** | Cartesian product | `product([1,2], ['a','b'])` |
| **permutations** | Permutations | `permutations([1,2,3], 2)` |
| **combinations** | Combinations | `combinations([1,2,3,4], 2)` |
| **combinations_with_replacement** | Combinations with replacement | `combinations_with_replacement([1,2,3], 2)` |
| **groupby** | Group by key | `groupby(data, key=lambda x: x[0])` |
| **accumulate** | Accumulate values | `accumulate(data)` |

---

## Common Mistakes

### 1. Forgetting to Sort for groupby

```python
# WRONG
data = [('A', 1), ('B', 3), ('A', 2), ('B', 4), ('A', 5)]
for key, group in groupby(data, key=lambda x: x[0]):
    print(key, list(group))
# A [('A', 1)]
# B [('B', 3)]
# A [('A', 2)]
# B [('B', 4)]
# A [('A', 5)]

# RIGHT
data.sort(key=lambda x: x[0])
for key, group in groupby(data, key=lambda x: x[0]):
    print(key, list(group))
```

### 2. Using itertools When Built-in is Fine

```python
# WRONG (unnecessary)
from itertools import islice
list(islice(range(10), 5))

# RIGHT (simpler)
list(range(5))
```

### 3. Not Using itertools for Complex Iteration

```python
# WRONG (manual implementation)
def combinations(iterable, r):
    pool = tuple(iterable)
    n = len(pool)
    if r > n:
        return
    indices = list(range(r))
    yield tuple(pool[i] for i in indices)
    while True:
        for i in reversed(range(r)):
            if indices[i] != i + n - r:
                break
        else:
            return
        indices[i] += 1
        for j in range(i+1, r):
            indices[j] = indices[j-1] + 1
    yield tuple(pool[i] for i in indices)

# RIGHT (use itertools)
from itertools import combinations
list(combinations([1, 2, 3, 4], 2))
```

---

## Production Notes

1. **Use itertools for complex iteration** - More efficient than manual implementation
2. **Use generator expressions when possible** - More readable
3. **Use `chain` for multiple iterables** - More efficient than `+` for lists
4. **Use `islice` for slicing iterators** - Memory efficient
5. **Use `groupby` for grouping** - Requires sorted data
6. **Use `accumulate` for running totals** - More efficient than manual
7. **Use `product` for nested loops** - More readable
8. **Use `permutations` and `combinations`** - For combinatoric problems
9. **Profile performance** - Sometimes built-in functions are faster
10. **Document complex iteration patterns** - For maintainability

---

## Further Reading

- Python documentation on itertools
- Python documentation on functools
- Python documentation on operator
- Fluent Python by Luciano Ramalho
