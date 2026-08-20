# Test Containers - Internals

## Container Lifecycle

1. **Image Pull**: Download Docker image if not cached
2. **Container Create**: Create container with configuration
3. **Container Start**: Start the container
4. **Wait for Ready**: Check wait strategy condition
5. **Execute Tests**: Run test methods
6. **Container Stop**: Send stop signal
7. **Container Remove**: Remove container and resources

## Port Mapping

```
Host Port → Container Port
Dynamic   → Fixed (e.g., 5432)
Assigned by Docker
```

## Wait Strategies

| Strategy | Condition |
|----------|-----------|
| WaitForLogMessage | Log contains string |
| WaitForHttpEndpoint | HTTP endpoint responds |
| WaitForListeningPort | Port accepts connections |
| WaitAllStrategy | All child strategies pass |

## Resource Cleanup

- Containers stopped via Docker API
- Volumes removed if created by Testcontainers
- Networks cleaned up
- Temp files deleted
