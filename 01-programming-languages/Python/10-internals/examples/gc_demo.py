"""
Python Garbage Collection Demo
Demonstrates garbage collection mechanisms and debugging
"""

import gc
import sys
import weakref

# ============================================
# Basic Reference Counting
# ============================================

def demonstrate_reference_counting() -> None:
    """Show how reference counting works."""
    class RefCountDemo:
        def __init__(self, name: str) -> None:
            self.name = name
        def __del__(self) -> None:
            print(f"  Deleting {self.name}")
    
    print("=== Reference Counting ===")
    a = RefCountDemo("Object A")
    print(f"  Ref count of A: {sys.getrefcount(a) - 1}")  # -1 for getrefcount arg
    
    b = a  # Another reference
    print(f"  Ref count after b = a: {sys.getrefcount(a) - 1}")
    
    c = a  # Yet another reference
    print(f"  Ref count after c = a: {sys.getrefcount(a) - 1}")
    
    del b  # Remove one reference
    print(f"  Ref count after del b: {sys.getrefcount(a) - 1}")
    
    del c  # Remove another reference
    print(f"  Ref count after del c: {sys.getrefcount(a) - 1}")
    
    print("  Exiting scope, A will be deleted")
    # A will be deleted when function exits

# ============================================
# Circular References
# ============================================

def demonstrate_circular_references() -> None:
    """Show how circular references work."""
    class Node:
        def __init__(self, name: str) -> None:
            self.name = name
            self.ref = None
        def __del__(self) -> None:
            print(f"  Deleting node: {self.name}")
    
    print("\n=== Circular References ===")
    gc.disable()  # Disable GC to see circular reference
    
    node1 = Node("Node 1")
    node2 = Node("Node 2")
    
    # Create circular reference
    node1.ref = node2
    node2.ref = node1
    
    print(f"  Node 1 ref count: {sys.getrefcount(node1) - 1}")
    print(f"  Node 2 ref count: {sys.getrefcount(node2) - 1}")
    
    del node1
    del node2
    
    print("  Objects still exist (circular reference)")
    print(f"  Objects in gc.garbage: {len(gc.garbage)}")
    
    gc.enable()
    gc.collect()  # Force garbage collection
    print("  GC collected circular references")

# ============================================
# Garbage Collector Statistics
# ============================================

def show_gc_statistics() -> None:
    """Show garbage collector statistics."""
    print("\n=== GC Statistics ===")
    
    # Get current stats
    stats = gc.get_stats()
    print(f"  GC generations: {len(stats)}")
    for i, gen_stat in enumerate(stats):
        print(f"  Generation {i}: {gen_stat}")
    
    # Get thresholds
    thresholds = gc.get_threshold()
    print(f"  Thresholds: {thresholds}")
    
    # Get counts
    counts = gc.get_count()
    print(f"  Counts: {counts}")

# ============================================
# GC Debugging
# ============================================

def demonstrate_gc_debugging() -> None:
    """Show GC debugging capabilities."""
    print("\n=== GC Debugging ===")
    
    # Enable debug flags
    gc.set_debug(gc.DEBUG_STATS)
    
    # Create some objects
    objects = [list(range(100)) for _ in range(10)]
    
    # Force collection
    collected = gc.collect()
    print(f"  Collected {collected} objects")
    
    # Disable debug
    gc.set_debug(0)

# ============================================
# Weak References
# ============================================

def demonstrate_weak_references() -> None:
    """Show weak references and weakvalue dictionaries."""
    class HeavyObject:
        def __init__(self, name: str) -> None:
            self.name = name
        def __repr__(self) -> str:
            return f"HeavyObject({self.name})"
    
    print("\n=== Weak References ===")
    
    # Create object
    obj = HeavyObject("Test")
    print(f"  Object: {obj}")
    
    # Create weak reference
    weak_ref = weakref.ref(obj)
    print(f"  Weak ref: {weak_ref}")
    print(f"  Dereferenced: {weak_ref()}")
    
    # Delete object
    del obj
    print(f"  After deletion: {weak_ref()}")
    
    # WeakValueDictionary
    print("\n=== WeakValueDictionary ===")
    cache = weakref.WeakValueDictionary()
    
    obj1 = HeavyObject("Obj 1")
    obj2 = HeavyObject("Obj 2")
    
    cache["key1"] = obj1
    cache["key2"] = obj2
    
    print(f"  Cache before deletion: {list(cache.keys())}")
    
    del obj1
    print(f"  Cache after deleting obj1: {list(cache.keys())}")

# ============================================
# Memory Profiling
# ============================================

def demonstrate_memory_profiling() -> None:
    """Show memory usage tracking."""
    print("\n=== Memory Profiling ===")
    
    # Get memory info
    import resource
    usage = resource.getrusage(resource.RUSAGE_SELF)
    print(f"  Max memory usage: {usage.ru_maxrss} KB")
    
    # Create objects to see memory impact
    large_list = [i for i in range(1000000)]
    print(f"  List of 1M ints created")
    
    # Force collection
    gc.collect()
    usage_after = resource.getrusage(resource.RUSAGE_SELF)
    print(f"  Memory after list: {usage_after.ru_maxrss} KB")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    demonstrate_reference_counting()
    demonstrate_circular_references()
    show_gc_statistics()
    demonstrate_gc_debugging()
    demonstrate_weak_references()
    demonstrate_memory_profiling()
