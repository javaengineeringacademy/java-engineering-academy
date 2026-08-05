## EF Core Deep Dive

Advanced Entity Framework Core topics including performance optimization, global query filters, and advanced mapping.

## Overview

EF Core is the modern, lightweight, cross-platform ORM for .NET. Understanding its internals, performance patterns, and advanced features is essential for production applications.

## Why It Matters

- Performance optimization reduces database load
- Advanced mapping enables complex domain models
- Query optimization prevents N+1 and other issues
- Interceptors enable cross-cutting concerns

## Key Concepts

- **Interceptors**: Hook into EF Core operations
- **Owned Types**: Value object mapping
- **Table Splitting**: Sharing tables across entities
- **Global Query Filters**: Automatic query predicates
- **Shadow Properties**: Properties not in the entity class
- **Backing Fields**: Direct field access bypassing properties
- **Compiled Queries**: Pre-compiled LINQ queries

## Core Topics

- Interceptors for logging and auditing
- Value converters and value comparers
- Owned types and complex types
- Table splitting and entity splitting
- Global query filters for multi-tenancy
- Bulk operations (EFCore.BulkExtensions)
- Raw SQL queries
- Cosmos DB and other NoSQL providers

## Best Practices

- Use compiled queries for frequently executed queries
- Implement global query filters for multi-tenancy
- Use NoTracking for read-only queries
- Apply split queries for complex includes
- Use interceptors for auditing instead of overridden SaveChanges

## Hands-on Labs

- Implement audit logging with interceptors
- Add multi-tenancy with global query filters
- Optimize a slow query with projections and split queries
- Use owned types for value objects

## Interview Questions

1. How do EF Core interceptors work?
2. What are global query filters and when should you use them?
3. How do you optimize EF Core queries for performance?
4. What is the difference between split queries and single queries?

## References

- https://learn.microsoft.com/ef/core/interceptors/
- https://learn.microsoft.com/ef/core/modeling/value-conversions/
- https://learn.microsoft.com/ef/core/querying/split-queries
