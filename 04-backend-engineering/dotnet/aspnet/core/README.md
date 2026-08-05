## ASP.NET Core

Cross-platform, high-performance web framework for building modern cloud applications.

## Overview

ASP.NET Core is the modern, cross-platform evolution of ASP.NET, designed for cloud-native applications with high performance, dependency injection, and middleware pipeline.

## Why It Matters

- Runs on Windows, Linux, macOS
- High performance (consistently top in TechEmpower benchmarks)
- Built-in dependency injection
- Middleware-based architecture
- Cloud-ready with Docker and Kubernetes support

## Key Concepts

- **Middleware Pipeline**: Request processing chain
- **Dependency Injection**: Built-in IoC container
- **Configuration**: Hierarchical configuration from multiple sources
- **Logging**: Built-in structured logging
- **Health Checks**: Application health monitoring
- **Kestrel**: High-performance HTTP server
- **Minimal Hosting**: Simplified app startup

## Core Topics

- Host builder and minimal hosting model
- Middleware pipeline and custom middleware
- Dependency injection and service lifetimes
- Configuration providers (appsettings, environment, Azure)
- Logging with ILogger and providers
- Health checks and readiness probes
- Error handling and exception middleware

## Best Practices

- Use the minimal hosting model for new projects
- Register services with appropriate lifetimes (Scoped, Singleton, Transient)
- Use configuration hierarchy for environment-specific settings
- Implement structured logging with correlation IDs
- Add health checks for production deployments

## Hands-on Labs

- Build an ASP.NET Core app with minimal hosting
- Create custom middleware for request logging
- Implement health check endpoints
- Configure multi-environment settings

## Interview Questions

1. How does the ASP.NET Core middleware pipeline work?
2. What are the differences between Scoped, Singleton, and Transient?
3. How does configuration hierarchy work?
4. What is Kestrel and why is it used?

## References

- https://learn.microsoft.com/aspnet/core/fundamentals/
- https://learn.microsoft.com/aspnet/core/fundamentals/middleware/
- https://learn.microsoft.com/aspnet/core/fundamentals/dependency-injection/
