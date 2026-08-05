## SignalR

Real-time web communication framework for server-to-client messaging over WebSockets, Server-Sent Events, and long polling.

## Overview

SignalR provides abstraction over real-time transport protocols, enabling server push to connected clients. It supports hubs for grouped messaging and automatic reconnection.

## Why It Matters

- Real-time features like chat, notifications, live dashboards
- Automatic transport negotiation
- Built-in scaling with Azure SignalR Service
- Strongly-typed hubs
- Client libraries for .NET, JavaScript, Java

## Key Concepts

- **Hub**: High-level pipeline for client-server communication
- **Hub Connection**: Client-side connection to a hub
- **Group**: Logical grouping of clients for targeted messaging
- **Transport**: WebSockets, Server-Sent Events, Long Polling
- **Stateful Redis Backplane**: Scaling SignalR across servers

## Core Topics

- Hub creation and methods
- Client connection and invocation
- Groups and group management
- Authentication and authorization
- Scaling with Azure SignalR Service
- Strongly-typed hubs
- Streaming with IAsyncEnumerable

## Best Practices

- Use strongly-typed hubs for better IntelliSense
- Implement proper authentication for hubs
- Use groups for targeted messaging
- Scale with Azure SignalR Service for production
- Handle client reconnection gracefully

## Hands-on Labs

- Build a real-time chat application
- Implement a live notification system
- Create a dashboard with live data updates
- Scale SignalR with Azure SignalR Service

## Interview Questions

1. What transport protocols does SignalR support?
2. How does SignalR scale across multiple servers?
3. What are hubs and how do they work?
4. How does authentication work with SignalR?

## References

- https://learn.microsoft.com/aspnet/core/signalr/
- https://learn.microsoft.com/aspnet/core/signalr/hubs
- https://learn.microsoft.com/azure/azure-signalr/
