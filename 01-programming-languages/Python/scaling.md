# Python Scaling

## Celery

Distributed task queue.

```bash
pip install celery redis
```

### Configuration
```python
# celery_config.py
from celery import Celery

app = Celery('tasks', broker='redis://localhost:6379/0')

app.conf.update(
    task_serializer='json',
    accept_content=['json'],
    result_serializer='json',
    timezone='UTC',
    enable_utc=True,
    task_routes={
        'tasks.heavy': {'queue': 'heavy'},
    },
)
```

### Define Tasks
```python
@app.task
def add(x, y):
    return x + y

@app.task(bind=True)
def process_data(self, data):
    try:
        # Process data
        return result
    except Exception as exc:
        self.retry(exc=exc, countdown=60)
```

### Execute Tasks
```python
# Async
result = add.delay(4, 4)

# With options
result = add.apply_async(args=[4, 4], countdown=10)

# Get result
print(result.get(timeout=10))
```

## Multiprocessing

```python
from multiprocessing import Pool, Process
import os

def worker(x):
    return x * x

# Pool
with Pool(4) as p:
    results = p.map(worker, range(10))

# Process
def background_task():
    print(f"Worker PID: {os.getpid()}")

process = Process(target=background_task)
process.start()
process.join()
```

### Manager for Shared State
```python
from multiprocessing import Manager

def worker(shared_dict, key, value):
    shared_dict[key] = value

with Manager() as manager:
    shared_dict = manager.dict()
    processes = []
    for i in range(5):
        p = Process(target=worker, args=(shared_dict, f"key{i}", i))
        processes.append(p)
        p.start()
    for p in processes:
        p.join()
    print(dict(shared_dict))
```

## asyncio

```python
import asyncio

async def fetch_data(url):
    await asyncio.sleep(1)
    return f"Data from {url}"

async def main():
    # Run concurrently
    tasks = [fetch_data(url) for url in urls]
    results = await asyncio.gather(*tasks)
    return results

# Run
asyncio.run(main())
```

### asyncio with aiohttp
```python
import aiohttp
import asyncio

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.text()

async def main():
    urls = ["http://example.com/1", "http://example.com/2"]
    async with aiohttp.ClientSession() as session:
        tasks = [fetch(session, url) for url in urls]
        results = await asyncio.gather(*tasks)
        return results
```

## Load Balancing

### Nginx Configuration
```nginx
upstream python_app {
    server 127.0.0.1:8001;
    server 127.0.0.1:8002;
    server 127.0.0.1:8003;
}

server {
    listen 80;
    
    location / {
        proxy_pass http://python_app;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## Horizontal Scaling

### Docker Compose Scale
```bash
# Scale web service to 3 instances
docker-compose up -d --scale web=3
```

### Kubernetes
```yaml
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
      - name: app
        image: myapp:latest
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

## Caching

### Redis Cache
```python
import redis
import json

r = redis.Redis(host='localhost', port=6379, db=0)

def get_data(key):
    cached = r.get(key)
    if cached:
        return json.loads(cached)
    
    data = expensive_query()
    r.setex(key, 300, json.dumps(data))  # Cache for 5 minutes
    return data
```

### functools.lru_cache
```python
from functools import lru_cache

@lru_cache(maxsize=128)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)
```

## Database Connection Pooling

### SQLAlchemy
```python
from sqlalchemy import create_engine

engine = create_engine(
    "postgresql://user:pass@localhost/db",
    pool_size=20,
    max_overflow=0,
    pool_pre_ping=True
)
```

## Best Practices

1. Use Celery for background tasks
2. Implement connection pooling
3. Cache frequently accessed data
4. Use async for I/O-bound operations
5. Monitor queue lengths and task durations
6. Implement circuit breakers for external services
7. Use read replicas for database scaling
8. Consider message queues for decoupling
