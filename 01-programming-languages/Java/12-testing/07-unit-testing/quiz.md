# Unit Testing Best Practices Quiz

## Question 1
What does the "I" in FIRST principles stand for?

- A) Important
- B) Independent
- C) Instant
- D) Intentional

**Answer: B**
**Explanation:** Unit tests should be independent - no test should depend on another test's state or execution order.

---

## Question 2
Why should you avoid testing private methods directly?

- A) They are too fast
- B) They are implementation details
- C) They cannot be accessed
- D) Both B and C

**Answer: D**
**Explanation:** Private methods are implementation details; test them through the public API they support.

---

## Question 3
What is a pure function?

- A) A function with no side effects
- B) A function that returns void
- C) A function with only one parameter
- D) A function that never fails

**Answer: A**
**Explanation:** Pure functions have no side effects and always return the same output for the same input.

---

## Question 4
What is the main purpose of unit tests?

- A) Replace documentation
- B) Find all bugs
- C) Provide fast feedback on code correctness
- D) Increase code complexity

**Answer: C**
**Explanation:** Unit tests provide fast feedback during development and serve as living documentation.

---

## Question 5
What makes a test "repeatable"?

- A) It can be run multiple times
- B) It produces the same result every time
- C) It has no random behavior
- D) All of the above

**Answer: D**
**Explanation:** Repeatable tests produce consistent results regardless of environment or execution order.

---

## Question 6
Why is test isolation important?

- A) Tests run faster
- B) Tests don't interfere with each other
- C) Tests use less memory
- D) Tests are easier to write

**Answer: B**
**Explanation:** Isolated tests ensure failures in one test don't cause cascading failures in others.

---

## Question 7
What is the recommended assertion style?

- A) Assert all conditions in one test
- B) One assertion per test (preferably)
- C) No assertions - just run the code
- D) Multiple unrelated assertions

**Answer: B**
**Explanation:** One assertion per test makes failures easier to identify and tests more focused.

---

## Question 8
What is a test fixture?

- A) A broken test
- B) Test data and setup used across tests
- C) A type of assertion
- D) A test configuration file

**Answer: B**
**Explanation:** Fixtures are pre-defined test data and setup used to ensure consistent test conditions.

---

## Question 9
When should you refactor test code?

- A) Never
- B) When it becomes duplicated or unclear
- C) Only when production code changes
- D) Only during code reviews

**Answer: B**
**Explanation:** Test code should be maintained like production code for readability and maintainability.

---

## Question 10
What is the test pyramid's recommended ratio?

- A) 10% unit, 20% integration, 70% E2E
- B) 70% unit, 20% integration, 10% E2E
- C) 50% unit, 30% integration, 20% E2E
- D) Equal distribution

**Answer: B**
**Explanation:** Most tests should be fast unit tests, fewer integration tests, and minimal E2E tests.
