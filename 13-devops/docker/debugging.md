# Docker Debugging

## docker exec

### Access Running Container
```bash
# Interactive shell
docker exec -it mycontainer bash

# Execute specific command
docker exec mycontainer ls -la /app

# Run as different user
docker exec -u root mycontainer cat /etc/passwd

# Set environment variables
docker exec -e MY_VAR=value mycontainer env
```

### Debug Container Issues
```bash
# Check container processes
docker exec mycontainer ps aux

# Check network
docker exec mycontainer netstat -tuln

# Check disk usage
docker exec mycontainer df -h

# Check environment
docker exec mycontainer env
```

## docker logs

### View Logs
```bash
# All logs
docker logs mycontainer

# Follow logs (stream)
docker logs -f mycontainer

# Show timestamps
docker logs -t mycontainer

# Show last N lines
docker logs --tail 100 mycontainer

# Since specific time
docker logs --since 2024-01-01T00:00:00 mycontainer

# Until specific time
docker logs --until 2024-01-02T00:00:00 mycontainer
```

### Log Drivers
```bash
# Check current log driver
docker inspect mycontainer --format='{{.HostConfig.LogConfig.Type}}'

# View logs with specific driver
docker logs --log-driver json-file mycontainer
```

## docker inspect

### Inspect Container
```bash
# Full inspection
docker inspect mycontainer

# Specific field
docker inspect --format='{{.State.Status}}' mycontainer
docker inspect --format='{{.NetworkSettings.IPAddress}}' mycontainer
docker inspect --format='{{.Mounts}}' mycontainer

# JSON output
docker inspect mycontainer | jq .
```

### Inspect Image
```bash
docker inspect myimage:tag
docker inspect --format='{{.Config.Env}}' myimage:tag
docker inspect --format='{{.Config.ExposedPorts}}' myimage:tag
```

### Inspect Network
```bash
docker network inspect bridge
docker network inspect mynetwork
```

## Network Troubleshooting

### Check Container Network
```bash
# Container IP address
docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mycontainer

# List all containers on network
docker network inspect bridge | jq '.[].Containers'

# Test connectivity
docker exec mycontainer ping other_container
```

### DNS Issues
```bash
# Check DNS configuration
docker exec mycontainer cat /etc/resolv.conf

# Test DNS resolution
docker exec mycontainer nslookup example.com

# Fix DNS issues
docker run --dns=8.8.8.8 myimage
```

### Port Issues
```bash
# Check port mappings
docker port mycontainer

# Check listening ports
docker exec mycontainer netstat -tuln

# Test port connectivity
docker exec mycontainer curl localhost:8000
```

## Storage Troubleshooting

### Check Disk Usage
```bash
# Docker disk usage
docker system df

# Detailed disk usage
docker system df -v

# Container disk usage
docker exec mycontainer df -h
```

### Clean Up Resources
```bash
# Remove stopped containers
docker container prune

# Remove unused images
docker image prune

# Remove unused volumes
docker volume prune

# Remove everything unused
docker system prune -a
```

## Performance Troubleshooting

### Check Resource Usage
```bash
# Real-time stats
docker stats

# Specific container
docker stats mycontainer

# One-time stats
docker stats --no-stream
```

### Check Processes
```bash
# Container processes
docker exec mycontainer ps aux

# Top processes
docker top mycontainer
```

## Image Troubleshooting

### Build Issues
```bash
# Build with verbose output
docker build --progress=plain -t myimage .

# Build with no cache
docker build --no-cache -t myimage .

# Check build history
docker history myimage
```

### Image Issues
```bash
# Check image layers
docker history myimage

# Inspect image
docker inspect myimage

# Run image with debug
docker run -it myimage /bin/bash
```

## Container Issues

### Container Won't Start
```bash
# Check container logs
docker logs mycontainer

# Check container events
docker events --filter container=mycontainer

# Inspect container
docker inspect mycontainer
```

### Container Keeps Crashing
```bash
# Check logs
docker logs mycontainer

# Check exit code
docker inspect --format='{{.State.ExitCode}}' mycontainer

# Run with debug
docker run -it myimage /bin/bash
```

## Docker Daemon Issues

### Daemon Not Starting
```bash
# Check daemon status
sudo systemctl status docker

# View daemon logs
sudo journalctl -u docker.service

# Restart daemon
sudo systemctl restart docker
```

### Permission Issues
```bash
# Check user groups
groups $USER

# Add user to docker group
sudo usermod -aG docker $USER
newgrp docker
```

## Useful Commands

### Debugging Commands
```bash
# Container info
docker inspect mycontainer

# Container logs
docker logs mycontainer

# Container processes
docker exec mycontainer ps aux

# Container network
docker exec mycontainer netstat -tuln

# Container disk
docker exec mycontainer df -h

# Container environment
docker exec mycontainer env
```

### System Commands
```bash
# Docker info
docker info

# Docker version
docker version

# System events
docker events

# Disk usage
docker system df
```

## Best Practices

1. Always check logs first
2. Use docker exec for interactive debugging
3. Inspect container configuration
4. Check network connectivity
5. Monitor resource usage
6. Clean up unused resources
7. Use health checks for monitoring
8. Keep Docker updated
