# Object Pool Pattern

## Intent
Reuse objects that are expensive to create by maintaining a pool of ready-to-use objects. Clients borrow objects from the pool and return them when done.

## Key Components
- **Pool Interface**: Defines borrow/return operations
- **ObjectPool**: Generic pool implementation with thread safety
- **Pooled Object**: The expensive resource to reuse

## When to Use
- Object creation is costly (database connections, threads, sockets)
- Objects are frequently created and destroyed
- You need to limit the number of concurrent resources
- High-throughput systems with shared expensive resources

## Benefits
- Reduced object creation overhead
- Controlled resource usage
- Improved performance under load
- Automatic resource cleanup

## Example
```java
DatabaseConnectionPool pool = new DatabaseConnectionPool(5);
DatabaseConnection conn = pool.borrow();
// use connection
pool.returnObject(conn);
```
