# SQLAlchemy — SQL Toolkit and Object-Relational Mapper

> **Write Python, not SQL. Let SQLAlchemy handle the translation.**

## What

SQLAlchemy provides two layers: a Core (SQL expression language and connection pooling) and an ORM (object-relational mapping). It maps Python classes to database tables, handles relationships, manages sessions, and generates SQL from Python expressions. It supports every major database: PostgreSQL, MySQL, SQLite, Oracle, and more.

## Why

- **Database abstraction:** Switch between SQLite (dev) and PostgreSQL (prod) with one config change.
- **SQL injection protection:** Parameterized queries built into the expression language.
- **Relationships:** One-to-many, many-to-many, and inheritance mapped naturally.
- **Connection pooling:** Built-in pool management, no manual connection handling.
- **Migration support:** Alembic (SQLAlchemy's migration tool) tracks schema changes.

## When

| Scenario | SQLAlchemy Approach | Why |
|----------|-------------------|-----|
| Web app ORM | `declarative_base()` models | Map classes to tables |
| Raw SQL control | Core expression language | SQL without injection risk |
| Data migration | Alembic migrations | Version-controlled schema changes |
| Connection pooling | `create_engine(pool_size=5)` | Efficient connection reuse |
| Complex queries | Core queries or ORM hybrid | Best of both worlds |
| Multi-database | Multiple engines | Route queries to different DBs |

## How

### Engine and Connection

```python
from sqlalchemy import create_engine

# Engine (connection factory)
engine = create_engine(
    'postgresql://user:pass@localhost/mydb',
    pool_size=5,           # Connection pool size
    max_overflow=10,       # Extra connections beyond pool_size
    pool_pre_ping=True,    # Verify connections before use
    echo=True              # Log SQL (dev only)
)

# Raw SQL with connection
with engine.connect() as conn:
    result = conn.execute("SELECT * FROM users WHERE age > :age", {"age": 30})
    for row in result:
        print(row)
```

### Declarative Models

```python
from sqlalchemy import Column, Integer, String, Float, ForeignKey, DateTime, Boolean
from sqlalchemy.orm import declarative_base, relationship, Session
from datetime import datetime

Base = declarative_base()

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    username = Column(String(50), unique=True, nullable=False)
    email = Column(String(100), unique=True, nullable=False)
    age = Column(Integer)
    is_active = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)

    # Relationships
    posts = relationship('Post', back_populates='author', cascade='all, delete-orphan')
    profile = relationship('Profile', back_populates='user', uselist=False)

    def __repr__(self):
        return f'<User {self.username}>'

class Post(Base):
    __tablename__ = 'posts'

    id = Column(Integer, primary_key=True)
    title = Column(String(200), nullable=False)
    content = Column(String)
    author_id = Column(Integer, ForeignKey('users.id'), nullable=False)

    # Relationships
    author = relationship('User', back_populates='posts')
    tags = relationship('Tag', secondary='post_tags', back_populates='posts')

class Profile(Base):
    __tablename__ = 'profiles'

    id = Column(Integer, primary_key=True)
    bio = Column(String)
    user_id = Column(Integer, ForeignKey('users.id'), unique=True)

    user = relationship('User', back_populates='profile')

# Many-to-many
from sqlalchemy import Table
post_tags = Table('post_tags', Base.metadata,
    Column('post_id', Integer, ForeignKey('posts.id'), primary_key=True),
    Column('tag_id', Integer, ForeignKey('tags.id'), primary_key=True)
)

class Tag(Base):
    __tablename__ = 'tags'

    id = Column(Integer, primary_key=True)
    name = Column(String(30), unique=True)
    posts = relationship('Post', secondary=post_tags, back_populates='tags')
```

### CRUD Operations

```python
from sqlalchemy.orm import Session

# Create
with Session(engine) as session:
    user = User(username='alice', email='alice@example.com', age=30)
    session.add(user)
    session.commit()

# Read
with Session(engine) as session:
    # Get by ID
    user = session.get(User, 1)

    # Query
    users = session.query(User).filter(User.age > 25).all()
    active = session.query(User).filter_by(is_active=True).first()
    count = session.query(User).count()

# Update
with Session(engine) as session:
    user = session.get(User, 1)
    user.age = 31
    session.commit()

# Delete
with Session(engine) as session:
    user = session.get(User, 1)
    session.delete(user)
    session.commit()
```

### Relationships and Loading

```python
# Eager loading (loads related objects in one query)
users = session.query(User).options(
    joinedload(User.posts),
    joinedload(User.profile)
).all()

# Lazy loading (loads on access — triggers additional queries)
user = session.get(User, 1)
posts = user.posts  # SQL query happens here

# Relationship queries
posts = session.query(Post).join(Post.author).filter(User.username == 'alice').all()

# Many-to-many
tag = session.query(Tag).filter_by(name='python').first()
tag.posts  # All posts with this tag
```

### Session Management

```python
from sqlalchemy.orm import sessionmaker

# Factory pattern
SessionLocal = sessionmaker(bind=engine)

# Use in context manager (recommended)
def get_user(user_id: int):
    with SessionLocal() as session:
        return session.get(User, user_id)

# Manual session (careful with exceptions!)
session = SessionLocal()
try:
    user = User(username='bob', email='bob@example.com')
    session.add(user)
    session.commit()
except Exception:
    session.rollback()
    raise
finally:
    session.close()
```

## Production Checklist

- [ ] **Use `pool_pre_ping=True`** — handles stale connections
- [ ] **Set `pool_size` and `max_overflow`** — control connection usage
- [ ] **Never commit in loops** — batch operations, commit once
- [ ] **Use `session.expire_on_commit=False`** — avoid lazy load after commit
- [ ] **Enable SQL logging in dev only** — `echo=True` in dev, `False` in prod
- [ ] **Use Alembic for migrations** — never manually alter production schemas
- [ ] **Parameterize queries** — never concatenate user input into SQL
- [ ] **Close sessions** — use context managers or `try/finally`

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **Raw SQL** | `engine.execute()` with string SQL. No ORM. |
| 2 | **Basic ORM** | Declarative models, simple queries, basic relationships. |
| 3 | **Intermediate** | Complex joins, eager loading, custom types, events. |
| 4 | **Advanced** | Multiple databases, hybrid properties, association proxies, bulk operations. |
| 5 | **Expert** | Custom dialects, connection pooling tuning, async SQLAlchemy, performance profiling. |

## Common Myths

### Myth 1: "SQLAlchemy is slow because of the ORM overhead"
**Reality:** The ORM adds minimal overhead for most operations. For bulk inserts, use `session.bulk_save_objects()` or Core's `insert()` for near-native speed. The ORM's convenience far outweighs its overhead in typical web applications.

### Myth 2: "You should use raw SQL for performance"
**Reality:** Most performance issues come from N+1 queries (solved with `joinedload()`), missing indexes, or bad schema design — not ORM overhead. Profile first, then optimize.

### Myth 3: "SQLAlchemy's session is just a database connection"
**Reality:** The session is a Unit of Work pattern. It tracks changes, manages identity (one Python object per DB row), and handles transactions. Understanding this is key to using SQLAlchemy correctly.

## One-Minute Revision

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Engine | `create_engine(url)` | Connection factory |
| Model | `class User(Base)` | Map to table |
| Session | `Session(engine)` | Unit of work |
| Query | `session.query(User).filter(...)` | Read data |
| Create | `session.add(obj)` + `commit()` | Insert row |
| Update | `obj.attr = value` + `commit()` | Modify row |
| Delete | `session.delete(obj)` + `commit()` | Remove row |
| Relationship | `relationship('Model', back_populates='...')` | Link tables |
| ForeignKey | `Column(Integer, ForeignKey('table.id'))` | Referential integrity |
| Migration | `alembic revision --autogenerate` | Schema versioning |

## Related Topics

- [22-libraries-flask](../22-libraries-flask/) - Web framework integration
- [23-libraries-django](../23-libraries-django/) - Django ORM alternative
- [19-libraries-numpy](../19-libraries-numpy/) - Database ↔ DataFrame pipeline
- [08-file-io](../08-file-io/) - File-based alternatives

---

> **Remember:** SQLAlchemy's power is its two-layer design. Use the ORM for convenience, drop to Core when you need control. Both are first-class citizens.
