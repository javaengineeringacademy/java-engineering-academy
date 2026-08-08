"""
Module 16 - Best Practices: Project Structure Solutions
Complete solutions with explanations

Python Project Structure Best Practices:
- src/ layout or flat layout
- Proper package organization
- Clear module boundaries
- Configuration management
- Entry points
"""

import os
from pathlib import Path
from dataclasses import dataclass
from typing import Optional


# =============================================================================
# Exercise 1: Package Structure - SOLUTION
# =============================================================================

def exercise_1_package_structure():
    """
    Design a proper Python package structure.
    """
    # Recommended project structure
    structure = """
    project_name/
    ├── README.md
    ├── setup.py
    ├── pyproject.toml
    ├── requirements.txt
    ├── src/
    │   └── project_name/
    │       ├── __init__.py
    │       ├── core/
    │       │   ├── __init__.py
    │       │   ├── module1.py
    │       │   └── module2.py
    │       └── utils/
    │           ├── __init__.py
    │           └── helpers.py
    ├── tests/
    │   ├── __init__.py
    │   ├── test_module1.py
    │   └── test_module2.py
    └── docs/
        └── index.md
    """
    
    return {
        'structure': structure,
        'has_src_layout': True,
        'has_tests': True,
        'has_docs': True,
    }


# =============================================================================
# Exercise 2: Module Imports - SOLUTION
# =============================================================================

# Good import style (organized by groups)
# 1. Standard library imports
import os
import sys
from pathlib import Path
from typing import List, Dict

# 2. Third-party imports
# import numpy as np
# import pandas as pd

# 3. Local imports
# from .core import Module1
# from .utils import helper_function


def exercise_2_module_imports():
    """
    Use proper import styles.
    """
    # Absolute imports (preferred)
    # from project_name.core.module1 import MyClass
    # from project_name.utils.helpers import helper_function
    
    # Relative imports (within package)
    # from .core import Module1
    # from ..utils import helper_function
    
    # Examples of good imports
    import json
    from collections import defaultdict
    from datetime import datetime
    
    return {
        'json': json,
        'defaultdict': defaultdict,
        'datetime': datetime,
    }


# =============================================================================
# Exercise 3: Configuration Management - SOLUTION
# =============================================================================

@dataclass
class Config:
    """
    Configuration management using dataclass.
    """
    database_host: str = "localhost"
    database_port: int = 5432
    database_name: str = "mydb"
    debug: bool = False
    api_key: Optional[str] = None
    
    @classmethod
    def from_env(cls):
        """Load configuration from environment variables."""
        import os
        return cls(
            database_host=os.getenv('DB_HOST', 'localhost'),
            database_port=int(os.getenv('DB_PORT', '5432')),
            database_name=os.getenv('DB_NAME', 'mydb'),
            debug=os.getenv('DEBUG', 'false').lower() == 'true',
            api_key=os.getenv('API_KEY'),
        )
    
    def get(self, key, default=None):
        """Get configuration value."""
        return getattr(self, key, default)
    
    def set(self, key, value):
        """Set configuration value."""
        if hasattr(self, key):
            setattr(self, key, value)


def exercise_3_configuration_management():
    """
    Test configuration management.
    """
    config = Config()
    assert config.database_host == "localhost"
    
    # Test get method
    assert config.get('database_port') == 5432
    assert config.get('nonexistent', 'default') == 'default'
    
    # Test set method
    config.set('debug', True)
    assert config.debug == True
    
    return {
        'config': config,
        'host': config.database_host,
        'port': config.database_port,
    }


# =============================================================================
# Exercise 4: Entry Points - SOLUTION
# =============================================================================

# __main__.py (allows running package as: python -m project_name)
"""
def main():
    # Entry point for the application
    print("Starting application...")
    
    # Parse arguments
    import argparse
    parser = argparse.ArgumentParser(description='My Application')
    parser.add_argument('--debug', action='store_true')
    args = parser.parse_args()
    
    # Initialize and run
    config = Config(debug=args.debug)
    print(f"Running with config: {config}")

if __name__ == "__main__":
    main()
"""


def exercise_4_entry_points():
    """
    Create proper entry points for a package.
    """
    entry_points = {
        'main_function': 'def main(): pass',
        'cli_interface': 'Uses argparse or click',
        'package_execution': 'python -m package_name',
    }
    
    return entry_points


# =============================================================================
# Exercise 5: Documentation Structure - SOLUTION
# =============================================================================

def exercise_5_documentation():
    """
    Create proper documentation structure.
    """
    readme_template = """# Project Name

Brief description of what this project does.

## Installation

```bash
pip install project-name
```

Or from source:

```bash
git clone https://github.com/user/project.git
cd project
pip install -e .
```

## Usage

```python
from project_name import MyClass

instance = MyClass()
result = instance.do_something()
```

## Configuration

| Option | Default | Description |
|--------|---------|-------------|
| DEBUG | false | Enable debug mode |
| DB_HOST | localhost | Database host |

## Development

```bash
# Install dev dependencies
pip install -e ".[dev]"

# Run tests
pytest

# Run linter
flake8 src/
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

MIT License
"""
    
    # API documentation example
    api_docstring = """
    def calculate_total(items: List[dict]) -> float:
        \"\"\"
        Calculate total price from list of items.
        
        Args:
            items: List of dictionaries with 'price' key
            
        Returns:
            Total price as float
            
        Raises:
            ValueError: If items list is empty
            
        Example:
            >>> items = [{'price': 10}, {'price': 20}]
            >>> calculate_total(items)
            30.0
        \"\"\"
        pass
    """
    
    return {
        'readme': readme_template,
        'api_docstring': api_docstring,
    }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 16 - Project Structure Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Package Structure")
    result = exercise_1_package_structure()
    assert isinstance(result, dict)
    assert result['has_src_layout'] == True
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Module Imports")
    result = exercise_2_module_imports()
    assert 'json' in result
    assert 'defaultdict' in result
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Configuration Management")
    result = exercise_3_configuration_management()
    assert result['host'] == 'localhost'
    assert result['port'] == 5432
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Entry Points")
    result = exercise_4_entry_points()
    assert isinstance(result, dict)
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Documentation Structure")
    result = exercise_5_documentation()
    assert 'readme' in result
    assert '# Project Name' in result['readme']
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
