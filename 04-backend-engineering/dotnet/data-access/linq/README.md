## LINQ Data Access

Using LINQ for data access across different providers including LINQ to SQL and LINQ to Entities.

## Overview

LINQ provides a unified query syntax across data sources. LINQ to SQL and LINQ to Entities translate LINQ queries to SQL, enabling type-safe database queries.

## Why It Matters

- Compile-time checked database queries
- Consistent query syntax across data sources
- Reduced SQL injection risk
- IntelliSense support for queries
- Foundation for EF Core LINQ queries

## Key Concepts

- **IQueryable<T>:** Translatable query expression
- **Expression Trees**: Query representations for translation
- **Provider Pattern**: Database-specific translation
- **Deferred Execution**: Query not executed until enumerated
- **Query Translation**: Converting LINQ to SQL

## Core Topics

- LINQ to SQL basics and limitations
- LINQ to Entities (EF6)
- EF Core LINQ query translation
- IQueryable vs IEnumerable usage
- Expression tree limitations
- Raw SQL fallback with LINQ

## Best Practices

- Use EF Core LINQ over LINQ to SQL
- Understand expression tree limitations
- Materialize results when needed
- Use raw SQL for complex queries
- Profile generated SQL

## Hands-on Labs

- Write LINQ queries for database access
- Compare IQueryable and IEnumerable behavior
- Debug EF Core query translation
- Use raw SQL alongside LINQ

## Interview Questions

1. What is the difference between IQueryable and IEnumerable?
2. How are expression trees used in LINQ to SQL?
3. What are the limitations of LINQ expression trees?

## References

- https://learn.microsoft.com/dotnet/csharp/linq/
- https://learn.microsoft.com/ef/core/querying/
- https://learn.microsoft.com/dotnet/api/system.linq.iqueryable-1
