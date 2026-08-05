# Docker Performance

## Layer Caching

### How Caching Works
Docker caches each layer. If nothing changes in a layer or its parents, Docker uses the cached version.

### Cache-Friendly Dockerfile
```dockerfile
# BAD - cache invalidated frequently
FROM python:3.11-slim
WORKDIR /app
COPY . .
RUN pip install -r requirements.txt

# GOOD - dependencies cached separately
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
```

### Order Matters
```dockerfile
# Install system deps (rarely changes)
RUN apt-get update && apt-get install -y \
    gcc \
    libpq-dev

# Install Python deps (changes occasionally)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy application code (changes frequently)
COPY . .
```

## Multi-Stage Builds

### Smaller Images
```dockerfile
# Build stage
FROM python:3.11 AS builder
WORKDIR /app
COPY requirements.txt .
RUN pip install --user --no-cache-dir -r requirements.txt

# Production stage
FROM python:3.11-slim
WORKDIR /app
COPY --from=builder /root/.local /root/.local
COPY . .
ENV PATH=/root/.local/bin:$PATH
CMD ["python", "app.py"]
```

### Benefits
- Smaller final image
- Build tools not in production
- Better security (fewer tools)
- Faster pulls and pushes

## Image Size Optimization

### Base Image Selection
```dockerfile
# Full image (1GB+)
FROM ubuntu:22.04

# Slim image (100-200MB)
FROM python:3.11-slim

# Alpine image (5-50MB)
FROM python:3.11-alpine
```

### Alpine Considerations
```dockerfile
FROM python:3.11-alpine

# Install build dependencies
RUN apk add --no-cache gcc musl-dev libffi-dev

# Install Python packages
RUN pip install --no-cache-dir -r requirements.txt
```

### Reduce Layers
```dockerfile
# BAD - multiple layers
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y wget
RUN rm -rf /var/lib/apt/lists/*

# GOOD - single layer
RUN apt-get update && \
    apt-get install -y curl wget && \
    rm -rf /var/lib/apt/lists/*
```

### Clean Up
```dockerfile
# Remove cache and temp files
RUN pip install --no-cache-dir -r requirements.txt

# Remove build dependencies
RUN apt-get purge -y gcc && \
    apt-get autoremove -y

# Remove pip cache
RUN rm -rf /root/.cache
```

## BuildKit Features

### Enable BuildKit
```bash
DOCKER_BUILDKIT=1 docker build .
```

### Mount Cache
```dockerfile
# syntax=docker/dockerfile:1
FROM python:3.11-slim

RUN --mount=type=cache,target=/root/.cache/pip \
    pip install -r requirements.txt
```

### Mount Secret
```dockerfile
# syntax=docker/dockerfile:1
RUN --mount=type=secret,id=pipconf \
    pip install -r requirements.txt --config-settings=pip.conf=/run/secrets/pipconf
```

### SSH Forwarding
```dockerfile
# syntax=docker/dockerfile:1
RUN --mount=type=ssh git clone git@github.com:user/repo.git
```

## Resource Limits

### CPU
```bash
# Limit CPU
docker run --cpus=2 myimage

# CPU shares (relative weight)
docker run --cpu-shares=512 myimage
```

### Memory
```bash
# Limit memory
docker run --memory=512m myimage

# Memory swap
docker run --memory=512m --memory-swap=1g myimage

# Memory reservation
docker run --memory=512m --memory-reservation=256m myimage
```

### I/O
```bash
# Block I/O
docker run --device-read-bps /dev/sda:1mb myimage
docker run --device-write-bps /dev/sda:1mb myimage
```

## Network Performance

### Host Networking
```bash
# Remove network overhead
docker run --network host myimage
```

### DNS Configuration
```json
{
  "dns": ["8.8.8.8", "8.8.4.4"],
  "dns-opts": ["ndots:0"]
}
```

## Storage Performance

### Storage Drivers
```bash
# Check current driver
docker info | grep "Storage Driver"

# Use overlay2 (recommended)
{
  "storage-driver": "overlay2"
}
```

### Volume Performance
```bash
# Named volumes (better performance)
docker run -v myvolume:/data myimage

# Bind mounts (good for development)
docker run -v /host/path:/container/path myimage
```

## Monitoring

### Build Metrics
```bash
# Build with timing
docker build --progress=plain .

# BuildKit statistics
docker build --progress=rawjson .
```

### Runtime Metrics
```bash
# Container stats
docker stats

# Specific container
docker stats mycontainer
```

## Best Practices

1. Order Dockerfile instructions from least to most changing
2. Use multi-stage builds for smaller images
3. Use .dockerignore to exclude unnecessary files
4. Clean up caches and temp files
5. Use specific base image tags
6. Leverage BuildKit caching
7. Set resource limits in production
8. Monitor image size and build times
