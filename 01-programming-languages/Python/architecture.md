# Python Architecture

## CPython Interpreter

The default Python implementation. Compiles source code to bytecode, then executes it via the CPython virtual machine.

### Bytecode Compilation

1. Source code (`.py`) parsed into Abstract Syntax Tree (AST)
2. AST compiled to bytecode (`.pyc` files in `__pycache__/`)
3. Python Virtual Machine (PVM) executes bytecode instructions

Key bytecode files:
- `__pycache__/*.pyc` - Compiled bytecode
- `importlib` - Module loading system

### Global Interpreter Lock (GIL)

The GIL is a mutex protecting access to Python objects, preventing multiple threads from executing Python bytecode simultaneously.

**Implications:**
- CPU-bound threads cannot run in parallel
- I/O-bound operations release the GIL during wait times
- `multiprocessing` module bypasses GIL via separate processes
- `asyncio` provides concurrency for I/O-bound tasks

**Bypassing GIL:**
- Use `multiprocessing` for CPU-bound work
- Use `asyncio` for I/O-bound work
- Use C extensions (NumPy, Cython)
- Consider `subprocess` for external commands

### Memory Management

Python uses a private heap containing all objects and data structures.

**Reference Counting:**
- Each object maintains a reference count
- When count drops to zero, memory is deallocated
- `sys.getrefcount()` returns current count
- Circular references handled by garbage collector

**Garbage Collector:**
- Detects and collects cyclic references
- Generational collector (0, 1, 2)
- `gc` module for manual control
- `gc.collect()` forces collection

### Object Model

Everything in Python is an object:
- Functions are first-class objects
- Classes are objects
- Modules are objects
- Even `None` is an object

**Object Structure:**
```python
import sys

class Example:
    pass

obj = Example()
print(sys.getsizeof(obj))  # Memory size of object
print(sys.getrefcount(obj))  # Reference count
```

### Memory Optimization

**Interning:**
- Small integers (-5 to 256) cached
- String interning for identifiers
- `sys.intern()` for manual interning

**Small Object Allocator:**
- Pymalloc for objects < 512 bytes
- Memory pools organized by size
- Reduces system call overhead

### Key Internals

**Import System:**
- `importlib` handles module loading
- Module search path: `sys.path`
- `__init__.py` marks packages

**Startup Sequence:**
1. Python executable starts
2. `site.py` executed (user site-packages)
3. `sys.path` populated
4. Interactive mode or script execution

### Performance Considerations

**CPython Limitations:**
- Interpreted language overhead
- Dynamic typing adds runtime cost
- GIL limits multi-threading

**Optimization Strategies:**
- Use built-in data structures
- Leverage C extensions
- Profile before optimizing
- Consider PyPy for long-running scripts

### Reference Materials

- CPython source: `Lib/` and `Python/` directories
- `dis` module for bytecode disassembly
- `inspect` module for runtime introspection
