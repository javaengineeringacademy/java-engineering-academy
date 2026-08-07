# Python Strings Reference

## What are Python Strings?

Strings are immutable sequences of Unicode characters. They are one of the most commonly used data types in Python, used for text processing, data manipulation, and more.

## Why does Python Strings matter?

Understanding strings helps you:
- Process and manipulate text data effectively
- Handle encoding and decoding properly
- Use regular expressions for pattern matching
- Avoid common string-related bugs

---

## 1. String Creation

```python
# Single quotes
s1 = 'hello'

# Double quotes
s2 = "hello"

# Triple quotes (multi-line)
s3 = '''Multi-line
string'''
s4 = """Another
multi-line
string"""

# Escape characters
s5 = 'hello\nworld'  # Newline
s6 = 'hello\tworld'  # Tab
s7 = 'hello\\world'  # Backslash
s8 = 'it\'s'         # Single quote

# Raw strings
s9 = r'hello\nworld'  # Literal backslash-n
s10 = r'C:\new\folder'

# String concatenation
s11 = 'hello' + ' ' + 'world'  # 'hello world'
s12 = 'hello' * 3  # 'hellohellohello'
```

---

## 2. String Methods

### Case Methods

```python
s = "Hello, World!"

print(s.lower())      # hello, world!
print(s.upper())      # HELLO, WORLD!
print(s.title())      # Hello, World!
print(s.capitalize()) # Hello, world!
print(s.swapcase())   # hELLO, wORLD!
```

### Search Methods

```python
s = "Hello, World!"

print(s.find("World"))      # 7
print(s.find("xyz"))        # -1
print(s.rfind("l"))         # 10
print(s.index("World"))     # 7
# s.index("xyz")            # ValueError
print(s.count("l"))         # 3
print(s.startswith("Hello")) # True
print(s.endswith("!"))      # True
```

### Split and Join

```python
s = "Hello, World!"

# Split
print(s.split(", "))       # ['Hello', 'World!']
print(s.split("l"))        # ['He', '', 'o, Wor', 'd!']
print(s.rsplit("l", 1))    # ['Hello, Wor', 'd!']

# Splitlines
s2 = "Line 1\nLine 2\nLine 3"
print(s2.splitlines())     # ['Line 1', 'Line 2', 'Line 3']

# Join
words = ['Hello', 'World']
print(' '.join(words))     # 'Hello World'
print(', '.join(words))    # 'Hello, World'
print('\n'.join(words))    # 'Hello\nWorld'
```

### Strip Methods

```python
s = "  Hello, World!  "

print(s.strip())       # 'Hello, World!'
print(s.lstrip())      # 'Hello, World!  '
print(s.rstrip())      # '  Hello, World!'
print(s.strip(" !"))   # 'Hello, World'

# Strip specific characters
s2 = "###Hello###"
print(s2.strip("#"))   # 'Hello'
```

### Replace and Translate

```python
s = "Hello, World!"

# Replace
print(s.replace("World", "Python"))  # 'Hello, Python!'
print(s.replace("l", "L", 2))        # 'HeLLo, World!'

# Translate
table = str.maketrans("aeiou", "12345")
print("hello".translate(table))  # 'h2ll4'
```

### Padding and Alignment

```python
s = "Hello"

print(s.center(20))      # '       Hello        '
print(s.ljust(20))       # 'Hello               '
print(s.rjust(20))       # '               Hello'
print(s.zfill(20))       # '000000000000000Hello'

# Format alignment
print(f"{'Hello':^20}")  # '       Hello        '
print(f"{'Hello':<20}")  # 'Hello               '
print(f"{'Hello':>20}")  # '               Hello'
```

### Checking Methods

```python
s = "Hello, World!"

print(s.isalnum())     # False (has punctuation)
print(s.isalpha())     # False (has punctuation)
print(s.isdigit())     # False
print(s.isnumeric())   # False
print(s.isdecimal())   # False
print(s.isspace())     # False
print(s.islower())     # False
print(s.isupper())     # False
print(s.istitle())     # True

# Empty string
print("".isalnum())    # False
print("".isalpha())    # False
```

---

## 3. String Formatting

### f-strings (Python 3.6+)

```python
name = "Alice"
age = 30

# Basic formatting
print(f"Hello, {name}!")  # Hello, Alice!

# Expressions
print(f"{name.lower()}")  # alice
print(f"{2 + 2}")  # 4

# Format specifiers
print(f"{age:.2f}")  # 30.00
print(f"{1000000:,}")  # 1,000,000
print(f"{0.25:.1%}")  # 25.0%
print(f"{42:05d}")  # 00042

# Debugging (Python 3.8+)
x = 42
print(f"{x=}")  # x=42

# Alignment
print(f"{'Hello':>20}")  # '               Hello'
print(f"{'Hello':<20}")  # 'Hello               '
print(f"{'Hello':^20}")  # '       Hello        '
```

### str.format()

```python
# Positional arguments
print("{} is {} years old".format("Alice", 30))

# Indexed arguments
print("{0} is {1} years old".format("Alice", 30))

# Named arguments
print("{name} is {age} years old".format(name="Alice", age=30))

# Format specifiers
print("{:.2f}".format(3.14159))  # 3.14
print("{:,}".format(1000000))    # 1,000,000
print("{:>10}".format("Hello"))  # '     Hello'
```

### % Formatting

```python
# Basic formatting
print("Hello, %s!" % "World")
print("%s is %d years old" % ("Alice", 30))

# Format specifiers
print("%.2f" % 3.14159)  # 3.14
print("%10s" % "Hello")   # '     Hello'
print("%-10s" % "Hello")  # 'Hello     '
```

---

## 4. String Encoding

```python
# Encode to bytes
s = "Hello, World!"
b = s.encode('utf-8')
print(b)  # b'Hello, World!'

# Different encodings
b_ascii = s.encode('ascii')
b_latin1 = s.encode('latin-1')
b_utf16 = s.encode('utf-16')

# Decode to string
s2 = b.decode('utf-8')
print(s2)  # Hello, World!

# Handle encoding errors
s = "Hello, 世界"
try:
    s.encode('ascii')
except UnicodeEncodeError as e:
    print(f"Error: {e}")

# Use errors parameter
s.encode('ascii', errors='ignore')   # b'Hello, '
s.encode('ascii', errors='replace')  # b'Hello, ??'
s.encode('ascii', errors='xmlcharrefreplace')  # b'Hello, &#30012;&#30022;'
```

---

## 5. Regular Expressions

```python
import re

# Basic patterns
text = "Hello, World! 123"

# Find all matches
print(re.findall(r'\d+', text))  # ['123']

# Search
match = re.search(r'World', text)
if match:
    print(match.group())  # World

# Match (beginning of string)
match = re.match(r'Hello', text)
if match:
    print(match.group())  # Hello

# Substitution
print(re.sub(r'\d+', 'NUM', text))  # Hello, World! NUM

# Compile pattern
pattern = re.compile(r'\w+')
print(pattern.findall(text))  # ['Hello', 'World', '123']
```

### Pattern Syntax

```python
import re

# Character classes
print(re.findall(r'[aeiou]', 'hello'))  # ['e', 'o']

# Quantifiers
print(re.findall(r'go*d', 'gd god good'))  # ['gd', 'god', 'good']

# Anchors
print(re.findall(r'^Hello', 'Hello World'))  # ['Hello']
print(re.findall(r'World$', 'Hello World'))  # ['World']

# Groups
match = re.search(r'(\w+) (\w+)', 'Hello World')
if match:
    print(match.group(1))  # Hello
    print(match.group(2))  # World

# Named groups
match = re.search(r'(?P<first>\w+) (?P<second>\w+)', 'Hello World')
if match:
    print(match.group('first'))  # Hello
```

---

## 6. String Constants

```python
import string

# ASCII letters and digits
print(string.ascii_letters)   # abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
print(string.ascii_lowercase)  # abcdefghijklmnopqrstuvwxyz
print(string.ascii_uppercase)  # ABCDEFGHIJKLMNOPQRSTUVWXYZ
print(string.digits)          # 0123456789

# Punctuation
print(string.punctuation)     # !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~

# Whitespace
print(string.whitespace)      # ' \t\n\r\x0b\x0c'

# Template
t = string.Template('Hello, $name!')
print(t.substitute(name='World'))  # Hello, World!
```

---

## One-Minute Revision Table

| Method | Description | Example |
|--------|-------------|---------|
| `lower()` | Convert to lowercase | `"Hello".lower()` → `"hello"` |
| `upper()` | Convert to uppercase | `"Hello".upper()` → `"HELLO"` |
| `strip()` | Remove whitespace | `"  hi  ".strip()` → `"hi"` |
| `split()` | Split into list | `"a,b".split(",")` → `["a", "b"]` |
| `join()` | Join list to string | `",".join(["a","b"])` → `"a,b"` |
| `replace()` | Replace substring | `"hi".replace("i", "o")` → `"ho"` |
| `find()` | Find substring index | `"hello".find("l")` → `2` |
| `count()` | Count occurrences | `"hello".count("l")` → `2` |
| `startswith()` | Check prefix | `"hello".startswith("he")` → `True` |
| `endswith()` | Check suffix | `"hello".endswith("lo")` → `True` |

---

## Common Mistakes

### 1. String Immutability

```python
# WRONG
s = "hello"
s[0] = 'H'  # TypeError

# RIGHT
s = "hello"
s = 'H' + s[1:]
```

### 2. Using == for Identity

```python
# WRONG
if s is "hello":  # Warning: literal comparison
    pass

# RIGHT
if s == "hello":
    pass
```

### 3. Forgetting to Join

```python
# WRONG
s = "hello" + " " + "world"

# RIGHT (for multiple strings)
s = " ".join(["hello", "world"])
```

### 4. Encoding Issues

```python
# WRONG
s = "Hello, 世界"
b = s.encode('ascii')  # UnicodeEncodeError

# RIGHT
b = s.encode('utf-8')
```

---

## Production Notes

1. **Use f-strings for formatting** - More readable and faster
2. **Use `join()` for concatenation** - More efficient than `+`
3. **Use `raw strings` for regex** - Avoids escape character issues
4. **Handle encoding properly** - Use UTF-8 by default
5. **Use `re.compile()` for repeated patterns** - More efficient
6. **Be careful with string slicing** - Index out of range errors
7. **Use `string.Template` for user-provided templates** - Safer than f-strings
8. **Use `str.translate()` for bulk replacements** - More efficient than multiple `replace()`
9. **Use `textwrap.dedent()` for multi-line strings** - Removes common whitespace
10. **Use `functools.lru_cache` for expensive string operations** - Caches results

---

## Further Reading

- Python documentation on strings
- Python documentation on re module
- Fluent Python by Luciano Ramalho
- Python Cookbook by David Beazley
