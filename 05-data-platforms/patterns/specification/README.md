# Specification Pattern

## Overview

The Specification Pattern encapsulates business rules into composable, reusable objects that can be combined using boolean logic. In the context of data access, specifications define query criteria that repositories can execute, decoupling query logic from repository implementations.

Specifications make complex query conditions explicit, testable, and chainable. They replace scattered if-conditions and complex query string building with well-structured objects.

## When to Use

- Query criteria are complex and involve multiple conditions
- Same query conditions are used across different repositories
- Business rules for filtering need to be testable in isolation
- Dynamic query building is required based on user input
- Combining multiple filters with AND/OR logic

## Implementation

### TypeScript

```typescript
interface Specification<T> {
  isSatisfiedBy(entity: T): boolean;
  toQuery(): string;
}

class AndSpecification<T> implements Specification<T> {
  constructor(private left: Specification<T>, private right: Specification<T>) {}

  isSatisfiedBy(entity: T): boolean {
    return this.left.isSatisfiedBy(entity) && this.right.isSatisfiedBy(entity);
  }

  toQuery(): string {
    return `(${this.left.toQuery()} AND ${this.right.toQuery()})`;
  }
}

class EmailSpecification implements Specification<User> {
  constructor(private domain: string) {}

  isSatisfiedBy(user: User): boolean {
    return user.email.endsWith(`@${this.domain}`);
  }

  toQuery(): string {
    return `email LIKE '%@${this.domain}'`;
  }
}

class ActiveSpecification implements Specification<User> {
  isSatisfiedBy(user: User): boolean {
    return user.isActive;
  }

  toQuery(): string {
    return `is_active = true`;
  }
}

// Composable queries
const activeUsersFromDomain = new AndSpecification(
  new ActiveSpecification(),
  new EmailSpecification('company.com')
);
```

### Java

```java
public interface Specification<T> {
    boolean isSatisfiedBy(T entity);
    String toPredicate();
    default Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }
}

public class ActiveUserSpecification implements Specification<User> {
    @Override
    public boolean isSatisfiedBy(User user) {
        return user.isActive();
    }

    @Override
    public String toPredicate() {
        return "status = 'ACTIVE'";
    }
}

public class EmailDomainSpecification implements Specification<User> {
    private final String domain;

    public EmailDomainSpecification(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean isSatisfiedBy(User user) {
        return user.getEmail().endsWith("@" + domain);
    }

    @Override
    public String toPredicate() {
        return "email LIKE '%" + domain + "'";
    }
}
```

### Python

```python
from abc import ABC, abstractmethod
from typing import Generic, TypeVar

T = TypeVar('T')

class Specification(ABC, Generic[T]):
    @abstractmethod
    def is_satisfied_by(self, entity: T) -> bool:
        pass

    @abstractmethod
    def to_sql(self) -> str:
        pass

    def and_(self, other: 'Specification[T]') -> 'AndSpecification':
        return AndSpecification(self, other)

    def or_(self, other: 'Specification[T]') -> 'OrSpecification':
        return OrSpecification(self, other)

class ActiveUserSpecification(Specification[User]):
    def is_satisfied_by(self, user: User) -> bool:
        return user.is_active

    def to_sql(self) -> str:
        return "is_active = true"

class MinAgeSpecification(Specification[User]):
    def __init__(self, min_age: int):
        self.min_age = min_age

    def is_satisfied_by(self, user: User) -> bool:
        return user.age >= self.min_age

    def to_sql(self) -> str:
        return f"age >= {self.min_age}"
```

### C\#

```csharp
public interface ISpecification<T> {
    bool IsSatisfiedBy(T entity);
    string ToExpression();
}

public class AndSpecification<T> : ISpecification<T> {
    private readonly ISpecification<T> _left;
    private readonly ISpecification<T> _right;

    public AndSpecification(ISpecification<T> left, ISpecification<T> right) {
        _left = left;
        _right = right;
    }

    public bool IsSatisfiedBy(T entity) =>
        _left.IsSatisfiedBy(entity) && _right.IsSatisfiedBy(entity);

    public string ToExpression() =>
        $"({_left.ToExpression()} AND {_right.ToExpression()})";
}
```

## Best Practices

- Keep specifications focused on a single business rule
- Make specifications composable through boolean operators
- Use specifications for both in-memory filtering and query generation
- Write unit tests for each specification independently
- Avoid embedding SQL in specifications when possible
- Name specifications after business concepts, not technical filters

## Interview Questions

1. How does the Specification Pattern differ from query builder patterns?
2. Can specifications work with both in-memory and database queries?
3. How do you handle specifications that cannot be translated to SQL?
4. What are the performance implications of combining many specifications?
5. How does this pattern relate to domain-driven design concepts?

## References

- Evans, Eric. *Domain-Driven Design*, chapter on Specifications
- Fowler, Martin. *Specification Pattern*
- Vernon, Vaughn. *Implementing Domain-Driven Design*
- Martin Fowler. *Checking Whether Something Happened*
