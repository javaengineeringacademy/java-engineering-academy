# Docker Corner Cases

## Overlay Network Issues

Overlay networks enable container-to-container communication across hosts. DNS resolution may fail if the Docker daemon restarts. Containers connected before the restart lose network connectivity.

Use `--restart=always` to restart containers, but be aware that network state may not fully restore. Remove and recreate containers if network issues persist.

## Storage Driver Corruption

The `overlay2` storage driver stores layers on the host filesystem. If the filesystem becomes corrupted, containers may fail to start. Running `docker system prune` removes unused data but does not fix corruption.

Use `docker inspect` to check layer health. If layers are corrupted, remove the image and re-pull it. Consider using a dedicated volume for container data.

## Container Runtime Errors

`OCI runtime create failed` often indicates a missing runtime or misconfigured containerd. Check that the correct runtime is specified in `daemon.json`. Default is `runc`.

`no such file or directory` errors when starting containers usually mean the entrypoint or command binary is missing from the image. Verify the Dockerfile `ENTRYPOINT` and `CMD`.

## DNS Resolution Failures

Containers may fail to resolve external DNS names. This is often caused by the Docker daemon using the host's DNS resolver, which is not accessible from within the container.

Use `--dns` flag to specify a custom DNS server. For custom networks, set DNS in the network configuration.

## Volume Permission Issues

Volumes mounted from the host may have incorrect permissions inside the container. The container runs as a specific user (often root), but the host files may belong to a different user.

Use `--user` to run the container as a specific UID/GID. Or use `chown` on the host to adjust permissions before mounting.

## Image Layer Caching Failures

Docker caches image layers during build. If a layer is invalidated (e.g., `COPY` source changed), all subsequent layers are rebuilt. This can cause slow builds if layers are not ordered correctly.

Use multi-stage builds to reduce image size and build time. Place frequently changing instructions later in the Dockerfile.

## Docker Compose Dependency Ordering

`depends_on` does not wait for a service to be ready, only for it to start. A web server may start before its database is ready. Use `healthcheck` and `depends_on.condition: service_healthy` for proper ordering.

Compose v2 uses `depends_on` with conditions. Compose v1 does not support conditions.

## Container Logging

Default logging driver is `json-file`. Logs are written to `/var/lib/docker/containers/<id>/<id>-json.log`. Without log rotation, logs grow unbounded and can fill the disk.

Use `--log-opt max-size=10m --log-opt max-file=3` to enable log rotation. For production, consider `syslog` or `fluentd` logging drivers.

## Image Pull Failures

`unauthorized` errors indicate authentication issues. Use `docker login` to authenticate before pulling private images. `manifest unknown` means the image or tag does not exist in the registry.

`i/o timeout` indicates network issues. Check proxy settings and firewall rules. Use `--pull always` to force re-pulling.

## Docker Daemon Resource Limits

The Docker daemon uses host resources. Without limits, containers can consume all CPU, memory, and disk. Use `--cpus`, `--memory`, and `--storage-opt` to set limits.

`OOMKilled` indicates the container exceeded its memory limit. Increase the limit or optimize the application.

## Network Port Conflicts

`bind: address already in use` means the host port is occupied. Use `docker ps` to find which container is using the port. Or use a different host port: `-p 8081:80`.

Use `docker network ls` and `docker network inspect` to check network conflicts.
