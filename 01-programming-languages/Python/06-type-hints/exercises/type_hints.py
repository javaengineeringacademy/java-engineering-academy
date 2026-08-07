"""
Module 06: Type Hints - Exercises
Practice type hints and annotations in Python.
"""

from typing import List, Dict, Tuple, Optional, Union, Any, TypeVar, Generic, Callable
from typing import Protocol


# =============================================================================
# Exercise 1: Basic Type Hints (⭐)
# =============================================================================
# Add type hints to functions that perform basic operations

def add_numbers(a: int, b: int) -> int:
    """Add two numbers and return the result.
    TODO: Add type hints for parameters and return type
    """
    # TODO: Implement the function
    pass


def greet_user(name: str) -> str:
    """Greet a user by name.
    TODO: Add type hints for parameters and return type
    """
    # TODO: Implement the function
    pass


def is_even(number: int) -> bool:
    """Check if a number is even.
    TODO: Add type hints for parameters and return type
    """
    # TODO: Implement the function
    pass


# Test Exercise 1
def test_exercise_1():
    print("Exercise 1: Basic Type Hints")

    assert add_numbers(5, 3) == 8, f"Expected 8, got {add_numbers(5, 3)}"
    assert add_numbers(-1, 1) == 0, f"Expected 0, got {add_numbers(-1, 1)}"

    assert greet_user("Alice") == "Hello, Alice!", f"Expected 'Hello, Alice!', got {greet_user('Alice')}"
    assert greet_user("Bob") == "Hello, Bob!", f"Expected 'Hello, Bob!', got {greet_user('Bob')}"

    assert is_even(4) == True, f"Expected True, got {is_even(4)}"
    assert is_even(7) == False, f"Expected False, got {is_even(7)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 2: Complex Types (⭐⭐)
# =============================================================================
# Implement functions using complex type hints

def sum_list(numbers: List[int]) -> int:
    """Sum all numbers in a list.
    TODO: Add type hints for List[int] parameter and int return
    """
    # TODO: Implement the function
    pass


def merge_dicts(dict1: Dict[str, int], dict2: Dict[str, int]) -> Dict[str, int]:
    """Merge two dictionaries, with dict2 overriding dict1.
    TODO: Add type hints for Dict parameters and return
    """
    # TODO: Implement the function
    pass


def get_coordinates() -> Tuple[float, float]:
    """Return a tuple of (latitude, longitude).
    TODO: Add type hints for Tuple return
    """
    # TODO: Implement the function
    pass


def find_name(names: List[str], target: str) -> Optional[str]:
    """Find a name in the list, return None if not found.
    TODO: Add type hints for Optional return
    """
    # TODO: Implement the function
    pass


def process_value(value: Union[int, str]) -> str:
    """Process a value that can be int or str.
    TODO: Add type hints for Union parameter
    """
    # TODO: Implement the function
    pass


# Test Exercise 2
def test_exercise_2():
    print("\nExercise 2: Complex Types")

    assert sum_list([1, 2, 3, 4, 5]) == 15, f"Expected 15, got {sum_list([1, 2, 3, 4, 5])}"
    assert sum_list([]) == 0, f"Expected 0, got {sum_list([])}"

    result = merge_dicts({"a": 1, "b": 2}, {"b": 3, "c": 4})
    assert result == {"a": 1, "b": 3, "c": 4}, f"Expected merged dict, got {result}"

    coords = get_coordinates()
    assert isinstance(coords, tuple) and len(coords) == 2, f"Expected tuple of 2, got {coords}"

    assert find_name(["Alice", "Bob"], "Bob") == "Bob", f"Expected 'Bob', got {find_name(['Alice', 'Bob'], 'Bob')}"
    assert find_name(["Alice", "Bob"], "Charlie") is None, f"Expected None, got {find_name(['Alice', 'Bob'], 'Charlie')}"

    assert process_value(42) == "42", f"Expected '42', got {process_value(42)}"
    assert process_value("hello") == "hello", f"Expected 'hello', got {process_value('hello')}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 3: Generic Types (⭐⭐⭐)
# =============================================================================
# Create generic classes using TypeVar and Generic

T = TypeVar('T')


class Stack(Generic[T]):
    """A generic stack data structure.
    TODO: Implement using TypeVar and Generic
    """

    def __init__(self) -> None:
        # TODO: Initialize the stack
        pass

    def push(self, item: T) -> None:
        """Push an item onto the stack."""
        # TODO: Implement push
        pass

    def pop(self) -> Optional[T]:
        """Pop an item from the stack. Return None if empty."""
        # TODO: Implement pop
        pass

    def peek(self) -> Optional[T]:
        """View the top item without removing. Return None if empty."""
        # TODO: Implement peek
        pass

    def is_empty(self) -> bool:
        """Check if the stack is empty."""
        # TODO: Implement is_empty
        pass

    def size(self) -> int:
        """Return the number of items in the stack."""
        # TODO: Implement size
        pass


# Test Exercise 3
def test_exercise_3():
    print("\nExercise 3: Generic Types")

    # Test with integers
    int_stack: Stack[int] = Stack()
    assert int_stack.is_empty() == True, "Expected empty stack"
    int_stack.push(1)
    int_stack.push(2)
    int_stack.push(3)
    assert int_stack.size() == 3, f"Expected size 3, got {int_stack.size()}"
    assert int_stack.peek() == 3, f"Expected 3, got {int_stack.peek()}"
    assert int_stack.pop() == 3, f"Expected 3, got {int_stack.pop()}"
    assert int_stack.size() == 2, f"Expected size 2, got {int_stack.size()}"

    # Test with strings
    str_stack: Stack[str] = Stack()
    str_stack.push("hello")
    str_stack.push("world")
    assert str_stack.pop() == "world", f"Expected 'world', got {str_stack.pop()}"
    assert str_stack.peek() == "hello", f"Expected 'hello', got {str_stack.peek()}"

    # Test empty stack
    empty_stack: Stack[int] = Stack()
    assert empty_stack.pop() is None, "Expected None from empty stack"
    assert empty_stack.peek() is None, "Expected None from empty stack"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 4: Protocol and Structural Subtyping (⭐⭐⭐)
# =============================================================================
# Implement Protocol classes to define structural subtyping

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
        # TODO: Return "Drawing circle with radius {radius}"
        pass


class Rectangle:
    """A rectangle that can be drawn."""
    def __init__(self, width: float, height: float) -> None:
        self.width = width
        self.height = height

    def draw(self) -> str:
        # TODO: Return "Drawing rectangle {width}x{height}"
        pass


def draw_shape(shape: Drawable) -> str:
    """Draw any shape that implements the Drawable protocol.
    This function accepts any object with a draw() method.
    """
    # TODO: Call and return shape.draw()
    pass


def draw_all(shapes: List[Drawable]) -> List[str]:
    """Draw all shapes in a list.
    TODO: Implement using List[Drawable] type hint
    """
    # TODO: Implement the function
    pass


# Test Exercise 4
def test_exercise_4():
    print("\nExercise 4: Protocol and Structural Subtyping")

    circle = Circle(5.0)
    rectangle = Rectangle(4.0, 6.0)

    assert draw_shape(circle) == "Drawing circle with radius 5.0", f"Expected circle description, got {draw_shape(circle)}"
    assert draw_shape(rectangle) == "Drawing rectangle 4.0x6.0", f"Expected rectangle description, got {draw_shape(rectangle)}"

    shapes = [circle, rectangle, Circle(3.0)]
    results = draw_all(shapes)
    assert len(results) == 3, f"Expected 3 results, got {len(results)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Exercise 5: Callable and Function Types (⭐⭐⭐⭐)
# =============================================================================
# Write functions that accept and return other functions

def apply_operation(numbers: List[int], operation: Callable[[int], int]) -> List[int]:
    """Apply an operation to each number in the list.
    TODO: Add proper Callable type hints
    """
    # TODO: Implement the function
    pass


def create_multiplier(factor: int) -> Callable[[int], int]:
    """Create and return a function that multiplies by factor.
    TODO: Add proper type hints
    """
    # TODO: Implement and return the multiplier function
    pass


def compose_functions(f: Callable[[int], int], g: Callable[[int], int]) -> Callable[[int], int]:
    """Compose two functions: returns a function that applies g then f.
    TODO: Add proper Callable type hints
    """
    # TODO: Implement and return the composed function
    pass


# Test Exercise 5
def test_exercise_5():
    print("\nExercise 5: Callable and Function Types")

    numbers = [1, 2, 3, 4, 5]

    # Test apply_operation
    doubled = apply_operation(numbers, lambda x: x * 2)
    assert doubled == [2, 4, 6, 8, 10], f"Expected [2, 4, 6, 8, 10], got {doubled}"

    squared = apply_operation(numbers, lambda x: x ** 2)
    assert squared == [1, 4, 9, 16, 25], f"Expected [1, 4, 9, 16, 25], got {squared}"

    # Test create_multiplier
    double = create_multiplier(2)
    triple = create_multiplier(3)
    assert double(5) == 10, f"Expected 10, got {double(5)}"
    assert triple(5) == 15, f"Expected 15, got {triple(5)}"

    # Test compose_functions
    add_one = lambda x: x + 1
    multiply_by_two = lambda x: x * 2
    composed = compose_functions(multiply_by_two, add_one)
    assert composed(5) == 12, f"Expected 12 (5+1=6, 6*2=12), got {composed(5)}"

    print("  ✓ All tests passed!")


# =============================================================================
# Main
# =============================================================================
if __name__ == "__main__":
    print("=" * 60)
    print("Module 06: Type Hints - Exercises")
    print("=" * 60)

    test_exercise_1()
    test_exercise_2()
    test_exercise_3()
    test_exercise_4()
    test_exercise_5()

    print("\n" + "=" * 60)
    print("All exercises completed!")
    print("=" * 60)