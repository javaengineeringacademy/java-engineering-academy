"""
Module 05 - Testing: Pytest Solutions
Difficulty: Intermediate
"""

import pytest

# =============================================================================
# Exercise 1: Basic Pytest Tests - Solution
# =============================================================================
class MathUtils:
    """Mathematical utility functions."""

    @staticmethod
    def factorial(n):
        if n < 0:
            raise ValueError("Factorial not defined for negative numbers")
        if n <= 1:
            return 1
        return n * MathUtils.factorial(n - 1)

    @staticmethod
    def is_prime(n):
        if n < 2:
            return False
        for i in range(2, int(n ** 0.5) + 1):
            if n % i == 0:
                return False
        return True

    @staticmethod
    def fibonacci(n):
        if n <= 0:
            return []
        if n == 1:
            return [0]
        fib = [0, 1]
        for i in range(2, n):
            fib.append(fib[i-1] + fib[i-2])
        return fib

def test_factorial():
    """Test factorial function."""
    assert MathUtils.factorial(0) == 1
    assert MathUtils.factorial(1) == 1
    assert MathUtils.factorial(5) == 120
    assert MathUtils.factorial(10) == 3628800

def test_is_prime():
    """Test is_prime function."""
    assert MathUtils.is_prime(2) is True
    assert MathUtils.is_prime(7) is True
    assert MathUtils.is_prime(4) is False
    assert MathUtils.is_prime(1) is False

def test_fibonacci():
    """Test fibonacci function."""
    assert MathUtils.fibonacci(0) == []
    assert MathUtils.fibonacci(1) == [0]
    assert MathUtils.fibonacci(5) == [0, 1, 1, 2, 3]
    assert MathUtils.fibonacci(8) == [0, 1, 1, 2, 3, 5, 8, 13]


# =============================================================================
# Exercise 2: Test Fixtures - Solution
# =============================================================================
class User:
    """User class for testing."""

    def __init__(self, name, email, active=True):
        self.name = name
        self.email = email
        self.active = active

    def deactivate(self):
        self.active = False

    def activate(self):
        self.active = True

@pytest.fixture
def sample_user():
    """Create a sample user fixture."""
    return User("Alice", "alice@example.com")

@pytest.fixture
def inactive_user():
    """Create an inactive user fixture."""
    user = User("Bob", "bob@example.com")
    user.deactivate()
    return user

def test_user_creation(sample_user):
    """Test user creation."""
    assert sample_user.name == "Alice"
    assert sample_user.email == "alice@example.com"
    assert sample_user.active is True

def test_user_deactivation(sample_user):
    """Test user deactivation."""
    sample_user.deactivate()
    assert sample_user.active is False

def test_user_activation(inactive_user):
    """Test user activation."""
    inactive_user.activate()
    assert inactive_user.active is True


# =============================================================================
# Exercise 3: Parametrize Tests - Solution
# =============================================================================
class Calculator:
    """Calculator for testing."""

    def add(self, a, b):
        return a + b

    def multiply(self, a, b):
        return a * b

@pytest.mark.parametrize("a, b, expected", [
    (1, 2, 3),
    (-1, 1, 0),
    (0, 0, 0),
    (100, 200, 300)
])
def test_add_parametrized(a, b, expected):
    """Test addition with multiple inputs."""
    calc = Calculator()
    assert calc.add(a, b) == expected

@pytest.mark.parametrize("a, b, expected", [
    (2, 3, 6),
    (-1, 1, -1),
    (0, 5, 0),
    (10, 10, 100)
])
def test_multiply_parametrized(a, b, expected):
    """Test multiplication with multiple inputs."""
    calc = Calculator()
    assert calc.multiply(a, b) == expected


# =============================================================================
# Exercise 4: Test Markers - Solution
# =============================================================================
class API:
    """API client for testing."""

    def get(self, url):
        return {"status": 200, "data": "response"}

    def post(self, url, data):
        return {"status": 201, "data": data}

@pytest.mark.slow
def test_slow_operation():
    """Test slow operation."""
    import time
    time.sleep(0.1)
    assert True

@pytest.mark.network
def test_api_get():
    """Test API GET request."""
    api = API()
    result = api.get("http://example.com")
    assert result["status"] == 200

@pytest.mark.network
def test_api_post():
    """Test API POST request."""
    api = API()
    result = api.post("http://example.com", {"key": "value"})
    assert result["status"] == 201

@pytest.mark.skip(reason="Not implemented yet")
def test_future_feature():
    """Test future feature."""
    pass

@pytest.mark.xfail(reason="Known bug")
def test_known_failure():
    """Test expected failure."""
    assert False


# =============================================================================
# Exercise 5: Conftest and Fixtures - Solution
# =============================================================================
class Database:
    """Database connection."""

    def __init__(self, connection_string):
        self.connection_string = connection_string
        self.connected = False

    def connect(self):
        self.connected = True
        return self

    def disconnect(self):
        self.connected = False

    def query(self, sql):
        return f"Result of: {sql}"

# conftest.py would contain:
@pytest.fixture(scope="session")
def db_connection():
    """Session-scoped database connection."""
    db = Database("sqlite:///:memory:")
    db.connect()
    yield db
    db.disconnect()

@pytest.fixture
def db_session(db_connection):
    """Function-scoped database session."""
    yield db_connection

def test_database_connection(db_connection):
    """Test database connection."""
    assert db_connection.connected is True

def test_database_query(db_session):
    """Test database query."""
    result = db_session.query("SELECT * FROM users")
    assert "Result of:" in result
