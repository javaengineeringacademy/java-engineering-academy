# SQLAlchemy

## Why SQLAlchemy Exists

Every Python developer who works with databases eventually writes raw SQL strings, forgets to parameterize queries, and struggles with connection management. SQLAlchemy was created to solve these problems by providing two layers: a Core for SQL expression language with built-in injection protection, and an ORM that maps Python classes to database tables. It handles connection pooling, relationships, and schema migrations so you can focus on your application logic.

## What You'll Learn

By the end of this section, you'll be able to:

- Define models with relationships using SQLAlchemy's declarative base
- Perform CRUD operations with proper session management
- Use Alembic for database schema migrations

## When to Use SQLAlchemy

| Use Case | Why SQLAlchemy | Alternative |
|----------|---------------|-------------|
| Web app ORM | Map classes to tables naturally | Django ORM |
| Raw SQL control | SQL without injection risk | Raw SQL |
| Data migration | Version-controlled schema changes | Manual SQL |
| Connection pooling | Efficient connection reuse | Manual management |
| Complex queries | Core queries or ORM hybrid | Raw SQL |
| Multi-database | Route queries to different DBs | Manual routing |

## How SQLAlchemy Works Internally

SQLAlchemy's Core layer provides a Pythonic way to build SQL statements. Instead of concatenating strings, you use expression operators like `select()`, `where()`, and `join()`. These create Clause Elements that are rendered into database-specific SQL when executed. The Connection object manages a transaction and uses a pool of database connections for efficiency.

The ORM layer builds on Core. Each mapped class has a Table object that defines the schema. When you query `session.query(User).filter(User.age > 25)`, SQLAlchemy translates this into a SELECT statement with a WHERE clause, executes it, and hydrates the results into User objects. The Session tracks all changes and commits them in a single transaction.

```python
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import declarative_base, Session

Base = declarative_base()

class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    name = Column(String(50))

engine = create_engine('sqlite:///app.db')
Base.metadata.create_all(engine)

with Session(engine) as session:
    user = User(name='Alice')
    session.add(user)
    session.commit()
```

## Production Checklist

### ✅ Before using SQLAlchemy in production:

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

### ❌ Myth 1: SQLAlchemy is slow because of the ORM overhead
**Reality:** The ORM adds minimal overhead for most operations. For bulk inserts, use `session.bulk_save_objects()` or Core's `insert()` for near-native speed.

### ❌ Myth 2: You should use raw SQL for performance
**Reality:** Most performance issues come from N+1 queries (solved with `joinedload()`), missing indexes, or bad schema design — not ORM overhead.

### ❌ Myth 3: SQLAlchemy's session is just a database connection
**Reality:** The session is a Unit of Work pattern. It tracks changes, manages identity (one Python object per DB row), and handles transactions.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | SQL toolkit and ORM |
| Complexity | O(1) for simple queries, O(n) for joins |
| Thread Safe | No (sessions are not thread-safe) |
| Best Alternative | Django ORM for Django apps |
| When to Use | Database-driven applications |
| When to Avoid | Simple scripts with hardcoded queries |

## Related Topics

- [04-flask](../04-flask/) - Web framework integration
- [05-django](../05-django/) - Django ORM alternative
- [17-sqlite3](../17-sqlite3/) - Built-in SQLite alternative
