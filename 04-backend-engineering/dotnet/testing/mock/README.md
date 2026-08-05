## Moq Mocking Library

Popular mocking framework for .NET creating test doubles for dependencies.

## Overview

Moq is the most widely used mocking library for .NET, enabling creation of stubs, mocks, and fakes for unit testing. It uses lambda expressions for intuitive setup.

## Why It Matters

- Isolates unit tests from dependencies
- Verifies interactions between components
- Simple, fluent API
- Works with any .NET testing framework
- Enables testing of complex dependency graphs

## Key Concepts

- **Mock<T>:** Creates a mock object
- **Setup**: Defines expected behavior
- **Returns**: Specifies return values
- **Verify**: Confirms method was called
- **It.IsAny<T>:** Argument matcher
- **Callback**: Side effect on method call
- **Strict vs Loose**: Mocking behavior

## Core Topics

- Creating mocks and stubs
- Setting up return values
- Verifying method calls
- Argument matchers (IsAny, Is, It.Is)
- Callback for side effects
- Sequential return values
- Async method mocking
- Property mocking

## Best Practices

- Mock interfaces, not concrete classes
- Use strict mocking for critical paths
- Verify critical interactions
- Avoid over-mocking
- Use It.IsAny<T> carefully

## Hands-on Labs

- Create mocks for service dependencies
- Verify method call frequency
- Mock async methods
- Mock property getters and setters
- Use callback for sequence testing

## Interview Questions

1. What is the difference between a mock and a stub?
2. How do you verify a method was called?
3. When should you use strict vs loose mocking?

## References

- https://github.com/moq/moq4
- https://github.com/moq/moq4/wiki/Quickstart
- https://learn.microsoft.com/dotnet/core/testing/
