## MVC to Minimal APIs Migration

Migrating ASP.NET MVC applications to the modern Minimal APIs pattern in .NET 6+.

## Overview

MVC applications can be migrated to Minimal APIs for simpler, more performant API development. The migration involves converting controllers to route handlers.

## Why It Matters

- Reduced boilerplate code
- Better performance
- Simpler code organization
- Modern .NET patterns
- Ideal for microservices

## Key Concepts

- **Controller to Route Handler**: Direct mapping
- **Action Filters to Endpoint Filters**: Request processing
- **Model Binding**: Automatic parameter binding
- **Route Groups**: Organizing endpoints
- **Typed Results**: Strongly-typed responses

## Core Topics

- Converting controllers to route handlers
- Mapping action filters to endpoint filters
- Preserving route patterns
- Maintaining model validation
- Migrating authentication/authorization
- API versioning migration
- Testing migrated endpoints

## Best Practices

- Start with simple GET/POST endpoints
- Preserve existing route patterns
- Use route groups for organization
- Maintain existing validation logic
- Test each migrated endpoint

## Hands-on Labs

- Convert an MVC controller to Minimal API
- Map action filters to endpoint filters
- Implement route groups
- Test migrated endpoints

## Interview Questions

1. How do MVC controllers map to Minimal API handlers?
2. What replaces action filters in Minimal APIs?
3. How do you organize Minimal API endpoints?

## References

- https://learn.microsoft.com/aspnet/core/fundamentals/minimal-apis/
- https://learn.microsoft.com/aspnet/core/fundamentals/minimal-apis/migrate-mvc
- https://learn.microsoft.com/aspnet/core/fundamentals/minimal-apis/samples/
