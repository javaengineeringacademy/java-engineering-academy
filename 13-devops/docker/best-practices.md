# Docker Best Practices

## Dockerfile Best Practices

### 1. Use Official Base Images
```dockerfile
FROM python:3.11-slim
FROM node:18-alpine
FROM nginx:1.21
```

### 2. Minimize Layers
```dockerfile
# BAD
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y wget

# GOOD
RUN apt-get update && \
    apt-get install -y curl wget && \
    rm -rf /var/lib/apt/lists/*
```

### 3. Order Instructions Correctly
```dockerfile
# System deps (rarely changes)
RUN apt-get update && apt-get install -y gcc

# App deps (changes occasionally)
COPY requirements.txt .
RUN pip install -r requirements.txt

# App code (changes frequently)
COPY . .
```

### 4. Use Multi-Stage Builds
```dockerfile
FROM python:3.11 AS builder
WORKDIR /app
COPY requirements.txt .
RUN pip install --user -r requirements.txt

FROM python:3.11-slim
COPY --from=builder /root/.local /root/.local
COPY . .
```

### 5. Use .dockerignore
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

## Image Best Practices

### 6. Use Specific Tags
```dockerfile
# BAD
FROM python
FROM python:latest

# GOOD
FROM python:3.11-slim
FROM python:3.11.4-slim
```

### 7. Use Smaller Base Images
```dockerfile
# Full image (1GB+)
FROM ubuntu:22.04

# Slim image (100-200MB)
FROM python:3.11-slim

# Alpine image (5-50MB)
FROM python:3.11-alpine
```

### 8. Clean Up After Installation
```dockerfile
RUN apt-get update && \
    apt-get install -y --no-install-recommends gcc && \
    pip install --no-cache-dir -r requirements.txt && \
    apt-get purge -y gcc && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/*
```

## Security Best Practices

### 9. Run as Non-Root User
```dockerfile
RUN useradd --create-home appuser
USER appuser
```

### 10. Use Read-Only Filesystem
```bash
docker run --read-only --tmpfs /tmp myimage
```

### 11. Scan Images for Vulnerabilities
```bash
docker scout cves myimage:tag
trivy image myimage:tag
```

### 12. Use Docker Secrets
```yaml
services:
  app:
    secrets:
      - db_password
secrets:
  db_password:
    file: ./secrets/db_password.txt
```

## Compose Best Practices

### 13. Use Override Files
```bash
docker-compose.yml           # Base
docker-compose.override.yml  # Development
docker-compose.prod.yml      # Production
```

### 14. Define Health Checks
```yaml
services:
  app:
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8000/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

### 15. Use Named Volumes
```yaml
services:
  db:
    volumes:
      - db-data:/var/lib/postgresql/data

volumes:
  db-data:
```

### 16. Set Resource Limits
```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 512M
```

## Performance Best Practices

### 17. Leverage Build Cache
```dockerfile
# Install dependencies first
COPY requirements.txt .
RUN pip install -r requirements.txt

# Copy code last
COPY . .
```

### 18. Use BuildKit
```bash
DOCKER_BUILDKIT=1 docker build .
```

### 19. Use .dockerignore
```
# Exclude unnecessary files
.git
*.md
docs/
tests/
```

## Operational Best Practices

### 20. Use Labels
```dockerfile
LABEL maintainer="team@example.com"
LABEL version="1.0"
LABEL description="My application"
```

### 21. Implement Graceful Shutdown
```bash
# Use exec form for signal handling
CMD ["python", "app.py"]
```

### 22. Use Restart Policies
```yaml
services:
  app:
    restart: unless-stopped
```

### 23. Centralize Logging
```yaml
services:
  app:
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"
```

## Quick Checklist

- [ ] Use official base images
- [ ] Pin image versions
- [ ] Use multi-stage builds
- [ ] Minimize layers
- [ ] Order instructions correctly
- [ ] Use .dockerignore
- [ ] Run as non-root user
- [ ] Set health checks
- [ ] Set resource limits
- [ ] Use named volumes
- [ ] Scan for vulnerabilities
- [ ] Use Docker secrets
- [ ] Centralize logging
- [ ] Implement graceful shutdown
- [ ] Use restart policies
