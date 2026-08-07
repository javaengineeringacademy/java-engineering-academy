# Python Testing Quiz

## Question 1 (MCQ - unittest vs pytest)
What is a key advantage of `pytest` over `unittest`?

- A) `pytest` uses `assert` statements instead of `self.assertEqual` methods, making tests cleaner
- B) `pytest` is part of the Python standard library; `unittest` is not
- C) `unittest` supports fixtures; `pytest` does not
- D) `pytest` cannot run tests from `unittest.TestCase` classes

**Answer: A**
**Explanation:** `pytest` uses plain `assert` statements (`assert result == expected`) and provides detailed failure messages automatically. `unittest` requires method calls like `self.assertEqual(a, b)`. `pytest` can also run `unittest.TestCase` tests, and is more feature-rich with fixtures, markers, and plugins.

---

## Question 2 (MCQ - Test Isolation)
Why should tests be independent of each other?

- A) Independent tests are faster to run
- B) If tests share state, one test's failure can cause cascading failures in subsequent tests
- C) Python requires tests to be independent
- D) Independent tests use less memory

**Answer: B**
**Explanation:** Shared state (global variables, file system changes, database rows) means one test can modify data that another test depends on. This creates order-dependent failures that are hard to debug. Each test should set up its own state and clean up after itself.

---

## Question 3 (Code Output - Mocking)
What is the output of this test?

```python
from unittest.mock import patch, MagicMock

class OrderService:
    def process(self, item):
        return {"status": "ok", "item": item}

def test_process():
    with patch('__main__.OrderService') as MockClass:
        mock_instance = MockClass.return_value
        mock_instance.process.return_value = {"status": "ok", "item": "test"}
        service = OrderService()
        result = service.process("test")
        print(result)

test_process()
```

A) `{"status": "ok", "item": "test"}`
B) `{'status': 'ok', 'item': 'test'}`
C) The test fails because `OrderService` is patched
D) Error: cannot patch `__main__`

**Answer: B**
**Explanation:** When `OrderService` is patched, `OrderService()` returns `mock_instance` (via `.return_value`). `mock_instance.process("test")` returns the configured value. The printed output is the mock's return value, not the real method's result.

---

## Question 4 (MCQ - Fixtures)
What is the purpose of `@pytest.fixture`?

- A) To run a function before every test in the module
- B) To set up test dependencies (data, connections, etc.) that are automatically injected into tests
- C) To mark a test as expected to fail
- D) To parametrize test inputs

**Answer: B**
**Explanation:** Fixtures provide setup and teardown logic. They can yield resources (like database connections) that are injected into tests by name. Fixtures support scoping (function, class, module, session) and can depend on other fixtures, creating a powerful dependency graph.

---

## Question 5 (Code Output - Parametrize)
What is the output of this test?

```python
import pytest

@pytest.mark.parametrize("input,expected", [
    (1, 2),
    (2, 4),
    (3, 6),
])
def test_double(input, expected):
    result = input * 2
    print(f"{input} * 2 = {result}")
    assert result == expected
```

A) One test that runs 3 times
B) Three separate tests: `test_double[1-2]`, `test_double[2-4]`, `test_double[3-6]`
C) One test that fails on the third case
D) Error: parametrize cannot be used with assert

**Answer: B**
**Explanation:** `@pytest.mark.parametrize` creates multiple test cases from input data. Each tuple becomes a separate test run with its own name (e.g., `test_double[1-2]`). The output shows 3 separate test executions: `1 * 2 = 2`, `2 * 2 = 4`, `3 * 2 = 6`.

---

## Question 6 (MCQ - Test Coverage)
What does 100% test coverage guarantee?

- A) The code has no bugs
- B) Every line of code has been executed at least once during testing
- C) All edge cases are tested
- D) The application is production-ready

**Answer: B**
**Explanation:** Coverage measures code execution, not correctness. 100% coverage means every line ran, but it doesn't verify that assertions are meaningful, edge cases are tested, or that the code does the right thing. High coverage is a useful metric but not a guarantee of quality.

---

## Question 7 (Scenario - Integration vs Unit Tests)
A team has a service that calls an external API. Which testing approach is correct?

- A) Only unit tests with mocks for everything
- B) Only integration tests against the real API
- C) Unit tests with mocked API responses + integration tests to verify actual connectivity
- D) Skip testing external dependencies

**Answer: C**
**Explanation:** Unit tests with mocks verify business logic in isolation (fast, reliable). Integration tests against the real API verify the actual integration works (slow, but catches contract changes). Both are needed: unit tests for speed and coverage, integration tests for real-world validation.

---

## Question 8 (Bug Finding - Assertion Messages)
This test fails but the output is unhelpful. Find the issue:

```python
def test_user_data():
    user = get_user(1)
    assert user["name"] == "Alice"
    assert user["email"] == "alice@example.com"
    assert user["age"] == 30
```

When run, it shows: `AssertionError`
Which improvement makes debugging easier?

- A) Add more test functions
- B) Add descriptive messages: `assert user["age"] == 30, f"Expected 30, got {user['age']}"`
- C) Use `print()` instead of `assert`
- D) Remove all assertions

**Answer: B**
**Explanation:** Bare assertions give minimal info. Adding custom messages (or using `pytest`'s assertion rewriting) shows the actual vs expected values. `pytest` automatically shows detailed diffs for common comparisons, but custom messages add context that auto-rewriting can't infer.

---

## Question 9 (MCQ - Test Discovery)
How does `pytest` discover test files by default?

- A) Any Python file in the project
- B) Files matching `test_*.py` or `*_test.py` patterns
- C) Files named exactly `tests.py`
- D) Files containing the word "test" in any location

**Answer: B**
**Explanation:** `pytest` uses default discovery rules: files named `test_*.py` or `*_test.py`, containing functions/methods named `test_*` or `Test*` classes with `test_*` methods. This convention eliminates configuration for most projects. Custom patterns can be set in `pytest.ini`.

---

## Question 10 (Architecture Decision - Code Under Test)
A function has complex business logic with database queries, file I/O, and email sending. How should you structure tests?

- A) Test everything in one integration test
- B) Refactor into separate functions: pure logic, DB access, file ops, email — then unit test the logic with mocks for the rest
- C) Don't test code with side effects
- D) Only test the email sending

**Answer: B**
**Explanation:** Separate concerns: extract pure business logic (testable without mocks), DB queries (test with test database or mocks), file I/O (mock or use temp files), email (mock SMTP). This makes tests faster, more isolated, and easier to maintain. Integration tests verify the pieces work together.
