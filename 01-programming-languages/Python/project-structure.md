# Python Project Structure

## Standard Layout

### src Layout (Recommended)

```
myproject/
├── src/
│   └── myproject/
│       ├── __init__.py
│       ├── main.py
│       ├── models.py
│       └── utils.py
├── tests/
│   ├── __init__.py
│   ├── test_main.py
│   └── test_models.py
├── docs/
│   └── README.md
├── pyproject.toml
├── requirements.txt
├── README.md
├── LICENSE
└── .gitignore
```

### Flat Layout

```
myproject/
├── myproject/
│   ├── __init__.py
│   ├── main.py
│   └── utils.py
├── tests/
├── pyproject.toml
├── README.md
└── .gitignore
```

### src Layout Benefits
- Prevents accidental imports from source directory
- Cleaner testing (tests import installed package)
- Better separation of source and tests

## Essential Files

### pyproject.toml
```toml
[build-system]
requires = ["setuptools>=68.0"]
build-backend = "setuptools.backends._legacy:_Backend"

[project]
name = "myproject"
version = "0.1.0"

[tool.setuptools.packages.find]
where = ["src"]
```

### setup.py (Legacy)
```python
from setuptools import setup, find_packages

setup(
    name="myproject",
    version="0.1.0",
    packages=find_packages(where="src"),
    package_dir={"": "src"},
)
```

### __init__.py
```python
"""MyProject - A brief description."""

__version__ = "0.1.0"
```

### .gitignore
```gitignore
__pycache__/
*.py[cod]
*.egg-info/
dist/
build/
*.egg
.env
venv/
.venv/
```

## Directory Structure

### Source Code Organization
```
src/myproject/
├── __init__.py
├── __main__.py      # Entry point
├── config.py        # Configuration
├── models/          # Data models
│   ├── __init__.py
│   └── user.py
├── services/        # Business logic
│   ├── __init__.py
│   └── auth.py
├── utils/           # Utilities
│   ├── __init__.py
│   └── helpers.py
└── api/             # API endpoints
    ├── __init__.py
    └── routes.py
```

### Test Structure
```
tests/
├── __init__.py
├── conftest.py      # Fixtures
├── unit/
│   ├── __init__.py
│   └── test_models.py
├── integration/
│   ├── __init__.py
│   └── test_api.py
└── fixtures/        # Test data
    └── users.json
```

## Entry Points

### Console Scripts
```toml
[project.scripts]
myproject = "myproject.main:main"
```

### Python Module
```bash
python -m myproject
```

### __main__.py
```python
"""Entry point for python -m myproject."""
from myproject.main import main

if __name__ == "__main__":
    main()
```

## Best Practices

1. Use src layout for new projects
2. Keep tests outside source code
3. Include `__init__.py` in test directories
4. Use `conftest.py` for shared fixtures
5. Separate unit and integration tests
6. Include type hints in all modules
7. Add docstrings to all public functions
8. Use `pyproject.toml` over `setup.py`
