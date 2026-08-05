## .NET Remoting

Legacy .NET technology for cross-domain and cross-process communication using serialized objects.

## Overview

.NET Remoting was the original mechanism for inter-process and cross-domain communication in .NET Framework. It has been replaced by WCF and gRPC.

## Why It Matters

- Legacy systems may still use Remoting
- Understanding helps with migration to gRPC
- Historical context for modern IPC patterns

## Key Concepts

- **MarshalByRefObject**: Objects that can be accessed across domains
- **Remoting Channel**: Communication transport (TCP, HTTP)
- **Sink**: Message processing pipeline
- **Well-Known Objects**: Server-activated objects
- **Client-Activated Objects**: Client-created instances
- **Lease-Based lifetime**: Distributed garbage collection

## Core Topics

- Remote object creation and hosting
- Channel configuration (TCP, HTTP)
- Object lifetime management
- Serialization for remoting
-ponsor objects for remote objects
- Security configuration
- Migration to WCF/gRPC

## Best Practices

- Migrate to gRPC for new development
- Use MarshalByRefObject carefully
- Configure proper lifetime management

## Hands-on Labs

- Identify Remoting usage in codebase
- Plan migration to gRPC
- Compare Remoting to modern alternatives

## Interview Questions

1. What replaced .NET Remoting?
2. How did MarshalByRefObject work?
3. What are the limitations of .NET Remoting?

## References

- https://learn.microsoft.com/dotnet/framework/remoting/
- https://learn.microsoft.com/dotnet/framework/remoting/overview
