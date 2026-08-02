# Module 11: Testing

## Overview

This module covers comprehensive software testing methodologies and practices in Java. Students will learn unit testing with JUnit 5, mocking frameworks, integration testing, test-driven development, and best practices for building reliable, maintainable test suites.

## Learning Objectives

By the end of this module, you will be able to:

- Write effective unit tests using JUnit 5 features
- Create mocks and stubs using Mockito framework
- Design integration and end-to-end tests
- Apply test-driven development (TDD) methodology
- Implement code coverage and test metrics
- Use advanced testing patterns and anti-patterns
- Build automated test pipelines

## Prerequisites

- [Module 10: Design Patterns](../10-design-patterns/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [Testing Fundamentals](01-testing-fundamentals/) | 2 hours | Testing principles, test pyramid, AAA pattern |
| 02 | [JUnit 5 Basics](02-junit5/) | 3 hours | Annotations, assertions, test lifecycle |
| 03 | [JUnit 5 Advanced](03-junit5-advanced/) | 3 hours | Parameterized tests, extensions, nested tests |
| 04 | [Mockito Basics](04-mockito/) | 3 hours | Mocking, stubbing, verification |
| 05 | [Mockito Advanced](05-mockito-advanced/) | 2 hours | Argument captors, spy, partial mocking |
| 06 | [Integration Testing](06-integration-testing/) | 3 hours | Spring context, TestContainers, database tests |
| 07 | [Unit Testing](07-unit-testing/) | 2 hours | Best practices, test isolation, determinism |
| 08 | [Test Design](08-test-design/) | 2 hours | Test smells, refactoring tests, patterns |
| 09 | [Code Coverage](09-code-coverage/) | 2 hours | JaCoCo, coverage metrics, mutation testing |
| 10 | [TDD](10-tdd/) | 3 hours | Red-green-refactor, TDD workflow |
| 11 | [BDD](11-bdd/) | 2 hours | Cucumber, Gherkin, behavior-driven testing |
| 12 | [API Testing](12-api-testing/) | 2 hours | REST Assured, contract testing |
| 13 | [Performance Testing](13-performance-testing/) | 3 hours | JMeter, Gatling, load testing |
| 14 | [Test Automation](14-test-automation/) | 2 hours | CI/CD integration, test reporting |

## Key Concepts

- Test doubles: mocks, stubs, fakes, spies
- Test isolation and determinism
- Coverage metrics and quality gates
- Mutation testing and test effectiveness
- Contract testing for microservices

## Enterprise Applications

Robust testing practices are essential for maintaining code quality, enabling continuous integration, and ensuring reliable deployments in enterprise environments with complex business logic and multiple integration points.

## Estimated Total Time

**34 hours**

## Module Project

Build a **Test Suite for E-Commerce Application** that:
- Implements comprehensive unit tests for business logic
- Creates integration tests with test containers
- Applies TDD for new feature development
- Generates code coverage reports
- Demonstrates performance testing scenarios

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://site.mockito.org/)
- [Testing Spring Boot](https://spring.io/guides/gs/testing-web/)

**Previous Module**: [Module 10: Design Patterns](../10-design-patterns/)
**Next Module**: [Module 12: Build Tools](../12-build-tools/)