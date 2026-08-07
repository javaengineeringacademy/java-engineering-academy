from pydantic import BaseModel, Field, validator
from typing import Optional
from datetime import datetime

class User(BaseModel):
    id: int
    name: str = Field(..., min_length=1, max_length=100)
    email: str
    age: Optional[int] = Field(None, ge=0, le=150)
    created_at: datetime = Field(default_factory=datetime.now)

    @validator('email')
    def validate_email(cls, v):
        if '@' not in v:
            raise ValueError('Invalid email address')
        return v

    @validator('name')
    def name_must_be_alphanumeric(cls, v):
        if not v.replace(' ', '').isalnum():
            raise ValueError('Name must be alphanumeric')
        return v

# Valid data
user = User(id=1, name="Alice Smith", email="alice@example.com", age=30)
print(user.dict())

# Invalid data - will raise ValidationError
try:
    user = User(id=1, name="", email="invalid-email", age=-5)
except ValueError as e:
    print(f"Validation error: {e}")

# JSON serialization
print(user.json())
