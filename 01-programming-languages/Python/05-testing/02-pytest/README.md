# pytest

When you need a flexible and feature-rich testing framework, pytest simplifies test writing and supports advanced features. Python's fixtures, parametrize, and extensive plugin ecosystem make pytest a powerful tool for detailed testing.

## Overview

pytest is a mature, feature-rich testing framework. It's simpler than unittest and supports fixtures, parametrize, and extensive plugin ecosystem.

## When to Use

- Any Python testing
- Complex fixture requirements
- Parameterized tests
- Integration with CI/CD

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic tests | `test_pytest.py:16-23` | assert, no classes needed |
| Fixtures | `test_pytest.py:28-46` | @pytest.fixture |
| Parametrize | `test_pytest.py:50-60` | @pytest.mark.parametrize |
| Scoped fixtures | `test_pytest.py:64-70` | scope="module" |
| Markers | `test_pytest.py:74-82` | @slow, @skip, @xfail |
| Temp files | `test_pytest.py:86-93` | tmp_path |
| Mocking | `test_pytest.py:97-103` | @patch |
| Async tests | `test_pytest.py:107-110` | @pytest.mark.asyncio |

## Common Mistakes

1. **Not installing pytest** — `pip install pytest`
2. **Forgetting to return in fixtures** — yield for cleanup
3. **Over-mocking** — test real behavior when possible
4. **Not using conftest.py** — shared fixtures belong there

## Interview Questions

1. What is the difference between unittest and pytest?
2. How do fixtures work?
3. When would you use @pytest.mark.parametrize?
4. What is conftest.py?

## Production Checklist

- [ ] Use `conftest.py` for shared fixtures across test modules
- [ ] Implement fixture cleanup with `yield` (not `return`) for teardown
- [ ] Apply `@pytest.mark.parametrize` for data-driven tests
- [ ] Use markers (`@pytest.mark.slow`, `@pytest.mark.integration`) for test filtering
- [ ] Run tests with `pytest -v --tb=short` for clear output
- [ ] Add `pytest-cov` for coverage reporting in CI
- [ ] Use `monkeypatch` for environment and attribute patching
- [ ] Implement `tmp_path` fixture for temporary file isolation
- [ ] Use `pytest-xdist` for parallel test execution
- [ ] Configure `pytest.ini` or `pyproject.toml` for consistent settings

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Writes simple test functions with `assert`; uses basic fixtures |
| **Intermediate** | Uses `@pytest.fixture` with yield; applies `@pytest.mark.parametrize` and markers |
| **Advanced** | Implements scoped fixtures, `conftest.py` hierarchies, and custom markers |
| **Expert** | Designs plugin architectures, custom fixtures with complex dependencies, and pytest hooks |

## Common Myths

1. **"pytest requires test classes"** — pytest discovers and runs plain test functions
2. **"Fixtures are just setup functions"** — They provide dependency injection, cleanup, and scoped lifecycle
3. **"More tests = better"** — Focus on meaningful assertions; avoid testing implementation details
4. **"Mock everything"** — Over-mocking hides bugs; test real behavior when safe
5. **"conftest.py is optional"** — It's essential for shared fixtures and plugin configuration
6. **"pytest is just unittest with syntax sugar"** — pytest has its own fixture system, plugin architecture, and discovery mechanism

## One-Minute Revision

- **Test functions**: Plain functions with `assert`; no class needed; names start with `test_`
- **Fixtures**: `@pytest.fixture`; `yield` for setup/teardown; inject via function arguments
- **Parametrize**: `@pytest.mark.parametrize("args", [cases])` for data-driven tests
- **Markers**: `@pytest.mark.slow`, `@skip`, `@xfail`; filter with `-m` flag
- **conftest.py**: Shared fixtures; placed in test directories; auto-loaded by pytest
- **monkeypatch**: Replace attributes, environment variables, and imports during tests
- **tmp_path**: Built-in fixture for temporary directories; auto-cleaned after test
- **Plugins**: pytest-cov, pytest-xdist, pytest-mock; extend via `pyproject.toml`
- **Best practice**: Use `yield` fixtures for cleanup; prefer `monkeypatch` over `mock.patch`
- **Discovery**: pytest finds `test_*.py` files; configure paths in `pyproject.toml`

## Production Incidents

### Incident 1: Flaky Test Causing False Positives

**Problem:** A test intermittently passes and fails without code changes, causing CI to report false failures and eroding team confidence in the test suite.

**Cause:** Test depends on external state (network, time, random values, shared databases) or has race conditions with other tests. Order-dependent tests expose hidden coupling.

**Impact:** Developers ignore test failures ("it's just flaky"). Real bugs slip through. CI pipeline becomes unreliable. Release velocity slows as teams manually re-run tests.

**Detection:** Run tests multiple times in CI (`pytest --count=10`). Track test failure rates over time. Use `pytest-randomly` to detect order dependencies.

**Solution:** Make tests deterministic:
```python
# Bad: depends on current time
def test_expiry():
    assert token.expires_at > datetime.now()

# Good: controls time
def test_expiry(monkeypatch):
    fixed_time = datetime(2025, 1, 1)
    monkeypatch.setattr(datetime, "now", lambda: fixed_time)
    assert token.expires_at > fixed_time
```

** Prevention:** Mock external dependencies. Use `pytest-randomly` to randomize test order. Add flaky test detection to CI. Isolate tests with fixtures.

---

### Incident 2: Missing Mock Causing Test Pollution

**Problem:** A test modifies real database records or files, affecting subsequent tests and causing cascading failures across the test suite.

**Cause:** Tests interact with real external services (databases, APIs, filesystem) without proper mocking or isolation. Fixtures don't clean up after themselves.

**Impact:** Tests fail unpredictably based on execution order. Data corruption in test databases. CI takes hours to debug due to cascading failures.

**Detection:** Run tests in isolation to identify polluting tests. Check for database records after test runs. Use `pytest --forked` to detect state leakage.

**Solution:** Use proper mocking and fixtures with cleanup:
```python
@pytest.fixture
def db_session():
    session = create_session()
    yield session
    session.rollback()  # cleanup
    session.close()

@pytest.fixture
def mock_api(monkeypatch):
    monkeypatch.setattr("myapp.api.requests.get", lambda url: mock_response())
```

** Prevention:** Use `conftest.py` for shared fixtures. Always yield in fixtures for cleanup. Use `pytest-forked` for test isolation. Mock external services by default.
