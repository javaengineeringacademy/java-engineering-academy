# sqlite3

## Why sqlite3 Exists

Every Python developer who needs a database for a small application, script, or prototype doesn't want to install and configure PostgreSQL or MySQL. sqlite3 is Python's built-in module that provides a lightweight, file-based database. It requires zero configuration, stores the entire database in a single file, and supports SQL. It's the standard for embedded databases in Python.

## What You'll Learn

By the end of this section, you'll be able to:

- Connect to SQLite databases and execute SQL queries
- Use parameterized queries to prevent SQL injection
- Manage transactions and handle errors properly

## When to Use sqlite3

| Use Case | Why sqlite3 | Alternative |
|----------|-----------|-------------|
| Local apps | Zero configuration, file-based | PostgreSQL |
| Prototyping | Built-in, instant setup | PostgreSQL |
| Embedded systems | Lightweight, no server | MySQL |
| Testing | In-memory database for tests | Mock databases |
| Data analysis | Quick query execution | Pandas |
| Mobile apps | SQLite is standard on mobile | PostgreSQL |

## How sqlite3 Works Internally

sqlite3 is a wrapper around the SQLite C library. When you connect, it opens (or creates) a database file. SQL statements are compiled into bytecode and executed by SQLite's virtual machine. The module provides a DB-API 2.0 compliant interface, meaning it works with Python's standard database patterns.

Transactions are managed explicitly. When you execute `INSERT`, `UPDATE`, or `DELETE`, the changes are not permanent until you call `connection.commit()`. If an error occurs, `connection.rollback()` undoes all changes since the last commit. This ensures data integrity.

```python
import sqlite3

# Connect (creates file if it doesn't exist)
conn = sqlite3.connect('app.db')
cursor = conn.cursor()

# Create table
cursor.execute('''
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY,
        name TEXT NOT NULL,
        email TEXT UNIQUE
    )
''')

# Insert with parameterized query
cursor.execute('INSERT INTO users (name, email) VALUES (?, ?)',
               ('Alice', 'alice@example.com'))
conn.commit()

# Query
cursor.execute('SELECT * FROM users WHERE age > ?', (25,))
rows = cursor.fetchall()

conn.close()
```

## Production Checklist

### ✅ Before using sqlite3 in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: SQLite is only for development
**Reality:** SQLite handles thousands of concurrent reads and is used in production by many applications (iOS, Android, browsers). It's appropriate for read-heavy workloads.

### ❌ Myth 2: SQLite doesn't support concurrent writes
**Reality:** SQLite supports WAL (Write-Ahead Logging) mode, which allows concurrent reads while writing. For write-heavy workloads, use PostgreSQL.

### ❌ Myth 3: sqlite3 is slow compared to PostgreSQL
**Reality:** For single-user or small-scale applications, SQLite is often faster because there's no network overhead. PostgreSQL scales better for multi-user concurrent access.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Built-in file-based database |
| Complexity | O(1) for simple queries |
| Thread Safe | No (per connection) |
| Best Alternative | PostgreSQL for production |
| When to Use | Prototypes, local apps, testing |
| When to Avoid | Multi-user production systems |

## Related Topics

- [06-sqlalchemy](../06-sqlalchemy/) - ORM for SQLite
- [05-django](../05-django/) - Django SQLite support
- [02-pandas](../02-pandas/) - Query SQLite with Pandas
