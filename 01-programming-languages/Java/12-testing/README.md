# Module 12: Testing

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 35 min | **Practice:** 60 min | **Total:** 95 min

## Overview

Testing is essential for code quality, refactoring confidence, and production reliability. This module covers unit testing, integration testing, JUnit 5, Mockito, test design patterns, test doubles, mutation testing, and code coverage. Learn to write tests that catch bugs early and provide documentation.

## Learning Objectives

- [ ] Write unit tests using JUnit 5 (annotations, assertions, parameterized tests)
- [ ] Create mocks and stubs using Mockito
- [ ] Apply the Arrange-Act-Assert pattern
- [ ] Test exception handling and edge cases
- [ ] Use test doubles (stubs, mocks, fakes, spies, dummies)
- [ ] Measure and improve code coverage
- [ ] Apply mutation testing to verify test quality
- [ ] Write integration tests with Testcontainers

## Prerequisites

- Java fundamentals and OOP
- Basic understanding of Maven/Gradle
- Familiarity with interfaces and dependency injection

## History

- **2000** — JUnit 3 introduced test discovery via method naming conventions
- **2003** — JUnit 4 added annotations (`@Test`, `@Before`, `@After`)
- **2012** — Mockito 1.x released, simplifying mock creation
- **2014** — Java 8 lambdas improved test readability
- **2017** — JUnit 5 released with modern features (extensions, parameterized tests)
- **2018** — Mockito 2.x added inline mock making
- **2020** — Testcontainers gained popularity for integration testing
- **2021** — JUnit 5.8 added `@Nested` test classes, `@TestFactory`

## Production Notes

- **Where is it used?** In every Java application that needs quality assurance
- **Why is it useful?** Catch bugs early, enable refactoring, document behavior
- **When should it be avoided?** Not applicable; testing is essential
- **Alternative?** Manual testing (unreliable), property-based testing, chaos engineering

## Why This Concept Exists

Without testing:
- Bugs discovered in production
- Refactoring breaks existing functionality
- No documentation of expected behavior
- Fear of changing code
- Technical debt accumulates

## Core Concepts

### Testing Pyramid

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

### Test Doubles Taxonomy

| Type | Purpose | Behavior |
|------|---------|----------|
| Dummy | Fill parameter slots | No behavior, just passed |
| Stub | Provide canned answers | Returns predefined values |
| Mock | Verify interactions | Records and verifies calls |
| Spy | Wrap real object | Delegates to real, records calls |
| Fake | Working implementation | Simplified real implementation |

### Arrange-Act-Assert

```java
@Test
void shouldCalculateTotal() {
    // Arrange
    Order order = new Order();
    order.addItem(new Item("Laptop", 999.99));
    
    // Act
    double total = order.calculateTotal();
    
    // Assert
    assertEquals(999.99, total, 0.01);
}
```

## Internal Working

### JUnit 5 Lifecycle

```
@BeforeAll → @BeforeEach → @Test → @AfterEach → @AfterAll
```

### Mockito Mock Creation

```java
// Interface mock
List<String> mockList = mock(List.class);

// Class mock
UserService mockService = mock(UserService.class);

// Stub behavior
when(mockService.findById(1L)).thenReturn(new User(1L, "Alice"));

// Verify interactions
verify(mockService, times(1)).findById(1L);
```

## Syntax

```java
// JUnit 5 annotations
@Test
@DisplayName("Should calculate total")
@Timeout(5)
@Disabled("Not implemented yet")
@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
@CsvSource({"1,1,2", "2,3,5"})
@MethodSource("dataProvider")

// Assertions
assertEquals(expected, actual);
assertThrows(Exception.class, () -> method());
assertAll(() -> assertEquals(a, b), () -> assertEquals(c, d));

// Mockito
mock(Class.class);
when(mock.method()).thenReturn(value);
verify(mock, times(1)).method();
doThrow(new Exception()).when(mock).method();
```

## Examples

### Easy: Basic Unit Test
```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    void shouldAddTwoNumbers() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
    }
    
    @Test
    void shouldThrowOnDivideByZero() {
        Calculator calc = new Calculator();
        assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
    }
}
```

### Medium: Mockito Test
```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository repository;
    
    @Test
    void shouldFindUserById() {
        when(repository.findById(1L)).thenReturn(Optional.of(new User(1L, "Alice")));
        
        UserService service = new UserService(repository);
        User user = service.findById(1L);
        
        assertEquals("Alice", user.getName());
        verify(repository, times(1)).findById(1L);
    }
}
```

### Hard: Parameterized Test
```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {
    @ParameterizedTest
    @CsvSource({
        "1, 1, 2",
        "2, 3, 5",
        "0, 0, 0",
        "-1, 1, 0"
    })
    void shouldAdd(int a, int b, int expected) {
        assertEquals(expected, new MathUtils().add(a, b));
    }
}
```

### Enterprise: Integration Test with Testcontainers
```java
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserRepositoryIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");
    
    @Test
    void shouldSaveAndRetrieveUser() {
        UserRepository repo = new UserRepository(postgres.getJdbcUrl());
        User user = new User(null, "Alice", "alice@example.com");
        
        User saved = repo.save(user);
        User found = repo.findById(saved.getId()).orElseThrow();
        
        assertEquals("Alice", found.getName());
    }
}
```

## Performance Considerations

| Test Type | Execution Time | Cost |
|-----------|---------------|------|
| Unit test | ~1ms | Low |
| Integration test | ~100ms | Medium |
| E2E test | ~1s | High |
| Mutation test | ~10s | Very High |

## Best Practices

**Do's:**
- Follow FIRST principles (Fast, Independent, Repeatable, Self-validating, Timely)
- Use Arrange-Act-Assert pattern
- Test behavior, not implementation
- Test edge cases (null, empty, boundary)
- Use meaningful test names
- Keep tests independent

**Don'ts:**
- Don't test private methods
- Don't use `System.out.println` in tests
- Don't rely on test execution order
- Don't write tests that depend on external state
- Don't ignore failing tests

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Testing implementation | Brittle tests | Test behavior (what, not how) |
| Shared mutable state | Flaky tests | Create fresh instances in each test |
| Ignoring edge cases | Missed bugs | Test null, empty, boundary values |
| Over-mocking | Tests don't test real code | Use real objects when possible |
| No assertions | Tests always pass | Always assert expected outcomes |

## Interview Questions

### Q1: What is the difference between unit and integration tests?
**Answer:** Unit tests test individual classes in isolation (fast, no dependencies). Integration tests test component interactions (slower, may use databases/APIs). Unit tests are many; integration tests are fewer.

### Q2: What is a mock vs a stub?
**Answer:** A stub provides canned answers (state verification). A mock verifies interactions (behavior verification). Stubs are for testing; mocks are for verifying expectations.

### Q3: What is the Arrange-Act-Assert pattern?
**Answer:** Structure tests in three parts: Arrange (set up), Act (execute), Assert (verify). This makes tests readable and maintainable.

### Q4: What is mutation testing?
**Answer:** Automatically modifies source code (mutants) to verify test quality. If tests still pass after mutation, they're not catching bugs. Use PITest for Java.

### Q5: What is code coverage and what's a good target?
**Answer:** Percentage of code executed by tests. Aim for 80%+ line coverage, but focus on meaningful coverage (edge cases, error paths) not just numbers.

### Q6: What is Testcontainers?
**Answer:** Library that runs Docker containers for integration testing. Provides real databases, message brokers, etc. without mocking. Tests run against real infrastructure.

### Q7: What is the difference between `@BeforeAll` and `@BeforeEach`?
**Answer:** `@BeforeAll` runs once before all tests (setup expensive resources). `@BeforeEach` runs before each test (setup fresh state). `@BeforeAll` must be static in JUnit 5.

### Q8: What is parameterized testing?
**Answer:** Running the same test with different inputs. JUnit 5 provides `@ParameterizedTest` with `@ValueSource`, `@CsvSource`, `@MethodSource`. Reduces test duplication.

### Q9: What is a test double and what types exist?
**Answer:** Objects used in place of real dependencies. Types: Dummy (fill parameters), Stub (canned answers), Mock (verify interactions), Spy (wrap real), Fake (simplified real).

### Q10: What is the FIRST principle?
**Answer:** Fast (runs quickly), Independent (no dependencies), Repeatable (same result), Self-validating (clear pass/fail), Timely (written with code).

## Cross-References

- **Previous Module:** [11 - Design Patterns](../11-design-patterns/)
- **Next Module:** [13 - Reflection & Annotations](../13-reflection-annotations/)
- **Related:** [06 - Generics](../06-generics/) — parameterized tests
- **Related:** [07 - Functional Programming](../07-functional-programming/) — lambdas in tests
- **Related:** [14 - Logging](../14-logging/) — test logging

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Flaky test | Isolation + logging | Run test in isolation; add logging |
| Mock not working | Mockito debugger | Verify mock setup; check method signatures |
| Slow test | Profiling | Identify slow operations; mock external calls |
| Test order dependency | Randomize execution | Run tests in random order |
| Coverage gap | Coverage report | Analyze uncovered lines; add tests |

## Code Review Checklist

- [ ] Tests follow AAA pattern
- [ ] Test names are descriptive
- [ ] Edge cases covered
- [ ] No shared mutable state
- [ ] Mocks are verified
- [ ] Tests are independent
- [ ] No ignored tests without reason

## Architecture Considerations

Testing is a cross-cutting concern that affects every layer of an application. At scale, test strategy determines release velocity and production reliability. For microservices, contract testing ensures API compatibility. For event-driven systems, integration tests verify message flow.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Unit testing | Individual classes | Pros: Fast, isolated; Cons: Doesn't test integration |
| Integration testing | Component interactions | Pros: Tests real behavior; Cons: Slower, complex setup |
| E2E testing | Complete workflows | Pros: Tests user experience; Cons: Brittle, expensive |
| Contract testing | API compatibility | Pros: Ensures compatibility; Cons: Requires consumer/provider |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Test data in production | Data corruption | Use test databases, not production |
| Mocked security checks | Bypassed authentication | Test security with real implementations |
| Test credentials exposed | Credential leak | Use environment variables, not hardcoded |
| Test code in production | Attack surface | Exclude test code from production builds |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| JUnit 3 | Method naming | Use annotations (JUnit 4+) |
| JUnit 4 | Annotations | Use `@Test`, `@Before`, `@After` |
| JUnit 5 | Extensions, parameterized | Use `@ExtendWith`, `@ParameterizedTest` |
| Mockito 2 | Inline mock maker | Use `mockito-inline` |
| Testcontainers | Docker-based testing | Add for integration tests |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| JUnit 5 | Java 8+ | Stable |
| Mockito 5 | Java 11+ | Stable |
| Testcontainers | Java 8+ | Stable |
| PITest | Java 8+ | Stable |

## Production Incidents

### Incident 1: Flaky Test Hiding Bug

**Problem:** A flaky test was marked as `@Disabled` for 3 months, hiding a real concurrency bug.
**Cause:** Test was order-dependent; running alone passed, running with others failed. Developer disabled it.
**Impact:** Bug reached production; caused data corruption in 2% of transactions.
**Detection:** Production incident revealed bug that flaky test was supposed to catch.
**Solution:** Fixed test isolation; removed `@Disabled`; added to CI with retry.
**Prevention:** Never disable flaky tests; fix the root cause; add test isolation checks.

### Incident 2: Mock Hiding Real Behavior

**Problem:** Unit tests passed but integration tests failed; mock didn't match real API behavior.
**Cause:** Mock returned empty list; real API returned null; code didn't handle null.
**Impact:** 10% of API calls failed in production.
**Detection:** Integration tests caught the issue; unit tests didn't.
**Solution:** Used integration tests for critical paths; improved mock accuracy.
**Prevention:** Use real implementations for critical paths; verify mocks match real behavior.

### Incident 3: Test Coverage Metric Gaming

**Problem:** Code coverage was 90% but production bugs increased; tests weren't meaningful.
**Cause:** Developer added trivial tests to boost coverage; didn't test edge cases or error paths.
**Impact:** 15% increase in production bugs despite high coverage.
**Detection:** Code review revealed trivial tests; mutation testing showed low mutation score.
**Solution:** Focused on mutation testing; added edge case tests; improved test quality.
**Prevention:** Use mutation testing alongside coverage; focus on test quality over quantity.

## Production Checklist

- [ ] Tests follow AAA pattern
- [ ] Test names are descriptive
- [ ] Edge cases covered
- [ ] No shared mutable state
- [ ] Mocks are verified
- [ ] Tests are independent
- [ ] No ignored tests without reason
- [ ] CI runs all tests
- [ ] Coverage meets threshold
- [ ] Mutation testing verifies quality

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Writes basic tests; uses `System.out.println`; ignores edge cases |
| Intermediate | Uses JUnit 5; applies AAA; tests edge cases |
| Advanced | Uses Mockito; writes integration tests; applies test doubles |
| Expert | Designs test strategy; uses mutation testing; mentors on testing |

## Common Myths

1. **Myth**: 100% code coverage means no bugs
   **Truth**: Coverage measures execution, not quality. Tests can cover code without testing meaningful scenarios.

2. **Myth**: Unit tests are always better than integration tests
   **Truth**: Both are necessary. Unit tests verify isolation; integration tests verify interactions.

3. **Myth**: Mocks are always better than real objects
   **Truth**: Mocks can hide real behavior. Use real implementations for critical paths.

4. **Myth**: Tests slow down development
   **Truth**: Tests enable faster development by catching bugs early and enabling safe refactoring.

5. **Myth**: Test code doesn't need review
   **Truth**: Test code is code. It should follow standards and be reviewed.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Verify code correctness, enable refactoring |
| Pyramid | Many unit, some integration, few E2E |
| AAA | Arrange, Act, Assert |
| Test doubles | Dummy, Stub, Mock, Spy, Fake |
| JUnit 5 | Extensions, parameterized, nested |
| Mockito | Mock creation, stubbing, verification |
| Best practice | Test behavior, not implementation |
| Common mistake | 100% coverage = no bugs |
| When to use | All Java applications |
| When to avoid | Never — testing is essential |
