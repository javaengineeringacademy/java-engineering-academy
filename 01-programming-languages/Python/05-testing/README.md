# Testing

Write reliable tests with Python's built-in unittest and the popular pytest framework.

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | unittest | TestCase, assertions, setUp/tearDown, test discovery |
| 02 | pytest | Fixtures, parametrize, markers, plugins |

## Prerequisites

- Python Fundamentals (01-fundamentals)
- Object-Oriented Programming (02-oop)

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
