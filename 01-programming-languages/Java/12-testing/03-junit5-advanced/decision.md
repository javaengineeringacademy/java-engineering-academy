# Decision: JUnit 5 Advanced

## When to Use

**Use Parameterized Tests when:**
- Testing the same logic with multiple inputs
- Data-driven testing is needed
- Boundary conditions must be covered systematically

**Use Dynamic Tests when:**
- Test data is generated at runtime
- Combinatorial testing is required
- Tests depend on external data sources

**Use Custom Extensions when:**
- Reusable test infrastructure is needed
- Cross-cutting concerns apply to multiple tests
- Custom lifecycle management is required

## Extension Points

| Extension Point | Use Case | Example |
|----------------|----------|---------|
| BeforeAllCallback | Setup before all tests | Database migration |
| BeforeEachCallback | Setup before each test | Reset mock state |
| AfterEachCallback | Cleanup after each test | Close connections |
| AfterAllCallback | Cleanup after all tests | Drop test database |
| ParameterResolver | Inject parameters | Inject configuration |
| TestExecutionExceptionHandler | Handle exceptions | Retry failed tests |

## Parameterized Test Strategy

| Source | Use When | Example |
|--------|----------|---------|
| @ValueSource | Single array of literals | String, int, double |
| @CsvSource | Multiple arguments | Test multi-param methods |
| @MethodSource | Complex data generation | Objects, collections |
| @CsvFileSource | Large datasets from files | Production test data |
| @ArgumentsSource | Custom argument provider | Dynamic data |
