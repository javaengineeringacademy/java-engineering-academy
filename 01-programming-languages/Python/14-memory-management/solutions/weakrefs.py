"""
Module 14: Memory Management - Weak References Solutions
Practice using weak references in Python.
"""

import weakref
import gc
from typing import Any


def create_weak_ref(obj, callback=None):
    """Create a weak reference to an object."""
    return weakref.ref(obj, callback)


def is_alive(ref):
    """Check if a weak reference is still alive."""
    return ref() is not None


class WeakRefDemo:
    """Demo class for weak references."""

    def __init__(self, value):
        self.value = value

    def __repr__(self):
        return f"WeakRefDemo({self.value})"


class WeakValueDictionaryDemo:
    """Demo class using weakref.WeakValueDictionary."""

    def __init__(self):
        self._cache = weakref.WeakValueDictionary()

    def get_or_create(self, key, value):
        """Get existing or create new weakly referenced value."""
        if key in self._cache:
            return self._cache[key]
        obj = WeakRefDemo(value)
        self._cache[key] = obj
        return obj

    def get_keys(self):
        """Get all keys in the dictionary."""
        return list(self._cache.keys())


class WeakSetDemo:
    """Demo class using weakref.WeakSet."""

    def __init__(self):
        self._items = weakref.WeakSet()

    def add(self, obj):
        """Add an object to the weak set."""
        self._items.add(obj)

    def remove(self, obj):
        """Remove an object from the weak set."""
        self._items.discard(obj)

    def get_items(self):
        """Get all items in the set."""
        return list(self._items)

    def __len__(self):
        return len(self._items)


class Parent:
    """Parent class that holds weak references to children."""

    def __init__(self, name):
        self.name = name
        self._children = weakref.WeakSet()

    def add_child(self, child):
        """Add a child with a weak reference."""
        self._children.add(child)

    def get_children(self):
        """Get all children."""
        return list(self._children)


class Child:
    """Child class."""

    def __init__(self, name):
        self.name = name

    def __repr__(self):
        return f"Child({self.name})"


def create_weak_key_dict():
    """Create a weak key dictionary demo."""
    d = weakref.WeakKeyDictionary()

    key1 = WeakRefDemo("key1")
    d[key1] = "value1"

    key2 = WeakRefDemo("key2")
    d[key2] = "value2"

    return d


if __name__ == "__main__":
    print("Testing Weak References Solutions...")

    # Test basic weak reference
    obj = WeakRefDemo(42)
    ref = create_weak_ref(obj)

    assert is_alive(ref) is True
    assert ref().value == 42

    del obj
    gc.collect()
    assert is_alive(ref) is False
    print("✓ Exercise 1 passed: basic weak reference works")

    # Test WeakValueDictionary
    cache = WeakValueDictionaryDemo()

    obj1 = cache.get_or_create("key1", "value1")
    obj2 = cache.get_or_create("key1", "other")
    assert obj1 is obj2
    assert obj1.value == "value1"

    obj3 = cache.get_or_create("key2", "value2")
    assert "key1" in cache.get_keys()
    assert "key2" in cache.get_keys()
    print("✓ Exercise 2 passed: WeakValueDictionary works")

    # Test WeakSet
    ws = WeakSetDemo()

    obj1 = WeakRefDemo(1)
    obj2 = WeakRefDemo(2)
    obj3 = WeakRefDemo(3)

    ws.add(obj1)
    ws.add(obj2)
    ws.add(obj3)
    assert len(ws) == 3

    del obj2
    gc.collect()
    assert len(ws) == 2
    print("✓ Exercise 3 passed: WeakSet works")

    # Test Parent-Child relationship
    parent = Parent("parent")
    child1 = Child("child1")
    child2 = Child("child2")

    parent.add_child(child1)
    parent.add_child(child2)
    assert len(parent.get_children()) == 2

    del child1
    gc.collect()
    assert len(parent.get_children()) == 1
    print("✓ Exercise 4 passed: Parent-Child weak references work")

    # Test WeakKeyDictionary
    d = create_weak_key_dict()
    assert len(d) == 2
    print("✓ Exercise 5 passed: WeakKeyDictionary works")

    print("All Weak References solutions passed!")
