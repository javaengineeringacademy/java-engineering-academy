# Python Troubleshooting

## Import Errors

### ModuleNotFoundError
```bash
ModuleNotFoundError: No module named 'requests'
```

**Solutions:**
```bash
# Install the module
pip install requests

# Check if installed
pip list | grep requests

# Check Python path
python -c "import sys; print(sys.path)"

# Use correct Python version
pip3 install requests  # For Python 3
```

### ImportError
```python
from mymodule import function  # ImportError
```

**Solutions:**
```python
# Check module location
import mymodule
print(mymodule.__file__)

# Add to path
import sys
sys.path.insert(0, '/path/to/module')
```

## Dependency Conflicts

### Version Conflicts
```bash
ERROR: pip's dependency resolver does not currently take into account all the packages that are installed.
```

**Solutions:**
```bash
# Create fresh virtual environment
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Check installed versions
pip list

# Install specific version
pip install requests==2.28.0

# Use pip-tools
pip install pip-tools
pip-compile requirements.in
```

### Circular Dependencies
```python
# a.py
from b import B

# b.py
from a import A  # Circular!
```

**Solution:**
```python
# b.py
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from a import A
```

## Encoding Issues

### UnicodeDecodeError
```python
# Bad
with open('file.txt') as f:
    data = f.read()  # May fail

# Good
with open('file.txt', encoding='utf-8') as f:
    data = f.read()
```

### Encoding Problems
```python
# Detect encoding
import chardet

with open('file.txt', 'rb') as f:
    raw = f.read()
    result = chardet.detect(raw)
    print(result['encoding'])

# Read with detected encoding
with open('file.txt', encoding=result['encoding']) as f:
    data = f.read()
```

## Virtual Environment Issues

### Activation Problems
```bash
# macOS/Linux
source venv/bin/activate

# Windows
venv\Scripts\activate

# If activation fails
python -m venv venv --clear
```

### Wrong Python Version
```bash
# Check which Python
which python
which python3

# Use specific version
python3.11 -m venv venv
```

## Path Issues

### sys.path Problems
```python
import sys

# Add custom path
sys.path.append('/path/to/your/modules')

# Or set PYTHONPATH
# export PYTHONPATH="/path/to/your/modules:$PYTHONPATH"
```

### Package Not Found
```python
# Check package installation
import pkg_resources
print(pkg_resources.get_distribution('requests').version)

# Reinstall package
pip install --force-reinstall requests
```

## Syntax Errors

### IndentationError
```python
# Bad - mixed tabs and spaces
def func():
    if True:
        pass
\tpass  # IndentationError
```

**Solution:**
```bash
# Configure editor to use spaces
# .editorconfig
[*.py]
indent_style = space
indent_size = 4
```

### SyntaxError
```python
# Missing colon
def func()  # SyntaxError
    pass

# Missing parenthesis
print "hello"  # Python 2 syntax
```

## Runtime Errors

### AttributeError
```python
# Object doesn't have attribute
obj.nonexistent  # AttributeError

# Check if attribute exists
if hasattr(obj, 'attribute'):
    obj.attribute

# Use getattr with default
value = getattr(obj, 'attribute', default)
```

### TypeError
```python
# Wrong argument types
def add(a, b):
    return a + b

add("1", 2)  # TypeError

# Solution - type checking
def add(a, b):
    if not isinstance(a, (int, float)) or not isinstance(b, (int, float)):
        raise TypeError("Arguments must be numbers")
    return a + b
```

## Performance Issues

### Slow Code
```python
# Profile first
import cProfile
cProfile.run('my_function()')

# Common fixes
# 1. Use appropriate data structures
# 2. Avoid unnecessary loops
# 3. Use built-in functions
# 4. Consider caching
```

### Memory Issues
```python
# Check memory usage
import sys
print(sys.getsizeof(object))

# Use generators for large datasets
def read_large_file():
    with open('large.txt') as f:
        for line in f:
            yield line
```

## Package Management

### pip Issues
```bash
# Upgrade pip
pip install --upgrade pip

# Clear cache
pip cache purge

# Check pip configuration
pip config list
```

### conda Issues
```bash
# Update conda
conda update conda

# Clean packages
conda clean --all

# Check environment
conda list
```

## IDE Issues

### VS Code Python
```json
// settings.json
{
    "python.defaultInterpreterPath": "./venv/bin/python",
    "python.linting.enabled": true,
    "python.linting.pylintEnabled": true
}
```

### Import Resolution
```bash
# Reload VS Code window
Cmd+Shift+P > "Reload Window"

# Select interpreter
Cmd+Shift+P > "Python: Select Interpreter"
```

## Best Practices

1. Always use virtual environments
2. Pin dependencies in requirements.txt
3. Use `requirements-dev.txt` for dev tools
4. Check Python version compatibility
5. Use `pyproject.toml` for modern projects
6. Run linters before committing
7. Profile before optimizing
8. Check encoding when reading files
