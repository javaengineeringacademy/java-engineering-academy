# JUnit 5 Quiz

## Question 1
Which annotation marks a method as a test in JUnit 5?

- A) @Test
- B) @TestMethod
- C) @TestCase
- D) @Run

**Answer: A**
**Explanation:** @Test is the JUnit 5 annotation that marks a method as a test case.

---

## Question 2
What is the correct order of lifecycle methods?

- A) @AfterAll → @BeforeAll → @BeforeEach → @AfterEach
- B) @BeforeAll → @BeforeEach → @AfterEach → @AfterAll
- C) @BeforeEach → @BeforeAll → @AfterAll → @AfterEach
- D) @BeforeAll → @AfterAll → @BeforeEach → @AfterEach

**Answer: B**
**Explanation:** BeforeAll runs once before all tests, BeforeEach runs before each test, AfterEach runs after each test, AfterAll runs once after all tests.

---

## Question 3
Which assertion checks that an exception is thrown?

- A) assertThrows
- B) assertException
- C) assertThrowsException
- D) expectException

**Answer: A**
**Explanation:** assertThrows takes an exception class and an executable, verifying the exception is thrown.

---

## Question 4
What does @DisplayName do?

- A) Sets the test class name
- B) Provides a readable name for test output
- C) Marks the test as important
- D) Enables test debugging

**Answer: B**
**Explanation:** @DisplayName provides a human-readable description shown in test reports.

---

## Question 5
Which annotation runs a method before each test?

- A) @BeforeAll
- B) @BeforeEach
- C) @Before
- D) @Setup

**Answer: B**
**Explanation:** @BeforeEach runs before each test method in the class.

---

## Question 6
What is the purpose of @Nested?

- A) Runs tests in parallel
- B) Groups related tests into inner classes
- C) Marks tests as mandatory
- D) Enables parameterized tests

**Answer: B**
**Explanation:** @Nested groups related tests using inner classes, improving organization and readability.

---

## Question 7
Which provides a value to a parameterized test?

- A) @ValueSource
- B) @ParameterSource
- C) @DataProvider
- D) @TestSource

**Answer: A**
**Explanation:** @ValueSource provides a single array of literal values for parameterized tests.

---

## Question 8
What does @Disabled do?

- A) Deletes the test
- B) Skips the test during execution
- C) Runs the test last
- D) Marks test for code review

**Answer: B**
**Explanation:** @Disabled skips test execution and reports it as disabled in test output.

---

## Question 9
Which is NOT a JUnit 5 lifecycle annotation?

- A) @BeforeAll
- B) @BeforeEach
- C) @BeforeMethod
- D) @AfterAll

**Answer: C**
**Explanation:** @BeforeMethod is a TestNG annotation. JUnit 5 uses @BeforeEach.

---

## Question 10
What is the purpose of @Tag?

- A) Adds comments to tests
- B) Filters which tests to run
- C) Groups tests by priority
- D) Enables code coverage

**Answer: B**
**Explanation:** @Tag allows selective test execution by filtering on tag names.
