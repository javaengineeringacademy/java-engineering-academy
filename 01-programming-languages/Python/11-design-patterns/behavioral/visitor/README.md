# Visitor Pattern in Python

The Visitor pattern represents an operation to be performed on elements of an object structure. It lets you define a new operation without changing the classes of the elements on which it operates. Python's `singledispatch` makes this pattern elegant.

## When to Use

- Operations on complex object structures
- Avoiding polluting element classes with operations
- Related operations should be grouped together
- Many unrelated operations on object structures
- Compiler design, AST traversal, document processing

## Python Implementation

### Using `singledispatch`
```python
from functools import singledispatch

class TextNode:
    def __init__(self, text: str):
        self.text = text

class ImageNode:
    def __init__(self, url: str, alt: str):
        self.url = url
        self.alt = alt

class VideoNode:
    def __init__(self, url: str, duration: int):
        self.url = url
        self.duration = duration

@singledispatch
def render(node):
    raise NotImplementedError

@render.register(TextNode)
def _(node):
    return f"<p>{node.text}</p>"

@render.register(ImageNode)
def _(node):
    return f'<img src="{node.url}" alt="{node.alt}">'

@render.register(VideoNode)
def _(node):
    return f'<video src="{node.url}">{node.duration}s</video>'

# Usage
nodes = [TextNode("Hello"), ImageNode("img.jpg", "Photo")]
for node in nodes:
    print(render(node))
```

### Classic Visitor Pattern
```python
from abc import ABC, abstractmethod

class Element(ABC):
    @abstractmethod
    def accept(self, visitor: "Visitor"):
        pass

class Visitor(ABC):
    @abstractmethod
    def visit_element_a(self, element: "ElementA"):
        pass

    @abstractmethod
    def visit_element_b(self, element: "ElementB"):
        pass

class ElementA(Element):
    def accept(self, visitor: Visitor):
        visitor.visit_element_a(self)

class ElementB(Element):
    def accept(self, visitor: Visitor):
        visitor.visit_element_b(self)

class ConcreteVisitor(Visitor):
    def visit_element_a(self, element: ElementA):
        return "Visiting Element A"

    def visit_element_b(self, element: ElementB):
        return "Visiting Element B"

# Usage
elements = [ElementA(), ElementB()]
visitor = ConcreteVisitor()
for element in elements:
    print(element.accept(visitor))
```

### Dictionary-Based Dispatch
```python
class DocumentVisitor:
    def __init__(self):
        self._handlers = {}

    def register(self, node_type, handler):
        self._handlers[node_type] = handler

    def visit(self, node):
        handler = self._handlers.get(type(node))
        if handler:
            return handler(node)
        raise TypeError(f"No handler for {type(node)}")

class Heading:
    def __init__(self, text: str, level: int):
        self.text = text
        self.level = level

class Paragraph:
    def __init__(self, text: str):
        self.text = text

# Usage
visitor = DocumentVisitor()
visitor.register(Heading, lambda h: f"<h{h.level}>{h.text}</h{h.level}>")
visitor.register(Paragraph, lambda p: f"<p>{p.text}</p>")

print(visitor.visit(Heading("Title", 1)))
print(visitor.visit(Paragraph("Content")))
```

## Pythonic Alternative

Use pattern matching (Python 3.10+):
```python
def process_node(node):
    match node:
        case TextNode(text):
            return f"<p>{text}</p>"
        case ImageNode(url, alt):
            return f'<img src="{url}" alt="{alt}">'
        case _:
            raise TypeError(f"Unknown node: {type(node)}")
```

## Real-World Example

```python
from ast import NodeVisitor, parse

class VariableFinder(NodeVisitor):
    def __init__(self):
        self.variables = []

    def visit_Name(self, node):
        self.variables.append(node.id)
        self.generic_visit(node)

# Usage
code = "x = 1\ny = x + 2"
tree = parse(code)
finder = VariableFinder()
finder.visit(tree)
print(finder.variables)  # ['x', 'y', 'x']
```

## Best Practices

1. Use `singledispatch` for simple visitor implementations
2. Keep visitor methods focused
3. Consider double dispatch for complex hierarchies
4. Document which node types each visitor supports
5. Use AST visitors for code analysis tools

## Interview Questions

1. How does `singledispatch` simplify Visitor pattern?
2. What is double dispatch and when is it needed?
3. When would you use Visitor over polymorphism?
4. How would you add a new element type without modifying visitors?
5. What are the testing strategies for visitors?

## References

- *Design Patterns* - GoF, Chapter 5
- `functools.singledispatch` documentation
- Python `ast` module documentation
- *Fluent Python* - Luciano Ramalho
