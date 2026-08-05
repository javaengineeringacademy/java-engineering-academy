# Docker Relationships

## Works With

### Kubernetes

Docker provides the container runtime for Kubernetes. Kubernetes orchestrates Docker containers across a cluster. Docker images are the standard unit of deployment in Kubernetes.

Kubernetes uses containerd as its runtime by default (not Docker daemon directly). Docker images work with any OCI-compliant runtime.

### Docker Compose

Compose defines and runs multi-container Docker applications. It uses a YAML file to configure services, networks, and volumes. Compose is ideal for local development and testing.

Compose v2 integrates with Docker Desktop and runs as a Docker CLI plugin. Compose v1 was a separate binary.

### Docker Swarm

Swarm mode provides native clustering for Docker. It uses the Docker API for orchestration. Swarm is simpler than Kubernetes but less feature-rich.

Swarm uses overlay networks for cross-host communication. It supports rolling updates and service scaling.

## Alternative

### Podman

Podman is a daemonless container engine compatible with Docker CLI and images. It runs containers as child processes without a background daemon. Podman supports rootless containers.

Consider Podman for security-sensitive environments where daemonless operation is preferred. It is a drop-in replacement for most Docker CLI commands.

Podman uses `podman-compose` or `podman generate kube` for multi-container orchestration.

## Competitor

### containerd

containerd is a container runtime used by Docker, Kubernetes, and other orchestrators. It manages the complete container lifecycle. Docker uses containerd as its runtime.

containerd is more lightweight than the full Docker daemon. It provides only runtime functionality, not build tools or CLI.

For Kubernetes, containerd is the preferred runtime over Docker. It has lower resource overhead and fewer components.

## Migration Notes

Migrating from Docker to alternatives requires consideration of:
- Dockerfile compatibility (OCI standard)
- Image format compatibility
- Compose file compatibility
- Volume and network configuration
- Build context and caching

Migrating to Docker from alternatives requires:
- Dockerfile creation or adaptation
- Image building workflow
- Compose configuration for multi-container setups
- Registry integration
- CI/CD pipeline updates
