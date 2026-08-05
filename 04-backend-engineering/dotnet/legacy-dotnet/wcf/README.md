## Windows Communication Foundation (WCF)

Microsoft framework for building service-oriented applications with support for multiple protocols and messaging patterns.

## Overview

WCF provides a unified programming model for building service-oriented applications. It supports multiple transport protocols, message patterns, and security features.

## Why It Matters

- Enterprise-scale service communication
- Multiple binding options (HTTP, TCP, MSMQ)
- Complex security and transaction support
- Large existing codebase
- Migration target to gRPC

## Key Concepts

- **Service Contract**: Defines service operations
- **Data Contract**: Defines message data structure
- **Binding**: Transport and protocol configuration
- **Endpoint**: Address, binding, contract combination
- **Channel**: Communication infrastructure
- **Behavior**: Service/endpoint configuration

## Core Topics

- Service and data contracts
- Bindings (BasicHttp, NetTcp, WSHttp)
- Security configuration
- Instance management
- Transaction support
- Duplex communication
- WCF configuration

## Best Practices

- Use NetTcp for internal services
- Consider gRPC for new service development
- Keep contracts simple and versioned
- Use appropriate binding for scenario

## Hands-on Labs

- Create a WCF service
- Configure different bindings
- Implement duplex communication
- Migrate a WCF service to gRPC

## Interview Questions

1. What are the different WCF bindings?
2. How do you configure WCF security?
3. When should you migrate WCF to gRPC?

## References

- https://learn.microsoft.com/dotnet/framework/wcf/
- https://learn.microsoft.com/dotnet/framework/wcf/feature-details/
- https://learn.microsoft.com/dotnet/core/porting/wcf
