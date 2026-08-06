# JDBC Architecture

## Components
- **DriverManager**: Manages database connections
- **Connection**: Database session
- **Statement**: SQL execution
- **PreparedStatement**: Parameterized queries
- **ResultSet**: Query results

## Connection Types
- **DriverManager.getConnection()**: Basic connection
- **DataSource**: Connection pooling
- **ConnectionPool**: HikariCP, c3p0

## Connection Pooling (HikariCP)
- Faster connection reuse
- Configurable pool size
- Connection validation
- Automatic leak detection

## Best Practices
- Use try-with-resources
- Close connections properly
- Use PreparedStatement for SQL injection prevention
- Implement connection pooling
- Handle exceptions appropriately

## Performance Tips
- Batch inserts/updates
- Use appropriate fetch size
- Avoid N+1 queries
- Cache read-heavy data
- Use connection pooling

## Common Issues
- Connection leaks
- Resource not closed
- Transaction not committed/rolled back
- SQL injection vulnerabilities
- Blocking operations on main thread

## Security
- Use parameterized queries
- Never concatenate user input
- Restrict database permissions
- Use connection pooling with limits
- Implement proper authentication
