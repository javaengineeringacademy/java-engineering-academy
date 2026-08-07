"""
Module 16: Best Practices - Code Review Exercises
=================================================
Practice identifying and fixing code quality issues.
"""

# =============================================================================
# Exercise 1: Bug Identifier (★☆☆☆☆)
# =============================================================================
# TODO: Find and fix bugs in the code

def find_average(numbers):
    """Calculate average of list of numbers."""
    # Bug: Division by zero when list is empty
    # Bug: Doesn't handle non-numeric values
    # TODO: Fix both bugs
    pass

# Test Cases
def test_bug_identifier():
    assert find_average([1, 2, 3, 4, 5]) == 3.0
    assert find_average([]) is None  # Should return None, not crash
    assert find_average([10, "invalid", 20]) is None  # Should handle invalid input
    print("✓ Exercise 1 passed: bugs fixed correctly")

# =============================================================================
# Exercise 2: Code Smell Detector (★★☆☆☆)
# =============================================================================
# TODO: Identify code smells in given code

def detect_code_smells(code_string):
    """Analyze code and return list of detected smells."""
    # TODO: Detect: long methods, deep nesting, magic numbers, etc.
    pass

# Test Cases
def test_code_smell_detector():
    bad_code = """
def process(data):
    result = []
    for item in data:
        if item > 0:
            if item < 100:
                if item % 2 == 0:
                    result.append(item * 3.14159)
    return result
"""
    smells = detect_code_smells(bad_code)
    assert "deep_nesting" in smells
    assert "magic_number" in smells
    print(f"✓ Exercise 2 passed: detected {len(smells)} code smells")

# =============================================================================
# Exercise 3: Refactoring Helper (★★★☆☆)
# =============================================================================
# TODO: Refactor code to improve readability

def refactor_long_function(code_string):
    """Refactor a long function into smaller functions."""
    # TODO: Extract helper functions
    # TODO: Add proper documentation
    pass

# Test Tests
def test_refactoring():
    long_func = """
def process_order(order):
    # 50+ lines of code doing validation, calculation, persistence...
    pass
"""
    refactored = refactor_long_function(long_func)
    assert "def validate_order" in refactored
    assert "def calculate_total" in refactored
    assert "def save_order" in refactored
    print("✓ Exercise 3 passed: function refactored into smaller parts")

# =============================================================================
# Exercise 4: Documentation Generator (★★★★☆)
# =============================================================================
# TODO: Generate documentation for functions

def generate_docstring(func_name, code_string):
    """Generate proper docstring for a function."""
    # TODO: Analyze parameters and return type
    # TODO: Generate Google-style docstring
    pass

# Test Cases
def test_doc_generator():
    code = """
def calculate_discount(price, percentage):
    return price * (1 - percentage / 100)
"""
    docstring = generate_docstring("calculate_discount", code)
    assert "Args:" in docstring or "Parameters:" in docstring
    assert "price" in docstring
    assert "Returns:" in docstring
    print("✓ Exercise 4 passed: docstring generated")

# =============================================================================
# Exercise 5: Code Quality Scorer (★★★★★)
# =============================================================================
# TODO: Score code quality based on multiple metrics

def score_code_quality(code_string):
    """Score code quality from 0-100."""
    # TODO: Analyze: complexity, readability, documentation, etc.
    # TODO: Return score and breakdown
    pass

# Test Tests
def test_quality_scorer():
    good_code = """
def calculate_average(numbers: list[float]) -> float | None:
    \"\"\"Calculate the arithmetic mean of a list of numbers.
    
    Args:
        numbers: List of numeric values.
        
    Returns:
        The average, or None if list is empty.
        
    Raises:
        TypeError: If list contains non-numeric values.
    \"\"\"
    if not numbers:
        return None
    return sum(numbers) / len(numbers)
"""
    
    bad_code = """
def calc(d):
    s=0
    for x in d:s+=x
    return s/len(d)
"""
    
    good_score = score_code_quality(good_code)
    bad_score = score_code_quality(bad_code)
    
    assert good_score > bad_score
    assert good_score >= 70
    print(f"✓ Exercise 5 passed: good={good_score}, bad={bad_score}")

if __name__ == "__main__":
    print("Running Code Review Exercises...")
    print("=" * 50)
    test_bug_identifier()
    test_code_smell_detector()
    test_refactoring()
    test_doc_generator()
    test_quality_scorer()
    print("=" * 50)
    print("All tests passed!")
