## Language Integrated Query (LINQ)

LINQ provides a unified query syntax for working with data from any source including collections, databases, XML, and more.

## Overview

LINQ is one of C#'s most powerful features, enabling declarative data querying with compile-time type safety and IntelliSense support. It supports both query syntax and method syntax (fluent API).

## Why It Matters

- Reduces boilerplate for data transformation
- Compile-time checked queries catch errors early
- Consistent API across different data sources
- Composable operators enable complex queries from simple parts
- Foundation for EF Core database queries

## Key Concepts

- **Query Syntax**: SQL-like syntax (from, where, select, orderby)
- **Method Syntax**: Extension method chains (Where, Select, OrderBy)
- **Deferred Execution**: Query is not executed until enumerated
- **Immediate Execution**: ToList, ToArray, Count, First trigger execution
- **IQueryable<T>:** Translates queries to database SQL
- **IGrouping**: Grouping operations and group-by semantics
- **Join Operations**: Join, GroupJoin, SelectMany for relationships

## Core Topics

- LINQ operators (filtering, projection, ordering, grouping, aggregation)
- Deferred vs immediate execution
- IQueryable vs IEnumerable
- Expression trees and query translation
- Custom LINQ providers
- Parallel LINQ (PLINQ)
- LINQ to Objects, XML, DataSet

## Best Practices

- Prefer method syntax for readability in complex queries
- Be aware of deferred execution to avoid multiple enumerations
- Use `AsQueryable()` carefully to avoid N+1 queries
- Materialize results with `ToList()` when needed for multiple operations
- Use `IQueryable` for database queries and `IEnumerable` for in-memory
- Avoid side effects inside LINQ query operators

## Hands-on Labs

- Build complex queries using LINQ method chains
- Implement a custom IQueryable provider
- Use PLINQ for parallel data processing
- Compare performance of deferred vs immediate execution
- Build a repository using IQueryable

## Interview Questions

1. What is the difference between deferred and immediate execution?
2. Explain IQueryable vs IEnumerable.
3. How does expression tree translation work in EF Core?
4. What are the most common LINQ operators?
5. How do you avoid N+1 query problems with LINQ?

## References

- https://learn.microsoft.com/dotnet/csharp/linq/
- https://learn.microsoft.com/dotnet/csharp/linq/standard-query-operators/
- https://learn.microsoft.com/dotnet/api/system.linq
