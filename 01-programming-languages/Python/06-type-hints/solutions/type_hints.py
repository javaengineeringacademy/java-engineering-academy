"""
Module 06: Type Hints - Solutions
Practice type hints and annotations in Python.
"""

from typing import List, Dict, Tuple, Optional, Union, Any, TypeVar, Generic, Callable
from typing import Protocol


# =============================================================================
# Exercise 1: Basic Type Hints (⭐)
# =============================================================================

def add_numbers(a: int, b: int) -> int:
    """Add two numbers and return the result."""
    return a + b


def greet_user(name: str) -> str:
    """Greet a user by name."""
    return f"Hello, {name}!"


def is_even(number: int) -> bool:
    """Check if a number is even."""
    return number % 2 == 0


# =============================================================================
# Exercise 2: Complex Types (⭐⭐)
# =============================================================================

def sum_list(numbers: List[int]) -> int:
    """Sum all numbers in a list."""
    return sum(numbers)


def merge_dicts(dict1: Dict[str, int], dict2: Dict[str, int]) -> Dict[str, int]:
    """Merge two dictionaries, with dict2 overriding dict1."""
    result = dict1.copy()
    result.update(dict2)
    return result


def get_coordinates() -> Tuple[float, float]:
    """Return a tuple of (latitude, longitude)."""
    return (40.7128, -74.0060)


def find_name(names: List[str], target: str) -> Optional[str]:
    """Find a name in the list, return None if not found."""
    for name in names:
        if name == target:
            return name
    return None


def process_value(value: Union[int, str]) -> str:
    """Process a value that can be int or str."""
    return str(value)


# =============================================================================
# Exercise 3: Generic Types (⭐⭐⭐)
# =============================================================================

T = TypeVar('T')


class Stack(Generic[T]):
    """A generic stack data structure."""

    def __init__(self) -> None:
        self._items: List[T] = []

    def push(self, item: T) -> None:
        """Push an item onto the stack."""
        self._items.append(item)

    def pop(self) -> Optional[T]:
        """Pop an item from the stack. Return None if empty."""
        if self.is_empty():
            return None
        return self._items.pop()

    def peek(self) -> Optional[T]:
        """View the top item without removing. Return None if empty."""
        if self.is_empty():
            return None
        return self._items[-1]

    def is_empty(self) -> bool:
        """Check if the stack is empty."""
        return len(self._items) == 0

    def size(self) -> int:
        """Return the number of items in the stack."""
        return len(self._items)


# =============================================================================
# Exercise 4: Protocol and Structural Subtyping (⭐⭐⭐)
# =============================================================================

class Drawable(Protocol):
    """Protocol for objects that can be drawn."""
    def draw(self) -> str:
        """Draw the object and return a description."""
        ...


class Circle:
    """A circle that can be drawn."""
    def __init__(self, radius: float) -> None:
        self.radius = radius

    def draw(self) -> str:
        return f"Drawing circle with radius {self.radius}"


class Rectangle:
    """A rectangle that can be drawn."""
    def __init__(self, width: float, height: float) -> None:
        self.width = width
        self.height = height

    def draw(self) -> str:
        return f"Drawing rectangle {self.width}x{self.height}"


def draw_shape(shape: Drawable) -> str:
    """Draw any shape that implements the Drawable protocol."""
    return shape.draw()


def draw_all(shapes: List[Drawable]) -> List[str]:
    """Draw all shapes in a list."""
    return [shape.draw() for shape in shapes]


# =============================================================================
# Exercise 5: Callable and Function Types (⭐⭐⭐⭐)
# =============================================================================

def apply_operation(numbers: List[int], operation: Callable[[int], int]) -> List[int]:
    """Apply an operation to each number in the list."""
    return [operation(x) for x in numbers]


def create_multiplier(factor: int) -> Callable[[int], int]:
    """Create and return a function that multiplies by factor."""
    def multiplier(x: int) -> int:
        return x * factor
    return multiplier


def compose_functions(f: Callable[[int], int], g: Callable[[int], int]) -> Callable[[int], int]:
    """Compose two functions: returns a function that applies g then f."""
    def composed(x: int) -> int:
        return f(g(x))
    return composed


# =============================================================================
# Main
# =============================================================================

if __name__ == "__main__":
    print("Testing Type Hints Solutions...")

    # Test Exercise 1
    assert add_numbers(5, 3) == 8
    assert add_numbers(-1, 1) == 0
    assert greet_user("Alice") == "Hello, Alice!"
    assert greet_user("Bob") == "Hello, Bob!"
    assert is_even(4) == True
    assert is_even(7) == False

    # Test Exercise 2
    assert sum_list([1, 2, 3, 4, 5]) == 15
    assert sum_list([]) == 0
    result = merge_dicts({"a": 1, "b": 2}, {"b": 3, "c": 4})
    assert result == {"a": 1, "b": 3, "c": 4}
    coords = get_coordinates()
    assert isinstance(coords, tuple) and len(coords) == 2
    assert find_name(["Alice", "Bob"], "Bob") == "Bob"
    assert find_name(["Alice", "Bob"], "Charlie") is None
    assert process_value(42) == "42"
    assert process_value("hello") == "hello"

    # Test Exercise 3
    int_stack: Stack[int] = Stack()
    assert int_stack.is_empty() == True
    int_stack.push(1)
    int_stack.push(2)
    int_stack.push(3)
    assert int_stack.size() == 3
    assert int_stack.peek() == 3
    assert int_stack.pop() == 3
    assert int_stack.size() == 2
    str_stack: Stack[str] = Stack()
    str_stack.push("hello")
    str_stack.push("world")
    assert str_stack.pop() == "world"
    assert str_stack.peek() == "hello"
    empty_stack: Stack[int] = Stack()
    assert empty_stack.pop() is None
    assert empty_stack.peek() is None

    # Test Exercise 4
    circle = Circle(5.0)
    rectangle = Rectangle(4.0, 6.0)
    assert draw_shape(circle) == "Drawing circle with radius 5.0"
    assert draw_shape(rectangle) == "Drawing rectangle 4.0x6.0"
    shapes = [circle, rectangle, Circle(3.0)]
    results = draw_all(shapes)
    assert len(results) == 3

    # Test Exercise 5
    numbers = [1, 2, 3, 4, 5]
    doubled = apply_operation(numbers, lambda x: x * 2)
    assert doubled == [2, 4, 6, 8, 10]
    squared = apply_operation(numbers, lambda x: x ** 2)
    assert squared == [1, 4, 9, 16, 25]
    double = create_multiplier(2)
    triple = create_multiplier(3)
    assert double(5) == 10
    assert triple(5) == 15
    add_one = lambda x: x + 1
    multiply_by_two = lambda x: x * 2
    composed = compose_functions(multiply_by_two, add_one)
    assert composed(5) == 12

    print("All Type Hints solutions passed!")
