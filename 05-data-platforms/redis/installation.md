# Redis Installation

## Docker (Recommended for Development)

### Quick Start

```bash
# Run latest Redis
docker run -d --name redis -p 6379:6379 redis:7

# With password
docker run -d --name redis -p 6379:6379 redis:7 --requirepass mypassword

# With persistence
docker run -d --name redis \
  -p 6379:6379 \
  -v /data/redis:/data \
  redis:7 redis-server --appendonly yes
```

### Docker Compose

```yaml
version: '3.8'
services:
  redis:
    image: redis:7
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    command: redis-server --appendonly yes --requirepass mypassword
    restart: unless-stopped

volumes:
  redis-data:
```

### Redis Stack (with modules)

```bash
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack-server:latest
```

## Binary Installation

### macOS

```bash
# Homebrew
brew install redis

# Start as service
brew services start redis

# Or run manually
redis-server /opt/homebrew/etc/redis.conf
```

### Ubuntu/Debian

```bash
# Install
sudo apt update
sudo apt install redis-server

# Start and enable
sudo systemctl start redis-server
sudo systemctl enable redis-server

# Verify
redis-cli ping
# Should return: PONG
```

### CentOS/RHEL

```bash
# Install EPEL repository
sudo yum install epel-release

# Install Redis
sudo yum install redis

# Start and enable
sudo systemctl start redis
sudo systemctl enable redis
```

### Build from Source

```bash
# Download and compile
wget https://download.redis.io/redis-stable.tar.gz
tar -xzf redis-stable.tar.gz
cd redis-stable
make

# Install binaries
sudo make install

# Create directories
sudo mkdir -p /etc/redis /var/lib/redis

# Copy config
sudo cp redis.conf /etc/redis/redis.conf
sudo sed -i 's/^supervised no/supervised systemd/' /etc/redis/redis.conf
sudo sed -i 's|^dir \.|dir /var/lib/redis|' /etc/redis/redis.conf

# Create systemd service
sudo systemctl enable redis-server
```

## Redis Cloud

### Redis Cloud (Managed Service)

1. Visit [redis.com](https://redis.com)
2. Create free account
3. Create a subscription (free tier: 30 MB)
4. Get connection details
5. Connect via `redis-cli` or client library

### AWS ElastiCache

```bash
# Via AWS CLI
aws elasticache create-cache-cluster \
  --cache-cluster-id my-redis \
  --engine redis \
  --cache-node-type cache.t3.micro \
  --num-cache-nodes 1
```

### Azure Cache for Redis

```bash
# Via Azure CLI
az redis create \
  --name myRedis \
  --resource-group myResourceGroup \
  --location eastus \
  --sku Basic \
  --vm-size C0
```

### Google Cloud Memorystore

```bash
# Via gcloud CLI
gcloud redis instances create my-redis \
  --size=1 \
  --region=us-central1 \
  --tier=basic
```

## Connection

### Using redis-cli

```bash
# Local connection
redis-cli

# Remote connection
redis-cli -h host -p port -a password

# With SSL/TLS
redis-cli --tls --cert /path/to/client.crt --key /path/to/client.key
```

### Test Connection

```bash
redis-cli PING
# Output: PONG

redis-cli SET test "hello"
redis-cli GET test
# Output: "hello"
```

## Verify Installation

```bash
# Check version
redis-cli INFO server | grep redis_version

# Check memory
redis-cli INFO memory | grep used_memory_human

# Check connected clients
redis-cli INFO clients | grep connected_clients
```
