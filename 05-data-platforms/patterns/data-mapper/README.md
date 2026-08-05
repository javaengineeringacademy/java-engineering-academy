# Data Mapper Pattern

## Overview

The Data Mapper Pattern separates the in-memory objects from the database. A layer of mappers transfers data between objects and the database while keeping them independent of each other. The mapper itself knows how to read and write data without the domain objects knowing about the database.

Unlike Active Record, domain objects have no knowledge of the persistence mechanism. All mapping logic lives in dedicated mapper classes, keeping the domain model clean.

## When to Use

- Domain model should not depend on persistence framework
- Complex object-to-relational mappings exist
- Multiple representations of the same data are needed
- Domain objects are rich and cannot inherit from a base class
- Mapping logic is complex and belongs in its own layer

## Implementation

### TypeScript

```typescript
interface Mapper<TDomain, TRecord> {
  toDomain(record: TRecord): TDomain;
  toRecord(domain: TDomain): TRecord;
}

class UserMapper implements Mapper<User, UserRecord> {
  toDomain(record: UserRecord): User {
    return new User(
      record.id,
      record.first_name,
      record.last_name,
      record.email,
      new Date(record.created_at)
    );
  }

  toRecord(user: User): UserRecord {
    return {
      id: user.id,
      first_name: user.firstName,
      last_name: user.lastName,
      email: user.email,
      created_at: user.createdAt.toISOString()
    };
  }
}
```

### Java

```java
public class UserMapper {
    public User toDomain(UserEntity entity) {
        return User.builder()
            .id(entity.getId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .email(entity.getEmail())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    public UserEntity toEntity(User domain) {
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
```

### Python

```python
class UserMapper:
    def to_domain(self, record: dict) -> User:
        return User(
            id=record['id'],
            first_name=record['first_name'],
            last_name=record['last_name'],
            email=record['email'],
            created_at=record['created_at']
        )

    def to_record(self, user: User) -> dict:
        return {
            'id': user.id,
            'first_name': user.first_name,
            'last_name': user.last_name,
            'email': user.email,
            'created_at': user.created_at.isoformat()
        }

class UserRepository:
    def __init__(self, session, mapper: UserMapper):
        self.session = session
        self.mapper = mapper

    def find_by_id(self, id: str):
        row = self.session.execute(
            'SELECT * FROM users WHERE id = :id', {'id': id}
        ).fetchone()
        return self.mapper.to_domain(row) if row else None
```

### C\#

```csharp
public class UserMapper {
    public User ToDomain(UserRecord record) => new User {
        Id = record.Id,
        FirstName = record.FirstName,
        LastName = record.LastName,
        Email = record.Email,
        CreatedAt = record.CreatedAt
    };

    public UserRecord ToRecord(User user) => new UserRecord {
        Id = user.Id,
        FirstName = user.FirstName,
        LastName = user.LastName,
        Email = user.Email,
        CreatedAt = user.CreatedAt
    };
}
```

## Best Practices

- Keep mappers focused on a single entity type
- Place mapper interfaces in the domain layer, implementations in infrastructure
- Handle null and edge cases in mapping logic
- Use mapping libraries for simple transformations
- Write unit tests for mapper correctness
- Consider bidirectional mapping for round-trip persistence

## Interview Questions

1. What is the difference between Data Mapper and Active Record?
2. When should you use a mapping library instead of custom mappers?
3. How do you handle mapping between different data models (e.g., GraphQL to domain)?
4. How does Data Mapper support domain-driven design?
5. What are the performance implications of mapping layers?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, Chapter 12
- Evans, Eric. *Domain-Driven Design*
- ORM Framework Documentation (Hibernate, SQLAlchemy, Entity Framework)
- Vernon, Vaughn. *Implementing Domain-Driven Design*
