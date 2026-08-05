# Repository Pattern

## Overview

The Repository Pattern mediates between domain and data mapping layers, acting as an in-memory collection of domain objects. It abstracts the data access layer so that business logic works with a collection-like interface without knowing the underlying database technology.

Repositories encapsulate the logic for fetching, storing, and querying domain entities. They return domain objects directly, keeping persistence concerns separate from business rules.

## When to Use

- Domain logic should not depend on data access technology
- Multiple data sources need a unified interface
- Unit testing requires mockable data access
- Domain model is complex and deserves its own persistence abstraction
- Database technology may change in the future

## Implementation

### TypeScript

```typescript
interface Repository<T> {
  findById(id: string): Promise<T | null>;
  findAll(): Promise<T[]>;
  save(entity: T): Promise<T>;
  delete(id: string): Promise<void>;
}

class UserRepository implements Repository<User> {
  constructor(private db: DatabaseClient) {}

  async findById(id: string): Promise<User | null> {
    const row = await this.db.query('SELECT * FROM users WHERE id = $1', [id]);
    return row ? this.toDomain(row) : null;
  }

  async save(user: User): Promise<User> {
    const row = this.toRow(user);
    await this.db.query(
      'INSERT INTO users (id, name, email) VALUES ($1, $2, $3) ON CONFLICT (id) DO UPDATE SET name=$2, email=$3',
      [row.id, row.name, row.email]
    );
    return user;
  }

  private toDomain(row: any): User { /* mapping logic */ }
  private toRow(user: User): any { /* mapping logic */ }
}
```

### Java

```java
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void deleteById(ID id);
}

@Repository
public class JpaUserRepository implements Repository<User, Long> {
    @Autowired
    private UserRepositoryJpa userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        userRepository.save(entity);
        return user;
    }
}
```

### Python

```python
from abc import ABC, abstractmethod
from typing import List, Optional

class Repository(ABC):
    @abstractmethod
    def find_by_id(self, id: str) -> Optional[dict]:
        pass

    @abstractmethod
    def find_all(self) -> List[dict]:
        pass

    @abstractmethod
    def save(self, entity: dict) -> dict:
        pass

class SqlAlchemyUserRepository(Repository):
    def __init__(self, session):
        self.session = session

    def find_by_id(self, id: str):
        return self.session.query(UserModel).filter_by(id=id).first()

    def save(self, entity: dict):
        user = UserModel(**entity)
        self.session.add(user)
        self.session.commit()
        return entity
```

### C\#

```csharp
public interface IRepository<T> where T : class {
    Task<T?> GetByIdAsync(int id);
    Task<IEnumerable<T>> GetAllAsync();
    Task AddAsync(T entity);
    Task UpdateAsync(T entity);
    Task DeleteAsync(int id);
}

public class UserRepository : IRepository<User> {
    private readonly DbContext _context;

    public UserRepository(DbContext context) => _context = context;

    public async Task<User?> GetByIdAsync(int id) =>
        await _context.Set<User>().FindAsync(id);
}
```

## Best Practices

- Keep repositories focused on a single aggregate or entity type
- Return domain objects, not database entities or DTOs
- Define repository interfaces in the domain layer
- Avoid business logic in repository implementations
- Use specifications for complex queries
- Consider read-only repositories for reporting queries

## Interview Questions

1. How does the Repository Pattern differ from the DAO Pattern?
2. Should repository interfaces be defined in the domain or infrastructure layer?
3. How do you handle complex queries that span multiple entities?
4. What are the tradeoffs of using generic repositories versus specific ones?
5. How does the Repository Pattern support unit testing?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Chapter 10
- Evans, Eric. *Domain-Driven Design*, Repositories chapter
- Vernon, Vaughn. *Implementing Domain-Driven Design*, Chapter 6
- Microsoft. *Architecture Patterns - Repository Pattern*
