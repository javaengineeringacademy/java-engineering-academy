# OpenAPI Specification

## Comprehensive Guide to OpenAPI 3.0

OpenAPI 3.0 is a specification for describing RESTful APIs. This guide covers paths, components, security, and best practices for API documentation.

---

## Table of Contents

1. [OpenAPI Overview](#openapi-overview)
2. [Document Structure](#document-structure)
3. [Paths and Operations](#paths-and-operations)
4. [Components](#components)
5. [Security](#security)
6. [Examples](#examples)
7. [Best Practices](#best-practices)

---

## OpenAPI Overview

### What is OpenAPI?

```yaml
# Basic OpenAPI structure
openapi: 3.0.3
info:
  title: User Service API
  description: API for managing users
  version: 1.0.0
  contact:
    name: API Support
    email: support@example.com
  license:
    name: MIT
    url: https://opensource.org/licenses/MIT

servers:
  - url: https://api.example.com/v1
    description: Production server
  - url: https://staging-api.example.com/v1
    description: Staging server
  - url: http://localhost:8080/v1
    description: Local development

paths:
  /users:
    get:
      summary: List all users
      operationId: listUsers
      tags:
        - Users
      responses:
        '200':
          description: Successful operation
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/User'
```

---

## Document Structure

### Info Object

```yaml
info:
  title: Petstore API
  description: |
    This is a sample server for a pet store.
    
    ## Features
    - User management
    - Pet listings
    - Order processing
    
    ## Rate Limiting
    - 100 requests per minute for authenticated users
    - 20 requests per minute for unauthenticated users
  version: 2.0.0
  termsOfService: https://example.com/terms
  contact:
    name: API Support Team
    url: https://example.com/support
    email: api-support@example.com
  license:
    name: Apache 2.0
    url: https://www.apache.org/licenses/LICENSE-2.0.html
```

### Servers

```yaml
servers:
  - url: https://api.example.com/v1
    description: Production
    variables:
      version:
        default: v1
        enum:
          - v1
          - v2
        description: API version
      environment:
        default: production
        enum:
          - production
          - staging
          - development

  - url: https://{environment}.api.example.com/{version}
    description: Multi-environment
    variables:
      environment:
        default: api
        enum:
          - api
          - api-staging
          - api-dev
      version:
        default: v1
```

### Tags

```yaml
tags:
  - name: Users
    description: User management operations
    externalDocs:
      description: User guide
      url: https://example.com/docs/users

  - name: Orders
    description: Order processing operations

  - name: Products
    description: Product catalog operations
```

---

## Paths and Operations

### Path Item

```yaml
paths:
  /users:
    get:
      summary: List users
      description: Returns a list of users
      operationId: listUsers
      tags:
        - Users
      parameters:
        - $ref: '#/components/parameters/LimitParam'
        - $ref: '#/components/parameters/OffsetParam'
      responses:
        '200':
          description: Successful operation
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserList'
        '400':
          $ref: '#/components/responses/BadRequest'
        '401':
          $ref: '#/components/responses/Unauthorized'
      security:
        - bearerAuth: []

    post:
      summary: Create user
      description: Creates a new user
      operationId: createUser
      tags:
        - Users
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateUserRequest'
      responses:
        '201':
          description: User created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
        '400':
          $ref: '#/components/responses/BadRequest'
        '409':
          description: User already exists
```

### Parameters

```yaml
paths:
  /users/{userId}:
    parameters:
      - name: userId
        in: path
        required: true
        description: User ID
        schema:
          type: string
          format: uuid

    get:
      summary: Get user by ID
      operationId: getUserById
      parameters:
        - name: fields
          in: query
          description: Fields to include
          required: false
          schema:
            type: array
            items:
              type: string
              enum:
                - id
                - name
                - email
                - profile
          style: form
          explode: false

components:
  parameters:
    LimitParam:
      name: limit
      in: query
      description: Maximum number of items to return
      required: false
      schema:
        type: integer
        minimum: 1
        maximum: 100
        default: 20

    OffsetParam:
      name: offset
      in: query
      description: Number of items to skip
      required: false
      schema:
        type: integer
        minimum: 0
        default: 0

    PageSizeParam:
      name: pageSize
      in: query
      description: Page size
      required: false
      schema:
        type: integer
        minimum: 1
        maximum: 100
        default: 10
```

### Request Body

```yaml
paths:
  /users:
    post:
      requestBody:
        required: true
        description: User to create
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateUserRequest'
            examples:
              simple:
                summary: Simple user
                value:
                  name: John Doe
                  email: john@example.com
              full:
                summary: Complete user
                value:
                  name: John Doe
                  email: john@example.com
                  profile:
                    bio: Software developer
                    age: 30
                  roles:
                    - user
                    - admin

          multipart/form-data:
            schema:
              type: object
              properties:
                avatar:
                  type: string
                  format: binary
                name:
                  type: string
                email:
                  type: string
                  format: email
```

### Responses

```yaml
paths:
  /users/{userId}:
    get:
      responses:
        '200':
          description: Successful operation
          headers:
            X-Rate-Limit:
              description: Rate limit
              schema:
                type: integer
            X-Rate-Remaining:
              description: Remaining requests
              schema:
                type: integer
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
              examples:
                success:
                  summary: Successful response
                  value:
                    id: 123e4567-e89b-12d3-a456-426614174000
                    name: John Doe
                    email: john@example.com

        '404':
          $ref: '#/components/responses/NotFound'

        '500':
          $ref: '#/components/responses/InternalServerError'
```

---

## Components

### Schemas

```yaml
components:
  schemas:
    User:
      type: object
      required:
        - id
        - name
        - email
      properties:
        id:
          type: string
          format: uuid
          readOnly: true
          description: User ID
        name:
          type: string
          minLength: 1
          maxLength: 100
          description: User name
        email:
          type: string
          format: email
          description: User email
        profile:
          $ref: '#/components/schemas/UserProfile'
        roles:
          type: array
          items:
            $ref: '#/components/schemas/UserRole'
          uniqueItems: true
        createdAt:
          type: string
          format: date-time
          readOnly: true
        updatedAt:
          type: string
          format: date-time
          readOnly: true
      additionalProperties: false

    UserProfile:
      type: object
      properties:
        bio:
          type: string
          maxLength: 500
        age:
          type: integer
          minimum: 0
          maximum: 150
        avatar:
          type: string
          format: uri

    UserRole:
      type: string
      enum:
        - admin
        - user
        - guest

    CreateUserRequest:
      type: object
      required:
        - name
        - email
        - password
      properties:
        name:
          type: string
          minLength: 1
          maxLength: 100
        email:
          type: string
          format: email
        password:
          type: string
          minLength: 8
          format: password
        profile:
          $ref: '#/components/schemas/UserProfile'

    UserList:
      type: object
      properties:
        data:
          type: array
          items:
            $ref: '#/components/schemas/User'
        pagination:
          $ref: '#/components/schemas/Pagination'

    Pagination:
      type: object
      properties:
        total:
          type: integer
        limit:
          type: integer
        offset:
          type: integer
        hasMore:
          type: boolean
```

### Response Components

```yaml
components:
  responses:
    BadRequest:
      description: Bad request
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Error'

    Unauthorized:
      description: Unauthorized
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Error'

    NotFound:
      description: Resource not found
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Error'

    InternalServerError:
      description: Internal server error
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Error'

  schemas:
    Error:
      type: object
      required:
        - code
        - message
      properties:
        code:
          type: string
          description: Error code
        message:
          type: string
          description: Error message
        details:
          type: array
          items:
            type: object
            properties:
              field:
                type: string
              message:
                type: string
        timestamp:
          type: string
          format: date-time
```

### Security Schemes

```yaml
components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
      description: JWT token authentication

    apiKey:
      type: apiKey
      in: header
      name: X-API-Key
      description: API key authentication

    oauth2:
      type: oauth2
      flows:
        authorizationCode:
          authorizationUrl: https://auth.example.com/authorize
          tokenUrl: https://auth.example.com/token
          refreshUrl: https://auth.example.com/token/refresh
          scopes:
            read: Read access
            write: Write access
            admin: Admin access

    openIdConnect:
      type: openIdConnect
      openIdConnectUrl: https://auth.example.com/.well-known/openid-configuration
```

---

## Security

### Security Requirements

```yaml
# Global security
security:
  - bearerAuth: []

# Per-operation security
paths:
  /users:
    get:
      security:
        - bearerAuth: []
    post:
      security:
        - bearerAuth: []
          oauth2:
            - write

  /admin/users:
    get:
      security:
        - bearerAuth: []
          oauth2:
            - admin
```

---

## Examples

### Complete API Specification

```yaml
openapi: 3.0.3
info:
  title: User Management API
  description: API for managing user accounts
  version: 1.0.0

servers:
  - url: https://api.example.com/v1

tags:
  - name: Users
    description: User operations

paths:
  /users:
    get:
      tags: [Users]
      summary: List users
      operationId: listUsers
      parameters:
        - $ref: '#/components/parameters/LimitParam'
        - $ref: '#/components/parameters/OffsetParam'
      responses:
        '200':
          description: User list
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserList'
      security:
        - bearerAuth: []

    post:
      tags: [Users]
      summary: Create user
      operationId: createUser
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateUserRequest'
      responses:
        '201':
          description: User created
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
      security:
        - bearerAuth: []

  /users/{userId}:
    get:
      tags: [Users]
      summary: Get user
      operationId: getUser
      parameters:
        - name: userId
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: User details
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
        '404':
          $ref: '#/components/responses/NotFound'
      security:
        - bearerAuth: []

components:
  schemas:
    User:
      type: object
      properties:
        id:
          type: string
          format: uuid
        name:
          type: string
        email:
          type: string
          format: email

    CreateUserRequest:
      type: object
      required: [name, email]
      properties:
        name:
          type: string
        email:
          type: string
          format: email

    UserList:
      type: object
      properties:
        data:
          type: array
          items:
            $ref: '#/components/schemas/User'
        pagination:
          type: object
          properties:
            total:
              type: integer
            hasMore:
              type: boolean

  parameters:
    LimitParam:
      name: limit
      in: query
      schema:
        type: integer
        default: 20

    OffsetParam:
      name: offset
      in: query
      schema:
        type: integer
        default: 0

  responses:
    NotFound:
      description: Resource not found

  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

---

## Best Practices

### 1. Use Consistent Naming

```yaml
# Good
paths:
  /users:
  /users/{userId}
  /users/{userId}/posts
  /users/{userId}/posts/{postId}

# Bad
paths:
  /users:
  /user/{id}
  /userPosts
  /posts/user/{userId}
```

### 2. Use $ref for Reuse

```yaml
components:
  schemas:
    User:
      type: object
      properties:
        id:
          type: string
          format: uuid
        name:
          type: string

    UserList:
      type: object
      properties:
        data:
          type: array
          items:
            $ref: '#/components/schemas/User'

paths:
  /users:
    get:
      responses:
        '200':
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserList'
```

### 3. Document All Responses

```yaml
responses:
  '200':
    description: Success
  '400':
    description: Bad request
  '401':
    description: Unauthorized
  '403':
    description: Forbidden
  '404':
    description: Not found
  '500':
    description: Server error
```

### 4. Use Examples

```yaml
responses:
  '200':
    content:
      application/json:
        examples:
          success:
            summary: Successful response
            value:
              id: 123e4567-e89b-12d3-a456-426614174000
              name: John Doe
              email: john@example.com
```

### 5. Version Your API

```yaml
info:
  version: 1.0.0

servers:
  - url: https://api.example.com/v1
    description: Version 1
  - url: https://api.example.com/v2
    description: Version 2
```

---

## Further Reading

- [OpenAPI 3.0 Specification](https://swagger.io/specification/)
- [OpenAPI Initiative](https://www.openapis.org/)
- [Swagger Editor](https://editor.swagger.io/)
- [OpenAPI Best Practices](https://swagger.io/docs/open-source/community-guides/)
