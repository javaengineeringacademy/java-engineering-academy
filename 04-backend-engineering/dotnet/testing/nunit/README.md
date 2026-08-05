## NUnit Testing

Mature, widely-used testing framework for .NET with rich assertion library and flexible test organization.

## Overview

NUnit is one of the oldest and most established testing frameworks for .NET. It provides a comprehensive set of attributes and assertions for test development.

## Why It Matters

- Mature and battle-tested
- Rich assertion library
- Flexible test organization
- Data-driven testing support
- Extensible through add-ons

## Key Concepts

- **[Test]**: Test method attribute
- **[TestCase]**: Parameterized test data
- **[SetUp] / [TearDown]**: Test lifecycle methods
- **[OneTimeSetUp]**: One-time setup per fixture
- **Assert**: Verification methods
- **TestContext**: Test execution context

## Core Topics

- Test class and method attributes
- Setup and teardown patterns
- Parameterized tests with TestCase
- Collection assertions
- Async test support
- Constraint model assertions

## Best Practices

- Use [SetUp] for per-test initialization
- Use [OneTimeSetUp] for expensive setup
- Prefer constraint model for assertions
- Use TestContext for output

## Hands-on Labs

- Write basic NUnit tests
- Implement parameterized tests
- Use setup and teardown methods
- Test async code with NUnit

## Interview Questions

1. What is the difference between [SetUp] and [OneTimeSetUp]?
2. How do parameterized tests work in NUnit?

## References

- https://nunit.org/
- https://docs.nunit.org/
- https://learn.microsoft.com/dotnet/core/testing/unit-testing-using-nunit
