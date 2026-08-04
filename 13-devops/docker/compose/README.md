# Docker Compose

## Overview

Docker Compose is a tool for defining and running multi-container Docker applications using YAML files.

## Basic Configuration

```yaml
# docker-compose.yml
version: '3.8'

services:
  web:
    build: .
    ports:
      - "3000:3000"
    environment:
      - NODE_ENV=production
      - DB_HOST=postgres
    depends_on:
      - postgres
      - redis
    networks:
      - frontend
      - backend

  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: myapp
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - backend

  redis:
    image: redis:7-alpine
    networks:
      - backend

volumes:
  postgres_data:

networks:
  frontend:
  backend:
```

## Common Commands

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Scale services
docker-compose up -d --scale web=3

# Stop services
docker-compose down

# Rebuild images
docker-compose build --no-cache

# Execute command
docker-compose exec web sh
```

## Advanced Configuration

### Environment Files
```yaml
services:
  app:
    env_file:
      - .env
      - .env.production
    environment:
      - NODE_ENV=production
```

### Health Checks
```yaml
services:
  web:
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:3000/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

### Resource Limits
```yaml
services:
  web:
    deploy:
      resources:
        limits:
          cpus: '0.5'
          memory: 512M
        reservations:
          cpus: '0.25'
          memory: 256M
```

### Multiple Environments
```yaml
# docker-compose.override.yml
services:
  web:
    volumes:
      - .:/app
    command: npm run dev

# docker-compose.prod.yml
services:
  web:
    build:
      context: .
      dockerfile: Dockerfile.prod
    restart: always
```

## Best Practices

1. **Use .env files** - Manage environment variables
2. **Implement health checks** - Monitor service health
3. **Use named volumes** - Persist data properly
4. **Define networks** - Isolate services appropriately
5. **Use depends_on** - Control startup order
6. **Set resource limits** - Prevent resource exhaustion
7. **Use profiles** - Group services by purpose
8. **Implement logging** - Configure log drivers
9. **Use secrets** - Manage sensitive data
10. **Document services** - Add descriptions for complex setups
