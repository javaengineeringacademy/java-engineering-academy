# JUnit 5 Advanced - Internals

## Extension Model Architecture

JUnit 5 uses a hierarchical extension model:

```
Test Execution
├── BeforeAllCallback.beforeAll()
│   └── Extension Store initialized
├── For each test:
│   ├── ParameterResolver.resolveParameter()
│   ├── BeforeEachCallback.beforeEach()
│   ├── Test method execution
│   │   ├── @Test / @ParameterizedTest / @TestFactory
│   │   └── Assertion evaluation
│   ├── AfterEachCallback.afterEach()
│   └── TestExecutionExceptionHandler.handleTestExecutionException()
└── AfterAllCallback.afterAll()
```

## Parameterized Test Execution

1. Argument provider generates arguments
2. For each argument set:
   - ParameterResolver injects parameters
   - Test method invoked with arguments
   - Assertions evaluated
   - Results collected
3. All results aggregated into single test report

## Dynamic Test Discovery

```java
@TestFactory
Collection<DynamicTest> dynamicTests() {
    // Executed at DISCOVERY time
    // Returns test descriptors
    // Actual test body runs at EXECUTION time
}
```

- Discovery phase: generates test metadata
- Execution phase: runs test bodies
- Reports combined with static tests

## Extension Store

The Extension Store provides namespace-scoped storage:

- GLOBAL namespace: shared across all extensions
- Class-specific namespace: per test class
- Method-specific namespace: per test method
- Automatic cleanup via Closeable/AutoCloseable
