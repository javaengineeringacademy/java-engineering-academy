# Integration Testing Quiz

## Question 1
What does integration testing verify?

- A) Individual method correctness
- B) Interaction between components
- C) User interface
- D) Database performance

**Answer: B**
**Explanation:** Integration tests verify that multiple components work together correctly.

---

## Question 2
Which is NOT typically used in integration tests?

- A) Real database
- B) Mock objects
- C) REST APIs
- D) Message queues

**Answer: B**
**Explanation:** Integration tests typically use real dependencies; mocks are for unit tests.

---

## Question 3
What is a test container?

- A) A physical container
- B) Lightweight Docker container for testing
- C) Maven container
- D) Java Collection

**Answer: B**
**Explanation:** Test containers provide disposable Docker containers for integration testing.

---

## Question 4
What does @SpringBootTest test?

- A) Spring Boot application
- B) Only controllers
- C) Only services
- D) Only repositories

**Answer: A**
**Explanation:** @SpringBootTest loads the full Spring context for integration testing.

---

## Question 5
What is the purpose of @Sql in integration tests?

- A) Execute SQL queries
- B) Set up test data before tests
- C) Clean up after tests
- D) Both B and C

**Answer: D**
**Explanation:** @Sql can execute setup and teardown scripts for test data management.

---

## Question 6
Why are integration tests slower than unit tests?

- A) They run in parallel
- B) They involve real I/O operations
- C) They use more assertions
- D) They test fewer things

**Answer: B**
**Explanation:** Integration tests involve database, network, or file I/O which is slower than in-memory unit tests.

---

## Question 7
What is an end-to-end test?

- A) Tests one component
- B) Tests entire system from outside
- C) Tests database only
- D) Tests UI only

**Answer: B**
**Explanation:** E2E tests simulate real user interactions through the entire system.

---

## Question 8
What is contract testing?

- A) Testing legal contracts
- B) Verifying API contracts between services
- C) Testing database schema
- D) Testing UI contracts

**Answer: B**
**Explanation:** Contract testing verifies that API producers and consumers agree on the interface.

---

## Question 9
What does @TestPropertySource do?

- A) Sets test properties
- B) Overrides application properties
- C) Both A and B
- D) Neither

**Answer: C**
**Explanation:** @TestPropertySource overrides application properties for test configuration.

---

## Question 10
When should integration tests run in CI/CD?

- A) Only on weekends
- B) After unit tests pass
- C) Before unit tests
- D) Never

**Answer: B**
**Explanation:** Integration tests run after unit tests to ensure basic correctness before testing interactions.
