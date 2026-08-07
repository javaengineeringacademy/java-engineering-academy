"""
Weak References in Python
Demonstrates weak references and their applications
"""

import weakref
import gc
from typing import Dict, Any

# ============================================
# Basic Weak Reference
# ============================================

def basic_weakref() -> None:
    """Demonstrate basic weak references."""
    print("=== Basic Weak Reference ===")
    
    class MyClass:
        def __init__(self, value: int) -> None:
            self.value = value
        def __repr__(self) -> str:
            return f"MyClass({self.value})"
    
    # Create object
    obj = MyClass(42)
    print(f"  Object: {obj}")
    
    # Create weak reference
    weak_ref = weakref.ref(obj)
    print(f"  Weak ref: {weak_ref}")
    print(f"  Dereferenced: {weak_ref()}")
    
    # Check if object exists
    print(f"  Object alive: {weak_ref() is not None}")
    
    # Delete object
    del obj
    gc.collect()
    print(f"  After deletion: {weak_ref()}")
    print(f"  Object alive: {weak_ref() is not None}")

# ============================================
# Weak Reference Callback
# ============================================

def weakref_callback() -> None:
    """Demonstrate weak reference callbacks."""
    print("\n=== Weak Reference Callback ===")
    
    class TrackedObject:
        def __init__(self, name: str) -> None:
            self.name = name
        def __repr__(self) -> str:
            return f"TrackedObject({self.name})"
    
    def callback(ref):
        """Called when object is deleted."""
        print(f"  Callback: Object '{ref}' was deleted")
    
    # Create object
    obj = TrackedObject("Tracked")
    print(f"  Object: {obj}")
    
    # Create weak reference with callback
    weak_ref = weakref.ref(obj, callback)
    print(f"  Weak ref created")
    
    # Delete object (trigger callback)
    del obj
    gc.collect()

# ============================================
# WeakValueDictionary
# ============================================

def weakvaluedict() -> None:
    """Demonstrate WeakValueDictionary."""
    print("\n=== WeakValueDictionary ===")
    
    class ExpensiveObject:
        def __init__(self, name: str, data: str) -> None:
            self.name = name
            self.data = data
        def __repr__(self) -> str:
            return f"ExpensiveObject({self.name})"
    
    # Create cache
    cache = weakref.WeakValueDictionary()
    
    # Add objects
    obj1 = ExpensiveObject("Obj1", "Data1")
    obj2 = ExpensiveObject("Obj2", "Data2")
    obj3 = ExpensiveObject("Obj3", "Data3")
    
    cache["key1"] = obj1
    cache["key2"] = obj2
    cache["key3"] = obj3
    
    print(f"  Cache keys: {list(cache.keys())}")
    
    # Delete one object
    del obj1
    gc.collect()
    print(f"  After deleting obj1: {list(cache.keys())}")
    
    # Delete another
    del obj2
    gc.collect()
    print(f"  After deleting obj2: {list(cache.keys())}")

# ============================================
# WeakKeyDictionary
# ============================================

def weakkeydict() -> None:
    """Demonstrate WeakKeyDictionary."""
    print("\n=== WeakKeyDictionary ===")
    
    class Config:
        def __init__(self, name: str) -> None:
            self.name = name
    
    # Create dictionary with weak keys
    config_map = weakref.WeakKeyDictionary()
    
    # Add configs
    config1 = Config("Config1")
    config2 = Config("Config2")
    
    config_map[config1] = "Value1"
    config_map[config2] = "Value2"
    
    print(f"  Config map size: {len(config_map)}")
    
    # Delete config
    del config1
    gc.collect()
    print(f"  After deleting config1: {len(config_map)}")

# ============================================
# WeakSet
# ============================================

def weakset() -> None:
    """Demonstrate WeakSet."""
    print("\n=== WeakSet ===")
    
    class Item:
        def __init__(self, name: str) -> None:
            self.name = name
        def __repr__(self) -> str:
            return f"Item({self.name})"
    
    # Create weak set
    items = weakref.WeakSet()
    
    # Add items
    item1 = Item("Item1")
    item2 = Item("Item2")
    item3 = Item("Item3")
    
    items.add(item1)
    items.add(item2)
    items.add(item3)
    
    print(f"  Items: {list(items)}")
    
    # Delete item
    del item1
    gc.collect()
    print(f"  After deleting item1: {list(items)}")

# ============================================
# Proxy Reference
# ============================================

def proxy_reference() -> None:
    """Demonstrate proxy references."""
    print("\n=== Proxy Reference ===")
    
    class MyClass:
        def __init__(self, value: int) -> None:
            self.value = value
        def __repr__(self) -> str:
            return f"MyClass({self.value})"
    
    # Create object
    obj = MyClass(100)
    print(f"  Object: {obj}")
    
    # Create proxy
    proxy = weakref.proxy(obj)
    print(f"  Proxy: {proxy}")
    print(f"  Access via proxy: {proxy.value}")
    
    # Delete object
    del obj
    gc.collect()
    
    # Accessing proxy after deletion raises ReferenceError
    try:
        print(proxy.value)
    except ReferenceError as e:
        print(f"  ReferenceError: {e}")

# ============================================
# Practical: Cache with Weak References
# ============================================

def cache_example() -> None:
    """Practical example: cache with weak references."""
    print("\n=== Cache Example ===")
    
    class DataProcessor:
        def __init__(self, name: str) -> None:
            self.name = name
            self.processed_data = None
        
        def process(self, data: str) -> str:
            """Process data (expensive operation)."""
            result = f"Processed: {data.upper()}"
            self.processed_data = result
            return result
    
    # Create cache
    cache = weakref.WeakValueDictionary()
    
    def get_processor(name: str) -> DataProcessor:
        """Get or create processor."""
        if name not in cache:
            cache[name] = DataProcessor(name)
        return cache[name]
    
    # Use cache
    proc1 = get_processor("processor1")
    print(f"  Created: {proc1.name}")
    
    proc1_copy = get_processor("processor1")
    print(f"  Got from cache: {proc1_copy.name}")
    print(f"  Same object: {proc1 is proc1_copy}")
    
    # Delete original
    del proc1
    gc.collect()
    
    # Cache still has reference
    print(f"  Cache size: {len(cache)}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    basic_weakref()
    weakref_callback()
    weakvaluedict()
    weakkeydict()
    weakset()
    proxy_reference()
    cache_example()
