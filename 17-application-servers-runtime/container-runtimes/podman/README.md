# Podman

## Overview

Podman is a daemonless container engine for developing, managing, and running OCI containers. It provides a Docker-compatible CLI while offering rootless execution and pod management capabilities.

## Daemonless Architecture

Unlike Docker, Podman runs containers as child processes of the calling user. This eliminates the central daemon bottleneck and reduces the attack surface for container operations.

## Rootless Containers

Podman runs entirely without root privileges using user namespaces and slirp4netns for networking. This improves security by preventing container escapes from gaining root access on the host.

## Pod Support

Podman natively supports pod concepts matching Kubernetes pod semantics. Pods group containers sharing network namespaces, enabling co-located services to communicate via localhost.

## Docker Compatibility

Podman provides a Docker-compatible CLI (alias docker=podman). Most Docker commands work unchanged, and Docker Compose files can be used with podman-compose.

## Podman Desktop

Podman Desktop provides a graphical interface for managing containers, images, pods, and volumes. It supports Kubernetes deployment and integrates with IDE extensions for development workflows.

## Systemd Integration

Podman containers can be managed as systemd services. Quadlet provides declarative container management integrated with systemd for production deployments on Linux systems.

## Image Building

Podman uses Buildah for building container images. Buildah supports Dockerfile builds and scriptable image creation without requiring a daemon process.

## Networking

Podman uses CNI or Netavark for container networking. Rootless networking uses slirp4netns or pasta for user-namespace-compatible network isolation.
