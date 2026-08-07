"""
Module 14: Memory Management - Memory Profiling Exercises
=========================================================
Practice measuring and optimizing memory usage.
"""

import sys
import tracemalloc

# =============================================================================
# Exercise 1: Object Size Calculator (★☆☆☆☆)
# =============================================================================
# TODO: Calculate memory usage of objects

def deep_sizeof(obj, seen=None):
    """Calculate deep size of object including all referenced objects."""
    # TODO: Recursively calculate size
    pass

# Test Cases
def test_size_calculator():
    small = [1, 2, 3]
    large = list(range(1000))
    
    small_size = deep_sizeof(small)
    large_size = deep_sizeof(large)
    
    assert small_size > 0
    assert large_size > small_size
    print(f"✓ Exercise 1 passed: small={small_size}B, large={large_size}B")

# =============================================================================
# Exercise 2: Memory Snapshot (★★☆☆☆)
# =============================================================================
# TODO: Take and analyze memory snapshots

class MemorySnapshot:
    """Take and compare memory snapshots."""
    # TODO: Implement take_snapshot and compare
    pass

# Test Cases
def test_memory_snapshot():
    snapshot = MemorySnapshot()
    
    snapshot.take("before")
    data = [i for i in range(10000)]
    snapshot.take("after")
    
    diff = snapshot.compare("before", "after")
    assert diff["new allocations"] > 0
    assert diff["total size increase"] > 0
    print(f"✓ Exercise 2 passed: detected {diff['new allocations']} new allocations")

# =============================================================================
# Exercise 3: Memory Leak Detector (★★★☆☆)
# =============================================================================
# TODO: Detect potential memory leaks

def detect_leaks(iterations=100):
    """Run iterations and detect objects that grow without bound."""
    # TODO: Track object counts over iterations
    pass

# Test Tests
def test_leak_detector():
    # Simulate a leak
    leaked_data = []
    
    def leaky_func():
        leaked_data.append([0] * 1000)
    
    result = detect_leaks(leaky_func, iterations=10)
    assert result["potential_leak"] is True
    assert len(result["growing_objects"]) > 0
    print("✓ Exercise 3 passed: leak detection works")

# =============================================================================
# Exercise 4: Memory-Efficient Data Structure (★★★★☆)
# =============================================================================
# TODO: Implement memory-efficient data structure

class CompactStorage:
    """Memory-efficient storage for homogeneous data."""
    # TODO: Use array module or numpy for compact storage
    # TODO: Implement get, set, and size methods
    pass

# Test Cases
def test_compact_storage():
    # Compare memory usage
    regular_list = list(range(10000))
    compact = CompactStorage(range(10000))
    
    regular_size = sys.getsizeof(regular_list)
    compact_size = compact.sizeof()
    
    assert compact_size < regular_size
    assert compact[0] == 0
    assert compact[9999] == 9999
    print(f"✓ Exercise 4 passed: compact={compact_size}B vs regular={regular_size}B")

# =============================================================================
# Exercise 5: Object Pool (★★★★★)
# =============================================================================
# TODO: Implement object pool for memory reuse

class ObjectPool:
    """Pool of reusable objects to reduce allocation overhead."""
    # TODO: Implement acquire, release, and cleanup
    # TODO: Support thread-safe operations
    pass

# Test Cases
def test_object_pool():
    pool = ObjectPool(factory=list, max_size=5)
    
    obj1 = pool.acquire()
    obj1.append(1)
    pool.release(obj1)
    
    obj2 = pool.acquire()
    assert obj2 == [1]  # Reused object
    assert obj2 is obj1
    
    stats = pool.get_stats()
    assert stats["acquired"] == 2
    assert stats["released"] == 1
    assert stats["reused"] == 1
    
    print("✓ Exercise 5 passed: object pool reuses objects")

if __name__ == "__main__":
    print("Running Memory Profiling Exercises...")
    print("=" * 50)
    test_size_calculator()
    test_memory_snapshot()
    test_leak_detector()
    test_compact_storage()
    test_object_pool()
    print("=" * 50)
    print("All tests passed!")
