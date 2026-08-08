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

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Verify code correctness and prevent regressions |
| Complexity | O(n) per test run (n = assertions); fixtures add setup cost |
| Thread Safe | No (shared fixtures need thread-local setup) |
| Best Alternative | unittest for stdlib-only, pytest for rich ecosystem |
| When to Use | Any production code, CI/CD pipelines, refactoring safety net |
| When to Avoid | Throwaway scripts, trivial one-off utilities |
