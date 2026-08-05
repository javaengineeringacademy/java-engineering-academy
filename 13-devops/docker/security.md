# Docker Security

## Non-Root User

### Create Non-Root User
```dockerfile
FROM python:3.11-slim

# Create user
RUN useradd --create-home --shell /bin/bash appuser

# Set ownership
COPY --chown=appuser:appuser . /app

WORKDIR /app

# Switch to non-root user
USER appuser

CMD ["python", "app.py"]
```

### Why Non-Root?
- Reduces attack surface
- Prevents privilege escalation
- Follows principle of least privilege
- Required for many security standards

## Read-Only Filesystem

### Make Filesystem Read-Only
```bash
docker run --read-only \
  --tmpfs /tmp \
  --tmpfs /var/run \
  myimage
```

### Dockerfile
```dockerfile
FROM python:3.11-slim
RUN useradd --create-home appuser
USER appuser
# Container runs with read-only filesystem
```

### Writable Directories
```bash
# Use tmpfs for temporary files
docker run --read-only --tmpfs /tmp myimage

# Use volumes for persistent data
docker run --read-only -v data:/app/data myimage
```

## Secrets Management

### Docker Secrets (Swarm)
```yaml
version: '3.8'

services:
  app:
    image: myapp
    secrets:
      - db_password

secrets:
  db_password:
    file: ./secrets/db_password.txt
```

### Environment Variables (Not Recommended)
```bash
# Avoid this
docker run -e PASSWORD=secret myimage

# Use secrets instead
docker run --secret db_password myimage
```

### Build Secrets
```dockerfile
# syntax=docker/dockerfile:1
RUN --mount=type=secret,id=pipconf \
    pip install -r requirements.txt --config-settings=pip.conf=/run/secrets/pipconf
```

## Image Scanning

### Docker Scout
```bash
# Scan image
docker scout cves myimage:tag

# Quick scan
docker scout quickview myimage:tag
```

### Trivy
```bash
# Install
brew install trivy

# Scan
trivy image myimage:tag

# Scan with severity filter
trivy image --severity HIGH,CRITICAL myimage:tag
```

### Snyk
```bash
# Install
npm install -g snyk

# Scan
snyk container test myimage:tag
```

## Image Signing

### Content Trust
```bash
# Enable content trust
export DOCKER_CONTENT_TRUST=1

# Push signed image
docker push myimage:tag
```

### Cosign
```bash
# Install
brew install sigstore/cosign/cosign

# Sign image
cosign sign myregistry/myimage:tag

# Verify
cosign verify myregistry/myimage:tag
```

## Network Security

### Restrict Network Access
```bash
# Use specific network
docker run --network mynetwork myimage

# Disable networking
docker run --network none myimage
```

### Firewall Rules
```bash
# Restrict container communication
docker network create \
  --driver bridge \
  --internal \
  mynetwork
```

## Resource Limits

### Prevent DoS
```bash
# CPU limit
docker run --cpus=2 myimage

# Memory limit
docker run --memory=512m myimage

# PID limit
docker run --pids-limit=100 myimage
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
    pids_limit: 100
```

## Security Scanning

### CIS Benchmark
```bash
# Run CIS benchmark
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
  docker/docker-bench-security
```

### Hadolint
```bash
# Install
brew install hadolint

# Lint Dockerfile
hadolint Dockerfile
```

## Best Practices

1. Always use non-root user
2. Use read-only filesystem when possible
3. Scan images for vulnerabilities
4. Sign images for integrity
5. Use secrets management
6. Set resource limits
7. Use minimal base images
8. Keep images updated
9. Use Docker Content Trust
10. Run security benchmarks
