# pytest

When you need a flexible and feature-rich testing framework, pytest simplifies test writing and supports advanced features. Python's fixtures, parametrize, and extensive plugin ecosystem make pytest a powerful tool for comprehensive testing.

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
