# Docker Architecture

## Docker Engine

The core component that builds and runs containers.

### Components
- **dockerd**: Docker daemon (background process)
- **docker CLI**: Command-line interface
- **containerd**: Container runtime
- **runc**: Low-level container runtime

### Architecture Layers
```
┌─────────────────────────────────────┐
│           Docker CLI                │
├─────────────────────────────────────┤
│           dockerd                   │
├─────────────────────────────────────┤
│          containerd                 │
├─────────────────────────────────────┤
│            runc                     │
├─────────────────────────────────────┤
│        Linux Kernel                 │
└─────────────────────────────────────┘
```

## Container Runtime

### containerd
- Manages container lifecycle
- Handles image pulling/pushing
- Manages storage and networking
- Provides monitoring and logging

### runc
- OCI-compliant runtime
- Creates container processes
- Manages namespaces and cgroups
- Minimal and lightweight

## Linux Kernel Features

### Namespaces
Provide process isolation.

```bash
# Types of namespaces
- PID: Process ID isolation
- NET: Network isolation
- MNT: Mount point isolation
- UTS: Hostname isolation
- IPC: Inter-process communication
- USER: User ID isolation
- CGROUP: Cgroup isolation
```

### Control Groups (cgroups)
Limit and monitor resource usage.

```bash
# Resources managed
- CPU time
- Memory allocation
- I/O bandwidth
- Network bandwidth
- Device access
```

### Union File Systems
Layer-based filesystem for images.

```bash
# Storage drivers
- overlay2 (recommended)
- devicemapper
- btrfs
- zfs
```

## Image Layers

### Layer Architecture
```
┌─────────────────────────┐
│   Container Layer (RW)  │  ← Writable layer
├─────────────────────────┤
│   Image Layer 4         │  ← Read-only layers
├─────────────────────────┤
│   Image Layer 3         │
├─────────────────────────┤
│   Image Layer 2         │
├─────────────────────────┤
│   Image Layer 1 (Base)  │
└─────────────────────────┘
```

### Layer Benefits
- **Sharing**: Multiple containers share image layers
- **Caching**: Layers cached for faster builds
- **Efficiency**: Only changed layers are transferred
- **Storage**: Deduplication saves disk space

## Storage

### Storage Drivers
```bash
# overlay2 (default)
- Best performance
- Good for most workloads
- Supports page cache sharing

# devicemapper
- Good for RHEL/CentOS
- Direct LVM mode preferred

# btrfs/zfs
- Advanced features
- Better for specific use cases
```

### Volume Drivers
```bash
# Local driver (default)
- Stores on host filesystem
- Good for single-host use

# Remote drivers
- NFS, SMB, cloud storage
- Good for multi-host setups
```

## Networking

### Network Drivers
```bash
# bridge (default)
- Container-to-container on same host
- Network isolation between hosts

# host
- Remove network isolation
- Use host networking stack

# overlay
- Multi-host networking
- Used with Docker Swarm

# macvlan
- Assign MAC addresses
- Direct network access
```

### Network Stack
```
┌─────────────────────────┐
│    Application          │
├─────────────────────────┤
│    Network Namespace    │
├─────────────────────────┤
│    veth pair            │
├─────────────────────────┤
│    Bridge (docker0)     │
├─────────────────────────┤
│    Host Network Stack   │
└─────────────────────────┘
```

## Image Registry

### Docker Hub
- Public image repository
- Official base images
- Automated builds

### Private Registries
```bash
# Self-hosted
- Docker Registry
- Harbor
- Nexus

# Cloud-based
- AWS ECR
- Google GCR
- Azure ACR
```

### Image Distribution
```bash
# Push
docker push myimage:tag

# Pull
docker pull myimage:tag

# Save/Load
docker save myimage > image.tar
docker load < image.tar
```

## Security Features

### Container Security
- **Namespaces**: Process isolation
- **Seccomp**: System call filtering
- **AppArmor**: Mandatory access control
- **SELinux**: Security labels

### Image Security
```bash
# Scanning
docker scout cves myimage:tag

# Signing
docker trust sign myimage:tag

# Content trust
export DOCKER_CONTENT_TRUST=1
```

## BuildKit

### Features
- Parallel build stages
- Build caching
- Secret mounting
- SSH forwarding

### Usage
```bash
# Enable BuildKit
DOCKER_BUILDKIT=1 docker build .

# Or in daemon.json
{"features": {"buildkit": true}}
```

## Performance Considerations

### Optimization Tips
1. Use multi-stage builds
2. Leverage build cache
3. Minimize layers
4. Use .dockerignore
5. Choose appropriate base images

### Resource Limits
```bash
# CPU limit
docker run --cpus=2 myimage

# Memory limit
docker run --memory=512m myimage

# IO limits
docker run --device-read-bps /dev/sda:1mb myimage
```
