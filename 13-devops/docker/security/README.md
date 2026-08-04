# Docker Security

## Overview

Docker security involves protecting containers, images, and the Docker daemon from vulnerabilities and attacks.

## Image Security

### Use Trusted Base Images
```dockerfile
# Use official images
FROM node:18-alpine

# Pin specific versions
FROM python:3.11.4-slim

# Use minimal images
FROM scratch
```

### Scan Images
```bash
# Docker Scout
docker scout cves my-image:latest

# Trivy
trivy image my-image:latest

# Snyk
snyk container test my-image:latest
```

## Container Security

### Run as Non-Root
```dockerfile
RUN addgroup -g 1001 -S appgroup
RUN adduser -S appuser -u 1001
USER appuser
```

### Read-Only Filesystem
```bash
docker run --read-only --tmpfs /tmp my-app
```

### Drop Capabilities
```bash
docker run --cap-drop=ALL --cap-add=NET_BIND_SERVICE my-app
```

## Network Security

### Use Private Networks
```yaml
services:
  web:
    networks:
      - backend
  
  db:
    networks:
      - backend

networks:
  backend:
    internal: true
```

### Expose Only Necessary Ports
```yaml
services:
  web:
    ports:
      - "127.0.0.1:3000:3000"
```

## Secrets Management

```yaml
services:
  app:
    secrets:
      - db_password
    environment:
      DB_PASSWORD_FILE: /run/secrets/db_password

secrets:
  db_password:
    file: ./secrets/db_password.txt
```

## Docker Daemon Security

### TLS Configuration
```json
{
  "hosts": ["unix:///var/run/docker.sock", "tcp://0.0.0.0:2376"],
  "tls": true,
  "tlscacert": "/etc/docker/ca.pem",
  "tlscert": "/etc/docker/server-cert.pem",
  "tlskey": "/etc/docker/server-key.pem"
}
```

## Best Practices

1. **Use trusted images** - Pull from official registries
2. **Scan for vulnerabilities** - Regular security scans
3. **Run as non-root** - Don't use root user
4. **Use read-only filesystem** - Prevent writes
5. **Drop unnecessary capabilities** - Least privilege
6. **Use secrets management** - Don't hardcode secrets
7. **Enable Docker Content Trust** - Verify image signatures
8. **Monitor container activity** - Use logging and auditing
9. **Keep Docker updated** - Apply security patches
10. **Use security policies** - Implement OPA/Gatekeeper
