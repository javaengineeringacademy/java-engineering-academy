## Clean Architecture in .NET

Implementing Clean Architecture (Onion Architecture) for maintainable, testable .NET applications.

## Overview

Clean Architecture separates concerns into concentric layers with dependencies pointing inward. The domain layer is at the center, with application, infrastructure, and UI layers surrounding it.

## Why It Matters

- Clear separation of concerns
- High testability with dependency inversion
- Framework independence at the core
- Business logic isolation
- Maintainability over time

## Key Concepts

- **Domain Layer**: Core business entities and rules
- **Application Layer**: Use cases and orchestration
- **Infrastructure Layer**: External concerns (DB, APIs, files)
- **Presentation Layer**: UI and API controllers
- **Dependency Inversion**: Outer layers depend on inner layer interfaces
- **Entities vs Value Objects**: Domain modeling concepts

## Core Topics

- Layer structure and dependencies
- Domain entities and value objects
- Application services and use cases
- Repository interfaces and implementations
- Infrastructure adapters
- Presentation layer patterns
- Dependency injection configuration

## Best Practices

- Keep domain layer free of framework dependencies
- Define interfaces in application layer, implement in infrastructure
- Use Mediator for cross-cutting concerns
- Apply domain events for side effects
- Test each layer independently

## Hands-on Labs

- Scaffold a Clean Architecture solution
- Implement domain entities and value objects
- Create application services with MediatR
- Build infrastructure repositories
- Add API controllers in presentation layer

## Interview Questions

1. What is the dependency rule in Clean Architecture?
2. How do you keep the domain layer framework-independent?
3. What belongs in each architectural layer?

## References

- https://learn.microsoft.com/dotnet/architecture/modern-web-apps-azure/
- https://github.com/jasontaylordev/CleanArchitecture
- https://www.pluralsight.com/courses/clean-architecture-patterns-practices-net
