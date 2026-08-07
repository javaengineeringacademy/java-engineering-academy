# Pydantic

## Why Pydantic Exists

Every Python developer building APIs or processing external data faces the same problem: input data is messy, untyped, and unreliable. Manually validating every field with if-statements is tedious and error-prone. Pydantic was created to solve this by using Python type hints to define data schemas that automatically validate, serialize, and deserialize data. It's the standard for data validation in modern Python.

## What You'll Learn

By the end of this section, you'll be able to:

- Define data models with field types and validators
- Serialize and deserialize data between Python objects, JSON, and dictionaries
- Use Pydantic for settings management with environment variable validation

## When to Use Pydantic

| Use Case | Why Pydantic | Alternative |
|----------|------------|-------------|
| API request validation | Auto-validation from type hints | Manual checks |
| Settings management | Env vars with type coercion | os.environ |
| JSON serialization | Auto-convert models to JSON | json.dumps |
| Database models | Schema validation before persistence | Manual validation |
| CLI argument parsing | Type-safe argument handling | argparse |
| Config files | YAML/JSON config validation | Manual parsing |

## How Pydantic Works Internally

Pydantic uses metaclasses to inspect type annotations at class creation time. When you define `name: str`, Pydantic creates a validator that checks incoming data is a string (or can be coerced to one). At runtime, validation happens during model instantiation — `User(name="Alice")` runs all validators and raises `ValidationError` if any fail.

Serialization works in reverse. `model.model_dump()` converts the model to a dictionary, and `model.model_json()` converts to JSON string. Pydantic handles complex types (datetime, UUID, Decimal) by converting them to JSON-compatible formats automatically.

```python
from pydantic import BaseModel, EmailStr, validator

class User(BaseModel):
    name: str
    email: EmailStr
    age: int

    @validator('age')
    def age_must_be_positive(cls, v):
        if v < 0:
            raise ValueError('Age must be positive')
        return v

# Auto-validation
user = User(name='Alice', email='alice@example.com', age=30)

# Serialization
print(user.model_dump())  # {'name': 'Alice', 'email': 'alice@example.com', 'age': 30}
print(user.model_json())  # JSON string
```

## Production Checklist

### ✅ Before using Pydantic in production:

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

### ❌ Myth 1: Pydantic is only for FastAPI
**Reality:** Pydantic is a standalone library that works with any Python project. Use it for CLI tools, settings management, or any data validation need.

### ❌ Myth 2: Type hints are enough for validation
**Reality:** Type hints are for static analysis. Pydantic validates at runtime, checking actual values against types and custom rules.

### ❌ Myth 3: Pydantic v1 and v2 are the same
**Reality:** Pydantic v2 is a complete rewrite with 5-50x performance improvements. Always use v2 for new projects.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Data validation with type hints |
| Complexity | O(n) for model validation |
| Thread Safe | Yes |
| Best Alternative | dataclasses for simple models |
| When to Use | API validation, settings, serialization |
| When to Avoid | Simple data containers (use dataclasses) |

## Related Topics

- [09-fastapi](../09-fastapi/) - FastAPI integration
- [04-flask](../04-flask/) - Flask request validation
- [16-typer](../16-typer/) - CLI argument validation
