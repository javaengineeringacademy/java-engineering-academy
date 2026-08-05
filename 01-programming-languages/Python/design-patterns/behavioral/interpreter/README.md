# Interpreter Pattern in Python

The Interpreter pattern defines a grammatical representation for a language and provides an interpreter to deal with this grammar. Python's dynamic nature and `eval`/`exec` functions can facilitate simple interpreters.

## When to Use

- Simple grammar interpretation
- SQL parsing, mathematical expressions
- Configuration file parsing
- Domain-specific languages (DSL)
- Regular expression matching

## Python Implementation

### Abstract Syntax Tree Interpreter
```python
from abc import ABC, abstractmethod

class Expression(ABC):
    @abstractmethod
    def interpret(self) -> float:
        pass

class Number(Expression):
    def __init__(self, value: float):
        self.value = value

    def interpret(self) -> float:
        return self.value

class Add(Expression):
    def __init__(self, left: Expression, right: Expression):
        self.left = left
        self.right = right

    def interpret(self) -> float:
        return self.left.interpret() + self.right.interpret()

class Subtract(Expression):
    def __init__(self, left: Expression, right: Expression):
        self.left = left
        self.right = right

    def interpret(self) -> float:
        return self.left.interpret() - self.right.interpret()

class Multiply(Expression):
    def __init__(self, left: Expression, right: Expression):
        self.left = left
        self.right = right

    def interpret(self) -> float:
        return self.left.interpret() * self.right.interpret()

# Usage: (3 + 5) * 2
expression = Multiply(
    Add(Number(3), Number(5)),
    Number(2)
)
print(expression.interpret())  # 16.0
```

### Simple Parser
```python
class Calculator:
    def __init__(self):
        self.operators = {
            '+': lambda a, b: a + b,
            '-': lambda a, b: a - b,
            '*': lambda a, b: a * b,
            '/': lambda a, b: a / b
        }

    def evaluate(self, expression: str) -> float:
        tokens = expression.split()
        stack = []

        for token in tokens:
            if token in self.operators:
                b = stack.pop()
                a = stack.pop()
                stack.append(self.operators[token](a, b))
            else:
                stack.append(float(token))

        return stack[0]

# Usage (postfix notation)
calc = Calculator()
print(calc.evaluate("3 5 + 2 *"))  # 16.0
```

### Rule-Based Interpreter
```python
class Rule:
    def __init__(self, condition, action):
        self.condition = condition
        self.action = action

    def evaluate(self, context: dict):
        if self.condition(context):
            return self.action(context)
        return None

class RuleEngine:
    def __init__(self):
        self.rules = []

    def add_rule(self, rule: Rule):
        self.rules.append(rule)

    def execute(self, context: dict):
        results = []
        for rule in self.rules:
            result = rule.evaluate(context)
            if result:
                results.append(result)
        return results

# Usage
engine = RuleEngine()
engine.add_rule(Rule(
    lambda ctx: ctx.get("age", 0) >= 18,
    lambda ctx: "Adult access granted"
))
engine.add_rule(Rule(
    lambda ctx: ctx.get("vip", False),
    lambda ctx: "VIP access granted"
))

print(engine.execute({"age": 25, "vip": True}))
```

## Pythonic Alternative

Use `eval` with restricted globals for simple expressions:
```python
def safe_eval(expression: str, variables: dict = None):
    allowed = {"__builtins__": {}}
    if variables:
        allowed.update(variables)
    return eval(expression, allowed)

# Usage
result = safe_eval("x + y * 2", {"x": 3, "y": 5})
print(result)  # 13
```

## Real-World Example

```python
class QueryLanguage:
    def __init__(self):
        self.data = []

    def load(self, data: list):
        self.data = data
        return self

    def where(self, condition: str):
        self.data = [
            item for item in self.data
            if eval(condition, {"__builtins__": {}}, item)
        ]
        return self

    def select(self, *fields):
        return [
            {field: item[field] for field in fields if field in item}
            for item in self.data
        ]

# Usage
data = [
    {"name": "Alice", "age": 30},
    {"name": "Bob", "age": 25}
]
result = (QueryLanguage()
    .load(data)
    .where("age >= 28")
    .select("name"))
print(result)  # [{'name': 'Alice'}]
```

## Best Practices

1. Use AST for complex grammars
2. Implement proper error handling
3. Consider PLY or pyparsing for production parsers
4. Restrict eval/exec for security
5. Document grammar rules clearly

## Interview Questions

1. What is the difference between Interpreter and Visitor?
2. How would you implement operator precedence?
3. What are the security concerns with eval/exec?
4. When would you use a parsing library vs custom interpreter?
5. How would you add new operators to an existing interpreter?

## References

- *Design Patterns* - GoF, Chapter 5
- Python `ast` module documentation
- PLY (Python Lex-Yacc) documentation
- `pyparsing` library documentation
