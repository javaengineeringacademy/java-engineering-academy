# Python Context Managers Reference

## What are Python Context Managers?

Context managers are objects that define methods to be executed when entering and exiting a context. They are used with the `with` statement to ensure proper resource management, like file handling, database connections, and locks.

## Why does Python Context Managers matter?

Understanding context managers helps you:
- Ensure resources are properly released
- Write cleaner and more readable code
- Handle exceptions safely
- Manage complex resource lifecycles

---

## 1. Basic Context Manager

```python
# Using with statement
with open('file.txt', 'w') as f:
    f.write('Hello, World!')
# File is automatically closed

# Equivalent without with
f = open('file.txt', 'w')
try:
    f.write('Hello, World!')
finally:
    f.close()
```

---

## 2. Creating Context Managers

### Class-based

```python
class FileManager:
    def __init__(self, filename, mode):
        self.filename = filename
        self.mode = mode
        self.file = None
    
    def __enter__(self):
        self.file = open(self.filename, self.mode)
        return self.file
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.file:
            self.file.close()
        return False  # Don't suppress exceptions

with FileManager('file.txt', 'w') as f:
    f.write('Hello, World!')
```

### Generator-based

```python
from contextlib import contextmanager

@contextmanager
def managed_resource():
    resource = acquire_resource()
    try:
        yield resource
    finally:
        release_resource(resource)

with managed_resource() as resource:
    use_resource(resource)
```

---

## 3. contextlib Module

### contextmanager

```python
from contextlib import contextmanager

@contextmanager
def timer():
    import time
    start = time.time()
    yield
    end = time.time()
    print(f"Elapsed: {end - start:.4f} seconds")

with timer():
    sum(range(1000000))
```

### closing

```python
from contextlib import closing

class MyResource:
    def open(self):
        print("Resource opened")
    
    def close(self):
        print("Resource closed")

with closing(MyResource()) as resource:
    resource.open()
# Resource closed automatically
```

### suppress

```python
from contextlib import suppress

with suppress(FileNotFoundError):
    os.remove('file.txt')

# Equivalent to
try:
    os.remove('file.txt')
except FileNotFoundError:
    pass
```

### redirect_stdout

```python
from contextlib import redirect_stdout
import io

f = io.StringIO()
with redirect_stdout(f):
    print("Hello, World!")

output = f.getvalue()
print(output)  # Hello, World!
```

### ExitStack

```python
from contextlib import ExitStack

with ExitStack() as stack:
    files = [stack.enter_context(open(f, 'r')) for f in filenames]
    # All files are closed when exiting
```

---

## 4. Async Context Managers

```python
import asyncio
from contextlib import asynccontextmanager

@asynccontextmanager
async def async_resource():
    print("Acquiring resource")
    try:
        yield resource
    finally:
        print("Releasing resource")

async def main():
    async with async_resource() as resource:
        use_resource(resource)

asyncio.run(main())
```

---

## 5. Common Patterns

### Database Connection

```python
@contextmanager
def database_connection(connection_string):
    conn = create_connection(connection_string)
    try:
        yield conn
    finally:
        conn.close()

with database_connection('sqlite:///db.sqlite') as conn:
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM users')
```

### Temporary Directory

```python
import tempfile
import shutil

with tempfile.TemporaryDirectory() as tmpdir:
    # Use tmpdir
    pass
# Directory is automatically removed
```

### Lock

```python
import threading

lock = threading.Lock()

with lock:
    # Thread-safe code
    pass
```

---

## One-Minute Revision Table

| Context Manager | Description | Example |
|-----------------|-------------|---------|
| **with** | Execute code in context | `with open('f') as f:` |
| **__enter__** | Enter context | `def __enter__(self):` |
| **__exit__** | Exit context | `def __exit__(self, ...):` |
| **@contextmanager** | Generator-based | `@contextmanager` decorator |
| **closing** | Close resource | `closing(resource)` |
| **suppress** | Suppress exceptions | `suppress(Exception)` |
| **ExitStack** | Manage multiple contexts | `ExitStack()` |
| **asynccontextmanager** | Async context | `@asynccontextmanager` |

---

## Common Mistakes

### 1. Forgetting to Return

```python
# WRONG
@contextmanager
def managed_resource():
    resource = acquire_resource()
    yield resource
    release_resource(resource)

# RIGHT
@contextmanager
def managed_resource():
    resource = acquire_resource()
    try:
        yield resource
    finally:
        release_resource(resource)
```

### 2. Suppressing Exceptions

```python
# WRONG
def __exit__(self, exc_type, exc_val, exc_tb):
    return True  # Suppresses all exceptions

# RIGHT
def __exit__(self, exc_type, exc_val, exc_tb):
    return False  # Don't suppress exceptions
```

### 3. Not Using with Statement

```python
# WRONG
f = open('file.txt', 'w')
f.write('Hello')
f.close()

# RIGHT
with open('file.txt', 'w') as f:
    f.write('Hello')
```

---

## Production Notes

1. **Always use `with` statement** - Ensures resources are released
2. **Use `try/finally` in context managers** - Handle exceptions properly
3. **Use `@contextmanager` for simple cases** - More readable
4. **Use class-based for complex cases** - More control
5. **Use `ExitStack` for dynamic contexts** - Manage multiple resources
6. **Use `suppress` for expected exceptions** - Cleaner code
7. **Use `closing` for objects with `close()` method** - More explicit
8. **Document context manager behavior** - Especially side effects
9. **Test context managers thoroughly** - Include exception cases
10. **Use async context managers for async code** - Proper async resource management

---

## Further Reading

- Python documentation on contextlib
- PEP 343 - The 'with' statement
- Fluent Python by Luciano Ramalho
