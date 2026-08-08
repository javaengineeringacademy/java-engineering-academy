"""
Module 06 - Type Hints: Generics Exercises
Difficulty: Intermediate to Advanced
"""

from typing import TypeVar, Generic, List, Optional, Tuple

# =============================================================================
# Exercise 1: Type Variables (Difficulty: Intermediate)
# =============================================================================
# Create and use type variables.

T = TypeVar('T')
K = TypeVar('K')
V = TypeVar('V')

# TODO: Implement generic stack
class Stack(Generic[T]):
    """A generic stack data structure."""

    def __init__(self):
        pass

    def push(self, item: T) -> None:
        pass

    def pop(self) -> Optional[T]:
        pass

    def peek(self) -> Optional[T]:
        pass

    def is_empty(self) -> bool:
        pass

    def size(self) -> int:
        pass

# Test cases
# int_stack: Stack[int] = Stack()
# int_stack.push(1)
# int_stack.push(2)
# print(int_stack.pop())   # Expected: 2
# print(int_stack.peek())  # Expected: 1
#
# str_stack: Stack[str] = Stack()
# str_stack.push("hello")
# print(str_stack.pop())   # Expected: "hello"


# =============================================================================
# Exercise 2: Generic Functions (Difficulty: Intermediate)
# =============================================================================
# Create generic functions.

# TODO: Implement generic functions
def first_element(lst: List[T]) -> Optional[T]:
    """Return first element of list."""
    pass

def swap(a: Tuple[T, K]) -> Tuple[K, T]:
    """Swap elements in tuple."""
    pass

def filter_by_type(lst: List[Any], target_type: type) -> List[T]:
    """Filter list by type."""
    pass

# Test cases
# print(first_element([1, 2, 3]))      # Expected: 1
# print(first_element(["a", "b"]))     # Expected: "a"
# print(first_element([]))             # Expected: None
# print(swap((1, "hello")))            # Expected: ("hello", 1)
# print(filter_by_type([1, "a", 2, "b"], int))  # Expected: [1, 2]


# =============================================================================
# Exercise 3: Generic Classes (Difficulty: Advanced)
# =============================================================================
# Implement generic classes with multiple type parameters.

# TODO: Implement generic pair
class Pair(Generic[K, V]):
    """A generic key-value pair."""

    def __init__(self, key: K, value: V):
        pass

    def get_key(self) -> K:
        pass

    def get_value(self) -> V:
        pass

    def __repr__(self) -> str:
        pass

# TODO: Implement generic result type
class Result(Generic[T]):
    """A generic result type that can be success or failure."""

    def __init__(self, value: Optional[T] = None, error: Optional[str] = None):
        pass

    @classmethod
    def success(cls, value: T) -> 'Result[T]':
        pass

    @classmethod
    def failure(cls, error: str) -> 'Result[T]':
        pass

    def is_success(self) -> bool:
        pass

    def get_value(self) -> Optional[T]:
        pass

    def get_error(self) -> Optional[str]:
        pass

# Test cases
# pair = Pair(1, "one")
# print(pair.get_key())    # Expected: 1
# print(pair.get_value())  # Expected: "one"
#
# success_result = Result.success(42)
# failure_result = Result.failure("Something went wrong")
# print(success_result.is_success())  # Expected: True
# print(failure_result.is_success())  # Expected: False
# print(success_result.get_value())   # Expected: 42
# print(failure_result.get_error())   # Expected: "Something went wrong"


# =============================================================================
# Exercise 4: Generic Constraints (Difficulty: Advanced)
# =============================================================================
# Use TypeVar with constraints.

# TODO: Create constrained type
# Number = TypeVar('Number', int, float)

# TODO: Implement generic math functions
def add(a: Number, b: Number) -> Number:
    """Add two numbers."""
    pass

def multiply(a: Number, b: Number) -> Number:
    """Multiply two numbers."""
    pass

# Test cases
# print(add(1, 2))       # Expected: 3
# print(add(1.5, 2.5))   # Expected: 4.0
# print(multiply(3, 4))   # Expected: 12


# =============================================================================
# Exercise 5: Recursive Types (Difficulty: Advanced)
# =============================================================================
# Implement recursive generic types.

# TODO: Implement tree node
class TreeNode(Generic[T]):
    """A generic tree node."""

    def __init__(self, value: T, children: Optional[List['TreeNode[T]']] = None):
        pass

    def add_child(self, child: 'TreeNode[T]') -> None:
        pass

    def is_leaf(self) -> bool:
        pass

    def depth(self) -> int:
        pass

# Test cases
# root = TreeNode(1)
# child1 = TreeNode(2)
# child2 = TreeNode(3)
# root.add_child(child1)
# root.add_child(child2)
# child1.add_child(TreeNode(4))
# print(root.is_leaf())  # Expected: False
# print(root.depth())    # Expected: 2
