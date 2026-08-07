"""
Module 16: Best Practices - Naming Conventions Exercises
=======================================================
Practice proper Python naming conventions.
"""

# =============================================================================
# Exercise 1: Name Validator (★☆☆☆☆)
# =============================================================================
# TODO: Validate Python naming conventions

def validate_name(name, name_type):
    """Validate if name follows convention for its type.
    
    name_type: 'variable', 'function', 'class', 'constant', 'module'
    """
    # TODO: Implement PEP8 naming rules
    pass

# Test Cases
def test_name_validator():
    assert validate_name("my_variable", "variable") is True
    assert validate_name("myVariable", "variable") is False  # Should be snake_case
    assert validate_name("MyClass", "class") is True
    assert validate_name("my_class", "class") is False  # Should be PascalCase
    assert validate_name("MAX_SIZE", "constant") is True
    assert validate_name("my_func", "function") is True
    print("✓ Exercise 1 passed: name validation works")

# =============================================================================
# Exercise 2: Name Generator (★★☆☆☆)
# =============================================================================
# TODO: Generate proper names for variables based on context

def generate_variable_name(context, name_type="variable"):
    """Generate appropriate variable name for context."""
    # TODO: Use context to suggest meaningful names
    pass

# Test Tests
def test_name_generator():
    assert generate_variable_name("list of users") == "users"
    assert generate_variable_name("count of items") == "item_count"
    assert generate_variable_name("flag for debug mode") == "debug_enabled"
    print("✓ Exercise 2 passed: name generation works")

# =============================================================================
# Exercise 3: Name Refactor (★★★☆☆)
# =============================================================================
# TODO: Rename variables to follow conventions

def refactor_names(code_string):
    """Refactor all names in code to follow PEP8."""
    # TODO: Rename variables, functions, etc.
    pass

# Test Tests
def test_name_refactor():
    bad_names = """
MyVariable = 10
def myFunction(paramOne):
    secondParam = paramOne * MyVariable
    return secondParam
"""
    refactored = refactor_names(bad_names)
    assert "my_variable" in refactored
    assert "my_function" in refactored
    assert "second_param" in refactored
    print("✓ Exercise 3 passed: names refactored to snake_case")

# =============================================================================
# Exercise 4: Docstring Parameter Names (★★★★☆)
# =============================================================================
# TODO: Extract and validate parameter names from docstrings

def extract_params_from_docstring(docstring):
    """Extract parameter names and types from docstring."""
    # TODO: Parse Google/Numpy style docstrings
    pass

# Test Tests
def test_docstring_params():
    docstring = """
    Process user data.
    
    Args:
        user_id: The unique identifier for the user.
        data (dict): The data to process.
        verbose (bool): Enable verbose logging.
        
    Returns:
        bool: True if successful.
    """
    params = extract_params_from_docstring(docstring)
    assert "user_id" in params
    assert params["data"]["type"] == "dict"
    assert params["verbose"]["type"] == "bool"
    print(f"✓ Exercise 4 passed: extracted {len(params)} parameters")

# =============================================================================
# Exercise 5: Naming Convention Enforcer (★★★★★)
# =============================================================================
# TODO: Enforce naming conventions across codebase

class NamingEnforcer:
    """Enforce naming conventions in Python code."""
    # TODO: Check variable, function, class, constant names
    # TODO: Support custom rules
    pass

# Test Tests
def test_naming_enforcer():
    enforcer = NamingEnforcer()
    
    violations = enforcer.check("""
myList = []
def GetData():
    MAX_VALUE = 100
    class my_class:
        pass
""")
    
    assert len(violations) >= 3
    assert any("myList" in v for v in violations)
    assert any("GetData" in v for v in violations)
    assert any("my_class" in v for v in violations)
    print(f"✓ Exercise 5 passed: found {len(violations)} naming violations")

if __name__ == "__main__":
    print("Running Naming Conventions Exercises...")
    print("=" * 50)
    test_name_validator()
    test_name_generator()
    test_name_refactor()
    test_docstring_params()
    test_naming_enforcer()
    print("=" * 50)
    print("All tests passed!")
