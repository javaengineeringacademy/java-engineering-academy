## Web API Design

Designing and building RESTful Web APIs using ASP.NET Core with proper HTTP semantics and resource modeling.

## Overview

Web API design covers creating HTTP-based services with proper REST conventions, content negotiation, versioning, and documentation. ASP.NET Core provides excellent support for building production-ready APIs.

## Why It Matters

- APIs are the backbone of modern distributed systems
- Proper design improves developer experience
- REST conventions enable predictable interfaces
- API versioning supports evolution without breaking consumers

## Key Concepts

- **REST**: Resource-oriented architecture with HTTP verbs
- **Content Negotiation**: Serving different formats based on Accept header
- **API Versioning**: URL, header, or query string versioning
- **HATEOAS**: Hypermedia as the engine of application state
- **OpenAPI/Swagger**: API documentation standard
- **Rate Limiting**: Controlling request frequency

## Core Topics

- RESTful resource design and URL patterns
- HTTP methods and status codes
- Content negotiation and formatters
- API versioning strategies
- Pagination, filtering, sorting
- Error handling and problem details
- Rate limiting and throttling
- API documentation with Swashbuckle

## Best Practices

- Use plural nouns for resource URLs
- Return appropriate HTTP status codes
- Implement pagination for list endpoints
- Use DTOs to decouple API contracts from domain
- Document APIs with OpenAPI/Swagger

## Hands-on Labs

- Design a RESTful API for an e-commerce system
- Implement API versioning
- Add rate limiting middleware
- Generate OpenAPI documentation

## Interview Questions

1. What are the principles of RESTful API design?
2. How do you handle API versioning?
3. What HTTP status codes should you use and when?
4. How does content negotiation work?

## References

- https://learn.microsoft.com/aspnet/core/web-api/
- https://learn.microsoft.com/aspnet/core/web-api/advanced/conventions
- https://learn.microsoft.com/aspnet/core/web-api/http-methods
