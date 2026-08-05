# PostgreSQL Installation

## Homebrew (macOS)

### Installation

```bash
# Install PostgreSQL
brew install postgresql@16

# Start service
brew services start postgresql@16

# Initialize database
initdb /usr/local/var/postgres
```

### Management

```bash
# Start
brew services start postgresql@16

# Stop
brew services stop postgresql@16

# Restart
brew services restart postgresql@16
```

### Connecting

```bash
# Connect as default user
psql postgres

# Connect to specific database
psql -d mydb -U myuser -h localhost
```

## apt (Ubuntu/Debian)

### Installation

```bash
# Update package list
sudo apt update

# Install PostgreSQL
sudo apt install postgresql postgresql-contrib

# Start service
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Configuration

```bash
# Switch to postgres user
sudo -u postgres psql

# Create user
sudo -u postgres createuser --interactive

# Create database
sudo -u postgres createdb mydb
```

### Service Management

```bash
# Status
sudo systemctl status postgresql

# Stop
sudo systemctl stop postgresql

# Restart
sudo systemctl restart postgresql
```

## Docker

### Basic Usage

```bash
# Run PostgreSQL container
docker run --name postgres-db \
  -e POSTGRES_PASSWORD=secret \
  -e POSTGRES_DB=mydb \
  -p 5432:5432 \
  -d postgres:16

# Connect to container
docker exec -it postgres-db psql -U postgres
```

### Docker Compose

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: secret
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
volumes:
  postgres-data:
```

### Persistent Storage

```bash
# With volume mount
docker run --name postgres-db \
  -e POSTGRES_PASSWORD=secret \
  -v /path/to/data:/var/lib/postgresql/data \
  -p 5432:5432 \
  -d postgres:16
```

## Managed Services

### AWS RDS

```bash
# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier mydb \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --engine-version 16 \
  --master-username admin \
  --master-user-password secret
```

### Google Cloud SQL

```bash
# Create Cloud SQL instance
gcloud sql instances create mydb \
  --database-version=POSTGRES_16 \
  --tier=db-f1-micro \
  --region=us-central1
```

### Azure Database

```bash
# Create Azure PostgreSQL
az postgres server create \
  --name mydb \
  --resource-group mygroup \
  --sku-name B_Gen5_1 \
  --admin-user myadmin \
  --admin-password secret
```

## Post-Installation

### Create User and Database

```sql
-- Create user
CREATE USER myuser WITH PASSWORD 'secret';

-- Create database
CREATE DATABASE mydb OWNER myuser;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE mydb TO myuser;
```

### Verify Installation

```bash
# Check version
psql --version

# Test connection
psql -U postgres -c "SELECT version();"
```

## Troubleshooting

### Common Issues

1. Connection refused: Check if service is running
2. Authentication failed: Check pg_hba.conf
3. Port conflict: Change default port
4. Permission denied: Check file permissions

### Logs Location

```
Linux: /var/log/postgresql/
macOS: /usr/local/var/log/postgres.log
Docker: docker logs container-name
```

## Best Practices

1. Use strong passwords
2. Configure SSL for remote connections
3. Set up regular backups
4. Monitor disk space
5. Keep PostgreSQL updated
