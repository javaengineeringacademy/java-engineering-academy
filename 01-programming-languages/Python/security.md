# Python Security

## Dependency Scanning

### Safety
```bash
pip install safety

# Check installed packages
safety check

# Check requirements file
safety check -r requirements.txt
```

### pip-audit
```bash
pip install pip-audit

# Audit installed packages
pip-audit

# Audit requirements
pip-audit -r requirements.txt
```

### Dependabot
```yaml
# .github/dependabot.yml
version: 2
updates:
  - package-ecosystem: "pip"
    directory: "/"
    schedule:
      interval: "weekly"
```

## Static Analysis

### Bandit
```bash
pip install bandit

# Scan project
bandit -r src/

# Generate JSON report
bandit -r src/ -f json -o report.json
```

### Semgrep
```bash
pip install semgrep

# Run rules
semgrep --config=auto src/
```

## Secrets Management

### Environment Variables
```python
import os

# Never hardcode secrets
api_key = os.environ.get("API_KEY")
if not api_key:
    raise ValueError("API_KEY not set")
```

### python-dotenv
```python
from dotenv import load_dotenv
import os

load_dotenv()
secret = os.getenv("SECRET_KEY")
```

### AWS Secrets Manager
```python
import boto3
import json

def get_secret(secret_name):
    client = boto3.client('secretsmanager')
    response = client.get_secret_value(SecretId=secret_name)
    return json.loads(response['SecretString'])
```

## Input Validation

### Pydantic
```python
from pydantic import BaseModel, validator

class UserInput(BaseModel):
    username: str
    age: int

    @validator('username')
    def username_alphanumeric(cls, v):
        if not v.isalnum():
            raise ValueError('Username must be alphanumeric')
        return v

    @validator('age')
    def age_positive(cls, v):
        if v < 0 or v > 150:
            raise ValueError('Invalid age')
        return v
```

### Marshmallow
```python
from marshmallow import Schema, fields, validate

class UserSchema(Schema):
    username = fields.Str(required=True, validate=validate.Length(min=3))
    email = fields.Email(required=True)
```

## SQL Injection Prevention

### Parameterized Queries
```python
import sqlite3

# Unsafe
cursor.execute(f"SELECT * FROM users WHERE id = {user_id}")

# Safe
cursor.execute("SELECT * FROM users WHERE id = ?", (user_id,))
```

### SQLAlchemy ORM
```python
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import Session

# ORM protects against injection
user = session.query(User).filter(User.id == user_id).first()
```

## XSS Prevention

```python
from markupsafe import escape

# Escape user input
safe_output = escape(user_input)

# Use templates with auto-escaping
# Jinja2 auto-escapes by default
```

## CSRF Protection

```python
from flask_wtf.csrf import CSRFProtect

csrf = CSRFProtect(app)

# In templates
# <form method="post">
#     {{ csrf_token() }}
# </form>
```

## Password Hashing

### bcrypt
```python
import bcrypt

# Hash password
password = b"secret"
hashed = bcrypt.hashpw(password, bcrypt.gensalt())

# Verify password
if bcrypt.checkpw(password, hashed):
    print("Password matches")
```

### Argon2
```python
from argon2 import PasswordHasher

ph = PasswordHasher()
hash = ph.hash("password")

try:
    ph.verify(hash, "password")
except:
    print("Invalid password")
```

## CORS Configuration

```python
from flask import Flask
from flask_cors import CORS

app = Flask(__name__)
CORS(app, resources={r"/api/*": {"origins": "https://example.com"}})
```

## Security Headers

```python
from flask_talisman import Talisman

Talisman(app, 
    force_https=True,
    strict_transport_security=True,
    content_security_policy={
        'default-src': "'self'"
    }
)
```

## Logging Sensitive Data

```python
import logging

logger = logging.getLogger(__name__)

# Never log secrets
logger.info("Processing request")  # Safe
logger.debug(f"Token: {token}")  # Unsafe - don't do this

# Use structlog with redaction
import structlog
log = structlog.get_logger()
log.info("request_processed", user_id=user.id)  # Safe
```

## Best Practices

1. Never commit secrets to version control
2. Use environment variables for configuration
3. Validate all user input
4. Use parameterized queries
5. Hash passwords with bcrypt or argon2
6. Enable HTTPS in production
7. Scan dependencies regularly
8. Use security-focused linters (bandit)
