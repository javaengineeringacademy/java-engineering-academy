# Amazon RDS (Relational Database Service)

## Overview

Amazon RDS is a managed relational database service supporting MySQL, PostgreSQL, MariaDB, Oracle, SQL Server, and Amazon Aurora.

## Engine Comparison

| Engine      | Performance | Availability | Cost     | Best For              |
|-------------|-------------|--------------|----------|-----------------------|
| Aurora      | 5x MySQL    | Multi-AZ     | Medium   | High performance      |
| PostgreSQL  | Standard    | Multi-AZ     | Low      | Open-source apps      |
| MySQL       | Standard    | Multi-AZ     | Low      | Legacy applications   |
| MariaDB     | Standard    | Multi-AZ     | Low      | MySQL compatibility   |
| Oracle      | Enterprise  | Multi-AZ     | High     | Enterprise apps       |
| SQL Server  | Standard    | Multi-AZ     | Medium   | .NET applications     |

## Instance Classes

### Burstable (T-series)
- **t3.micro**: 2 vCPU, 1 GiB RAM (free tier)
- **t3.medium**: 2 vCPU, 4 GiB RAM
- **Use case**: Dev/test, small workloads

### General Purpose (M-series)
- **m5.large**: 2 vCPU, 8 GiB RAM
- **m6i.2xlarge**: 8 vCPU, 32 GiB RAM
- **Use case**: Most workloads

### Memory Optimized (R-series)
- **r5.large**: 2 vCPU, 16 GiB RAM
- **r6i.4xlarge**: 16 vCPU, 128 GiB RAM
- **Use case**: In-memory databases, real-time analytics

### Storage Optimized (I-series)
- **db.r6gd**: 2 vCPU, 16 GiB RAM, NVMe SSD
- **Use case**: High I/O workloads

## Amazon Aurora

### Aurora Features
- **Storage**: Auto-scales up to 128 TB
- **Performance**: 5x MySQL, 3x PostgreSQL throughput
- **Replication**: 6 copies across 3 AZs
- **Backup**: Continuous to S3, 35-day retention
- **Failover**: Automatic, typically <30 seconds

### Aurora Serverless
```sql
-- v2: Auto-scales capacity
-- Min: 0.5 ACU, Max: 128 ACU
-- Ideal for variable/unpredictable workloads
```

### Aurora Global Database
```bash
# Create global database
aws rds create-global-cluster \
  --global-cluster-identifier my-global-db \
  --engine aurora-mysql \
  --engine-version 8.0.mysql_aurora.3.04.0
```

## Multi-AZ Deployments

### How Multi-AZ Works
```
Primary AZ                    Standby AZ
┌─────────────┐              ┌─────────────┐
│   Primary   │───Sync──────→│   Standby   │
│  Instance   │   Replication│  Instance   │
└──────┬──────┘              └──────┬──────┘
       │                            │
       └──────────┬─────────────────┘
                  │
            ┌─────┴─────┐
            │  Route 53  │
            │   DNS      │
            └───────────┘
```

### Failover Behavior
1. Primary fails health check
2. Route 53 detects failure
3. DNS endpoint promotes standby
4. Applications reconnect (automatic)
5. Typical failover: 60-120 seconds

### Enabling Multi-AZ
```bash
aws rds create-db-instance \
  --db-instance-identifier mydb \
  --multi-az \
  --db-instance-class db.r5.large \
  --engine aurora-mysql
```

## Read Replicas

### Read Replica Architecture
```
Primary DB ──→ Read Replica 1 (Same Region)
    │
    ├──→ Read Replica 2 (Same Region)
    │
    └──→ Read Replica 3 (Cross-Region)
```

### Creating Read Replicas
```bash
# Same-region replica
aws rds create-db-instance-read-replica \
  --db-instance-identifier my-replica \
  --source-db-instance-identifier mydb

# Cross-region replica
aws rds create-db-instance-read-replica \
  --db-instance-identifier my-replica \
  --source-db-instance-identifier mydb \
  --source-region us-east-1 \
  --region us-west-2
```

### Read Replica Limitations
- MySQL: Up to 15 read replicas
- PostgreSQL: Up to 10 read replicas
- Aurora: Up to 15 read replicas
- Cross-region: Up to 5 read replicas

## Storage

### General Purpose (gp2/gp3)
- **gp3**: Baseline 3,000 IOPS, 125 MB/s, scalable to 16,000 IOPS
- **gp2**: 3 IOPS per GB, burst to 16,000 IOPS
- **Use case**: Most workloads

### Provisioned IOPS (io1/io2)
- **io2**: Up to 64,000 IOPS, 1,000 MB/s per GB
- **io2 Block Express**: Up to 256,000 IOPS
- **Use case**: Mission-critical applications

### Magnetic (Standard)
- **Use case**: Legacy applications, infrequent access

## Backups

### Automated Backups
```bash
# Enable automated backups
aws rds modify-db-instance \
  --db-instance-identifier mydb \
  --backup-retention-period 35 \
  --preferred-backup-window "03:00-04:00"
```

### Manual Snapshots
```bash
# Create snapshot
aws rds create-db-snapshot \
  --db-instance-identifier mydb \
  --db-snapshot-identifier my-snapshot

# Restore from snapshot
aws rds restore-db-instance-from-db-snapshot \
  --db-instance-identifier mydb-restored \
  --db-snapshot-identifier my-snapshot
```

## Parameter Groups

```bash
# Create parameter group
aws rds create-db-cluster-parameter-group \
  --db-cluster-parameter-group-name my-params \
  --db-parameter-group-family aurora-mysql8.0 \
  --description "Custom parameters"

# Modify parameter
aws rds modify-db-cluster-parameter-group \
  --db-cluster-parameter-group-name my-params \
  --parameters '{
    "Name": "innodb_buffer_pool_size",
    "Value": "8589934592",
    "ApplyMethod": "pending-reboot"
  }'
```

## Security

### Encryption at Rest
```bash
# Enable encryption
aws rds create-db-instance \
  --db-instance-identifier mydb \
  --storage-encrypted \
  --kms-key-id arn:aws:kms:us-east-1:123456789012:key/my-key
```

### Encryption in Transit
```bash
# Force SSL connections
aws rds modify-db-instance \
  --db-instance-identifier mydb \
  --ca-certificate-identifier rds-ca-rsa2048-g1 \
  --force-ssl
```

### IAM Database Authentication
```bash
# Enable IAM auth
aws rds modify-db-instance \
  --db-instance-identifier mydb \
  --enable-iam-database-authentication
```

## Monitoring

```bash
# Get DB metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/RDS \
  --metric-name CPUUtilization \
  --dimensions Name=DBInstanceIdentifier,Value=mydb \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Average

# Enable Performance Insights
aws rds modify-db-instance \
  --db-instance-identifier mydb \
  --enable-performance-insights \
  --performance-insights-retention-period 7
```

## Cost Optimization

### Reserved Instances
| Term    | Discount | Payment |
|---------|----------|---------|
| 1 year  | Up to 40%| All/Partial/No |
| 3 year  | Up to 60%| All/Partial/No |

### Right-Sizing
```bash
# Get recommendations
aws ce get-rightsizing-recommendation \
  --service "Amazon RDS" \
  --configuration DaysSinceLaunch=30
```

### Storage Optimization
- Delete unused snapshots
- Use appropriate storage type
- Enable storage autoscaling with threshold

## Best Practices

1. **High Availability**: Use Multi-AZ for production
2. **Performance**: Use read replicas for read-heavy workloads
3. **Security**: Encrypt at rest and in transit, use IAM auth
4. **Monitoring**: Enable Performance Insights and Enhanced Monitoring
5. **Backup**: Test restores regularly
6. **Networking**: Use VPC security groups, not public access
7. **Maintenance**: Apply patches during maintenance window
8. **Scaling**: Use storage autoscaling, vertical scaling for demand
