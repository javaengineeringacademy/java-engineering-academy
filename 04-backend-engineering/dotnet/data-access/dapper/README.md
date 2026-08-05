## Dapper

High-performance micro-ORM for .NET providing fast data access with minimal overhead.

## Overview

Dapper is a lightweight micro-ORM created by the Stack Overflow team. It extends IDbConnection with methods for executing queries and mapping results to objects with minimal performance overhead.

## Why It Matters

- Exceptional performance (close to raw ADO.NET)
- Simple API with minimal learning curve
- No configuration or entity mapping required
- SQL control with object mapping convenience
- Used by high-traffic sites like Stack Overflow

## Key Concepts

- **Query Methods**: Query, QueryFirst, QuerySingle, QueryMultiple
- **Execute Methods**: Execute for non-query commands
- **Dynamic Results**: Mapping to dynamic objects
- **Multi-Mapping**: Join results to multiple types
- **Bulk Operations**: Insert, Update, Delete extensions
- **Type Handlers**: Custom type conversion

## Core Topics

- Basic CRUD operations
- Parameterized queries
- Multi-mapping for joins
- Stored procedure execution
- Transactions with Dapper
- Type handlers for custom types
- Async operations
- Dapper.Contrib for simple CRUD

## Best Practices

- Always use parameterized queries
- Use QueryMultiple for related data
- Implement custom TypeHandlers for complex types
- Use transactions for related operations
- Consider Dapper for read-heavy scenarios

## Hands-on Labs

- Implement CRUD operations with Dapper
- Use QueryMultiple for complex joins
- Create custom TypeHandlers
- Build a repository pattern with Dapper

## Interview Questions

1. How does Dapper differ from Entity Framework Core?
2. What are the performance characteristics of Dapper?
3. How do you handle complex types with Dapper?
4. When should you choose Dapper over EF Core?

## References

- https://github.com/DapperLib/Dapper
- https://learn.microsoft.com/dotnet/api/system.data.sqlclient
- https://dapper-tutorial.net/
