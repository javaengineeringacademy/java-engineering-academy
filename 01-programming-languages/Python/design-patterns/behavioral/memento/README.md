# Memento Pattern in Python

The Memento pattern provides the ability to restore an object to its previous state. Python's `copy` module and `pickle` make state capture and restoration straightforward.

## When to Use

- Undo/redo functionality
- State persistence and restoration
- Transaction rollback
- Game save states
- Snapshot-based versioning

## Python Implementation

### Basic Memento
```python
import copy
from typing import List, Any

class Memento:
    def __init__(self, state: Any):
        self._state = copy.deepcopy(state)

    def get_state(self) -> Any:
        return copy.deepcopy(self._state)

class Originator:
    def __init__(self):
        self._state = {}

    def set_state(self, state: dict):
        self._state = state

    def get_state(self) -> dict:
        return copy.deepcopy(self._state)

    def save(self) -> Memento:
        return Memento(self._state)

    def restore(self, memento: Memento):
        self._state = memento.get_state()

class Caretaker:
    def __init__(self, originator: Originator):
        self._originator = originator
        self._history: List[Memento] = []

    def save_state(self):
        self._history.append(self._originator.save())

    def undo(self):
        if self._history:
            memento = self._history.pop()
            self._originator.restore(memento)

    def get_history_size(self) -> int:
        return len(self._history)

# Usage
originator = Originator()
caretaker = Caretaker(originator)

originator.set_state({"x": 1})
caretaker.save_state()

originator.set_state({"x": 2})
caretaker.save_state()

print(originator.get_state())  # {'x': 2}
caretaker.undo()
print(originator.get_state())  # {'x': 1}
```

### Using Pickle
```python
import pickle

class GameState:
    def __init__(self):
        self.player_pos = [0, 0]
        self.health = 100
        self.inventory = []

    def save(self, filename: str):
        with open(filename, 'wb') as f:
            pickle.dump(self.__dict__, f)

    def load(self, filename: str):
        with open(filename, 'rb') as f:
            self.__dict__ = pickle.load(f)

# Usage
game = GameState()
game.player_pos = [10, 20]
game.health = 80
game.save("savegame.pkl")

game.health = 0
game.load("savegame.pkl")
print(game.health)  # 80
```

### Snapshot-Based Memento
```python
import time
import copy

class Snapshot:
    def __init__(self, state: dict, timestamp: float = None):
        self.state = copy.deepcopy(state)
        self.timestamp = timestamp or time.time()

class VersionedDocument:
    def __init__(self):
        self._content = ""
        self._snapshots = []

    @property
    def content(self):
        return self._content

    @content.setter
    def content(self, value):
        self._content = value

    def create_snapshot(self) -> Snapshot:
        snapshot = Snapshot({"content": self._content})
        self._snapshots.append(snapshot)
        return snapshot

    def restore_snapshot(self, snapshot: Snapshot):
        self._content = snapshot.state["content"]

    def get_versions(self) -> list:
        return [(s.timestamp, s.state) for s in self._snapshots]

# Usage
doc = VersionedDocument()
doc.content = "Version 1"
snapshot1 = doc.create_snapshot()

doc.content = "Version 2"
snapshot2 = doc.create_snapshot()

doc.restore_snapshot(snapshot1)
print(doc.content)  # Version 1
```

## Pythonic Alternative

Use dataclasses with `asdict`:
```python
from dataclasses import dataclass, field, asdict
import copy

@dataclass
class State:
    x: int = 0
    y: int = 0
    data: list = field(default_factory=list)

class StateManager:
    def __init__(self):
        self._history = []

    def save(self, state: State):
        self._history.append(asdict(state))

    def restore(self) -> State:
        if self._history:
            return State(**self._history.pop())
        return State()
```

## Real-World Example

```python
class TextEditor:
    def __init__(self):
        self._text = ""
        self._clipboard = ""
        self._history = []

    def type(self, words: str):
        self._save_state()
        self._text += words

    def _save_state(self):
        self._history.append({
            "text": self._text,
            "clipboard": self._clipboard
        })

    def undo(self):
        if self._history:
            state = self._history.pop()
            self._text = state["text"]
            self._clipboard = state["clipboard"]

    @property
    def text(self):
        return self._text
```

## Best Practices

1. Use `copy.deepcopy` for complex state
2. Limit history size to prevent memory issues
3. Consider using `pickle` for serializable state
4. Make memento immutable
5. Document state structure clearly

## Interview Questions

1. What is the difference between Memento and Command?
2. How would you implement undo for a complex object graph?
3. What are the memory implications of Memento pattern?
4. How would you persist mementos across application restarts?
5. When would you use snapshot vs incremental memento?

## References

- *Design Patterns* - GoF, Chapter 5
- Python `copy` module documentation
- `pickle` module documentation
- *Python Cookbook* - Alex Martelli
