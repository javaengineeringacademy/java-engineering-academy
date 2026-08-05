## Integration Testing in .NET

Testing component interactions using WebApplicationFactory for real HTTP testing without external dependencies.

## Overview

Integration tests verify that multiple components work together correctly. ASP.NET Core provides WebApplicationFactory for testing without spinning up a real web server.

## Why It Matters

- Tests real application behavior
- Caches issues between unit and end-to-end tests
- No external dependencies needed
- Fast and reliable
- Tests the full request pipeline

## Key Concepts

- **WebApplicationFactory**: In-memory test server
- **TestServer**: Low-level test server
- **IClassFixture<T>:** Shared test context
- **HttpClient**: Test client for requests
- **Custom WebApplicationFactory**: Configuring test dependencies

## Core Topics

- Setting up WebApplicationFactory
- Configuring test services
- Making HTTP requests in tests
- Testing authentication in integration tests
- Database testing with Testcontainers
- Test data seeding
- Asserting responses

## Best Practices

- Use WebApplicationFactory for API testing
- Mock external services, not databases
- Seed test data for consistent tests
- Use Testcontainers for database tests
- Test error scenarios

## Hands-on Labs

- Set up WebApplicationFactory
- Test API endpoints
- Configure test database
- Test authenticated endpoints
- Mock external service dependencies

## Interview Questions

1. How does WebApplicationFactory work?
2. When should you use integration tests vs unit tests?
3. How do you test authenticated endpoints?

## References

- https://learn.microsoft.com/aspnet/core/test/integration-tests
- https://learn.microsoft.com/dotnet/core/testing/
- https://testcontainers.com/
