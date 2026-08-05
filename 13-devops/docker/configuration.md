# Docker Configuration

## daemon.json

Docker daemon configuration file.

### Location
- Linux: `/etc/docker/daemon.json`
- macOS: `~/.docker/daemon.json`
- Windows: `C:\ProgramData\docker\config\daemon.json`

### Common Settings
```json
{
  "data-root": "/var/lib/docker",
  "storage-driver": "overlay2",
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  },
  "registry-mirrors": ["https://mirror.example.com"],
  "insecure-registries": ["myregistry.local:5000"],
  "dns": ["8.8.8.8", "8.8.4.4"],
  "default-address-pools": [
    {"base": "172.17.0.0/12", "size": 24}
  ]
}
```

### Reload Daemon
```bash
sudo systemctl reload docker
sudo kill -HUP $(pidof dockerd)
```

## Dockerfile Instructions

### FROM
```dockerfile
FROM ubuntu:22.04
FROM python:3.11-slim
FROM scratch
```

### RUN
```dockerfile
RUN apt-get update && apt-get install -y curl
RUN ["apt-get", "update"]
```

### COPY vs ADD
```dockerfile
COPY requirements.txt .
COPY . /app
ADD archive.tar.gz /app/
```

### ENTRYPOINT vs CMD
```dockerfile
ENTRYPOINT ["python", "app.py"]
CMD ["python", "app.py"]
ENTRYPOINT ["python"]
CMD ["app.py"]
```

### ENV and ARG
```dockerfile
ENV APP_ENV=production
ENV PATH="/app/bin:${PATH}"
ARG VERSION=1.0
```

### Other Instructions
```dockerfile
WORKDIR /app
RUN useradd -m -u 1000 appuser
USER appuser
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8000/health || exit 1
EXPOSE 8000
VOLUME /data
LABEL version="1.0"
```

## .dockerignore

```
.git
.gitignore
.env
.env.*
__pycache__
*.pyc
venv/
.venv/
node_modules/
build/
dist/
*.log
.DS_Store
docker-compose*.yml
Dockerfile*
README.md
.vscode/
.idea/
```

## Compose Configuration

### Services
```yaml
version: '3.8'

services:
  web:
    build:
      context: .
      dockerfile: Dockerfile
      args:
        BUILD_VERSION: 1.0
    image: myapp:latest
    container_name: web
    ports:
      - "8000:8000"
    volumes:
      - ./data:/app/data
      - logs:/app/logs
    environment:
      - NODE_ENV=production
    env_file:
      - .env
    networks:
      - frontend
    depends_on:
      - db
    restart: unless-stopped
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 512M
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8000/health"]
      interval: 30s
      timeout: 10s
      retries: 3
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"

networks:
  frontend:
    driver: bridge

volumes:
  logs:
  db-data:
```

## Port Mapping
```yaml
ports:
  - "80:80"
  - "8000-8100:8000"
  - "127.0.0.1:80:80"
```

## Volume Mounts
```yaml
volumes:
  - /host/path:/container/path
  - named_volume:/container/path
  - ./config:/app/config:ro
```

## Resource Limits
```yaml
deploy:
  resources:
    limits:
      cpus: '2'
      memory: 512M
    reservations:
      cpus: '0.5'
      memory: 256M
```

## Restart Policies
```yaml
restart: "no"
restart: always
restart: unless-stopped
restart: on-failure
```

## Logging Configuration
```yaml
logging:
  driver: json-file
  options:
    max-size: "10m"
    max-file: "3"
```

## Best Practices

1. Use multi-line RUN to reduce layers
2. Combine related commands in single RUN
3. Order instructions from least to most frequently changing
4. Use COPY instead of ADD unless extracting archives
5. Use .dockerignore to exclude unnecessary files
6. Set HEALTHCHECK for container monitoring
7. Use non-root USER for security
8. Pin base image versions for reproducibility
