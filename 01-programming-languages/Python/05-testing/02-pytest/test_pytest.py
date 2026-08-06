"""pytest: fixtures, parametrize, and assertions."""

# ── Code to Test ─────────────────────────────────────────────────────
def add(a, b):
    return a + b

def divide(a, b):
    if b == 0:
        raise ValueError("Cannot divide by zero")
    return a / b

class User:
    def __init__(self, name, email):
        self.name = name
        self.email = email
        self.is_active = True

    def deactivate(self):
        self.is_active = False

    def __repr__(self):
        return f"User('{self.name}', '{self.email}')"

# ── Basic Tests (assert, no classes needed) ──────────────────────────
def test_add():
    assert add(2, 3) == 5

def test_add_negative():
    assert add(-1, -1) == -2

def test_divide():
    assert divide(10, 3) == pytest.approx(3.333, rel=1e-3)

def test_divide_by_zero():
    with pytest.raises(ValueError, match="Cannot divide"):
        divide(1, 0)

# ── Fixtures ─────────────────────────────────────────────────────────
import pytest

@pytest.fixture
def sample_user():
    """Create a sample user for tests."""
    return User("Alice", "alice@example.com")

@pytest.fixture
def user_list():
    """Create a list of users."""
    return [
        User("Alice", "alice@example.com"),
        User("Bob", "bob@example.com"),
        User("Charlie", "charlie@example.com"),
    ]

def test_user_name(sample_user):
    assert sample_user.name == "Alice"

def test_user_active(sample_user):
    assert sample_user.is_active is True

def test_user_deactivate(sample_user):
    sample_user.deactivate()
    assert sample_user.is_active is False

def test_user_list_length(user_list):
    assert len(user_list) == 3

# ── Parametrize ──────────────────────────────────────────────────────
@pytest.mark.parametrize("a, b, expected", [
    (1, 2, 3),
    (0, 0, 0),
    (-1, 1, 0),
    (100, 200, 300),
])
def test_add_parametrize(a, b, expected):
    assert add(a, b) == expected

@pytest.mark.parametrize("a, b, expected", [
    (10, 2, 5),
    (9, 3, 3),
    (1, 1, 1),
])
def test_divide_parametrize(a, b, expected):
    assert divide(a, b) == expected

# ── Fixtures with Scope ─────────────────────────────────────────────
@pytest.fixture(scope="module")
def expensive_resource():
    """Shared across all tests in this module."""
    print("Setting up expensive resource")
    data = {"db": "connection", "cache": {}}
    yield data
    print("Tearing down expensive resource")

def test_resource(expensive_resource):
    assert "db" in expensive_resource

# ── Markers ──────────────────────────────────────────────────────────
@pytest.mark.slow
def test_slow_operation():
    import time
    time.sleep(1)
    assert True

@pytest.mark.skip(reason="Not implemented yet")
def test_future_feature():
    pass

@pytest.mark.xfail(reason="Known bug #123")
def test_known_bug():
    assert False

# ── Conftest.py Pattern ─────────────────────────────────────────────
# conftest.py can contain shared fixtures
# They're automatically available to all tests in the directory

# ── Fixture with Temp Directory ─────────────────────────────────────
@pytest.fixture
def temp_file(tmp_path):
    """Create a temporary file."""
    file = tmp_path / "test.txt"
    file.write_text("Hello, World!")
    return file

def test_file_content(temp_file):
    assert temp_file.read_text() == "Hello, World!"

# ── Mocking ──────────────────────────────────────────────────────────
from unittest.mock import patch, MagicMock

def get_data():
    import requests
    return requests.get("https://api.example.com/data").json()

@patch("requests.get")
def test_get_data(mock_get):
    mock_get.return_value.json.return_value = {"result": "ok"}
    result = get_data()
    assert result == {"result": "ok"}

# ── Async Tests ──────────────────────────────────────────────────────
import asyncio

@pytest.mark.asyncio
async def test_async_operation():
    await asyncio.sleep(0.1)
    assert True

# ── Running Tests ────────────────────────────────────────────────────
# pytest                          # Run all tests
# pytest test_pytest.py           # Run specific file
# pytest -k "test_add"            # Run matching tests
# pytest -v                       # Verbose output
# pytest -x                       # Stop on first failure
# pytest --tb=short               # Short traceback
# pytest -m "not slow"            # Skip slow tests
