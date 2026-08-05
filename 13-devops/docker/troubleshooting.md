# Docker Troubleshooting

## Common Issues

### Container Won't Start
```bash
# Check logs
docker logs mycontainer

# Check container status
docker inspect --format='{{.State.Status}}' mycontainer

# Check exit code
docker inspect --format='{{.State.ExitCode}}' mycontainer

# Run with debug
docker run -it myimage /bin/bash
```

### Permission Denied
```bash
# Add user to docker group
sudo usermod -aG docker $USER
newgrp docker

# Or use sudo
sudo docker run hello-world

# Check file permissions
ls -la /var/run/docker.sock
```

### Port Already in Use
```bash
# Find process using port
lsof -i :80

# Use different port
docker run -p 8080:80 nginx

# Kill process
kill -9 <PID>
```

### No Space Left on Device
```bash
# Check disk usage
docker system df

# Clean up resources
docker system prune -a

# Remove unused images
docker image prune -a

# Remove unused volumes
docker volume prune
```

## Network Issues

### No Network Connectivity
```bash
# Check container network
docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mycontainer

# Check DNS
docker exec mycontainer cat /etc/resolv.conf

# Test connectivity
docker exec mycontainer ping google.com

# Restart network
docker network prune
```

### DNS Resolution Issues
```bash
# Check DNS settings
docker exec mycontainer cat /etc/resolv.conf

# Use custom DNS
docker run --dns=8.8.8.8 myimage

# Check DNS resolution
docker exec mycontainer nslookup example.com
```

### Container Cannot Reach Other Container
```bash
# Check network
docker network ls
docker network inspect bridge

# Use container name
docker exec mycontainer ping other_container

# Create custom network
docker network create mynetwork
docker run --network mynetwork --name web nginx
docker run --network mynetwork --name app myapp
```

## Build Issues

### Build Fails
```bash
# Build with verbose output
docker build --progress=plain -t myimage .

# Build without cache
docker build --no-cache -t myimage .

# Check Dockerfile syntax
hadolint Dockerfile
```

### Slow Build
```bash
# Check build cache
docker history myimage

# Optimize Dockerfile order
# 1. System deps
# 2. App deps
# 3. App code

# Use BuildKit
DOCKER_BUILDKIT=1 docker build .
```

### Image Too Large
```bash
# Check image size
docker images myimage

# Check layers
docker history myimage

# Use multi-stage builds
# Use smaller base images
# Clean up after installation
```

## Storage Issues

### Volume Not Working
```bash
# Check volumes
docker volume ls
docker volume inspect myvolume

# Check mount point
docker inspect --format='{{json .Mounts}}' mycontainer

# Recreate volume
docker volume rm myvolume
docker volume create myvolume
```

### Data Lost After Container Restart
```bash
# Use named volumes
docker run -v myvolume:/data myimage

# Check volume mounts
docker inspect --format='{{json .Mounts}}' mycontainer

# Backup volume
docker run --rm -v myvolume:/data -v $(pwd):/backup alpine tar czf /backup/backup.tar.gz -C /data .
```

### Container Cannot Write
```bash
# Check permissions
docker exec mycontainer ls -la /app

# Run as root
docker run -u root myimage

# Fix permissions
docker run -v $(pwd)/data:/app/data myimage chown -R appuser:appuser /app/data
```

## Resource Issues

### Container Using Too Much Memory
```bash
# Check memory usage
docker stats mycontainer

# Set memory limit
docker run --memory=512m myimage

# Check OOM kills
docker inspect --format='{{.State.OOMKilled}}' mycontainer
```

### Container Using Too Much CPU
```bash
# Check CPU usage
docker stats mycontainer

# Set CPU limit
docker run --cpus=2 myimage

# Check CPU shares
docker run --cpu-shares=512 myimage
```

## Docker Daemon Issues

### Docker Not Starting
```bash
# Check status
sudo systemctl status docker

# View logs
sudo journalctl -u docker.service

# Restart
sudo systemctl restart docker

# Check daemon.json
cat /etc/docker/daemon.json
```

### Cannot Connect to Docker Daemon
```bash
# Check Docker socket
ls -la /var/run/docker.sock

# Check Docker process
ps aux | grep docker

# Restart Docker
sudo systemctl restart docker
```

## Image Issues

### Image Not Found
```bash
# Check image name
docker images

# Pull image
docker pull nginx:latest

# Check registry
docker search nginx
```

### Tag Issues
```bash
# List tags
docker images nginx

# Pull specific tag
docker pull nginx:1.21

# Use full tag
docker run nginx:1.21-alpine
```

## Quick Fixes

### Restart Everything
```bash
# Stop all containers
docker stop $(docker ps -aq)

# Remove all containers
docker rm $(docker ps -aq)

# Remove all images
docker rmi $(docker images -q)

# Clean up
docker system prune -a
```

### Reset Docker
```bash
# Stop Docker
sudo systemctl stop docker

# Remove Docker data
sudo rm -rf /var/lib/docker

# Start Docker
sudo systemctl start docker
```

## Best Practices

1. Always check logs first
2. Use docker exec for debugging
3. Check resource usage
4. Clean up unused resources
5. Use health checks
6. Monitor disk space
7. Keep Docker updated
8. Use proper logging
