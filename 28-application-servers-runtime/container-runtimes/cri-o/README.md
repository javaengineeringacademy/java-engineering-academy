# CRI-O

## Overview

CRI-O is a lightweight container runtime specifically designed for Kubernetes. It implements the Container Runtime Interface (CRI) to run OCI-compatible containers with minimal overhead.

## Purpose-Built for Kubernetes

CRI-O was created to provide a stable, secure, and minimal container runtime for Kubernetes. It removes features unnecessary for Kubernetes while maintaining full CRI compatibility.

## Architecture

CRI-O uses a daemon process (crio) that listens on a Unix socket for CRI requests from the kubelet. It manages container creation, execution, and cleanup through OCI-compliant runtimes.

## OCI Runtime Support

CRI-O supports multiple OCI runtimes including runc, crun, and KATA Containers. The runtime can be configured per-pod for different isolation requirements.

## Image Management

CRI-O pulls images from registries using containers/image library. It supports image filtering, signature verification, and storage drivers for efficient image management.

## Pod Sandbox Management

CRI-O creates and manages pod sandboxes using the OCI runtime. Sandboxes provide the network and storage namespace for all containers within a pod.

## Integration with Podman

CRI-O shares image storage with Podman through containers/image and containers/storage libraries. Images pulled by Podman are available to CRI-O and vice versa.

## Security

CRI-O enforces security through SELinux integration, seccomp profiles, and capabilities dropping. It runs containers with minimal privileges and supports user namespace remapping.

## Configuration

CRI-O configuration (crio.conf) defines runtime options, image configuration, and network settings. Configuration can be managed through systemd and Kubernetes node configuration.
