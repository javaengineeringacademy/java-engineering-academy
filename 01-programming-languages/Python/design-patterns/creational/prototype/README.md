# Prototype Pattern in Python

The Prototype pattern creates new objects by copying an existing object (prototype). Python's `copy` module provides built-in support for shallow and deep copying, making this pattern straightforward to implement.

## When to Use

- Creating objects is expensive (database calls, network requests)
- You want to avoid subclasses of an object creator
- Objects have few combinations of state
- You need many similar objects with slight variations
- Runtime type determination isn't needed

## Python Implementation

### Using `copy` Module
```python
import copy
from dataclasses import dataclass, field
from typing import List

@dataclass
class Document:
    title: str
    content: str
    metadata: dict = field(default_factory=dict)
    tags: List[str] = field(default_factory=list)
    
    def clone(self, deep: bool = True):
        if deep:
            return copy.deepcopy(self)
        return copy.copy(self)

# Usage
original = Document(
    title="Report",
    content="Initial content",
    metadata={"author": "John"},
    tags=["draft"]
)

copy1 = original.clone()
copy1.title = "Report v2"
copy1.tags.append("final")

print(original.title)  # "Report"
print(copy1.title)     # "Report v2"
```

### Prototype Registry
```python
import copy

class PrototypeRegistry:
    def __init__(self):
        self._prototypes = {}
    
    def register(self, name: str, prototype):
        self._prototypes[name] = prototype
    
    def unregister(self, name: str):
        del self._prototypes[name]
    
    def clone(self, name: str, **kwargs):
        prototype = self._prototypes[name]
        clone = copy.deepcopy(prototype)
        for key, value in kwargs.items():
            setattr(clone, key, value)
        return clone

# Usage
registry = PrototypeRegistry()
registry.register("basic_report", Document(title="Basic", content=""))

report = registry.clone("basic_report", title="Custom Report")
```

### Custom Clone Method
```python
class Shape:
    def __init__(self, color: str, x: int, y: int):
        self.color = color
        self.x = x
        self.y = y
    
    def clone(self):
        return Shape(self.color, self.x, self.y)
    
    def __repr__(self):
        return f"Shape({self.color}, {self.x}, {self.y})"

circle = Shape("red", 10, 20)
circle_clone = circle.clone()
```

## Pythonic Alternative

Use dataclasses with `field(default_factory)` for mutable defaults:
```python
from dataclasses import dataclass, field

@dataclass
class Config:
    name: str
    settings: dict = field(default_factory=dict)
    
    def with_settings(self, **kwargs):
        new_config = Config(self.name, self.settings.copy())
        new_config.settings.update(kwargs)
        return new_config
```

## Real-World Example

```python
import copy

class GameCharacter:
    def __init__(self, name: str, health: int, skills: list):
        self.name = name
        self.health = health
        self.skills = skills
    
    def create_npc(self, name: str):
        npc = copy.deepcopy(self)
        npc.name = name
        return npc

# Template character
warrior = GameCharacter("Template", 100, ["slash", "shield"])

npc1 = warrior.create_npc("Guard")
npc2 = warrior.create_npc("Soldier")
```

## Best Practices

1. Use `copy.deepcopy()` for objects with nested mutable structures
2. Implement `__copy__` and `__deepcopy__` for custom copy behavior
3. Keep prototype registry centralized
4. Document which attributes are copied shallow vs deep
5. Consider performance implications of deep copying large objects

## Interview Questions

1. What is the difference between shallow and deep copy?
2. When would you use Prototype over Factory?
3. How would you handle circular references in Prototype?
4. What are the performance considerations of deep copying?
5. How would you make Prototype thread-safe?

## References

- *Design Patterns* - GoF, Chapter 3
- Python `copy` module documentation
- PEP 3141 - Abstract Base Classes
- *Python Cookbook* - Alex Martelli
