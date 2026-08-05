## Blazor

Full-stack C# web development using Razor components for interactive client-side and server-side UI.

## Overview

Blazor enables building interactive web UIs entirely in C# without JavaScript. It supports both WebAssembly (client-side) and Server (SignalR-based) hosting models.

## Why It Matters

- Full-stack C# without JavaScript
- WebAssembly support for client-side execution
- Server-side rendering with live connection
- Component-based architecture
- Shared code between client and server

## Key Concepts

- **Blazor WebAssembly**: Client-side execution in browser via WebAssembly
- **Blazor Server**: Server-side execution with SignalR connection
- **Razor Components**: Reusable UI components with .razor files
- **Component Lifecycle**: OnInitialized, OnParametersSet, OnAfterRender
- **JS Interop**: Calling JavaScript from C# and vice versa
- **State Management**: CascadingParameters, Scoped services

## Core Topics

- Blazor WebAssembly vs Server hosting
- Component model and lifecycle
- Data binding (one-way, two-way, events)
- Routing and navigation
- JavaScript interop
- Authentication and authorization
- Deployment and optimization

## Best Practices

- Use Blazor Server for real-time apps
- Use WebAssembly for offline-capable apps
- Minimize JavaScript interop calls
- Use CascadingParameters for shared state
- Implement proper error boundaries

## Hands-on Labs

- Build a Blazor WebAssembly app
- Create reusable Razor components
- Implement authentication with OIDC
- Deploy to Azure Static Web Apps

## Interview Questions

1. What is the difference between Blazor WebAssembly and Blazor Server?
2. How does the component lifecycle work?
3. How do you call JavaScript from C# in Blazor?
4. What are CascadingParameters?

## References

- https://learn.microsoft.com/aspnet/core/blazor/
- https://learn.microsoft.com/aspnet/core/blazor/webassembly-build-tools-and-aot/
- https://github.com/dotnet/blazor-samples
