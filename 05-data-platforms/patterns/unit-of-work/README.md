# Unit of Work Pattern

## Overview

The Unit of Work Pattern maintains a list of objects affected by a business transaction and coordinates the writing of changes and the resolution of concurrency problems. It ensures that all changes are committed atomically or rolled back entirely if any operation fails.

A Unit of Work tracks inserted, updated, and deleted entities within a single transaction boundary, batching database operations until an explicit commit is requested.

## When to Use

- Multiple entities must be modified within a single transaction
- Changes need to be committed or rolled back atomically
- You need to track which objects have changed before persisting
- Concurrency control requires change tracking
- Domain operations span multiple repository calls

## Implementation

### TypeScript

```typescript
class UnitOfWork {
  private newEntities: Map<string, any> = new Map();
  private updatedEntities: Map<string, any> = new Map();
  private deletedEntities: Map<string, any> = new Map();

  registerNew(entity: any) {
    this.newEntities.set(entity.id, entity);
  }

  registerDirty(entity: any) {
    this.updatedEntities.set(entity.id, entity);
  }

  registerDeleted(entity: any) {
    this.deletedEntities.set(entity.id, entity);
  }

  async commit(): Promise<void> {
    const client = await this.db.connect();
    try {
      await client.query('BEGIN');
      for (const entity of this.newEntities.values()) {
        await this.insert(entity, client);
      }
      for (const entity of this.updatedEntities.values()) {
        await this.update(entity, client);
      }
      for (const entity of this.deletedEntities.values()) {
        await this.delete(entity, client);
      }
      await client.query('COMMIT');
      this.clear();
    } catch (error) {
      await client.query('ROLLBACK');
      throw error;
    } finally {
      client.release();
    }
  }
}
```

### Java

```java
public class UnitOfWork {
    private final EntityManager entityManager;
    private final List<Runnable> insertActions = new ArrayList<>();
    private final List<Runnable> updateActions = new ArrayList<>();
    private final List<Runnable> deleteActions = new ArrayList<>();

    @Transactional
    public void commit() {
        Transaction tx = entityManager.getTransaction();
        tx.begin();
        try {
            insertActions.forEach(Runnable::run);
            updateActions.forEach(Runnable::run);
            deleteActions.forEach(Runnable::run);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}
```

### Python

```python
from typing import List, Any
from enum import Enum

class ChangeType(Enum):
    INSERT = 'insert'
    UPDATE = 'update'
    DELETE = 'delete'

class UnitOfWork:
    def __init__(self, session):
        self.session = session
        self._dirty: List[tuple] = []

    def register_change(self, entity: Any, change_type: ChangeType):
        self._dirty.append((entity, change_type))

    def commit(self):
        try:
            for entity, change_type in self._dirty:
                if change_type == ChangeType.INSERT:
                    self.session.add(entity)
                elif change_type == ChangeType.UPDATE:
                    self.session.merge(entity)
                elif change_type == ChangeType.DELETE:
                    self.session.delete(entity)
            self.session.commit()
            self._dirty.clear()
        except Exception:
            self.session.rollback()
            raise

    def rollback(self):
        self.session.rollback()
        self._dirty.clear()
```

### C\#

```csharp
public class UnitOfWork<TContext> where TContext : DbContext {
    private readonly TContext _context;
    private bool _disposed;

    public UnitOfWork(TContext context) => _context = context;

    public async Task<int> CommitAsync() {
        using var transaction = await _context.Database.BeginTransactionAsync();
        try {
            int changes = await _context.SaveChangesAsync();
            await transaction.CommitAsync();
            return changes;
        } catch {
            await transaction.RollbackAsync();
            throw;
        }
    }
}
```

## Best Practices

- Keep the UnitOfWork scope as small as possible
- Pair with repositories for a clean separation of concerns
- Use database transactions underneath for ACID guarantees
- Avoid keeping long-lived UnitOfWork instances in memory
- Consider ORM frameworks that provide implicit Unit of Work (JPA, EF)
- Rollback explicitly on domain errors before they reach the database

## Interview Questions

1. How does Unit of Work differ from database transactions?
2. Can you implement Unit of Work without an ORM?
3. How do you handle concurrency conflicts within a Unit of Work?
4. What happens if an error occurs after partial commits?
5. How does Unit of Work interact with the Repository Pattern?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Chapter 11
- Evans, Eric. *Domain-Driven Design*, Aggregates chapter
- Microsoft. *Unit of Work Pattern*
- Entity Framework Documentation. *Saving Data with Transactions*
