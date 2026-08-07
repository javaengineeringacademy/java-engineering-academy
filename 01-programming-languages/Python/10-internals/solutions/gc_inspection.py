"""
Module 10: Python Internals - GC Inspection Solutions
Practice inspecting Python's garbage collection.
"""

import gc
import sys
import weakref


def get_gc_stats():
    """Get garbage collection statistics."""
    return {
        "counts": gc.get_count(),
        "threshold": gc.get_threshold(),
        "garbage": len(gc.garbage),
        "stats": gc.get_stats()
    }


def disable_gc():
    """Disable garbage collection."""
    gc.disable()


def enable_gc():
    """Enable garbage collection."""
    gc.enable()


def collect_garbage():
    """Force garbage collection and return number of objects collected."""
    return gc.collect()


def get_objects_by_type(type_name):
    """Get all objects of a specific type."""
    objects = []
    for obj in gc.get_objects():
        if type(obj).__name__ == type_name:
            objects.append(obj)
    return objects


def get_referrers(obj):
    """Get all objects that refer to the given object."""
    return gc.get_referrers(obj)


def get_referents(obj):
    """Get all objects that the given object refers to."""
    return gc.get_referents(obj)


def create_weak_reference(obj):
    """Create a weak reference to an object."""
    def callback(ref):
        pass

    return weakref.ref(obj, callback)


def check_weak_reference(ref):
    """Check if a weak reference is still valid."""
    return ref() is not None


def get_weakref_count(obj):
    """Get the number of weak references to an object."""
    return weakref.getweakrefcount(obj)


class TrackedObject:
    """Object that tracks its references."""

    def __init__(self, value):
        self.value = value
        self.references = []

    def add_reference(self, obj):
        self.references.append(obj)

    def __del__(self):
        pass


if __name__ == "__main__":
    print("Testing GC Inspection Solutions...")

    # Test get_gc_stats
    stats = get_gc_stats()
    assert "counts" in stats
    assert "threshold" in stats
    assert "garbage" in stats
    print(f"✓ Exercise 1 passed: GC stats retrieved")

    # Test collect_garbage
    collected = collect_garbage()
    assert isinstance(collected, int)
    print(f"✓ Exercise 2 passed: collected {collected} objects")

    # Test get_objects_by_type
    obj1 = TrackedObject(1)
    obj2 = TrackedObject(2)
    objects = get_objects_by_type("TrackedObject")
    assert len(objects) >= 2
    print(f"✓ Exercise 3 passed: found {len(objects)} TrackedObject instances")

    # Test weak references
    obj = TrackedObject(3)
    ref = create_weak_reference(obj)
    assert check_weak_reference(ref) is True

    del obj
    collected = collect_garbage()
    assert check_weak_reference(ref) is False
    print(f"✓ Exercise 4 passed: weak reference tracking works")

    # Test referrers/referents
    obj = TrackedObject(4)
    referents = get_referents(obj)
    assert len(referents) > 0
    print(f"✓ Exercise 5 passed: object has {len(referents)} referents")

    print("All GC Inspection solutions passed!")
