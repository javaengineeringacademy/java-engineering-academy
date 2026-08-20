# Testing Fundamentals - Internals

## Test Discovery Process

When a test framework runs tests, the following occurs:

1. **Classpath Scanning**: Framework scans classpath for test classes
2. **Annotation Processing**: Identifies methods annotated with @Test, @BeforeEach, etc.
3. **Test Class Instantiation**: Creates new instance per test method (JUnit 5 default)
4. **Lifecycle Execution**: Runs setup/teardown methods in order
5. **Assertion Evaluation**: Compares expected vs actual values
6. **Result Collection**: Gathers pass/fail/skip status

## Assertion Internals

Assertions work by comparing values and throwing `AssertionError` on failure:

```java
// Pseudocode of assertEquals internals
static void assertEquals(Object expected, Object actual) {
    if (expected == null && actual == null) return;
    if (expected != null && expected.equals(actual)) return;
    throw new AssertionError("Expected: " + expected + " but was: " + actual);
}
```

## Test Instance Lifecycle

JUnit 5 creates a **new test instance per test method** by default:

```
@Test method 1 → new TestClass() → setup → execute → teardown → GC
@Test method 2 → new TestClass() → setup → execute → teardown → GC
```

This ensures test isolation—no state leaks between tests.

## Reflection Usage

Test frameworks heavily use reflection:
- `Method.getAnnotation()` to detect test annotations
- `Class.newInstance()` to create test instances
- `Method.invoke()` to execute test methods
- `Constructor.newInstance()` for parameterized constructors

## Test Runner Architecture

```
Test Runner
├── Test Discovery (classpath scan)
├── Test Filtering (tags, conditions)
├── Test Ordering (optional)
├── Test Execution (lifecycle)
├── Assertion Evaluation
└── Result Reporting
```
