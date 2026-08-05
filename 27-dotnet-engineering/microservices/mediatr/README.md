## MediatR Library

In-memory mediator implementing Mediator pattern for decoupling request handling in .NET applications.

## Overview

MediatR is a popular .NET library that implements the Mediator pattern, providing a simple way to send requests and publish notifications within a process. It is widely used with CQRS and Clean Architecture.

## Why It Matters

- Decouples request sender from handler
- Reduces cross-cutting concern boilerplate
- Enables pipeline behaviors for cross-cutting logic
- Foundation for CQRS implementation
- Excellent integration with ASP.NET Core

## Key Concepts

- **IRequest<T>:** Request that returns a response
- **IRequestHandler<TRequest, TResponse>:** Handles requests
- **INotification**: Publish notification to multiple handlers
- **INotificationHandler<T>:** Handles notifications
- **IPipelineBehavior<TRequest, TResponse>:** Request pipeline
- **IRequestPreProcessor / IRequestPostProcessor**: Pre/post processing

## Core Topics

- Setting up MediatR in ASP.NET Core
- Implementing request/response handlers
- Publishing notifications to multiple handlers
- Pipeline behaviors for cross-cutting concerns
- Validation with FluentValidation integration
- Exception handling behaviors
- Logging and telemetry behaviors

## Best Practices

- Use pipeline behaviors for cross-cutting concerns
- Keep handlers focused and small
- Use FluentValidation for command validation
- Use notifications for side effects
- Avoid circular dependencies between handlers

## Hands-on Labs

- Set up MediatR in an ASP.NET Core project
- Implement a CQRS pattern with MediatR
- Add validation pipeline behavior
- Add logging and exception handling behaviors

## Interview Questions

1. What is the difference between requests and notifications in MediatR?
2. How do pipeline behaviors work?
3. When should you use MediatR over direct service calls?

## References

- https://github.com/jbogard/MediatR
- https://learn.microsoft.com/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/
- https://www.benfoster.io/blog/mediatr-pipeline-behaviours/
