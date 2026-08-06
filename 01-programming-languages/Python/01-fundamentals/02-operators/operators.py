"""Arithmetic, comparison, logical, and other operators."""

# ── Arithmetic Operators ─────────────────────────────────────────────
a, b = 17, 5
print(f"Add:       {a + b}")    # 22
print(f"Subtract:  {a - b}")    # 12
print(f"Multiply:  {a * b}")    # 85
print(f"Divide:    {a / b}")    # 3.4 (float)
print(f"Floor div: {a // b}")   # 3   (int)
print(f"Modulus:   {a % b}")    # 2   (remainder)
print(f"Power:     {a ** b}")   # 1419857

# ── Comparison Operators ─────────────────────────────────────────────
# ==, !=, <, >, <=, >=  — return bool
# Chaining: 1 < x < 10  (Pythonic)
print(1 < 5 < 10)    # True
print(1 < 5 > 3)     # True

# ── Logical Operators ────────────────────────────────────────────────
# and, or, not — short-circuit evaluation
x = 10
result = x > 5 and x < 20   # True
result = x < 5 or x > 3     # True
result = not (x > 5)         # False

# Short-circuit: 'or' returns first truthy, 'and' returns first falsy
val = "" or "default"        # "default"
val = "hello" or "default"   # "hello"
val = 0 and "something"      # 0

# ── Bitwise Operators ────────────────────────────────────────────────
# &, |, ^, ~, <<, >>
flags = 0b1010
mask = 0b1100
print(f"AND:  {flags & mask:04b}")   # 1000
print(f"OR:   {flags | mask:04b}")   # 1110
print(f"XOR:  {flags ^ mask:04b}")   # 0110
print(f"NOT:  {~flags}")             # -11
print(f"Left:  {1 << 3}")            # 8
print(f"Right: {16 >> 2}")           # 4

# ── Identity Operators ───────────────────────────────────────────────
# is / is not — compare memory addresses, not values
a = [1, 2, 3]
b = [1, 2, 3]
c = a
print(a is b)       # False — different objects
print(a is c)       # True  — same reference
print(a == b)       # True  — same value

# ── Membership Operators ─────────────────────────────────────────────
# in / not in — check containment
print(3 in [1, 2, 3])           # True
print("hi" in "hello world")    # False
print("key" in {"key": 1})      # True

# ── Assignment Operators ─────────────────────────────────────────────
# =, +=, -=, *=, /=, //=, %=, **=, &=, |=, ^=, <<=, >>=
counter = 0
counter += 5    # counter = 5
counter *= 3    # counter = 15

# ── Operator Precedence (high to low) ───────────────────────────────
# 1. ()                  Grouping
# 2. **                  Exponentiation
# 3. ~, +, -             Unary operators
# 4. *, /, //, %         Multiplicative
# 5. +, -                Additive
# 6. <<, >>              Bitwise shift
# 7. &                   Bitwise AND
# 8. ^                   Bitwise XOR
# 9. |                   Bitwise OR
# 10. ==, !=, <, <=, >, >=  Comparison
# 11. not                 Logical NOT
# 12. and                 Logical AND
# 13. or                  Logical OR
