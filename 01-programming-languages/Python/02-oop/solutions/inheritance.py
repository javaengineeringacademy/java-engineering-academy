"""
Module 02: OOP - Inheritance Solutions
Practice inheritance and polymorphism in Python.
"""

from abc import ABC, abstractmethod
import math


class Animal(ABC):
    """Abstract base class for all animals."""

    def __init__(self, name, legs=4):
        self.name = name
        self.legs = legs

    @abstractmethod
    def sound(self):
        """Return the sound this animal makes."""
        pass

    @abstractmethod
    def move(self):
        """Return how this animal moves."""
        pass

    def __repr__(self):
        return f"{self.__class__.__name__}('{self.name}')"


class Dog(Animal):
    """A Dog class that inherits from Animal."""

    def __init__(self, name, breed):
        super().__init__(name)
        self.breed = breed

    def sound(self):
        return "Woof!"

    def move(self):
        return "Running"

    def fetch(self, item):
        return f"{self.name} fetched the {item}!"


class Bird(Animal):
    """A Bird class that inherits from Animal."""

    def __init__(self, name, can_fly=True):
        super().__init__(name, legs=2)
        self.can_fly = can_fly

    def sound(self):
        return "Tweet!"

    def move(self):
        return "Flying" if self.can_fly else "Walking"

    def fly(self):
        return self.can_fly


class Shape(ABC):
    """Abstract base class for geometric shapes."""

    @abstractmethod
    def area(self):
        pass

    @abstractmethod
    def perimeter(self):
        pass

    @abstractmethod
    def shape_type(self):
        pass

    def __repr__(self):
        return f"{self.shape_type()}()"


class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius

    def area(self):
        return math.pi * self.radius**2

    def perimeter(self):
        return 2 * math.pi * self.radius

    def shape_type(self):
        return "Circle"


class Rectangle(Shape):
    def __init__(self, width, height):
        self.width = width
        self.height = height

    def area(self):
        return self.width * self.height

    def perimeter(self):
        return 2 * (self.width + self.height)

    def shape_type(self):
        return "Rectangle"

    def is_square(self):
        return self.width == self.height


class Triangle(Shape):
    def __init__(self, a, b, c):
        self.a = a
        self.b = b
        self.c = c

    def area(self):
        s = (self.a + self.b + self.c) / 2
        return math.sqrt(s * (s - self.a) * (s - self.b) * (s - self.c))

    def perimeter(self):
        return self.a + self.b + self.c

    def shape_type(self):
        return "Triangle"

    def is_valid(self):
        return (self.a + self.b > self.c and
                self.a + self.c > self.b and
                self.b + self.c > self.a)


class Employee(ABC):
    """Abstract base class for employees."""

    def __init__(self, name, employee_id, base_salary):
        self.name = name
        self.employee_id = employee_id
        self.base_salary = base_salary

    @abstractmethod
    def calculate_bonus(self):
        pass

    @abstractmethod
    def employee_type(self):
        pass

    def total_compensation(self):
        return self.base_salary + self.calculate_bonus()

    def __repr__(self):
        return f"{self.employee_type()}('{self.name}', '{self.employee_id}')"


class Developer(Employee):
    def __init__(self, name, employee_id, base_salary, projects_completed=0):
        super().__init__(name, employee_id, base_salary)
        self.projects_completed = projects_completed

    def calculate_bonus(self):
        return self.base_salary + (self.projects_completed * 1000)

    def employee_type(self):
        return "Developer"

    def add_project(self):
        self.projects_completed += 1


class Manager(Employee):
    def __init__(self, name, employee_id, base_salary, team_size=0):
        super().__init__(name, employee_id, base_salary)
        self.team_size = team_size

    def calculate_bonus(self):
        return self.base_salary + (self.team_size * 500)

    def employee_type(self):
        return "Manager"

    def add_team_member(self):
        self.team_size += 1


class Executive(Employee):
    def __init__(self, name, employee_id, base_salary, stock_options=0):
        super().__init__(name, employee_id, base_salary)
        self.stock_options = stock_options

    def calculate_bonus(self):
        return self.base_salary + (self.stock_options * 100)

    def employee_type(self):
        return "Executive"


class AppError(Exception):
    """Base application error."""

    def __init__(self, message, error_code=None):
        super().__init__(message)
        self.message = message
        self.error_code = error_code

    def to_dict(self):
        return {"message": self.message, "error_code": self.error_code}


class ValidationError(AppError):
    def __init__(self, field, value, message="Invalid value"):
        super().__init__(message)
        self.field = field
        self.value = value


class NotFoundError(AppError):
    def __init__(self, resource_type, resource_id):
        super().__init__(f"{resource_type} with id {resource_id} not found")
        self.resource_type = resource_type
        self.resource_id = resource_id


class PermissionError(AppError):
    def __init__(self, action, resource):
        super().__init__(f"Permission denied: cannot {action} on {resource}")
        self.action = action
        self.resource = resource


class Plugin(ABC):
    """Base plugin class."""

    def __init__(self, name, version="1.0.0"):
        self.name = name
        self.version = version

    @abstractmethod
    def execute(self, *args, **kwargs):
        pass

    @abstractmethod
    def get_description(self):
        pass

    def __repr__(self):
        return f"{self.__class__.__name__}('{self.name}', v{self.version})"


class PluginManager:
    def __init__(self):
        self._plugins = {}

    def register(self, plugin):
        if plugin.name in self._plugins:
            raise ValueError(f"Plugin '{plugin.name}' already registered")
        self._plugins[plugin.name] = plugin

    def unregister(self, name):
        if name not in self._plugins:
            raise KeyError(f"Plugin '{name}' not found")
        del self._plugins[name]

    def get_plugin(self, name):
        if name not in self._plugins:
            raise KeyError(f"Plugin '{name}' not found")
        return self._plugins[name]

    def list_plugins(self):
        return list(self._plugins.keys())

    def execute_plugin(self, name, *args, **kwargs):
        return self._plugins[name].execute(*args, **kwargs)


if __name__ == "__main__":
    print("Testing Inheritance Solutions...")

    # Test Animal Hierarchy
    dog = Dog("Rex", "German Shepherd")
    bird = Bird("Tweety")
    assert dog.sound() == "Woof!"
    assert bird.move() == "Flying"
    assert bird.fly() == True
    assert dog.fetch("ball") == "Rex fetched the ball!"

    # Test Shape Calculator
    circle = Circle(5)
    rect = Rectangle(4, 6)
    tri = Triangle(3, 4, 5)
    assert abs(circle.area() - 78.54) < 0.01
    assert rect.area() == 24
    assert rect.perimeter() == 20
    assert tri.area() == 6.0
    assert rect.is_square() == False

    # Test Employee System
    dev = Developer("Alice", "D001", 80000, 5)
    mgr = Manager("Bob", "M001", 100000, 10)
    exec = Executive("Charlie", "E001", 150000, 100)
    assert dev.calculate_bonus() == 85000
    assert mgr.calculate_bonus() == 105000
    assert exec.calculate_bonus() == 160000

    # Test Custom Exceptions
    try:
        raise ValidationError("email", "invalid-email")
    except ValidationError as e:
        assert e.field == "email"

    try:
        raise NotFoundError("User", 123)
    except NotFoundError as e:
        assert "User" in str(e)

    # Test Plugin System
    manager = PluginManager()
    greet = type('GreetPlugin', (Plugin,), {
        'execute': lambda self, name="World": f"Hello, {name}!",
        'get_description': lambda self: "A greeting plugin"
    })()
    greet.name = "greet"
    manager.register(greet)
    assert manager.list_plugins() == ["greet"]
    assert manager.execute_plugin("greet", "World") == "Hello, World!"

    print("All Inheritance solutions passed!")
