# Command Pattern in Python

The Command pattern encapsulates a request as an object, allowing parameterization of clients with different requests, queueing, logging, and support for undoable operations. Python's callable objects make this pattern natural.

## When to Use

- Undo/redo functionality
- Transaction logging and rollback
- Delayed or scheduled execution
- Decoupling sender from receiver
- Macro recording and playback

## Python Implementation

### Basic Command
```python
from abc import ABC, abstractmethod

class Command(ABC):
    @abstractmethod
    def execute(self):
        pass

    @abstractmethod
    def undo(self):
        pass

class TextEditor:
    def __init__(self):
        self.content = ""

    def insert(self, text):
        self.content += text

    def delete(self, length):
        self.content = self.content[:-length]

class InsertCommand(Command):
    def __init__(self, editor: TextEditor, text: str):
        self.editor = editor
        self.text = text

    def execute(self):
        self.editor.insert(self.text)

    def undo(self):
        self.editor.delete(len(self.text))

class DeleteCommand(Command):
    def __init__(self, editor: TextEditor, length: int):
        self.editor = editor
        self.length = length
        self.deleted_text = ""

    def execute(self):
        self.deleted_text = self.editor.content[-self.length:]
        self.editor.delete(self.length)

    def undo(self):
        self.editor.insert(self.deleted_text)

class CommandHistory:
    def __init__(self):
        self._history = []

    def push(self, command: Command):
        command.execute()
        self._history.append(command)

    def pop(self):
        if self._history:
            command = self._history.pop()
            command.undo()
```

### Callable Object Command
```python
class Light:
    def __init__(self, location: str):
        self.location = location
        self.is_on = False

    def toggle(self):
        self.is_on = not self.is_on
        return f"Light at {self.location} is {'on' if self.is_on else 'off'}"

class LightCommand:
    def __init__(self, light: Light):
        self.light = light
        self.previous_state = None

    def __call__(self):
        self.previous_state = self.light.is_on
        return self.light.toggle()

    def undo(self):
        self.light.is_on = self.previous_state
```

### Using `functools.partial`
```python
from functools import partial

class Printer:
    def print_doc(self, doc, copies=1):
        return f"Printing {doc} x{copies}"

printer = Printer()
commands = [
    partial(printer.print_doc, "report.pdf"),
    partial(printer.print_doc, "invoice.pdf", copies=2)
]

for cmd in commands:
    print(cmd())
```

## Pythonic Alternative

Use simple callables for straightforward commands:
```python
class Button:
    def __init__(self, action=None):
        self.action = action or (lambda: None)

    def press(self):
        return self.action()

# Usage
btn = Button(action=lambda: print("Clicked!"))
btn.press()
```

## Real-World Example

```python
import json

class InventoryManager:
    def __init__(self):
        self.stock = {}
        self.log = []

    def add_stock(self, item, quantity):
        self.stock[item] = self.stock.get(item, 0) + quantity
        self.log.append(("add", item, quantity))

    def remove_stock(self, item, quantity):
        if self.stock.get(item, 0) >= quantity:
            self.stock[item] -= quantity
            self.log.append(("remove", item, quantity))
            return True
        return False

    def undo_last(self):
        if self.log:
            action, item, qty = self.log.pop()
            if action == "add":
                self.stock[item] -= qty
            else:
                self.stock[item] = self.stock.get(item, 0) + qty
```

## Best Practices

1. Keep commands focused and single-purpose
2. Implement full undo support when needed
3. Use `functools.partial` for simple commands
4. Log commands for audit trails
5. Consider command queues for async processing

## Interview Questions

1. What problem does the Command pattern solve?
2. How would you implement undo/redo functionality?
3. What is the difference between Command and Strategy?
4. How would you serialize commands for persistence?
5. When would you use Command over direct method calls?

## References

- *Design Patterns* - GoF, Chapter 5
- `functools` documentation
- *Python Cookbook* - Alex Martelli
