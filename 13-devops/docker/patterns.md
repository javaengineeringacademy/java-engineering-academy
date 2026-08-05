# Docker Patterns

## 1. Multi-Stage Builds

**Problem:** Final images contain build tools, source code, and dependencies not needed at runtime, bloating image size.

**Solution:** Use multiple FROM stages to build in one stage and copy only artifacts to a minimal runtime stage.

**Implementation:**
```dockerfile
# Build stage
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build

# Runtime stage
FROM node:20-alpine AS runtime
WORKDIR /app
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/node_modules ./node_modules
EXPOSE 3000
CMD ["node", "dist/server.js"]
```

**When to Use:** Any compiled language or framework where build dependencies differ from runtime dependencies.

**When NOT to Use:** Simple scripts or interpreted languages with no build step. The overhead of multi-stage is unnecessary.

---

## 2. Distroless Images

**Problem:** Base images include shells, package managers, and utilities that increase attack surface.

**Solution:** Use distroless images containing only the application runtime and its dependencies.

**Implementation:**
```dockerfile
FROM gcr.io/distroless/java17-debian12
COPY target/app.jar /app.jar
USER nonroot
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

**When to Use:** Production images where security is a priority and debugging via shell is not needed.

**When NOT to Use:** Development images where you need shell access for debugging or interactive troubleshooting.

---

## 3. One Process Per Container

**Problem:** Multiple processes in one container cannot be independently scaled, restarted, or monitored.

**Solution:** Run a single process per container. Use orchestrators for multi-process coordination.

**Implementation:**
```dockerfile
# Bad: multiple processes
CMD ["sh", "-c", "nginx -g 'daemon off;' & php-fpm -D && cron -f"]

# Good: single process
CMD ["php-fpm", "--nodaemonize"]
```

**When to Use:** Always in production. Each concern (web server, worker, scheduler) gets its own container.

**When NOT to Use:** Local development where docker-compose replaces orchestration and convenience matters.

---

## 4. Healthcheck Pattern

**Problem:** Orchestration platforms cannot determine if a container is actually serving traffic.

**Solution:** Define HEALTHCHECK instructions that probe application readiness.

**Implementation:**
```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# For containers without curl
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD node -e "require('http').get('http://localhost:8080/health', r => { process.exit(r.statusCode === 200 ? 0 : 1) })"
```

**When to Use:** Every container in a production orchestrator. Without healthchecks, orchestrators cannot route or restart intelligently.

**When NOT to Use:** One-off batch containers or CLI tools that exit after completing work.

---

## 5. Layer Caching Optimization

**Problem:** Unnecessary layer invalidation causes slow rebuilds when only application code changes.

**Solution:** Order Dockerfile instructions from least-changing to most-changing to maximize cache hits.

**Implementation:**
```dockerfile
# Order matters: system deps first, then app deps, then source
FROM node:20-alpine
RUN apk add --no-cache tini          # Rarely changes
COPY package.json package-lock.json ./
RUN npm ci --only=production          # Changes on dep updates
COPY . .                              # Changes frequently
ENTRYPOINT ["tini", "--"]
CMD ["node", "server.js"]
```

**When to Use:** Always. Layer caching is free and dramatically speeds up builds.

**When NOT to Use:** Never. There is no scenario where ignoring layer order is beneficial.

---

## 6. .dockerignore

**Problem:** Build context includes node_modules, .git, and test files, slowing builds and leaking secrets.

**Solution:** Use .dockerignore to exclude unnecessary files from the build context.

**Implementation:**
```
node_modules
.git
.env
.env.*
*.md
tests/
docker-compose*.yml
.dockerignore
Dockerfile
.vscode
coverage/
```

**When to Use:** Every project. Even small exclusions reduce context transfer time and prevent accidental secret exposure.

**When NOT to Use:** Never. Always maintain a .dockerignore file.

---

## 7. Non-Root User

**Problem:** Containers running as root can escape container boundaries and compromise the host.

**Solution:** Create and switch to a non-root user in the Dockerfile.

**Implementation:**
```dockerfile
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Or with numeric UID for Kubernetes compatibility
RUN addgroup -g 1001 appgroup && adduser -u 1001 -G appgroup -s /bin/sh -D appuser
USER 1001:1001
```

**When to Use:** Every production container. Running as root is a security anti-pattern.

**When NOT to Use:** When the application genuinely requires root (e.g., binding to port 80 without capabilities). Use capabilities instead.

---

## 8. Golden Base Image

**Problem:** Teams use inconsistent base images, leading to drift, vulnerability gaps, and support burden.

**Solution:** Maintain a single golden base image with hardened OS, approved packages, and shared configuration.

**Implementation:**
```dockerfile
# Golden base (maintained by platform team)
FROM ubuntu:22.04
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl ca-certificates tini && \
    rm -rf /var/lib/apt/lists/* && \
    useradd -m -s /bin/bash appuser
HEALTHCHECK --interval=30s CMD curl -f http://localhost:8080/health || exit 1
ENTRYPOINT ["tini", "--"]

# Application Dockerfile
FROM myorg/golden-base:2024.01
COPY --chown=appuser:appuser target/app.jar /app/app.jar
USER appuser
CMD ["java", "-jar", "/app/app.jar"]
```

**When to Use:** Organizations with multiple teams building containers that need consistent base patching and compliance.

**When NOT to Use:** Solo projects or prototypes where the overhead of maintaining a golden image is not justified.

---

## Best Practices

- Use specific image tags, never `:latest`, for reproducible builds.
- Combine related RUN commands to minimize layers.
- Run containers as non-root with read-only filesystems where possible.
- Use tini or dumb-init as PID 1 for proper signal handling.
- Scan images with Trivy or Snyk before pushing to registry.
- Use `.dockerignore` to exclude secrets and build artifacts.
- Prefer Alpine or distroless for smaller attack surfaces.
