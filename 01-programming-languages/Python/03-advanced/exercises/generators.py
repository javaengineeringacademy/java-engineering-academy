"""
Python Advanced - Generators & Iterators Exercises
Complete each exercise by implementing the required generator.
Run the test cases to verify your solution.
"""

import itertools


# Exercise 1: Fibonacci Generator (Easy)
# Generate Fibonacci sequence using generators

def fibonacci_generator():
    """
    Generate infinite Fibonacci sequence.
    
    Yields:
        Next Fibonacci number
    
    Usage:
        gen = fibonacci_generator()
        next(gen)  # 0
        next(gen)  # 1
        next(gen)  # 1
        next(gen)  # 2
    """
    # TODO: Implement this generator
    pass


def fibonacci_limited(limit):
    """
    Generate Fibonacci numbers up to limit.
    
    Args:
        limit: Maximum value
    
    Yields:
        Fibonacci numbers <= limit
    """
    # TODO: Implement this generator
    pass


# Exercise 2: File Line Reader (Medium)
# Lazy file reading with generators

def read_large_file(file_path):
    """
    Read a large file line by line using a generator.
    
    Requirements:
    - Lazy loading (don't read entire file)
    - Strip whitespace from each line
    - Skip empty lines
    """
    # TODO: Implement this generator
    pass


def chunked_file_reader(file_path, chunk_size=1024):
    """
    Read a file in chunks using a generator.
    
    Args:
        file_path: Path to file
        chunk_size: Size of each chunk in bytes
    """
    # TODO: Implement this generator
    pass


# Exercise 3: Infinite Counter (Medium)
# Generate infinite sequences

def infinite_counter(start=0, step=1):
    """
    Generate infinite sequence starting from start.
    
    Args:
        start: Starting value
        step: Increment value
    
    Yields:
        Infinite sequence: start, start+step, start+2*step, ...
    """
    # TODO: Implement this generator
    pass


def cycle_through(iterable):
    """
    Cycle through an iterable infinitely.
    
    Args:
        iterable: Any iterable
    
    Yields:
        Elements repeatedly
    """
    # TODO: Implement this generator
    pass


def batch(iterable, batch_size):
    """
    Split iterable into batches of specified size.
    
    Args:
        iterable: Any iterable
        batch_size: Size of each batch
    
    Yields:
        Lists of batch_size elements (last batch may be smaller)
    """
    # TODO: Implement this generator
    pass


# Exercise 4: Pipeline Generator (Hard)
# Chain generators together

def read_words(text):
    """Generator that yields individual words from text."""
    # TODO: Implement this generator
    pass


def filter_words(words, min_length=3):
    """Generator that filters words by minimum length."""
    # TODO: Implement this generator
    pass


def transform_words(words, transform_func):
    """Generator that transforms words using provided function."""
    # TODO: Implement this generator
    pass


def pipeline(*generators):
    """
    Chain multiple generators together.
    
    Args:
        *generators: Generator functions to chain
    
    Yields:
        Values that pass through all generators
    """
    # TODO: Implement this generator
    pass


# Exercise 5: Custom Iterator Class (Hard)
# Implement iterator protocol

class Range:
    """
    Custom range implementation using iterator protocol.
    
    Requirements:
    - Implement __iter__ and __next__
    - Support start, stop, step
    - Raise StopIteration when exhausted
    """
    
    def __init__(self, start, stop=None, step=1):
        # TODO: Implement this method
        pass
    
    def __iter__(self):
        # TODO: Implement this method
        pass
    
    def __next__(self):
        # TODO: Implement this method
        pass


class FibonacciIterator:
    """
    Iterator class for Fibonacci sequence.
    
    Requirements:
    - Implement __iter__ and __next__
    - Support max count
    """
    
    def __init__(self, max_count=None):
        # TODO: Implement this method
        pass
    
    def __iter__(self):
        # TODO: Implement this method
        pass
    
    def __next__(self):
        # TODO: Implement this method
        pass


class WindowIterator:
    """
    Iterator that yields sliding windows of specified size.
    
    Requirements:
    - Yield tuples of window_size consecutive elements
    - Handle case where iterable is shorter than window
    """
    
    def __init__(self, iterable, window_size=3):
        # TODO: Implement this method
        pass
    
    def __iter__(self):
        # TODO: Implement this method
        pass
    
    def __next__(self):
        # TODO: Implement this method
        pass


# ==================== TEST CASES ====================

def test_exercises():
    print("Testing Exercise 1: Fibonacci Generator")
    gen = fibonacci_generator()
    fibs = [next(gen) for _ in range(10)]
    assert fibs == [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]
    
    limited = list(fibonacci_limited(100))
    assert limited == [0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89]
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 2: File Line Reader")
    # Create test file
    test_content = "Line 1\nLine 2\n\nLine 4\nLine 5\n"
    with open("/tmp/test_gen.txt", "w") as f:
        f.write(test_content)
    
    lines = list(read_large_file("/tmp/test_gen.txt"))
    assert lines == ["Line 1", "Line 2", "Line 4", "Line 5"]
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 3: Infinite Counter")
    counter = infinite_counter(0, 2)
    evens = [next(counter) for _ in range(5)]
    assert evens == [0, 2, 4, 6, 8]
    
    cycled = list(itertools.islice(cycle_through([1, 2, 3]), 7))
    assert cycled == [1, 2, 3, 1, 2, 3, 1]
    
    batched = list(batch(range(7), 3))
    assert batched == [[0, 1, 2], [3, 4, 5], [6]]
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 4: Pipeline Generator")
    text = "Hello World from Python"
    result = list(pipeline(
        lambda: read_words(text),
        lambda w: filter_words(w, min_length=4),
        lambda w: transform_words(w, str.upper)
    ))
    assert "HELLO" in result
    assert "WORLD" in result
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 5: Custom Iterator")
    custom_range = list(Range(5))
    assert custom_range == [0, 1, 2, 3, 4]
    
    custom_range_step = list(Range(0, 10, 2))
    assert custom_range_step == [0, 2, 4, 6, 8]
    
    fib_iter = list(FibonacciIterator(8))
    assert fib_iter == [0, 1, 1, 2, 3, 5, 8, 13]
    
    window = list(WindowIterator([1, 2, 3, 4, 5], 3))
    assert window == [(1, 2, 3), (2, 3, 4), (3, 4, 5)]
    print("  ✓ All tests passed!\n")

    print("All generator exercises passed!")


if __name__ == "__main__":
    test_exercises()
