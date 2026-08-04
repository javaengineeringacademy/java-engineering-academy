# 12 - Testing

## Overview

Software testing verifies that applications behave correctly and meet requirements. A comprehensive testing strategy combines multiple testing types at different levels to ensure quality, reliability, and maintainability.

## Testing Types

### 1. Unit Testing
Testing individual components in isolation.

- **JUnit 5** - Java testing framework
- **Hamcrest** - Matcher-based assertions
- **AssertJ** - Fluent assertion library
- **Mockito** - Mocking framework
- **EasyMock** - Mock object library

### 2. Integration Testing
Testing interactions between components.

- **Spring Boot Test** - Spring context testing
- **Testcontainers** - Docker-based test infrastructure
- **WebTestClient** - Reactive web testing
- **WireMock** - HTTP stubbing
- **REST Assured** - API testing
- **Contract Testing** - Pact-based contracts
- **Chaos Engineering** - Resilience testing

### 3. End-to-End Testing
Testing complete user workflows.

- **Cucumber** - BDD with Gherkin
- **Serenity BDD** - Enhanced BDD reporting
- **Playwright** - Modern browser automation
- **Selenium** - WebDriver-based testing
- **Appium** - Mobile testing
- **Cypress** - JavaScript E2E testing

### 4. Performance Testing
Testing system performance under load.

- **JMeter** - Load testing tool
- **Gatling** - Scala-based performance testing
- **k6** - Modern load testing
- **Locust** - Python-based load testing
- **wrk** - HTTP benchmarking

### 5. Security Testing
Identifying security vulnerabilities.

- **OWASP ZAP** - Dynamic security testing
- **Dependency Check** - Vulnerability scanning
- **Snyk** - Security analysis
- **Bandit** - Python security linting
- **Brakeman** - Rails security scanner

### 6. BDD (Behavior-Driven Development)
Business-readable specifications.

- **Cucumber** - Gherkin-based BDD
- **SpecFlow** - .NET BDD framework
- **Fitness Functions** - Architecture testing

### 7. Mutation Testing
Testing the tests themselves.

- **PITest** - Java mutation testing
- **Theory** - Mutation testing concepts

### 8. Test Design
Patterns and best practices for test code.

- **Patterns** - AAA, Factory, Builder patterns
- **Anti-Patterns** - Common test mistakes
- **Best Practices** - Testing guidelines
- **Test Pyramid** - Testing strategy

## Directory Structure

```
12-testing/
├── unit-testing/
│   ├── junit5/
│   ├── hamcrest/
│   ├── assertj/
│   ├── mockito/
│   ├── easymock/
│   └── structured-testing/
├── integration-testing/
│   ├── spring-boot-test/
│   ├── testcontainers/
│   ├── webtestclient/
│   ├── wiremock/
│   ├── rest-assured/
│   ├── spring-batch-test/
│   ├── contract/
│   └── chaos-engineering/
├── e2e-testing/
│   ├── cucumber/
│   ├── serenity/
│   ├── playwright/
│   ├── selenium/
│   ├── appium/
│   ├── cypress/
│   └── behave/
├── performance-testing/
│   ├── jmeter/
│   ├── gatling/
│   ├── wrk/
│   ├── k6/
│   ├── locust/
│   ├── blaze-meter/
│   └── load-testing/
├── security-testing/
│   ├── owasp-zap/
│   ├── dependency-check/
│   ├── snyk/
│   ├── bandit/
│   ├── brakeman/
│   └── sonarqube/
├── tdd/
│   ├── basics/
│   ├── approaches/
│   └── benefits/
├── bdd/
│   ├── cucumber/
│   ├── specflow/
│   └── fitness-functions/
├── mutation-testing/
│   ├── pitest/
│   └── theory/
└── test-design/
    ├── patterns/
    ├── anti-patterns/
    ├── best-practices/
    └── test-pyramid/
```

## The Testing Pyramid

```
         /\
        /  \        E2E Tests (Few)
       /    \       - Critical user journeys
      /------\
     /        \     Integration Tests (Some)
    /          \    - Component interactions
   /------------\
  /              \  Unit Tests (Many)
 /                \ - Individual components
/------------------\
```

| Level | Count | Speed | Cost | Confidence |
|-------|-------|-------|------|------------|
| Unit | Many | Fast | Low | Component |
| Integration | Some | Medium | Medium | Interaction |
| E2E | Few | Slow | High | System |

## Testing Best Practices

1. **FIRST Principles**
   - **Fast** - Tests should run quickly
   - **Independent** - Tests shouldn't depend on each other
   - **Repeatable** - Same result every time
   - **Self-Validating** - Pass or fail automatically
   - **Timely** - Written at the right time (TDD)

2. **AAA Pattern**
   ```java
   @Test
   void shouldCalculateTotal() {
       // Arrange
       Order order = new Order();
       order.addItem(new Item("Book", 29.99));
       
       // Act
       double total = order.calculateTotal();
       
       // Assert
       assertThat(total).isEqualTo(29.99);
   }
   ```

3. **Test Naming Convention**
   - `should_ExpectedBehavior_When_Condition`
   - `given_Precondition_when_Action_then_ExpectedResult`

4. **Avoid Test Smells**
   - Test interdependence
       implementation details
   - Slow tests
   - Flaky tests
   - Excessive mocking

## Test Automation

### CI/CD Integration
```yaml
# GitHub Actions example
test:
  steps:
    - name: Unit Tests
      run: mvn test
    - name: Integration Tests
      run: mvn verify -P integration-tests
    - name: Security Scan
      run: mvn org.owasp:dependency-check-maven:check
    - name: Mutation Tests
      run: mvn org.pitest:pitest-maven:mutationCoverage
```

### Coverage Tools
- **JaCoCo** - Java code coverage
- **SonarQube** - Quality gate enforcement
- **Codecov** - Coverage reporting

## References

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Test Driven Development - Kent Beck](https://www.oreilly.com/library/view/test-driven-development/0321146530/)
- [xUnit Test Patterns - Gerard Meszaros](https://www.amazon.com/xUnit-Test-Patterns-Refactoring-Test-Code/dp/0131495054)
- [Testing Microservices - Sam Newman](https://samnewman.io/books/building_microservices_2nd_edition/)
