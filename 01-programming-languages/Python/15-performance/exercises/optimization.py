"""
Module 15: Performance - Optimization Exercises
===============================================
Practice optimizing Python code for better performance.
"""

import time
from functools import lru_cache

# =============================================================================
# Exercise 1: Memoization Decorator (★☆☆☆☆)
# =============================================================================
# TODO: Implement memoization decorator

def memoize(func):
    """Cache function results based on arguments."""
    # TODO: Implement caching
    pass

# Test Cases
call_count = 0

@memoize
def fibonacci(n):
    global call_count
    call_count += 1
    if n < 2:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

def test_memoize():
    global call_count
    call_count = 0
    result = fibonacci(30)
    assert result == 832040
    assert call_count < 30  # Should be much less than 30 without memoization
    print(f"✓ Exercise 1 passed: fib(30)={result}, calls={call_count}")

# =============================================================================
# Exercise 2: Lazy Evaluation (★★☆☆☆)
# =============================================================================
# TODO: Implement lazy evaluation wrapper

class LazyValue:
    """Lazily evaluated value."""
    # TODO: Compute value only when accessed
    # TODO: Cache the result
    pass

# Test Tests
def test_lazy_value():
    compute_count = 0
    
    def expensive_compute():
        nonlocal compute_count
        compute_count += 1
        return 42
    
    lazy = LazyValue(expensive_compute)
    assert compute_count == 0  # Not computed yet
    
    value = lazy.value
    assert value == 42
    assert compute_count == 1  # Computed once
    
    value2 = lazy.value
    assert compute_count == 1  # Not recomputed
    print("✓ Exercise 2 passed: lazy evaluation deferred computation")

# =============================================================================
# Exercise 3: Generator Optimization (★★★☆☆)
# =============================================================================
# TODO: Convert list operations to generators

def sum_of_squares_list(n):
    """Sum of squares using list (inefficient)."""
    return sum([i * i for i in range(n)])

def sum_of_squares_gen(n):
    """Sum of squares using generator (memory efficient)."""
    # TODO: Convert to generator expression
    pass

# Test Tests
def test_generator_optimization():
    result_list = sum_of_squares_list(1000)
    result_gen = sum_of_squares_gen(1000)
    
    assert result_list == result_gen
    print(f"✓ Exercise 3 passed: generator matches list output")

# =============================================================================
# Exercise 4: Batch Processor (★★★★☆)
# =============================================================================
# TODO: Process items in batches for efficiency

def batch_process(items, batch_size, process_func):
    """Process items in batches to reduce overhead."""
    # TODO: Implement batch processing
    pass

# Test Cases
def test_batch_processor():
    processed = []
    
    def process_batch(batch):
        processed.extend([x * 2 for x in batch])
    
    items = list(range(100))
    batch_process(items, batch_size=10, process_func=process_batch)
    
    assert len(processed) == 100
    assert processed[0] == 0
    assert processed[99] == 198
    print(f"✓ Exercise 4 passed: batch processed {len(processed)} items")

# =============================================================================
# Exercise 5: String Builder (★★★★★)
# =============================================================================
# TODO: Implement efficient string concatenation

class StringBuilder:
    """Efficient string building using list of parts."""
    # TODO: Append strings and build final result
    # TODO: Support chain calls
    pass

# Test Tests
def test_string_builder():
    builder = StringBuilder()
    for i in range(1000):
        builder.append(f"line {i}\n")
    
    result = builder.build()
    assert result.startswith("line 0\n")
    assert result.endswith("line 999\n")
    assert result.count("\n") == 1000
    
    # Verify it's faster than concatenation
    builder2 = StringBuilder()
    start = time.time()
    for i in range(10000):
        builder2.append(f"part{i}")
    builder2.build()
    gen_time = time.time() - start
    
    start = time.time()
    s = ""
    for i in range(10000):
        s += f"part{i}"
    concat_time = time.time() - start
    
    assert gen_time < concat_time
    print(f"✓ Exercise 5 passed: builder={gen_time:.4f}s, concat={concat_time:.4f}s")

if __name__ == "__main__":
    print("Running Optimization Exercises...")
    print("=" * 50)
    test_memoize()
    test_lazy_value()
    test_generator_optimization()
    test_batch_processor()
    test_string_builder()
    print("=" * 50)
    print("All tests passed!")
