# Python Design Patterns

Common patterns and when to use them.

## Singleton

```python
def singleton(cls):
    instances = {}
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return get_instance

@singleton
class Database:
    def __init__(self):
        self.connection = "connected"
```

## Factory

```python
class Dog:
    def speak(self): return "Woof"

class Cat:
    def speak(self): return "Meow"

def animal_factory(animal_type):
    animals = {"dog": Dog, "cat": Cat}
    return animals[animal_type]()

animal = animal_factory("dog")
```

## Observer

```python
class EventEmitter:
    def __init__(self):
        self._listeners = {}

    def on(self, event, callback):
        self._listeners.setdefault(event, []).append(callback)

    def emit(self, event, *args, **kwargs):
        for callback in self._listeners.get(event, []):
            callback(*args, **kwargs)
```

## Strategy

```python
class Sorter:
    def __init__(self, strategy=None):
        self._strategy = strategy

    def sort(self, data):
        return self._strategy(data) if self._strategy else sorted(data)

bubble_sort = lambda lst: lst  # simplified
quick_sort = lambda lst: sorted(lst)

sorter = Sorter(strategy=quick_sort)
```

## Decorator (Pattern)

```python
def memoize(func):
    cache = {}
    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]
    return wrapper

@memoize
def fibonacci(n):
    if n < 2: return n
    return fibonacci(n-1) + fibonacci(n-2)
```

## Context Manager

```python
class Timer:
    def __enter__(self):
        import time
        self.start = time.time()
        return self

    def __exit__(self, *args):
        import time
        self.elapsed = time.time() - self.start
```

## Builder

```python
class QueryBuilder:
    def __init__(self):
        self._table = ""
        self._conditions = []
        self._limit = None

    def table(self, name):
        self._table = name
        return self

    def where(self, condition):
        self._conditions.append(condition)
        return self

    def limit(self, n):
        self._limit = n
        return self

    def build(self):
        q = f"SELECT * FROM {self._table}"
        if self._conditions:
            q += " WHERE " + " AND ".join(self._conditions)
        if self._limit:
            q += f" LIMIT {self._limit}"
        return q

query = QueryBuilder().table("users").where("age > 18").limit(10).build()
```
