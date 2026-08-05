# Docker Core Concepts

## Images

### What is a Docker Image?
- Read-only template for creating containers
- Built from Dockerfile instructions
- Composed of multiple layers
- Stored in registries

### Image Layers
```dockerfile
FROM python:3.11-slim        # Layer 1: Base image
RUN pip install flask        # Layer 2: Dependencies
COPY . /app                  # Layer 3: Application code
CMD ["python", "app.py"]     # Layer 4: Default command
```

### Image Tags
```bash
# Tag formats
nginx              # latest tag (default)
nginx:1.21         # Specific version
myrepo/nginx:v1    # Repository with version
registry.io/nginx  # Full registry path
```

## Containers

### What is a Container?
- Running instance of an image
- Isolated process with its own filesystem
- Ephemeral by default
- Created from images

### Container Lifecycle
```bash
docker create    # Create container (not started)
docker start     # Start container
docker run       # Create and start
docker stop      # Graceful stop
docker kill      # Force stop
docker rm        # Remove container
docker pause     # Pause container
docker unpause   # Unpause container
```

### Container Operations
```bash
# Run container
docker run -d --name myapp nginx

# Execute command in running container
docker exec -it myapp bash

# View logs
docker logs myapp

# Inspect container
docker inspect myapp

# Copy files
docker cp myapp:/app/file.txt .
```

## Dockerfile

### Basic Instructions
```dockerfile
# Base image
FROM python:3.11-slim

# Working directory
WORKDIR /app

# Copy files
COPY requirements.txt .
COPY . .

# Run commands
RUN pip install -r requirements.txt

# Expose port
EXPOSE 8000

# Environment variables
ENV APP_ENV=production

# User
RUN useradd -m appuser
USER appuser

# Default command
CMD ["python", "app.py"]
```

### Build Arguments
```dockerfile
# Build-time variables
ARG PYTHON_VERSION=3.11
FROM python:${PYTHON_VERSION}-slim

# Usage
docker build --build-arg PYTHON_VERSION=3.10 .
```

## Volumes

### Types of Volumes
```bash
# Named volumes (managed by Docker)
docker volume create myvolume
docker run -v myvolume:/app/data nginx

# Bind mounts (host directory)
docker run -v /host/path:/container/path nginx

# tmpfs (in-memory)
docker run --tmpfs /app/temp nginx
```

### Volume Commands
```bash
docker volume create     # Create volume
docker volume ls         # List volumes
docker volume inspect    # Inspect volume
docker volume rm         # Remove volume
docker volume prune      # Remove unused volumes
```

### Best Practices
- Use named volumes for persistent data
- Use bind mounts for development
- Avoid storing data in writable layer
- Use read-only mounts when possible

## Networks

### Network Types
```bash
# Bridge (default)
docker network create mybridge
docker run --network mybridge nginx

# Host
docker run --network host nginx

# Overlay
docker network create --driver overlay myoverlay

# None
docker run --network none nginx
```

### Network Commands
```bash
docker network create      # Create network
docker network ls          # List networks
docker network inspect     # Inspect network
docker network connect     # Connect container
docker network disconnect  # Disconnect container
docker network rm          # Remove network
```

### Container Communication
```bash
# Containers on same network can communicate
docker network create mynet
docker run --network mynet --name web nginx
docker run --network mynet --name app myapp

# app can reach web at hostname "web"
```

## Docker Compose

### Compose File Structure
```yaml
version: '3.8'

services:
  web:
    image: nginx
    ports:
      - "80:80"
    networks:
      - frontend

  app:
    build: .
    environment:
      - DATABASE_URL=postgres://db:5432/mydb
    depends_on:
      - db
    networks:
      - frontend
      - backend

  db:
    image: postgres:15
    volumes:
      - db-data:/var/lib/postgresql/data
    networks:
      - backend

networks:
  frontend:
  backend:

volumes:
  db-data:
```

### Compose Commands
```bash
docker-compose up           # Start services
docker-compose down         # Stop services
docker-compose ps           # List services
docker-compose logs         # View logs
docker-compose build        # Build images
docker-compose pull         # Pull images
docker-compose exec         # Execute command
docker-compose run          # Run one-off command
```

## Tags and Registries

### Image Tags
```bash
# Semantic versioning
nginx:1.21.0
nginx:1.21
nginx:1

# Tags for different purposes
nginx:alpine      # Alpine Linux variant
nginx:slim        # Slim variant
nginx:latest      # Latest version
```

### Registry Commands
```bash
docker login           # Login to registry
docker logout          # Logout
docker push            # Push image
docker pull            # Pull image
docker search          # Search registry
```

## Best Practices

1. Use official base images
2. Minimize image layers
3. Use .dockerignore
4. Don't run as root
5. Use multi-stage builds
6. Scan images for vulnerabilities
7. Use specific tags (not :latest)
8. Clean up unused resources
