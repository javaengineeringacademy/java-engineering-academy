# containerd

## Overview

containerd is an industry-standard container runtime that manages the complete container lifecycle. Originally extracted from Docker, it serves as the core runtime for Kubernetes and other container platforms.

## Architecture

containerd uses a gRPC API for client communication and a plugin-based architecture for extensibility. Core plugins handle image management, container execution, and snapshot management.

## CRI Compatibility

containerd implements the Container Runtime Interface (CRI) plugin for Kubernetes integration. It manages pod sandbox creation, container lifecycle, and image operations for Kubernetes nodes.

## Image Management

containerd handles image pulling, pushing, unpacking, and mounting. It supports multiple image formats and provides content-addressable storage for efficient image distribution.

## Snapshots

containerd uses snapshot drivers for filesystem management. Different drivers (overlayfs, btrfs, zfs) provide various performance and feature characteristics for container storage.

## Shims

containerd uses shim processes between itself and container runtimes (runc, crun). Shims keep container processes running if containerd restarts, improving reliability.

## Namespace Isolation

containerd supports namespaces for multi-tenant deployments. Different namespaces can manage independent sets of containers, images, and configurations within a single containerd instance.

## Integration

containerd integrates with Docker (as its runtime), Kubernetes, and other container platforms. Its stable gRPC API enables custom tooling and workflow automation.

## Monitoring

containerd provides metrics through Prometheus endpoints and events through gRPC streaming. Monitoring container health, resource usage, and event patterns supports operational visibility.
