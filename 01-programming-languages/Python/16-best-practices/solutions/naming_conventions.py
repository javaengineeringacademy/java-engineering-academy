"""
Module 16: Best Practices - Naming Conventions Solutions
Practice naming conventions in Python.
"""

from typing import List, Dict, Any
from dataclasses import dataclass
from enum import Enum


class NamingConvention(Enum):
    """Python naming conventions."""
    VARIABLE = "snake_case"
    FUNCTION = "snake_case"
    CLASS = "PascalCase"
    CONSTANT = "UPPER_SNAKE_CASE"
    MODULE = "snake_case"
    PACKAGE = "snake_case"
    PRIVATE = "_leading_underscore"
    NAME_MANGLED = "__double_leading_underscore"


def snake_case_to_pascal_case(name: str) -> str:
    """Convert snake_case to PascalCase."""
    return ''.join(word.capitalize() for word in name.split('_'))


def pascal_case_to_snake_case(name: str) -> str:
    """Convert PascalCase to snake_case."""
    import re
    s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
    return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()


def validate_variable_name(name: str) -> bool:
    """Validate if a variable name follows Python conventions."""
    if not name.isidentifier():
        return False

    # Should be snake_case
    if '_' in name:
        parts = name.split('_')
        return all(part.islower() or part.isdigit() for part in parts if part)

    # Single word should be lowercase
    return name.islower() or name.isupper()


def validate_class_name(name: str) -> bool:
    """Validate if a class name follows Python conventions."""
    if not name.isidentifier():
        return False

    # Should be PascalCase
    return name[0].isupper() and not '_' in name


def validate_function_name(name: str) -> bool:
    """Validate if a function name follows Python conventions."""
    if not name.isidentifier():
        return False

    # Should be snake_case
    if '_' in name:
        parts = name.split('_')
        return all(part.islower() or part.isdigit() for part in parts if part)

    return name.islower()


def validate_constant_name(name: str) -> bool:
    """Validate if a constant name follows Python conventions."""
    if not name.isidentifier():
        return False

    # Should be UPPER_SNAKE_CASE
    return name.isupper() or (name.isupper() and '_' in name)


class NamingConventionChecker:
    """Check naming conventions in code."""

    def __init__(self):
        self.issues = []

    def check_variable(self, name: str, line_num: int):
        """Check variable naming convention."""
        if not validate_variable_name(name):
            self.issues.append(f"Line {line_num}: Variable '{name}' should be snake_case")

    def check_class(self, name: str, line_num: int):
        """Check class naming convention."""
        if not validate_class_name(name):
            self.issues.append(f"Line {line_num}: Class '{name}' should be PascalCase")

    def check_function(self, name: str, line_num: int):
        """Check function naming convention."""
        if not validate_function_name(name):
            self.issues.append(f"Line {line_num}: Function '{name}' should be snake_case")

    def check_constant(self, name: str, line_num: int):
        """Check constant naming convention."""
        if not validate_constant_name(name):
            self.issues.append(f"Line {line_num}: Constant '{name}' should be UPPER_SNAKE_CASE")

    def get_issues(self) -> List[str]:
        """Get all naming issues."""
        return self.issues.copy()


class NameConverter:
    """Convert between different naming conventions."""

    @staticmethod
    def to_snake_case(name: str) -> str:
        """Convert any case to snake_case."""
        return pascal_case_to_snake_case(name)

    @staticmethod
    def to_pascal_case(name: str) -> str:
        """Convert any case to PascalCase."""
        return snake_case_to_pascal_case(name)

    @staticmethod
    def to_camel_case(name: str) -> str:
        """Convert any case to camelCase."""
        pascal = snake_case_to_pascal_case(name)
        return pascal[0].lower() + pascal[1:]

    @staticmethod
    def to_upper_snake_case(name: str) -> str:
        """Convert any case to UPPER_SNAKE_CASE."""
        return snake_case_to_pascal_case(name).upper()


if __name__ == "__main__":
    print("Testing Naming Conventions Solutions...")

    # Test conversions
    assert snake_case_to_pascal_case("my_variable") == "MyVariable"
    assert snake_case_to_pascal_case("class_name") == "ClassName"
    print("✓ Exercise 1 passed: snake to pascal works")

    assert pascal_case_to_snake_case("MyVariable") == "my_variable"
    assert pascal_case_to_snake_case("ClassName") == "class_name"
    print("✓ Exercise 2 passed: pascal to snake works")

    # Test validations
    assert validate_variable_name("my_var") is True
    assert validate_variable_name("MyVar") is False
    assert validate_variable_name("my-var") is False
    print("✓ Exercise 3 passed: variable validation works")

    assert validate_class_name("MyClass") is True
    assert validate_class_name("my_class") is False
    print("✓ Exercise 4 passed: class validation works")

    assert validate_function_name("my_function") is True
    assert validate_function_name("MyFunction") is False
    print("✓ Exercise 5 passed: function validation works")

    assert validate_constant_name("MAX_VALUE") is True
    assert validate_constant_name("max_value") is False
    print("✓ Exercise 6 passed: constant validation works")

    # Test checker
    checker = NamingConventionChecker()
    checker.check_variable("MyVar", 1)
    checker.check_class("my_class", 2)
    checker.check_function("MyFunction", 3)
    checker.check_constant("max_value", 4)
    assert len(checker.get_issues()) == 4
    print("✓ Exercise 7 passed: checker works")

    # Test converter
    converter = NameConverter()
    assert converter.to_snake_case("MyClass") == "my_class"
    assert converter.to_pascal_case("my_class") == "MyClass"
    assert converter.to_camel_case("my_class") == "myClass"
    assert converter.to_upper_snake_case("my_class") == "MY_CLASS"
    print("✓ Exercise 8 passed: converter works")

    print("All Naming Conventions solutions passed!")
