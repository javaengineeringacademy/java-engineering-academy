# Docker Cheat Sheet

## Image Commands

```bash
# Build image
docker build -t myimage:tag .

# Build with no cache
docker build --no-cache -t myimage:tag .

# List images
docker images

# Pull image
docker pull nginx:latest

# Push image
docker push myimage:tag

# Remove image
docker rmi myimage:tag

# Remove unused images
docker image prune -a
```

## Container Commands

```bash
# Run container
docker run -d --name mycontainer nginx

# Run interactively
docker run -it ubuntu bash

# List running containers
docker ps

# List all containers
docker ps -a

# Stop container
docker stop mycontainer

# Start container
docker start mycontainer

# Remove container
docker rm mycontainer

# Remove running container
docker rm -f mycontainer

# Execute command
docker exec -it mycontainer bash
```

## Dockerfile Instructions

```dockerfile
FROM nginx:latest
WORKDIR /app
COPY . .
ADD archive.tar.gz /app/
RUN apt-get update && apt-get install -y curl
EXPOSE 80
ENV APP_ENV=production
ARG VERSION=1.0
USER nginx
HEALTHCHECK --interval=30s CMD curl -f http://localhost/ || exit 1
CMD ["nginx", "-g", "daemon off;"]
ENTRYPOINT ["nginx"]
VOLUME /data
LABEL version="1.0"
```

## Volume Commands

```bash
# Create volume
docker volume create myvolume

# List volumes
docker volume ls

# Inspect volume
docker volume inspect myvolume

# Remove volume
docker volume rm myvolume

# Remove unused volumes
docker volume prune

# Run with volume
docker run -v myvolume:/data nginx

# Run with bind mount
docker run -v /host/path:/container/path nginx
```

## Network Commands

```bash
# Create network
docker network create mynetwork

# List networks
docker network ls

# Inspect network
docker network inspect mynetwork

# Connect container
docker network connect mynetwork mycontainer

# Disconnect container
docker network disconnect mynetwork mycontainer

# Remove network
docker network rm mynetwork
```

## Docker Compose Commands

```bash
# Start services
docker compose up -d

# Stop services
docker compose down

# List services
docker compose ps

# View logs
docker compose logs

# Follow logs
docker compose logs -f

# Build services
docker compose build

# Restart services
docker compose restart

# Execute command
docker compose exec myservice bash
```

## System Commands

```bash
# Docker info
docker info

# Docker version
docker version

# System df
docker system df

# System prune
docker system prune -a

# Disk usage
docker system df -v
```

## Useful Flags

```bash
# Detached mode
docker run -d

# Interactive
docker run -it

# Remove container after exit
docker run --rm

# Port mapping
docker run -p 80:80

# Volume mount
docker run -v /host:/container

# Environment variable
docker run -e MY_VAR=value

# Network
docker run --network mynetwork

# Resource limits
docker run --cpus=2 --memory=512m

# Restart policy
docker run --restart=unless-stopped
```

## Quick Reference

### Port Mapping
```bash
-p 80:80           # host:container
-p 8080-8090:80    # range
-p 127.0.0.1:80:80 # specific interface
```

### Volume Mounts
```bash
-v myvolume:/data           # Named volume
-v /host:/container         # Bind mount
-v /container               # Anonymous volume
-v ./config:/app/config:ro  # Read-only
```

### Environment Variables
```bash
-e KEY=value                # Direct
-e KEY=$HOST_KEY            # From host
--env-file .env             # From file
```

### Resource Limits
```bash
--cpus=2                    # CPU limit
--memory=512m               # Memory limit
--pids-limit=100            # PID limit
```

### Restart Policies
```bash
--restart=no                # No restart
--restart=always            # Always restart
--restart=unless-stopped    # Unless stopped
--restart=on-failure:5      # On failure (max 5)
```

### Security Options
```bash
--read-only                 # Read-only filesystem
--security-opt=no-new-privileges  # No privilege escalation
--cap-drop=ALL              # Drop all capabilities
```
