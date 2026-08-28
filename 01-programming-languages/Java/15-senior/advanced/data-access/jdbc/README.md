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

## Interview Questions

1. **Why should you always use PreparedStatement over Statement?**
   PreparedStatement prevents SQL injection by separating SQL structure from data. The database parses the SQL template once and reuses the execution plan. Statement concatenates user input directly into SQL strings, allowing injection attacks like `' OR '1'='1`. PreparedStatement also performs better for repeated queries due to plan caching.

2. **Explain HikariCP's connection leak detection. How does it work?**
   HikariCP tracks the timestamp when each connection is borrowed. If a connection is not returned within `leakDetectionThreshold` (default: 0 = disabled), HikariCP logs a warning with the stack trace of where the connection was borrowed. It uses a housekeeping thread that periodically checks borrowed connections. Set `leakDetectionThreshold=30000` (30s) in production to catch leaks early.

3. **What is the optimal HikariCP pool size formula?**
   For CPU-bound: `pool_size = CPU_cores + 1`. For I/O-bound: `pool_size = CPU_cores * 2 * (1 + wait_time/service_time)`. Most web apps: 10-20 connections is sufficient. Too many connections cause context switching overhead and database lock contention. Monitor `hikaricp_connections_active` vs `hikaricp_connections_max` to tune.

4. **How does JDBC batch processing improve performance?**
   Without batching: 1000 inserts = 1000 round trips = ~10 seconds. With batching: 1000 inserts = 10 batches × 1 round trip = ~0.5 seconds. Batching reduces network round trips, transaction overhead, and allows the database to optimize the execution plan. Use `addBatch()` + `executeBatch()` with batch sizes of 500-1000.

5. **What causes connection leaks and how do you diagnose them?**
   Leaks happen when `Connection.close()` is not called in a `finally` block or try-with-resources. Diagnosis: (1) Enable HikariCP leak detection; (2) Monitor `hikaricp_connections_active` growing over time; (3) Check thread dumps for threads holding connections; (4) Use `jstack` to find threads stuck in `HikariDataSource.getConnection()`. Prevention: always use try-with-resources.

6. **Compare JDBC, JPA/Hibernate, and jOOQ for data access.**
   JDBC: raw SQL, full control, fastest, most verbose. JPA/Hibernate: ORM, object-oriented, N+1 query risk, convenient. jOOQ: type-safe SQL, compiles SQL to Java, good middle ground. Use JDBC for simple queries and performance-critical paths. Use JPA for complex domain models. Use jOOQ when you want type-safe SQL without full ORM overhead.

## Performance

### Connection Acquisition Time
| Source | Time |
|--------|------|
| New TCP connection | 50-150ms |
| TLS handshake | 10-50ms |
| Database authentication | 5-20ms |
| HikariCP pool hit | <1ms |

### Batch Processing Benchmarks
```
Single inserts (1000 rows):  ~8-12 seconds
Batch (1000 rows, batch=100): ~0.8-1.5 seconds
Batch (1000 rows, batch=1000): ~0.3-0.8 seconds
COPY command (PostgreSQL):     ~0.1-0.3 seconds
```

### HikariCP Pool Size Impact
```
Pool=5:   200 req/sec, 0% timeout
Pool=10:  450 req/sec, 0% timeout
Pool=20:  500 req/sec, 2% timeout (contention)
Pool=50:  480 req/sec, 8% timeout (DB overload)
```

## Examples

```java
// Try-with-resources for automatic resource cleanup
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement(
         "SELECT id, name, email FROM users WHERE id = ?")) {
    ps.setLong(1, userId);
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            return new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")
            );
        }
    }
}

// Batch insert with HikariCP
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement(
         "INSERT INTO orders (user_id, product, amount) VALUES (?, ?, ?)")) {
    conn.setAutoCommit(false);
    int batchSize = 0;
    for (Order order : orders) {
        ps.setLong(1, order.getUserId());
        ps.setString(2, order.getProduct());
        ps.setBigDecimal(3, order.getAmount());
        ps.addBatch();
        if (++batchSize >= 500) {
            ps.executeBatch();
            batchSize = 0;
        }
    }
    if (batchSize > 0) ps.executeBatch();
    conn.commit();
}

// HikariCP configuration
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
config.setUsername("app_user");
config.setPassword("secret");
config.setMaximumPoolSize(20);
config.setMinimumIdle(5);
config.setConnectionTimeout(30000);
config.setIdleTimeout(600000);
config.setMaxLifetime(1800000);
config.setLeakDetectionThreshold(30000);
config.addDataSourceProperty("cachePrepStmts", "true");
config.addDataSourceProperty("prepStmtCacheSize", "250");
config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

HikariDataSource dataSource = new HikariDataSource(config);
```

## Internal Working

### HikariCP Connection Lifecycle
1. `getConnection()` → check idle connections (LIFO stack for cache locality)
2. If no idle connection and pool not full → create new TCP connection to DB
3. If pool full → wait up to `connectionTimeout` for a connection to be returned
4. On return → validate connection (lightweight query or JDBC4 `isValid()`)
5. If invalid → discard and create replacement
6. Housekeeping thread runs every 300ms to clean idle/expired connections

### JDBC Driver Architecture
```
Application Code
    ↓
JDBC API (java.sql.*)
    ↓
Driver Manager → selects driver by URL prefix
    ↓
Database Driver (PostgreSQL, MySQL, etc.)
    ↓
Wire Protocol (binary, database-specific)
    ↓
Database Server
```

### PreparedStatement Execution
1. SQL template sent to database for parsing and plan creation
2. Parameters bound via `setXxx()` calls (stored in driver buffer)
3. `executeQuery()`/`executeUpdate()` sends bound parameters to database
4. Database reuses cached execution plan (major performance gain)
5. Results streamed back via ResultSet (fetch size controls batch size)

## Why This Concept Exists

JDBC exists because Java needed a standardized API for database access. Before JDBC, each database vendor had proprietary APIs (Oracle's OCI, Sybase's CT-Lib), making it impossible to write portable database code. JDBC provides: (1) Vendor-independent API — switch databases by changing driver JAR and URL; (2) Type-safe resource management via AutoCloseable; (3) Connection pooling integration for performance; (4) Transaction management across different databases.

## Overview

JDBC (Java Database Connectivity) is the standard Java API for connecting to relational databases. It provides `Connection`, `Statement`, `PreparedStatement`, and `ResultSet` interfaces for executing SQL and processing results. JDBC drivers implement these interfaces for specific databases (PostgreSQL, MySQL, Oracle). In production, connection pooling (HikariCP) manages a reusable pool of connections to avoid the overhead of creating new TCP connections for every query.

## Pitfalls

```java
// PITFALL 1: Not closing resources — connection leak
Connection conn = dataSource.getConnection(); // NEVER released!

// PITFALL 2: SQL injection via string concatenation
String query = "SELECT * FROM users WHERE name = '" + userName + "'";
// Attacker sends: "'; DROP TABLE users; --"

// PITFALL 3: Auto-commit left on for batch operations
conn.setAutoCommit(true); // Each insert is a separate transaction!
// Fix: conn.setAutoCommit(false) + conn.commit()

// PITFALL 4: Not checking ResultSet before reading
ResultSet rs = ps.executeQuery();
rs.getString(1); // ResultSetMetaData not checked!

// PITFALL 5: Using SELECT * in production
// Wastes bandwidth, breaks if schema changes, prevents covering indexes

// PITFALL 6: Setting fetch size too high
ps.setFetchSize(100000); // Loads all results into memory at once
// Fix: Use fetch size of 100-1000 for streaming results
```

## References

- [HikariCP GitHub](https://github.com/brettwooldridge/HikariCP)
- [JDBC API Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.sql/java/sql/package-summary.html)
- [PostgreSQL JDBC Performance](https://jdbc.postgresql.org/documentation/head/performance.html)
- "High Performance Java Persistence" by Vlad Mihalcea
