## Windows Services

Background applications that run without user interaction on Windows using .NET.

## Overview

Windows Services (formerly NT Services) are long-running executables that start with Windows and run in the background. Modern .NET provides Background Service as an alternative.

## Why It Matters

- Background processing without user interaction
- Auto-start with Windows
- Process monitoring and recovery
- Migration to Background Service recommended

## Key Concepts

- **ServiceBase**: Base class for Windows Services
- **ServiceInstaller**: Installation configuration
- **ServiceProcessInstaller**: Process-level settings
- **BackgroundService**: Modern .NET alternative
- **IHostedService**: Interface for hosted services

## Core Topics

- Creating Windows Services with .NET
- Service lifecycle (OnStart, OnStop, OnPause)
- Service installation and configuration
- Background Service in ASP.NET Core
- IHostedService implementation
- Health monitoring

## Best Practices

- Use Background Service for new development
- Implement proper error handling
- Use ILogger for diagnostic logging
- Configure service recovery options
- Consider containerization alternatives

## Hands-on Labs

- Create a Windows Service
- Implement Background Service
- Configure service recovery
- Deploy as a Windows Service

## Interview Questions

1. What is the difference between Windows Service and Background Service?
2. How do you handle errors in a background service?

## References

- https://learn.microsoft.com/dotnet/core/extensions/workers/
- https://learn.microsoft.com/dotnet/core/extensions/background-service
- https://learn.microsoft.com/windows/win32/services/services
