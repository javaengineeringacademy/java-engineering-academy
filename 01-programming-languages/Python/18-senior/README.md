# Python Senior - Production Mastery

## Why Production Mastery Matters

Every senior Python engineer needs to bridge the gap between writing code that works and writing code that scales, maintains itself, and survives production incidents. Production mastery encompasses architecture patterns, deployment strategies, monitoring, and the hard-won knowledge that separates junior from senior engineers. Without this knowledge, you'd repeat mistakes that experienced engineers have already solved.

Without production mastery, you'd build applications that work in development but fail under real-world load, make architectural decisions that create technical debt, and struggle to debug issues in complex distributed systems. That's why production mastery exists — it provides the patterns, practices, and mindset for building Python applications that are reliable, maintainable, and performant at scale.

## What You'll Learn

By the end of this module, you'll be able to:

- Design and implement production-grade architecture patterns
- Deploy, monitor, and maintain Python applications in production
- Handle debugging, profiling, and optimization at scale
- Apply security best practices and handle common vulnerabilities
- Lead technical decisions and mentor other engineers

## Architecture Patterns

### MVC (Model-View-Controller)
```python
# Model - Data layer
class User:
    def __init__(self, id, name, email):
        self.id = id
        self.name = name
        self.email = email

# Controller - Business logic
class UserController:
    def __init__(self, user_repository):
        self.repo = user_repository

    def get_user(self, user_id):
        return self.repo.find_by_id(user_id)

    def create_user(self, data):
        user = User(**data)
        return self.repo.save(user)

# View - Presentation layer
def user_template(user):
    return f"<div><h1>{user.name}</h1><p>{user.email}</p></div>"
```

### MVVM (Model-View-ViewModel)
```python
from dataclasses import dataclass, field
from typing import List

@dataclass
class TodoViewModel:
    items: List[str] = field(default_factory=list)
    _observers: List[callable] = field(default_factory=list)

    def add_item(self, item: str):
        self.items.append(item)
        self._notify()

    def _notify(self):
        for observer in self._observers:
            observer(self.items)

    def register_observer(self, callback):
        self._observers.append(callback)
```

### Microservices Pattern
```python
from fastapi import FastAPI, HTTPException
import httpx

app = FastAPI()

class OrderService:
    def __init__(self):
        self.user_service = "http://user-service:8001"
        self.inventory_service = "http://inventory-service:8002"

    async def create_order(self, order_data):
        async with httpx.AsyncClient() as client:
            user = await client.get(f"{self.user_service}/users/{order_data.user_id}")
            inventory = await client.get(
                f"{self.inventory_service}/items/{order_data.item_id}"
            )

            if user.status_code != 200:
                raise HTTPException(404, "User not found")
            if inventory.json()["quantity"] < order_data.quantity:
                raise HTTPException(400, "Insufficient stock")

            return {"status": "created", "order_id": "12345"}

# Each microservice runs independently
# Communicate via HTTP/gRPC/message queues
```

### Event-Driven Architecture
```python
from dataclasses import dataclass
from typing import Callable, Dict, List
from datetime import datetime
import uuid

@dataclass
class Event:
    id: str
    type: str
    data: dict
    timestamp: datetime

class EventBus:
    def __init__(self):
        self._handlers: Dict[str, List[Callable]] = {}

    def subscribe(self, event_type: str, handler: Callable):
        self._handlers.setdefault(event_type, []).append(handler)

    def publish(self, event: Event):
        for handler in self._handlers.get(event.type, []):
            handler(event)

# Usage
bus = EventBus()

def order_created_handler(event: Event):
    print(f"Send confirmation email for order {event.data['order_id']}")

def inventory_handler(event: Event):
    print(f"Reserve stock for {event.data['item_id']}")

bus.subscribe("order.created", order_created_handler)
bus.subscribe("order.created", inventory_handler)

event = Event(
    id=str(uuid.uuid4()),
    type="order.created",
    data={"order_id": "12345", "item_id": "ITEM001"},
    timestamp=datetime.now()
)
bus.publish(event)
```

## Production Deployment

### Docker Production Setup
```dockerfile
# Dockerfile
FROM python:3.12-slim AS builder

WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir --user -r requirements.txt

FROM python:3.12-slim

RUN useradd --create-home appuser
WORKDIR /home/appuser/app

COPY --from=builder /root/.local /home/appuser/.local
COPY . .

ENV PATH=/home/appuser/.local/bin:$PATH
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

USER appuser

EXPOSE 8000

HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8000/health || exit 1

CMD ["gunicorn", "app:app", "--workers", "4", "--bind", "0.0.0.0:8000"]
```

### Docker Compose for Production
```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8000:8000"
    environment:
      - DATABASE_URL=postgresql://user:pass@db:5432/mydb
      - REDIS_URL=redis://redis:6379
      - SECRET_KEY=${SECRET_KEY}
    depends_on:
      - db
      - redis
    deploy:
      replicas: 3
      restart_policy:
        condition: on-failure

  db:
    image: postgres:15-alpine
    volumes:
      - postgres_data:/var/lib/postgresql/data
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=${DB_PASSWORD}

  redis:
    image: redis:7-alpine
    volumes:
      - redis_data:/data

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
      - ./certs:/etc/nginx/certs

volumes:
  postgres_data:
  redis_data:
```

### Kubernetes Deployment
```yaml
# k8s-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: python-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: python-app
  template:
    metadata:
      labels:
        app: python-app
    spec:
      containers:
      - name: python-app
        image: myapp:latest
        ports:
        - containerPort: 8000
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /health
            port: 8000
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8000
          initialDelaySeconds: 5
          periodSeconds: 5
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: app-secrets
              key: database-url

---
apiVersion: v1
kind: Service
metadata:
  name: python-app-service
spec:
  selector:
    app: python-app
  ports:
  - port: 80
    targetPort: 8000
  type: LoadBalancer
```

### CI/CD Pipeline
```yaml
# .github/workflows/deploy.yml
name: Deploy

on:
  push:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up Python
        uses: actions/setup-python@v5
        with:
          python-version: '3.12'
      - name: Install dependencies
        run: pip install -r requirements.txt -r requirements-dev.txt
      - name: Run tests
        run: pytest --cov=app --cov-report=xml
      - name: Run linting
        run: ruff check . && mypy .

  deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build Docker image
        run: docker build -t myapp:${{ github.sha }} .
      - name: Push to registry
        run: |
          echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
          docker tag myapp:${{ github.sha }} myrepo/myapp:latest
          docker push myrepo/myapp:latest
      - name: Deploy to Kubernetes
        run: |
          kubectl set image deployment/python-app python-app=myrepo/myapp:${{ github.sha }}
          kubectl rollout status deployment/python-app
```

### Environment Variables & Configuration
```python
from pydantic_settings import BaseSettings
from functools import lru_cache

class Settings(BaseSettings):
    app_name: str = "MyApp"
    debug: bool = False
    database_url: str
    redis_url: str = "redis://localhost:6379"
    secret_key: str
    api_key: str = ""
    allowed_origins: list[str] = ["http://localhost:3000"]
    log_level: str = "INFO"

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"

@lru_cache()
def get_settings():
    return Settings()

# .env file (NEVER commit this)
# DATABASE_URL=postgresql://user:pass@localhost:5432/mydb
# SECRET_KEY=your-secret-key-here
# DEBUG=false
```

### Health Checks
```python
from fastapi import FastAPI
from fastapi.responses import JSONResponse
import asyncio
import redis.asyncio as redis

app = FastAPI()

@app.get("/health")
async def health_check():
    return {"status": "healthy"}

@app.get("/health/detailed")
async def detailed_health():
    checks = {
        "database": await check_database(),
        "redis": await check_redis(),
        "disk_space": await check_disk_space(),
    }

    status = "healthy" if all(checks.values()) else "degraded"
    return JSONResponse(
        status_code=200 if status == "healthy" else 503,
        content={"status": status, "checks": checks}
    )

async def check_database():
    try:
        # Run simple query
        return True
    except Exception:
        return False

async def check_redis():
    try:
        r = redis.from_url("redis://localhost:6379")
        await r.ping()
        return True
    except Exception:
        return False
```

### Graceful Shutdown
```python
import signal
import asyncio
from contextlib import asynccontextmanager
from fastapi import FastAPI

class GracefulShutdown:
    def __init__(self):
        self.shutdown_event = asyncio.Event()
        self.cleanup_tasks = []

    def register_cleanup(self, func):
        self.cleanup_tasks.append(func)

    async def shutdown(self):
        print("Initiating graceful shutdown...")
        self.shutdown_event.set()

        for task in self.cleanup_tasks:
            try:
                if asyncio.iscoroutinefunction(task):
                    await task()
                else:
                    task()
            except Exception as e:
                print(f"Error in cleanup: {e}")

        print("Shutdown complete")

@asynccontextmanager
async def lifespan(app: FastAPI):
    shutdown_handler = GracefulShutdown()

    async def cleanup():
        await asyncio.gather(
            close_database_connections(),
            flush_pending_logs(),
            release_resources(),
        )

    shutdown_handler.register_cleanup(cleanup)

    loop = asyncio.get_event_loop()
    for sig in (signal.SIGTERM, signal.SIGINT):
        loop.add_signal_handler(sig, lambda: asyncio.create_task(shutdown_handler.shutdown()))

    yield

    await shutdown_handler.shutdown()

app = FastAPI(lifespan=lifespan)
```

## Performance Optimization

### Profiling
```python
import cProfile
import pstats
from io import StringIO

def profile_function(func):
    def wrapper(*args, **kwargs):
        pr = cProfile.Profile()
        pr.enable()
        result = func(*args, **kwargs)
        pr.disable()

        s = StringIO()
        ps = pstats.Stats(pr, stream=s).sort_stats("cumulative")
        ps.print_stats(20)
        print(s.getvalue())

        return result
    return wrapper

@profile_function
def slow_function():
    return sum(range(1000000))
```

### Memory Optimization
```python
import sys
from typing import Generator

# Use slots for memory efficiency
class Point:
    __slots__ = ['x', 'y']

    def __init__(self, x, y):
        self.x = x
        self.y = y

# Regular class: ~152 bytes per instance
# Slotted class: ~56 bytes per instance

# Use generators instead of lists
def get_large_dataset() -> Generator:
    for i in range(10_000_000):
        yield {"id": i, "value": f"item_{i}"}

# Bad: loads everything into memory
# data = list(get_large_dataset())

# Good: lazy evaluation
for item in get_large_dataset():
    process(item)

# Use memory-efficient data structures
from array import array
import numpy as np

# List of integers
list_ints = [i for i in range(1000000)]  # ~8MB

# Array of integers
array_ints = array('i', range(1000000))  # ~4MB

# NumPy array (most efficient for numeric data)
numpy_ints = np.arange(1000000, dtype=np.int32)  # ~4MB
```

## Security Best Practices

### OWASP Top 10 Prevention
```python
from fastapi import FastAPI, HTTPException, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, EmailStr, validator
import re

app = FastAPI()

# 1. CORS Configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["https://trusted-domain.com"],
    allow_credentials=True,
    allow_methods=["GET", "POST"],
    allow_headers=["*"],
)

# 2. Input Validation
class CreateUserRequest(BaseModel):
    username: str
    email: EmailStr
    age: int

    @validator("username")
    def username_alphanumeric(cls, v):
        if not re.match(r"^[a-zA-Z0-9_]{3,20}$", v):
            raise ValueError("Username must be 3-20 alphanumeric characters")
        return v

    @validator("age")
    def age_valid(cls, v):
        if v < 0 or v > 150:
            raise ValueError("Invalid age")
        return v

# 3. SQL Injection Prevention
import asyncpg

async def get_user_safe(user_id: int):
    # NEVER do this: f"SELECT * FROM users WHERE id = {user_id}"

    # DO this:
    query = "SELECT * FROM users WHERE id = $1"
    return await connection.fetchrow(query, user_id)

# 4. Rate Limiting
from slowapi import Limiter
from slowapi.util import get_remote_address

limiter = Limiter(key_func=get_remote_address)
app.state.limiter = limiter

@app.post("/api/login")
@limiter.limit("5/minute")
async def login(request: Request, credentials: LoginRequest):
    # Rate limited to 5 attempts per minute
    pass
```

## Scaling Python

### Horizontal Scaling with Load Balancer
```nginx
# nginx.conf
upstream python_apps {
    least_conn;
    server app1:8000 weight=3;
    server app2:8000 weight=2;
    server app3:8000 weight=1;

    keepalive 32;
}

server {
    listen 80;
    server_name example.com;

    location / {
        proxy_pass http://python_apps;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        proxy_connect_timeout 5s;
        proxy_read_timeout 60s;
    }
}
```

### Database Connection Pooling
```python
import asyncpg
from contextlib import asynccontextmanager

class DatabasePool:
    def __init__(self, dsn: str, min_size: int = 10, max_size: int = 20):
        self.dsn = dsn
        self.min_size = min_size
        self.max_size = max_size
        self.pool = None

    async def initialize(self):
        self.pool = await asyncpg.create_pool(
            self.dsn,
            min_size=self.min_size,
            max_size=self.max_size,
            command_timeout=60,
        )

    @asynccontextmanager
    async def acquire(self):
        async with self.pool.acquire() as connection:
            yield connection

    async def close(self):
        await self.pool.close()
```

## Cost Analysis

| Aspect | Python | Java | Go | C++ |
|--------|--------|------|-----|-----|
| Development Speed | Fast | Medium | Medium | Slow |
| Runtime Speed | Slow | Fast | Fast | Very Fast |
| Memory Usage | High | Medium | Low | Low |
| Hosting Cost | $$ | $$$ | $$ | $$ |
| Developer Cost | $ | $$ | $$$ | $$$$ |
| Best For | AI/ML, Web | Enterprise | Systems | Performance |

## Production Checklist

```markdown
### Code Quality
- [ ] Type hints on all functions
- [ ] Docstrings on public APIs
- [ ] Unit test coverage > 80%
- [ ] Integration tests
- [ ] Linting (ruff/flake8)
- [ ] Type checking (mypy)

### Security
- [ ] Input validation
- [ ] SQL parameterized queries
- [ ] CORS configured
- [ ] Rate limiting
- [ ] Authentication/Authorization
- [ ] Secrets not in code
- [ ] HTTPS enabled

### Performance
- [ ] Database connection pooling
- [ ] Caching implemented
- [ ] Async I/O where needed
- [ ] Memory profiling done
- [ ] Load testing completed

### Deployment
- [ ] Docker containerized
- [ ] Health checks implemented
- [ ] Graceful shutdown
- [ ] Logging structured
- [ ] Monitoring alerts
- [ ] CI/CD pipeline
- [ ] Rollback strategy

### Operations
- [ ] Environment variables
- [ ] Backup strategy
- [ ] Disaster recovery plan
- [ ] Runbook documented
- [ ] On-call rotation
```

## Maturity Levels

| Level | Description | Focus |
|-------|-------------|-------|
| L1 - Beginner | Writing scripts | Syntax, basic concepts |
| L2 - Intermediate | Building applications | OOP, libraries, testing |
| L3 - Advanced | System design | Architecture, patterns |
| L4 - Senior | Production systems | DevOps, security, scaling |
| L5 - Staff | Technical leadership | Strategy, mentoring, standards |

## Common Myths

**Myth: Python is too slow for production**
Reality: With proper architecture (async, caching, C extensions), Python handles millions of requests.

**Myth: Python doesn't scale**
Reality: Instagram, YouTube, and Spotify run on Python at massive scale.

**Myth: You need microservices for everything**
Reality: A well-structured monolith is often better than poorly designed microservices.

**Myth: More tests = better code**
Reality: Right tests matter more than more tests. Focus on critical paths.

## One-Minute Revision

```
Architecture: MVC/MVVM for UI, Microservices for scale, Events for decoupling
Deployment: Docker + K8s + CI/CD pipeline
Performance: Profile first, cache second, async for I/O
Security: Validate input, parameterize queries, rate limit
Scaling: Horizontal (more machines) > Vertical (bigger machine)
Cost: Python wins on development speed, loses on raw performance
Checklist: Code quality, Security, Performance, Deployment, Operations
```

## Interview Questions

### Q1: What is the difference between monolith and microservices?
**Answer:** Monolith: single deployable unit. Microservices: distributed services. Monolith simpler to start, microservices scale better. Choose based on team size and complexity.

### Q2: What is the 12-factor app methodology?
**Answer:** 12 principles for cloud-native apps: codebase, dependencies, config, backing services, build/release/run, processes, port binding, concurrency, disposability, dev/prod parity, logs, admin processes.

### Q3: What is the difference between horizontal and vertical scaling?
**Answer:** Vertical: add more power to existing machine. Horizontal: add more machines. Horizontal is more fault-tolerant, vertical is simpler.

### Q4: What is the CAP theorem?
**Answer:** Distributed systems can guarantee only 2 of 3: Consistency, Availability, Partition tolerance. Choose based on your requirements.

### Q5: What is the difference between SRE and DevOps?
**Answer:** DevOps: culture and practices for collaboration. SRE: specific implementation of DevOps with error budgets, SLIs, SLOs.
