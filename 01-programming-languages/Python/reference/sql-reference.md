# Python SQL Reference

## What is SQL in Python?

Python provides `sqlite3` for working with SQLite databases, which follows the DB-API 2.0 specification. It's used for local data storage and is included with Python.

## Why does SQL matter?

Understanding SQL in Python helps you:
- Store and retrieve data locally
- Build database applications
- Work with structured data
- Implement data persistence

---

## 1. Basic Connection

```python
import sqlite3

# Connect to database (creates if doesn't exist)
conn = sqlite3.connect('example.db')

# Create cursor
cursor = conn.cursor()

# Execute SQL
cursor.execute('CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)')

# Commit changes
conn.commit()

# Close connection
conn.close()
```

---

## 2. CRUD Operations

```python
import sqlite3

conn = sqlite3.connect('example.db')
cursor = conn.cursor()

# Create table
cursor.execute('''
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    age INTEGER,
    email TEXT UNIQUE
)
''')

# Insert (Create)
cursor.execute('INSERT INTO users (name, age, email) VALUES (?, ?, ?)',
               ('Alice', 30, 'alice@example.com'))

# Insert many
users = [('Bob', 25, 'bob@example.com'),
         ('Charlie', 35, 'charlie@example.com')]
cursor.executemany('INSERT INTO users (name, age, email) VALUES (?, ?, ?)', users)

conn.commit()

# Read
cursor.execute('SELECT * FROM users')
rows = cursor.fetchall()
for row in rows:
    print(row)

# Read one
cursor.execute('SELECT * FROM users WHERE id = ?', (1,))
row = cursor.fetchone()
print(row)

# Read many
cursor.execute('SELECT * FROM users WHERE age > ?', (25,))
rows = cursor.fetchmany(10)
for row in rows:
    print(row)

# Update
cursor.execute('UPDATE users SET age = ? WHERE id = ?', (31, 1))
conn.commit()

# Delete
cursor.execute('DELETE FROM users WHERE id = ?', (1,))
conn.commit()

conn.close()
```

---

## 3. Context Manager

```python
import sqlite3

# Using with statement
with sqlite3.connect('example.db') as conn:
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM users')
    rows = cursor.fetchall()
# Connection automatically closed
```

---

## 4. Parameterized Queries

```python
import sqlite3

conn = sqlite3.connect('example.db')
cursor = conn.cursor()

# WRONG (SQL injection risk)
name = "Alice"
cursor.execute(f"SELECT * FROM users WHERE name = '{name}'")

# RIGHT (parameterized)
cursor.execute("SELECT * FROM users WHERE name = ?", (name,))

# Multiple parameters
cursor.execute("SELECT * FROM users WHERE age > ? AND name LIKE ?",
               (25, '%Ali%'))

conn.close()
```

---

## 5. Row Factory

```python
import sqlite3

conn = sqlite3.connect('example.db')
conn.row_factory = sqlite3.Row

cursor = conn.cursor()
cursor.execute('SELECT * FROM users')
rows = cursor.fetchall()

# Access by column name
for row in rows:
    print(row['name'], row['age'])

conn.close()
```

---

## 6. Transactions

```python
import sqlite3

conn = sqlite3.connect('example.db')

try:
    cursor = conn.cursor()
    cursor.execute('INSERT INTO users (name, age) VALUES (?, ?)',
                   ('Alice', 30))
    cursor.execute('INSERT INTO users (name, age) VALUES (?, ?)',
                   ('Bob', 25))
    conn.commit()
except Exception as e:
    conn.rollback()
    print(f"Error: {e}")
finally:
    conn.close()
```

---

## 7. Database Info

```python
import sqlite3

conn = sqlite3.connect('example.db')
cursor = conn.cursor()

# Get table info
cursor.execute("PRAGMA table_info(users)")
columns = cursor.fetchall()
for col in columns:
    print(col)

# Get all tables
cursor.execute("SELECT name FROM sqlite_master WHERE type='table'")
tables = cursor.fetchall()
print(tables)

conn.close()
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **connect** | Connect to database | `sqlite3.connect('db.sqlite')` |
| **cursor** | Create cursor | `conn.cursor()` |
| **execute** | Execute SQL | `cursor.execute('SQL')` |
| **executemany** | Execute many SQL | `cursor.executemany('SQL', data)` |
| **fetchone** | Fetch one row | `cursor.fetchone()` |
| **fetchmany** | Fetch many rows | `cursor.fetchmany(n)` |
| **fetchall** | Fetch all rows | `cursor.fetchall()` |
| **commit** | Commit transaction | `conn.commit()` |
| **rollback** | Rollback transaction | `conn.rollback()` |
| **close** | Close connection | `conn.close()` |

---

## Common Mistakes

### 1. SQL Injection

```python
# WRONG
name = "Alice'; DROP TABLE users; --"
cursor.execute(f"SELECT * FROM users WHERE name = '{name}'")

# RIGHT
cursor.execute("SELECT * FROM users WHERE name = ?", (name,))
```

### 2. Not Committing Changes

```python
# WRONG
cursor.execute('INSERT INTO users VALUES (1, "Alice")')
# Changes not saved

# RIGHT
cursor.execute('INSERT INTO users VALUES (1, "Alice")')
conn.commit()
```

### 3. Not Using Context Manager

```python
# WRONG
conn = sqlite3.connect('example.db')
cursor = conn.cursor()
cursor.execute('SELECT * FROM users')
# Connection may not be closed

# RIGHT
with sqlite3.connect('example.db') as conn:
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM users')
```

### 4. Fetching All Rows

```python
# WRONG (memory issues)
cursor.execute('SELECT * FROM huge_table')
rows = cursor.fetchall()  # Loads everything into memory

# RIGHT (use fetchmany or iterate)
cursor.execute('SELECT * FROM huge_table')
while True:
    rows = cursor.fetchmany(1000)
    if not rows:
        break
    for row in rows:
        process(row)
```

---

## Production Notes

1. **Always use parameterized queries** - Prevent SQL injection
2. **Use context managers** - Ensure connections are closed
3. **Commit after changes** - Don't forget to commit
4. **Use transactions** - For data integrity
5. **Use row_factory for named access** - More readable
6. **Handle exceptions** - Use try/except/finally
7. **Use fetchmany for large datasets** - Don't load everything
8. **Use WAL mode** - Better performance
9. **Close connections** - Don't leave them open
10. **Use ORM for complex apps** - SQLAlchemy, etc.

---

## Further Reading

- Python documentation on sqlite3 module
- SQLite documentation
- DB-API 2.0 specification
- SQLAlchemy documentation
