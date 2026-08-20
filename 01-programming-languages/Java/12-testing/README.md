# Testing

> Comprehensive guide to Java testing — unit testing, integration testing, JUnit 5, Mockito, test design, and best practices.

## Why Testing?

Testing is essential for:
- **Code quality** — catch bugs early
- **Refactoring confidence** — change code safely
- **Documentation** — tests document expected behavior
- **Production reliability** — prevent regressions
- **Team collaboration** — shared understanding of requirements

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | [Testing Fundamentals](01-testing-fundamentals/) | Core testing concepts and principles |
| 02 | [JUnit 5](02-junit5/) | Modern Java testing framework |
| 03 | [JUnit 5 Advanced](03-junit5-advanced/) | Extensions, parameterized tests, lifecycle |
| 04 | [Mockito](04-mockito/) | Mocking framework for unit tests |
| 05 | [Mockito Advanced](05-mockito-advanced/) | Spying, verification, argument matchers |
| 06 | [Integration Testing](06-integration-testing/) | Testing component interactions |
| 07 | [Unit Testing](07-unit-testing/) | Best practices and patterns |

## Testing Pyramid

```
┌─────────────────────────────────────┐
│           Testing Pyramid           │
├─────────────────────────────────────┤
│  ┌─────────────────────────────┐    │
│  │    E2E Tests (Few)          │    │
│  │    Slow, brittle, expensive │    │
│  └─────────────────────────────┘    │
│  ┌─────────────────────────────┐    │
│  │  Integration Tests (Some)   │    │
│  │  Test component interactions│    │
│  └─────────────────────────────┘    │
│  ┌─────────────────────────────┐    │
│  │  Unit Tests (Many)          │    │
│  │  Fast, isolated, cheap      │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

## Test Categories

### Unit Tests
- Test individual methods/classes in isolation
- Fast execution (milliseconds)
- No external dependencies
- Use mocks for dependencies

### Integration Tests
- Test component interactions
- May use real databases, APIs
- Slower execution (seconds)
- Verify contracts between components

### End-to-End Tests
- Test complete user workflows
- Slowest execution (minutes)
- Full system stack
- Fewest in number

## Key Testing Principles

1. **FIRST** — Fast, Independent, Repeatable, Self-validating, Timely
2. **Arrange-Act-Assert** — Structure your tests clearly
3. **Test behavior, not implementation** — Test what, not how
4. **One assertion per test** — Keep tests focused
5. **Test edge cases** — Null, empty, boundary values

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://site.mockito.org/)
- [Testing Pyramid](https://martinfowler.com/articles/practical-test-pyramid.html)
- [Test Driven Development](https://martinfowler.com/articles/tdd.html)
