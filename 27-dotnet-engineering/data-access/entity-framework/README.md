## Entity Framework

Object-relational mapping framework for .NET with LINQ support, change tracking, and database migrations.

## Overview

Entity Framework is Microsoft's ORM for .NET. EF6 is the mature version for .NET Framework, while EF Core is the modern, cross-platform version for .NET Core and later.

## Why It Matters

- Eliminates most boilerplate data access code
- LINQ queries with compile-time type safety
- Automatic change tracking and save
- Database-agnostic with multiple providers
- Migration support for schema management

## Key Concepts

- **DbContext**: Represents a database session
- **DbSet<T>:** Collection of entities in the context
- **Entity State**: Added, Modified, Deleted, Unchanged, Detached
- **Conventions**: Default naming and mapping rules
- **Fluent API**: Code-based configuration
- **Migrations**: Schema evolution management

## Core Topics

- EF6 vs EF Core comparison
- DbContext configuration
- Querying with LINQ
- CRUD operations
- Relationships and navigation properties
- Database-first vs Code-first approach
- Migrations and schema management

## Best Practices

- Use asynchronous query methods
- Implement No-Tracking queries for read scenarios
- Use projection to select only needed columns
- Apply AsSplitQuery for complex joins
- Use transactions for multi-context operations

## Hands-on Labs

- Set up EF Core with Code-first approach
- Implement CRUD operations with DbContext
- Create and apply database migrations
- Optimize queries with projections

## Interview Questions

1. What are the differences between EF6 and EF Core?
2. How does change tracking work?
3. What is the difference between eager and lazy loading?

## References

- https://learn.microsoft.com/ef/ef6/
- https://learn.microsoft.com/ef/core/
- https://learn.microsoft.com/ef/core/querying/
