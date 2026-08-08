"""
Module 06 - Type Hints: Generics Solutions
Difficulty: Intermediate to Advanced
"""

from typing import TypeVar, Generic, List, Optional, Tuple, Any

T = TypeVar('T')
K = TypeVar('K')
V = TypeVar('V')

# =============================================================================
# Exercise 1: Type Variables - Solution
# =============================================================================
class Stack(Generic[T]):
    """A generic stack data structure."""

    def __init__(self):
        self._items: List[T] = []

    def push(self, item: T) -> None:
        self._items.append(item)

    def pop(self) -> Optional[T]:
        if self.is_empty():
            return None
        return self._items.pop()

    def peek(self) -> Optional[T]:
        if self.is_empty():
            return None
        return self._items[-1]

    def is_empty(self) -> bool:
        return len(self._items) == 0

    def size(self) -> int:
        return len(self._items)

int_stack: Stack[int] = Stack()
int_stack.push(1)
int_stack.push(2)
print(int_stack.pop())   # 2
print(int_stack.peek())  # 1

str_stack: Stack[str] = Stack()
str_stack.push("hello")
print(str_stack.pop())   # "hello"


# =============================================================================
# Exercise 2: Generic Functions - Solution
# =============================================================================
def first_element(lst: List[T]) -> Optional[T]:
    """Return first element of list."""
    if not lst:
        return None
    return lst[0]

def swap(a: Tuple[T, K]) -> Tuple[K, T]:
    """Swap elements in tuple."""
    return (a[1], a[0])

def filter_by_type(lst: List[Any], target_type: type) -> List[Any]:
    """Filter list by type."""
    return [item for item in lst if isinstance(item, target_type)]

print(first_element([1, 2, 3]))      # 1
print(first_element(["a", "b"]))     # "a"
print(first_element([]))             # None
print(swap((1, "hello")))            # ("hello", 1)
print(filter_by_type([1, "a", 2, "b"], int))  # [1, 2]


# =============================================================================
# Exercise 3: Generic Classes - Solution
# =============================================================================
class Pair(Generic[K, V]):
    """A generic key-value pair."""

    def __init__(self, key: K, value: V):
        self._key = key
        self._value = value

    def get_key(self) -> K:
        return self._key

    def get_value(self) -> V:
        return self._value

    def __repr__(self) -> str:
        return f"Pair({self._key}, {self._value})"

class Result(Generic[T]):
    """A generic result type that can be success or failure."""

    def __init__(self, value: Optional[T] = None, error: Optional[str] = None):
        self._value = value
        self._error = error

    @classmethod
    def success(cls, value: T) -> 'Result[T]':
        return cls(value=value)

    @classmethod
    def failure(cls, error: str) -> 'Result[T]':
        return cls(error=error)

    def is_success(self) -> bool:
        return self._error is None

    def get_value(self) -> Optional[T]:
        return self._value

    def get_error(self) -> Optional[str]:
        return self._error

pair = Pair(1, "one")
print(pair.get_key())    # 1
print(pair.get_value())  # "one"

success_result = Result.success(42)
failure_result = Result.failure("Something went wrong")
print(success_result.is_success())  # True
print(failure_result.is_success())  # False
print(success_result.get_value())   # 42
print(failure_result.get_error())   # "Something went wrong"


# =============================================================================
# Exercise 4: Generic Constraints - Solution
# =============================================================================
Number = TypeVar('Number', int, float)

def add(a: Number, b: Number) -> Number:
    """Add two numbers."""
    return a + b

def multiply(a: Number, b: Number) -> Number:
    """Multiply two numbers."""
    return a * b

print(add(1, 2))       # 3
print(add(1.5, 2.5))   # 4.0
print(multiply(3, 4))   # 12


# =============================================================================
# Exercise 5: Recursive Types - Solution
# =============================================================================
class TreeNode(Generic[T]):
    """A generic tree node."""

    def __init__(self, value: T, children: Optional[List['TreeNode[T]']] = None):
        self.value = value
        self.children = children or []

    def add_child(self, child: 'TreeNode[T]') -> None:
        self.children.append(child)

    def is_leaf(self) -> bool:
        return len(self.children) == 0

    def depth(self) -> int:
        if self.is_leaf():
            return 0
        return 1 + max(child.depth() for child in self.children)

root = TreeNode(1)
child1 = TreeNode(2)
child2 = TreeNode(3)
root.add_child(child1)
root.add_child(child2)
child1.add_child(TreeNode(4))
print(root.is_leaf())  # False
print(root.depth())    # 2
