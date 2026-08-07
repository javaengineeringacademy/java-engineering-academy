"""
Garbage Collection Demo in Python
Demonstrates garbage collection mechanisms and debugging
"""

import gc
import sys
import weakref
from typing import List, Dict

# ============================================
# Reference Counting
# ============================================

def reference_counting() -> None:
    """Demonstrate reference counting."""
    print("=== Reference Counting ===")
    
    class ReferenceDemo:
        def __init__(self, name: str) -> None:
            self.name = name
        def __del__(self) -> None:
            print(f"  Deleted: {self.name}")
    
    # Create object
    obj = ReferenceDemo("Object1")
    print(f"  Reference count: {sys.getrefcount(obj) - 1}")  # -1 for getrefcount arg
    
    # Add references
    ref1 = obj
    ref2 = obj
    print(f"  After adding refs: {sys.getrefcount(obj) - 1}")
    
    # Remove references
    del ref1
    del ref2
    print(f"  After removing refs: {sys.getrefcount(obj) - 1}")

# ============================================
# Circular References
# ============================================

def circular_references() -> None:
    """Demonstrate circular references."""
    print("\n=== Circular References ===")
    
    class Node:
        def __init__(self, name: str) -> None:
            self.name = name
            self.ref = None
        def __del__(self) -> None:
            print(f"  Deleted: {self.name}")
    
    # Disable GC to show circular reference
    gc.disable()
    
    node1 = Node("Node1")
    node2 = Node("Node2")
    
    # Create circular reference
    node1.ref = node2
    node2.ref = node1
    
    print(f"  Node1 refcount: {sys.getrefcount(node1) - 1}")
    print(f"  Node2 refcount: {sys.getrefcount(node2) - 1}")
    
    del node1
    del node2
    
    print("  Objects still exist (circular reference)")
    
    # Enable GC and collect
    gc.enable()
    collected = gc.collect()
    print(f"  GC collected: {collected} objects")

# ============================================
# GC Generations
# ============================================

def gc_generations() -> None:
    """Demonstrate GC generations."""
    print("\n=== GC Generations ===")
    
    # Get current stats
    stats = gc.get_stats()
    print(f"  Number of generations: {len(stats)}")
    
    for i, gen_stat in enumerate(stats):
        print(f"  Generation {i}:")
        print(f"    Collections: {gen_stat.get('collections', 0)}")
        print(f"    Collected: {gen_stat.get('collected', 0)}")
        print(f"    Uncollectable: {gen_stat.get('uncollectable', 0)}")
    
    # Get thresholds
    thresholds = gc.get_threshold()
    print(f"  Thresholds: {thresholds}")
    
    # Get counts
    counts = gc.get_count()
    print(f"  Counts: {counts}")

# ============================================
# GC Debugging
# ============================================

def gc_debugging() -> None:
    """Demonstrate GC debugging."""
    print("\n=== GC Debugging ===")
    
    # Enable debug flags
    gc.set_debug(gc.DEBUG_STATS)
    
    # Create some objects
    objects = [list(range(100)) for _ in range(10)]
    
    # Force collection
    collected = gc.collect()
    print(f"  Collected: {collected} objects")
    
    # Disable debug
    gc.set_debug(0)

# ============================================
# Weak References
# ============================================

def weak_references() -> None:
    """Demonstrate weak references."""
    print("\n=== Weak References ===")
    
    class HeavyObject:
        def __init__(self, name: str) -> None:
            self.name = name
        def __repr__(self) -> str:
            return f"HeavyObject({self.name})"
    
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
    
    obj1 = HeavyObject("Obj1")
    obj2 = HeavyObject("Obj2")
    
    cache["key1"] = obj1
    cache["key2"] = obj2
    
    print(f"  Cache before: {list(cache.keys())}")
    
    del obj1
    print(f"  Cache after deleting obj1: {list(cache.keys())}")

# ============================================
# Memory Profiling
# ============================================

def memory_profiling() -> None:
    """Demonstrate memory profiling."""
    print("\n=== Memory Profiling ===")
    
    # Get memory info
    import resource
    usage = resource.getrusage(resource.RUSAGE_SELF)
    print(f"  Max memory: {usage.ru_maxrss} KB")
    
    # Create objects
    large_list = [i for i in range(1000000)]
    print(f"  Created list of 1M ints")
    
    # Force collection
    gc.collect()
    usage_after = resource.getrusage(resource.RUSAGE_SELF)
    print(f"  Memory after: {usage_after.ru_maxrss} KB")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    reference_counting()
    circular_references()
    gc_generations()
    gc_debugging()
    weak_references()
    memory_profiling()
