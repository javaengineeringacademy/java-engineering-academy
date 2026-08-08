# Testing

## Why Testing Matters

Every production application needs confidence that changes won't break existing functionality. Testing in Python provides systematic ways to verify your code works correctly — from unit tests that validate individual functions to integration tests that ensure components work together. Without testing, you'd deploy code with unknown bugs and live in fear of every release.

Without tests, you'd have to manually verify every feature after every change, which is impossible at scale. That's why testing exists — it provides automated regression detection, documentation of expected behavior, and the confidence to refactor and extend code without breaking things.

## What You'll Learn

By the end of this module, you'll be able to:

- Write unit tests using unittest's TestCase and assertions
- Use pytest fixtures and parametrize for concise test suites
- Organize and run tests with test discovery
- Mock external dependencies to isolate units under test
- Apply test-driven development (TDD) workflows

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Any production code, CI/CD pipelines, refactoring safety net | Throwaway scripts, trivial utilities |
| When NOT to use | Don't test implementation details; don't over-mock | Test behavior, not implementation |
| Alternatives | unittest for stdlib-only, pytest for rich ecosystem | Manual testing for prototypes |
| Production Examples | Web services, data pipelines, libraries | Quick scripts, prototypes |
| Common Mistakes | Flaky tests, over-mocking, testing implementation | Write stable, isolated tests |

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | unittest | TestCase, assertions, setUp/tearDown, test discovery |
| 02 | pytest | Fixtures, parametrize, markers, plugins |

## Prerequisites

- Python Fundamentals (01-fundamentals)
- Object-Oriented Programming (02-oop)

## Interview Questions

### Q1: What is the difference between unit, integration, and end-to-end tests?
**Answer:** Unit tests test individual functions. Integration tests test component interactions. End-to-end tests test complete workflows. Unit tests are fastest and most numerous.

### Q2: What is test-driven development (TDD)?
**Answer:** Write tests before code. Red (fail) → Green (pass) → Refactor. TDD ensures test coverage and forces clear interfaces.

### Q3: What is mocking and when should you use it?
**Answer:** Mocking replaces real objects with fake ones. Use for external services (APIs, databases), slow operations, or non-deterministic behavior.

### Q4: What is the difference between `unittest` and `pytest`?
**Answer:** unittest is Python's built-in framework (class-based, verbose). pytest is third-party (simpler syntax, fixtures, parametrize). pytest is more popular.

### Q5: What is a fixture in pytest?
**Answer:** A fixture is a function that provides test setup/teardown. Decorated with @pytest.fixture, can be injected into tests by name.

## Learning Objectives

By the end of this module you will be able to:

- Write unit tests using unittest's TestCase and assertions
- Use pytest fixtures and parametrize for concise test suites
- Organize and run tests with test discovery
- Mock external dependencies to isolate units under test
- Apply test-driven development (TDD) workflows

## Quick Start

```bash
# Run any topic directly
python 01-unittest/test_basics.py
python 02-pytest/test_basics.py

# Or run with pytest
pytest 02-pytest/
```

## Production Incidents

### Incident 1: Flaky Test Causing False Alarms

**Problem:** CI/CD pipeline failed randomly on test_payment_processing
**Cause:** Test depended on system time; timezone differences caused failures
**Impact:** 30% of deployments delayed; team lost confidence in test suite
**Detection:** Random test failures in CI; no pattern in failures
**Solution:**
```python
# BAD: depends on real time
def test_payment_timeout():
    start = time.time()
    process_payment(timeout=5)
    assert time.time() - start < 5

# GOOD: mock time
def test_payment_timeout(mocker):
    mock_time = mocker.patch('time.time')
    mock_time.side_effect = [0, 6]  # Simulate 6 seconds elapsed
    with pytest.raises(TimeoutError):
        process_payment(timeout=5)
```
**Prevention:** Mock external dependencies (time, network, DB); use `pytest-mock`; isolate tests

### Incident 2: Mocking Too Much Causing False Positives

**Problem:** Tests passed but integration tests failed
**Cause:** Mocks replaced real implementations; edge cases not caught
**Impact:** Bug reached production; hotfix required
**Detection:** Integration test failures after deployment
**Solution:**
```python
# BAD: Mock everything
def test_user_creation():
    with mock.patch('database.save'):
        with mock.patch('email.send'):
            user = create_user(data)
            assert user is not None  # Always passes!

# GOOD: Test real behavior
def test_user_creation(db_session):
    user = create_user(data)
    assert db_session.query(User).count() == 1
```
**Prevention:** Mock only external boundaries (APIs, filesystem); test real behavior when possible; use integration tests

### Incident 3: Test Pollution Between Tests

**Problem:** Tests passed individually but failed in suite
**Cause:** Test modified global state (module-level variable) affecting other tests
**Impact:** CI failures only when running full suite; slow debugging
**Detection:** Test passed with `--lf` but failed in full run
**Solution:**
```python
# BAD: modifies global state
def test_config():
    global_config.debug = True
    assert process() == expected

# GOOD: use fixture to isolate
@pytest.fixture
def test_config():
    original = config.debug
    config.debug = True
    yield config
    config.debug = original
```
**Prevention:** Use fixtures for setup/teardown; avoid global state; run tests in isolation

## Production Checklist

### ✅ Before using testing practices in production:

☐ I know the time/space complexity of test suites and fixture setup
☐ I know common mistakes (flaky tests, over-mocking, testing implementation details)
☐ I know alternatives (unittest vs pytest, integration vs unit vs contract tests)
☐ I know limitations (tests can't prove absence of bugs, mock fidelity issues)
☐ I know how to debug it (pytest -x, pdb for failing tests, --lf flag)
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

### ❌ Myth 1: 100% code coverage means the code is bug-free
**Reality:** Coverage measures executed lines, not correctness. Edge cases, concurrency issues, and integration bugs can exist at 100% coverage.

### ❌ Myth 2: Unit tests are always better than integration tests
**Reality:** Unit tests are fast and isolated but miss real-world interaction bugs. A balanced testing pyramid includes both.

### ❌ Myth 3: Tests slow down development
**Reality:** Well-written tests catch regressions early, reduce debugging time, and enable confident refactoring—saving time long-term.

## Related Topics

- [09-exception-handling](../09-exception-handling/) - Testing exception paths
- [16-best-practices](../16-best-practices/) - Testing best practices
- [18-senior](../18-senior/) - Production testing strategies

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Verify code correctness and prevent regressions |
| Complexity | O(n) per test run (n = assertions); fixtures add setup cost |
| Thread Safe | No (shared fixtures need thread-local setup) |
| Best Alternative | unittest for stdlib-only, pytest for rich ecosystem |
| When to Use | Any production code, CI/CD pipelines, refactoring safety net |
| When to Avoid | Throwaway scripts, trivial one-off utilities |
