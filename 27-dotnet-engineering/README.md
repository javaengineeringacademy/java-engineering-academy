## 27-dotnet-engineering

Comprehensive guide to the .NET engineering ecosystem covering language, runtime, web frameworks, data access, security, cloud, testing, and modernization strategies.

## Overview

.NET is a free, open-source developer platform for building many types of applications. From web APIs to desktop apps, cloud-native services to IoT, .NET provides a unified ecosystem with consistent APIs and tooling across platforms.

## Why It Matters

- Cross-platform support (Windows, Linux, macOS)
- High performance with modern language features
- Massive ecosystem of libraries and frameworks
- Strong enterprise adoption and long-term support
- Active open-source community and governance

## Key Concepts

- **C# Language**: Modern, type-safe, object-oriented language
- **.NET Runtime (CLR)**: Managed execution environment with garbage collection
- **ASP.NET Core**: Cross-platform web framework
- **Data Access**: EF Core, Dapper, ADO.NET
- **Security**: Identity, OAuth2, JWT, OIDC
- **Microservices**: Clean Architecture, DDD, CQRS, gRPC
- **Cloud**: Azure Functions, Service Bus, Cosmos DB, AKS
- **Testing**: xUnit, NUnit, MSTest, Moq
- **Legacy**: .NET Framework, WCF, Web Forms
- **Modernization**: Migration strategies and patterns

## Core Topics

- C# Language (Fundamentals, OOP, Advanced, LINQ, Async/Await)
- .NET Runtime (CLR, CTS, CLS, JIT, GC, Assemblies, NuGet)
- ASP.NET (MVC, Core, Minimal APIs, Razor Pages, Blazor, SignalR, Web API)
- Data Access (ADO.NET, Entity Framework, EF Core, Dapper, LINQ)
- Security (Identity, OAuth2, OIDC, JWT, Auth Patterns)
- Microservices (Clean Architecture, DDD, CQRS, MediatR, Event-Driven, gRPC)
- Cloud (Azure, Azure Functions, Service Bus, Storage, SQL, Cosmos DB, AKS)
- Testing (xUnit, NUnit, MSTest, Moq, Integration Testing)
- Legacy .NET (Web Forms, WCF, .NET Framework, Remoting, Enterprise Services)
- Modernization (Framework Migration, WCF to gRPC, Web Forms to Blazor, Azure Migration)

## Best Practices

- Target the latest LTS release (.NET 8 or .NET 9)
- Use nullable reference types for null safety
- Write unit tests for business logic
- Apply SOLID principles in architecture
- Use dependency injection throughout
- Follow async all the way for I/O-bound operations
- Use structured logging with ILogger
- Implement health checks for production services

## Hands-on Labs

- Build a RESTful API with ASP.NET Core Minimal APIs
- Implement JWT authentication with ASP.NET Identity
- Create a microservices solution with Clean Architecture
- Deploy to Azure using AKS and Azure Functions
- Write integration tests with WebApplicationFactory
- Migrate a .NET Framework project to .NET 8

## Interview Questions

1. What are the key differences between .NET Framework and .NET Core?
2. Explain the CLR garbage collection generations.
3. What is the difference between ValueTask and Task?
4. How does dependency injection work in ASP.NET Core?
5. Explain the difference between synchronous and asynchronous programming in .NET.
6. What are the benefits of using records in C#?
7. How does EF Core differ from EF6?
8. What is the role of the Common Type System (CTS)?

## References

- https://learn.microsoft.com/dotnet/
- https://learn.microsoft.com/aspnet/core/
- https://learn.microsoft.com/dotnet/csharp/
- https://learn.microsoft.com/dotnet/standard/
- https://github.com/dotnet
