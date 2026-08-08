"""
Module 06 - Type Hints: Protocols Solutions
Difficulty: Intermediate to Advanced
"""

from typing import Protocol, runtime_checkable, List, Optional, Any

# =============================================================================
# Exercise 1: Basic Protocols - Solution
# =============================================================================
class Drawable(Protocol):
    """Protocol for drawable objects."""

    def draw(self) -> str:
        ...

class Resizable(Protocol):
    """Protocol for resizable objects."""

    def resize(self, factor: float) -> None:
        ...

class Circle:
    """Circle that can be drawn and resized."""

    def __init__(self, radius: float):
        self.radius = radius

    def draw(self) -> str:
        return f"Drawing circle with radius {self.radius}"

    def resize(self, factor: float) -> None:
        self.radius *= factor

class Square:
    """Square that can be drawn and resized."""

    def __init__(self, size: float):
        self.size = size

    def draw(self) -> str:
        return f"Drawing square with size {self.size}"

    def resize(self, factor: float) -> None:
        self.size *= factor

def render(shape: Drawable) -> str:
    """Render any drawable shape."""
    return shape.draw()

circle = Circle(5)
square = Square(4)
print(render(circle))  # "Drawing circle with radius 5"
print(render(square))  # "Drawing square with size 4"


# =============================================================================
# Exercise 2: Runtime Checkable Protocols - Solution
# =============================================================================
@runtime_checkable
class Comparable(Protocol):
    """Protocol for comparable objects."""

    def __lt__(self, other: Any) -> bool:
        ...

    def __eq__(self, other: Any) -> bool:
        ...

def is_comparable(obj: Any) -> bool:
    """Check if object implements Comparable protocol."""
    return isinstance(obj, Comparable)

print(is_comparable(5))           # True
print(is_comparable("hello"))     # True
print(is_comparable([1, 2, 3]))   # False


# =============================================================================
# Exercise 3: Protocol with Methods - Solution
# =============================================================================
class Serializable(Protocol):
    """Protocol for serializable objects."""

    def to_dict(self) -> dict:
        ...

    @classmethod
    def from_dict(cls, data: dict) -> 'Serializable':
        ...

class User:
    """User that can be serialized."""

    def __init__(self, name: str, age: int):
        self.name = name
        self.age = age

    def to_dict(self) -> dict:
        return {"name": self.name, "age": self.age}

    @classmethod
    def from_dict(cls, data: dict) -> 'User':
        return cls(data["name"], data["age"])

def serialize(obj: Serializable) -> dict:
    """Serialize any serializable object."""
    return obj.to_dict()

def deserialize(data: dict, cls: type) -> Serializable:
    """Deserialize data into object."""
    return cls.from_dict(data)

user = User("Alice", 30)
data = serialize(user)
print(data)  # {'name': 'Alice', 'age': 30}
restored = deserialize(data, User)
print(restored.name)  # "Alice"


# =============================================================================
# Exercise 4: Protocol Inheritance - Solution
# =============================================================================
class Readable(Protocol):
    def read(self) -> str:
        ...

class Writable(Protocol):
    def write(self, data: str) -> None:
        ...

class ReadWritable(Readable, Writable, Protocol):
    """Protocol for objects that can be read and written."""
    ...

class FileHandler:
    """File handler that can read and write."""

    def __init__(self, filename: str):
        self.filename = filename
        self.content = ""

    def read(self) -> str:
        return self.content

    def write(self, data: str) -> None:
        self.content = data

def process_file(handler: ReadWritable) -> str:
    """Process file using read/write protocol."""
    content = handler.read()
    handler.write(content.upper())
    return content

handler = FileHandler("test.txt")
handler.write("Hello, Protocol!")
content = process_file(handler)
print(content)  # "Hello, Protocol!"


# =============================================================================
# Exercise 5: Structural Subtyping - Solution
# =============================================================================
class Processable(Protocol):
    """Protocol for processable items."""

    def process(self) -> str:
        ...

class DataProcessor:
    """Processes data."""

    def process(self) -> str:
        return "Processing data"

class TextProcessor:
    """Processes text."""

    def process(self) -> str:
        return "Processing text"

def batch_process(items: List[Processable]) -> List[str]:
    """Process multiple items."""
    return [item.process() for item in items]

processors = [DataProcessor(), TextProcessor(), DataProcessor()]
results = batch_process(processors)
print(results)  # ["Processing data", "Processing text", "Processing data"]
