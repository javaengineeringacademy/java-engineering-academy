## Web Forms to Blazor Migration

Migrating ASP.NET Web Forms applications to Blazor for modern, interactive web development.

## Overview

Web Forms applications can be migrated to Blazor to use C#-based UI development without JavaScript. Blazor provides a similar component-based model with modern capabilities.

## Why It Matters

- Web Forms is no longer actively developed
- Blazor provides similar C#-based UI model
- Modern component architecture
- No ViewState overhead
- Better performance and smaller payloads

## Key Concepts

- **Component Mapping**: Server controls to Blazor components
- **Event Handlers**: Postback to Blazor event handling
- **Data Binding**: Server-side binding to Blazor binding
- **State Management**: ViewState to component state
- **Master Pages**: To Layout components

## Core Topics

- Mapping server controls to components
- Migrating data binding
- Converting event handlers
- State management migration
- Layout and navigation
- Authentication migration
- Incremental migration strategy

## Best Practices

- Use the strangler fig pattern
- Migrate page by page
- Start with new features in Blazor
- Test each migrated page thoroughly
- Consider Blazor WebAssembly for client-side

## Hands-on Labs

- Map Web Forms controls to Blazor components
- Convert a simple page to Blazor
- Implement navigation between Blazor pages
- Migrate authentication

## Interview Questions

1. How do Web Forms server controls map to Blazor components?
2. What replaces ViewState in Blazor?
3. How do you do incremental migration?

## References

- https://learn.microsoft.com/dotnet/architecture/blazor-migration/
- https://learn.microsoft.com/aspnet/core/blazor/
- https://learn.microsoft.com/dotnet/core/porting/
