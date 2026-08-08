"""
Module 02 - OOP: Abstraction Solutions
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Abstract Base Classes - Solution
# =============================================================================
from abc import ABC, abstractmethod
import math

class Shape(ABC):
    """Abstract shape class."""

    @abstractmethod
    def area(self):
        pass

    @abstractmethod
    def perimeter(self):
        pass

    def describe(self):
        return f"A shape with area {self.area():.2f}"

class Circle(Shape):
    """Circle implementation."""

    def __init__(self, radius):
        self.radius = radius

    def area(self):
        return math.pi * self.radius ** 2

    def perimeter(self):
        return 2 * math.pi * self.radius

class Rectangle(Shape):
    """Rectangle implementation."""

    def __init__(self, width, height):
        self.width = width
        self.height = height

    def area(self):
        return self.width * self.height

    def perimeter(self):
        return 2 * (self.width + self.height)

shapes = [Circle(5), Rectangle(4, 6)]
for shape in shapes:
    print(f"{shape.__class__.__name__}: {shape.describe()}")
# Circle: A shape with area 78.54
# Rectangle: A shape with area 24.00
try:
    shape = Shape()
except TypeError as e:
    print(e)


# =============================================================================
# Exercise 2: Interface Pattern - Solution
# =============================================================================
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
        self._store = {}

    def get(self, id):
        return self._store.get(id)

    def save(self, entity):
        self._store[entity.id] = entity

    def delete(self, id):
        if id in self._store:
            del self._store[id]

    def list_all(self):
        return list(self._store.values())

class User:
    """User entity."""

    def __init__(self, id, name, email):
        self.id = id
        self.name = name
        self.email = email

    def __str__(self):
        return f"User({self.id}, {self.name})"

repo = InMemoryRepository()
repo.save(User(1, "Alice", "alice@example.com"))
repo.save(User(2, "Bob", "bob@example.com"))
print(repo.get(1))      # User(1, Alice)
print(repo.list_all())  # [User(1, Alice), User(2, Bob)]
repo.delete(1)
print(repo.list_all())  # [User(2, Bob)]


# =============================================================================
# Exercise 3: Template Method Pattern - Solution
# =============================================================================
class DataProcessor(ABC):
    """Template for data processing."""

    def process(self, data):
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

csv_data = ["name,age", "Alice,30", "", "Bob,25"]
csv_processor = CSVProcessor()
print(csv_processor.process(csv_data))  # "Loaded 3 CSV records"

json_data = '{"name": "Alice", "age": 30}'
json_processor = JSONProcessor()
print(json_processor.process(json_data))  # "Loaded JSON with 2 keys"


# =============================================================================
# Exercise 4: Strategy Pattern with ABC - Solution
# =============================================================================
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
        arr = data.copy()
        n = len(arr)
        for i in range(n):
            for j in range(0, n-i-1):
                if arr[j] > arr[j+1]:
                    arr[j], arr[j+1] = arr[j+1], arr[j]
        return arr

class QuickSort(SortStrategy):
    """Quick sort implementation."""

    def sort(self, data):
        if len(data) <= 1:
            return data
        pivot = data[len(data) // 2]
        left = [x for x in data if x < pivot]
        middle = [x for x in data if x == pivot]
        right = [x for x in data if x > pivot]
        return self.sort(left) + middle + self.sort(right)

class MergeSort(SortStrategy):
    """Merge sort implementation."""

    def sort(self, data):
        if len(data) <= 1:
            return data
        mid = len(data) // 2
        left = self.sort(data[:mid])
        right = self.sort(data[mid:])
        return self._merge(left, right)

    def _merge(self, left, right):
        result = []
        i = j = 0
        while i < len(left) and j < len(right):
            if left[i] <= right[j]:
                result.append(left[i])
                i += 1
            else:
                result.append(right[j])
                j += 1
        result.extend(left[i:])
        result.extend(right[j:])
        return result

class Sorter:
    """Context that uses sorting strategies."""

    def __init__(self, strategy):
        self.strategy = strategy

    def sort(self, data):
        return self.strategy.sort(data)

    def set_strategy(self, strategy):
        self.strategy = strategy

data = [64, 34, 25, 12, 22, 11, 90]
sorter = Sorter(BubbleSort())
print(f"BubbleSort: {sorter.sort(data.copy())}")
sorter.set_strategy(QuickSort())
print(f"QuickSort: {sorter.sort(data.copy())}")
sorter.set_strategy(MergeSort())
print(f"MergeSort: {sorter.sort(data.copy())}")


# =============================================================================
# Exercise 5: Plugin Architecture - Solution
# =============================================================================
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
        return data.upper()

class ReversePlugin(Plugin):
    """Reverse text."""

    @property
    def name(self):
        return "Reverse"

    def execute(self, data):
        return data[::-1]

class WordCountPlugin(Plugin):
    """Count words in text."""

    @property
    def name(self):
        return "WordCount"

    def execute(self, data):
        return len(data.split())

class PluginManager:
    """Manages and executes plugins."""

    def __init__(self):
        self.plugins = []

    def register(self, plugin):
        self.plugins.append(plugin)

    def execute_all(self, data):
        results = {}
        for plugin in self.plugins:
            results[plugin.name] = plugin.execute(data)
        return results

manager = PluginManager()
manager.register(UpperCasePlugin())
manager.register(ReversePlugin())
manager.register(WordCountPlugin())
results = manager.execute_all("Hello World")
for name, result in results.items():
    print(f"{name}: {result}")
# UpperCase: HELLO WORLD
# Reverse: dlroW olleH
# WordCount: 2
