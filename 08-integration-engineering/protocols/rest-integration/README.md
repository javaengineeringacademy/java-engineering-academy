# Integration Protocols - REST Integration

## Overview

REST (Representational State Transfer) is an architectural style for designing networked applications using HTTP methods.

## Table of Contents

1. [REST Principles](#rest-principles)
2. [HTTP Methods](#http-methods)
3. [REST Patterns](#rest-patterns)
4. [API Design](#api-design)
5. [Versioning](#versioning)

## REST Principles

### REST Constraints

| Constraint | Description |
|------------|-------------|
| Client-Server | Separation of concerns |
| Stateless | No client context on server |
| Cacheable | Response caching |
| Uniform Interface | Standard HTTP methods |
| Layered System | Hierarchical architecture |
| Code on Demand | Optional executable code |

## HTTP Methods

### Method Usage

| Method | Description | Idempotent | Safe |
|--------|-------------|------------|------|
| GET | Read resource | Yes | Yes |
| POST | Create resource | No | No |
| PUT | Update/Replace | Yes | No |
| PATCH | Partial update | No | No |
| DELETE | Delete resource | Yes | No |

### Example Usage

```http
GET /api/orders/123
POST /api/orders
PUT /api/orders/123
PATCH /api/orders/123
DELETE /api/orders/123
```

## REST Patterns

### Resource Naming

```
/api/orders           # Collection
/api/orders/123       # Specific resource
/api/orders/123/items # Sub-resource
```

### Pagination

```
GET /api/orders?page=1&size=10
GET /api/orders?offset=0&limit=10
```

### Filtering

```
GET /api/orders?status=PENDING
GET /api/orders?createdAfter=2024-01-01
```

### Sorting

```
GET /api/orders?sort=createdDate,desc
GET /api/orders?sortBy=total&order=desc
```

## API Design

### HATEOAS

```json
{
  "id": "123",
  "status": "PROCESSED",
  "links": [
    {"rel": "self", "href": "/api/orders/123"},
    {"rel": "cancel", "href": "/api/orders/123/cancel"},
    {"rel": "items", "href": "/api/orders/123/items"}
  ]
}
```

### Error Response

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid order data",
    "details": [
      {"field": "total", "error": "Must be positive"}
    ]
  }
}
```

## Versioning

### URI Versioning

```
/api/v1/orders
/api/v2/orders
```

### Header Versioning

```http
Accept: application/vnd.api.v1+json
Accept: application/vnd.api.v2+json
```

### Query Parameter

```
/api/orders?version=1
/api/orders?version=2
```

## Best Practices

1. **Use nouns for resources**: /orders not /getOrders
2. **Use HTTP methods correctly**: GET for read, POST for create
3. **Return appropriate status codes**: 200, 201, 404, 500
4. **Use HATEOAS**: Include links for discoverability
5. **Version your API**: Plan for versioning
6. **Document API**: Use OpenAPI/Swagger
7. **Secure endpoints**: Use authentication/authorization
8. **Rate limiting**: Implement rate limiting

## References

- [REST API Design](https://restfulapi.net/)
- [HTTP Methods](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods)
