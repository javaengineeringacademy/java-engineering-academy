"""
Python Testing - Pytest Exercises
Complete each exercise by implementing the test functions.
Run with: python -m pytest pytest_basics.py -v
"""

import pytest
from unittest.mock import Mock, patch, MagicMock
import math
import os


# Functions to test

def multiply(a, b):
    """Multiply two numbers."""
    return a * b

def power(base, exp):
    """Calculate base to the power of exp."""
    return base ** exp

def process_data(data, validator=None):
    """Process data with optional validator."""
    if validator and not validator(data):
        raise ValueError("Validation failed")
    return [x * 2 for x in data]

def read_config(path):
    """Read configuration from file."""
    if not os.path.exists(path):
        raise FileNotFoundError(f"Config not found: {path}")
    with open(path, 'r') as f:
        return f.read()


# ==================== EXERCISE TEST CASES ====================

# Exercise 1: Pytest Fixtures (Easy)
@pytest.fixture
def sample_list():
    """Fixture providing a sample list."""
    # TODO: Implement fixture
    pass

@pytest.fixture
def sample_dict():
    """Fixture providing a sample dictionary."""
    # TODO: Implement fixture
    pass

def test_sample_list_length(sample_list):
    """Test that sample list has correct length."""
    # TODO: Implement test using fixture
    pass

def test_sample_dict_keys(sample_dict):
    """Test that sample dict has expected keys."""
    # TODO: Implement test using fixture
    pass

def test_list_operations(sample_list):
    """Test various list operations."""
    # TODO: Implement test
    pass


# Exercise 2: Parametrized Tests (Medium)
@pytest.mark.parametrize("a,b,expected", [
    (2, 3, 6),
    (0, 5, 0),
    (-1, 4, -4),
    (10, 10, 100),
])
def test_multiply_parametrized(a, b, expected):
    """Test multiply with multiple inputs."""
    # TODO: Implement test
    pass

@pytest.mark.parametrize("base,exp,expected", [
    (2, 10, 1024),
    (5, 0, 1),
    (10, 1, 10),
    (2, -1, 0.5),
])
def test_power_parametrized(base, exp, expected):
    """Test power function with multiple inputs."""
    # TODO: Implement test
    pass

@pytest.mark.parametrize("data,expected", [
    ([1, 2, 3], [2, 4, 6]),
    ([], []),
    ([0], [0]),
    ([-1, -2], [-2, -4]),
])
def test_process_data_parametrized(data, expected):
    """Test process_data with multiple inputs."""
    # TODO: Implement test
    pass


# Exercise 3: Mocking with pytest (Medium)
def test_process_data_with_mock_validator():
    """Test process_data with mock validator."""
    # TODO: Create mock validator and test
    pass

@patch('builtins.print')
def test_print_called(mock_print):
    """Test that print is called."""
    # TODO: Implement test
    pass

def test_read_config_mock_filesystem(tmp_path):
    """Test read_config with temporary file."""
    # TODO: Create temp file and test
    pass


# Exercise 4: Conftest and Plugins (Medium)
# These fixtures would normally be in conftest.py

@pytest.fixture
def setup_database():
    """Fixture that sets up and tears down database."""
    # TODO: Implement setup
    db = {}
    yield db
    # TODO: Implement teardown

def test_database_fixture(setup_database):
    """Test using database fixture."""
    # TODO: Implement test
    pass

@pytest.fixture
def mock_api():
    """Fixture providing a mock API client."""
    # TODO: Implement fixture
    pass


# Exercise 5: Markers and Skipping (Medium)
@pytest.mark.skip(reason="Not implemented yet")
def test_not_implemented():
    """This test should be skipped."""
    pass

@pytest.mark.skipif(os.name == 'nt', reason="Unix only test")
def test_unix_only():
    """This test only runs on Unix systems."""
    pass

@pytest.mark.slow
def test_slow_operation():
    """Mark test as slow."""
    # TODO: Implement test
    pass

@pytest.mark.parametrize("input,expected", [
    (1, 1),
    (2, 4),
    (3, 9),
])
def test_with_markers(input, expected):
    """Test with markers."""
    # TODO: Implement test
    pass


# Exercise 6: Exception Testing (Medium)
def test_process_data_validation_error():
    """Test that validation error is raised."""
    # TODO: Implement test
    pass

def test_read_config_file_not_found():
    """Test that FileNotFoundError is raised."""
    # TODO: Implement test
    pass

def test_calculation_error():
    """Test various calculation errors."""
    # TODO: Implement test
    pass


# ==================== FIXTURES ====================

@pytest.fixture
def calculator():
    """Simple calculator fixture."""
    class Calculator:
        def add(self, a, b): return a + b
        def subtract(self, a, b): return a - b
        def multiply(self, a, b): return a * b
        def divide(self, a, b):
            if b == 0:
                raise ValueError("Division by zero")
            return a / b
    return Calculator()

@pytest.fixture(autouse=True)
def setup_and_teardown():
    """Auto-use fixture for setup and teardown."""
    # Setup
    yield
    # Teardown


if __name__ == "__main__":
    pytest.main([__file__, "-v"])
