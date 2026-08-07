# Python Enum Reference

## What are Enums?

Enums (enumerations) are a set of symbolic names bound to unique, constant values. They are used to represent a fixed set of choices or states.

## Why do Enums matter?

Understanding Enums helps you:
- Create readable constants
- Prevent invalid values
- Improve code clarity
- Implement type safety

---

## 1. Basic Enum

```python
from enum import Enum

class Color(Enum):
    RED = 1
    GREEN = 2
    BLUE = 3

# Usage
print(Color.RED)        # Color.RED
print(Color.RED.value)  # 1
print(Color.RED.name)   # 'RED'

# Access by value
print(Color(1))  # Color.RED

# Access by name
print(Color['RED'])  # Color.RED

# Iterate
for color in Color:
    print(color)
```

---

## 2. IntEnum

```python
from enum import IntEnum

class Priority(IntEnum):
    LOW = 1
    MEDIUM = 2
    HIGH = 3

# Can compare with integers
print(Priority.HIGH > 2)  # True
print(Priority.HIGH == 3)  # True
```

---

## 3. Auto

```python
from enum import Enum, auto

class Color(Enum):
    RED = auto()
    GREEN = auto()
    BLUE = auto()

print(Color.RED.value)  # 1
print(Color.GREEN.value)  # 2
print(Color.BLUE.value)  # 3
```

---

## 4. Flag

```python
from enum import Flag, auto

class Permission(Flag):
    READ = auto()
    WRITE = auto()
    EXECUTE = auto()

# Combine permissions
perm = Permission.READ | Permission.WRITE
print(perm)  # Permission.READ|WRITE
print(perm in Permission.READ)  # True
```

---

## 5. Functional API

```python
from enum import Enum

# Create enum dynamically
Color = Enum('Color', ['RED', 'GREEN', 'BLUE'])
print(Color.RED.value)  # 1

# From values
Animal = Enum('Animal', {'ANT': 1, 'BEE': 2, 'CAT': 3})
print(Animal.ANT.value)  # 1
```

---

## 6. Methods

```python
from enum import Enum

class Color(Enum):
    RED = 1
    GREEN = 2
    BLUE = 3
    
    def describe(self):
        return f"{self.name} is color #{self.value}"

print(Color.RED.describe())  # RED is color #1
```

---

## One-Minute Revision Table

| Type | Description | Example |
|------|-------------|---------|
| **Enum** | Basic enum | `class Color(Enum):` |
| **IntEnum** | Integer enum | `class Priority(IntEnum):` |
| **Flag** | Combinable flags | `class Permission(Flag):` |
| **auto** | Auto values | `RED = auto()` |
| **.value** | Get value | `Color.RED.value` |
| **.name** | Get name | `Color.RED.name` |

---

## Common Mistakes

### 1. Comparing with `==`

```python
# WRONG
if Color.RED == 1:  # False

# RIGHT
if Color.RED.value == 1:  # True
```

### 2. Modifying Enum Values

```python
# WRONG
class Color(Enum):
    RED = 1

Color.RED = 2  # AttributeError

# RIGHT (use functional API for dynamic)
Color = Enum('Color', ['RED', 'GREEN'])
```

### 3. Using Enum as Dict Key

```python
# WRONG (may cause issues)
d = {Color.RED: 'red'}

# RIGHT (use value as key)
d = {Color.RED.value: 'red'}
```

---

## Production Notes

1. **Use Enum for fixed choices** - More readable than constants
2. **Use IntEnum for integer comparison** - When you need numeric operations
3. **Use Flag for bitwise operations** - Permissions, options
4. **Use auto() for auto-incrementing values** - Less manual work
5. **Use functional API for dynamic enums** - When values come from data
6. **Override methods for custom behavior** - Add utility methods
7. **Use __missing__ for missing values** - Handle unknown values
8. **Be careful with pickling** - Enums may not pickle correctly
9. **Document enum values** - Especially for flags
10. **Use enum for type safety** - Prevent invalid values

---

## Further Reading

- Python documentation on enum module
- PEP 435 - Enum
- enum documentation
