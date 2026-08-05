## Razor Pages

Page-based web development model for building dynamic web pages with clean separation of page logic and presentation.

## Overview

Razor Pages provide a page-focused development model where each page is self-contained with its own route, model, and view. It is simpler than MVC for page-based scenarios.

## Why It Matters

- Simpler than MVC for page-based applications
- Page-level organization reduces navigation complexity
- Built-in model binding and validation
- Supports complex page scenarios
- Good for form-heavy applications

## Key Concepts

- **Page Model**: Code-behind class handling page logic
- **Page Route**: Convention-based URL routing
- **Model Binding**: Automatic form-to-model mapping
- **Handler Methods**: OnGet, OnPost, OnGetAsync, OnPostAsync
- **Tag Helpers**: HTML helpers for server-side rendering
- **Partial Pages**: Reusable page components

## Core Topics

- Page structure and routing
- Handler methods (OnGet, OnPost, OnPut, OnDelete)
- Model binding and validation
- Page filters and global filters
- Partial pages and layout pages
- Data binding with asp-for tag helpers
- Page model services

## Best Practices

- Use handler methods for each HTTP verb
- Apply model validation with DataAnnotations
- Use partial pages for reusable components
- Keep page models focused and thin

## Hands-on Labs

- Build a form-heavy application with Razor Pages
- Implement CRUD operations using handler methods
- Create reusable partial pages
- Add page filters for logging

## Interview Questions

1. How does Razor Pages differ from MVC?
2. What are handler methods?
3. How does model binding work in Razor Pages?

## References

- https://learn.microsoft.com/aspnet/core/razor-pages/
- https://learn.microsoft.com/aspnet/core/razor-pages/razor-pages-conventions
- https://learn.microsoft.com/aspnet/core/razor-pages/ui-class
