# Decision: TestNG

## When to Use TestNG

**Choose TestNG when:**
- Large test suites with parallel execution needs
- Complex test dependencies between methods/classes
- XML-based suite configuration is required
- Data-driven testing with large datasets
- Integration testing with multiple components

**Choose JUnit 5 when:**
- Modern Java projects (records, sealed classes)
- Extension-based test customization
- Simpler test configuration
- Community support is important

## Configuration Strategy

| Scenario | TestNG Approach |
|----------|----------------|
| Parallel execution | XML parallel attribute |
| Test groups | @Test(groups) |
| Data providers | @DataProvider |
| Dependencies | @Test(dependsOnMethods) |
| Parameters | @Parameters + XML |

## Test Organization

- Use groups for categorization (smoke, regression)
- Use suites for different environments
- Use listeners for custom reporting
- Use factories for dynamic test creation
