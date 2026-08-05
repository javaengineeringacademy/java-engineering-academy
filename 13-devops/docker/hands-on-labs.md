# Docker Hands-On Labs

## Lab 1: Basic Container Operations

### Exercise
```bash
# Pull and run nginx
docker pull nginx:latest
docker run -d --name web -p 80:80 nginx

# Verify
curl http://localhost

# Stop and remove
docker stop web
docker rm web
```

## Lab 2: Create Custom Image

### Exercise
```dockerfile
# Create Dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
EXPOSE 5000
CMD ["python", "app.py"]
```

```bash
# Build and run
docker build -t myapp .
docker run -d -p 5000:5000 myapp
```

## Lab 3: Docker Volumes

### Exercise
```bash
# Create named volume
docker volume create mydata

# Run container with volume
docker run -d --name db -v mydata:/var/lib/postgresql/data postgres:15

# Verify data persists
docker exec db psql -U postgres -c "CREATE DATABASE test;"
docker stop db
docker rm db

# Recreate container
docker run -d --name db2 -v mydata:/var/lib/postgresql/data postgres:15
docker exec db2 psql -U postgres -l
```

## Lab 4: Docker Networks

### Exercise
```bash
# Create custom network
docker network create mynet

# Run containers on network
docker run -d --name web --network mynet nginx
docker run -d --name app --network mynet python:3.11

# Test connectivity
docker exec app ping web
```

## Lab 5: Docker Compose

### Exercise
```yaml
# Create docker-compose.yml
version: '3.8'
services:
  web:
    image: nginx
    ports:
      - "80:80"
  app:
    build: .
    ports:
      - "5000:5000"
    depends_on:
      - web
```

```bash
# Start services
docker compose up -d

# View logs
docker compose logs

# Stop services
docker compose down
```

## Lab 6: Multi-Stage Build

### Exercise
```dockerfile
# Create multi-stage Dockerfile
FROM python:3.11 AS builder
WORKDIR /app
COPY requirements.txt .
RUN pip install --user -r requirements.txt

FROM python:3.11-slim
COPY --from=builder /root/.local /root/.local
COPY . .
EXPOSE 5000
CMD ["python", "app.py"]
```

```bash
# Build and compare sizes
docker build -t myapp:multi .
docker images myapp
```

## Lab 7: Health Checks

### Exercise
```dockerfile
# Add health check to Dockerfile
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:5000/health || exit 1
```

```bash
# Run and check health
docker run -d --name app myapp
docker inspect --format='{{.State.Health.Status}}' app
```

## Lab 8: Resource Limits

### Exercise
```bash
# Run with resource limits
docker run -d --name app \
  --cpus=2 \
  --memory=512m \
  myapp

# Monitor resources
docker stats app
```

## Lab 9: Docker Security

### Exercise
```dockerfile
# Create secure Dockerfile
FROM python:3.11-slim
RUN useradd --create-home appuser
WORKDIR /app
COPY --chown=appuser:appuser . .
USER appuser
EXPOSE 5000
CMD ["python", "app.py"]
```

```bash
# Run and verify non-root
docker run -d myapp
docker exec myapp whoami
```

## Lab 10: Cleanup

### Exercise
```bash
# Stop all containers
docker stop $(docker ps -aq)

# Remove all containers
docker rm $(docker ps -aq)

# Remove all images
docker rmi $(docker images -q)

# Clean up unused resources
docker system prune -a
```

## Solutions

Each lab includes:
1. Problem description
2. Step-by-step instructions
3. Expected output
4. Verification commands

### Running Labs
```bash
# Create working directory
mkdir docker-labs && cd docker-labs

# Follow lab instructions
# Verify results
docker ps
docker images
docker volume ls
docker network ls
```
