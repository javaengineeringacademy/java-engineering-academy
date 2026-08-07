# pytest

## Why pytest Exists

Every Python developer who writes tests eventually gets frustrated with unittest's verbose syntax, manual test discovery, and ceremony. pytest was created to make testing simple: discover tests automatically, use plain `assert` statements, and provide powerful fixtures for setup/teardown. It turns testing from a chore into a natural part of development.

## What You'll Learn

By the end of this section, you'll be able to:

- Write tests using fixtures for reusable setup and teardown
- Parametrize tests to run the same logic with multiple inputs
- Mock external dependencies using monkeypatch and unittest.mock

## When to Use pytest

| Use Case | Why pytest | Alternative |
|----------|-----------|-------------|
| Unit tests | Auto-discovery, fast execution | unittest |
| Integration tests | Fixtures for DB/API setup | Manual setup |
| Parametrized tests | Test many inputs efficiently | Loops |
| Code coverage | pytest-cov plugin | Manual coverage |
| Mocking | monkeypatch fixture | unittest.mock |
| Parallel tests | pytest-xdist plugin | Sequential |

## How pytest Works Internally

pytest discovers tests by scanning files matching `test_*.py` or `*_test.py`. It collects functions and classes starting with `test_`, then executes them. The key innovation is fixtures — functions decorated with `@pytest.fixture` that provide setup and teardown. pytest injects fixtures into test functions by matching parameter names.

When a test fails, pytest's assertion rewriting kicks in. It rewrites `assert` statements at import time to provide detailed failure messages. Instead of just "False," you see the actual values of both sides of the comparison. This makes debugging test failures much faster.

```python
import pytest

@pytest.fixture
def sample_data():
    return [1, 2, 3, 4, 5]

def test_sum(sample_data):
    assert sum(sample_data) == 15

@pytest.mark.parametrize("input,expected", [
    (2, True),
    (3, False),
    (4, True),
])
def test_is_even(input, expected):
    assert input % 2 == 0 == expected
```

## Production Checklist

### ✅ Before using pytest in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: 100% coverage means no bugs
**Reality:** Coverage measures which lines executed, not whether assertions were meaningful. You can have 100% coverage with no assertions at all.

### ❌ Myth 2: Tests slow down development
**Reality:** Tests speed up development by catching bugs before they reach production. The time spent writing tests is saved many times over in debugging.

### ❌ Myth 3: Unit tests are enough
**Reality:** Unit tests verify individual functions in isolation. Integration tests verify that components work together. You need both.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Testing framework with auto-discovery |
| Complexity | O(n) for test execution |
| Thread Safe | No (each test is isolated) |
| Best Alternative | unittest for built-in testing |
| When to Use | All Python testing needs |
| When to Avoid | Legacy projects using unittest |

## Related Topics

- [04-flask](../04-flask/) - Testing Flask apps
- [05-django](../05-django/) - Django test runner
- [06-sqlalchemy](../06-sqlalchemy/) - Database testing
