# Unit Testing - Internals

## Test Execution Architecture

```
Test Runner
├── Discovery Phase
│   ├── Scan classpath for @Test
│   ├── Filter by tags/conditions
│   └── Order tests (optional)
├── Execution Phase
│   ├── Create test instance (new instance per test)
│   ├── Execute @BeforeAll (once per class)
│   ├── For each test:
│   │   ├── Execute @BeforeEach
│   │   ├── Execute test method
│   │   ├── Evaluate assertions
│   │   └── Execute @AfterEach
│   └── Execute @AfterAll (once per class)
└── Reporting Phase
    ├── Collect pass/fail/skip
    └── Generate reports
```

## Mock Object Creation

Mock objects are created using ByteBuddy:

1. Generate subclass of target class/interface
2. Override all methods
3. Install invocation handler
4. Return default values for unstubbed methods
5. Record invocations for verification

## Assertion Mechanics

Assertions compare expected vs actual:

```java
assertEquals(expected, actual)
// Internally:
if (!expected.equals(actual)) {
    throw new AssertionError("expected: " + expected + " but was: " + actual);
}
```

## Test Instance Lifecycle

JUnit 5 creates a new instance per test:

```
Test 1: new TestClass() → setup → execute → teardown → GC
Test 2: new TestClass() → setup → execute → teardown → GC
```

This ensures complete isolation between tests.
