# Identity Map Pattern

## Overview

The Identity Map Pattern ensures that each object is loaded from the database only once per transaction by maintaining an in-memory cache of loaded objects keyed by their identity. When an object is requested, the map checks if it already exists before hitting the database.

This pattern prevents duplicate objects representing the same database record, maintains object identity within a session, and reduces unnecessary database queries.

## When to Use

- Multiple parts of code need the same entity within a transaction
- Ensuring object identity consistency within a session
- Reducing database round-trips for frequently accessed objects
- Preventing stale data from concurrent modifications
- Working with ORMs that manage object lifecycles

## Implementation

### TypeScript

```typescript
class IdentityMap<T> {
  private map: Map<string, T> = new Map();

  get(id: string): T | undefined {
    return this.map.get(id);
  }

  add(id: string, entity: T): void {
    this.map.set(id, entity);
  }

  has(id: string): boolean {
    return this.map.has(id);
  }

  remove(id: string): void {
    this.map.delete(id);
  }

  clear(): void {
    this.map.clear();
  }
}

class UserSession {
  private users = new IdentityMap<User>();

  async getUser(id: string, db: DatabaseClient): Promise<User> {
    if (this.users.has(id)) {
      return this.users.get(id)!;
    }
    const row = await db.query('SELECT * FROM users WHERE id = $1', [id]);
    const user = this.mapToUser(row);
    this.users.add(id, user);
    return user;
  }
}
```

### Java

```java
public class IdentityMap<T> {
    private final Map<String, T> map = new ConcurrentHashMap<>();

    public Optional<T> get(String id) {
        return Optional.ofNullable(map.get(id));
    }

    public void put(String id, T entity) {
        map.put(id, entity);
    }

    public boolean contains(String id) {
        return map.containsKey(id);
    }

    public void remove(String id) {
        map.remove(id);
    }
}

@Repository
public class CachedUserRepository {
    private final IdentityMap<User> identityMap = new IdentityMap<>();
    private final UserRepository delegate;

    public User findById(String id) {
        return identityMap.get(id)
            .orElseGet(() -> {
                User user = delegate.findById(id);
                if (user != null) identityMap.put(id, user);
                return user;
            });
    }
}
```

### Python

```python
from typing import Dict, Optional, TypeVar, Generic

T = TypeVar('T')

class IdentityMap(Generic[T]):
    def __init__(self):
        self._map: Dict[str, T] = {}

    def get(self, entity_id: str) -> Optional[T]:
        return self._map.get(entity_id)

    def add(self, entity_id: str, entity: T) -> None:
        self._map[entity_id] = entity

    def has(self, entity_id: str) -> bool:
        return entity_id in self._map

    def remove(self, entity_id: str) -> None:
        self._map.pop(entity_id, None)

    def clear(self) -> None:
        self._map.clear()
```

### C\#

```csharp
public class IdentityMap<T> where T : class {
    private readonly Dictionary<string, T> _map = new();

    public T? Get(string id) =>
        _map.TryGetValue(id, out var entity) ? entity : null;

    public void Add(string id, T entity) =>
        _map[id] = entity;

    public bool Contains(string id) =>
        _map.ContainsKey(id);

    public void Remove(string id) =>
        _map.Remove(id);

    public void Clear() =>
        _map.Clear();
}
```

## Best Practices

- Clear the identity map at the end of each transaction or session
- Use entity IDs as keys consistently
- Be aware of memory usage with large datasets
- Invalidate cached objects when data may have changed externally
- Combine with Unit of Work for consistent change tracking
- Consider TTL-based expiration for long-lived sessions

## Interview Questions

1. How does Identity Map prevent stale data issues?
2. What happens when an entity is modified outside the current session?
3. How do identity maps interact with distributed caching systems?
4. Should the identity map span multiple transactions?
5. How do ORMs like Hibernate implement identity map behavior?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Chapter 12
- Hibernate Documentation. *Session and Identity Map*
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- Evans, Eric. *Domain-Driven Design*, chapter on repositories
