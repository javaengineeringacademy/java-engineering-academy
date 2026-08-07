# Python Internals

A detailed guide to CPython internals and how Python works under the hood.

## Table of Contents

- [CPython Architecture](#cpython-architecture)
- [Bytecode](#bytecode)
- [Global Interpreter Lock (GIL)](#global-interpreter-lock-gil)
- [Import System](#import-system)
- [Data Model](#data-model)
- [Memory Architecture](#memory-architecture)
- [Object Model](#object-model)

---

## CPython Architecture

### Compilation Pipeline

```python
# Source Code -> Tokens -> AST -> Bytecode -> Execution

# 1. Lexical Analysis (Tokenization)
import tokenize
import io

code = "x = 1 + 2"
tokens = list(tokenize.generate_tokens(io.StringIO(code).readline))
for token in tokens:
    print(token)

# 2. Parsing (AST Generation)
import ast

code = "x = 1 + 2"
tree = ast.parse(code)
print(ast.dump(tree, indent=2))

# 3. Code Object (Bytecode)
code_obj = compile(tree, '<string>', 'exec')
print(code_obj.co_code)  # Raw bytecode
print(code_obj.co_consts)  # Constants
print(code_obj.co_varnames)  # Variable names

# 4. Execution
exec(code_obj)
```

### Python Virtual Machine (PVM)

```python
# The PVM is a stack-based interpreter
# It executes bytecode instructions

# Disassemble bytecode
import dis

def add(a, b):
    return a + b

dis.dis(add)
# Output:
# 2     0 LOAD_FAST      0 (a)
#       2 LOAD_FAST      1 (b)
#       4 BINARY_ADD
#       6 RETURN_VALUE

# Bytecode instructions
# LOAD_FAST: Load local variable
# LOAD_CONST: Load constant value
# BINARY_ADD: Add top two stack items
# RETURN_VALUE: Return top of stack
```

---

## Bytecode

### Code Objects

```python
import dis

def example(x, y):
    z = x + y
    return z * 2

# Code object properties
code = example.__code__
print(f"Code object: {code}")
print(f"Bytecode: {code.co_code}")
print(f"Constants: {code.co_consts}")
print(f"Variable names: {code.co_varnames}")
print(f"Argument count: {code.co_argcount}")
print(f"Stack size: {code.co_stacksize}")
print(f"Flags: {code.co_flags}")

# Disassemble
dis.dis(example)
```

### Bytecode Instructions

```python
import dis

# Common bytecode instructions
def example():
    # LOAD_CONST    0 (1)
    # STORE_FAST    0 (x)
    x = 1

    # LOAD_FAST     0 (x)
    # LOAD_CONST    1 (2)
    # BINARY_ADD
    # STORE_FAST    1 (y)
    y = x + 2

    # LOAD_FAST     1 (y)
    # RETURN_VALUE
    return y

dis.dis(example)

# Instruction sets
# Stack manipulation: POP_TOP, DUP_TOP, ROT_TWO, ROT_THREE
# Variables: LOAD_FAST, STORE_FAST, LOAD_GLOBAL, STORE_GLOBAL
# Constants: LOAD_CONST
# Operations: BINARY_ADD, BINARY_SUBTRACT, BINARY_MULTIPLY
# Control: JUMP_FORWARD, JUMP_IF_TRUE, JUMP_IF_FALSE
# Functions: CALL_FUNCTION, MAKE_FUNCTION, RETURN_VALUE
```

### Bytecode Optimization

```python
# Python performs some bytecode optimizations
# 1. Constant folding
x = 2 + 3  # Compiled as LOAD_CONST 5

# 2. Dead code elimination
def example():
    return 1
    print("This is never executed")  # Removed from bytecode

# 3. String concatenation optimization
s = "hello" + " " + "world"  # May be folded to "hello world"

#查看优化后的字节码
import dis
dis.dis(lambda: 2 + 3)
```

---

## Global Interpreter Lock (GIL)

### How the GIL Works

```python
# The GIL is a mutex in CPython
# Only one thread can execute Python bytecode at a time

# GIL switching interval
import sys
print(sys.getswitchinterval())  # Default: 5ms

# Set switch interval
sys.setswitchinterval(0.001)  # 1ms

# The GIL is released during:
# 1. I/O operations
# 2. C extension calls that release GIL
# 3. time.sleep()

import threading
import time

def cpu_bound():
    total = 0
    for i in range(10**7):
        total += i
    return total

# Threading won't help for CPU-bound tasks
# due to GIL

# For CPU-bound tasks, use multiprocessing:
from multiprocessing import Pool

with Pool(4) as p:
    results = p.map(cpu_bound, range(4))
```

### GIL and C Extensions

```python
# C extensions can release the GIL
# NumPy releases GIL for array operations

import numpy as np
import time
import threading

# This runs in parallel despite GIL
a = np.random.rand(10000, 10000)
b = np.random.rand(10000, 10000)

start = time.time()
c = np.dot(a, b)  # GIL released during computation
print(f"NumPy: {time.time() - start:.2f}s")

# Pure Python (GIL limits parallelism)
def pure_python_sum(n):
    return sum(range(n))

# Use concurrent.futures for simple parallelism
from concurrent.futures import ProcessPoolExecutor

with ProcessPoolExecutor() as executor:
    results = list(executor.map(pure_python_sum, range(4)))
```

---

## Import System

### Import Machinery

```python
# Python's import system
# 1. Finds the module (finders)
# 2. Loads the module (loaders)
# 3. Executes the module code

# 查看import hooks
import sys
print(sys.meta_path)  # List of finders
print(sys.path_hooks)  # List of path hooks

# Custom finder
class MyFinder:
    @classmethod
    def find_module(cls, fullname, path=None):
        print(f"Finding module: {fullname}")
        return None  # Let next finder handle it

sys.meta_path.insert(0, MyFinder())

# Custom loader
class MyLoader:
    @classmethod
    def load_module(cls, fullname):
        print(f"Loading module: {fullname}")
        import types
        module = types.ModuleType(fullname)
        module.__loader__ = cls
        module.__file__ = f"<custom {fullname}>"
        return module

# 查看已导入的模块
print(sys.modules.keys())
```

### Module Cache

```python
import sys

# sys.modules is the module cache
print('os' in sys.modules)  # True (already imported)

# Clear module from cache
del sys.modules['os']
import os  # Re-imports

# Force reimport
import importlib
importlib.reload(os)

# 查看模块信息
import os
print(os.__file__)  # File path
print(os.__spec__)  # Module spec
print(os.__path__)  # Package path
```

### Package Structure

```python
# Package directory structure
# mypackage/
# ├── __init__.py
# ├── module1.py
# ├── module2.py
# └── subpackage/
#     ├── __init__.py
#     └── submodule.py

# __init__.py controls what gets imported
# mypackage/__init__.py
from .module1 import Class1
from .module2 import Class2

__all__ = ['Class1', 'Class2']

# Relative imports
from . import module1
from .module1 import Class1
from .. import other_package
```

---

## Data Model

### Python Data Model

```python
# Everything in Python is an object
# Objects have:
# - Identity (id)
# - Type (type)
# - Value (data)

# 查看对象属性
x = 42
print(id(x))      # Identity (memory address)
print(type(x))    # Type (<class 'int'>)
print(x)          # Value (42)

# Type hierarchy
print(type(int))       # <class 'type'>
print(type(type))      # <class 'type'> (metaclass)
print(type(object))    # <class 'type'>
print(isinstance(int, type))  # True
```

### Attribute Access

```python
class Example:
    class_attr = "class"

    def __init__(self):
        self.instance_attr = "instance"

obj = Example()

# Attribute lookup order:
# 1. Data descriptors (from class and its bases)
# 2. Instance __dict__
# 3. Non-data descriptors (from class and its bases)

# 查看__dict__
print(obj.__dict__)  # {'instance_attr': 'instance'}
print(Example.__dict__.keys())  # Class attributes

# Descriptor protocol
class Descriptor:
    def __get__(self, obj, objtype=None):
        print("Getting attribute")
        return 42

    def __set__(self, obj, value):
        print("Setting attribute")

class WithDescriptor:
    attr = Descriptor()

obj = WithDescriptor()
print(obj.attr)  # Getting attribute, 42
obj.attr = 10    # Setting attribute
```

### Special Methods

```python
class MyClass:
    def __new__(cls, *args, **kwargs):
        """Called before __init__"""
        print("__new__ called")
        return super().__new__(cls)

    def __init__(self, value):
        """Initialize instance"""
        print("__init__ called")
        self.value = value

    def __del__(self):
        """Called when garbage collected"""
        print("__del__ called")

    def __repr__(self):
        """Unambiguous representation"""
        return f"MyClass({self.value!r})"

    def __str__(self):
        """Readable representation"""
        return f"MyClass: {self.value}"

    def __format__(self, format_spec):
        """Format string"""
        return f"{self.value:{format_spec}}"

    def __bytes__(self):
        """Bytes representation"""
        return bytes(str(self.value), 'utf-8')

    def __bool__(self):
        """Boolean truth value"""
        return bool(self.value)

    def __hash__(self):
        """Hash value"""
        return hash(self.value)

    def __eq__(self, other):
        """Equality comparison"""
        return self.value == other.value

# Usage
obj = MyClass(42)
print(repr(obj))   # MyClass(42)
print(str(obj))    # MyClass: 42
print(f"{obj:.2f}")  # 42.00
print(bytes(obj))  # b'42'
print(bool(obj))   # True
```

---

## Memory Architecture

### Object Memory Layout

```python
import sys

# Every Python object has:
# 1. Reference count (Py_ssize_t)
# 2. Pointer to type object
# 3. Object-specific data

# 查看对象大小
print(sys.getsizeof(0))      # 24 bytes (int)
print(sys.getsizeof(""))     # 49 bytes (str)
print(sys.getsizeof([]))     # 56 bytes (list)
print(sys.getsizeof({}))     # 64 bytes (dict)

# For custom objects
class Empty:
    pass

class WithSlots:
    __slots__ = ('x', 'y')

print(sys.getsizeof(Empty()))     # 48 bytes
print(sys.getsizeof(WithSlots()))  # 40 bytes
```

### Memory Allocation

```python
# Small objects (<= 512 bytes) use memory pools
# Organized by size class (8 bytes to 512 bytes)

# 查看内存分配
import tracemalloc

tracemalloc.start()

# Your code
data = [i for i in range(1000)]

current, peak = tracemalloc.get_traced_memory()
print(f"Current: {current / 1024:.2f} KB")
print(f"Peak: {peak / 1024:.2f} KB")

# Integer caching (-5 to 256)
a = 256
b = 256
print(a is b)  # True (cached)

a = 257
b = 257
print(a is b)  # False (not cached)
```

---

## Object Model

### Type System

```python
# Everything is an object
# Types are objects too

print(type(42))        # <class 'int'>
print(type(int))       # <class 'type'>
print(type(type))      # <class 'type'>

# Type hierarchy
print(int.__bases__)   # (<class 'object'>,)
print(object.__bases__)  # ()

# 创建类型
MyType = type('MyType', (object,), {'attr': 42})
obj = MyType()
print(obj.attr)  # 42
print(type(obj))  # <class '__main__.MyType'>
```

### Metaclasses

```python
class Meta(type):
    def __new__(cls, name, bases, dict):
        print(f"Creating class: {name}")
        return super().__new__(cls, name, bases, dict)

class MyClass(metaclass=Meta):
    pass

# Output: Creating class: MyClass
```

### Descriptor Protocol

```python
class Property:
    def __init__(self, fget):
        self.fget = fget

    def __get__(self, obj, objtype=None):
        return self.fget(obj)

class MyClass:
    @Property
    def value(self):
        return 42

obj = MyClass()
print(obj.value)  # 42
```

---

## Summary

Python internals:

- **CPython** is the reference implementation
- **Bytecode** is compiled from source code
- **GIL** limits true parallelism for CPU-bound tasks
- **Import system** is extensible via finders and loaders
- **Data model** defines how objects behave
- **Memory architecture** uses pools and generations
- Understanding internals helps write better Python code

## Production Checklist

- [ ] Use `dis.dis()` to inspect bytecode for performance-critical functions
- [ ] Profile with `cProfile` before claiming Python is too slow
- [ ] Understand GIL release during C extensions (NumPy, I/O operations)
- [ ] Use `sys.getswitchinterval()` to tune GIL switching for latency-sensitive apps
- [ ] Clear `sys.modules` cache when hot-reloading modules in development
- [ ] Use `__slots__` to reduce memory footprint for high-instance-count classes
- [ ] Monitor integer caching behavior (`is` vs `==` for small ints)
- [ ] Avoid `__del__` in production; rely on context managers and explicit cleanup
- [ ] Use `tracemalloc` to debug memory leaks in long-running processes
- [ ] Understand attribute lookup order (data descriptors → instance dict → non-data descriptors)

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Understands Python compiles to bytecode; knows GIL limits CPU-bound threading |
| **Intermediate** | Can read `dis.dis()` output; uses `sys.getsizeof()` and `sys.getrefcount()` |
| **Advanced** | Writes C extensions; custom import hooks; tunes GIL switch interval |
| **Expert** | Hacks CPython internals; implements custom bytecode optimizations; contributes to CPython |

## Common Myths

1. **"Python is interpreted"** — CPython compiles to bytecode first; the PVM executes bytecode
2. **"GIL prevents all parallelism"** — C extensions release GIL; multiprocessing bypasses it entirely
3. **"Import is always fast"** — First import executes module code; use lazy imports for startup time
4. **"All objects are on the heap"** — Small ints and strings are interned; tuples may be optimized
5. **"CPython is Python"** — CPython is one implementation; Jython, PyPy, GraalPy exist
6. **"Bytecode is optimized"** — Python does constant folding and dead code elimination, but no JIT

## One-Minute Revision

- **Compilation pipeline**: Source → Tokens → AST → Bytecode → PVM execution
- **Bytecode**: Stack-based instructions; `dis.dis()` for disassembly; cached per module
- **GIL**: Mutex; one thread executes bytecode at a time; released during I/O and C extensions
- **Import system**: Finders locate modules; loaders execute code; `sys.modules` caches results
- **Data model**: Dunder methods define object behavior; attribute lookup follows descriptor protocol
- **Memory**: Reference counting (primary) + generational GC (cyclic); memory pools for small objects
- **Object model**: Everything is an object; types are objects too; metaclasses control type creation
- **Integer caching**: -5 to 256 are cached; `is` works for cached values; use `==` for comparison
- **String interning**: Short strings interned; use `sys.intern()` for repeated lookups
- **C extensions**: Can release GIL; NumPy uses them for vectorized parallel operations
