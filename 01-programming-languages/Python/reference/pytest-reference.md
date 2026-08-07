# Python pytest Reference

## What is pytest?

pytest is a mature full-featured Python testing tool that helps you write better programs. It's more powerful and easier to use than unittest.

## Why does pytest matter?

Understanding pytest helps you:
- Write tests more quickly
- Use powerful fixtures and markers
- Run tests efficiently
- Get detailed failure reports

---

## 1. Basic Tests

```python
# test_example.py
def test_addition():
    assert 1 + 1 == 2

def test_string():
    assert "hello".upper() == "HELLO"
```

---

## 2. Fixtures

```python
import pytest

@pytest.fixture
def sample_data():
    return [1, 2, 3, 4, 5]

def test_sum(sample_data):
    assert sum(sample_data) == 15

# Fixture with yield (setup/teardown)
@pytest.fixture
def db_connection():
    conn = create_connection()
    yield conn
    conn.close()

def test_query(db_connection):
    result = db_connection.execute("SELECT * FROM users")
    assert result is not None
```

---

## 3. Markers

```python
import pytest

@pytest.mark.slow
def test_slow_function():
    # slow test
    pass

@pytest.mark.skip(reason="Not implemented")
def test_not_implemented():
    pass

@pytest.mark.skipif(sys.platform == "win32", reason="Windows only")
def test_windows_only():
    pass

@pytest.mark.parametrize("input,expected", [
    (1, 2),
    (2, 4),
    (3, 6),
])
def test_double(input, expected):
    assert input * 2 == expected
```

---

## 4. Assertions

```python
import pytest

def test_assertions():
    # Basic assertion
    assert 1 + 1 == 2
    
    # Exception
    with pytest.raises(ZeroDivisionError):
        1 / 0
    
    # Approximate
    assert 0.1 + 0.2 == pytest.approx(0.3)
    
    # Regex match
    with pytest.raises(ValueError, match="invalid value"):
        raise ValueError("invalid value: 42")
```

---

## 5. Parametrize

```python
import pytest

@pytest.mark.parametrize("input,expected", [
    (1, 2),
    (2, 4),
    (3, 6),
    (4, 8),
])
def test_double(input, expected):
    assert input * 2 == expected

# Multiple parameters
@pytest.mark.parametrize("x", [1, 2, 3])
@pytest.mark.parametrize("y", [4, 5, 6])
def test_multiply(x, y):
    assert x * y > 0
```

---

## 6. Fixtures Scope

```python
import pytest

@pytest.fixture(scope="function")
def function_fixture():
    # Runs for each test function
    pass

@pytest.fixture(scope="class")
def class_fixture():
    # Runs for each test class
    pass

@pytest.fixture(scope="module")
def module_fixture():
    # Runs for each test module
    pass

@pytest.fixture(scope="session")
def session_fixture():
    # Runs once for entire test session
    pass
```

---

## 7. Plugins

```python
# pytest.ini or pyproject.toml
[tool.pytest.ini_options]
testpaths = ["tests"]
python_files = ["test_*.py"]
python_classes = ["Test*"]
python_functions = ["test_*"]

# Common plugins
# pytest-cov: Coverage reporting
# pytest-xdist: Parallel testing
# pytest-mock: Mocking support
# pytest-html: HTML reports
```

---

## One-Minute Revision Table

| Feature | Description | Example |
|---------|-------------|---------|
| **test_** | Test file prefix | `test_example.py` |
| **test_** | Test function prefix | `def test_addition():` |
| **assert** | Assertion | `assert 1 + 1 == 2` |
| **@pytest.fixture** | Test fixture | `@pytest.fixture def data():` |
| **@pytest.mark** | Test marker | `@pytest.mark.slow` |
| **@pytest.mark.parametrize** | Parameterize tests | `@pytest.mark.parametrize("input,expected", [...])` |
| **pytest.raises** | Exception testing | `with pytest.raises(Exception):` |
| **pytest.approx** | Approximate comparison | `assert 0.1 + 0.2 == pytest.approx(0.3)` |

---

## Common Mistakes

### 1. Not Using Fixtures

```python
# WRONG
def test_database():
    conn = create_connection()  # Duplicated
    # test

# RIGHT
@pytest.fixture
def db_connection():
    return create_connection()

def test_database(db_connection):
    # test
```

### 2. Not Using Parametrize

```python
# WRONG
def test_addition_1():
    assert 1 + 1 == 2

def test_addition_2():
    assert 2 + 2 == 4

# RIGHT
@pytest.mark.parametrize("input,expected", [(1, 2), (2, 4)])
def test_addition(input, expected):
    assert input + input == expected
```

### 3. Not Using pytest.raises

```python
# WRONG
def test_exception():
    try:
        1 / 0
    except ZeroDivisionError:
        pass

# RIGHT
def test_exception():
    with pytest.raises(ZeroDivisionError):
        1 / 0
```

---

## Production Notes

1. **Use fixtures for setup/teardown** - More powerful than setUp/tearDown
2. **Use parametrize for multiple inputs** - Reduce test duplication
3. **Use markers for test organization** - Skip, slow, etc.
4. **Use plugins for extra functionality** - Coverage, parallel, etc.
5. **Use pytest.approx for floating point** - Avoid precision issues
6. **Use pytest.raises for exceptions** - More readable
7. **Keep tests fast** - Parallel testing with xdist
8. **Use conftest.py for shared fixtures** - Across modules
9. **Use test discovery** - Don't manually list tests
10. **Use pytest.ini for configuration** - Consistent settings

---

## Further Reading

- pytest documentation
- pytest fixtures documentation
- pytest markers documentation
- pytest plugins documentation
