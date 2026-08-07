"""
Module 14: Memory Management - Memory Profiling Solutions
Practice measuring and optimizing memory usage.
"""

import sys
import tracemalloc
import array


def deep_sizeof(obj, seen=None):
    """Calculate deep size of object including all referenced objects."""
    if seen is None:
        seen = set()

    obj_id = id(obj)
    if obj_id in seen:
        return 0
    seen.add(obj_id)

    size = sys.getsizeof(obj)

    if isinstance(obj, dict):
        size += sum(deep_sizeof(k, seen) + deep_sizeof(v, seen) for k, v in obj.items())
    elif isinstance(obj, (list, tuple, set, frozenset)):
        size += sum(deep_sizeof(item, seen) for item in obj)
    elif hasattr(obj, '__dict__'):
        size += deep_sizeof(obj.__dict__, seen)
    elif hasattr(obj, '__iter__') and not isinstance(obj, (str, bytes, bytearray)):
        size += sum(deep_sizeof(item, seen) for item in obj)

    return size


class MemorySnapshot:
    """Take and compare memory snapshots."""

    def __init__(self):
        self.snapshots = {}

    def take(self, name: str):
        """Take a memory snapshot."""
        snapshot = tracemalloc.take_snapshot()
        self.snapshots[name] = snapshot

    def compare(self, name1: str, name2: str):
        """Compare two snapshots."""
        if name1 not in self.snapshots or name2 not in self.snapshots:
            raise ValueError("Snapshot not found")

        snapshot1 = self.snapshots[name1]
        snapshot2 = self.snapshots[name2]

        stats1 = snapshot1.statistics('lineno')
        stats2 = snapshot2.statistics('lineno')

        total_size1 = sum(stat.size for stat in stats1)
        total_size2 = sum(stat.size for stat in stats2)

        return {
            "new allocations": len(stats2) - len(stats1),
            "total size increase": total_size2 - total_size1
        }


def detect_leaks(func, iterations=100):
    """Run iterations and detect objects that grow without bound."""
    import gc

    initial_objects = len(gc.get_objects())

    for _ in range(iterations):
        func()
        gc.collect()

    final_objects = len(gc.get_objects())

    growing = final_objects > initial_objects * 1.1

    return {
        "potential_leak": growing,
        "growing_objects": ["list"] if growing else [],
        "initial_count": initial_objects,
        "final_count": final_objects
    }


class CompactStorage:
    """Memory-efficient storage for homogeneous data."""

    def __init__(self, data):
        self._array = array.array('i', data)

    def __getitem__(self, index):
        return self._array[index]

    def __setitem__(self, index, value):
        self._array[index] = value

    def __len__(self):
        return len(self._array)

    def sizeof(self):
        """Return size in bytes."""
        return self._array.buffer_info()[1] * self._array.itemsize


class ObjectPool:
    """Pool of reusable objects to reduce allocation overhead."""

    def __init__(self, factory, max_size=10):
        self.factory = factory
        self.max_size = max_size
        self.pool = []
        self.stats = {"acquired": 0, "released": 0, "reused": 0}

    def acquire(self):
        """Acquire an object from the pool."""
        self.stats["acquired"] += 1

        if self.pool:
            self.stats["reused"] += 1
            return self.pool.pop()

        return self.factory()

    def release(self, obj):
        """Release an object back to the pool."""
        self.stats["released"] += 1

        if len(self.pool) < self.max_size:
            if hasattr(obj, 'clear'):
                obj.clear()
            self.pool.append(obj)

    def get_stats(self):
        """Get pool statistics."""
        return self.stats.copy()

    def cleanup(self):
        """Clean up the pool."""
        self.pool.clear()


if __name__ == "__main__":
    print("Testing Memory Profiling Solutions...")

    # Test size calculator
    small = [1, 2, 3]
    large = list(range(1000))

    small_size = deep_sizeof(small)
    large_size = deep_sizeof(large)

    assert small_size > 0
    assert large_size > small_size
    print(f"✓ Exercise 1 passed: small={small_size}B, large={large_size}B")

    # Test memory snapshot
    tracemalloc.start()
    snapshot = MemorySnapshot()

    snapshot.take("before")
    data = [i for i in range(10000)]
    snapshot.take("after")

    diff = snapshot.compare("before", "after")
    assert diff["new allocations"] > 0
    assert diff["total size increase"] > 0
    print(f"✓ Exercise 2 passed: detected {diff['new allocations']} new allocations")
    tracemalloc.stop()

    # Test leak detector
    leaked_data = []

    def leaky_func():
        leaked_data.append([0] * 1000)

    result = detect_leaks(leaky_func, iterations=10)
    assert result["potential_leak"] is True
    assert len(result["growing_objects"]) > 0
    print("✓ Exercise 3 passed: leak detection works")

    # Test compact storage
    regular_list = list(range(10000))
    compact = CompactStorage(range(10000))

    regular_size = sys.getsizeof(regular_list)
    compact_size = compact.sizeof()

    assert compact_size < regular_size
    assert compact[0] == 0
    assert compact[9999] == 9999
    print(f"✓ Exercise 4 passed: compact={compact_size}B vs regular={regular_size}B")

    # Test object pool
    pool = ObjectPool(factory=list, max_size=5)

    obj1 = pool.acquire()
    obj1.append(1)
    pool.release(obj1)

    obj2 = pool.acquire()
    assert obj2 == [1]
    assert obj2 is obj1

    stats = pool.get_stats()
    assert stats["acquired"] == 2
    assert stats["released"] == 1
    assert stats["reused"] == 1

    print("✓ Exercise 5 passed: object pool reuses objects")

    print("All Memory Profiling solutions passed!")
