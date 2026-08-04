# Docker Fundamentals

## Overview

Docker is a platform for developing, shipping, and running applications in containers. Containers are lightweight, portable, and self-sufficient.

## Images and Containers

### Dockerfile Basics
```dockerfile
# Multi-stage build example
FROM node:18-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production

COPY . .
RUN npm run build

# Production stage
FROM node:18-alpine

WORKDIR /app
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/node_modules ./node_modules
COPY package.json .

EXPOSE 3000
USER node
CMD ["node", "dist/index.js"]
```

### Docker Commands
```bash
# Build image
docker build -t my-app:1.0 .

# Run container
docker run -d -p 3000:3000 --name my-app my-app:1.0

# List containers
docker ps -a

# View logs
docker logs -f my-app

# Execute command in container
docker exec -it my-app sh

# Stop and remove
docker stop my-app && docker rm my-app
```

## Dockerfile Instructions

### Common Instructions
```dockerfile
# Base image
FROM node:18-alpine

# Working directory
WORKDIR /app

# Copy files (with .dockerignore)
COPY package*.json ./
COPY src/ ./src/

# Run commands
RUN npm ci --only=production
RUN addgroup -g 1001 -S appgroup
RUN adduser -S appuser -u 1001

# Environment variables
ENV NODE_ENV=production
ENV PORT=3000

# Expose ports
EXPOSE 3000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:3000/health || exit 1

# User
USER appuser

# Command
CMD ["node", "src/index.js"]
```

## .dockerignore

```
node_modules
npm-debug.log
Dockerfile
.dockerignore
.git
.gitignore
.env
.env.*
README.md
coverage/
.nyc_output/
```

## Volume Management

```bash
# Named volumes
docker volume create my-data
docker run -v my-data:/app/data my-app

# Bind mounts
docker run -v $(pwd)/src:/app/src my-app

# Read-only mounts
docker run -v $(pwd)/config:/app/config:ro my-app
```

## Networking

```bash
# Create network
docker network create my-network

# Run with network
docker run --network my-network --name my-app my-app

# Inspect network
docker network inspect my-network
```

## Best Practices

1. **Use multi-stage builds** - Reduce final image size
2. **Minimize layers** - Combine RUN commands
3. **Use .dockerignore** - Exclude unnecessary files
4. **Run as non-root** - Add USER instruction
5. **Use specific base image tags** - Avoid using `latest`
6. **Leverage build cache** - Order instructions properly
7. **Use health checks** - Monitor container health
8. **Scan images** - Check for vulnerabilities
9. **Use labels** - Add metadata to images
10. **Clean up** - Remove unused images and containers
