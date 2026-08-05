## NuGet Package Management

NuGet is the package manager for .NET, providing library discovery, installation, and distribution.

## Overview

NuGet is the package ecosystem for .NET, hosting over 350,000 packages. It handles dependency resolution, versioning, and package distribution through public and private feeds.

## Why It Matters

- Enables code reuse across projects
- Handles complex dependency graphs automatically
- Provides private feed support for enterprise code
- Essential for library development and distribution
- Powers the .NET open-source ecosystem

## Key Concepts

- **Package Source**: Feed location (nuget.org, private feeds)
- **Package Version**: Semantic versioning with ranges
- **Dependencies**: Transitive dependency resolution
- **Package Reference**: Modern project-level dependency format
- **Global Packages Cache**: Shared package storage
- **NuGet.config**: Project/solution package source configuration
- **Symbol Packages**: Debug symbol distribution

## Core Topics

- Package installation and management
- PackageReference vs packages.config
- Version ranges and resolution
- Creating and publishing NuGet packages
- Private feed configuration
- Package lock files
- Central Package Management (CPM)

## Best Practices

- Use PackageReference format (not packages.config)
- Pin exact versions for deterministic builds
- Use Central Package Management for solutions
- Create symbol packages for debugging
- Validate package dependencies before publishing

## Hands-on Labs

- Create and publish a NuGet package
- Configure a private Azure Artifacts feed
- Set up Central Package Management
- Implement package version locking

## Interview Questions

1. What is the difference between PackageReference and packages.config?
2. How does NuGet dependency resolution work?
3. What is Central Package Management?

## References

- https://learn.microsoft.com/nuget/
- https://learn.microsoft.com/nuget/create-packages/
- https://learn.microsoft.com/nuget/consume-packages/
