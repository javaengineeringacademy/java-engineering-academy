"""
Python OOP - Inheritance & Polymorphism Exercises
Complete each exercise by implementing the required classes.
Run the test cases to verify your solution.
"""

from abc import ABC, abstractmethod
import math


# Exercise 1: Animal Hierarchy (Easy)
# Create base Animal class with subclasses

class Animal(ABC):
    """
    Abstract base class for all animals.
    
    Requirements:
    - Abstract methods for sound and movement
    - Common attributes (name, legs, sound)
    """
    
    def __init__(self, name, legs=4):
        # TODO: Implement this method
        pass
    
    @abstractmethod
    def sound(self):
        """Return the sound this animal makes."""
        pass
    
    @abstractmethod
    def move(self):
        """Return how this animal moves."""
        pass
    
    def __repr__(self):
        # TODO: Implement this method
        pass


class Dog(Animal):
    """A Dog class that inherits from Animal."""
    
    def __init__(self, name, breed):
        # TODO: Implement this method
        pass
    
    def sound(self):
        # TODO: Implement this method
        pass
    
    def move(self):
        # TODO: Implement this method
        pass
    
    def fetch(self, item):
        """Dogs can fetch items."""
        # TODO: Implement this method
        pass


class Bird(Animal):
    """A Bird class that inherits from Animal."""
    
    def __init__(self, name, can_fly=True):
        # TODO: Implement this method (birds have 2 legs)
        pass
    
    def sound(self):
        # TODO: Implement this method
        pass
    
    def move(self):
        # TODO: Implement this method
        pass
    
    def fly(self):
        """Birds can fly (unless flightless)."""
        # TODO: Implement this method
        pass


# Exercise 2: Shape Calculator (Medium)
# Polymorphic shape classes with area and perimeter calculations

class Shape(ABC):
    """Abstract base class for geometric shapes."""
    
    @abstractmethod
    def area(self):
        """Calculate and return the area."""
        pass
    
    @abstractmethod
    def perimeter(self):
        """Calculate and return the perimeter."""
        pass
    
    @abstractmethod
    def shape_type(self):
        """Return the type of shape as string."""
        pass
    
    def __repr__(self):
        # TODO: Implement this method
        pass


class Circle(Shape):
    def __init__(self, radius):
        # TODO: Implement this method
        pass
    
    def area(self):
        # TODO: Implement this method (π * r²)
        pass
    
    def perimeter(self):
        # TODO: Implement this method (2 * π * r)
        pass
    
    def shape_type(self):
        # TODO: Implement this method
        pass


class Rectangle(Shape):
    def __init__(self, width, height):
        # TODO: Implement this method
        pass
    
    def area(self):
        # TODO: Implement this method
        pass
    
    def perimeter(self):
        # TODO: Implement this method
        pass
    
    def shape_type(self):
        # TODO: Implement this method
        pass
    
    def is_square(self):
        """Check if rectangle is a square."""
        # TODO: Implement this method
        pass


class Triangle(Shape):
    def __init__(self, a, b, c):
        # TODO: Implement this method (sides of triangle)
        pass
    
    def area(self):
        # TODO: Implement this method (Heron's formula)
        pass
    
    def perimeter(self):
        # TODO: Implement this method
        pass
    
    def shape_type(self):
        # TODO: Implement this method
        pass
    
    def is_valid(self):
        """Check if triangle is valid."""
        # TODO: Implement this method
        pass


# Exercise 3: Employee System (Medium)
# Different employee types with bonuses

class Employee(ABC):
    """Abstract base class for employees."""
    
    def __init__(self, name, employee_id, base_salary):
        # TODO: Implement this method
        pass
    
    @abstractmethod
    def calculate_bonus(self):
        """Calculate bonus based on employee type."""
        pass
    
    @abstractmethod
    def employee_type(self):
        """Return type of employee."""
        pass
    
    def total_compensation(self):
        """Return base salary + bonus."""
        # TODO: Implement this method
        pass
    
    def __repr__(self):
        # TODO: Implement this method
        pass


class Developer(Employee):
    """A Developer employee with project bonus."""
    
    def __init__(self, name, employee_id, base_salary, projects_completed=0):
        # TODO: Implement this method
        pass
    
    def calculate_bonus(self):
        # TODO: Implement this method (base + $1000 per project)
        pass
    
    def employee_type(self):
        # TODO: Implement this method
        pass
    
    def add_project(self):
        """Increment projects completed."""
        # TODO: Implement this method
        pass


class Manager(Employee):
    """A Manager employee with team bonus."""
    
    def __init__(self, name, employee_id, base_salary, team_size=0):
        # TODO: Implement this method
        pass
    
    def calculate_bonus(self):
        # TODO: Implement this method (base + $500 per team member)
        pass
    
    def employee_type(self):
        # TODO: Implement this method
        pass
    
    def add_team_member(self):
        """Add a team member."""
        # TODO: Implement this method
        pass


class Executive(Employee):
    """An Executive with stock options bonus."""
    
    def __init__(self, name, employee_id, base_salary, stock_options=0):
        # TODO: Implement this method
        pass
    
    def calculate_bonus(self):
        # TODO: Implement this method (base + stock_options * 100)
        pass
    
    def employee_type(self):
        # TODO: Implement this method
        pass


# Exercise 4: Custom Exception Hierarchy (Medium)
# Build exception classes

class AppError(Exception):
    """Base application error."""
    
    def __init__(self, message, error_code=None):
        # TODO: Implement this method
        pass
    
    def to_dict(self):
        """Return error as dictionary."""
        # TODO: Implement this method
        pass


class ValidationError(AppError):
    """Validation error for invalid input."""
    
    def __init__(self, field, value, message="Invalid value"):
        # TODO: Implement this method
        pass


class NotFoundError(AppError):
    """Resource not found error."""
    
    def __init__(self, resource_type, resource_id):
        # TODO: Implement this method
        pass


class PermissionError(AppError):
    """Permission denied error."""
    
    def __init__(self, action, resource):
        # TODO: Implement this method
        pass


# Exercise 5: Plugin System (Hard)
# Create an extensible plugin architecture

class Plugin(ABC):
    """Base plugin class that all plugins must inherit from."""
    
    def __init__(self, name, version="1.0.0"):
        # TODO: Implement this method
        pass
    
    @abstractmethod
    def execute(self, *args, **kwargs):
        """Execute the plugin's main functionality."""
        pass
    
    @abstractmethod
    def get_description(self):
        """Return plugin description."""
        pass
    
    def __repr__(self):
        # TODO: Implement this method
        pass


class PluginManager:
    """
    Manages plugin registration and execution.
    
    Requirements:
    - Register/unregister plugins
    - Execute plugins by name
    - List available plugins
    """
    
    def __init__(self):
        # TODO: Implement this method
        pass
    
    def register(self, plugin):
        """Register a plugin. Raise ValueError if already registered."""
        # TODO: Implement this method
        pass
    
    def unregister(self, name):
        """Unregister a plugin by name. Raise KeyError if not found."""
        # TODO: Implement this method
        pass
    
    def get_plugin(self, name):
        """Get plugin by name. Raise KeyError if not found."""
        # TODO: Implement this method
        pass
    
    def list_plugins(self):
        """Return list of registered plugin names."""
        # TODO: Implement this method
        pass
    
    def execute_plugin(self, name, *args, **kwargs):
        """Execute a plugin by name."""
        # TODO: Implement this method
        pass


# Example plugins for testing
class GreetPlugin(Plugin):
    def __init__(self):
        super().__init__("greet", "1.0.0")
    
    def execute(self, name="World"):
        return f"Hello, {name}!"
    
    def get_description(self):
        return "A greeting plugin"


class MathPlugin(Plugin):
    def __init__(self):
        super().__init__("math", "1.0.0")
    
    def execute(self, operation, a, b):
        if operation == "add":
            return a + b
        elif operation == "multiply":
            return a * b
    
    def get_description(self):
        return "A math operations plugin"


# ==================== TEST CASES ====================

def test_exercises():
    print("Testing Exercise 1: Animal Hierarchy")
    dog = Dog("Rex", "German Shepherd")
    bird = Bird("Tweety")
    assert dog.sound() == "Woof!"
    assert bird.move() == "Flying"
    assert bird.fly() == True
    assert dog.fetch("ball") == "Rex fetched the ball!"
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 2: Shape Calculator")
    circle = Circle(5)
    rect = Rectangle(4, 6)
    tri = Triangle(3, 4, 5)
    assert abs(circle.area() - 78.54) < 0.01
    assert rect.area() == 24
    assert rect.perimeter() == 20
    assert tri.area() == 6.0
    assert rect.is_square() == False
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 3: Employee System")
    dev = Developer("Alice", "D001", 80000, 5)
    mgr = Manager("Bob", "M001", 100000, 10)
    exec = Executive("Charlie", "E001", 150000, 100)
    assert dev.calculate_bonus() == 85000
    assert mgr.calculate_bonus() == 105000
    assert exec.calculate_bonus() == 160000
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 4: Custom Exceptions")
    try:
        raise ValidationError("email", "invalid-email")
    except ValidationError as e:
        assert e.field == "email"
    
    try:
        raise NotFoundError("User", 123)
    except NotFoundError as e:
        assert "User" in str(e)
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 5: Plugin System")
    manager = PluginManager()
    greet = GreetPlugin()
    math = MathPlugin()
    manager.register(greet)
    manager.register(math)
    assert manager.list_plugins() == ["greet", "math"]
    assert manager.execute_plugin("greet", "World") == "Hello, World!"
    assert manager.execute_plugin("math", "add", 2, 3) == 5
    manager.unregister("greet")
    assert manager.list_plugins() == ["math"]
    print("  ✓ All tests passed!\n")

    print("All inheritance exercises passed!")


if __name__ == "__main__":
    test_exercises()
