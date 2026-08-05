# Data Access Object Pattern

## Overview

The Data Access Object (DAO) Pattern provides an abstract interface to a database or other persistence mechanism. It separates the data access logic from the business logic, allowing the business layer to work with data without knowing the underlying storage details.

DAO encapsulates all data access operations (CRUD) for a specific data source. It acts as a bridge between the domain and the persistence layer, hiding the specifics of data storage and retrieval.

## When to Use

- Need to abstract database-specific details from business logic
- Multiple data sources require different access strategies
- Testing requires mocking the data access layer
- Database technology may change independently
- Data access logic is complex and needs encapsulation

## Implementation

### TypeScript

```typescript
interface UserDao {
  findById(id: string): Promise<User | null>;
  findByEmail(email: string): Promise<User | null>;
  create(user: User): Promise<User>;
  update(user: User): Promise<User>;
  delete(id: string): Promise<void>;
}

class PostgresUserDao implements UserDao {
  constructor(private pool: Pool) {}

  async findById(id: string): Promise<User | null> {
    const result = await this.pool.query('SELECT * FROM users WHERE id = $1', [id]);
    return result.rows[0] ? this.toDomain(result.rows[0]) : null;
  }

  async create(user: User): Promise<User> {
    const query = 'INSERT INTO users (name, email) VALUES ($1, $2) RETURNING *';
    const result = await this.pool.query(query, [user.name, user.email]);
    return this.toDomain(result.rows[0]);
  }

  async delete(id: string): Promise<void> {
    await this.pool.query('DELETE FROM users WHERE id = $1', [id]);
  }

  private toDomain(row: any): User { /* mapping logic */ }
}
```

### Java

```java
public interface UserDao {
    User findById(Long id);
    User findByEmail(String email);
    User save(User user);
    User update(User user);
    void delete(Long id);
}

@Repository
public class JdbcUserDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?",
            new UserRowMapper(), id
        );
    }

    @Override
    public User save(User user) {
        jdbcTemplate.update(
            "INSERT INTO users (name, email) VALUES (?, ?)",
            user.getName(), user.getEmail()
        );
        return user;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}
```

### Python

```python
from abc import ABC, abstractmethod
from typing import Optional, List

class UserDao(ABC):
    @abstractmethod
    def find_by_id(self, id: str) -> Optional[dict]:
        pass

    @abstractmethod
    def find_by_email(self, email: str) -> Optional[dict]:
        pass

    @abstractmethod
    def create(self, data: dict) -> dict:
        pass

    @abstractmethod
    def delete(self, id: str) -> None:
        pass

class PostgresUserDao(UserDao):
    def __init__(self, connection):
        self.conn = connection

    def find_by_id(self, id: str):
        cursor = self.conn.cursor()
        cursor.execute('SELECT * FROM users WHERE id = %s', (id,))
        row = cursor.fetchone()
        return self._to_dict(row) if row else None

    def create(self, data: dict):
        cursor = self.conn.cursor()
        cursor.execute(
            'INSERT INTO users (name, email) VALUES (%s, %s) RETURNING id',
            (data['name'], data['email'])
        )
        self.conn.commit()
        return {**data, 'id': cursor.fetchone()[0]}

    def delete(self, id: str):
        cursor = self.conn.cursor()
        cursor.execute('DELETE FROM users WHERE id = %s', (id,))
        self.conn.commit()
```

### C\#

```csharp
public interface IUserDao {
    Task<User?> GetByIdAsync(int id);
    Task<User?> GetByEmailAsync(string email);
    Task<User> CreateAsync(User user);
    Task UpdateAsync(User user);
    Task DeleteAsync(int id);
}

public class UserDao : IUserDao {
    private readonly IDbConnection _connection;

    public UserDao(IDbConnection connection) => _connection = connection;

    public async Task<User?> GetByIdAsync(int id) {
        return await _connection.QuerySingleOrDefaultAsync<User>(
            "SELECT * FROM Users WHERE Id = @Id", new { Id = id });
    }

    public async Task DeleteAsync(int id) {
        await _connection.ExecuteAsync(
            "DELETE FROM Users WHERE Id = @Id", new { Id = id });
    }
}
```

## Best Practices

- Define DAO interfaces for each entity or aggregate
- Keep DAO responsibilities limited to data access
- Use row mappers or converters to translate between database rows and domain objects
- Handle connection management and error handling within the DAO
- Unit test DAOs against in-memory databases or test containers
- Consider read-only DAOs for reporting queries

## Interview Questions

1. How does the DAO Pattern differ from the Repository Pattern?
2. Should DAOs return domain objects or data entities?
3. How do you handle database-specific SQL in cross-database applications?
4. What testing strategies work best with DAOs?
5. How does DAO relate to the service layer?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Data Mapper chapter
- Microsoft. *Data Access Object (DAO) Pattern*
- Core J2EE Patterns. *Data Access Object*
- Oracle. *Core J2EE Patterns - Data Access Object*
