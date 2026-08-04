# API Gateway Pattern

## Overview

Single entry point for all client requests, handling cross-cutting concerns.

## Responsibilities

- Request routing
- Authentication/Authorization
- Rate limiting
- Request/Response transformation
- Load balancing

## Implementation

```python
class APIGateway:
    def __init__(self):
        self.routes = {}
        self.middleware = []
    
    def route(self, path, service):
        self.routes[path] = service
    
    def handle(self, request):
        # Apply middleware
        for mw in self.middleware:
            request = mw.process(request)
        
        # Route to service
        service = self.routes.get(request.path)
        if service:
            return service.handle(request)
        return Response(status=404)
```

## Tools

- Kong
- AWS API Gateway
- Zuul
- Envoy
