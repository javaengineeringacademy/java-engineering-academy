# Decision: JUnit 5

## When to Use JUnit 5

**Use JUnit 5 when:**
- Writing unit or integration tests for Java applications
- Need modern annotations (@Nested, @DisplayName, @Tag)
- Parameterized testing is required
- Custom test lifecycle management via Extensions
- Java 8+ features (lambdas, streams) are preferred

## When to Consider Alternatives

| Scenario | Alternative | Why |
|----------|-------------|-----|
| Legacy JUnit 4 tests exist | JUnit Vintage | Backward compatibility |
| Complex test grouping/parallel | TestNG | More flexible XML config |
| BDD-style with Groovy | Spock | Native BDD syntax |
| Microbenchmarking | JMH | Purpose-built for benchmarks |

## JUnit 5 vs JUnit 4

| Feature | JUnit 4 | JUnit 5 |
|---------|---------|---------|
| Annotations | @Before/@After | @BeforeEach/@AfterEach |
| Nested tests | No | @Nested |
| Parameterized | Limited | Full support |
| Extensions | Runners | Extension model |
| Display names | No | @DisplayName |
| Tags | @Category | @Tag |

## Common Patterns

- **Setup/Teardown**: @BeforeEach/@AfterEach for per-test, @BeforeAll/@AfterAll for class-level
- **Exception testing**: assertThrows() with lambda
- **Group assertions**: assertAll() for multiple checks
- **Conditional execution**: @EnabledOnOs, @DisabledIf
