# Python Anti-Patterns

Common mistakes and how to avoid them.

## Mutable Default Arguments

```python
# BAD
def append_to(item, lst=[]):
    lst.append(item)
    return lst

# GOOD
def append_to(item, lst=None):
    if lst is None:
        lst = []
    lst.append(item)
    return lst
```

## Using `is` for Value Comparison

```python
# BAD
if x == True:
if x == None:

# GOOD
if x is True:
if x is None:
```

## Importing Inside Functions

```python
# BAD — repeated imports
def process():
    import os
    import json
    # ...

# GOOD — import at module level
import os
import json

def process():
    # ...
```

## Bare Except

```python
# BAD
try:
    risky_operation()
except:
    pass

# GOOD
try:
    risky_operation()
except ValueError as e:
    handle_error(e)
```

## Modifying Collection While Iterating

```python
# BAD
for item in lst:
    if item.bad():
        lst.remove(item)

# GOOD
lst = [item for item in lst if not item.bad()]
```

## Using Global State

```python
# BAD
counter = 0
def increment():
    global counter
    counter += 1

# GOOD
class Counter:
    def __init__(self):
        self.value = 0
    def increment(self):
        self.value += 1
```

## String Concatenation in Loop

```python
# BAD — O(n²)
result = ""
for word in words:
    result += word

# GOOD — O(n)
result = "".join(words)
```

## Not Using Context Managers

```python
# BAD
f = open("file.txt")
data = f.read()
f.close()

# GOOD
with open("file.txt") as f:
    data = f.read()
```

## Overusing `type()` Checks

```python
# BAD
if type(obj) == str:

# GOOD — duck typing or isinstance
if isinstance(obj, str):
if hasattr(obj, 'encode'):
```

## Late Binding Closure

```python
# BAD — all functions see i=9
funcs = [lambda: i for i in range(10)]

# GOOD — capture value
funcs = [lambda i=i: i for i in range(10)]
```
