## ADO.NET

Low-level data access framework providing direct database communication with Connection, Command, and DataReader objects.

## Overview

ADO.NET is the foundational data access technology in .NET, providing direct, unbuffered access to database resources. It offers maximum control and performance for data operations.

## Why It Matters

- Maximum performance and control
- Foundation for all higher-level data access
- Unbuffered streaming for large datasets
- Direct parameter binding for security
- Works with any ADO.NET-compatible database

## Key Concepts

- **DbConnection**: Database connection abstraction
- **DbCommand**: SQL command execution
- **DbDataReader**: Forward-only, read-only data streaming
- **DbDataAdapter**: Bridge between DataSet and database
- **Parameter**: Preventing SQL injection
- **Transaction**: Atomic database operations

## Core Topics

- Connection management and pooling
- Parameterized queries
- DbDataReader usage patterns
- Transaction management
- Async data operations
- Bulk copy operations
- DataSet and DataTable usage

## Best Practices

- Always use parameterized queries
- Use async methods for I/O operations
- Dispose connections properly with using
- Use connection pooling for performance
- Prefer DbDataReader over DataSet for streaming

## Hands-on Labs

- Execute parameterized queries
- Implement async data operations
- Use transactions for multi-table updates
- Build a bulk insert operation

## Interview Questions

1. What is the difference between DbDataReader and DataSet?
2. How does connection pooling work?
3. Why should you always use parameterized queries?

## References

- https://learn.microsoft.com/dotnet/framework/data/adonet/
- https://learn.microsoft.com/dotnet/api/system.data.common.dbconnection
- https://learn.microsoft.com/dotnet/standard/data/
