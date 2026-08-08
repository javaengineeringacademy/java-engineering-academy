"""
Module 16 - Best Practices: Project Structure Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Python project organization and packaging
"""

import os
from pathlib import Path


# =============================================================================
# Exercise 1: Package Structure (⭐⭐⭐)
# =============================================================================

def exercise_1_package_structure():
    """
    Design a proper Python package structure.
    
    TODO:
    1. Create directory structure
    2. Add __init__.py files
    3. Organize modules logically
    """
    structure = {
        'project_name': {
            '__init__.py': '',
            'core': {
                '__init__.py': '',
                'module1.py': '',
                'module2.py': '',
            },
            'utils': {
                '__init__.py': '',
                'helpers.py': '',
            },
        }
    }
    
    # TODO: Create the structure
    return structure


# =============================================================================
# Exercise 2: Module Imports (⭐⭐⭐)
# =============================================================================

def exercise_2_module_imports():
    """
    Use proper import styles.
    
    TODO:
    1. Use absolute imports
    2. Use relative imports appropriately
    3. Organize imports (stdlib, third-party, local)
    """
    # Bad imports (don't do this)
    # from module import *
    # import module1, module2, module3
    
    # TODO: Write proper imports
    pass


# =============================================================================
# Exercise 3: Configuration Management (⭐⭐⭐⭐)
# =============================================================================

class Config:
    """
    Implement configuration management.
    
    TODO:
    1. Load config from environment
    2. Support multiple environments
    3. Use dataclass or similar
    """
    def __init__(self):
        # TODO: Initialize config
        pass
    
    def get(self, key, default=None):
        # TODO: Get config value
        pass
    
    def set(self, key, value):
        # TODO: Set config value
        pass


# =============================================================================
# Exercise 4: Entry Points (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_entry_points():
    """
    Create proper entry points for a package.
    
    TODO:
    1. Create __main__.py
    2. Create CLI interface
    3. Support package execution
    """
    # TODO: Create entry point code
    pass


# =============================================================================
# Exercise 5: Documentation Structure (⭐⭐⭐⭐)
# =============================================================================

def exercise_5_documentation():
    """
    Create proper documentation structure.
    
    TODO:
    1. Create README.md template
    2. Add docstrings to all public APIs
    3. Create CHANGELOG.md
    """
    readme_template = """
    # Project Name
    
    ## Description
    Brief description of the project.
    
    ## Installation
    pip install project-name
    
    ## Usage
    Basic usage examples.
    
    ## Configuration
    Configuration options.
    
    ## Contributing
    Guidelines for contributors.
    
    ## License
    License information.
    """
    
    # TODO: Create documentation files
    return readme_template


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 16 - Project Structure Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Package Structure")
    try:
        result = exercise_1_package_structure()
        assert isinstance(result, dict)
        print(f"  Structure: {list(result.keys())}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Module Imports")
    try:
        result = exercise_2_module_imports()
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Configuration Management")
    try:
        config = Config()
        assert hasattr(config, 'get')
        assert hasattr(config, 'set')
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Entry Points")
    try:
        result = exercise_4_entry_points()
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Documentation Structure")
    try:
        result = exercise_5_documentation()
        assert isinstance(result, str)
        assert "# Project Name" in result
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
