# JUnit 5 Advanced Quiz

## Question 1
What annotation creates a parameterized test?

- A) @ParameterizedTest
- B) @Parameterized
- C) @TestWithParams
- D) @DataDriven

**Answer: A**
**Explanation:** @ParameterizedTest is the JUnit 5 annotation for parameterized testing.

---

## Question 2
Which source provides a stream of arguments?

- A) @ValueSource
- B) @MethodSource
- C) @CsvSource
- D) @FileSource

**Answer: B**
**Explanation:** @MethodSource references a static method that returns a Stream of Arguments.

---

## Question 3
What is a Dynamic Test?

- A) Tests generated at runtime
- B) Tests that can fail randomly
- C) Tests with dynamic assertions
- D) Tests that change behavior

**Answer: A**
**Explanation:** Dynamic Tests are created at runtime by a @TestFactory method.

---

## Question 4
Which extension interface runs before each test?

- A) BeforeEachCallback
- B) BeforeAllCallback
- C) TestWatcher
- D) TestExecutionListener

**Answer: A**
**Explanation:** BeforeEachCallback's beforeEach method runs before each test method.

---

## Question 5
What does @TestFactory return?

- A) void
- B) Collection<DynamicTest>
- C) Stream<DynamicTest>
- D) Both B and C

**Answer: D**
**Explanation:** @TestFactory can return Collection, Iterable, Iterator, or Stream of DynamicTest.

---

## Question 6
What is the purpose of ExtensionContext.getStore()?

- A) Store test results
- B) Share state between extensions
- C) Cache test data
- D) All of the above

**Answer: D**
**Explanation:** Store provides a namespace-scoped map for sharing state within extension lifecycle.

---

## Question 7
Which annotation enables method ordering?

- A) @TestMethodOrder
- B) @Order
- C) @TestMethodOrder with @Order
- D) @RunInOrder

**Answer: C**
**Explanation:** @TestMethodOrder specifies the ordering strategy, @Order sets individual priorities.

---

## Question 8
What is a custom composed annotation?

- A) An annotation that combines multiple annotations
- B) An annotation created by the framework
- C) An annotation with parameters
- D) An annotation that runs tests

**Answer: A**
**Explanation:** Custom composed annotations combine existing annotations for reusable test configurations.

---

## Question 9
Which exception handler catches test failures?

- A) TestExecutionExceptionHandler
- B) ExceptionHandler
- C) TestFailureHandler
- D) AssertionFailureHandler

**Answer: A**
**Explanation:** TestExecutionExceptionHandler allows catching and handling exceptions during test execution.

---

## Question 10
What does @RepeatedTest do?

- A) Runs a test multiple times
- B) Repeats on failure
- C) Runs tests in parallel
- D) Loops test data

**Answer: A**
**Explanation:** @RepeatedTest runs the test method the specified number of times.
