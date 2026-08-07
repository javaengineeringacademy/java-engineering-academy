"""
Module 05: Testing - Test Example Solutions
Practice testing concepts in Python.
"""

import pytest
from unittest.mock import Mock, patch, MagicMock
import math
import os


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
    return [1, 2, 3, 4, 5]


@pytest.fixture
def sample_dict():
    """Fixture providing a sample dictionary."""
    return {"a": 1, "b": 2, "c": 3}


def test_sample_list_length(sample_list):
    """Test that sample list has correct length."""
    assert len(sample_list) == 5


def test_sample_dict_keys(sample_dict):
    """Test that sample dict has expected keys."""
    assert set(sample_dict.keys()) == {"a", "b", "c"}


def test_list_operations(sample_list):
    """Test various list operations."""
    assert sample_list[0] == 1
    assert sample_list[-1] == 5
    assert sum(sample_list) == 15


# Exercise 2: Parametrized Tests (Medium)
@pytest.mark.parametrize("a,b,expected", [
    (2, 3, 6),
    (0, 5, 0),
    (-1, 4, -4),
    (10, 10, 100),
])
def test_multiply_parametrized(a, b, expected):
    """Test multiply with multiple inputs."""
    assert multiply(a, b) == expected


@pytest.mark.parametrize("base,exp,expected", [
    (2, 10, 1024),
    (5, 0, 1),
    (10, 1, 10),
    (2, -1, 0.5),
])
def test_power_parametrized(base, exp, expected):
    """Test power function with multiple inputs."""
    assert power(base, exp) == expected


@pytest.mark.parametrize("data,expected", [
    ([1, 2, 3], [2, 4, 6]),
    ([], []),
    ([0], [0]),
    ([-1, -2], [-2, -4]),
])
def test_process_data_parametrized(data, expected):
    """Test process_data with multiple inputs."""
    assert process_data(data) == expected


# Exercise 3: Mocking with pytest (Medium)
def test_process_data_with_mock_validator():
    """Test process_data with mock validator."""
    mock_validator = Mock(return_value=True)
    result = process_data([1, 2, 3], validator=mock_validator)
    assert result == [2, 4, 6]
    mock_validator.assert_called_once_with([1, 2, 3])


@patch('builtins.print')
def test_print_called(mock_print):
    """Test that print is called."""
    print("Hello")
    mock_print.assert_called_once_with("Hello")


def test_read_config_mock_filesystem(tmp_path):
    """Test read_config with temporary file."""
    config_file = tmp_path / "config.txt"
    config_file.write_text("test config")
    result = read_config(str(config_file))
    assert result == "test config"


# Exercise 4: Conftest and Plugins (Medium)
@pytest.fixture
def setup_database():
    """Fixture that sets up and tears down database."""
    db = {}
    yield db
    db.clear()


def test_database_fixture(setup_database):
    """Test using database fixture."""
    setup_database["key"] = "value"
    assert setup_database["key"] == "value"


@pytest.fixture
def mock_api():
    """Fixture providing a mock API client."""
    api = Mock()
    api.get.return_value = {"status": "ok"}
    return api


# Exercise 5: Markers and Skipping (Medium)
@pytest.mark.skip(reason="Not implemented yet")
def test_not_implemented():
    """This test should be skipped."""
    pass


@pytest.mark.skipif(os.name == 'nt', reason="Unix only test")
def test_unix_only():
    """This test only runs on Unix systems."""
    assert os.name != 'nt'


@pytest.mark.slow
def test_slow_operation():
    """Mark test as slow."""
    assert True


@pytest.mark.parametrize("input,expected", [
    (1, 1),
    (2, 4),
    (3, 9),
])
def test_with_markers(input, expected):
    """Test with markers."""
    assert input ** 2 == expected


# Exercise 6: Exception Testing (Medium)
def test_process_data_validation_error():
    """Test that validation error is raised."""
    with pytest.raises(ValueError, match="Validation failed"):
        process_data([1, 2, 3], validator=lambda x: False)


def test_read_config_file_not_found():
    """Test that FileNotFoundError is raised."""
    with pytest.raises(FileNotFoundError):
        read_config("/nonexistent/path/config.txt")


def test_calculation_error():
    """Test various calculation errors."""
    with pytest.raises(ZeroDivisionError):
        1 / 0


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
    yield


if __name__ == "__main__":
    pytest.main([__file__, "-v"])
