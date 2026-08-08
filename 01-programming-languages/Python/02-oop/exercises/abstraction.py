"""
Module 02 - OOP: Abstraction Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Abstract Base Classes (Difficulty: Intermediate)
# =============================================================================
# Create abstract classes with ABC.

from abc import ABC, abstractmethod

# TODO: Implement the abstract class
class Shape(ABC):
    """Abstract shape class."""

    @abstractmethod
    def area(self):
        """Calculate area."""
        pass

    @abstractmethod
    def perimeter(self):
        """Calculate perimeter."""
        pass

    def describe(self):
        """Non-abstract method."""
        return f"A shape with area {self.area():.2f}"

# TODO: Implement concrete classes
class Circle(Shape):
    """Circle implementation."""

    def __init__(self, radius):
        pass

    def area(self):
        pass

    def perimeter(self):
        pass

class Rectangle(Shape):
    """Rectangle implementation."""

    def __init__(self, width, height):
        pass

    def area(self):
        pass

    def perimeter(self):
        pass

# Test cases
# shapes = [Circle(5), Rectangle(4, 6)]
# for shape in shapes:
#     print(f"{shape.__class__.__name__}: {shape.describe()}")
# # Expected:
# # Circle: A shape with area 78.54
# # Rectangle: A shape with area 24.00
# try:
#     shape = Shape()  # Should raise TypeError
# except TypeError as e:
#     print(e)


# =============================================================================
# Exercise 2: Interface Pattern (Difficulty: Intermediate)
# =============================================================================
# Create interfaces using ABC.

# TODO: Implement the interfaces
class Repository(ABC):
    """Interface for data repositories."""

    @abstractmethod
    def get(self, id):
        pass

    @abstractmethod
    def save(self, entity):
        pass

    @abstractmethod
    def delete(self, id):
        pass

    @abstractmethod
    def list_all(self):
        pass

class InMemoryRepository(Repository):
    """In-memory implementation."""

    def __init__(self):
        pass

    def get(self, id):
        pass

    def save(self, entity):
        pass

    def delete(self, id):
        pass

    def list_all(self):
        pass

class User:
    """User entity."""

    def __init__(self, id, name, email):
        self.id = id
        self.name = name
        self.email = email

    def __str__(self):
        return f"User({self.id}, {self.name})"

# Test cases
# repo = InMemoryRepository()
# repo.save(User(1, "Alice", "alice@example.com"))
# repo.save(User(2, "Bob", "bob@example.com"))
# print(repo.get(1))      # Expected: User(1, Alice)
# print(repo.list_all())  # Expected: [User(1, Alice), User(2, Bob)]
# repo.delete(1)
# print(repo.list_all())  # Expected: [User(2, Bob)]


# =============================================================================
# Exercise 3: Template Method Pattern (Difficulty: Intermediate)
# =============================================================================
# Use template method pattern with abstract classes.

# TODO: Implement the template
class DataProcessor(ABC):
    """Template for data processing."""

    def process(self, data):
        """Template method."""
        cleaned = self.clean(data)
        transformed = self.transform(cleaned)
        return self.load(transformed)

    @abstractmethod
    def clean(self, data):
        pass

    @abstractmethod
    def transform(self, data):
        pass

    @abstractmethod
    def load(self, data):
        pass

class CSVProcessor(DataProcessor):
    """CSV data processor."""

    def clean(self, data):
        return [row.strip() for row in data if row.strip()]

    def transform(self, data):
        return [row.split(',') for row in data]

    def load(self, data):
        return f"Loaded {len(data)} CSV records"

class JSONProcessor(DataProcessor):
    """JSON data processor."""

    def clean(self, data):
        return data.strip()

    def transform(self, data):
        import json
        return json.loads(data)

    def load(self, data):
        return f"Loaded JSON with {len(data)} keys"

# Test cases
# csv_data = ["name,age", "Alice,30", "", "Bob,25"]
# csv_processor = CSVProcessor()
# print(csv_processor.process(csv_data))
# # Expected: "Loaded 3 CSV records"
#
# json_data = '{"name": "Alice", "age": 30}'
# json_processor = JSONProcessor()
# print(json_processor.process(json_data))
# # Expected: "Loaded JSON with 2 keys"


# =============================================================================
# Exercise 4: Strategy Pattern with ABC (Difficulty: Advanced)
# =============================================================================
# Implement strategy pattern using abstract classes.

# TODO: Implement the strategies
class SortStrategy(ABC):
    """Abstract sorting strategy."""

    @abstractmethod
    def sort(self, data):
        pass

    @property
    def name(self):
        return self.__class__.__name__

class BubbleSort(SortStrategy):
    """Bubble sort implementation."""

    def sort(self, data):
        pass

class QuickSort(SortStrategy):
    """Quick sort implementation."""

    def sort(self, data):
        pass

class MergeSort(SortStrategy):
    """Merge sort implementation."""

    def sort(self, data):
        pass

# TODO: Context class
class Sorter:
    """Context that uses sorting strategies."""

    def __init__(self, strategy):
        pass

    def sort(self, data):
        pass

    def set_strategy(self, strategy):
        pass

# Test cases
# data = [64, 34, 25, 12, 22, 11, 90]
# sorter = Sorter(BubbleSort())
# print(f"{sorter.sort(data.copy())}")
# sorter.set_strategy(QuickSort())
# print(f"{sorter.sort(data.copy())}")
# sorter.set_strategy(MergeSort())
# print(f"{sorter.sort(data.copy())}")


# =============================================================================
# Exercise 5: Plugin Architecture (Difficulty: Advanced)
# =============================================================================
# Create a plugin system using abstract classes.

# TODO: Implement the plugin system
class Plugin(ABC):
    """Abstract plugin class."""

    @property
    @abstractmethod
    def name(self):
        pass

    @abstractmethod
    def execute(self, data):
        pass

class UpperCasePlugin(Plugin):
    """Convert text to uppercase."""

    @property
    def name(self):
        return "UpperCase"

    def execute(self, data):
        pass

class ReversePlugin(Plugin):
    """Reverse text."""

    @property
    def name:
        return "Reverse"

    def execute(self, data):
        pass

class WordCountPlugin(Plugin):
    """Count words in text."""

    @property
    def name(self):
        return "WordCount"

    def execute(self, data):
        pass

# TODO: Plugin manager
class PluginManager:
    """Manages and executes plugins."""

    def __init__(self):
        pass

    def register(self, plugin):
        pass

    def execute_all(self, data):
        pass

# Test cases
# manager = PluginManager()
# manager.register(UpperCasePlugin())
# manager.register(ReversePlugin())
# manager.register(WordCountPlugin())
# results = manager.execute_all("Hello World")
# for name, result in results.items():
#     print(f"{name}: {result}")
# # Expected:
# # UpperCase: HELLO WORLD
# # Reverse: dlroW olleH
# # WordCount: 2
