# Repository Pattern

## Intent
Mediate between the domain and data mapping layers using a collection-like interface for accessing domain objects. Decouples business logic from data persistence concerns.

## Key Components
- **Entity**: Base class with identity (id)
- **Repository Interface**: Defines CRUD and query operations
- **In-Memory Implementation**: For testing and prototyping
- **Database Implementation**: Real persistence layer

## When to Use
- Domain logic should not depend on data access technology
- You need to swap persistence mechanisms without changing business code
- You want to unit test domain logic without a database

## Benefits
- Separation of concerns
- Testability (in-memory repos for unit tests)
- Swappable backends
- Domain-driven design alignment

## Example
```java
UserRepository repo = new InMemoryUserRepository();
repo.save(new User("john@example.com", "John"));
Optional<User> user = repo.findByEmail("john@example.com");
```
