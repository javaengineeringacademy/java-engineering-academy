"""
Module 03 - Advanced: Generators Solutions
Difficulty: Intermediate to Advanced
"""

# =============================================================================
# Exercise 1: Basic Generators - Solution
# =============================================================================
def fibonacci(limit):
    """Generate Fibonacci numbers up to limit."""
    a, b = 0, 1
    while a <= limit:
        yield a
        a, b = b, a + b

def custom_range(start, stop=None, step=1):
    """Implement custom range using generator."""
    if stop is None:
        start, stop = 0, start

    if step == 0:
        raise ValueError("step cannot be zero")

    current = start
    while (step > 0 and current < stop) or (step < 0 and current > stop):
        yield current
        current += step

print(list(fibonacci(50)))  # [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
print(list(custom_range(5)))  # [0, 1, 2, 3, 4]
print(list(custom_range(1, 10, 2)))  # [1, 3, 5, 7, 9]


# =============================================================================
# Exercise 2: Generator Expressions - Solution
# =============================================================================
def sum_of_squares(n):
    """Calculate sum of squares using generator expression."""
    return sum(x * x for x in range(1, n + 1))

def flatten_generator(nested_list):
    """Flatten a nested list using generator."""
    for item in nested_list:
        if isinstance(item, list):
            yield from flatten_generator(item)
        else:
            yield item

print(sum_of_squares(10))  # 385
nested = [[1, 2], [3, 4], [5, 6]]
print(list(flatten_generator(nested)))  # [1, 2, 3, 4, 5, 6]


# =============================================================================
# Exercise 3: Generator Pipelines - Solution
# =============================================================================
def read_data(source):
    """Read data from source (simulated)."""
    for item in source:
        yield item

def filter_data(data, condition):
    """Filter data based on condition."""
    for item in data:
        if condition(item):
            yield item

def transform_data(data, func):
    """Transform data using function."""
    for item in data:
        yield func(item)

source = range(100)
pipeline = transform_data(
    filter_data(read_data(source), lambda x: x % 2 == 0),
    lambda x: x * x
)
print(list(pipeline)[:5])  # [0, 4, 16, 36, 64]


# =============================================================================
# Exercise 4: Infinite Generators - Solution
# =============================================================================
def running_average():
    """Calculate running average of values sent to generator."""
    total = 0
    count = 0
    average = None
    while True:
        value = yield average
        total += value
        count += 1
        average = total / count

def traffic_light():
    """Simulate traffic light state machine."""
    states = ["Green", "Yellow", "Red"]
    while True:
        for state in states:
            yield state

avg = running_average()
avg.send(None)  # Initialize
print(avg.send(10))   # 10.0
print(avg.send(20))   # 15.0
print(avg.send(30))   # 20.0

light = traffic_light()
next(light)  # Initialize
print(next(light))  # "Green"
print(next(light))  # "Yellow"
print(next(light))  # "Red"


# =============================================================================
# Exercise 5: Generator Performance - Solution
# =============================================================================
def chunked_file_reader(file_path, chunk_size=1024):
    """Read file in chunks using generator."""
    with open(file_path, 'rb') as f:
        while True:
            chunk = f.read(chunk_size)
            if not chunk:
                break
            yield chunk

class LazyRange:
    """A lazy range that generates values on demand."""

    def __init__(self, start, stop=None, step=1):
        if stop is None:
            start, stop = 0, start
        self.start = start
        self.stop = stop
        self.step = step

    def __iter__(self):
        current = self.start
        while (self.step > 0 and current < self.stop) or \
              (self.step < 0 and current > self.stop):
            yield current
            current += self.step

    def __len__(self):
        return max(0, (self.stop - self.start + self.step - 1) // self.step)

    def __getitem__(self, index):
        if isinstance(index, slice):
            indices = range(*index.indices(len(self)))
            return [self[i] for i in indices]
        if index < 0:
            index += len(self)
        if index < 0 or index >= len(self):
            raise IndexError("Index out of range")
        return self.start + index * self.step

lazy = LazyRange(1000000)
print(sum(lazy))  # 499999500000 (memory efficient)
print(lazy[:5])   # [0, 1, 2, 3, 4]
