# Google Cloud SQL

## Overview

Cloud SQL is a fully managed relational database service for MySQL, PostgreSQL, and SQL Server.

## Engine Support

| Engine      | Versions          | Use Case              |
|-------------|-------------------|-----------------------|
| MySQL       | 5.7, 8.0         | Web applications      |
| PostgreSQL  | 13, 14, 15       | Enterprise apps       |
| SQL Server  | 2019              | .NET applications     |

## Creating Instances

### gcloud CLI
```bash
# Create MySQL instance
gcloud sql instances create my-mysql \
  --database-version=MYSQL_8_0 \
  --tier=db-n1-standard-2 \
  --region=us-central1 \
  --root-password=my-password

# Create PostgreSQL instance
gcloud sql instances create my-postgres \
  --database-version=POSTGRES_15 \
  --tier=db-n1-standard-2 \
  --region=us-central1

# Create SQL Server instance
gcloud sql instances create my-sqlserver \
  --database-version=SQLSERVER_2019_STANDARD \
  --tier=db-custom-4-16384 \
  --region=us-central1
```

### Terraform
```hcl
resource "google_sql_database_instance" "default" {
  name             = "my-instance"
  database_version = "MYSQL_8_0"
  region           = "us-central1"

  settings {
    tier = "db-n1-standard-2"
    
    availability_type = "REGIONAL"
    
    backup_configuration {
      enabled          = true
      binary_log_enabled = true
    }
    
    ip_configuration {
      ipv4_enabled    = false
      private_network = "projects/my-project/global/networks/my-vpc"
    }
  }
}
```

## High Availability

### Regional HA
```bash
# Create HA instance
gcloud sql instances create my-ha-instance \
  --database-version=MYSQL_8_0 \
  --tier=db-n1-standard-2 \
  --region=us-central1 \
  --availability-type=REGIONAL
```

### Failover
```bash
# Failover to standby
gcloud sql instances failover my-ha-instance
```

## Read Replicas

```bash
# Create read replica
gcloud sql instances create my-replica \
  --master-instance-name=my-master \
  --region=us-central1 \
  --replica-type=READ

# Create cross-region replica
gcloud sql instances create my-cross-region-replica \
  --master-instance-name=my-master \
  --region=us-east1 \
  --replica-type=READ
```

## Backups

```bash
# Enable backups
gcloud sql instances patch my-instance \
  --backup-start-time=02:00

# Create backup
gcloud sql backups create \
  --instance=my-instance \
  --description="Manual backup"

# Restore from backup
gcloud sql backups restore BACKUP_ID \
  --instance=my-instance
```

## Database Operations

```bash
# Create database
gcloud sql databases create mydb --instance=my-instance

# Create user
gcloud sql users create myuser \
  --instance=my-instance \
  --password=mypassword
```

## Cloud SQL Proxy

```bash
# Download proxy
curl -o cloud-sql-proxy https://storage.googleapis.com/cloud-sql-connectors/cloud-sql-proxy/v2.8.0/cloud-sql-proxy.linux.amd64

# Start proxy
./cloud-sql-proxy my-project:us-central1:my-instance

# Connect to database
mysql -h 127.0.0.1 -u myuser -p mydb
```

## IAM Authentication

```bash
# Enable IAM auth
gcloud sql instances patch my-instance \
  --database-flags=cloudsql_iam_authentication=on

# Create IAM user
gcloud sql users create myuser \
  --instance=my-instance \
  --type=cloud_iam_service_account
```

## Encryption

```bash
# Create with CMEK
gcloud sql instances create my-instance \
  --database-version=MYSQL_8_0 \
  --tier=db-n1-standard-2 \
  --region=us-central1 \
  --kms-key-name=projects/my-project/locations/us-central1/keyRings/my-ring/cryptoKeys/my-key
```

## Connection Options

### Private IP
```bash
# Create instance with private IP
gcloud sql instances create my-private-instance \
  --database-version=MYSQL_8_0 \
  --tier=db-n1-standard-2 \
  --region=us-central1 \
  --network=my-vpc \
  --no-assign-ip
```

### Public IP
```bash
# Create instance with public IP
gcloud sql instances create my-public-instance \
  --database-version=MYSQL_8_0 \
  --tier=db-n1-standard-2 \
  --region=us-central1 \
  --assign-ip
```

## Monitoring

```bash
# Get instance metrics
gcloud monitoring metrics list \
  --filter='metric.type="cloudsql.googleapis.com/database/cpu/utilization"'
```

## Cost Optimization

- **Right-size instances** based on workload
- **Use HA** for production databases
- **Implement read replicas** for scaling
- **Use Cloud SQL Auth Proxy** for secure connections
- **Enable automatic backups**

## Best Practices

1. **Use HA** for production
2. **Implement read replicas** for scaling
3. **Enable backups** with point-in-time recovery
4. **Use Cloud SQL Proxy** for connections
5. **Implement encryption** at rest and in transit
6. **Use private IP** for security
7. **Monitor with Cloud Monitoring**
8. **Implement proper IAM** authentication
9. **Set up maintenance windows**
10. **Regular performance reviews**
