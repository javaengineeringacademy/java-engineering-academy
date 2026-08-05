# Flyweight Pattern in Python

The Flyweight pattern minimizes memory usage by sharing as much data as possible with similar objects. Python's `__slots__` and caching make this pattern effective for memory optimization.

## When to Use

- Application uses many objects with shared intrinsic state
- Memory costs are high due to large number of objects
- Most object state can be made extrinsic
- Object identity is not required for equality
- String pooling, character rendering, or database connections

## Python Implementation

### Using `__slots__`
```python
class Flyweight:
    __slots__ = ['intrinsic_state']
    
    def __init__(self, intrinsic_state: str):
        self.intrinsic_state = intrinsic_state
    
    def operation(self, extrinsic_state: str):
        return f"Flyweight({self.intrinsic_state}, {extrinsic_state})"

class FlyweightFactory:
    _flyweights = {}
    
    @classmethod
    def get_flyweight(cls, key: str) -> Flyweight:
        if key not in cls._flyweights:
            cls._flyweights[key] = Flyweight(key)
        return cls._flyweights[key]
    
    @classmethod
    def get_flyweight_count(cls) -> int:
        return len(cls._flyweights)

# Usage
fw1 = FlyweightFactory.get_flyweight("shared")
fw2 = FlyweightFactory.get_flyweight("shared")
print(fw1 is fw2)  # True - same object
print(fw1.operation("external"))  # Flyweight(shared, external)
```

### Using LRU Cache
```python
from functools import lru_cache

class Character:
    def __init__(self, char: str, font: str):
        self.char = char
        self.font = font
    
    def render(self, x: int, y: int):
        return f"Render '{self.char}' in {self.font} at ({x},{y})"

@lru_cache(maxsize=128)
def get_character(char: str, font: str) -> Character:
    return Character(char, font)

# Usage
char1 = get_character('A', 'Arial')
char2 = get_character('A', 'Arial')
print(char1 is char2)  # True
```

### Dictionary-Based Pool
```python
class Color:
    def __init__(self, name: str, r: int, g: int, b: int):
        self.name = name
        self.r = r
        self.g = g
        self.b = b
    
    def __repr__(self):
        return f"Color({self.name}, {self.r}, {self.g}, {self.b})"

class ColorPool:
    _pool = {}
    
    @classmethod
    def get_color(cls, name: str, r: int, g: int, b: int) -> Color:
        key = (name, r, g, b)
        if key not in cls._pool:
            cls._pool[key] = Color(name, r, g, b)
        return cls._pool[key]
    
    @classmethod
    def clear(cls):
        cls._pool.clear()

# Usage
red1 = ColorPool.get_color("red", 255, 0, 0)
red2 = ColorPool.get_color("red", 255, 0, 0)
print(red1 is red2)  # True
```

## Pythonic Alternative

Use `functools.lru_cache` for memoization:
```python
from functools import lru_cache

@lru_cache(maxsize=256)
def expensive_computation(x: int) -> int:
    return x * x

# Automatically caches results
result = expensive_computation(42)
```

## Real-World Example

```python
class TreeType:
    def __init__(self, name: str, color: str, texture: str):
        self.name = name
        self.color = color
        self.texture = texture
    
    def draw(self, x: int, y: int):
        return f"Draw {self.name} at ({x},{y})"

class TreeFactory:
    _types = {}
    
    @classmethod
    def get_type(cls, name: str, color: str, texture: str) -> TreeType:
        key = (name, color, texture)
        if key not in cls._types:
            cls._types[key] = TreeType(name, color, texture)
        return cls._types[key]

class Tree:
    def __init__(self, x: int, y: int, type_: TreeType):
        self.x = x
        self.y = y
        self.type = type_
    
    def draw(self):
        return self.type.draw(self.x, self.y)

# Usage - 1000 trees, only 3 TreeType objects
types = [TreeFactory.get_type("Oak", "green", "rough") for _ in range(1000)]
```

## Best Practices

1. Use `__slots__` to reduce memory footprint
2. Cache expensive objects in factory
3. Separate intrinsic (shared) from extrinsic (unique) state
4. Use `lru_cache` for simple memoization needs
5. Monitor memory usage with `sys.getsizeof()`

## Interview Questions

1. What is the difference between Flyweight and Singleton?
2. How would you separate intrinsic from extrinsic state?
3. What are the performance trade-offs of Flyweight?
4. How does `__slots__` help in Flyweight pattern?
5. When would you NOT use Flyweight?

## References

- *Design Patterns* - GoF, Chapter 4
- Python `__slots__` documentation
- `functools.lru_cache` documentation
- *Python Cookbook* - Alex Martelli
