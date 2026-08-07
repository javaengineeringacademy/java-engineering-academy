"""
pytest Testing Basics
Demonstrates fixtures, parametrize, exceptions, and mocking
"""

import pytest

# ============================================
# Code Under Test
# ============================================

def add(a, b):
    return a + b

def divide(a, b):
    if b == 0:
        raise ValueError("Cannot divide by zero")
    return a / b

def is_even(n):
    return n % 2 == 0

def process_user(user):
    if not user.get('name'):
        raise ValueError("Name is required")
    if not user.get('email'):
        raise ValueError("Email is required")
    return {
        'name': user['name'].upper(),
        'email': user['email'].lower(),
        'active': user.get('active', True)
    }

class Calculator:
    def __init__(self):
        self.history = []

    def add(self, a, b):
        result = a + b
        self.history.append(('add', a, b, result))
        return result

    def multiply(self, a, b):
        result = a * b
        self.history.append(('multiply', a, b, result))
        return result

# ============================================
# Basic Tests
# ============================================

def test_add_positive():
    assert add(2, 3) == 5

def test_add_negative():
    assert add(-1, -1) == -2

def test_add_zero():
    assert add(0, 5) == 5

def test_divide_normal():
    assert divide(10, 2) == 5.0

def test_divide_by_zero():
    with pytest.raises(ValueError, match="Cannot divide by zero"):
        divide(10, 0)

# ============================================
# Parametrize
# ============================================

@pytest.mark.parametrize("input,expected", [
    (2, True),
    (3, False),
    (4, True),
    (5, False),
    (0, True),
    (-2, True),
    (-3, False),
])
def test_is_even(input, expected):
    assert is_even(input) == expected

@pytest.mark.parametrize("a,b,expected", [
    (1, 2, 3),
    (5, 5, 10),
    (-1, 1, 0),
    (0, 0, 0),
])
def test_add_parametrized(a, b, expected):
    assert add(a, b) == expected

# ============================================
# Fixtures
# ============================================

@pytest.fixture
def sample_user():
    return {
        'name': 'Alice',
        'email': 'Alice@Example.COM',
        'age': 30
    }

@pytest.fixture
def calculator():
    return Calculator()

def test_user_name(sample_user):
    result = process_user(sample_user)
    assert result['name'] == 'ALICE'

def test_user_email(sample_user):
    result = process_user(sample_user)
    assert result['email'] == 'alice@example.com'

def test_user_active_by_default(sample_user):
    result = process_user(sample_user)
    assert result['active'] is True

def test_calculator_add(calculator):
    assert calculator.add(2, 3) == 5
    assert len(calculator.history) == 1

def test_calculator_multiply(calculator):
    assert calculator.multiply(4, 5) == 20
    assert calculator.history[0] == ('multiply', 4, 5, 20)

def test_calculator_history(calculator):
    calculator.add(1, 2)
    calculator.multiply(3, 4)
    assert len(calculator.history) == 2
    assert calculator.history[0][0] == 'add'
    assert calculator.history[1][0] == 'multiply'

# ============================================
# Exception Testing
# ============================================

def test_process_user_missing_name():
    with pytest.raises(ValueError, match="Name is required"):
        process_user({'email': 'test@example.com'})

def test_process_user_missing_email():
    with pytest.raises(ValueError, match="Email is required"):
        process_user({'name': 'Alice'})

# ============================================
# Marks (skip, skipif)
# ============================================

@pytest.mark.skip(reason="Not implemented yet")
def test_future_feature():
    pass

@pytest.mark.skipif(
    not hasattr(str, 'upper'),
    reason="String upper not available"
)
def test_string_upper():
    assert "hello".upper() == "HELLO"

# ============================================
# Main for running without pytest
# ============================================

if __name__ == "__main__":
    print("Running tests manually...")
    print()

    # Run basic tests
    test_add_positive()
    print("PASS: test_add_positive")

    test_add_negative()
    print("PASS: test_add_negative")

    test_add_zero()
    print("PASS: test_add_zero")

    test_divide_normal()
    print("PASS: test_divide_normal")

    test_divide_by_zero()
    print("PASS: test_divide_by_zero")

    # Run parametrized tests
    test_cases = [
        (2, True), (3, False), (4, True),
        (5, False), (0, True), (-2, True), (-3, False)
    ]
    for input_val, expected in test_cases:
        test_is_even(input_val, expected)
    print(f"PASS: test_is_even ({len(test_cases)} cases)")

    # Run fixture tests
    user = {
        'name': 'Alice',
        'email': 'Alice@Example.COM',
        'age': 30
    }
    result = process_user(user)
    assert result['name'] == 'ALICE'
    assert result['email'] == 'alice@example.com'
    print("PASS: test_user_name")
    print("PASS: test_user_email")

    # Calculator tests
    calc = Calculator()
    assert calc.add(2, 3) == 5
    assert calc.multiply(4, 5) == 20
    assert len(calc.history) == 2
    print("PASS: test_calculator_add")
    print("PASS: test_calculator_multiply")
    print("PASS: test_calculator_history")

    # Exception tests
    try:
        process_user({'email': 'test@example.com'})
        assert False, "Should have raised ValueError"
    except ValueError as e:
        assert "Name is required" in str(e)
    print("PASS: test_process_user_missing_name")

    print()
    print("All tests passed!")
    print()
    print("To run with pytest: pytest test_basics.py -v")
