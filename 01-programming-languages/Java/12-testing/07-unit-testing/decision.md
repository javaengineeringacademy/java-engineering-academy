# Decision: Unit Testing Best Practices

## When to Write Unit Tests

**Always write unit tests for:**
- Business logic and domain rules
- Edge cases and boundary conditions
- Error handling paths
- Pure functions and utilities
- Complex algorithms

**Consider skipping for:**
- Simple getters/setters (POJOs)
- Configuration classes
- Third-party wrapper code
- Trivial delegation methods

## Testing Strategy

| Code Type | Testing Approach |
|-----------|-----------------|
| Pure functions | Direct assertion, no mocks |
| Stateful objects | Test state transitions |
| Dependencies | Mock external services |
| Private methods | Test through public API |
| Static methods | Test directly, consider DI for testability |

## Test Design Principles

1. **F.I.R.S.T**: Fast, Independent, Repeatable, Self-validating, Timely
2. **Single assertion per test** (preferably)
3. **Descriptive test names** that explain behavior
4. **Arrange-Act-Assert** pattern
5. **Test behavior, not implementation**

## Code Coverage Guidelines

| Level | Target | Focus |
|-------|--------|-------|
| Line coverage | 70-80% | Core business logic |
| Branch coverage | 60-70% | Conditional paths |
| Mutation coverage | 50-60% | Test quality |
