# Python Operators Reference

## What are Python Operators?

Python operators are special symbols that perform operations on variables and values. They are the building blocks of expressions and control the behavior of code.

## Why does Python Operators matter?

Understanding operators helps you:
- Write concise and efficient expressions
- Understand code precedence and associativity
- Use operators effectively for different data types
- Avoid common operator-related bugs

---

## 1. Arithmetic Operators

```python
# Addition
print(5 + 3)      # 8
print(5.5 + 3.5)  # 9.0
print([1, 2] + [3, 4])  # [1, 2, 3, 4]

# Subtraction
print(5 - 3)      # 2
print(3 - 5)      # -2

# Multiplication
print(5 * 3)      # 15
print('ha' * 3)   # 'hahaha'
print([0] * 5)    # [0, 0, 0, 0, 0]

# Division (float result)
print(5 / 3)      # 1.6666666666666667
print(5 / 2)      # 2.5

# Floor Division
print(5 // 3)     # 1
print(-5 // 3)    # -2 (rounds toward negative infinity)

# Modulo
print(5 % 3)      # 2
print(-5 % 3)     # 1 (always non-negative result)

# Exponentiation
print(5 ** 3)     # 125
print(5 ** -1)    # 0.2
```

---

## 2. Comparison Operators

```python
# Equal
print(5 == 5)      # True
print(5 == 5.0)    # True
print([1, 2] == [1, 2])  # True

# Not Equal
print(5 != 3)      # True

# Greater Than
print(5 > 3)       # True

# Less Than
print(5 < 3)       # False

# Greater Than or Equal
print(5 >= 5)      # True

# Less Than or Equal
print(5 <= 3)      # False

# Chained Comparisons
x = 5
print(1 < x < 10)  # True
print(1 < x < 3)   # False
```

---

## 3. Logical Operators

```python
# AND
print(True and True)    # True
print(True and False)   # False
print(False and True)   # False
print(False and False)  # False

# OR
print(True or True)     # True
print(True or False)    # True
print(False or True)    # True
print(False or False)   # False

# NOT
print(not True)         # False
print(not False)        # True

# Short-circuit evaluation
def expensive():
    print("Expensive operation")
    return True

# Only calls expensive() if x is True
x = False
if x and expensive():
    pass  # expensive() is NOT called

x = True
if x and expensive():
    pass  # expensive() IS called
```

---

## 4. Bitwise Operators

```python
# AND
print(12 & 8)    # 8 (1100 & 1000 = 1000)

# OR
print(12 | 8)    # 12 (1100 | 1000 = 1100)

# XOR
print(12 ^ 8)    # 4 (1100 ^ 1000 = 0100)

# NOT
print(~12)       # -13 (inverts all bits)

# Left Shift
print(12 << 2)   # 48 (1100 << 2 = 110000)

# Right Shift
print(12 >> 2)   # 3 (1100 >> 2 = 11)
```

---

## 5. Assignment Operators

```python
# Basic assignment
x = 10

# Augmented assignment
x += 5    # x = x + 5
x -= 3    # x = x - 3
x *= 2    # x = x * 2
x /= 4    # x = x / 4
x //= 3   # x = x // 3
x **= 2   # x = x ** 2
x %= 5    # x = x % 5

# Bitwise assignment
x &= 3    # x = x & 3
x |= 3    # x = x | 3
x ^= 3    # x = x ^ 3
x <<= 2   # x = x << 2
x >>= 2   # x = x >> 2

# Walrus operator (Python 3.8+)
if (n := len([1, 2, 3])) > 2:
    print(f"List has {n} elements")
```

---

## 6. Identity Operators

```python
# is - checks if two variables refer to the same object
a = [1, 2, 3]
b = [1, 2, 3]
c = a

print(a is b)      # False (different objects)
print(a is c)      # True (same object)
print(a is not b)  # True

# is vs ==
# is checks identity (same object)
# == checks equality (same value)

# Example
x = 256
y = 256
print(x is y)  # True (small integers are cached)

x = 257
y = 257
print(x is y)  # False (not cached)
```

---

## 7. Membership Operators

```python
# in - checks if value is in sequence
print(1 in [1, 2, 3])          # True
print('a' in 'abc')            # True
print('x' in 'abc')            # False
print(1 in {1: 'a', 2: 'b'})  # True (checks keys)

# not in
print(4 not in [1, 2, 3])      # True

# Works with custom __contains__ method
class Range:
    def __init__(self, start, end):
        self.start = start
        self.end = end
    
    def __contains__(self, value):
        return self.start <= value <= self.end

r = Range(1, 10)
print(5 in r)   # True
print(15 in r)  # False
```

---

## 8. Ternary Operator

```python
# Conditional expression
x = 10
result = "positive" if x > 0 else "non-positive"
print(result)  # positive

# Nested ternary
x = 0
result = "positive" if x > 0 else "zero" if x == 0 else "negative"
print(result)  # zero

# In list comprehension
numbers = [1, -2, 3, -4, 5]
absolute = [x if x > 0 else -x for x in numbers]
print(absolute)  # [1, 2, 3, 4, 5]
```

---

## 9. Operator Precedence

From highest to lowest:

| Operator | Description |
|----------|-------------|
| `()` | Parentheses |
| `**` | Exponentiation |
| `+x`, `-x`, `~x` | Unary plus, minus, bitwise NOT |
| `*`, `/`, `//`, `%` | Multiplication, division, floor division, modulo |
| `+`, `-` | Addition, subtraction |
| `<<`, `>>` | Bitwise shifts |
| `&` | Bitwise AND |
| `^` | Bitwise XOR |
| `\|` | Bitwise OR |
| `==`, `!=`, `<`, `>`, `<=`, `>=`, `is`, `in` | Comparisons, identity, membership |
| `not` | Logical NOT |
| `and` | Logical AND |
| `or` | Logical OR |
| `:=` | Walrus operator |
| `lambda` | Lambda expression |

### Examples

```python
# Precedence examples
print(2 + 3 * 4)      # 14 (multiplication first)
print((2 + 3) * 4)    # 20 (parentheses first)
print(2 ** 3 ** 2)     # 512 (right-associative: 2**(3**2))
print(not True or False)  # False (not first, then or)
```

---

## 10. Operator Overloading

```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)
    
    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y)
    
    def __mul__(self, scalar):
        return Vector(self.x * scalar, self.y * scalar)
    
    def __eq__(self, other):
        return self.x == other.x and self.y == other.y
    
    def __repr__(self):
        return f"Vector({self.x}, {self.y})"

v1 = Vector(1, 2)
v2 = Vector(3, 4)

print(v1 + v2)    # Vector(4, 6)
print(v1 - v2)    # Vector(-2, -2)
print(v1 * 3)     # Vector(3, 6)
print(v1 == v2)   # False
```

---

## One-Minute Revision Table

| Operator | Example | Description |
|----------|---------|-------------|
| `+` | `5 + 3` | Addition |
| `-` | `5 - 3` | Subtraction |
| `*` | `5 * 3` | Multiplication |
| `/` | `5 / 3` | Division |
| `//` | `5 // 3` | Floor Division |
| `%` | `5 % 3` | Modulo |
| `**` | `5 ** 3` | Exponentiation |
| `==` | `5 == 5` | Equal |
| `!=` | `5 != 3` | Not Equal |
| `>` | `5 > 3` | Greater Than |
| `<` | `5 < 3` | Less Than |
| `>=` | `5 >= 5` | Greater or Equal |
| `<=` | `5 <= 3` | Less or Equal |
| `and` | `True and False` | Logical AND |
| `or` | `True or False` | Logical OR |
| `not` | `not True` | Logical NOT |
| `is` | `a is b` | Identity |
| `in` | `1 in [1,2]` | Membership |
| `&` | `12 & 8` | Bitwise AND |
| `\|` | `12 \| 8` | Bitwise OR |
| `^` | `12 ^ 8` | Bitwise XOR |
| `<<` | `12 << 2` | Left Shift |
| `>>` | `12 >> 2` | Right Shift |

---

## Common Mistakes

### 1. Using `==` Instead of `is` for None

```python
# WRONG
if x == None:
    pass

# RIGHT
if x is None:
    pass
```

### 2. Integer Caching

```python
# Small integers are cached
a = 256
b = 256
print(a is b)  # True

# Large integers are not cached
a = 257
b = 257
print(a is b)  # False
```

### 3. Floor Division with Negative Numbers

```python
# WRONG
print(-5 // 3)  # -2 (not -1)

# RIGHT
import math
print(math.floor(-5 / 3))  # -2
```

### 4. Modulo with Negative Numbers

```python
# WRONG
print(-5 % 3)  # 1 (not -2)

# RIGHT
print(-5 % 3)  # 1
```

---

## Production Notes

1. **Use parentheses for clarity** - Even if operator precedence is clear
2. **Be careful with floating-point comparison** - Use `math.isclose()` for approximate equality
3. **Use `is` for None, True, False** - Identity checks are faster
4. **Use short-circuit evaluation** - For performance and safety
5. **Use bitwise operators for flags** - More efficient than boolean operations
6. **Use walrus operator for efficiency** - Avoids redundant calculations
7. **Overload operators thoughtfully** - Don't abuse operator overloading
8. **Use `operator` module** - For functional programming with operators
9. **Be aware of associativity** - Right-associative for `**`
10. **Test edge cases** - Zero, negative, large numbers

---

## Further Reading

- Python documentation on expressions
- Python documentation on operator module
- Fluent Python by Luciano Ramalho
- Python Cookbook by David Beazley
