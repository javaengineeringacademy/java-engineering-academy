# Docker Pitfalls

## Large Images

### Problem
Images too large due to:
- Unnecessary files in build context
- Multiple RUN layers
- Build tools in production
- Not using .dockerignore

### Solution
```dockerfile
# Use multi-stage builds
FROM python:3.11 AS builder
WORKDIR /app
COPY requirements.txt .
RUN pip install --user -r requirements.txt

FROM python:3.11-slim
COPY --from=builder /root/.local /root/.local
COPY . .
```

### Use .dockerignore
```
.git
.gitignore
.env
__pycache__
*.pyc
venv/
node_modules/
build/
dist/
*.log
.DS_Store
```

## Running as Root

### Problem
Container runs as root by default, increasing attack surface.

### Solution
```dockerfile
# Create non-root user
RUN useradd --create-home --shell /bin/bash appuser

# Set ownership
COPY --chown=appuser:appuser . /app

# Switch to non-root user
USER appuser
```

## No Health Checks

### Problem
Docker cannot determine if container is healthy.

### Solution
```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8000/health || exit 1
```

## Not Using Multi-Stage Builds

### Problem
Build tools and dependencies in production image.

### Solution
```dockerfile
# Build stage
FROM python:3.11 AS builder
WORKDIR /app
COPY requirements.txt .
RUN pip install --user -r requirements.txt

# Production stage
FROM python:3.11-slim
COPY --from=builder /root/.local /root/.local
COPY . .
```

## Caching Issues

### Problem
Docker cache invalidated frequently.

### Solution
```dockerfile
# Install dependencies first (rarely changes)
COPY requirements.txt .
RUN pip install -r requirements.txt

# Copy code last (changes frequently)
COPY . .
```

## Not Cleaning Up

### Problem
Build artifacts and caches left in image.

### Solution
```dockerfile
RUN apt-get update && \
    apt-get install -y gcc && \
    pip install --no-cache-dir -r requirements.txt && \
    apt-get purge -y gcc && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/*
```

## Wrong CMD Format

### Problem
Using shell form instead of exec form.

### Solution
```dockerfile
# BAD (shell form)
CMD python app.py

# GOOD (exec form)
CMD ["python", "app.py"]
```

## Not Using .env Files

### Problem
Hardcoded environment variables.

### Solution
```yaml
# docker-compose.yml
services:
  app:
    env_file:
      - .env

# .env
DATABASE_URL=postgres://localhost/mydb
SECRET_KEY=my-secret
```

## Ignoring .dockerignore

### Problem
Build context includes unnecessary files.

### Solution
```
# .dockerignore
.git
.gitignore
.env
.env.*
__pycache__
*.pyc
venv/
.venv/
node_modules/
build/
dist/
*.log
.DS_Store
docker-compose*.yml
Dockerfile*
README.md
```

## Not Setting Resource Limits

### Problem
Containers consume unlimited resources.

### Solution
```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 512M
        reservations:
          cpus: '1'
          memory: 256M
```

## Using :latest Tag

### Problem
Unpredictable behavior with latest tag.

### Solution
```dockerfile
# BAD
FROM python:latest
FROM python

# GOOD
FROM python:3.11-slim
FROM python:3.11.4-slim
```

## Not Handling Signals

### Problem
Container doesn't shut down gracefully.

### Solution
```dockerfile
# Use exec form
CMD ["python", "app.py"]
```

```python
import signal
import sys

def handler(sig, frame):
    cleanup()
    sys.exit(0)

signal.signal(signal.SIGTERM, handler)
```

## Exposing Unnecessary Ports

### Problem
Documenting ports that shouldn't be exposed.

### Solution
```dockerfile
# Only expose necessary ports
EXPOSE 8000

# Don't expose development ports
# EXPOSE 5000  # Flask dev server
# EXPOSE 8080  # Debug port
```

## Not Using Health Checks

### Problem
Docker cannot determine container health.

### Solution
```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8000/health || exit 1
```

## Ignoring Security

### Problem
Running containers with default settings.

### Solution
```bash
# Run as non-root
docker run --user 1000:1000 myimage

# Read-only filesystem
docker run --read-only --tmpfs /tmp myimage

# No new privileges
docker run --security-opt=no-new-privileges myimage

# Drop capabilities
docker run --cap-drop=ALL --cap-add=NET_BIND_SERVICE myimage
```

## Best Practices

1. Use .dockerignore
2. Run as non-root user
3. Use multi-stage builds
4. Set health checks
5. Set resource limits
6. Use specific image tags
7. Clean up after installations
8. Handle signals properly
9. Scan images for vulnerabilities
10. Keep images updated
