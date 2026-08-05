## MSTest Framework

Microsoft's built-in testing framework integrated with Visual Studio and .NET SDK.

## Overview

MSTest is Microsoft's official testing framework, fully integrated with Visual Studio. It is the default testing framework for new projects created in Visual Studio.

## Why It Matters

- Built into Visual Studio
- Simple, familiar syntax
- Good Visual Studio integration
- Data-driven testing support
- No additional packages needed

## Key Concepts

- **[TestMethod]**: Test method attribute
- **[TestClass]**: Test class attribute
- **[DataRow]**: Parameterized test data
- **[DataTestMethod]**: Parameterized test method
- **CollectionAssert**: Collection verification
- **StringAssert**: String comparison methods

## Core Topics

- Test class and method conventions
- Data-driven testing with DataRow
- Deployment items for test data
- Ordered test execution
- Unit test templates

## Best Practices

- Use [DataRow] for parameterized tests
- Use CollectionAssert for collection comparisons
- Keep tests simple and focused
- Use Test Explorer for running tests

## Hands-on Labs

- Write basic MSTest tests
- Implement data-driven tests
- Test ASP.NET Core controllers
- Run tests in CI/CD pipeline

## Interview Questions

1. How does MSTest differ from xUnit?
2. What is [DataTestMethod] vs [DataRow]?

## References

- https://learn.microsoft.com/visualstudio/test/
- https://learn.microsoft.com/dotnet/core/testing/unit-testing-using-mstest
- https://github.com/microsoft/testfx
