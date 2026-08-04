# Docker Optimization

## Overview

Docker optimization focuses on reducing image size, improving build performance, and enhancing container runtime efficiency.

## Image Size Optimization

### Layer Caching
```dockerfile
# Bad - copies everything first
COPY . .
RUN npm install

# Good - copies package files first
COPY package*.json ./
RUN npm ci --only=production
COPY . .
```

### Minimize Layers
```dockerfile
# Bad - multiple layers
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y wget
RUN apt-get clean

# Good - single layer
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
```

### Use Alpine Images
```dockerfile
# Large image
FROM node:18

# Small image
FROM node:18-alpine
```

## Build Performance

### BuildKit
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

### Build Arguments
```dockerfile
ARG NODE_VERSION=18
FROM node:${NODE_VERSION}-alpine
```

### Cache Mounts
```dockerfile
RUN --mount=type=cache,target=/root/.npm npm ci
```

## Runtime Optimization

### Resource Limits
```bash
docker run -d \
  --memory=512m \
  --cpus=1.5 \
  --pids-limit=100 \
  my-app
```

### Health Checks
```dockerfile
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:3000/health || exit 1
```

## Best Practices

1. **Use multi-stage builds** - Separate build and runtime
2. **Order instructions properly** - Maximize cache hits
3. **Use .dockerignore** - Exclude unnecessary files
4. **Minimize layers** - Combine RUN commands
5. **Use specific tags** - Avoid using `latest`
6. **Clean up caches** - Remove build artifacts
7. **Use BuildKit** - Faster builds with cache mounts
8. **Scan images** - Check for vulnerabilities
9. **Use distroless images** - Minimal attack surface
10. **Monitor image size** - Track and optimize regularly
