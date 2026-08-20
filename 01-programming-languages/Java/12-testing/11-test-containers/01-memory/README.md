# Test Containers - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           Test JVM                  │
│  - Testcontainers client            │
│  - Docker client (TCP)              │
│  - Container connection pools       │
├─────────────────────────────────────┤
│        Docker Daemon                │
│  - Container processes              │
│  - Container filesystem             │
│  - Network namespaces               │
└─────────────────────────────────────┘
```

## Memory Considerations

- Container processes use host resources
- Port mappings consume host ports
- Image layers cached in Docker
- Multiple containers share base layers

## Performance Tips

1. Use reusable containers across test classes
2. Pull images in advance
3. Use small base images
4. Configure appropriate resource limits
5. Use fixed ports for debugging
