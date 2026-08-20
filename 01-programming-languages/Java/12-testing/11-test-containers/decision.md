# Decision: Test Containers

## When to Use Test Containers

**Use Testcontainers when:**
- Testing with real databases
- Integration testing with message brokers
- Testing against specific Docker images
- Need production-like test environments

**Use embedded databases when:**
- Simple unit tests
- Fast feedback is needed
- Docker is not available
- Database features are not critical

## Container Selection

| Need | Container |
|------|-----------|
| PostgreSQL | PostgreSQLContainer |
| MySQL | MySQLContainer |
| Kafka | KafkaContainer |
| Redis | GenericContainer("redis") |
| Custom | GenericContainer(imageName) |

## Configuration Guidelines

1. Use @Container for automatic lifecycle
2. Configure wait strategies for readiness
3. Use fixed ports for debugging
4. Share containers across test classes when possible
5. Clean up volumes after tests
