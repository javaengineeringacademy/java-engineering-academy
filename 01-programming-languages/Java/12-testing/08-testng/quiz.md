# TestNG Quiz

## Question 1
Which annotation runs before each test method?

- A) @BeforeSuite
- B) @BeforeClass
- C) @BeforeMethod
- D) @BeforeTest

**Answer: C**
**Explanation:** @BeforeMethod runs before each @Test method in the class.

---

## Question 2
What does @DataProvider provide?

- A) Test configuration
- B) Test data for parameterized tests
- C) Mock objects
- D) Test reports

**Answer: B**
**Explanation:** @DataProvider returns a 2D array of test data for data-driven testing.

---

## Question 3
How do you run tests in parallel?

- A) @Test(parallel=true)
- B) XML configuration
- C) Both A and B
- D) Neither

**Answer: B**
**Explanation:** Parallel execution is configured in the TestNG XML suite file.

---

## Question 4
What does dependsOnMethods create?

- A) Method grouping
- B) Test dependencies
- C) Test priority
- D) Test timeout

**Answer: B**
**Explanation:** dependsOnMethods ensures specified methods run before the current test.

---

## Question 5
Which is NOT a TestNG annotation?

- A) @BeforeSuite
- B) @BeforeEach
- C) @AfterClass
- D) @AfterTest

**Answer: B**
**Explanation:** @BeforeEach is a JUnit 5 annotation. TestNG uses @BeforeMethod.

---

## Question 6
What is a TestNG group?

- A) A collection of test classes
- B) A category for test methods
- C) A thread pool
- D) A report type

**Answer: B**
- **Explanation:** Groups categorize tests for selective execution (e.g., "smoke", "regression").

---

## Question 7
How do you pass parameters to TestNG tests?

- A) @Parameters annotation + XML
- B) @DataProvider only
- C) Command line arguments
- D) System properties

**Answer: A**
**Explanation:** @Parameters reads values from XML configuration and injects them into test methods.

---

## Question 8
What is a TestNG listener?

- A) A method that listens for events
- B) A class that reacts to test lifecycle events
- C) A configuration file
- D) A test reporter

**Answer: B**
**Explanation:** Listeners implement interfaces to react to test events like start, finish, success, failure.

---

## Question 9
What does @Factory do?

- A) Creates test instances
- B) Provides test data
- C) Configures threads
- D) Generates reports

**Answer: A**
**Explanation:** @Factory creates test instances dynamically at runtime for data-driven testing.

---

## Question 10
How is TestNG suite configured?

- A) Only via annotations
- B) Via XML file
- C) Via command line only
- D) Via pom.xml only

**Answer: B**
**Explanation:** TestNG suites are configured using XML files that define test classes, groups, and parameters.
