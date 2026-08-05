# Composite Pattern in Python

The Composite pattern composes objects into tree structures to represent part-whole hierarchies. Python's duck typing makes this pattern particularly elegant, as components don't need formal interfaces.

## When to Use

- Representing part-whole hierarchies (trees)
- Wanting clients to treat individual and composed objects uniformly
- Building file systems, GUI widgets, or organizational structures
- When operations apply to both leaves and composite objects
- Recursive data structures

## Python Implementation

### File System Example
```python
from abc import ABC, abstractmethod
from typing import List

class FileSystemItem(ABC):
    def __init__(self, name: str):
        self.name = name
    
    @abstractmethod
    def get_size(self) -> int:
        pass
    
    @abstractmethod
    def display(self, indent: int = 0) -> str:
        pass

class File(FileSystemItem):
    def __init__(self, name: str, size: int):
        super().__init__(name)
        self.size = size
    
    def get_size(self) -> int:
        return self.size
    
    def display(self, indent: int = 0) -> str:
        return f"{'  ' * indent}{self.name} ({self.size} bytes)"

class Directory(FileSystemItem):
    def __init__(self, name: str):
        super().__init__(name)
        self._children: List[FileSystemItem] = []
    
    def add(self, item: FileSystemItem) -> "Directory":
        self._children.append(item)
        return self
    
    def remove(self, item: FileSystemItem):
        self._children.remove(item)
    
    def get_size(self) -> int:
        return sum(child.get_size() for child in self._children)
    
    def display(self, indent: int = 0) -> str:
        lines = [f"{'  ' * indent}{self.name}/"]
        for child in self._children:
            lines.append(child.display(indent + 1))
        return "\n".join(lines)

# Usage
root = Directory("root")
root.add(File("file1.txt", 100))
subdir = Directory("subdir")
subdir.add(File("file2.txt", 200))
root.add(subdir)

print(root.display())
print(f"Total size: {root.get_size()}")
```

### Duck Typing Approach
```python
class Leaf:
    def __init__(self, value):
        self.value = value
    
    def operation(self):
        return self.value

class Composite:
    def __init__(self):
        self._children = []
    
    def add(self, child):
        self._children.append(child)
        return self
    
    def operation(self):
        return sum(child.operation() for child in self._children)

# Works without shared interface due to duck typing
leaf1 = Leaf(10)
leaf2 = Leaf(20)
composite = Composite()
composite.add(leaf1).add(leaf2)
print(composite.operation())  # 30
```

## Pythonic Alternative

Use dataclasses for simple composites:
```python
from dataclasses import dataclass, field
from typing import List

@dataclass
class Component:
    name: str
    children: List["Component"] = field(default_factory=list)
    
    def total(self):
        if not self.children:
            return 1
        return sum(child.total() for child in self.children)
```

## Real-World Example

```python
class Widget:
    def __init__(self, name: str):
        self.name = name
        self.children = []
    
    def add(self, widget):
        self.children.append(widget)
        return self
    
    def render(self, indent: int = 0):
        result = f"{'  ' * indent}<{self.name}>\n"
        for child in self.children:
            result += child.render(indent + 1)
        result += f"{'  ' * indent}</{self.name}>\n"
        return result

# Build UI tree
root = Widget("div")
root.add(Widget("h1")).add(Widget("p"))
print(root.render())
```

## Best Practices

1. Use duck typing when components share behavior but not type
2. Implement `__iter__` for traversal support
3. Consider visitor pattern for operations on composites
4. Keep component interface consistent
5. Use recursion for tree operations

## Interview Questions

1. What is the difference between Composite and Decorator?
2. How would you traverse a composite tree?
3. When would you use Composite over inheritance?
4. How does duck typing simplify Composite in Python?
5. What are the trade-offs of using Composite?

## References

- *Design Patterns* - GoF, Chapter 4
- Python `collections.abc` documentation
- *Fluent Python* - Luciano Ramalho
- PEP 484 - Type Hints
