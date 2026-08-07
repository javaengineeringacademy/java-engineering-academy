"""
Generic Type Hints in Python
Demonstrates TypeVar and generic containers
"""

from typing import TypeVar, Generic, List, Dict, Optional, Callable

T = TypeVar('T')
K = TypeVar('K')
V = TypeVar('V')

# ============================================
# Basic Generic Function
# ============================================

def first_element(items: List[T]) -> T:
    """Generic function that works with any list type."""
    if items:
        return items[0]
    raise IndexError("List is empty")

def zip_lists(keys: List[K], values: List[V]) -> List[tuple]:
    """Generic function with multiple type variables."""
    return list(zip(keys, values))

# ============================================
# Generic Class
# ============================================

class Stack(Generic[T]):
    """Generic stack implementation."""
    
    def __init__(self) -> None:
        self._items: List[T] = []
    
    def push(self, item: T) -> None:
        """Add item to top of stack."""
        self._items.append(item)
    
    def pop(self) -> T:
        """Remove and return top item."""
        if not self._items:
            raise IndexError("Stack is empty")
        return self._items.pop()
    
    def peek(self) -> T:
        """Return top item without removing."""
        if not self._items:
            raise IndexError("Stack is empty")
        return self._items[-1]
    
    def is_empty(self) -> bool:
        """Check if stack is empty."""
        return len(self._items) == 0
    
    def size(self) -> int:
        """Return number of items."""
        return len(self._items)

# ============================================
# Generic Mapping
# ============================================

class Mapping(Generic[K, V]):
    """Generic key-value mapping."""
    
    def __init__(self) -> None:
        self._data: Dict[K, V] = {}
    
    def set(self, key: K, value: V) -> None:
        """Set key-value pair."""
        self._data[key] = value
    
    def get(self, key: K) -> Optional[V]:
        """Get value by key, returns None if not found."""
        return self._data.get(key)
    
    def keys(self) -> List[K]:
        """Return all keys."""
        return list(self._data.keys())
    
    def values(self) -> List[V]:
        """Return all values."""
        return list(self._data.values())

# ============================================
# Generic with Constraint
# ============================================

Number = TypeVar('Number', int, float)

def average(values: List[Number]) -> float:
    """Generic function constrained to numeric types."""
    return sum(values) / len(values)

# ============================================
# Generic Callback
# ============================================

def apply_to_list(items: List[T], func: Callable[[T], T]) -> List[T]:
    """Apply a function to each item in list."""
    return [func(item) for item in items]

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Basic generics
    ints = [1, 2, 3, 4, 5]
    strings = ["a", "b", "c"]
    
    print(f"First int: {first_element(ints)}")      # First int: 1
    print(f"First string: {first_element(strings)}")  # First string: a
    
    # Zip lists
    zipped = zip_lists(["a", "b", "c"], [1, 2, 3])
    print(f"Zipped: {zipped}")  # Zipped: [('a', 1), ('b', 2), ('c', 3)]
    
    # Generic stack
    int_stack: Stack[int] = Stack()
    int_stack.push(10)
    int_stack.push(20)
    int_stack.push(30)
    print(f"Stack size: {int_stack.size()}")  # Stack size: 3
    print(f"Pop: {int_stack.pop()}")          # Pop: 30
    print(f"Peek: {int_stack.peek()}")        # Peek: 20
    
    str_stack: Stack[str] = Stack()
    str_stack.push("hello")
    str_stack.push("world")
    print(f"String stack pop: {str_stack.pop()}")  # String stack pop: world
    
    # Generic mapping
    mapping: Mapping[str, int] = Mapping()
    mapping.set("one", 1)
    mapping.set("two", 2)
    print(f"Get 'one': {mapping.get('one')}")  # Get 'one': 1
    print(f"Keys: {mapping.keys()}")           # Keys: ['one', 'two']
    
    # Constrained generic
    print(f"Average ints: {average([1, 2, 3, 4])}")       # 2.5
    print(f"Average floats: {average([1.5, 2.5, 3.5])}")  # 2.5
    
    # Generic callback
    doubled = apply_to_list([1, 2, 3], lambda x: x * 2)
    print(f"Doubled: {doubled}")  # Doubled: [2, 4, 6]
