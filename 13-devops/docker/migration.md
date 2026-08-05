# Docker Migration

## Docker Version Upgrades

### Upgrade Docker Engine
```bash
# Ubuntu/Debian
sudo apt update
sudo apt upgrade docker-ce docker-ce-cli containerd.io

# CentOS/RHEL
sudo yum update docker-ce docker-ce-cli containerd.io

# Verify
docker --version
```

### Upgrade Docker Desktop
```bash
# macOS
brew upgrade --cask docker

# Windows
# Download new installer from docker.com
```

## Docker Compose V1 to V2

### Changes
```bash
# V1 (standalone)
docker-compose up

# V2 (plugin)
docker compose up
```

### Migration Steps
```bash
# Install compose plugin
sudo apt install docker-compose-plugin

# Update scripts
# Replace docker-compose with docker compose

# Verify
docker compose version
```

### Compose File Updates
```yaml
# V1
version: '3'

# V2 (optional, can omit)
version: '3.8'
```

## Dockerfile Migration

### Multi-Stage Builds
```dockerfile
# Before
FROM python:3.11
RUN apt-get update && apt-get install -y gcc
COPY . .
RUN pip install -r requirements.txt

# After
FROM python:3.11 AS builder
WORKDIR /app
COPY requirements.txt .
RUN pip install --user -r requirements.txt

FROM python:3.11-slim
COPY --from=builder /root/.local /root/.local
COPY . .
```

### BuildKit Migration
```bash
# Enable BuildKit
DOCKER_BUILDKIT=1 docker build .

# Or in daemon.json
{
  "features": {
    "buildkit": true
  }
}
```

## Base Image Migration

### Ubuntu to Alpine
```dockerfile
# Before
FROM ubuntu:22.04
RUN apt-get update && apt-get install -y python3

# After
FROM python:3.11-alpine
```

### Python Image Variants
```dockerfile
# Full image
FROM python:3.11

# Slim image (recommended)
FROM python:3.11-slim

# Alpine image
FROM python:3.11-alpine
```

## Compose File Migration

### Version 2 to 3
```yaml
# Version 2
version: '2'
services:
  web:
    image: nginx

# Version 3
version: '3.8'
services:
  web:
    image: nginx
```

### Add Health Checks
```yaml
services:
  app:
    image: myapp
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8000/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

### Add Resource Limits
```yaml
services:
  app:
    image: myapp
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 512M
```

## Network Migration

### Bridge to Overlay
```yaml
# Before
networks:
  default:
    driver: bridge

# After (Swarm)
networks:
  default:
    driver: overlay
```

### Custom Networks
```yaml
services:
  web:
    networks:
      - frontend
  db:
    networks:
      - backend

networks:
  frontend:
  backend:
```

## Volume Migration

### Bind Mount to Named Volume
```yaml
# Before
services:
  db:
    volumes:
      - /var/lib/postgresql/data:/var/lib/postgresql/data

# After
services:
  db:
    volumes:
      - db-data:/var/lib/postgresql/data

volumes:
  db-data:
```

## Security Migration

### Add Non-Root User
```dockerfile
# Before
FROM python:3.11
COPY . /app
CMD ["python", "app.py"]

# After
FROM python:3.11
RUN useradd --create-home appuser
COPY --chown=appuser:appuser . /app
USER appuser
CMD ["python", "app.py"]
```

### Add Health Checks
```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8000/health || exit 1
```

## Configuration Migration

### Environment Variables
```yaml
# Before
services:
  app:
    environment:
      - DATABASE_URL=postgres://localhost/mydb

# After
services:
  app:
    env_file:
      - .env
```

### Secrets
```yaml
# Before
services:
  app:
    environment:
      - DB_PASSWORD=secret

# After
services:
  app:
    secrets:
      - db_password

secrets:
  db_password:
    file: ./secrets/db_password.txt
```

## Monitoring Migration

### Add Logging
```yaml
services:
  app:
    image: myapp
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"
```

### Add Prometheus
```yaml
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
```

## Best Practices

1. Test changes in development first
2. Update Dockerfiles incrementally
3. Use multi-stage builds
4. Add health checks
5. Set resource limits
6. Use named volumes
7. Implement logging
8. Document changes
