# Java Testing Examples

Comprehensive examples demonstrating testing concepts, frameworks, and best practices.

## Files

| # | File | Topic | Key Concepts |
|---|------|-------|--------------|
| 1 | TestingFundamentalsDemo.java | Testing Fundamentals | AAA pattern, FIRST principles, test types |
| 2 | Junit5Demo.java | JUnit 5 Basics | Annotations, assertions, lifecycle |
| 3 | Junit5AdvancedDemo.java | JUnit 5 Advanced | Extensions, parameterized, nested tests |
| 4 | MockitoDemo.java | Mockito Basics | Mocking, stubbing, verification |
| 5 | MockitoAdvancedDemo.java | Mockito Advanced | Spying, argument matchers, callbacks |
| 6 | IntegrationTestingDemo.java | Integration Testing | Spring Boot Test, TestContainers, WebMvcTest |
| 7 | UnitTestingDemo.java | Unit Testing Patterns | Best practices, DI testing, TDD |

## How to Run

```bash
# Compile and run any example
javac -cp "lib/*" examples/TestingFundamentalsDemo.java
java -cp "lib/*:." academy.javaengineering.testing.TestingFundamentalsDemo

# Or use Maven/Gradle
mvn test -Dtest=TestingFundamentalsDemo
```

## Prerequisites

- Java 17+
- JUnit 5.10+
- Mockito 5.x
- Spring Boot 3.x (for integration examples)
