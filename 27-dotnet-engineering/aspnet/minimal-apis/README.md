## Minimal APIs in .NET 6+

Lightweight API development model for building HTTP services with minimal ceremony.

## Overview

Minimal APIs provide a streamlined approach to building HTTP APIs without the overhead of MVC controllers. Introduced in .NET 6, they are ideal for microservices, serverless functions, and simple APIs.

## Why It Matters

- Less boilerplate than MVC controllers
- Faster startup and lower memory usage
- Ideal for microservices and serverless
- Built-in OpenAPI/Swagger support
- Full access to all ASP.NET Core features

## Key Concepts

- **Route Handlers**: Lambda-based request handling
- **Parameter Binding**: Automatic binding from route, query, body
- **Filters**: Endpoint-level filters for cross-cutting concerns
- **Typed Results**: Strongly-typed HTTP results
- **Route Groups**: Organize endpoints with shared prefixes
- **OpenAPI**: Built-in Swagger generation

## Core Topics

- Defining routes and handlers
- Parameter binding (route, query, body, header)
- Response types and status codes
- Filters and middleware per endpoint
- Route groups and organization
- Authentication and authorization
- OpenAPI/Swagger integration

## Best Practices

- Use route groups for API organization
- Apply endpoint filters for validation
- Use TypedResults for testability
- Return Results<T> for proper status codes
- Add OpenAPI for API documentation

## Hands-on Labs

- Build a CRUD API with Minimal APIs
- Implement route groups for versioning
- Add validation with endpoint filters
- Generate OpenAPI documentation

## Interview Questions

1. What are the advantages of Minimal APIs over MVC?
2. How does parameter binding work in Minimal APIs?
3. What are route groups and when should you use them?
4. How do you test Minimal API endpoints?

## References

- https://learn.microsoft.com/aspnet/core/fundamentals/minimal-apis/
- https://learn.microsoft.com/aspnet/core/fundamentals/minimal-apis/samples/
- https://learn.microsoft.com/aspnet/core/fundamentals/minimal-apis/openapi
