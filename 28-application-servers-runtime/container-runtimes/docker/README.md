# Docker Engine

## Overview

Docker Engine is a containerization platform that packages applications with dependencies into standardized units called containers. It provides the tools for building, distributing, and running containers.

## Architecture

Docker Engine uses a client-server model with the Docker CLI communicating with the Docker daemon (dockerd). The daemon manages container lifecycle, images, networks, and volumes.

## Images and Containers

Images are read-only templates containing application code, runtime, libraries, and dependencies. Containers are writable instances of images running as isolated processes on the host.

## Dockerfile

Dockerfiles define image build instructions. Each instruction creates a layer in the image, and layer caching accelerates rebuilds when dependencies haven't changed.

```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
CMD ["node", "server.js"]
```

## Docker Compose

Docker Compose defines multi-container applications using YAML files. It orchestrates services, networks, and volumes for development and testing environments.

## Networking

Docker provides bridge, host, overlay, and macvlan network drivers. Bridge networking isolates containers, while overlay enables multi-host networking for Swarm and Kubernetes.

## Volumes

Docker volumes persist data beyond container lifecycle. Named volumes, bind mounts, and tmpfs mounts provide different persistence and performance characteristics.

## Docker Hub

Docker Hub is the default registry for finding and sharing container images. Organizations use private registries for proprietary images and security scanning.
