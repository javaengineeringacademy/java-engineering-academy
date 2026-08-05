## ASP.NET MVC

Model-View-Controller pattern for building structured web applications with separation of concerns.

## Overview

ASP.NET MVC provides a mature, well-tested pattern for web development with strong separation of concerns, testability, and extensibility. It remains a solid choice for complex web applications.

## Why It Matters

- Clear separation of concerns
- Highly testable architecture
- Extensive ecosystem of filters and features
- Convention-based routing and configuration
- Strong community support

## Key Concepts

- **Model**: Data and business logic
- **View**: UI rendering with Razor syntax
- **Controller**: Request handling and response coordination
- **Routing**: URL pattern matching to controller actions
- **Filters**: Pre/post request processing
- **Model Binding**: Automatic request-to-model mapping
- **TempData/ViewBag**: Passing data between requests/views

## Core Topics

- Controller actions and routing
- Razor views and layout pages
- Model binding and validation
- Filters (authorization, action, result, exception)
- Areas for feature organization
- Tag helpers and HTML helpers
- Bundling and minification

## Best Practices

- Keep controllers thin, move logic to services
- Use model validation attributes
- Implement custom filters for cross-cutting concerns
- Use Areas for large applications
- Return ViewModels, not domain models

## Hands-on Labs

- Build an MVC CRUD application
- Implement custom model validation
- Create reusable tag helpers
- Add action filters for logging

## Interview Questions

1. How does model binding work in MVC?
2. What are the different filter types?
3. How do you test MVC controllers?
4. What is the difference between ViewBag and ViewData?

## References

- https://learn.microsoft.com/aspnet/core/mvc/
- https://learn.microsoft.com/aspnet/core/mvc/controllers/
- https://learn.microsoft.com/aspnet/core/mvc/views/
