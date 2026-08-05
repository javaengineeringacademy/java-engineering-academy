# Facade Pattern in Python

The Facade pattern provides a simplified interface to a complex subsystem. Python's modules and classes naturally support this pattern by hiding complexity behind clean APIs.

## When to Use

- Simplifying complex library or framework usage
- Providing a unified interface to a set of interfaces
- Reducing dependencies on external subsystems
- Layering subsystems and defining entry points
- Creating higher-level operations from primitive ones

## Python Implementation

### Class-Based Facade
```python
class CPU:
    def freeze(self):
        return "Freezing CPU"
    
    def jump(self, address):
        return f"Jumping to {address}"
    
    def execute(self):
        return "Executing instructions"

class Memory:
    def load(self, address, data):
        return f"Loading {data} to {address}"

class HardDrive:
    def read(self, sector, size):
        return f"Reading {size} bytes from sector {sector}"

class ComputerFacade:
    def __init__(self):
        self._cpu = CPU()
        self._memory = Memory()
        self._hd = HardDrive()
    
    def start(self):
        steps = []
        steps.append(self._cpu.freeze())
        steps.append(self._hd.read(0, 1024))
        steps.append(self._memory.load(0, "boot_data"))
        steps.append(self._cpu.jump(0))
        steps.append(self._cpu.execute())
        return steps

# Usage
computer = ComputerFacade()
computer.start()
```

### Module-Level Facade
```python
# database_facade.py
import sqlite3
from contextlib import contextmanager

_connection = None

def init_db(db_path: str):
    global _connection
    _connection = sqlite3.connect(db_path)

@contextmanager
def get_cursor():
    cursor = _connection.cursor()
    try:
        yield cursor
        _connection.commit()
    except Exception:
        _connection.rollback()
        raise

def create_table(name: str, schema: str):
    with get_cursor() as cursor:
        cursor.execute(f"CREATE TABLE IF NOT EXISTS {name} ({schema})")

def insert(table: str, data: dict):
    columns = ", ".join(data.keys())
    placeholders = ", ".join(["?" for _ in data])
    with get_cursor() as cursor:
        cursor.execute(
            f"INSERT INTO {table} ({columns}) VALUES ({placeholders})",
            list(data.values())
        )

def query(table: str, conditions: dict = None):
    sql = f"SELECT * FROM {table}"
    params = []
    if conditions:
        where = " AND ".join([f"{k} = ?" for k in conditions.keys()])
        sql += f" WHERE {where}"
        params = list(conditions.values())
    with get_cursor() as cursor:
        cursor.execute(sql, params)
        return cursor.fetchall()
```

## Pythonic Alternative

Use context managers for resource management:
```python
from contextlib import contextmanager

@contextmanager
def database_connection():
    conn = sqlite3.connect("db.sqlite3")
    try:
        yield conn
    finally:
        conn.close()

# Usage
with database_connection() as conn:
    conn.execute("SELECT * FROM users")
```

## Real-World Example

```python
class EmailFacade:
    def __init__(self, config: dict):
        self._smtp = config.get("smtp")
        self._templates = {}
    
    def send_welcome(self, user_email: str, name: str):
        template = self._load_template("welcome")
        body = template.format(name=name)
        self._send(user_email, "Welcome!", body)
    
    def send_notification(self, user_email: str, message: str):
        self._send(user_email, "Notification", message)
    
    def _send(self, to: str, subject: str, body: str):
        # Complex email sending logic
        print(f"Sending to {to}: {subject}")
    
    def _load_template(self, name: str):
        return f"Hello {name}!" if name == "welcome" else ""
```

## Best Practices

1. Keep facade focused on simplification, not new functionality
2. Document which subsystem methods the facade uses
3. Allow direct subsystem access for advanced users
4. Consider multiple facades for different use cases
5. Keep facade implementation behind an interface

## Interview Questions

1. What is the difference between Facade and Adapter?
2. When would you use Facade over direct subsystem access?
3. How would you implement a facade for a complex library?
4. What are the drawbacks of using Facade?
5. How would you test code that uses a facade?

## References

- *Design Patterns* - GoF, Chapter 4
- Python `contextlib` documentation
- *Python Cookbook* - Alex Martelli
- PEP 343 - The `with` Statement
