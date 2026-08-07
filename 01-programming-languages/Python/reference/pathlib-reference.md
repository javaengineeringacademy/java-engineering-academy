# Python pathlib Reference

## What is pathlib?

pathlib is a module that provides an object-oriented interface to the filesystem. It represents paths as objects rather than strings, making path manipulation more intuitive and cross-platform.

## Why does pathlib matter?

Understanding pathlib helps you:
- Work with paths in a more Pythonic way
- Write cross-platform code
- Avoid string manipulation errors
- Use modern path operations

---

## 1. Creating Path Objects

```python
from pathlib import Path

# Current directory
p = Path('.')

# Absolute path
p = Path('/path/to/file')

# Using /
p = Path('path') / 'to' / 'file.txt'

# Home directory
p = Path.home()

# Current working directory
p = Path.cwd()
```

---

## 2. Path Properties

```python
from pathlib import Path

p = Path('/path/to/file.txt')

# Name (filename with extension)
print(p.name)      # file.txt

# Stem (filename without extension)
print(p.stem)      # file

# Suffix
print(p.suffix)    # .txt

# Parent directory
print(p.parent)    # /path/to

# Parts
print(p.parts)     # ('/', 'path', 'to', 'file.txt')

# Anchor
print(p.anchor)    # /
```

---

## 3. Path Operations

```python
from pathlib import Path

p = Path('/path/to/file.txt')

# Join paths
new_p = p.parent / 'new_file.txt'
print(new_p)  # /path/to/new_file.txt

# With name
new_p = p.with_name('new_file.txt')
print(new_p)  # /path/to/new_file.txt

# With suffix
new_p = p.with_suffix('.csv')
print(new_p)  # /path/to/file.csv

# Absolute path
abs_p = p.absolute()
print(abs_p)
```

---

## 4. File and Directory Operations

```python
from pathlib import Path

p = Path('file.txt')

# Check existence
print(p.exists())    # True/False

# Check type
print(p.is_file())   # True/False
print(p.is_dir())    # True/False
print(p.is_symlink()) # True/False

# Create
p.touch()            # Create file
p.mkdir(parents=True, exist_ok=True)  # Create directory

# Remove
p.unlink()           # Remove file
p.rmdir()            # Remove empty directory

# Rename
p.rename('new_name.txt')

# Stat
stat = p.stat()
print(stat.st_size)  # File size
print(stat.st_mtime) # Modification time
```

---

## 5. Reading and Writing

```python
from pathlib import Path

p = Path('file.txt')

# Read
content = p.read_text()
binary_content = p.read_bytes()

# Write
p.write_text('Hello, World!')
p.write_bytes(b'Hello, World!')

# Read with encoding
content = p.read_text(encoding='utf-8')

# Write with encoding
p.write_text('Hello, World!', encoding='utf-8')
```

---

## 6. Globbing and Matching

```python
from pathlib import Path

p = Path('.')

# Glob
for file in p.glob('*.txt'):
    print(file)

# Rglob (recursive)
for file in p.rglob('*.py'):
    print(file)

# Match
p = Path('file.txt')
print(p.match('*.txt'))  # True
```

---

## 7. Iterating Directory

```python
from pathlib import Path

p = Path('.')

# List files
for item in p.iterdir():
    print(item)

# Walk
for root, dirs, files in p.walk():
    for file in files:
        print(root / file)
```

---

## One-Minute Revision Table

| Method | Description | Example |
|--------|-------------|---------|
| **Path()** | Create path object | `Path('file.txt')` |
| **name** | Filename with extension | `p.name` |
| **stem** | Filename without extension | `p.stem` |
| **suffix** | File extension | `p.suffix` |
| **parent** | Parent directory | `p.parent` |
| **exists()** | Check if exists | `p.exists()` |
| **is_file()** | Check if file | `p.is_file()` |
| **is_dir()** | Check if directory | `p.is_dir()` |
| **touch()** | Create file | `p.touch()` |
| **mkdir()** | Create directory | `p.mkdir()` |
| **unlink()** | Remove file | `p.unlink()` |
| **rmdir()** | Remove directory | `p.rmdir()` |
| **read_text()** | Read text file | `p.read_text()` |
| **write_text()** | Write text file | `p.write_text('text')` |
| **glob()** | Find files | `p.glob('*.txt')` |
| **rglob()** | Find files recursively | `p.rglob('*.py')` |

---

## Common Mistakes

### 1. Not Using Path Objects

```python
# WRONG
import os
path = os.path.join('path', 'to', 'file.txt')

# RIGHT
from pathlib import Path
path = Path('path') / 'to' / 'file.txt'
```

### 2. Forgetting to Convert to String

```python
# WRONG
p = Path('file.txt')
open(p)  # Works in Python 3.6+

# RIGHT
p = Path('file.txt')
open(str(p))  # Explicit conversion
```

### 3. Not Using `exist_ok`

```python
# WRONG
p.mkdir()  # FileExistsError if exists

# RIGHT
p.mkdir(exist_ok=True)
```

---

## Production Notes

1. **Use `pathlib` instead of `os.path`** - More modern and readable
2. **Use `/` operator for path joining** - More intuitive
3. **Use `with_name` and `with_suffix`** - For path manipulation
4. **Use `glob` and `rglob`** - For finding files
5. **Use `read_text` and `write_text`** - For text files
6. **Use `read_bytes` and `write_bytes`** - For binary files
7. **Use `touch()` to create files** - More convenient
8. **Use `mkdir(parents=True, exist_ok=True)`** - Create nested directories
9. **Use `stat()` for file information** - Get file size, modification time
10. **Use `iterdir()` for listing directories** - More memory efficient

---

## Further Reading

- Python documentation on pathlib module
- PEP 343 - pathlib module
- Fluent Python by Luciano Ramalho
