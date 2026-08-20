# Decision: Testing

## When to Use Different Test Types

**Use Unit Tests when:**
- Testing individual methods or classes
- Testing business logic
- Testing edge cases and error handling
- You need fast feedback

**Use Integration Tests when:**
- Testing database queries
- Testing API endpoints
- Testing component interactions
- You need to verify contracts

**Use E2E Tests when:**
- Testing critical user journeys
- Testing complete workflows
- You need high confidence
- Acceptance testing

## Test Framework Selection

| Framework | Use Case | Pros |
|-----------|----------|------|
| JUnit 5 | Modern Java testing | Extensions, parameterized tests |
| TestNG | Complex test suites | Flexible XML configuration |
| Mockito | Mocking | Clean API, widely used |
| AssertJ | Assertions | Fluent, readable |
| Hamcrest | Matchers | Composable matchers |

## Common Testing Patterns

| Pattern | Description |
|---------|-------------|
| AAA (Arrange-Act-Assert) | Standard test structure |
| Given-When-Then | BDD-style testing |
| Builder Pattern | Complex test data |
| Object Mother | Test fixture factory |
| Test Data Builder | Fluent test data creation |

## Further Reading

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://site.mockito.org/)
- [Testing Spring Boot](https://spring.io/guides/gs/testing-web/)
