# Docker Anti-Patterns

## 1. Large Docker Images
**Description:** Using base images that include unnecessary tools and dependencies.

**Why it's bad:** Slower pulls, larger attack surface, more storage costs.

**Example (bad code):**
```dockerfile
FROM ubuntu:22.04
RUN apt-get update && apt-get install -y \
    python3 python3-pip build-essential \
    vim curl wget git
COPY . /app
```

**Better approach:** Use minimal base images:
```dockerfile
FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .
```

**Impact:** Smaller images, faster deployments, reduced attack surface.

---

## 2. Running as Root
**Description:** Running containers as root user.

**Why it's bad:** Security risk, container escape vulnerabilities.

**Example (bad code):**
```dockerfile
FROM node:18
WORKDIR /app
COPY . .
RUN npm install
CMD ["node", "server.js"]
# Runs as root by default
```

**Better approach:** Use non-root user:
```dockerfile
FROM node:18
RUN groupadd -r appgroup && useradd -r -g appgroup appuser
WORKDIR /app
COPY --chown=appuser:appgroup . .
RUN npm install
USER appuser
CMD ["node", "server.js"]
```

**Impact:** Improved security, reduced privilege escalation risk.

---

## 3. No Health Checks
**Description:** Not defining HEALTHCHECK instructions.

**Why it's bad:** Orchestration cannot detect unhealthy containers.

**Example (bad code):**
```dockerfile
FROM nginx:latest
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
# No health check defined
```

**Better approach:** Add health checks:
```dockerfile
FROM nginx:latest
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost/ || exit 1
```

**Impact:** Better orchestration, automatic recovery.

---

## 4. Not Using Multi-Stage Builds
**Description:** Including build tools in final image.

**Why it's bad:** Larger images, unnecessary dependencies in production.

**Example (bad code):**
```dockerfile
FROM node:18
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build
CMD ["node", "dist/server.js"]
# Includes dev dependencies and build tools
```

**Better approach:** Use multi-stage builds:
```dockerfile
FROM node:18 AS builder
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

FROM node:18-slim
WORKDIR /app
COPY --from=builder /app/dist ./dist
CMD ["node", "dist/server.js"]
```

**Impact:** Minimal production images, reduced attack surface.

---

## 5. Copying Everything
**Description:** Using COPY . . without .dockerignore.

**Why it's bad:** Includes unnecessary files, larger context, slower builds.

**Example (bad code):**
```dockerfile
FROM node:18
WORKDIR /app
COPY . .
```

**Better approach:** Use .dockerignore:
```dockerignore
# .dockerignore
node_modules
.git
.env
*.md
Dockerfile
docker-compose.yml
```

**Impact:** Smaller build context, faster builds.

---

## 6. Not Using Specific Image Tags
**Description:** Using `latest` tag for base images.

**Why it's bad:** Unpredictable builds, may break with new versions.

**Example (bad code):**
```dockerfile
FROM node:latest
FROM python:latest
FROM nginx:latest
```

**Better approach:** Pin specific versions:
```dockerfile
FROM node:18.17.0-slim
FROM python:3.11.4-slim
FROM nginx:1.25.1-alpine
```

**Impact:** Reproducible builds, predictable behavior.

---

## 7. Multiple RUN Commands
**Description:** Not combining RUN commands to reduce layers.

**Why it's bad:** More layers, larger image, slower builds.

**Example (bad code):**
```dockerfile
RUN apt-get update
RUN apt-get install -y curl
RUN apt-get install -y wget
RUN apt-get clean
```

**Better approach:** Combine and clean up:
```dockerfile
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        curl \
        wget && \
    rm -rf /var/lib/apt/lists/*
```

**Impact:** Fewer layers, smaller image, faster builds.

---

## 8. Not Using .dockerignore
**Description:** Not excluding unnecessary files from build context.

**Why it's bad:** Larger context, slower builds, potential security issues.

**Example (bad code):**
```bash
docker build .
# Includes .git, node_modules, etc.
```

**Better approach:** Create .dockerignore:
```dockerignore
.git
node_modules
.env
*.log
__pycache__
.pytest_cache
```

**Impact:** Smaller context, faster builds, better security.

---

## 9. Storing Secrets in Images
**Description:** Hardcoding secrets in Dockerfile or image layers.

**Why it's bad:** Secrets exposed in image history, security risk.

**Example (bad code):**
```dockerfile
ENV DB_PASSWORD=secret123
COPY .env /app/.env
```

**Better approach:** Use secrets management:
```dockerfile
# Use build secrets
RUN --mount=type=secret,id=db_password \
    cat /run/secrets/db_password

# Or runtime secrets
# docker run -e DB_PASSWORD=secret
```

**Impact:** Secrets protected, better security.

---

## 10. Not Using Health Checks for Dependencies
**Description:** Not waiting for dependencies to be ready.

**Why it's bad:** Application starts before dependencies are available.

**Example (bad code):**
```dockerfile
CMD ["python", "app.py"]
# Assumes database is ready
```

**Better approach:** Wait for dependencies:
```dockerfile
HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
    CMD pg_isready -U postgres || exit 1
```

**Impact:** Reliable startup, dependency readiness.

---

## 11. Using ADD Instead of COPY
**Description:** Using ADD when COPY would suffice.

**Why it's bad:** Unexpected behavior, extraction, URLs.

**Example (bad code):**
```dockerfile
ADD config.json /app/config.json
ADD https://example.com/file /app/file
```

**Better approach:** Use COPY for local files:
```dockerfile
COPY config.json /app/config.json
# Only use ADD for tar extraction
ADD archive.tar.gz /app/
```

**Impact:** Predictable behavior, clearer intent.

---

## 12. Not Optimizing Layer Caching
**Description:** Not structuring Dockerfile for optimal layer caching.

**Why it's bad:** Unnecessary rebuilds, slower development cycles.

**Example (bad code):**
```dockerfile
FROM node:18
WORKDIR /app
COPY . .
RUN npm install
```

**Better approach:** Order by change frequency:
```dockerfile
FROM node:18
WORKDIR /app
COPY package*.json ./
RUN npm install --only=production
COPY . .
```

**Impact:** Better cache utilization, faster builds.