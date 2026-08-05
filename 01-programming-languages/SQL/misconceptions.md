# SQL Common Misconceptions

## 1. SQL is Simple

**Myth**: SQL is easy to learn and doesn't require deep knowledge.

**Reality**: SQL has significant complexity:
- Window functions, CTEs, and recursive queries
- Execution plans and optimizer behavior
- Transaction isolation levels
- Performance tuning and indexing strategies
- Data types and casting rules
- NULL handling and three-valued logic

**Why People Believe It**: Basic CRUD operations are simple. SQL reads like English.

**Evidence**: 
- Advanced SQL requires understanding of set theory
- Query optimization is a specialized skill
- Different databases have different SQL dialects
- ORMs abstract complexity but don't eliminate it

**Interview Relevance**: Demonstrate SQL depth. Discuss window functions, CTEs, and query optimization. Show awareness of dialect differences.

---

## 2. All Databases are the Same

**Myth**: SQL databases are interchangeable; it doesn't matter which one you use.

**Reality**: Databases have different strengths:
- **PostgreSQL**: Advanced features, extensibility
- **MySQL**: Simplicity, replication
- **SQLite**: Embedded, zero-config
- **Oracle**: Enterprise features, support
- **SQL Server**: Microsoft ecosystem integration

**Why People Believe It**: Basic SQL works across databases. Similarities outweigh differences for simple operations.

**Evidence**: 
- Each database has unique syntax and features
- Performance characteristics vary significantly
- Replication and clustering differ
- Extension ecosystems are database-specific

**Interview Relevance**: Discuss database selection criteria. Explain tradeoffs. Mention specific database features and limitations.

---

## 3. JOINs are Always Slow

**Myth**: JOIN operations are inherently performance bottlenecks.

**Reality**: JOINs are optimized by databases:
- Hash joins for large datasets
- Merge joins for sorted data
- Nested loops for small datasets
- Proper indexing eliminates performance issues
- Denormalization isn't always necessary

**Why People Believe It**: Complex JOINs can be slow. ORMs generate inefficient queries. Missing indexes cause problems.

**Evidence**: 
- Database optimizers are sophisticated
- Explain plans reveal join strategies
- Indexes on foreign keys improve JOIN performance
- Denormalization trades write performance for read performance

**Interview Relevance**: Explain join algorithms. Discuss indexing strategies. Show how to analyze query plans.

---

## 4. NULL equals NULL

**Myth**: NULL equals NULL in comparisons.

**Reality**: NULL is not equal to anything, including itself:
```sql
SELECT NULL = NULL;  -- NULL (not TRUE)
SELECT NULL IS NULL;  -- TRUE
SELECT NULL <> NULL;  -- NULL (not TRUE)
```

**Why People Believe It**: Logically, NULL should equal NULL. Other languages treat null/nil this way.

**Evidence**: 
- SQL uses three-valued logic (TRUE, FALSE, NULL)
- `IS NULL` and `IS NOT NULL` are the correct operators
- `COALESCE` and `NULLIF` handle NULL values
- Aggregate functions ignore NULLs (except COUNT(*))

**Interview Relevance**: Explain three-valued logic. Show NULL handling techniques. Discuss common NULL-related bugs.

---

## 5. Indexing Always Helps

**Myth**: Adding indexes always improves performance.

**Reality**: Indexes have tradeoffs:
- **Read performance**: Improves SELECT operations
- **Write performance**: Slows INSERT, UPDATE, DELETE
- **Storage**: Indexes consume disk space
- **Maintenance**: Indexes require rebuilding
- **Selectivity**: Low-selectivity indexes may not help

**Why People Believe It**: Indexes speed up reads. Missing indexes cause obvious performance problems.

**Evidence**: 
- Full table scans may be faster for small tables
- Composite indexes must match query patterns
- Over-indexing causes write performance degradation
- Index statistics affect query optimization

**Interview Relevance**: Discuss indexing strategy. Explain when indexes help vs. hurt. Mention index types (B-tree, hash, GIN, GiST).

---

## 6. SQL Databases Don't Scale

**Myth**: SQL databases can't handle large-scale applications.

**Reality**: SQL scales with proper architecture:
- Read replicas distribute read load
- Sharding splits data across nodes
- Connection pooling manages connections
- Caching reduces database load
- Partitioning improves query performance

**Why People Believe It**: NoSQL databases were created for scale. SQL scaling requires more planning.

**Evidence**: 
- PostgreSQL handles billions of rows
- MySQL powers large-scale web applications (Facebook, Twitter)
- NewSQL databases (CockroachDB, TiDB) combine SQL with horizontal scaling
- Cloud-managed SQL services (Aurora, Cloud SQL) simplify scaling

**Interview Relevance**: Discuss SQL scaling strategies. Explain when to scale vertically vs. horizontally. Mention NewSQL alternatives.
