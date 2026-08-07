# pytest — Testing Made Simple

> **If it's not tested, it's broken. pytest makes testing painless.**

## What

pytest is the most popular Python testing framework. It discovers tests automatically, provides powerful fixtures for setup/teardown, supports parameterized testing, and has a rich plugin ecosystem. It makes writing tests concise and readable with assert-based syntax.

## Why

- **Auto-discovery:** Finds tests in files named `test_*.py` automatically.
- **Fixtures:** Reusable setup/teardown with dependency injection.
- **Parametrize:** Run the same test with multiple inputs, no loops needed.
- **Readable assertions:** Plain `assert` with informative failure messages.
- **Plugins:** 1000+ plugins for coverage, mocking, parallel execution, and more.

## When

| Scenario | pytest Approach | Why |
|----------|----------------|-----|
| Unit tests | `test_*.py` files | Auto-discovery, fast execution |
| Integration tests | Fixtures for DB/API setup | Clean setup/teardown |
| Parametrized tests | `@pytest.mark.parametrize` | Test many inputs efficiently |
| Code coverage | `pytest-cov` plugin | Measure test coverage |
| Mocking | `monkeypatch` fixture | Replace dependencies easily |
| Parallel tests | `pytest-xdist` plugin | Run tests across CPUs |
| CI/CD | `pytest --junitxml` | Generate test reports |

## How

### Basic Test Structure

```python
# test_math.py
import pytest

def add(a, b):
    return a + b

# Simple test function
def test_add_positive():
    assert add(2, 3) == 5

def test_add_negative():
    assert add(-1, -1) == -2

def test_add_zero():
    assert add(0, 5) == 5

# Run: pytest test_math.py -v
```

### Fixtures

```python
# conftest.py (shared fixtures)
import pytest

@pytest.fixture
def sample_user():
    """Provide a sample user for tests."""
    return {
        'name': 'Alice',
        'email': 'alice@example.com',
        'age': 30
    }

@pytest.fixture
def db_connection():
    """Setup and teardown database connection."""
    conn = create_connection()
    yield conn
    conn.close()

@pytest.fixture(autouse=True)
def setup_logging():
    """Run before every test automatically."""
    logging.basicConfig(level=logging.DEBUG)
    yield
    logging.shutdown()

# test_user.py
def test_user_name(sample_user):
    assert sample_user['name'] == 'Alice'

def test_user_email(sample_user):
    assert '@' in sample_user['email']
```

### Parametrize

```python
import pytest

def is_even(n):
    return n % 2 == 0

@pytest.mark.parametrize("input,expected", [
    (2, True),
    (3, False),
    (4, True),
    (5, False),
    (0, True),
    (-2, True),
])
def test_is_even(input, expected):
    assert is_even(input) == expected

# Multiple parameters
@pytest.mark.parametrize("a,b,expected", [
    (1, 2, 3),
    (5, 5, 10),
    (-1, 1, 0),
])
def test_add(a, b, expected):
    assert a + b == expected
```

### Exception Testing

```python
import pytest

def divide(a, b):
    if b == 0:
        raise ValueError("Cannot divide by zero")
    return a / b

def test_divide_by_zero():
    with pytest.raises(ValueError, match="Cannot divide by zero"):
        divide(10, 0)

def test_divide_normal():
    assert divide(10, 2) == 5.0
```

### Markers

```python
import pytest

@pytest.mark.slow
def test_heavy_computation():
    result = expensive_function()
    assert result is not None

@pytest.mark.skip(reason="Not implemented yet")
def test_future_feature():
    pass

@pytest.mark.skipif(
    sys.platform == "win32",
    reason="Not supported on Windows"
)
def test_unix_only():
    pass

# Run: pytest -m "not slow"
```

### Monkeypatching

```python
import pytest

def get_config():
    import os
    return os.environ.get('APP_ENV', 'development')

def test_production_config(monkeypatch):
    monkeypatch.setenv('APP_ENV', 'production')
    assert get_config() == 'production'

def test_default_config(monkeypatch):
    monkeypatch.delenv('APP_ENV', raising=False)
    assert get_config() == 'development'
```

### Mocking

```python
from unittest.mock import patch, MagicMock
import pytest

def fetch_user_data(user_id):
    # External API call
    response = requests.get(f'https://api.example.com/users/{user_id}')
    return response.json()

@patch('mymodule.requests.get')
def test_fetch_user(mock_get):
    mock_get.return_value = MagicMock(
        status_code=200,
        json=lambda: {'name': 'Alice'}
    )
    result = fetch_user_data(1)
    assert result['name'] == 'Alice'
    mock_get.assert_called_once()
```

### Fixture Scope and Factories

```python
import pytest

@pytest.fixture(scope="session")
def db_engine():
    """Create database engine once per test session."""
    engine = create_engine('sqlite:///:memory:')
    yield engine
    engine.dispose()

@pytest.fixture(scope="function")
def db_session(db_engine):
    """New session per test, rolled back after."""
    Session = sessionmaker(bind=db_engine)
    session = Session()
    yield session
    session.rollback()
    session.close()

@pytest.fixture
def user_factory(db_session):
    """Factory fixture for creating test users."""
    def _create_user(name='Test User', email='test@example.com'):
        user = User(name=name, email=email)
        db_session.add(user)
        db_session.commit()
        return user
    return _create_user

def test_create_user(user_factory):
    user = user_factory(name='Alice')
    assert user.name == 'Alice'
```

### Configuration (pytest.ini / pyproject.toml)

```ini
# pytest.ini
[pytest]
testpaths = tests
python_files = test_*.py
python_functions = test_*
addopts = -v --tb=short
markers =
    slow: marks tests as slow
    integration: marks integration tests
```

## Production Checklist

- [ ] **Run tests in CI/CD** — every commit should trigger tests
- [ ] **Measure coverage** — `pytest --cov=src --cov-report=html`
- [ ] **Use fixtures for setup/teardown** — never put setup in test functions
- [ ] **Parametrize repetitive tests** — one function, many inputs
- [ ] **Mock external services** — don't hit real APIs in tests
- [ ] **Name tests descriptively** — `test_user_email_required` not `test_user`
- [ ] **Keep tests fast** — slow tests discourage running them
- [ ] **Test edge cases** — empty inputs, None, large data, errors

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **print Testing** | `print()` to check output. No assertions. |
| 2 | **Basic Tests** | `assert` statements. Manual test discovery. |
| 3 | **Structured** | Fixtures, parametrize, markers. Auto-discovery. |
| 4 | **Comprehensive** | Coverage reporting, mocking, CI integration. |
| 5 | **Expert** | Custom fixtures, plugins, property-based testing, mutation testing. |

## Common Myths

### Myth 1: "100% coverage means no bugs"
**Reality:** Coverage measures which lines executed, not whether assertions were meaningful. You can have 100% coverage with no assertions at all. Focus on meaningful tests, not coverage percentages.

### Myth 2: "Tests slow down development"
**Reality:** Tests speed up development by catching bugs before they reach production. The time spent writing tests is saved many times over in debugging. Well-written tests enable confident refactoring.

### Myth 3: "Unit tests are enough"
**Reality:** Unit tests verify individual functions in isolation. Integration tests verify that components work together. You need both. Many bugs only appear at integration boundaries.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Test file | `test_*.py` | Auto-discovered by pytest |
| Test function | `def test_something():` | Individual test case |
| Fixture | `@pytest.fixture` | Setup/teardown with DI |
| Parametrize | `@pytest.mark.parametrize` | Multiple test inputs |
| Exception | `pytest.raises(Error)` | Test for expected exceptions |
| Skip | `@pytest.mark.skip` | Skip test with reason |
| Mock | `@patch('module.func')` | Replace dependencies |
| Monkeypatch | `monkeypatch.setenv()` | Modify environment |
| Mark | `@pytest.mark.slow` | Categorize tests |
| Coverage | `pytest --cov=src` | Measure code coverage |

## Related Topics

- [05-testing](../05-testing/) - Testing fundamentals
- [22-libraries-flask](../22-libraries-flask/) - Testing Flask apps with pytest
- [23-libraries-django](../23-libraries-django/) - Django test runner integration
- [16-best-practices](../16-best-practices/) - Testing best practices

---

> **Remember:** Write tests that would fail if the code breaks. If a test never fails, it's not testing anything useful.
