## xUnit Testing

Modern, community-driven testing framework for .NET with built-in support for asynchronous testing and data-driven tests.

## Overview

xUnit is the most popular testing framework for .NET, used by Microsoft for ASP.NET Core and EF Core. It provides a clean, extensible testing experience.

## Why It Matters

- Default testing framework for .NET projects
- Clean, attribute-based test structure
- Built-in async/await support
- Excellent parameterized testing
- Strong community and extensibility

## Key Concepts

- **[Fact]**: Test method with no parameters
- **[Theory]**: Parameterized test method
- **[InlineData]**: Data for theory tests
- **[ClassData]**: Complex data for theory tests
- **ITestOutputHelper**: Test output logging
- **IClassFixture<T>:** Shared test context
- **ICollectionFixture<T>:** Shared across test classes

## Core Topics

- Test class and method conventions
- Fact and Theory attributes
- Parameterized testing with InlineData
- Test fixtures for shared context
- Async test methods
- Exception testing
- Test output and logging

## Best Practices

- Use descriptive test method names
- Use [Theory] for multiple test cases
- Implement IClassFixture for shared setup
- Use Assert.Single for collection assertions
- Keep tests independent and isolated

## Hands-on Labs

- Write Fact and Theory tests
- Implement parameterized testing
- Use IClassFixture for shared context
- Test async methods

## Interview Questions

1. What is the difference between [Fact] and [Theory]?
2. How do you share test context between tests?
3. How do you test async methods in xUnit?

## References

- https://xunit.net/
- https://xunit.net/docs/getting-started/v3/bool
- https://learn.microsoft.com/dotnet/core/testing/unit-testing-using-xunit
