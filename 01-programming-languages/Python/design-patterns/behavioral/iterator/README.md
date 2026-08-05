# Iterator Pattern in Python

The Iterator pattern provides a way to access elements of a collection sequentially without exposing its underlying representation. Python has built-in iterator protocols (`__iter__`, `__next__`) making this pattern fundamental to the language.

## When to Use

- Traversing collections without exposing internal structure
- Supporting multiple traversal strategies
- Providing a uniform interface for different collection types
- Lazy evaluation and memory-efficient iteration
- Tree and graph traversal

## Python Implementation

### Custom Iterator Class
```python
class NumberRange:
    def __init__(self, start: int, end: int, step: int = 1):
        self.start = start
        self.end = end
        self.step = step
        self.current = start

    def __iter__(self):
        return self

    def __next__(self):
        if self.current >= self.end:
            raise StopIteration
        value = self.current
        self.current += self.step
        return value

# Usage
for num in NumberRange(0, 10, 2):
    print(num)  # 0, 2, 4, 6, 8
```

### Generator-Based Iterator
```python
def fibonacci(limit: int):
    a, b = 0, 1
    while a < limit:
        yield a
        a, b = b, a + b

def flatten(nested_list):
    for item in nested_list:
        if isinstance(item, (list, tuple)):
            yield from flatten(item)
        else:
            yield item

# Usage
print(list(fibonacci(100)))  # [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89]
print(list(flatten([1, [2, 3], [4, [5, 6]]])))  # [1, 2, 3, 4, 5, 6]
```

### Tree Iterator
```python
class TreeNode:
    def __init__(self, value, children=None):
        self.value = value
        self.children = children or []

    def add_child(self, node):
        self.children.append(node)
        return self

    def __iter__(self):
        yield self.value
        for child in self.children:
            yield from child

    def depth_first(self):
        yield self.value
        for child in self.children:
            yield from child.depth_first()

    def breadth_first(self):
        queue = [self]
        while queue:
            node = queue.pop(0)
            yield node.value
            queue.extend(node.children)

# Usage
root = TreeNode(1)
root.add_child(TreeNode(2)).add_child(TreeNode(3))
root.add_child(TreeNode(4))
print(list(root))  # [1, 2, 3, 4]
```

## Pythonic Alternative

Use generators for memory-efficient iteration:
```python
def read_large_file(file_path):
    with open(file_path, 'r') as f:
        for line in f:
            yield line.strip()

# Process file line by line without loading entire file
for line in read_large_file("large.txt"):
    process(line)
```

## Real-World Example

```python
class PaginatedIterator:
    def __init__(self, fetch_func, page_size=10):
        self.fetch_func = fetch_func
        self.page_size = page_size
        self.current_page = 0
        self.has_more = True

    def __iter__(self):
        return self

    def __next__(self):
        if not self.has_more:
            raise StopIteration

        data = self.fetch_func(self.current_page, self.page_size)
        if not data:
            self.has_more = False
            raise StopIteration

        self.current_page += 1
        return data
```

## Best Practices

1. Use generators for simple iteration logic
2. Implement both `__iter__` and `__next__` for class-based iterators
3. Raise `StopIteration` to signal end of iteration
4. Consider `itertools` for common iteration patterns
5. Use `yield from` for delegation

## Interview Questions

1. What is the difference between `__iter__` and `__next__`?
2. How do generators implement the Iterator pattern?
3. What are the advantages of lazy iteration?
4. How would you create a bidirectional iterator?
5. What is the role of `itertools` in Python iteration?

## References

- Python Iterator protocol documentation
- PEP 234 - Iterators
- PEP 255 - Simple Generators
- `itertools` documentation
