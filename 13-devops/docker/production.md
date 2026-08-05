# Docker Production

## Production Dockerfile

### Optimized Dockerfile
```dockerfile
FROM python:3.11-slim AS builder

WORKDIR /app

# Install build dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

# Install Python dependencies
COPY requirements.txt .
RUN pip install --user --no-cache-dir -r requirements.txt

# Production stage
FROM python:3.11-slim

# Install runtime dependencies
RUN apt-get update && apt-get install -y \
    libpq5 \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy installed packages
COPY --from=builder /root/.local /root/.local

# Create non-root user
RUN useradd --create-home --shell /bin/bash appuser

WORKDIR /app

# Copy application code
COPY --chown=appuser:appuser . .

# Set environment
ENV PATH=/root/.local/bin:$PATH
ENV PYTHONUNBUFFERED=1
ENV PYTHONDONTWRITEBYTECODE=1

# Switch to non-root user
USER appuser

EXPOSE 8000

# Health check
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD curl -f http://localhost:8000/health || exit 1

CMD ["python", "-m", "gunicorn", "-w", "4", "-b", "0.0.0.0:8000", "app:app"]
```

## Health Checks

### HTTP Health Check
```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8000/health || exit 1
```

### TCP Health Check
```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD nc -z localhost 8000 || exit 1
```

### Command Health Check
```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD python -c "import requests; requests.get('http://localhost:8000/health')" || exit 1
```

### Health Check Options
```bash
--interval=30s      # Time between checks
--timeout=10s       # Timeout for each check
--start-period=5s   # Time to wait before starting checks
--retries=3         # Number of retries before unhealthy
```

## Resource Limits

### CPU Limits
```bash
# Limit CPU
docker run --cpus=2 myimage

# CPU shares (relative weight)
docker run --cpu-shares=512 myimage

# CPU set
docker run --cpuset-cpus="0,1" myimage
```

### Memory Limits
```bash
# Memory limit
docker run --memory=512m myimage

# Memory swap
docker run --memory=512m --memory-swap=1g myimage

# Memory reservation
docker run --memory=512m --memory-reservation=256m myimage

# OOM kill disable
docker run --memory=512m --oom-kill-disable myimage
```

### Docker Compose
```yaml
services:
  app:
    image: myimage
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 512M
        reservations:
          cpus: '1'
          memory: 256M
```

## Logging Configuration

### JSON File Logging
```yaml
services:
  app:
    image: myimage
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"
        tag: "{{.Name}}"
```

### Centralized Logging
```yaml
services:
  app:
    image: myimage
    logging:
      driver: syslog
      options:
        syslog-address: "tcp://log-server:514"
        syslog-facility: "local0"
        tag: "myapp"
```

### Log Rotation
```json
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  }
}
```

## Graceful Shutdown

### Handle SIGTERM
```dockerfile
# Use exec form for proper signal handling
CMD ["python", "app.py"]
```

### Application Code
```python
import signal
import sys

def signal_handler(sig, frame):
    print("Shutting down gracefully...")
    cleanup()
    sys.exit(0)

signal.signal(signal.SIGTERM, signal_handler)
signal.signal(signal.SIGINT, signal_handler)
```

### Stop Grace Period
```bash
docker stop --time=30 mycontainer
```

```yaml
services:
  app:
    image: myimage
    stop_grace_period: 30s
```

## Restart Policies

### Policy Options
```bash
# No restart (default)
docker run --restart=no myimage

# Always restart
docker run --restart=always myimage

# Restart unless manually stopped
docker run --restart=unless-stopped myimage

# Restart on failure
docker run --restart=on-failure:5 myimage
```

### Compose
```yaml
services:
  app:
    image: myimage
    restart: unless-stopped
```

## Security Hardening

### Read-Only Filesystem
```bash
docker run --read-only --tmpfs /tmp myimage
```

### No New Privileges
```bash
docker run --security-opt=no-new-privileges myimage
```

### Drop Capabilities
```bash
docker run --cap-drop=ALL --cap-add=NET_BIND_SERVICE myimage
```

## Monitoring

### Container Stats
```bash
docker stats --no-stream
```

### Resource Usage
```bash
docker system df
docker system df -v
```

### Logs
```bash
docker logs --tail=100 --follow mycontainer
```

## Best Practices

1. Use multi-stage builds
2. Run as non-root user
3. Set resource limits
4. Configure health checks
5. Use restart policies
6. Implement graceful shutdown
7. Centralize logging
8. Monitor resource usage
9. Scan images regularly
10. Keep images updated
