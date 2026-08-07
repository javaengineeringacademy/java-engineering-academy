"""
Module 03: Advanced - Generators Solutions
Practice generator implementation in Python.
"""

import itertools


def fibonacci_generator():
    """Generate infinite Fibonacci sequence."""
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b


def fibonacci_limited(limit):
    """Generate Fibonacci numbers up to limit."""
    a, b = 0, 1
    while a <= limit:
        yield a
        a, b = b, a + b


def read_large_file(file_path):
    """Read a large file line by line using a generator."""
    with open(file_path, 'r') as file:
        for line in file:
            stripped = line.strip()
            if stripped:
                yield stripped


def chunked_file_reader(file_path, chunk_size=1024):
    """Read a file in chunks using a generator."""
    with open(file_path, 'rb') as file:
        while True:
            chunk = file.read(chunk_size)
            if not chunk:
                break
            yield chunk


def infinite_counter(start=0, step=1):
    """Generate infinite sequence starting from start."""
    current = start
    while True:
        yield current
        current += step


def cycle_through(iterable):
    """Cycle through an iterable infinitely."""
    while True:
        for item in iterable:
            yield item


def batch(iterable, batch_size):
    """Split iterable into batches of specified size."""
    iterator = iter(iterable)
    while True:
        batch_list = list(itertools.islice(iterator, batch_size))
        if not batch_list:
            break
        yield batch_list


def read_words(text):
    """Generator that yields individual words from text."""
    for word in text.split():
        yield word


def filter_words(words, min_length=3):
    """Generator that filters words by minimum length."""
    for word in words:
        if len(word) >= min_length:
            yield word


def transform_words(words, transform_func):
    """Generator that transforms words using provided function."""
    for word in words:
        yield transform_func(word)


def pipeline(*generators):
    """Chain multiple generators together."""
    def apply_generators(data):
        result = data
        for gen_func in generators:
            result = gen_func(result)
        return result

    return apply_generators


class Range:
    """Custom range implementation using iterator protocol."""

    def __init__(self, start, stop=None, step=1):
        if stop is None:
            self.start = 0
            self.stop = start
        else:
            self.start = start
            self.stop = stop
        self.step = step
        self.current = self.start

    def __iter__(self):
        return self

    def __next__(self):
        if self.step > 0 and self.current >= self.stop:
            raise StopIteration
        if self.step < 0 and self.current <= self.stop:
            raise StopIteration

        value = self.current
        self.current += self.step
        return value


class FibonacciIterator:
    """Iterator class for Fibonacci sequence."""

    def __init__(self, max_count=None):
        self.max_count = max_count
        self.count = 0
        self.a = 0
        self.b = 1

    def __iter__(self):
        return self

    def __next__(self):
        if self.max_count is not None and self.count >= self.max_count:
            raise StopIteration

        value = self.a
        self.a, self.b = self.b, self.a + self.b
        self.count += 1
        return value


class WindowIterator:
    """Iterator that yields sliding windows of specified size."""

    def __init__(self, iterable, window_size=3):
        self.iterable = list(iterable)
        self.window_size = window_size
        self.index = 0

    def __iter__(self):
        return self

    def __next__(self):
        if self.index + self.window_size > len(self.iterable):
            raise StopIteration

        window = tuple(self.iterable[self.index:self.index + self.window_size])
        self.index += 1
        return window


if __name__ == "__main__":
    print("Testing Generators Solutions...")

    # Test Fibonacci Generator
    gen = fibonacci_generator()
    fibs = [next(gen) for _ in range(10)]
    assert fibs == [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]

    limited = list(fibonacci_limited(100))
    assert limited == [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89]

    # Test File Line Reader
    test_content = "Line 1\nLine 2\n\nLine 4\nLine 5\n"
    with open("/tmp/test_gen.txt", "w") as f:
        f.write(test_content)

    lines = list(read_large_file("/tmp/test_gen.txt"))
    assert lines == ["Line 1", "Line 2", "Line 4", "Line 5"]

    # Test Infinite Counter
    counter = infinite_counter(0, 2)
    evens = [next(counter) for _ in range(5)]
    assert evens == [0, 2, 4, 6, 8]

    cycled = list(itertools.islice(cycle_through([1, 2, 3]), 7))
    assert cycled == [1, 2, 3, 1, 2, 3, 1]

    batched = list(batch(range(7), 3))
    assert batched == [[0, 1, 2], [3, 4, 5], [6]]

    # Test Pipeline
    text = "Hello World from Python"
    result = list(pipeline(
        lambda: read_words(text),
        lambda w: filter_words(w, min_length=4),
        lambda w: transform_words(w, str.upper)
    )())
    assert "HELLO" in result
    assert "WORLD" in result

    # Test Custom Iterator
    custom_range = list(Range(5))
    assert custom_range == [0, 1, 2, 3, 4]

    custom_range_step = list(Range(0, 10, 2))
    assert custom_range_step == [0, 2, 4, 6, 8]

    fib_iter = list(FibonacciIterator(8))
    assert fib_iter == [0, 1, 1, 2, 3, 5, 8, 13]

    window = list(WindowIterator([1, 2, 3, 4, 5], 3))
    assert window == [(1, 2, 3), (2, 3, 4), (3, 4, 5)]

    print("All Generators solutions passed!")
