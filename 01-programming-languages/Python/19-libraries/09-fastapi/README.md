# FastAPI

## Why FastAPI Exists

Every Python developer building APIs faces a trade-off: Flask is simple but lacks async and automatic validation, while Django REST Framework is powerful but heavy. FastAPI was created to combine the best of both worlds — the simplicity of Flask with automatic request validation, async support, and self-documenting OpenAPI endpoints. It uses Python type hints to generate validation and documentation automatically.

## What You'll Learn

By the end of this section, you'll be able to:

- Build async APIs with automatic request validation using Pydantic models
- Use dependency injection for shared logic like database connections
- Generate OpenAPI documentation automatically from type annotations

## When to Use FastAPI

| Use Case | Why FastAPI | Alternative |
|----------|------------|-------------|
| REST API | Auto-validation, OpenAPI docs | Flask |
| Async endpoints | Native async/await support | Django |
| WebSocket support | Built-in WebSocket handling | Django Channels |
| Microservices | Fast startup, auto docs | Flask |
| Data validation | Pydantic integration | Manual validation |
| ML model serving | Async inference, type safety | Flask |

## How FastAPI Works Internally

FastAPI builds on Starlette (async framework) and Pydantic (data validation). When you define a path operation with type-annotated parameters, FastAPI uses those annotations to generate a Pydantic model for request validation. It also generates an OpenAPI schema from these same annotations, powering the automatic documentation.

Dependency injection in FastAPI works through function parameters. When you declare `def get_db()`, and reference it as `db: Session = Depends(get_db)`, FastAPI calls `get_db()` for each request, injects the result, and manages cleanup. Dependencies can be nested, creating a composable system for cross-cutting concerns.

```python
from fastapi import FastAPI, Depends
from pydantic import BaseModel

app = FastAPI()

class User(BaseModel):
    name: str
    email: str
    age: int

@app.post("/users")
async def create_user(user: User):
    return {"message": f"Created {user.name}"}

@app.get("/users/{user_id}")
async def get_user(user_id: int):
    return {"user_id": user_id}
```

## Production Checklist

### ✅ Before using FastAPI in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: FastAPI is just Flask with async
**Reality:** FastAPI adds automatic request validation, dependency injection, OpenAPI documentation, and WebSocket support. It's a full-featured API framework, not just Flask with async.

### ❌ Myth 2: You need async for all endpoints
**Reality:** Most endpoints are I/O-bound and benefit from async, but CPU-bound tasks should use synchronous endpoints or offload to background workers.

### ❌ Myth 3: FastAPI is only for new projects
**Reality:** FastAPI can be added incrementally to existing applications. It's designed to work alongside other frameworks.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Modern async API framework |
| Complexity | O(1) per request |
| Thread Safe | Yes (async) |
| Best Alternative | Flask for simple APIs |
| When to Use | APIs requiring validation and docs |
| When to Avoid | Full-stack web apps (use Django) |

## Related Topics

- [04-flask](../04-flask/) - Lightweight alternative
- [05-django](../05-django/) - Full-stack alternative
- [15-pydantic](../15-pydantic/) - Data validation foundation

## References
- FastAPI Documentation: https://fastapi.tiangolo.com/
- FastAPI Source: https://github.com/tiangolo/fastapi

## Version Validation
- Verified against: FastAPI 0.104+, Python 3.10+
