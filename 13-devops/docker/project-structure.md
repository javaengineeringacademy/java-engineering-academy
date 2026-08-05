# Docker Project Structure

## Standard Layout

### Basic Project
```
myproject/
├── Dockerfile
├── .dockerignore
├── docker-compose.yml
├── docker-compose.override.yml
├── src/
│   ├── app.py
│   └── requirements.txt
├── tests/
│   └── test_app.py
├── config/
│   └── nginx.conf
├── scripts/
│   └── entrypoint.sh
├── README.md
└── .gitignore
```

### Multi-Service Project
```
myproject/
├── docker-compose.yml
├── docker-compose.override.yml
├── docker-compose.prod.yml
├── .env
├── .env.example
├── services/
│   ├── api/
│   │   ├── Dockerfile
│   │   ├── .dockerignore
│   │   ├── requirements.txt
│   │   └── src/
│   ├── worker/
│   │   ├── Dockerfile
│   │   ├── requirements.txt
│   │   └── src/
│   └── nginx/
│       ├── Dockerfile
│       └── nginx.conf
├── scripts/
│   └── setup.sh
└── README.md
```

## .dockerignore

```
# Version control
.git
.gitignore

# Environment
.env
.env.*
!.env.example

# Dependencies
node_modules
venv
.venv
__pycache__
*.pyc

# Build artifacts
build
dist
*.egg-info

# IDE
.vscode
.idea
*.swp
*.swo

# OS files
.DS_Store
Thumbs.db

# Docker files
docker-compose*.yml
Dockerfile*

# Documentation
README.md
LICENSE
docs/
```

## Dockerfile Best Practices

### Python Application
```dockerfile
FROM python:3.11-slim

WORKDIR /app

# Install dependencies first (cache layer)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy application code
COPY . .

# Create non-root user
RUN useradd --create-home appuser
USER appuser

EXPOSE 8000

CMD ["python", "app.py"]
```

### Node.js Application
```dockerfile
FROM node:18-alpine

WORKDIR /app

# Install dependencies first
COPY package*.json ./
RUN npm ci --only=production

# Copy source code
COPY . .

EXPOSE 3000

CMD ["node", "server.js"]
```

## Multi-Stage Builds

### Production Build
```dockerfile
# Build stage
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production stage
FROM node:18-alpine
WORKDIR /app
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/node_modules ./node_modules
EXPOSE 3000
CMD ["node", "dist/server.js"]
```

### Python Multi-Stage
```dockerfile
FROM python:3.11-slim AS builder
WORKDIR /app
COPY requirements.txt .
RUN pip install --prefix=/install --no-cache-dir -r requirements.txt

FROM python:3.11-slim
WORKDIR /app
COPY --from=builder /install /usr/local
COPY . .
RUN useradd --create-home appuser
USER appuser
CMD ["python", "app.py"]
```

## docker-compose.yml

### Development
```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8000:8000"
    volumes:
      - .:/app
      - /app/node_modules
    environment:
      - NODE_ENV=development
    command: npm run dev
```

### Production
```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
      target: production
    ports:
      - "8000:8000"
    environment:
      - NODE_ENV=production
    restart: unless-stopped
    deploy:
      resources:
        limits:
          memory: 512M
```

## Scripts

### entrypoint.sh
```bash
#!/bin/bash
set -e

# Wait for dependencies
echo "Waiting for database..."
while ! nc -z db 5432; do
  sleep 1
done
echo "Database is ready"

# Run migrations
echo "Running migrations..."
python manage.py migrate

# Start application
echo "Starting application..."
exec "$@"
```

### setup.sh
```bash
#!/bin/bash
set -e

# Create .env from template
if [ ! -f .env ]; then
  cp .env.example .env
  echo "Created .env from .env.example"
fi

# Build and start
docker compose build
docker compose up -d

echo "Development environment is ready!"
```

## Best Practices

1. Use .dockerignore to exclude unnecessary files
2. Order Dockerfile instructions from least to most changing
3. Use multi-stage builds for smaller images
4. Use docker-compose.override.yml for development
5. Keep docker-compose.prod.yml for production
6. Use .env files for environment-specific variables
7. Create entrypoint scripts for initialization
8. Document setup in README.md
