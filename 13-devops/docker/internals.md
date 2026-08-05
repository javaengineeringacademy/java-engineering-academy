# Docker Internals

## Linux Namespaces

Docker containers use Linux namespaces for isolation. Each namespace provides a separate view of a global resource. The PID namespace gives containers their own process ID space, making the container's init process appear as PID 1. Nested PID namespaces allow containers to contain other containers. The network namespace provides isolated network interfaces, IP addresses, routing tables, and firewall rules.

The mount namespace isolates the filesystem mount points, allowing each container to have its own root filesystem. The UTS namespace isolates hostname and domain name. The IPC namespace isolates inter-process communication resources. The user namespace maps container root to a non-privileged host user, reducing the attack surface. Docker uses six namespace types to achieve process-level isolation.

Namespace creation uses the `clone()` system call with namespace flags. Each container gets its own set of namespaces. The `nsenter` utility allows entering a container's namespaces for debugging. Namespace types can be selectively disabled for privileged containers. The `--pid=host` flag shares the host PID namespace with the container.

The network namespace provides complete network isolation. Each container gets its own network stack: interfaces, IP addresses, routing tables, and iptables rules. The `--net=host` flag shares the host network namespace. The `--net=none` flag creates a container with no network access. The `--net=container:name` flag shares another container's network namespace.

## Control Groups (cgroups)

Cgroups limit, account for, and isolate resource usage. Docker uses cgroups v2 by default, which provides a unified hierarchy. Each container gets its own cgroup subtree. CPU limits are enforced via CFS (Completely Fair Scheduler) bandwidth control. Memory limits trigger the OOM killer when exceeded. Cgroups v2 simplifies the hierarchy compared to v1.

I/O bandwidth is controlled via blkio controllers. Network bandwidth can be limited using the tc (traffic control) subsystem integrated with cgroups. Docker translates resource flags (--memory, --cpus, --blkio-weight) into cgroup parameters. Cgroups also provide accounting: `docker stats` reads cgroup files like `cpuacct.usage` and `memory.usage_in_bytes`.

CPU shares define relative weights between containers. CPU sets pin containers to specific CPU cores. Memory+swap limits prevent swap usage from masking memory pressure. OOM kill scores determine which containers are killed first. The `--oom-kill-disable` flag prevents OOM kills for specific containers.

The cgroup filesystem is mounted at `/sys/fs/cgroup`. Each container's cgroup is created as a subdirectory. The `docker inspect` command shows cgroup settings. The `--cpu-shares` flag sets relative CPU weight. The `--cpuset-cpus` flag pins to specific CPUs. The `--memory-reservation` flag sets soft memory limits.

## Union Filesystem

Docker images use a union filesystem to layer multiple read-only layers into a single view. The container runtime (overlay2 by default) merges layers using copy-on-write. When a container writes to a file from a lower layer, the file is copied to the upper writable layer. This minimizes disk usage since unchanged files are shared across layers.

Each Dockerfile instruction creates a new layer. The base image layer is at the bottom; the topmost layer contains the final filesystem state. Overlay2 uses lower directories (read-only image layers) and an upper directory (writable container layer). The merged view is presented to the container. When the container is removed, the upper layer is deleted.

Layer deduplication uses content-addressable storage. Two layers with identical content share the same storage. The `docker system df` command reports layer disk usage. Whiteout files mark deletions in upper layers. The overlay2 driver uses the kernel's overlayfs module for efficient layer merging.

The union filesystem supports transparent compression. The ZSTD and LZ4 algorithms compress layers on disk. The `--storage-opt size=10G` flag limits container writable layer size. The `docker diff` command shows filesystem changes in a container. The `--userns-remap` flag uses user namespaces for layer isolation.

## Container Runtime

The container runtime handles container lifecycle operations. Docker uses containerd as the high-level runtime and runc as the low-level OCI-compliant runtime. When `docker run` is executed, the Docker daemon sends the request to containerd, which delegates to runc. Runc creates the container process using Linux syscalls: `clone()` with namespace flags, `pivot_root()` for the filesystem, and `chroot()` as fallback.

After the container process is created, runc exits and containerd manages the container's lifecycle. This separation allows Docker to support alternative runtimes (Kata Containers, gVisor) via the containerd-shim. The shim keeps container stdin/stdout open and reports status back to containerd. The OCI runtime specification defines the interface between containerd and runc.

The container lifecycle includes: create (prepare namespaces and filesystem), start (execute the container process), pause/resume (freeze/unfreeze cgroups), stop (send SIGTERM, then SIGKILL), and delete (clean up resources). The containerd-shim-v2 protocol manages these operations. The `docker inspect` command shows container state and configuration.

Container namespaces are created during the create phase. The mount namespace is set up with `pivot_root`. The PID namespace isolates the container process tree. The network namespace is configured by the CNI plugin. The container's init process is executed with the specified entrypoint and command.

## Image Layer Management

Images are stored as content-addressable layers in the registry. Each layer is identified by a SHA256 digest. The image manifest maps layer digests to the full filesystem diff. Docker pull downloads only layers not present locally, enabling efficient distribution. The image config stores metadata (entrypoint, cmd, env, labels).

Layer caching accelerates builds. When a Dockerfile instruction matches a previous build's instruction and inputs, Docker reuses the cached layer. BuildKit (Docker's modern build engine) parallelizes independent layer construction and uses a directed acyclic graph to optimize the build plan. Secret mounting and SSH forwarding are supported without persisting secrets in layers.

BuildKit uses a BuildKit daemon for remote builds. Build contexts are sent to the daemon, which executes instructions. Multi-stage builds create intermediate images that are discarded in the final stage. Build cache imports/exports allow sharing cache across builds. The `docker buildx` command supports multi-platform builds.

The image manifest contains a list of layer digests and the image config digest. The config stores environment variables, entrypoint, cmd, and labels. The image index (manifest list) supports multi-platform images. The `docker manifest inspect` command shows manifest details. The `docker history` command shows layer creation information.

## Storage Drivers

Docker supports multiple storage drivers for different filesystems. overlay2 is the recommended driver for most Linux systems. It uses the overlayfs kernel module to merge layers. btrfs and zfs drivers provide native copy-on-write semantics. devicemapper uses block-level thin provisioning.

Storage driver selection impacts performance and feature support. overlay2 does not support per-layer quotas; btrfs and zfs do. ZFS provides built-in compression, snapshots, and checksums. Docker stores driver metadata in `/var/lib/docker/<driver>/`. The `docker info` command reports the active storage driver and filesystem.

The storage driver manages image layers and container writable layers. Image layers are immutable and shared across containers. Container layers are per-container and writable. The `docker system prune` removes unused layers and containers. Storage quotas prevent unbounded disk usage. The `--storage-driver` flag selects the storage driver.

The overlay2 driver uses the kernel's overlayfs module. The lower directory contains image layers; the upper directory contains the container's writable layer. The merged directory presents the combined view. The `docker inspect` command shows the container's layer information. The `docker commit` command creates a new image from a container's changes.

## Networking Model

Docker creates a virtual network for each container. The bridge driver (default) creates a Linux bridge (`docker0`) and veth pairs. Each container gets one end of the veth pair; the other end attaches to the bridge. NAT provides outbound internet access. Bridge networks provide inter-container communication on the same host.

The overlay driver enables multi-host networking for Swarm and standalone containers. Overlay networks use VXLAN encapsulation to tunnel traffic between hosts. The macvlan driver assigns MAC addresses to containers, making them appear as physical devices on the network. Docker also supports custom network plugins for advanced configurations.

DNS resolution in Docker uses an embedded DNS server (127.0.0.11). Service discovery in Swarm mode uses DNS round-robin. Network isolation is enforced via network policies. Container-to-container traffic can be encrypted using overlay network encryption. The `docker network inspect` command shows network configuration and connected containers.

Docker networks support IP address management (IPAM). The default IPAM driver assigns IP addresses from a subnet. Custom IPAM drivers support external IP management. The `--ip-range` flag limits the IP range for containers. The `--subnet` flag specifies the network subnet. The `--gateway` flag sets the network gateway.

## Security Model

Docker reduces the attack surface through several mechanisms. User namespaces remap container root to an unprivileged host UID. Seccomp profiles filter dangerous syscalls (30+ blocked by default). AppArmor and SELinux provide mandatory access control. Capabilities drop unnecessary root privileges; Docker drops 14 capabilities by default.

Rootless mode runs the Docker daemon and containers without root privileges, using user namespaces and slirp4netns for networking. Content trust (Docker Notary) verifies image signatures. Read-only root filesystems prevent runtime modification. Resource limits (cgroups) prevent resource exhaustion attacks.

The Docker Content Trust system uses digital signatures to verify image integrity. Images signed by trusted publishers can be verified before pulling. The `DOCKER_CONTENT_TRUST=1` environment variable enables verification. Secret management uses Docker secrets in Swarm mode or external vaults. Container image scanning identifies known vulnerabilities before deployment.

Docker security best practices include: running containers as non-root users, using read-only root filesystems, dropping unnecessary capabilities, using seccomp profiles, and scanning images for vulnerabilities. The `docker scout` command provides vulnerability scanning. The `--security-opt` flag configures security options. The `--cap-drop` flag removes specific capabilities.
