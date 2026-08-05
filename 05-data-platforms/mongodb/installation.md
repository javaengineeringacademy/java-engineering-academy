# MongoDB Installation

## MongoDB Compass

### Download

1. Visit https://www.mongodb.com/products/compass
2. Download for your OS
3. Install and launch

### Connect

```javascript
// Connection string
mongodb://localhost:27017

// With authentication
mongodb://username:password@localhost:27017/dbname
```

## Docker

### Basic Usage

```bash
# Run MongoDB container
docker run --name mongodb \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  -d mongo:6.0

# Connect to container
docker exec -it mongodb mongosh
```

### Docker Compose

```yaml
version: '3.8'
services:
  mongodb:
    image: mongo:6.0
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: password
    ports:
      - "27017:27017"
    volumes:
      - mongo-data:/data/db
volumes:
  mongo-data:
```

### Persistent Storage

```bash
# With volume mount
docker run --name mongodb \
  -p 27017:27017 \
  -v /path/to/data:/data/db \
  -d mongo:6.0
```

## Package Manager

### Ubuntu/Debian

```bash
# Import public key
curl -fsSL https://www.mongodb.org/static/pgp/server-7.0.asc | \
   sudo gpg -o /usr/share/keyrings/mongodb-server-7.0.gpg

# Add repository
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | \
   sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

# Install
sudo apt-get update
sudo apt-get install -y mongodb-org

# Start service
sudo systemctl start mongod
sudo systemctl enable mongod
```

### macOS (Homebrew)

```bash
# Tap MongoDB
brew tap mongodb/brew

# Install MongoDB
brew install mongodb-community@7.0

# Start service
brew services start mongodb-community@7.0
```

### CentOS/RHEL

```bash
# Create repo
cat <<EOF | sudo tee /etc/yum.repos.d/mongodb-org-7.0.repo
[mongodb-org-7.0]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/8/mongodb-org/7.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://pgp.mongodb.com/server-7.0.asc
EOF

# Install
sudo yum install -y mongodb-org

# Start service
sudo systemctl start mongod
sudo systemctl enable mongod
```

## Managed Services

### MongoDB Atlas

1. Visit https://cloud.mongodb.com
2. Create account
3. Create cluster
4. Get connection string

### AWS DocumentDB

```bash
# Create cluster
aws docdb create-db-cluster \
  --db-cluster-identifier mycluster \
  --engine-version 5.0.0
```

### Azure Cosmos DB

```bash
# Create account
az cosmosdb create \
  --name myaccount \
  --resource-group mygroup \
  --kind MongoDB
```

## Post-Installation

### Create User

```javascript
// Connect to admin database
use admin

// Create admin user
db.createUser({
  user: "admin",
  pwd: "password",
  roles: ["root"]
})
```

### Enable Authentication

```yaml
# In mongod.conf
security:
  authorization: enabled
```

### Verify Installation

```bash
# Check version
mongod --version

# Test connection
mongosh
```

## Connection String

```javascript
// Standard
mongodb://localhost:27017

// With authentication
mongodb://username:password@localhost:27017/dbname

// Replica set
mongodb://host1:27017,host2:27017,host3:27017/?replicaSet=rs0

// Atlas
mongodb+srv://username:password@cluster.mongodb.net/dbname
```

## Troubleshooting

### Common Issues

1. Connection refused: Check if service is running
2. Authentication failed: Check credentials
3. Port conflict: Change default port
4. Permission denied: Check file permissions

### Logs Location

```
Linux: /var/log/mongodb/
macOS: /usr/local/var/log/mongodb/
Docker: docker logs container-name
```

## Best Practices

1. Use strong passwords
2. Enable authentication
3. Configure TLS for remote connections
4. Set up regular backups
5. Keep MongoDB updated
