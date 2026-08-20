# Mutation Testing Quiz

## Question 1
What is a "killed" mutant?

- A) Mutant that crashes
- B) Mutant detected by tests
- C) Mutant that survives
- D) Mutant that passes

**Answer: B**
**Explanation:** A killed mutant is detected by tests when they fail on the mutated code.

---

## Question 2
What does mutation score measure?

- A) Code coverage
- B) Test effectiveness
- C) Execution time
- D) Memory usage

**Answer: B**
**Explanation:** Mutation score measures how well tests detect code changes.

---

## Question 3
What is PIT (Pitest)?

- A) Code coverage tool
- B) Mutation testing framework
- C) Test runner
- D) IDE plugin

**Answer: B**
**Explanation:** PIT is a Java mutation testing framework.

---

## Question 4
What does a survived mutant indicate?

- A) Test is wrong
- B) Test doesn't detect the change
- C) Mutant is correct
- D) Code is buggy

**Answer: B**
**Explanation:** Survived mutants indicate tests that don't detect specific code changes.

---

## Question 5
How does PIT generate mutants?

- A) Modifying source code
- B) Modifying bytecode
- C) Modifying tests
- D) Modifying configuration

**Answer: B**
**Explanation:** PIT modifies bytecode to create mutants.

---

## Question 6
What is a "no coverage" mutant?

- A) Mutant with no tests
- B) Mutant in untested code
- C) Mutant that crashes
- D) Mutant with 100% coverage

**Answer: B**
**Explanation:** No coverage mutants are in code paths not executed by any test.

---

## Question 7
What mutation changes comparison operators?

- A) Return value mutation
- B) Condition boundary mutation
- C) Math operator mutation
- D) Void method mutation

**Answer: B**
**Explanation:** Condition boundary mutations change > to >=, < to <=, etc.

---

## Question 8
When should mutation testing run?

- A) During development
- B) In CI/CD pipeline
- C) Only on weekends
- D) Never

**Answer: B**
**Explanation:** Mutation testing should run in CI/CD to ensure test quality.

---

## Question 9
What is the relationship between coverage and mutation score?

- A) They are the same
- B) Coverage is necessary but not sufficient for high mutation score
- C) Mutation score is always lower
- D) They are unrelated

**Answer: B**
**Explanation:** High coverage doesn't guarantee high mutation score; tests must be effective.

---

## Question 10
How do you improve mutation score?

- A) Add more assertions
- B) Add tests for boundary conditions
- C) Test negative paths
- D) All of the above

**Answer: D**
**Explanation:** Improving mutation score requires comprehensive testing of all code paths.
