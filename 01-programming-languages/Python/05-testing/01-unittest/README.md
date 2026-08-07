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

## Production Checklist

- [ ] Run tests with `python -m unittest discover` for automatic test discovery
- [ ] Use `setUp()` and `tearDown()` for per-test resource management
- [ ] Use `setUpClass()` and `tearDownClass()` for expensive shared setup
- [ ] Apply `@skip`, `@skipIf`, `@skipUnless` for environment-specific tests
- [ ] Use `assertRaises` context manager for exception testing
- [ ] Implement `setUpModule()` / `tearDownModule()` for module-level fixtures
- [ ] Avoid test interdependencies; each test should be independent
- [ ] Use `subTest()` for parameterized testing without external packages
- [ ] Run tests in CI/CD pipeline with `python -m unittest -v`
- [ ] Use `mock.patch` for external dependency isolation

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Writes test classes inheriting from `TestCase`; uses `assertEqual`, `assertTrue` |
| **Intermediate** | Implements `setUp`/`tearDown`; uses `assertRaises` and `@skip` decorators |
| **Advanced** | Uses `mock.patch`, `subTest`, `expectedFailure`; manages test suites and loaders |
| **Expert** | Designs test frameworks with custom runners, result formatters, and plugin integration |

## Common Myths

1. **"unittest is outdated; use pytest"** — unittest is built-in, stable, and sufficient for many projects
2. **"Test methods can be private"** — Methods must start with `test_` to be discovered
3. **"setUp runs once per class"** — It runs before every test method; use `setUpClass` for once-per-class
4. **"TestCase can test non-test code directly"** — It's a test framework; use assertions to verify behavior
5. **"unittest can't do parameterized tests"** — `subTest()` provides parameterization without external packages
6. **"tearDown always runs"** — It runs even if setUp fails or tests error out

## One-Minute Revision

- **TestCase**: Base class; test methods start with `test_`; use `self.assert*` methods
- **setUp/tearDown**: Run before/after every test method; yield in setUp for cleanup pattern
- **setUpClass/tearDownClass**: Run once per class; `@classmethod` decorator required
- **assertRaises**: Context manager for exception testing; `with self.assertRaises(Err):`
- **assertEqual, assertTrue, assertFalse, assertIn, assertIsNone**: Core assertion methods
- **@skip, @skipIf, @skipUnless**: Skip tests conditionally with reasons
- **subTest**: Parameterized testing; `with self.subTest(i=i):` for iteration
- **mock.patch**: Replace objects during testing; restores after test completes
- **Test discovery**: `python -m unittest discover` finds all `test_*.py` files
- **Test runner**: `python -m unittest -v` for verbose output; `-k` for test filtering
