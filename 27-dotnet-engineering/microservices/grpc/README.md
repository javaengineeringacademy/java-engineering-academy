## gRPC in .NET

High-performance, contract-based RPC framework for inter-service communication.

## Overview

gRPC is a high-performance RPC framework using Protocol Buffers for serialization and HTTP/2 for transport. .NET has first-class gRPC support for both client and server.

## Why It Matters

- High-performance binary serialization
- Strongly-typed contracts with Protobuf
- HTTP/2 multiplexing and streaming
- Excellent for inter-service communication
- Cross-language compatibility

## Key Concepts

- **Proto Files**: Service and message definitions
- **Protobuf**: Binary serialization format
- **Channels**: Client connection management
- **Unary RPC**: Single request/response
- **Server Streaming**: Server sends multiple responses
- **Client Streaming**: Client sends multiple requests
- **Bidirectional Streaming**: Both sides stream

## Core Topics

- Proto file definition
- Server implementation
- Client generation and usage
- Streaming patterns (unary, server, client, bidirectional)
- Interceptors for cross-cutting concerns
- Authentication and authorization
- Load balancing and service discovery

## Best Practices

- Use streaming for large data transfers
- Implement interceptors for logging and tracing
- Use gRPC for internal service communication
- Keep proto files in a shared project
- Implement proper error handling with Status

## Hands-on Labs

- Create a gRPC service with Proto files
- Implement all streaming patterns
- Add authentication to gRPC services
- Build a gRPC client with interceptors

## Interview Questions

1. How does gRPC differ from REST?
2. What streaming patterns does gRPC support?
3. How do interceptors work in gRPC?
4. When should you use gRPC over REST?

## References

- https://learn.microsoft.com/aspnet/core/grpc/
- https://learn.microsoft.com/aspnet/core/grpc/benchmarking
- https://grpc.io/docs/languages/csharp/basics/
