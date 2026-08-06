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

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
