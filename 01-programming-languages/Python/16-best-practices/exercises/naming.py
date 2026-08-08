"""
Module 16 - Best Practices: Naming Exercises
Difficulty: ⭐⭐ (Intermediate)
Topic: Python naming conventions and best practices
"""


# =============================================================================
# Exercise 1: Variable Naming (⭐⭐)
# =============================================================================

# TODO: Rename variables to follow PEP 8 naming conventions
# Current (bad) naming:
myvar = 10
anotherVariable = 20
x = "hello"
data_list = [1, 2, 3]

# TODO: Create properly named variables
# Use descriptive names that explain purpose

def exercise_1_variable_naming():
    """
    Fix variable naming to follow PEP 8.
    
    TODO:
    1. Use snake_case for variables
    2. Use descriptive names
    3. Avoid single letters except in loops
    """
    # TODO: Fix these variable names
    pass


# =============================================================================
# Exercise 2: Function Naming (⭐⭐⭐)
# =============================================================================

# TODO: Rename functions to follow PEP 8 naming conventions

def calc(x, y):
    """Bad name - too vague."""
    return x + y

def getData():
    """Bad name - uses camelCase."""
    return {"key": "value"}

def process_data_and_return_result(data):
    """Too long and redundant."""
    return data

# TODO: Create properly named functions

def exercise_2_function_naming():
    """
    Fix function naming to follow PEP 8.
    
    TODO:
    1. Use snake_case for functions
    2. Use verb phrases
    3. Be descriptive but concise
    """
    pass


# =============================================================================
# Exercise 3: Class Naming (⭐⭐⭐)
# =============================================================================

# TODO: Rename classes to follow PEP 8 naming conventions

class my_class:
    """Bad name - uses snake_case."""
    pass

class dataProcessor:
    """Bad name - uses camelCase."""
    pass

class XMLParserForHTMLContent:
    """Inconsistent naming."""
    pass

# TODO: Create properly named classes

def exercise_3_class_naming():
    """
    Fix class naming to follow PEP 8.
    
    TODO:
    1. Use PascalCase for classes
    2. Use nouns
    3. Be descriptive
    """
    pass


# =============================================================================
# Exercise 4: Constant Naming (⭐⭐⭐)
# =============================================================================

# TODO: Rename constants to follow PEP 8 naming conventions

max_retries = 3
default_timeout = 30
API_KEY = "secret"

# TODO: Create properly named constants

def exercise_4_constant_naming():
    """
    Fix constant naming to follow PEP 8.
    
    TODO:
    1. Use UPPER_SNAKE_CASE for constants
    2. Group related constants
    """
    pass


# =============================================================================
# Exercise 5: Comprehensive Refactoring (⭐⭐⭐⭐)
# =============================================================================

# TODO: Refactor this code to follow all naming conventions

class userMgr:
    def __init__(self, nm, em):
        self.nm = nm
        self.em = em
        self.isActive = True
    
    def getUserData(self):
        return {"name": self.nm, "email": self.em}
    
    def setActive(self, status):
        self.isActive = status

def calcTotal(items):
    total = 0
    for item in items:
        total += item.get("price", 0)
    return total

# TODO: Refactor with proper naming

def exercise_5_comprehensive_refactoring():
    """
    Refactor code to follow all naming conventions.
    
    TODO:
    1. Rename all variables, functions, classes
    2. Add docstrings
    3. Follow PEP 8 throughout
    """
    pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 16 - Naming Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Variable Naming")
    try:
        result = exercise_1_variable_naming()
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Function Naming")
    try:
        result = exercise_2_function_naming()
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Class Naming")
    try:
        result = exercise_3_class_naming()
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Constant Naming")
    try:
        result = exercise_4_constant_naming()
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Comprehensive Refactoring")
    try:
        result = exercise_5_comprehensive_refactoring()
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
