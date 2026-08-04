# API Documentation

## OpenAPI (Swagger)

```yaml
openapi: 3.0.0
info:
  title: Order API
  version: 1.0.0
paths:
  /orders:
    get:
      summary: List orders
      responses:
        '200':
          description: Success
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Order'
```

## Tools

- Swagger UI
- Redoc
- Postman
- Stoplight

## Best Practices

1. Document all endpoints
2. Include request/response examples
3. Document error responses
4. Version your API docs
