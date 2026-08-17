# Testing Exercises

Practice Java testing through hands-on exercises.

## Exercise 1: Unit Tests for a Calculator

**Problem Statement:**
Write detailed unit tests for a `Calculator` class that supports add, subtract, multiply, divide, and modulo operations. Test normal cases, edge cases (zero, negative numbers), and error cases (division by zero).

**Expected Behavior:**
- `add(2, 3)` returns `5`.
- `subtract(10, 4)` returns `6`.
- `multiply(3, 7)` returns `21`.
- `divide(10, 3)` returns `3` (integer division).
- `divide(10, 0)` throws `ArithmeticException`.
- `modulo(10, 3)` returns `1`.
- Tests cover negative numbers and zero as inputs.

**Hints:**
- Use `@Test` annotation for each test method.
- Use `assertEquals(expected, actual)` for value checks.
- Use `assertThrows(ArithmeticException.class, () -> calc.divide(1, 0))` for exceptions.
- Name methods descriptively: `testAdd_PositiveNumbers`, `testDivide_ByZero`.

---

## Exercise 2: Mock a Database Repository

**Problem Statement:**
Write unit tests for a `UserService` class that depends on a `UserRepository`. Mock the repository using Mockito to test the service logic in isolation.

**Expected Behavior:**
- `UserService.findById(1)` returns a user when the repository finds it.
- `UserService.findById(999)` throws `UserNotFoundException` when not found.
- `UserService.save(user)` calls `repository.save()` exactly once.
- `UserService.save(null)` throws `IllegalArgumentException` without calling the repository.
- Mock verification confirms the correct repository methods were called.

**Hints:**
- Use `@Mock` annotation and `MockitoAnnotations.openMocks(this)` in `@BeforeEach`.
- Use `when(repository.findById(1)).thenReturn(Optional.of(user))`.
- Use `verify(repository, times(1)).save(user)` to confirm interactions.
- Use `verifyNoInteractions(repository)` when save should not be called.

---

## Exercise 3: Test Exception Handling

**Problem Statement:**
Write tests that verify exception behavior for a `FileProcessor` class. Test that invalid file paths, permission errors, and malformed content all throw the correct exceptions with appropriate messages.

**Expected Behavior:**
- Processing a null file path throws `IllegalArgumentException`.
- Processing a non-existent file throws `FileNotFoundException`.
- Processing a file with invalid content throws `ParseException`.
- Each exception carries a descriptive message.
- Tests verify both the exception type and the message content.

**Hints:**
- Use `assertThrows` and capture the exception for message verification.
- Use `@ParameterizedTest` with `@NullAndEmptySource` for null/empty path tests.
- Verify exception messages with `assertEquals("expected message", exception.getMessage())`.
- Use `assertAll` to group multiple assertions for the same test scenario.

---

## Exercise 4: Integration Test for REST API

**Problem Statement:**
Write integration tests for a REST API that manages products. Use `@SpringBootTest` with `TestRestTemplate` or `WebTestClient` to test the full request-response cycle including database interactions.

**Expected Behavior:**
- `GET /products` returns a list of all products with status 200.
- `GET /products/1` returns a specific product with status 200.
- `GET /products/999` returns status 404.
- `POST /products` creates a new product and returns status 201.
- `PUT /products/1` updates an existing product and returns status 200.
- `DELETE /products/1` removes a product and returns status 204.

**Hints:**
- Use `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)` for random port.
- Use `TestRestTemplate` or `WebTestClient` for making HTTP requests.
- Use an in-memory database (H2) for test isolation.
- Add `@Transactional` to rollback after each test.

---

## Exercise 5: Parameterized Tests

**Problem Statement:**
Rewrite calculator tests using JUnit 5 parameterized tests. Use `@ParameterizedTest` with `@ValueSource`, `@CsvSource`, `@CsvFileSource`, and `@MethodSource` to test multiple input combinations efficiently.

**Expected Behavior:**
- `@ValueSource` tests `isEven()` with multiple integer inputs.
- `@CsvSource` tests `add()` with multiple pairs of inputs and expected outputs.
- `@CsvFileSource` reads test data from a CSV file.
- `@MethodSource` provides test data from a static factory method.
- Each parameterized test runs independently with its own inputs.

**Hints:**
- Use `@ParameterizedTest @ValueSource(ints = {2, 4, 6, 8})` for single-value inputs.
- Use `@CsvSource({"1,2,3", "4,5,9", "-1,1,0"})` for CSV inline data.
- Use `@CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)` for file-based data.
- Use `@MethodSource("addTestCases")` where the method returns `Stream<Arguments>`.

---

## Exercise 6: Test Coverage Analysis

**Problem Statement:**
Write tests for a `StringUtils` class and analyze test coverage using JaCoCo. Identify untested code paths and write additional tests to achieve 90%+ line coverage.

**Expected Behavior:**
- Initial test suite achieves at least 70% line coverage.
- JaCoCo report shows uncovered branches (e.g., if-else conditions).
- Additional tests target uncovered branches to increase coverage.
- Final coverage report shows 90%+ line coverage and 80%+ branch coverage.
- No tests are redundant (each test covers a unique path).

**Hints:**
- Run `mvn jacoco:report` to generate the coverage report.
- Open `target/site/jacoco/index.html` in a browser.
- Look for red/yellow lines in the report indicating uncovered code.
- Focus on branch coverage by testing both true and false paths of conditionals.

---

## Exercise 7: Test-Driven Development

**Problem Statement:**
Use TDD to implement a `PasswordValidator` class. Write the test first for each requirement, then implement just enough code to make it pass. The validator should enforce minimum length, uppercase, lowercase, digit, and special character rules.

**Expected Behavior:**
- Red: Write a test for minimum length requirement; it fails.
- Green: Implement minimum length check; test passes.
- Refactor: Clean up code while keeping tests green.
- Red: Write a test for uppercase requirement; it fails.
- Green: Implement uppercase check; test passes.
- Continue until all requirements are implemented with passing tests.

**Hints:**
- Follow the Red-Green-Refactor cycle strictly.
- Write only one failing test at a time.
- Implement the simplest code that makes the test pass.
- Refactor only when all tests are green.
- Use descriptive test names: `testPassword_TooShort_IsInvalid`.

---

## Exercise 8: Refactor with Test Safety Net

**Problem Statement:**
Given a poorly structured `OrderCalculator` class with everything in one method, first write detailed tests to capture current behavior, then refactor the method into smaller, well-named methods while ensuring all tests continue to pass.

**Expected Behavior:**
- Existing behavior is captured in 15+ test cases before refactoring.
- Tests cover all branches and edge cases of the original method.
- After refactoring, the same tests pass against the new code.
- The refactored code has at least 4 smaller methods with clear names.
- No test was modified during refactoring (only possibly added).

**Hints:**
- Start by writing tests that exercise every code path in the monolithic method.
- Use `git stash` or a separate branch to preserve the original code.
- Extract methods one at a time, running tests after each extraction.
- Use IDE refactoring tools (Extract Method) for safety.
- Commit after each successful refactoring step.

## Interview Questions

1. **What is the difference between unit tests, integration tests, and end-to-end tests?**
   Unit tests verify individual methods/classes in isolation, usually with mocks for dependencies. Integration tests verify multiple components work together (e.g., service + repository + database). End-to-end tests verify the entire system through its external interface (HTTP requests, UI). Unit tests are fast and numerous; integration tests are slower and fewer; E2E tests are slowest and test critical user journeys.

2. **What is test coverage and is 100% coverage realistic?**
   Test coverage measures the percentage of code paths exercised by tests (line, branch, method). 100% coverage is achievable for pure logic but impractical for UI, infrastructure, and boilerplate code. High coverage (80%+) reduces regression risk but doesn't guarantee correctness—tests can pass while the requirements are wrong. Coverage is a tool, not a goal.

3. **What is mocking and when should you avoid it?**
   Mocking replaces real dependencies with controlled fakes to isolate the code under test. Use mocks for slow, non-deterministic, or external dependencies (databases, APIs, filesystems). Avoid mocking value objects, simple data classes, or the code under test itself. Over-mocking creates tests that verify implementation details rather than behavior, making refactoring fragile.

4. **Explain the Arrange-Act-Assert (AAA) pattern.**
   AAA structures every test into three sections: **Arrange** sets up objects and preconditions. **Act** calls the method under test. **Assert** verifies the result. This pattern improves readability and ensures each test has a single focus. Example: Arrange=create calculator, Act=add(2,3), Assert=assertEquals(5, result).

5. **What are parameterized tests and when should you use them?**
   Parameterized tests run the same test logic with different inputs and expected outputs. Use them when testing a method with many input combinations (e.g., boundary values, valid/invalid inputs). JUnit 5 provides `@ParameterizedTest` with `@ValueSource`, `@CsvSource`, `@MethodSource`, and `@CsvFileSource`. They reduce code duplication compared to writing individual test methods.

6. **What is the difference between `@BeforeAll`, `@BeforeEach`, `@AfterEach`, and `@AfterAll`?**
   `@BeforeAll` runs once before all tests in the class (must be static). `@BeforeEach` runs before each test method. `@AfterEach` runs after each test (cleanup). `@AfterAll` runs once after all tests (static, teardown). Use `BeforeEach` for test setup that needs fresh state per test. Use `BeforeAll` for expensive one-time setup like database connections.

7. **What is TDD and what are its benefits?**
   Test-Driven Development follows Red-Green-Refactor: write a failing test, implement minimal code to pass, then refactor. Benefits: tests drive design (write testable code first), provides safety net for refactoring, produces comprehensive test coverage naturally, and forces you to think about requirements before implementation. TDD is especially valuable for complex business logic.

8. **How do you test exception handling in Java?**
   Use `assertThrows(ExceptionClass.class, () -> codeThatThrows())` in JUnit 5. Capture the exception to verify its message: `Exception e = assertThrows(...); assertEquals("msg", e.getMessage())`. Use `@ParameterizedTest` with `@NullAndEmptySource` for null/empty input tests. Use `assertAll` to group multiple exception assertions for the same scenario.

9. **What is test isolation and why does it matter?**
   Test isolation means each test runs independently without depending on other tests' state or execution order. It matters because non-isolated tests produce flaky results (sometimes pass, sometimes fail). Achieve isolation by: using fresh objects per test, cleaning up in `@AfterEach`, using in-memory databases with transactions, and avoiding static mutable state.

10. **What are the best practices for writing maintainable tests?**
    - Follow naming convention: `testMethod_Scenario_ExpectedBehavior`
    - One assertion concept per test (not necessarily one assert call)
    - Use Arrange-Act-Assert structure
    - Avoid testing implementation details; test behavior
    - Keep tests fast (mock slow dependencies)
    - Use descriptive test names that explain the scenario
    - Don't test third-party libraries; trust their tests

## Pitfalls

1. **Testing Implementation Details** — Mocking internal classes and verifying private method calls creates fragile tests that break during refactoring. Test the public API behavior instead.

2. **Tetris Code in Tests** — Writing tests that depend on specific execution order (test2 depends on test1 passing). Tests must be independently runnable in any order.

3. **Over-Mocking** — Mocking everything including value objects creates tests that verify mocks, not real behavior. Use real objects when they're fast and deterministic.

4. **Ignoring Flaky Tests** — Tests that sometimes pass and sometimes fail erode confidence. Fix them immediately—common causes: shared state, timing issues, external dependencies, random data.

5. **Test Duplication** — Copying test logic across methods instead of using parameterized tests. Each copy must be maintained independently, increasing maintenance burden.

6. **Slow Test Suites** — Tests taking minutes instead of seconds discourage frequent running. Profile slow tests, mock external services, use in-memory databases, and parallelize independent tests.

7. **Asserting Too Much** — Tests with 20+ assertions are hard to understand when they fail. Split into smaller focused tests. Use `assertAll` for related assertions on the same object.

8. **Magic Numbers in Tests** — `assertEquals(42, result)` without explanation. Use named constants or inline comments: `assertEquals(AGE_THRESHOLD, result)` or `assertEquals(42, result, "Default retirement age")`.

## Performance

1. **Test Execution Speed** — Unit tests should run in milliseconds. If a test takes >100ms, check for I/O, Thread.sleep, or real network calls. Mock these dependencies.

2. **Test Suite Parallelization** — JUnit 5 supports parallel test execution. Configure `junit-platform.properties` with `junit.jupiter.execution.parallel.enabled=true`. Group tests that share resources into the same fork.

3. **Mock Creation Overhead** — Mockito creates proxies via CGLIB/ByteBuddy. Creating 10,000 mocks takes ~500ms. Reuse mock instances across tests when appropriate (don't recreate in each test method if stateless).

4. **Database Test Speed** — Use H2 in-memory database instead of PostgreSQL for unit tests. For integration tests, use Testcontainers with reusable containers (`@Testcontainers` with `reuse=true`). Connection pooling in tests reduces setup time.

5. **Parameterized Test Performance** — `@ParameterizedTest` with many arguments can be slower than individual tests due to reflection overhead. For 100+ combinations, consider `@MethodSource` with `Stream` for lazy evaluation.

6. **Assertion Library Performance** — AssertJ's fluent assertions are slightly slower than JUnit's built-in assertions. For performance-critical test suites (10,000+ assertions), use JUnit assertions. AssertJ provides better error messages.

7. **Test Discovery Overhead** — Spring Boot's `@SpringBootTest` loads the full application context, taking seconds. Use `@WebMvcTest` or `@DataJpaTest` for focused tests that load only needed beans.

8. **JaCoCo Coverage Impact** — Enabling JaCoCo adds ~10-15% to test execution time. Run coverage analysis in CI only, not during local development.

## Examples

```java
// Unit Test with AAA Pattern
@Test
void add_PositiveNumbers_ReturnsSum() {
    // Arrange
    Calculator calc = new Calculator();
    
    // Act
    int result = calc.add(2, 3);
    
    // Assert
    assertEquals(5, result);
}

// Parameterized Test
@ParameterizedTest
@CsvSource({"1,2,3", "4,5,9", "-1,1,0", "0,0,0"})
void add_MultipleInputs_ReturnsCorrectSum(int a, int b, int expected) {
    Calculator calc = new Calculator();
    assertEquals(expected, calc.add(a, b));
}

// Exception Test
@Test
void divide_ByZero_ThrowsArithmeticException() {
    Calculator calc = new Calculator();
    ArithmeticException ex = assertThrows(
        ArithmeticException.class, 
        () -> calc.divide(10, 0)
    );
    assertEquals("/ by zero", ex.getMessage());
}

// Mock Test
@Test
void findById_ExistingUser_ReturnsUser() {
    UserRepository mockRepo = mock(UserRepository.class);
    when(mockRepo.findById(1L)).thenReturn(Optional.of(new User("Alice")));
    
    UserService service = new UserService(mockRepo);
    User user = service.findById(1L);
    
    assertEquals("Alice", user.getName());
    verify(mockRepo, times(1)).findById(1L);
}

// TDD Cycle Example (PasswordValidator)
@Test
void password_TooShort_IsInvalid() {
    PasswordValidator validator = new PasswordValidator();
    assertFalse(validator.isValid("abc"));  // Red
}

// After implementing minimum length check:
@Test
void password_MeetsMinLength_IsValid() {
    PasswordValidator validator = new PasswordValidator();
    assertTrue(validator.isValid("abcdef"));  // Green
}
```

## Internal Working

Test frameworks use reflection to discover and invoke test methods. JUnit 5 scans the classpath for classes with `@Test` methods, creates test instances via no-arg constructors, executes `@BeforeEach` setup, runs the test method, executes `@AfterEach` cleanup, and reports results to `TestEngine`. Mockito creates dynamic proxies using ByteBuddy, intercepts method calls in `InvocationHandler`, and records/stubs invocations in an internal registry. Spring's test context caches application contexts across test classes to speed up execution.

## Why This Concept Exists

Software testing exists because bugs are expensive. The cost of fixing a bug increases exponentially the later it's found: trivial in development, costly in production. Automated tests provide fast feedback during development, serve as documentation of expected behavior, enable safe refactoring, and catch regressions. Testing frameworks (JUnit, Mockito, AssertJ) reduce the boilerplate of writing tests, making it practical to maintain large test suites.

## Overview

Java testing encompasses unit testing (JUnit 5), mocking (Mockito), integration testing (Spring Boot Test), and code coverage (JaCoCo). JUnit 5 provides `@Test`, `@ParameterizedTest`, `@BeforeEach`, and `assertThrows` for test structure. Mockito enables isolation via `mock()`, `when().thenReturn()`, and `verify()`. TDD drives design through Red-Green-Refactor. Best practices include AAA structure, descriptive names, test isolation, and 80%+ coverage for business logic.

## References

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://site.mockito.org/)
- [AssertJ Core API](https://assertj.github.io/doc/)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/)
- [Test Driven Development: By Example (Kent Beck)](https://www.amazon.com/Test-Driven-Development-Kent-Beck/dp/0321146530)
- [Related: Continuous Integration](https://martinfowler.com/articles/continuousIntegration.html)
- [Related: Test Doubles (Mocks, Stubs, Fakes)](https://martinfowler.com/bliki/TestDouble.html)
