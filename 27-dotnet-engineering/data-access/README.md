## Data Access in .NET

Techniques and frameworks for accessing databases and data stores in .NET applications.

## Overview

.NET provides multiple data access approaches from low-level ADO.NET to high-level ORMs like Entity Framework Core. Choosing the right approach depends on performance needs, complexity, and team expertise.

## Why It Matters

- Data access is central to most applications
- Performance varies significantly between approaches
- ORM choice affects architecture and maintainability
- Proper data access patterns prevent common bugs

## Key Concepts

- **ADO.NET**: Low-level, direct database access
- **Entity Framework Core**: Modern ORM with LINQ support
- **Dapper**: Micro-ORM with focus on performance
- **Repository Pattern**: Abstraction over data access
- **Unit of Work**: Transaction management pattern
- **Connection Pooling**: Reusing database connections
- **Migration**: Schema evolution management

## Core Topics

- ADO.NET fundamentals (Connection, Command, DataReader)
- Entity Framework Core and DbContext
- Dapper for high-performance queries
- Repository and Unit of Work patterns
- Connection management and pooling
- Database migrations and schema management
- Query optimization and N+1 prevention

## Best Practices

- Use connection pooling for performance
- Implement repository pattern for testability
- Use async data access methods
- Avoid N+1 query problems
- Use migrations for schema management
- Cache frequently accessed, rarely changing data

## Hands-on Labs

- Implement data access with ADO.NET
- Build a repository pattern with EF Core
- Optimize queries with Dapper
- Set up database migrations

## Interview Questions

1. What are the differences between ADO.NET, EF Core, and Dapper?
2. How do you prevent N+1 query problems?
3. What is the Unit of Work pattern?
4. When should you use raw SQL over an ORM?

## References

- https://learn.microsoft.com/dotnet/standard/data/
- https://learn.microsoft.com/ef/core/
- https://learn.microsoft.com/dotnet/api/system.data.sqlclient
