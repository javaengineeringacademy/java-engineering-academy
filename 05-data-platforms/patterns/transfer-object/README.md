# Data Transfer Object Pattern

## Overview

A Data Transfer Object (DTO) is an object that carries data between processes or layers without containing business logic. DTOs define the contract for data exchange across service boundaries, serialization formats, or architectural layers.

DTOs decouple internal domain models from external API contracts. They allow independent evolution of internal representations and public interfaces while preventing over-exposure of domain internals.

## When to Use

- Exposing domain objects directly would leak internal implementation details
- API contracts need to remain stable while internal models change
- Data needs to be serialized across network boundaries
- Multiple representations of the same entity are needed for different clients
- Aggregating data from multiple domain objects into a single response

## Implementation

### TypeScript

```typescript
class CreateUserDto {
  constructor(
    public readonly name: string,
    public readonly email: string,
    public readonly password: string
  ) {}
}

class UserResponseDto {
  constructor(
    public readonly id: string,
    public readonly name: string,
    public readonly email: string,
    public readonly createdAt: Date
  ) {}

  static fromDomain(user: User): UserResponseDto {
    return new UserResponseDto(
      user.id,
      user.name,
      user.email,
      user.createdAt
    );
  }
}

class UserService {
  async createUser(dto: CreateUserDto): Promise<UserResponseDto> {
    const user = new User(dto.name, dto.email, dto.password);
    await this.userRepository.save(user);
    return UserResponseDto.fromDomain(user);
  }
}
```

### Java

```java
public record CreateUserRequest(
    @NotBlank String name,
    @Email String email,
    @Size(min = 8) String password
) {}

public record UserResponse(
    String id,
    String name,
    String email,
    Instant createdAt
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}
```

### Python

```python
from pydantic import BaseModel, EmailStr
from datetime import datetime

class CreateUserDto(BaseModel):
    name: str
    email: EmailStr
    password: str

class UserResponseDto(BaseModel):
    id: str
    name: str
    email: str
    created_at: datetime

    @classmethod
    def from_domain(cls, user: 'User') -> 'UserResponseDto':
        return cls(
            id=user.id,
            name=user.name,
            email=user.email,
            created_at=user.created_at
        )
```

### C\#

```csharp
public record CreateUserRequest(
    string Name,
    string Email,
    string Password
);

public record UserResponse(
    string Id,
    string Name,
    string Email,
    DateTime CreatedAt
) {
    public static UserResponse FromDomain(User user) => new(
        user.Id,
        user.Name,
        user.Email,
        user.CreatedAt
    );
}
```

## Best Practices

- Keep DTOs as immutable data containers with no business logic
- Use separate DTOs for requests and responses
- Flatten nested domain objects into flat DTOs for API responses
- Validate DTOs at the boundary before passing to domain layer
- Use mapping libraries or static factory methods for conversion
- Version DTOs to support API evolution without breaking changes

## Interview Questions

1. What is the difference between a DTO and a Value Object?
2. When should you reuse DTOs across multiple API endpoints?
3. How do DTOs differ from the entity models in your domain?
4. What are the tradeoffs of flattening vs nesting in DTOs?
5. How do you handle DTO versioning for backward compatibility?

## References

- Fowler, Martin. *Patterns of Enterprise Application Architecture*, chapter on Data Transfer Object
- Microsoft. *Data Transfer Object pattern*
- Richardson, Chris. *Microservices Patterns*
- OpenAPI Specification for DTO documentation
