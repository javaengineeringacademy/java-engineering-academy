# 11.2 JUnit 5 Basics

## 1. Introduction

JUnit 5 is the latest major version of the most popular Java testing framework. It introduces a modular architecture, powerful new features, and modern Java capabilities. This module covers JUnit 5 annotations, assertions, lifecycle callbacks, and test organization.

## 2. Learning Objectives

- Master JUnit 5 annotations (@Test, @BeforeEach, @AfterEach, @BeforeAll, @AfterAll)
- Use assertions effectively with detailed messages
- Understand test lifecycle and execution order
- Organize tests with @DisplayName and @Nested
- Control test execution with @Disabled and @Enabled conditions

## 3. Prerequisites

- Java 17+ installed
- Basic understanding of testing concepts
- Maven or Gradle build tool
- IDE with JUnit support

## 4. Why This Concept Exists

JUnit 5 addresses limitations of JUnit 4 by providing:
- Modular architecture (JUnit Platform, Jupiter, Vintage)
- Modern Java features support
- Better extension model
- Improved assertions and assumptions
- Parameterized tests and nested test classes

## 5. Problem Statement

How do we write reliable, maintainable tests with proper setup/teardown, meaningful output, and flexible execution control?

## 6. Theory

### JUnit 5 Architecture

```
JUnit 5 = JUnit Platform + JUnit Jupiter + JUnit Vintage

JUnit Platform: Launches testing frameworks on JVM
JUnit Jupiter: New programming and extension model
JUnit Vintage: Backward compatibility with JUnit 3/4
```

### Core Annotations

| Annotation | Description |
|------------|-------------|
| `@Test` | Marks a method as a test |
| `@BeforeEach` | Runs before each test method |
| `@AfterEach` | Runs after each test method |
| `@BeforeAll` | Runs once before all tests in class |
| `@AfterAll` | Runs once after all tests in class |
| `@DisplayName` | Custom name for test display |
| `@Disabled` | Disable a test |
| `@Nested` | Group related tests |
| `@Tag` | Filter test execution |

### Assertion Methods

- `assertEquals(expected, actual)` - Value equality
- `assertNotEquals(expected, actual)` - Value inequality
- `assertTrue(condition)` - Boolean check
- `assertFalse(condition)` - Boolean negation
- `assertNull(object)` - Null check
- `assertNotNull(object)` - Not null check
- `assertThrows(exceptionClass, executable)` - Exception verification
- `assertAll(label, executables)` - Group assertions
- `assertIterableEquals(expected, actual)` - Collection comparison

## 7. Internal Working

1. **Test Discovery**: JUnit scans for `@Test` methods via reflection
2. **Test Execution**: Platform launcher executes tests via TestEngine API
3. **Lifecycle Management**: Framework manages setup/teardown callbacks
4. **Assertion Evaluation**: Assertions throw `AssertionError` on failure
5. **Result Reporting**: Results collected and formatted for output

## 8. JVM Perspective

- Tests run in the same JVM as production code
- No separate process for test execution
- Reflection used for test discovery (minimal overhead)
- Test classes loaded by same classloader
- Memory shared between tests (requires proper cleanup)

## 9. Memory Representation

```
JUnit 5 Execution Model:
┌────────────────────────────────┐
│        JUnit Platform          │
│  ┌──────────────────────────┐ │
│  │    TestEngine API        │ │
│  │  ┌────────────────────┐  │ │
│  │  │   Jupiter Engine   │  │ │
│  │  │  ┌──────────────┐  │  │ │
│  │  │  │ Test Class 1 │  │  │ │
│  │  │  │ Test Class 2 │  │  │ │
│  │  │  │ Test Class N │  │  │ │
│  │  │  └──────────────┘  │  │ │
│  │  └────────────────────┘  │ │
│  └──────────────────────────┘ │
│  ┌──────────────────────────┐ │
│  │   Launcher Discovery     │ │
│  │   Launcher Execution     │ │
│  └──────────────────────────┘ │
└────────────────────────────────┘
```

## 10. Architecture Diagram

```mermaid
graph TD
    A[JUnit Platform] --> B[Launcher]
    B --> C[Discovery Request]
    B --> D[Execution Request]
    
    C --> E[TestEngine Discovery]
    D --> F[TestEngine Execution]
    
    E --> G[Jupiter Engine]
    F --> G
    
    G --> H[Test Class]
    H --> I[Test Method]
    H --> J[Lifecycle Methods]
    
    I --> K[Assertions]
    J --> L[Setup/Teardown]
    
    K --> M[Results]
    L --> M
    
    M --> N[Reporting]
```

## 11. Flow Diagram

```mermaid
flowchart TD
    Start([Test Suite Start]) --> Discover[Discover Tests]
    Discover --> Filter[Filter by Tags/Conditions]
    Filter --> Create[Create Test Instance]
    
    Create --> BeforeAll[Run @BeforeAll]
    BeforeAll --> TestLoop[For Each Test]
    
    TestLoop --> BeforeEach[Run @BeforeEach]
    BeforeEach --> Arrange[Arrange Test Data]
    Arrange --> Act[Execute @Test Method]
    Act --> Assert[Run Assertions]
    Assert --> AfterEach[Run @AfterEach]
    AfterEach --> More{More Tests?}
    
    More -->|Yes| TestLoop
    More -->|No| AfterAll[Run @AfterAll]
    AfterAll --> Report[Generate Report]
    Report --> End([Suite Complete])
```

## 12. Syntax

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator Tests")
class CalculatorTest {
    
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before All Tests");
    }
    
    @AfterAll
    static void afterAll() {
        System.out.println("After All Tests");
    }
    
    @BeforeEach
    void setUp() {
        System.out.println("Before Each Test");
    }
    
    @AfterEach
    void tearDown() {
        System.out.println("After Each Test");
    }
    
    @Test
    @DisplayName("Should add two numbers correctly")
    void shouldAddTwoNumbers() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
    }
    
    @Test
    @Disabled("Not yet implemented")
    void notYetImplemented() {
        // TODO: Implement later
    }
    
    @Test
    void shouldVerifyException() {
        assertThrows(ArithmeticException.class, 
            () -> { int result = 1 / 0; });
    }
    
    @Nested
    @DisplayName("When using scientific functions")
    class ScientificTests {
        @Test
        void shouldCalculateSquareRoot() {
            assertEquals(2.0, Math.sqrt(4.0), 0.0001);
        }
    }
}
```

## 13. Easy Example

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringTest {
    
    @Test
    void shouldConcatenateStrings() {
        // Arrange
        String first = "Hello";
        String second = "World";
        
        // Act
        String result = first + " " + second;
        
        // Assert
        assertEquals("Hello World", result);
    }
    
    @Test
    void shouldCheckStringLength() {
        // Arrange
        String text = "JUnit";
        
        // Act & Assert
        assertEquals(5, text.length());
    }
    
    @Test
    void shouldHandleNullString() {
        // Arrange
        String nullString = null;
        
        // Act & Assert
        assertNull(nullString);
        assertNotNull("Not null");
    }
}
```

## 14. Medium Example

```java
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Collection Operations Tests")
class CollectionTest {
    
    private List<String> fruits;
    
    @BeforeEach
    void setUp() {
        fruits = List.of("Apple", "Banana", "Cherry", "Date");
    }
    
    @Test
    @DisplayName("Should contain all fruits")
    void shouldContainAllFruits() {
        // Assert multiple conditions
        assertAll("Fruit checks",
            () -> assertEquals(4, fruits.size()),
            () -> assertTrue(fruits.contains("Apple")),
            () -> assertTrue(fruits.contains("Banana")),
            () -> assertTrue(fruits.contains("Cherry")),
            () -> assertTrue(fruits.contains("Date"))
        );
    }
    
    @Test
    @DisplayName("Should find fruit starting with specific letter")
    void shouldFindFruitStartingWith() {
        // Arrange
        String prefix = "B";
        
        // Act
        List<String> result = fruits.stream()
            .filter(f -> f.startsWith(prefix))
            .toList();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("Banana", result.get(0));
    }
    
    @Test
    @DisplayName("Should throw exception for invalid index")
    void shouldThrowExceptionForInvalidIndex() {
        // Act & Assert
        IndexOutOfBoundsException exception = 
            assertThrows(IndexOutOfBoundsException.class, 
                () -> fruits.get(10));
        
        assertTrue(exception.getMessage().contains("10"));
    }
}
```

## 15. Hard Example

```java
import org.junit.jupiter.api.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Complex Lifecycle Tests")
class LifecycleTest {
    
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static ExecutorService executor;
    private String testName;
    
    @BeforeAll
    static void initAll() {
        executor = Executors.newFixedThreadPool(3);
        System.out.println("Global setup - initialized shared resources");
    }
    
    @AfterAll
    static void cleanupAll() {
        executor.shutdown();
        System.out.println("Global cleanup - resources released");
    }
    
    @BeforeEach
    void setUp(TestInfo testInfo) {
        testName = testInfo.getDisplayName();
        counter.incrementAndGet();
        System.out.println("Setting up: " + testName);
    }
    
    @AfterEach
    void tearDown(TestInfo testInfo) {
        System.out.println("Tearing down: " + testInfo.getDisplayName());
    }
    
    @Test
    @DisplayName("Should execute concurrently")
    void shouldExecuteConcurrently() throws Exception {
        // Arrange
        int tasks = 10;
        AtomicInteger successCount = new AtomicInteger(0);
        
        // Act
        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            futures.add(executor.submit(() -> {
                successCount.incrementAndGet();
                return true;
            }));
        }
        
        // Wait for all tasks
        for (Future<Boolean> future : futures) {
            assertTrue(future.get(5, TimeUnit.SECONDS));
        }
        
        // Assert
        assertEquals(tasks, successCount.get());
    }
    
    @Test
    @DisabledIf("isEvenCounter")
    @DisplayName("Should skip when counter is even")
    void shouldSkipWhenEven() {
        // This test runs only when counter is odd
        assertTrue(counter.get() % 2 != 0);
    }
    
    private boolean isEvenCounter() {
        return counter.get() % 2 == 0;
    }
    
    @Nested
    @DisplayName("Inner group of tests")
    class InnerTests {
        @Test
        void shouldPassInNestedGroup() {
            assertTrue(true);
        }
    }
}
```

## Interview Questions

1. **What is @BeforeEach used for?**
   Runs a method before each test method for setup logic.

2. **How do you disable a test in JUnit 5?**
   Use `@Disabled("reason")` annotation.

3. **What is @Nested used for?**
   Groups related tests into inner classes for better organization.

4. **How do you assert an exception is thrown?**
   Use `assertThrows(ExceptionClass.class, () -> { ... })`.

5. **What is the difference between @BeforeAll and @BeforeEach?**
   `@BeforeAll` runs once before all tests; `@BeforeEach` runs before each test.

## Pitfalls

1. **Shared mutable state**: Causes test interdependence and flakiness
2. **Overusing mocks**: Tests become coupled to implementation details
3. **Missing cleanup**: Leftover state affects subsequent tests
4. **Testing implementation**: Focus on behavior, not internal details

## Performance

1. **Test isolation**: Each test should be independent for parallel execution
2. **Setup cost**: Minimize expensive operations in @BeforeEach
3. **Mock creation**: Reuse mocks via @MockBean when possible
4. **Assertion count**: Use assertAll() for grouped assertions to see all failures

## Engineering Decision Framework

### ✅ Use JUnit 5 when:
- Writing unit tests for Java applications
- Integration testing with Spring or other frameworks
- Parameterized or data-driven testing is needed
- Test lifecycle management with @BeforeEach/@AfterEach
- Modern assertions and assumptions API is beneficial

### ❌ Avoid JUnit 5 when:
- Legacy JUnit 3/4 tests already work (use Vintage runner)
- Simple script-based testing suffices
- Testing requires external tools (Postman, curl for APIs)
- Performance benchmarking is the goal (use JMH instead)

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| TestNG | Advanced test grouping and parallel execution |
| Spock | BDD-style testing with Groovy |
| JMH | Microbenchmarking and performance measurement |
| Mockito | Mocking framework (used alongside JUnit) |
| AssertJ | Rich fluent assertions (complements JUnit) |

### Production Examples
- Unit testing service layer business logic
- Integration testing REST API endpoints
- Database repository testing with TestContainers
- Contract testing for microservices
- Regression test suites in CI/CD pipelines

### Common Production Mistakes
- Testing implementation details instead of behavior
- Not cleaning up test data (flaky tests)
- Overusing mocks (tests become coupled to implementation)
- Ignoring test execution time in CI pipelines
- Using @Disabled as a permanent fix instead of tracking tech debt

## Production Incidents

### Incident 1: Flaky Test Causing False Positives

**Problem:** A CI/CD pipeline showed intermittent test failures that passed on retry. Developers started ignoring test failures, assuming they were "just flaky."
**Cause:** A test depended on system time (`LocalDateTime.now()`) and ran slightly differently depending on execution speed. When the test ran near midnight, date-based assertions failed. The test also shared state via a static field that wasn't reset between runs.
**Impact:** 40% of CI builds were red. Developers stopped trusting the test suite. A real regression slipped through and reached production.
**Detection:** Test failure analysis showed failures correlated with midnight execution times.
**Solution:** Inject time via `Clock` parameter instead of using system time. Use `@BeforeEach` to reset all shared state. Add `@Timeout` to prevent hanging tests.
**Prevention:** Enforce test isolation rules. Use random test ordering to surface state dependencies. Add flaky test detection to CI pipeline.

### Incident 2: Missing Integration Test Causing Production Bug

**Problem:** A new feature passed all unit tests but broke in production. The bug caused data corruption affecting 10,000 records.
**Cause:** Unit tests mocked the database layer, testing only business logic. The actual SQL query had a syntax error that only manifested against a real database. No integration test existed for this feature.
**Impact:** Data corruption required manual remediation. 10,000 records affected. 6-hour production incident.
**Detection:** Users reported incorrect data in their accounts.
**Solution:** Add integration tests using TestContainers for database-dependent code. Require both unit and integration tests for features touching persistence.
**Prevention:** Establish test pyramid: unit tests (70%), integration tests (20%), E2E tests (10%). Require integration tests for any code touching external systems. Add test coverage gates to CI/CD.

## Production Checklist

### ✅ Before using JUnit 5 in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## Common Myths

### ❌ Myth 1: 100% code coverage = no bugs
**Reality:** Coverage != quality. High coverage doesn't mean tests are meaningful or thorough.

### ❌ Myth 2: Unit tests are enough
**Reality:** Need integration tests. Unit tests with mocks may not catch real-world interaction issues.

### ❌ Myth 3: Test order matters
**Reality:** Tests should be independent. Each test must work regardless of execution order.

## References

- [JUnit 5 Official Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Baeldung JUnit 5 Tutorial](https://www.baeldung.com/junit-5)
- [JUnit 5 GitHub Repository](https://github.com/junit-team/junit5)

## Alternatives

| Framework | Parallel | BDD Style | Parameterized | Extensions | Use When |
|-----------|----------|-----------|---------------|------------|----------|
| JUnit 5 | Yes | No | Yes | Yes | Standard Java unit testing |
| TestNG | Yes | No | Yes | Yes | Advanced grouping, parallel execution |
| Spock | Yes | Yes | Yes | Yes | BDD-style with Groovy |
| JMH | No | No | No | No | Microbenchmarking |
| AssertJ | N/A | No | N/A | N/A | Rich fluent assertions (complements JUnit) |

## Trade-offs

JUnit 5 provides modern testing because it:
- Requires more setup than ad-hoc scripts (use Vintage for legacy JUnit 4)
- Mocking frameworks add complexity (use real objects when possible)
- Test isolation requires discipline (use @BeforeEach to reset state)
- Parameterized tests can be hard to read (use @CsvSource or @MethodSource carefully)
- Integration tests are slower than unit tests (use TestContainers for database tests)

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations
