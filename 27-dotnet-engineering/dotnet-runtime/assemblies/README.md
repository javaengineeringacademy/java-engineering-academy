## Assemblies in .NET

Assemblies are the fundamental units of deployment, versioning, and security in .NET applications.

## Overview

An assembly is a compiled code library containing CIL code, metadata, resources, and a manifest. Understanding assemblies is essential for deployment, versioning, and dependency management.

## Why It Matters

- Assemblies define deployment boundaries
- Versioning and strong naming depend on assembly structure
- Assembly loading affects plugin architectures
- Understanding assemblies helps with dependency resolution

## Key Concepts

- **Assembly Manifest**: Metadata about the assembly (name, version, culture)
- **Strong Naming**: Cryptographic signing for identity and tamper protection
- **Assembly Versioning**: Major.Minor.Build.Revision scheme
- **Satellite Assemblies**: Localized resource assemblies
- **Dynamic Assembly**: Runtime-generated assemblies
- **AssemblyLoadContext**: Isolation for assembly loading

## Core Topics

- Assembly structure and components
- Strong naming and key files
- Assembly versioning and binding redirects
- Assembly information and attributes
- Single-file and multi-file assemblies
- Dynamic assembly generation
- Assembly loading and resolution

## Best Practices

- Use semantic versioning for assembly versions
- Strong name shared assemblies
- Use AssemblyLoadContext for plugin isolation
- Keep assemblies focused and cohesive
- Use InternalsVisibleTo for test projects

## Hands-on Labs

- Create a strong-named assembly
- Analyze assembly metadata with ildasm
- Implement plugin loading with AssemblyLoadContext
- Set up binding redirects

## Interview Questions

1. What is the difference between a strong-named and unsigned assembly?
2. How does assembly versioning work?
3. What is AssemblyLoadContext and when should you use it?

## References

- https://learn.microsoft.com/dotnet/standard/assembly/
- https://learn.microsoft.com/dotnet/api/system.reflection.assembly
- https://learn.microsoft.com/dotnet/core/dependency-loading/
