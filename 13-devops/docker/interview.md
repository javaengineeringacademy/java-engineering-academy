# Docker Interview Questions

## Basic Concepts

### 1. What is Docker?
Docker is a platform for developing, shipping, and running applications in containers. Containers are lightweight, portable, and self-sufficient units that package application code with all its dependencies.

### 2. What is the difference between a container and a virtual machine?
- **Container**: Shares host OS kernel, lightweight, fast startup
- **VM**: Has own OS, heavier, slower startup, better isolation

### 3. What is a Docker image?
A read-only template containing application code, runtime, libraries, and dependencies. Images are built from Dockerfiles and stored in registries.

### 4. What is a Dockerfile?
A text file containing instructions for building a Docker image. It defines the base image, dependencies, and configuration.

### 5. What is Docker Hub?
A cloud-based registry for storing and sharing Docker images. It hosts official and community images.

## Intermediate Concepts

### 6. Explain Docker layers.
Docker images consist of multiple layers. Each instruction in a Dockerfile creates a new layer. Layers are cached and shared between images, making builds faster and images smaller.

### 7. What is the difference between COPY and ADD?
- **COPY**: Copies files from host to container
- **ADD**: Same as COPY but can also extract tar archives and fetch URLs

### 8. What is the difference between ENTRYPOINT and CMD?
- **ENTRYPOINT**: Configures the container's main executable
- **CMD**: Provides default arguments, can be overridden
- **Combined**: ENTRYPOINT is the command, CMD provides default arguments

### 9. What are Docker volumes?
Persistent data storage mechanism that persists data beyond container lifecycle. Types: named volumes, bind mounts, tmpfs.

### 10. What is Docker Compose?
A tool for defining and running multi-container applications using YAML files. It manages services, networks, and volumes.

## Advanced Concepts

### 11. What is a multi-stage build?
A Dockerfile technique using multiple FROM statements to build smaller images. Build stage contains build tools, production stage contains only runtime.

### 12. Explain Docker networking drivers.
- **bridge**: Default, container-to-container on same host
- **host**: Removes network isolation
- **overlay**: Multi-host networking for Swarm
- **macvlan**: Assigns MAC addresses
- **none**: No networking

### 13. What is container orchestration?
Automating deployment, scaling, and management of containers. Tools: Docker Swarm, Kubernetes, Amazon ECS.

### 14. What is Docker Swarm?
Docker's native clustering and orchestration solution. It turns a pool of Docker hosts into a single virtual host.

### 15. How do you secure Docker containers?
- Use non-root user
- Scan images for vulnerabilities
- Use read-only filesystem
- Set resource limits
- Use Docker secrets
- Implement health checks

## System Design

### 16. How would you design a microservices architecture with Docker?
- Each service in its own container
- Use Docker Compose for local development
- Use orchestration for production
- Implement service discovery
- Use API gateway
- Centralized logging

### 17. How would you handle secrets in Docker?
- Use Docker secrets (Swarm)
- Use environment variables (less secure)
- Use external secret management (Vault, AWS Secrets Manager)
- Never commit secrets to version control

## Best Practices

### 18. What are Docker best practices?
1. Use official base images
2. Minimize layers
3. Use multi-stage builds
4. Use .dockerignore
5. Run as non-root user
6. Set health checks
7. Set resource limits
8. Scan images for vulnerabilities

### 19. How do you optimize Docker image size?
- Use smaller base images (Alpine, slim)
- Use multi-stage builds
- Clean up after installation
- Use .dockerignore
- Combine RUN commands

### 20. How do you debug Docker containers?
```bash
docker logs mycontainer
docker exec -it mycontainer bash
docker inspect mycontainer
docker stats
docker top mycontainer
```

## Quick Reference

### Essential Commands
```bash
docker build -t myimage .
docker run -d -p 80:80 myimage
docker ps
docker logs mycontainer
docker exec -it mycontainer bash
docker stop mycontainer
docker rm mycontainer
docker rmi myimage
```

### Compose Commands
```bash
docker compose up -d
docker compose down
docker compose ps
docker compose logs
docker compose build
docker compose exec myservice bash
```
