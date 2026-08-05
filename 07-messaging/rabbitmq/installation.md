# RabbitMQ Installation

> Docker, apt, Homebrew, Cloud, and manual installation methods.

## Docker

### Quick Start

```bash
docker run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  rabbitmq:3-management
```

### Docker Compose

```yaml
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: password
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
      - ./rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf
    healthcheck:
      test: rabbitmq-diagnostics -q ping
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  rabbitmq_data:
```

### Docker Tags

| Tag | Description |
|-----|-------------|
| `latest` | Latest stable release |
| `3-management` | With management UI |
| `3-alpine` | Minimal Alpine image |
| `3.12.x` | Specific version |

## Ubuntu/Debian (apt)

```bash
# Add repository
curl -1sLf 'https://packagecloud.io/rabbitmq/rabbitmq-server/gpgkey' | sudo apt-key add -
curl -1sLf 'https://packagecloud.io/rabbitmq/rabbitmq-server/ubuntu/dists/jammy/main/binary-amd64/Packages' | sudo tee /etc/apt/sources.list.d/rabbitmq.list

# Install
sudo apt-get update
sudo apt-get install rabbitmq-server

# Enable service
sudo systemctl enable rabbitmq-server
sudo systemctl start rabbitmq-server

# Enable management plugin
sudo rabbitmq-plugins enable rabbitmq_management
```

## macOS (Homebrew)

```bash
# Install
brew install rabbitmq

# Start as service
brew services start rabbitmq

# Or start manually
rabbitmq-server

# Enable management
rabbitmq-plugins enable rabbitmq_management
```

### Homebrew Paths

| Resource | Path |
|----------|------|
| Config | `$(brew --prefix)/etc/rabbitmq/` |
| Logs | `$(brew --prefix)/var/log/rabbitmq/` |
| Data | `$(brew --prefix)/var/lib/rabbitmq/` |
| Plugins | `$(brew --prefix)/lib/rabbitmq/plugins/` |

## Windows

```powershell
# Download installer from rabbitmq.com
# Run rabbitmq-server-x.x.x.exe

# Install as Windows service
rabbitmq-service.bat install

# Enable management
rabbitmq-plugins enable rabbitmq_management
```

## Cloud Services

### Amazon MQ

```
1. Console > Amazon MQ > Create broker
2. Choose RabbitMQ engine
3. Select instance type
4. Configure credentials
5. Create broker
```

### CloudAMQP

```
1. Sign up at cloudamqp.com
2. Create new instance
3. Select plan
4. Get connection URL
```

### Azure Service Bus (AMQP)

```
1. Azure Portal > Service Bus
2. Create namespace
3. Enable RabbitMQ protocol
4. Configure queues
```

## Cluster Setup

```bash
# Node 1
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl start_app

# Node 2
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster rabbit@node1
rabbitmqctl start_app

# Verify
rabbitmqctl cluster_status
```

## Verification

```bash
# Check status
rabbitmqctl status

# List queues
rabbitmqctl list_queues

# Management API
curl -u guest:guest http://localhost:15672/api/overview
```

## References

- [RabbitMQ Download](https://www.rabbitmq.com/download.html)
- [RabbitMQ Docker Image](https://hub.docker.com/_/rabbitmq)
- [Installation Guides](https://www.rabbitmq.com/install.html)

---
**Prerequisites:** None
**Related:** [RabbitMQ configuration](configuration.md) | [RabbitMQ production](production.md)
**Next:** [RabbitMQ configuration](configuration.md)
