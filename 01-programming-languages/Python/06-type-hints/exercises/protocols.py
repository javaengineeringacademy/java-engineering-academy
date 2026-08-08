"""
Module 06 - Type Hints: Protocols Exercises
Difficulty: Intermediate to Advanced
"""

from typing import Protocol, runtime_checkable, List, Optional

# =============================================================================
# Exercise 1: Basic Protocols (Difficulty: Intermediate)
# =============================================================================
# Define and implement protocols.

# TODO: Define protocols
class Drawable(Protocol):
    """Protocol for drawable objects."""

    def draw(self) -> str:
        pass

class Resizable(Protocol):
    """Protocol for resizable objects."""

    def resize(self, factor: float) -> None:
        pass

# TODO: Implement classes that satisfy protocols
class Circle:
    """Circle that can be drawn and resized."""

    def __init__(self, radius: float):
        pass

    def draw(self) -> str:
        pass

    def resize(self, factor: float) -> None:
        pass

class Square:
    """Square that can be drawn and resized."""

    def __init__(self, size: float):
        pass

    def draw(self) -> str:
        pass

    def resize(self, factor: float) -> None:
        pass

# TODO: Function that accepts protocol
def render(shape: Drawable) -> str:
    """Render any drawable shape."""
    pass

# Test cases
# circle = Circle(5)
# square = Square(4)
# print(render(circle))  # Expected: "Drawing circle with radius 5"
# print(render(square))  # Expected: "Drawing square with size 4"


# =============================================================================
# Exercise 2: Runtime Checkable Protocols (Difficulty: Intermediate)
# =============================================================================
# Use runtime_checkable for type checking.

# TODO: Define runtime checkable protocol
@runtime_checkable
class Comparable(Protocol):
    """Protocol for comparable objects."""

    def __lt__(self, other: Any) -> bool:
        pass

    def __eq__(self, other: Any) -> bool:
        pass

# TODO: Check if objects implement protocol
def is_comparable(obj: Any) -> bool:
    """Check if object implements Comparable protocol."""
    pass

# Test cases
# print(is_comparable(5))           # Expected: True
# print(is_comparable("hello"))     # Expected: True
# print(is_comparable([1, 2, 3]))   # Expected: False


# =============================================================================
# Exercise 3: Protocol with Methods (Difficulty: Advanced)
# =============================================================================
# Define protocols with specific method signatures.

# TODO: Define protocol
class Serializable(Protocol):
    """Protocol for serializable objects."""

    def to_dict(self) -> dict:
        pass

    @classmethod
    def from_dict(cls, data: dict) -> 'Serializable':
        pass

# TODO: Implement protocol
class User:
    """User that can be serialized."""

    def __init__(self, name: str, age: int):
        pass

    def to_dict(self) -> dict:
        pass

    @classmethod
    def from_dict(cls, data: dict) -> 'User':
        pass

# TODO: Generic serialization function
def serialize(obj: Serializable) -> dict:
    """Serialize any serializable object."""
    pass

def deserialize(data: dict, cls: type) -> Serializable:
    """Deserialize data into object."""
    pass

# Test cases
# user = User("Alice", 30)
# data = serialize(user)
# print(data)  # Expected: {'name': 'Alice', 'age': 30}
# restored = deserialize(data, User)
# print(restored.name)  # Expected: "Alice"


# =============================================================================
# Exercise 4: Protocol Inheritance (Difficulty: Advanced)
# =============================================================================
# Create protocols that inherit from others.

# TODO: Define base protocols
class Readable(Protocol):
    def read(self) -> str:
        pass

class Writable(Protocol):
    def write(self, data: str) -> None:
        pass

# TODO: Define combined protocol
class ReadWritable(Readable, Writable, Protocol):
    """Protocol for objects that can be read and written."""
    pass

# TODO: Implement combined protocol
class FileHandler:
    """File handler that can read and write."""

    def __init__(self, filename: str):
        pass

    def read(self) -> str:
        pass

    def write(self, data: str) -> None:
        pass

# TODO: Function using protocol
def process_file(handler: ReadWritable) -> str:
    """Process file using read/write protocol."""
    pass

# Test cases
# handler = FileHandler("test.txt")
# handler.write("Hello, Protocol!")
# content = process_file(handler)
# print(content)  # Expected: "Hello, Protocol!"


# =============================================================================
# Exercise 5: Structural Subtyping (Difficulty: Advanced)
# =============================================================================
# Use protocols for structural subtyping.

# TODO: Define protocol
class Processable(Protocol):
    """Protocol for processable items."""

    def process(self) -> str:
        pass

# TODO: Implement different classes
class DataProcessor:
    """Processes data."""

    def process(self) -> str:
        return "Processing data"

class TextProcessor:
    """Processes text."""

    def process(self) -> str:
        return "Processing text"

# TODO: Generic processing function
def batch_process(items: List[Processable]) -> List[str]:
    """Process multiple items."""
    pass

# Test cases
# processors = [DataProcessor(), TextProcessor(), DataProcessor()]
# results = batch_process(processors)
# print(results)  # Expected: ["Processing data", "Processing text", "Processing data"]
