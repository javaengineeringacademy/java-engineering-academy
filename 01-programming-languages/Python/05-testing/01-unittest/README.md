# unittest

When you need a built-in testing framework for unit and integration tests, unittest provides a structured approach. Python's TestCase, assertions, setup/teardown, and test organization help you write reliable tests without external dependencies.

## Overview

unittest is Python's built-in testing framework, inspired by JUnit. It provides test discovery, assertions, setup/teardown, and test organization.

## When to Use

- Standard unit testing
- Integration testing
- Test-driven development (TDD)
- When you can't install external packages

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic test | `test_basics.py:25-36` | test_add, assertEqual |
| setUp/tearDown | `test_basics.py:40-60` | Per-test setup |
| Assert methods | `test_basics.py:64-82` | assertIn, assertRaises |
| String tests | `test_basics.py:86-99` | Method testing |
| Skipping | `test_basics.py:103-115` | @skip, @skipIf |
| Test suite | `test_basics.py:119-124` | Manual suite creation |

## Common Mistakes

1. **Not inheriting from TestCase** — tests won't be discovered
2. **Test methods not starting with `test_`** — won't be run
3. **Forgetting setUp** — shared state between tests
4. **Not cleaning up** — tests may interfere with each other

## Interview Questions

1. What is the difference between unittest and pytest?
2. How do setUp and tearDown work?
3. When would you skip a test?
4. How do you run specific test methods?
