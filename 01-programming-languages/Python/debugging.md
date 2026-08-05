# Python Debugging

## pdb

Python Debugger.

### Basic Usage
```python
import pdb

def complex_function():
    result = 0
    for i in range(10):
        pdb.set_trace()  # Breakpoint here
        result += i
    return result
```

### Commands
```
n (next)        - Execute next line
s (step)        - Step into function
c (continue)    - Continue execution
p expr          - Print expression
l (list)        - Show current code
w (where)       - Show stack trace
d (down)        - Move down stack
u (up)          - Move up stack
q (quit)        - Exit debugger
h (help)        - Show help
```

### Remote Debugging
```python
import pdb; pdb.set_trace()

# Or with breakpoint() (Python 3.7+)
breakpoint()
```

## breakpoint() (Python 3.7+)

```python
def process_data(data):
    breakpoint()  # Cleaner than pdb.set_trace()
    return transform(data)
```

### Configure Breakpoint Behavior
```bash
# Disable all breakpoints
PYTHONBREAKPOINT=0 python script.py

# Use remote debugger
PYTHONBREAKPOINT=remote_pdb.set_trace python script.py
```

## IDE Debugging

### VS Code
```json
// .vscode/launch.json
{
    "version": "0.2.0",
    "configurations": [
        {
            "name": "Python: Current File",
            "type": "python",
            "request": "launch",
            "program": "${file}",
            "console": "integratedTerminal",
            "justMyCode": false
        }
    ]
}
```

### PyCharm
1. Click in gutter to set breakpoint
2. Right-click > Debug
3. Use Debug tool window for controls

## Logging for Debugging

```python
import logging

logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

def complex_function():
    logger.debug("Starting function")
    for i in range(10):
        logger.debug(f"Processing item {i}")
        result = process(i)
        logger.debug(f"Result: {result}")
    return result
```

## Assertions

```python
def calculate_average(numbers):
    assert len(numbers) > 0, "Cannot calculate average of empty list"
    assert all(isinstance(n, (int, float)) for n in numbers), "All items must be numbers"
    return sum(numbers) / len(numbers)
```

## Debugging with print

```python
def process(data):
    print(f"DEBUG: data = {data}")  # Quick debug
    result = transform(data)
    print(f"DEBUG: result = {result}")
    return result
```

### Using repr
```python
print(f"DEBUG: {repr(data)}")  # Shows quotes for strings
```

## Memory Debugging

### memory_profiler
```bash
pip install memory_profiler
```

```python
from memory_profiler import profile

@profile
def memory_heavy():
    data = [i for i in range(1000000)]
    return sum(data)
```

```bash
python -m memory_profiler script.py
```

### tracemalloc
```python
import tracemalloc

tracemalloc.start()

# Code to profile
data = [i for i in range(1000000)]

snapshot = tracemalloc.take_snapshot()
top_stats = snapshot.statistics('lineno')

for stat in top_stats[:10]:
    print(stat)
```

## Profiling

```python
import cProfile

def my_function():
    total = 0
    for i in range(1000000):
        total += i
    return total

cProfile.run('my_function()')
```

## Common Debug Scenarios

### Import Errors
```python
import sys
print(sys.path)  # Check Python path

# Debug import issues
try:
    import mymodule
except ImportError as e:
    print(f"Import error: {e}")
    print(f"Module search path: {sys.path}")
```

### Type Errors
```python
def process(value):
    print(f"Type: {type(value)}")  # Debug type
    return value * 2
```

### Exception Traceback
```python
import traceback

try:
    risky_operation()
except Exception as e:
    traceback.print_exc()  # Print full traceback
```

## Debug Tools

### icecream
```bash
pip install icecream
```

```python
from icecream import ic

def process(data):
    ic(data)  # Prints variable name and value
    result = transform(data)
    ic(result)
    return result
```

### py-spy
```bash
pip install py-spy

# Profile running process
py-spy top --pid 12345

# Record to file
py-spy record -o profile.svg --pid 12345
```

## Best Practices

1. Use `breakpoint()` over `pdb.set_trace()`
2. Configure IDE debugger for better experience
3. Use logging instead of print for production
4. Add assertions for preconditions
5. Profile before optimizing
6. Use `repr()` for debugging strings
7. Check `sys.path` for import issues
8. Use `traceback` for exception debugging
