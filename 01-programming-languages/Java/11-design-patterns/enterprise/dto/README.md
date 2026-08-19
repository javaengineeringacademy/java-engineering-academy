# DTO (Data Transfer Object) Pattern

## Intent
Transfer data between processes or layers without exposing internal domain model details. DTOs carry data across boundaries (API, network, layers) while keeping the domain clean.

## Key Components
- **Domain Entity**: Rich business object with behavior
- **DTO**: Flat data container for transfer
- **Mapper**: Converts between domain and DTO

## When to Use
- API responses should not expose entity internals (passwords, internal IDs)
- Reducing network calls by flattening nested objects
- Decoupling API contract from database schema
- Preventing over-fetching or under-fetching

## Benefits
- API stability independent of domain changes
- Security (hides sensitive fields)
- Reduced payload size
- Clear boundary contracts

## Example
```java
User user = new User(1L, "secret", "Alice", "alice@example.com");
UserDTO dto = UserMapper.toDTO(user);
// dto has no password field
```
