# Testing Fundamentals Quiz

## Question 1
What does the "A" in AAA pattern stand for?

- A) Arrange
- B) Act
- C) Assert
- D) Analyze

**Answer: A**
**Explanation:** AAA = Arrange (setup), Act (execute), Assert (verify). This is the standard test structure.

---

## Question 2
Which testing principle states that finding and fixing defects is useless if the system doesn't meet requirements?

- A) Early testing saves time
- B) Defects cluster together
- C) Absence-of-errors fallacy
- D) The pesticide paradox

**Answer: C**
**Explanation:** The absence-of-errors fallacy means verifying the system is built right, but not that the right system was built.

---

## Question 3
According to the testing pyramid, what is the ideal ratio of unit to integration to E2E tests?

- A) 10% unit, 20% integration, 70% E2E
- B) 70% unit, 20% integration, 10% E2E
- C) 50% unit, 30% integration, 20% E2E
- D) All types should be equal

**Answer: B**
**Explanation:** Unit tests are fast and cheap (70%), integration tests verify interactions (20%), E2E tests are expensive (10%).

---

## Question 4
What makes a test "self-validating"?

- A) It has no external dependencies
- B) It produces a clear pass/fail result
- C) It runs quickly
- D) It tests only one thing

**Answer: B**
**Explanation:** A self-validating test has a binary outcome—pass or fail—without manual interpretation.

---

## Question 5
What is the "pesticide paradox" in testing?

- A) Tests should be written quickly
- B) Repeated tests stop finding new bugs
- C) More tests always mean better quality
- D) Tests should be run in isolation

**Answer: B**
**Explanation:** Running the same tests repeatedly yields diminishing returns. Tests must be reviewed and updated regularly.

---

## Question 6
Which test type tests individual methods/classes in isolation?

- A) Integration test
- B) E2E test
- C) Unit test
- D) System test

**Answer: C**
**Explanation:** Unit tests verify individual components in isolation with no external dependencies.

---

## Question 7
What does the "F" in FIRST principles stand for?

- A) Fast
- B) Functional
- C) Formal
- D) Fixed

**Answer: A**
**Explanation:** FIRST = Fast, Independent, Repeatable, Self-validating, Timely.

---

## Question 8
When should you write tests in TDD?

- A) After implementation
- B) Before implementation
- C) Only when bugs are found
- D) During code review

**Answer: B**
**Explanation:** TDD requires writing tests first, then implementing code to make them pass.

---

## Question 9
What is the primary benefit of testing?

- A) It replaces code reviews
- B) It proves code is bug-free
- C) It catches bugs early and enables safe refactoring
- D) It eliminates the need for documentation

**Answer: C**
**Explanation:** Testing catches bugs early, provides a safety net for refactoring, and serves as living documentation.

---

## Question 10
What is a "flaky test"?

- A) A test that always fails
- B) A test that sometimes passes and sometimes fails without code changes
- C) A test that runs too slowly
- D) A test with too many assertions

**Answer: B**
**Explanation:** Flaky tests produce inconsistent results, often due to timing, shared state, or external dependencies.
