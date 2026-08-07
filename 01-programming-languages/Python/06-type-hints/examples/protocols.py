"""
Protocol Type Hints in Python
Demonstrates structural subtyping with Protocol
"""

from typing import Protocol, runtime_checkable, List, Any

# ============================================
# Basic Protocol Definition
# ============================================

class Drawable(Protocol):
    """Protocol for objects that can be drawn."""
    
    def draw(self) -> str:
        """Return string representation of drawing."""
        ...

class Resizable(Protocol):
    """Protocol for objects that can be resized."""
    
    def resize(self, factor: float) -> None:
        """Resize by given factor."""
        ...

# ============================================
# Classes Implementing Protocols
# ============================================

class Circle:
    """Circle class - implicitly implements Drawable and Resizable."""
    
    def __init__(self, radius: float) -> None:
        self.radius = radius
    
    def draw(self) -> str:
        return f"Drawing circle with radius {self.radius}"
    
    def resize(self, factor: float) -> None:
        self.radius *= factor

class Square:
    """Square class - implements Drawable only."""
    
    def __init__(self, side: float) -> None:
        self.side = side
    
    def draw(self) -> str:
        return f"Drawing square with side {self.side}"
    
    def resize(self, factor: float) -> None:
        self.side *= factor

class Line:
    """Line class - implements Drawable protocol differently."""
    
    def __init__(self, length: float) -> None:
        self.length = length
    
    def draw(self) -> str:
        return f"Drawing line of length {self.length}"

# ============================================
# Protocol Usage
# ============================================

def draw_shape(shape: Drawable) -> str:
    """Function accepting any object with draw() method."""
    return shape.draw()

def resize_shape(shape: Resizable, factor: float) -> None:
    """Function accepting any object with resize() method."""
    shape.resize(factor)

def process_drawables(shapes: List[Drawable]) -> List[str]:
    """Process multiple drawable objects."""
    return [shape.draw() for shape in shapes]

# ============================================
# Runtime Checkable Protocol
# ============================================

@runtime_checkable
class Serializable(Protocol):
    """Protocol for objects that can be serialized."""
    
    def serialize(self) -> str:
        """Return serialized string."""
        ...

class DataRecord:
    """Data record implementing Serializable."""
    
    def __init__(self, data: dict) -> None:
        self.data = data
    
    def serialize(self) -> str:
        import json
        return json.dumps(self.data)

class SimpleRecord:
    """Simple record NOT implementing Serializable."""
    
    def __init__(self, value: int) -> None:
        self.value = value

# ============================================
# Advanced Protocol with Properties
# ============================================

class Named(Protocol):
    """Protocol for objects with a name property."""
    
    @property
    def name(self) -> str:
        ...

class Person:
    """Person class implementing Named protocol."""
    
    def __init__(self, name: str, age: int) -> None:
        self._name = name
        self.age = age
    
    @property
    def name(self) -> str:
        return self._name

def greet_named(entity: Named) -> str:
    """Greet any object with a name property."""
    return f"Hello, {entity.name}!"

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    # Create instances
    circle = Circle(5.0)
    square = Square(4.0)
    line = Line(10.0)
    
    # Use protocol functions
    print(draw_shape(circle))   # Drawing circle with radius 5.0
    print(draw_shape(square))   # Drawing square with side 4.0
    print(draw_shape(line))     # Drawing line of length 10.0
    
    # Resize
    resize_shape(circle, 2.0)
    print(f"Resized circle radius: {circle.radius}")  # 10.0
    
    # Process multiple
    shapes: List[Drawable] = [circle, square, line]
    results = process_drawables(shapes)
    for r in results:
        print(r)
    
    # Runtime checking
    record = DataRecord({"name": "test", "value": 42})
    simple = SimpleRecord(100)
    
    print(f"DataRecord is Serializable: {isinstance(record, Serializable)}")
    print(f"SimpleRecord is Serializable: {isinstance(simple, Serializable)}")
    
    # Named protocol
    person = Person("Alice", 30)
    print(greet_named(person))  # Hello, Alice!
    
    # Structural typing - no inheritance needed
    print(f"Circle is Drawable: {isinstance(circle, Drawable)}")
    print(f"Square is Drawable: {isinstance(square, Drawable)}")
