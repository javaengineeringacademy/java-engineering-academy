"""
Python Optimization Techniques
Demonstrates various optimization strategies
"""

import time
from typing import List, Dict

# ============================================
# List Comprehensions vs Loops
# ============================================

def comprehension_vs_loop() -> None:
    """Compare comprehensions with loops."""
    print("=== Comprehensions vs Loops ===")
    
    n = 1000000
    
    # Loop approach
    start = time.time()
    result_loop = []
    for i in range(n):
        if i % 2 == 0:
            result_loop.append(i ** 2)
    loop_time = time.time() - start
    
    # Comprehension approach
    start = time.time()
    result_comp = [i ** 2 for i in range(n) if i % 2 == 0]
    comp_time = time.time() - start
    
    print(f"  Loop time: {loop_time:.4f}s")
    print(f"  Comprehension time: {comp_time:.4f}s")
    print(f"  Speedup: {loop_time / comp_time:.2f}x")

# ============================================
# Generator vs List
# ============================================

def generator_vs_list() -> None:
    """Compare generators with lists."""
    print("\n=== Generator vs List ===")
    
    n = 1000000
    
    # List approach
    start = time.time()
    list_result = [i ** 2 for i in range(n)]
    list_time = time.time() - start
    
    # Generator approach
    start = time.time()
    gen_result = (i ** 2 for i in range(n))
    # Consume generator
    _ = sum(gen_result)
    gen_time = time.time() - start
    
    print(f"  List time: {list_time:.4f}s")
    print(f"  Generator time: {gen_time:.4f}s")
    print(f"  List memory: ~{n * 8 / 1024 / 1024:.2f} MB")
    print(f"  Generator memory: ~8 bytes")

# ============================================
# String Concatenation
# ============================================

def string_optimization() -> None:
    """Optimize string operations."""
    print("\n=== String Optimization ===")
    
    n = 10000
    
    # Bad: String concatenation in loop
    start = time.time()
    result = ""
    for i in range(n):
        result += str(i)
    concat_time = time.time() - start
    
    # Good: Join with list
    start = time.time()
    result = "".join(str(i) for i in range(n))
    join_time = time.time() - start
    
    print(f"  Concatenation time: {concat_time:.4f}s")
    print(f"  Join time: {join_time:.4f}s")
    print(f"  Speedup: {concat_time / join_time:.2f}x")

# ============================================
# Dictionary Lookup
# ============================================

def dictionary_optimization() -> None:
    """Optimize dictionary operations."""
    print("\n=== Dictionary Optimization ===")
    
    # Create dictionary
    data = {i: i ** 2 for i in range(100000)}
    
    # Bad: Check membership then access
    start = time.time()
    results = []
    for i in range(100000):
        if i in data:
            results.append(data[i])
    bad_time = time.time() - start
    
    # Good: Direct access with get()
    start = time.time()
    results = []
    for i in range(100000):
        results.append(data.get(i))
    good_time = time.time() - start
    
    print(f"  Check then access: {bad_time:.4f}s")
    print(f"  Direct get(): {good_time:.4f}s")

# ============================================
# Function Call Optimization
# ============================================

def function_optimization() -> None:
    """Optimize function calls."""
    print("\n=== Function Optimization ===")
    
    # Bad: Global lookup
    import math
    n = 1000000
    
    start = time.time()
    results = []
    for i in range(n):
        results.append(math.sqrt(i))
    global_time = time.time() - start
    
    # Good: Local lookup
    start = time.time()
    sqrt = math.sqrt
    results = []
    for i in range(n):
        results.append(sqrt(i))
    local_time = time.time() - start
    
    print(f"  Global lookup: {global_time:.4f}s")
    print(f"  Local lookup: {local_time:.4f}s")

# ============================================
# Set vs List for Membership
# ============================================

def set_vs_list() -> None:
    """Compare set and list membership testing."""
    print("\n=== Set vs List ===")
    
    n = 100000
    data_list = list(range(n))
    data_set = set(range(n))
    
    # Test membership
    test_values = list(range(0, n, 10))  # Test every 10th value
    
    start = time.time()
    for val in test_values:
        _ = val in data_list
    list_time = time.time() - start
    
    start = time.time()
    for val in test_values:
        _ = val in data_set
    set_time = time.time() - start
    
    print(f"  List membership: {list_time:.4f}s")
    print(f"  Set membership: {set_time:.4f}s")
    print(f"  Speedup: {list_time / set_time:.2f}x")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    comprehension_vs_loop()
    generator_vs_list()
    string_optimization()
    dictionary_optimization()
    function_optimization()
    set_vs_list()
