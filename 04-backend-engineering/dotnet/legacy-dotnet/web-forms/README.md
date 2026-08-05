## ASP.NET Web Forms

Event-driven web development model using server controls and postback for interactive web applications.

## Overview

Web Forms was the original ASP.NET web development model, providing a rich server-side component model with drag-and-drop controls and event handling similar to Windows Forms development.

## Why It Matters

- Large existing codebase in production
- Understanding helps with migration to Blazor
- Event-driven model familiar to Windows developers
- Server controls provide rapid development

## Key Concepts

- **Server Controls**: HTML elements with server-side behavior
- **Postback**: Full page round-trip for state changes
- **ViewState**: Server-side state management
- **User Controls**: Reusable UI components
- **Master Pages**: Shared page layouts
- **Skin/CSS**: Theming and styling

## Core Topics

- Page lifecycle and events
- Server controls and data binding
- ViewState and state management
- Postback and cross-page posting
- Master pages and themes
- User controls and custom controls
- Membership and role management

## Best Practices

- Minimize ViewState for performance
- Use AJAX for partial page updates
- Separate business logic from code-behind
- Consider migration to Blazor for new development

## Hands-on Labs

- Analyze a Web Forms application structure
- Identify migration candidates
- Compare Web Forms to Blazor components

## Interview Questions

1. How does ViewState work in Web Forms?
2. What are the limitations of Web Forms?
3. How does the page lifecycle work?

## References

- https://learn.microsoft.com/previous-versions/aspnet/ms178139(v=vs.100)
- https://learn.microsoft.com/dotnet/architecture/blazor-migration/
