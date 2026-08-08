"""
Module 05 - Testing: Pytest Exercises
Difficulty: Intermediate
"""

# =============================================================================
# Exercise 1: Basic Pytest Tests (Difficulty: Beginner)
# =============================================================================
# Write basic pytest test functions.

# TODO: Implement the MathUtils class
class MathUtils:
    """Mathematical utility functions."""

    @staticmethod
    def factorial(n):
        pass

    @staticmethod
    def is_prime(n):
        pass

    @staticmethod
    def fibonacci(n):
        pass

# TODO: Write pytest test functions
def test_factorial():
    """Test factorial function."""
    pass

def test_is_prime():
    """Test is_prime function."""
    pass

def test_fibonacci():
    """Test fibonacci function."""
    pass


# =============================================================================
# Exercise 2: Test Fixtures (Difficulty: Beginner)
# =============================================================================
# Use pytest fixtures for test setup.

import pytest

# TODO: Implement the User class
class User:
    """User class for testing."""

    def __init__(self, name, email, active=True):
        pass

    def deactivate(self):
        pass

    def activate(self):
        pass

# TODO: Write tests with fixtures
@pytest.fixture
def sample_user():
    """Create a sample user fixture."""
    pass

@pytest.fixture
def inactive_user():
    """Create an inactive user fixture."""
    pass

def test_user_creation(sample_user):
    """Test user creation."""
    pass

def test_user_deactivation(sample_user):
    """Test user deactivation."""
    pass

def test_user_activation(inactive_user):
    """Test user activation."""
    pass


# =============================================================================
# Exercise 3: Parametrize Tests (Difficulty: Intermediate)
# =============================================================================
# Use pytest.mark.parametrize for multiple test cases.

# TODO: Implement the Calculator class
class Calculator:
    """Calculator for testing."""

    def add(self, a, b):
        pass

    def multiply(self, a, b):
        pass

# TODO: Write parametrized tests
@pytest.mark.parametrize("a, b, expected", [
    (1, 2, 3),
    (-1, 1, 0),
    (0, 0, 0),
    (100, 200, 300)
])
def test_add_parametrized(a, b, expected):
    """Test addition with multiple inputs."""
    pass

@pytest.mark.parametrize("a, b, expected", [
    (2, 3, 6),
    (-1, 1, -1),
    (0, 5, 0),
    (10, 10, 100)
])
def test_multiply_parametrized(a, b, expected):
    """Test multiplication with multiple inputs."""
    pass


# =============================================================================
# Exercise 4: Test Markers (Difficulty: Intermediate)
# =============================================================================
# Use pytest markers to organize tests.

# TODO: Implement the API class
class API:
    """API client for testing."""

    def get(self, url):
        pass

    def post(self, url, data):
        pass

# TODO: Write tests with markers
@pytest.mark.slow
def test_slow_operation():
    """Test slow operation."""
    pass

@pytest.mark.network
def test_api_get():
    """Test API GET request."""
    pass

@pytest.mark.network
def test_api_post():
    """Test API POST request."""
    pass

@pytest.mark.skip(reason="Not implemented yet")
def test_future_feature():
    """Test future feature."""
    pass

@pytest.mark.xfail(reason="Known bug")
def test_known_failure():
    """Test expected failure."""
    pass


# =============================================================================
# Exercise 5: Conftest and Fixtures (Difficulty: Advanced)
# =============================================================================
# Create conftest.py with shared fixtures.

# TODO: Implement database classes
class Database:
    """Database connection."""

    def __init__(self, connection_string):
        pass

    def connect(self):
        pass

    def disconnect(self):
        pass

    def query(self, sql):
        pass

# conftest.py would contain:
# @pytest.fixture(scope="session")
# def db_connection():
#     ...
#
# @pytest.fixture
# def db_session(db_connection):
#     ...

# TODO: Write tests using conftest fixtures
def test_database_connection(db_connection):
    """Test database connection."""
    pass

def test_database_query(db_session):
    """Test database query."""
    pass
