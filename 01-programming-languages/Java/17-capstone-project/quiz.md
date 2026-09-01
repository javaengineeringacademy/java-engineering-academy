# Capstone Project Quiz

## Multiple Choice Questions

### Q1: What is the primary purpose of a capstone project?
A) Learn new Java features
B) Integrate all learned concepts into a real-world application
C) Pass a certification exam
D) Write documentation

**Answer: B**

### Q2: Which design pattern is most commonly used for object creation?
A) Singleton
B) Observer
C) Factory
D) Strategy

**Answer: C**

### Q3: What is the recommended test coverage target?
A) 50%
B) 60%
C) 70%
D) 80%+

**Answer: D**

### Q4: Which module is essential for handling configuration?
A) 02-OOP
B) 04-Collections
C) 01-Fundamentals
D) 06-Generics

**Answer: C**

### Q5: What is the best approach for distributed transactions?
A) Two-phase commit
B) Saga pattern
C) Global locks
D) Distributed transactions

**Answer: B**

## Short Answer Questions

### Q6: Explain the difference between unit and integration tests.
**Answer:** Unit tests verify individual components in isolation (mocked dependencies). Integration tests verify components working together with real dependencies (database, external services).

### Q7: Why is dependency injection important in production applications?
**Answer:** DI promotes loose coupling, testability, and maintainability. It enables mocking for testing, supports different configurations per environment, and follows the SOLID principles.

### Q8: Describe the benefits of using records over traditional classes.
**Answer:** Records provide immutability, auto-generated equals/hashCode/toString, concise syntax, and are ideal for data transfer objects. They reduce boilerplate and prevent accidental mutation.

## Scenario Questions

### Q9: How would you handle a requirement for real-time data processing?
**Answer:** Use virtual threads (Java 21+) for I/O-bound operations; implement reactive streams with Project Reactor; use message queues for decoupling; implement backpressure handling; monitor throughput with Micrometer.

### Q10: Describe your approach to designing a REST API for a capstone project.
**Answer:** Follow REST principles (resources, HTTP methods, status codes); implement versioning; use DTOs for request/response; implement pagination; add validation; use HATEOAS for discoverability; document with OpenAPI.

---

**Score:** __/10
**Time:** 30 minutes
